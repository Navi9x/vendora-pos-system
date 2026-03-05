import {Product} from './Product';

export interface SaleItem {
  id: number;
  quantity: number;
  productDTO: Product;
  unitPrice: number;
  totalPrice: number;
}
