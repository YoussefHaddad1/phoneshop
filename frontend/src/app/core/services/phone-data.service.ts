import { Injectable, computed, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { finalize } from 'rxjs/operators';
import { forkJoin, Observable } from 'rxjs';
import { Phone } from '../models/phone.model';
import { Brand } from '../models/brand.model';

@Injectable({ providedIn: 'root' })
export class PhoneDataService {
  private readonly phonesSignal = signal<Phone[]>([]);
  private readonly brandsSignal = signal<Brand[]>([]);
  private readonly loadingSignal = signal<boolean>(false);
  private readonly errorSignal = signal<string | null>(null);

  readonly phones = computed(() => this.phonesSignal());
  readonly brands = computed(() => this.brandsSignal());
  readonly isLoading = computed(() => this.loadingSignal());
  readonly error = computed(() => this.errorSignal());
  readonly featuredPhones = computed(() => this.phonesSignal().slice(0, 3));
  readonly remainingPhones = computed(() => this.phonesSignal().slice(3));

  constructor(private readonly http: HttpClient) {}

  load(force = false): void {
    if (this.loadingSignal() || (!force && this.phonesSignal().length)) {
      return;
    }

    this.loadingSignal.set(true);
    this.errorSignal.set(null);

    forkJoin({
      phones: this.http.get<Phone[]>('/api/phones'),
      brands: this.http.get<Brand[]>('/api/brands'),
    })
      .pipe(finalize(() => this.loadingSignal.set(false)))
      .subscribe({
        next: ({ phones, brands }) => {
          this.phonesSignal.set(phones ?? []);
          this.brandsSignal.set(brands ?? []);
        },
        error: (error: unknown) => {
          console.error('Failed to fetch phones data', error);
          this.errorSignal.set('We could not reach the product catalogue.');
        },
      });
  }

  createBrand(name: string): Observable<Brand> {
    return this.http.post<Brand>('/api/brands', { name });
  }

  createPhone(payload: {
    model: string;
    price: number;
    stock: number;
    specs: string;
    brandId: number;
  }): Observable<Phone> {
    const body = {
      model: payload.model,
      price: payload.price,
      stock: payload.stock,
      specs: payload.specs,
      brand: { id: payload.brandId },
    } as const;

    return this.http.post<Phone>('/api/phones', body);
  }
}

