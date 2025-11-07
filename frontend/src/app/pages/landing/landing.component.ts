import { ChangeDetectionStrategy, Component, OnInit, computed, signal } from '@angular/core';
import { CommonModule, CurrencyPipe, DecimalPipe, NgClass } from '@angular/common';
import { NgOptimizedImage } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PhoneDataService } from '../../core/services/phone-data.service';
import { Phone } from '../../core/models/phone.model';
import { Brand } from '../../core/models/brand.model';

type Metric = {
  label: string;
  value: number;
  suffix?: string;
  kind?: 'currency' | 'percentage' | 'number';
};

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [CommonModule, NgClass, NgOptimizedImage, CurrencyPipe, DecimalPipe, FormsModule],
  templateUrl: './landing.component.html',
  styleUrl: './landing.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class LandingComponent implements OnInit {
  get isLoading() { return this.phoneDataService.isLoading; }
  get error() { return this.phoneDataService.error; }
  get phones() { return this.phoneDataService.phones; }
  get featuredPhones() { return this.phoneDataService.featuredPhones; }
  get remainingPhones() { return this.phoneDataService.remainingPhones; }
  get brands() { return this.phoneDataService.brands; }

  readonly heroPhone = computed(() => this.featuredPhones()[0] ?? null);
  readonly heroSpecs = computed(() => this.extractSpecs(this.heroPhone()));

  readonly marqueeBrands = computed(() => {
    const names = this.brands().map((brand) => brand.name);
    if (!names.length) {
      return ['Aurora', 'Pulse', 'Nova', 'Vertex', 'Nimbus', 'Flux'];
    }

    const rotations = Math.ceil(18 / names.length);
    return Array.from({ length: rotations }, () => names)
      .flat()
      .slice(0, Math.max(names.length * 2, 18));
  });

  readonly metrics = computed<Metric[]>(() => {
    const phones = this.phones();
    const brands = this.brands();

    if (!phones.length) {
      return [];
    }

    const totalStock = phones.reduce((acc, phone) => acc + (phone.stock ?? 0), 0);
    const inventoryValue = phones.reduce((acc, phone) => acc + phone.price * (phone.stock ?? 0), 0);
    const averagePrice = phones.reduce((acc, phone) => acc + phone.price, 0) / phones.length;
    const bestseller = phones.reduce((top, phone) => (phone.stock > (top?.stock ?? 0) ? phone : top), phones[0]);

    return [
      { label: 'Devices curated', value: phones.length, kind: 'number' },
      { label: 'Partner brands', value: brands.length || 12, kind: 'number' },
      { label: 'Avg. launch price', value: averagePrice, kind: 'currency' },
      { label: 'In live stock', value: totalStock, suffix: ' units', kind: 'number' },
      { label: 'Hero inventory value', value: inventoryValue, kind: 'currency' },
      { label: 'Top stock model', value: bestseller?.stock ?? 0, suffix: ` × ${bestseller?.model ?? ''}`, kind: 'number' },
    ];
  });

  readonly curatedHighlights = computed(() => this.composeHighlights(this.phones(), this.brands()));

  // Interactions state
  readonly searchQuery = signal<string>('');
  readonly selectedBrand = signal<string | null>(null);
  readonly sortKey = signal<'relevance' | 'priceAsc' | 'priceDesc' | 'nameAsc'>('relevance');
  readonly toastMessage = signal<string | null>(null);
  readonly selectedPhone = signal<Phone | null>(null);
  readonly isCartOpen = signal<boolean>(false);
  readonly cartItems = signal<{ phone: Phone; quantity: number }[]>([]);
  readonly pageSize = signal<number>(6);
  readonly pageIndex = signal<number>(0);

  readonly filteredPhones = computed<Phone[]>(() => {
    const q = this.searchQuery().trim().toLowerCase();
    const brand = this.selectedBrand();
    const items = this.phones();

    let result = items.filter((p) => {
      const specs = (p.specs ?? '').toLowerCase();
      const matchesQuery = !q || p.model.toLowerCase().includes(q) || specs.includes(q);
      const matchesBrand = !brand || p.brand?.name === brand;
      return matchesQuery && matchesBrand;
    });

    switch (this.sortKey()) {
      case 'priceAsc':
        result = [...result].sort((a, b) => this.toNumber(a.price) - this.toNumber(b.price));
        break;
      case 'priceDesc':
        result = [...result].sort((a, b) => this.toNumber(b.price) - this.toNumber(a.price));
        break;
      case 'nameAsc':
        result = [...result].sort((a, b) => a.model.localeCompare(b.model));
        break;
      case 'relevance':
      default:
        break;
    }
    return result;
  });

  readonly filteredFeatured = computed(() => this.filteredPhones().slice(0, 3));
  readonly remainingPool = computed(() => this.filteredPhones().slice(3));
  readonly pageCount = computed(() => {
    const total = this.remainingPool().length;
    const size = this.pageSize();
    if (!total || !size) {
      return 0;
    }
    return Math.ceil(total / size);
  });
  readonly pageNumbers = computed(() => Array.from({ length: this.pageCount() }, (_v, i) => i));
  readonly paginatedRemaining = computed(() => {
    const pool = this.remainingPool();
    if (!pool.length) {
      return [];
    }
    const size = this.pageSize();
    const maxIndex = Math.max(this.pageCount() - 1, 0);
    const index = Math.min(this.pageIndex(), maxIndex);
    if (index !== this.pageIndex()) {
      this.pageIndex.set(index);
    }
    const start = index * size;
    return pool.slice(start, start + size);
  });
  readonly cartCount = computed(() => this.cartItems().reduce((acc, item) => acc + item.quantity, 0));
  readonly cartTotal = computed(() =>
    this.cartItems().reduce((acc, item) => acc + this.toNumber(item.phone.price) * item.quantity, 0)
  );

  readonly realBrandOptions: readonly string[] = [
    'Apple',
    'Samsung',
    'Google',
    'OnePlus',
    'Xiaomi',
    'Huawei',
    'Sony',
    'Motorola',
    'Nokia',
    'Oppo',
    'Honor',
    'Asus',
    'Nothing',
    'Realme',
    'Vivo',
  ];

  readonly availableBrandOptions = computed(() =>
    this.realBrandOptions.filter(
      (name) => !this.brands().some((brand) => brand.name.toLowerCase() === name.toLowerCase())
    )
  );

  brandForm: { name: string } = { name: '' };
  readonly brandError = signal<string | null>(null);
  readonly brandSubmitting = signal<boolean>(false);

  phoneForm: { brandId: string; model: string; price: string; stock: string; specs: string } = {
    brandId: '',
    model: '',
    price: '',
    stock: '',
    specs: '',
  };
  readonly phoneError = signal<string | null>(null);
  readonly phoneSubmitting = signal<boolean>(false);
  readonly phoneModelPattern = '^[A-Za-z0-9][A-Za-z0-9\\s\\-+]{2,}$';

  constructor(private readonly phoneDataService: PhoneDataService) {}

  ngOnInit(): void {
    this.phoneDataService.load();
  }

  trackPhoneById(_index: number, phone: Phone): number {
    return phone.id;
  }

  trackBrandByName(_index: number, brand: string): string {
    return brand;
  }

  private extractSpecs(phone: Phone | null): string[] {
    if (!phone?.specs) {
      return [];
    }

    return phone.specs
      .split(/[\n\r,;|]/)
      .map((spec) => spec.trim())
      .filter(Boolean)
      .slice(0, 5);
  }

  private composeHighlights(phones: Phone[], brands: Brand[]): { title: string; caption: string }[] {
    if (!phones.length) {
      return [
        { title: 'Flagship drops weekly', caption: 'A steady cadence of next-gen releases sourced from premium manufacturers.' },
        { title: 'Glassmorphic showroom', caption: 'Layered UI inspired by boutique showrooms keeps focus on the devices.' },
        { title: 'Realtime stock pulse', caption: 'Dynamic inventory counters ensure your team always sees live availability.' },
      ];
    }

    const highStock = phones.filter((phone) => phone.stock > 0).length;
    const premiumPhones = phones.filter((phone) => phone.price > 999).length;
    const distinctBrands = brands.length || new Set(phones.map((phone) => phone.brand?.name)).size;

    return [
      {
        title: `${premiumPhones}+ ultra-premium selections`,
        caption: 'Curated for flagship experiences, meticulously filtered for spec-heavy audiences.',
      },
      {
        title: `${distinctBrands} brand alliances`,
        caption: 'From heritage icons to rising disruptors, united in one hyper-modern showcase.',
      },
      {
        title: `${highStock} models ready to ship`,
        caption: 'Realtime inventory sync keeps the hype moment-to-moment and your sales crew prepared.',
      },
    ];
  }

  reloadData(): void {
    this.phoneDataService.load(true);
  }

  scrollToCatalogue(): void {
    const el = document.getElementById('catalogue');
    if (el) {
      el.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }
  }

  onSearch(input: string): void {
    this.searchQuery.set(input);
    this.pageIndex.set(0);
  }

  selectBrand(name: string | null): void {
    this.selectedBrand.set(name);
    this.pageIndex.set(0);
  }

  setSort(key: 'relevance' | 'priceAsc' | 'priceDesc' | 'nameAsc'): void {
    this.sortKey.set(key);
    this.pageIndex.set(0);
  }

  openDetails(phone: Phone): void {
    this.selectedPhone.set(phone);
  }

  closeDetails(): void {
    this.selectedPhone.set(null);
  }

  addToCart(phone: Phone): void {
    this.cartItems.update((items) => {
      const existing = items.find((item) => item.phone.id === phone.id);
      if (existing) {
        return items.map((item) =>
          item.phone.id === phone.id ? { ...item, quantity: item.quantity + 1 } : item
        );
      }
      return [...items, { phone, quantity: 1 }];
    });
    this.toastMessage.set(`${phone.brand?.name ?? ''} ${phone.model} added to cart`.trim());
    this.isCartOpen.set(true);
    window.setTimeout(() => this.toastMessage.set(null), 2000);
  }

  incrementCartItem(phone: Phone): void {
    this.cartItems.update((items) =>
      items.map((item) =>
        item.phone.id === phone.id ? { ...item, quantity: item.quantity + 1 } : item
      )
    );
  }

  decrementCartItem(phone: Phone): void {
    this.cartItems.update((items) =>
      items
        .map((item) =>
          item.phone.id === phone.id ? { ...item, quantity: item.quantity - 1 } : item
        )
        .filter((item) => item.quantity > 0)
    );
  }

  removeFromCart(phone: Phone): void {
    this.cartItems.update((items) => items.filter((item) => item.phone.id !== phone.id));
  }

  clearCart(): void {
    this.cartItems.set([]);
  }

  toggleCart(): void {
    this.isCartOpen.set(!this.isCartOpen());
  }

  closeCart(): void {
    this.isCartOpen.set(false);
  }

  setPage(index: number): void {
    const clamped = Math.min(Math.max(index, 0), Math.max(this.pageCount() - 1, 0));
    this.pageIndex.set(clamped);
  }

  nextPage(): void {
    this.setPage(this.pageIndex() + 1);
  }

  prevPage(): void {
    this.setPage(this.pageIndex() - 1);
  }

  private toNumber(value: unknown): number {
    const num = typeof value === 'number' ? value : Number(value ?? 0);
    return Number.isFinite(num) ? num : 0;
  }

  specList(specs: string | null | undefined): string[] {
    if (!specs) {
      return [];
    }
    return specs
      .split(/[\n\r,;|]+/)
      .map((part) => part.trim())
      .filter(Boolean);
  }

  submitBrand(): void {
    const name = this.brandForm.name.trim();

    if (!name) {
      this.brandError.set('Select a brand from the list.');
      return;
    }

    if (!this.realBrandOptions.includes(name)) {
      this.brandError.set('Only recognised global brands are permitted.');
      return;
    }

    if (this.brands().some((brand) => brand.name.toLowerCase() === name.toLowerCase())) {
      this.brandError.set(`${name} is already in the catalogue.`);
      return;
    }

    this.brandSubmitting.set(true);
    this.brandError.set(null);

    this.phoneDataService.createBrand(name).subscribe({
      next: () => {
        this.brandForm = { name: '' };
        this.toastMessage.set(`${name} brand added.`);
        this.phoneDataService.load(true);
        window.setTimeout(() => this.toastMessage.set(null), 2000);
        this.brandSubmitting.set(false);
      },
      error: (err) => {
        const message = err?.error?.message ?? err?.message ?? 'Could not add brand. Ensure it is unique.';
        console.error('Brand creation failed', err);
        this.brandError.set(message);
        this.brandSubmitting.set(false);
      },
    });
  }

  submitPhone(): void {
    const brandId = Number(this.phoneForm.brandId);
    const model = this.phoneForm.model.trim();
    const price = Number(this.phoneForm.price);
    const stock = Number(this.phoneForm.stock);
    const specs = this.phoneForm.specs.trim();

    if (!brandId || !this.brands().some((brand) => brand.id === brandId)) {
      this.phoneError.set('Pick an existing brand before adding a device.');
      return;
    }

    if (!model || !new RegExp(this.phoneModelPattern).test(model)) {
      this.phoneError.set('Provide a valid model name (letters, numbers, + or -).');
      return;
    }

    if (!Number.isFinite(price) || price <= 0) {
      this.phoneError.set('Price must be a positive number.');
      return;
    }

    if (!Number.isInteger(stock) || stock < 0) {
      this.phoneError.set('Stock must be zero or a positive whole number.');
      return;
    }

    this.phoneSubmitting.set(true);
    this.phoneError.set(null);

    this.phoneDataService
      .createPhone({
        brandId,
        model,
        price,
        stock,
        specs,
      })
      .subscribe({
        next: () => {
          this.resetPhoneForm();
          this.toastMessage.set(`${model} added to catalogue.`);
          this.phoneDataService.load(true);
          this.pageIndex.set(0);
          window.setTimeout(() => this.toastMessage.set(null), 2000);
          this.phoneSubmitting.set(false);
        },
        error: (err) => {
          const message = err?.error?.message ?? err?.error ?? err?.message ?? 'Could not create phone. Please verify the fields.';
          console.error('Phone creation failed', err);
          this.phoneError.set(message);
          this.phoneSubmitting.set(false);
        },
      });
  }

  private resetPhoneForm(): void {
    this.phoneForm = { brandId: '', model: '', price: '', stock: '', specs: '' };
  }

  isBrandPresent(option: string): boolean {
    return this.brands().some((brand) => brand.name.toLowerCase() === option.toLowerCase());
  }
}
