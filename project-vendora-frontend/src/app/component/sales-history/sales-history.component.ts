import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';

import { UserService } from '../../service/user.service';
import { SaleResponse } from '../../model/SaleResponse';
import {CurrencyPipe, DatePipe, NgClass, NgForOf, NgIf} from '@angular/common';
import {
  MatDatepickerModule
} from '@angular/material/datepicker';
import {MatFormField, MatHint, MatInput, MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatOption} from '@angular/material/core';
import {MatSelect} from '@angular/material/select';
import {MatIcon} from '@angular/material/icon';
import {MatIconButton} from '@angular/material/button';
import {Cashier} from '../../model/Cashier';


@Component({
  selector: 'app-sales-history',
  standalone: true,
  imports: [RouterLink, FormsModule, CurrencyPipe, DatePipe, NgClass, NgForOf, NgIf, MatFormField,
    MatInput, MatFormFieldModule, MatDatepickerModule, MatInputModule, MatSelect, MatOption, MatIcon, MatIconButton],
  templateUrl: './sales-history.component.html',
  styleUrl: './sales-history.component.scss'
})
export class SalesHistoryComponent implements OnInit {

  // Table data
  sales: SaleResponse[] = [];
  cashiers: Cashier[] = [];

  // Summary cards
  totalRevenue: number = 0;
  averageSale: number = 0;
  totalItemsSold: number = 0;

  // Modal
  isModalOpen: boolean = false;
  selectedSale: SaleResponse | null = null;

  // Pagination (UI-only placeholder)
  pageSize: number = 10;
  pageStart: number = 0;
  pageEnd: number = 0;
  currentPage: number = 0;

  selectedDate: Date | null = null;
  cashierId: number | null = null;
  saleStatus: string | null = null;
  paymentStatus: string | null = null;

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.loadSalesHistory(this.currentPage,this.pageSize);
    this.loadCashiers();
  }

  loadSalesHistory(page: number, size: number): void {
    this.userService.loadSalesHistory(this.currentPage,this.pageSize,this.selectedDate,this.cashierId,this.saleStatus,this.paymentStatus).subscribe({
      next: (res: SaleResponse[]) => {
        this.sales = res ?? [];
        this.calculateSummary();
        this.updatePaginationInfo();
      },
      error: (err) => {
        console.error('Failed to load sales history:', err);
      }
    });
  }

  loadCashiers(): void {
    this.userService.getAllCashiers().subscribe({
      next: (res: Cashier[]) => {this.cashiers = res}
    })
  }

  protected searchApply(): void {
    console.log('Selected date:', this.selectedDate);
    console.log('Selected cashierId:', this.cashierId);
    this.loadSalesHistory(this.currentPage,this.pageSize);
  }

  // ---------- Summary calculations ----------
  private calculateSummary(): void {
    const count = this.sales.length;

    // Total revenue = sum of sale.total
    this.totalRevenue = this.sales.reduce((sum, s) => sum + (Number(s.total) || 0), 0);

    // Total items sold = sum of sale.totalQuantity
    this.totalItemsSold = this.sales.reduce(
      (sum, s) => sum + (Number(s.totalQuantity) || 0),
      0
    );

    // Average sale
    this.averageSale = count > 0 ? this.totalRevenue / count : 0;
  }

  // ---------- Modal ----------
  openSaleModal(s: SaleResponse): void {
    this.selectedSale = s;
    this.isModalOpen = true;
  }

  closeSaleModal(): void {
    this.isModalOpen = false;
    this.selectedSale = null;
  }

  // ---------- TrackBy ----------
  trackBySaleId(index: number, item: SaleResponse): number {
    return item.id;
  }

  // ---------- Actions (you can implement later) ----------
  exportSales(): void {
    console.log('Export clicked');
    // TODO: implement export (CSV/Excel)
  }

  printTable(): void {
    window.print();
  }

  printSale(s: SaleResponse): void {
    console.log('Print sale:', s.invoiceNumber);
    // TODO: implement receipt print
  }

  editSale(s: SaleResponse): void {
    console.log('Edit sale:', s.id);
    // TODO: navigate to edit route
  }

  // ---------- Pagination (UI-only for now) ----------
  onPageSizeChange(): void {
    this.updatePaginationInfo();
    this.loadSalesHistory(this.currentPage, this.pageSize);
  }

  goFirst(): void {
    this.updatePaginationInfo();
  }

  goPrev(): void {
    if(this.currentPage>0){
      this.currentPage--;
      this.loadSalesHistory(this.currentPage,this.pageSize);
    }
  }

  goNext(): void {
    this.currentPage++;
    this.loadSalesHistory(this.currentPage,this.pageSize);
  }

  goLast(): void {
    this.updatePaginationInfo();
  }

  private updatePaginationInfo(): void {
    const total = this.sales.length;

    if (total === 0) {
      this.pageStart = 0;
      this.pageEnd = 0;
      this.currentPage = 0;
      return;
    }

    this.pageStart = 1;
    this.pageEnd = Math.min(this.pageSize, total);
  }
}
