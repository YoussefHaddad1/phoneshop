import { Brand } from './brand.model';

export interface Phone {
  id: number;
  model: string;
  price: number;
  specs: string;
  stock: number;
  brand: Brand;
}

