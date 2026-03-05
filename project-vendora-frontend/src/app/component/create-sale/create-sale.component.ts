import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatSidenavModule, MatDrawer } from '@angular/material/sidenav';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatBadgeModule } from '@angular/material/badge';
import { MatDividerModule } from '@angular/material/divider';
import {UserAuthService} from '../../service/user-auth.service';
import {UserService} from '../../service/user.service';
import {Customer} from '../../model/Customer';
import {Category} from '../../model/Category';

interface Product {
  id: number;
  name: string;
  price: number;
  category: string;
  image: string;
  active : boolean;
}

interface CartItem extends Product {
  quantity: number;
}

@Component({
  selector: 'app-create-sale',
  imports: [
    MatButtonModule, MatIconModule, MatBadgeModule, MatFormFieldModule, MatInputModule, FormsModule, MatDrawer, MatSelectModule, CommonModule,
    MatSidenavModule,MatCardModule, MatChipsModule, MatDividerModule
  ],
  templateUrl: './create-sale.component.html',
  styleUrl: './create-sale.component.scss'
})
export class CreateSaleComponent implements OnInit {
  @ViewChild('cartDrawer') cartDrawer!: MatDrawer;

  constructor(private userAuthService: UserAuthService, private userService:UserService) {
  }

  business:any = null;
  user:any = null;
  invoiceNumber:string = '';
  totalSales:number = 0;

  // Cart state
  cartOpened = true;
  cartItems: any[] = [];

  //Customer Data
  customers:Customer[] = []

  //Category data
  categories:Category[] = []

  // Form data
  customerId:number | null=0;
  taxAmount = 0;
  discount = 0;
  deliveryCharge = 0;
  serviceCharge = 0;
  notes = '';
  paymentStatus = 'PENDING';

  // Search
  searchTerm = '';

  // Sample products data
  products: Product[] = [];
  filteredProducts: Product[] = [];


  ngOnInit(): void {
    this.loadProducts()
    this.loadCustomers()
    this.loadCategories()
    this.generateInvoiceNumber()
  }

  generateInvoiceNumber(){
    this.userService.countAllSales().subscribe(
      (res: any) => {
        this.totalSales = res;
        this.invoiceNumber = this.business.id+"INV"+(this.totalSales+1);
      }
    )
  }

  loadCategories(){
    this.userService.getAllCategories().subscribe({
      next: (categories: Category[]) => {this.categories = categories},
      error: error => console.log('Faild to load categories',error),
    })
  }

  loadCustomers(): void {
    this.userService.getAllCustomers().subscribe({
      next:(res) => {this.customers = res},
      error:(res)=>console.log('Failed to load customers',res)
    })
  }

  loadProducts(): void {
    this.user = this.userAuthService.getUser();
    console.log(this.user);
    this.business = this.userAuthService.getBusinesses()[0];
    this.userService.getAllProducts(this.business.id).subscribe(
      (res:any) => {
        console.log("B -"+this.business);
        console.log(res);
        this.products = res;
      }
    );
  }

  // Toggle cart sidebar
  toggleCartSidebar(): void {
    this.cartOpened = !this.cartOpened;
  }

  addToCart(product: Product): void {
    const existingItem = this.cartItems.find(item => item.id === product.id);

    if (existingItem) {
      // ✅ Reassign to trigger change detection
      this.cartItems = this.cartItems.map(item =>
        item.id === product.id ? { ...item, quantity: item.quantity + 1 } : item
      );
    } else {
      // ✅ Spread into new array instead of push()
      this.cartItems = [...this.cartItems, { ...product, quantity: 1 }];
    }

    if (!this.cartOpened) {
      this.cartOpened = true;
    }
  }

  // Increase item quantity
  increaseQuantity(item: CartItem): void {
    item.quantity++;
  }

  // Decrease item quantity
  decreaseQuantity(item: CartItem): void {
    if (item.quantity > 1) {
      item.quantity--;
    } else {
      this.removeFromCart(item);
    }
  }

  // Remove item from cart
  removeFromCart(item: CartItem): void {
    const index = this.cartItems.findIndex(i => i.id === item.id);
    if (index > -1) {
      this.cartItems.splice(index, 1);
    }
  }

  // Clear entire cart
  clearCart(): void {
    if (confirm('Are you sure you want to clear the cart?')) {
      this.cartItems = [];
      this.taxAmount = 0;
      this.discount = 0;
      this.deliveryCharge = 0;
      this.serviceCharge = 0;
      this.notes = '';
    }
  }

  // Calculate subtotal
  get subtotal(): number {
    return this.cartItems.reduce((sum, item) => sum + (item.price * item.quantity), 0);
  }

  // Calculate total
  calculateTotal(): number {
    return this.subtotal + this.taxAmount - this.discount + this.deliveryCharge + this.serviceCharge;
  }

  // Get cart item count
  get cartItemCount(): number {
    return this.cartItems.reduce((sum, item) => sum + item.quantity, 0);
  }


  search(searchText:string):void{
    console.log(searchText);
    if (searchText) {
      this.products = this.products.filter(item => item.name.startsWith(searchText));
    }else{
      this.loadProducts();
    }
  }

  // Complete sale
  completeSale(): void {
    if (this.cartItems.length === 0) {
      alert('Please add items to the cart');
      return;
    }


    // Prepare sale data
    const saleData = {
      businessId: this.business.id,
      userId: this.user.id,
      invoiceNumber: this.invoiceNumber,
      customerId: this.customerId,
      items: this.cartItems,
      subtotal: this.subtotal,
      taxAmount: this.taxAmount,
      discount: this.discount,
      deliveryCharge: this.deliveryCharge,
      serviceCharge: this.serviceCharge,
      total: this.calculateTotal(),
      notes: this.notes,
      paymentStatus: this.paymentStatus,
    };

    console.log('Sale Data:', saleData);

    this.userService.saveSales(saleData).subscribe(
      (res: any) => {
        console.log(res);
      }
    )

    alert('Sale completed successfully!');
    this.generateInvoiceNumber()

    // Clear cart and reset form
    this.cartItems = [];
    this.customerId = null;
    this.taxAmount = 0;
    this.discount = 0;
    this.deliveryCharge = 0;
    this.serviceCharge = 0;
    this.notes = '';
    this.paymentStatus = 'PENDING';
  }

  searchByCategory(category:string):void{
    if(category!="All"){
      this.filteredProducts = this.products.filter(item => item.category === category);
    }else{
      this.filteredProducts = [];
      this.loadProducts();
    }
  }
}
