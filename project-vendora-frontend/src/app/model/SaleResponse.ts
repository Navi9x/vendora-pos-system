import {User} from './User';
import {Customer} from './Customer';
import {Business} from './Business';
import {SaleItem} from './SaleItem';

export interface SaleResponse {
  id: number;
  createdAt: string;
  updatedAt: string;
  isDeleted: boolean;

  invoiceNumber: string;

  subtotal: number;
  taxAmount: number | null;
  deliveryCharge: number | null;
  discount: number | null;
  total: number;

  status: string;
  paymentStatus: string;
  notes: string | null;

  cashier: User;
  customer?: Customer;
  business: Business;

  items: SaleItem[];

  totalItems: number;
  totalQuantity: number;
}
