import {Component, OnInit} from '@angular/core';
import {UserService} from '../../service/user.service';
import {CategoryDTO, InventoryResponseDTO} from '../../model/inventory';
import {CurrencyPipe, DatePipe, NgForOf, NgIf, NgOptimizedImage} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {Category} from '../../model/Category';
import {MatDatepicker, MatDatepickerInput, MatDatepickerToggle} from '@angular/material/datepicker';
import {MatFormField, MatInput, MatLabel, MatSuffix} from '@angular/material/input';

@Component({
  selector: 'app-products',
  imports: [
    CurrencyPipe,
    FormsModule,
    DatePipe,
    NgOptimizedImage,
    NgIf,
    NgForOf,
    MatDatepicker,
    MatDatepickerInput,
    MatDatepickerToggle,
    MatInput,
    MatLabel,
    MatSuffix,
    MatFormField
  ],
  templateUrl: './products.component.html',
  styleUrl: './products.component.scss'
})
export class ProductsComponent implements OnInit {
  inventoryItems: InventoryResponseDTO[] = [];
  categories: Category[] = [];

  // Stats
  totalProducts: number = 0;
  lowStockCount: number = 0;
  outOfStockCount: number = 0;
  inventoryValue: number = 0;

  // Filters
  filters = {
    search: '',
    categoryId: null as number | null,
    stockStatus: null as string | null,
    isActive: null as boolean | null,
    unitType: null as string | null
  };

  // View mode
  viewMode: 'table' | 'grid' = 'table';

  // Pagination
  currentPage: number = 1;
  pageSize: number = 10;
  totalRecords: number = 0;
  totalPages: number = 0;
  visiblePages: number[] = [];

  // Modals
  showDetailModal: boolean = false;
  showRestockModal: boolean = false;
  selectedItem: InventoryResponseDTO | null = null;
  selectedDate: Date | null = null;

  // Restock form
  restockQuantity: number = 0;
  restockNotes: string = '';

  constructor(public userService: UserService) {
  }

  ngOnInit(): void {
    this.loadInventory();
    this.loadCategories();
  }

  public loadInventory(){
    this.userService.loadInventory().subscribe(inventory => {
      this.inventoryItems = inventory;
      this.totalRecords = inventory.length;
      this.calculateStats(inventory);
      this.totalPages = Math.ceil(this.totalRecords / this.pageSize);
      this.updateVisiblePages();
    });
  }


  loadCategories(): void {
    this.userService.getAllCategories().subscribe(categories => {
      this.categories = categories;
      console.log(categories);
    })
  }

  calculateStats(inventory:InventoryResponseDTO[]): void {
    this.totalProducts = inventory.length;
    this.inventoryValue = 0;
    this.lowStockCount = 0;
    this.outOfStockCount = 0;

    inventory.forEach(item => {
      const productCostPrice:number = item.product.costPrice??0;
      this.inventoryValue+=(item.quantity*productCostPrice);

      const minStock = item.minStockLevel ?? 0;
      if(item.quantity<=minStock){
        this.lowStockCount++;
      }

      if(item.quantity===0){
        this.outOfStockCount++;
      }
    })
  }

  // Filter methods
  applyFilters(): void {
    this.currentPage = 1;
    this.loadInventory();
  }

  clearFilters(): void {
    this.filters = {
      search: '',
      categoryId: null,
      stockStatus: null,
      isActive: null,
      unitType: null
    };
    this.applyFilters();
  }

  // View mode
  setViewMode(mode: 'table' | 'grid'): void {
    this.viewMode = mode;
  }

  // Stock status helpers
  isLowStock(item: InventoryResponseDTO): boolean {
    if (!item.minStockLevel) return false;
    return item.quantity > 0 && item.quantity <= item.minStockLevel;
  }

  isOutOfStock(item: InventoryResponseDTO): boolean {
    return item.quantity === 0;
  }

  getStockStatus(item: InventoryResponseDTO): string {
    if (this.isOutOfStock(item)) return 'Out of Stock';
    if (this.isLowStock(item)) return 'Low Stock';
    return 'In Stock';
  }

  getStockPercentage(item: InventoryResponseDTO): number {
    if (!item.maxStockLevel) return 100;
    return Math.min((item.quantity / item.maxStockLevel) * 100, 100);
  }

  getProfitMargin(item: InventoryResponseDTO): string {
    const price = item.product.price;
    const cost = item.product.costPrice || 0;
    const profit = price - cost;
    const margin = cost > 0 ? ((profit / cost) * 100) : 0;
    return `${profit.toFixed(2)} (${margin.toFixed(1)}%)`;
  }

  // Pagination
  get pageStart(): number {
    return (this.currentPage - 1) * this.pageSize + 1;
  }

  get pageEnd(): number {
    return Math.min(this.currentPage * this.pageSize, this.totalRecords);
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages) return;
    this.currentPage = page;
    this.loadInventory();
    this.updateVisiblePages();
  }

  previousPage(): void {
    if (this.currentPage > 1) {
      this.goToPage(this.currentPage - 1);
    }
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages) {
      this.goToPage(this.currentPage + 1);
    }
  }

  onPageSizeChange(): void {
    this.currentPage = 1;
    this.totalPages = Math.ceil(this.totalRecords / this.pageSize);
    this.loadInventory();
    this.updateVisiblePages();
  }

  updateVisiblePages(): void {
    const maxVisible = 5;
    const pages: number[] = [];

    let start = Math.max(1, this.currentPage - Math.floor(maxVisible / 2));
    let end = Math.min(this.totalPages, start + maxVisible - 1);

    if (end - start < maxVisible - 1) {
      start = Math.max(1, end - maxVisible + 1);
    }

    for (let i = start; i <= end; i++) {
      pages.push(i);
    }

    this.visiblePages = pages;
  }

  // Modal methods
  viewDetails(item: InventoryResponseDTO): void {
    this.selectedItem = item;
    this.showDetailModal = true;
  }

  closeDetailModal(): void {
    this.showDetailModal = false;
    this.selectedItem = null;
  }

  openRestockModal(item: InventoryResponseDTO): void {
    this.selectedItem = item;
    this.restockQuantity = 0;
    this.restockNotes = '';
    this.showRestockModal = true;
  }

  closeRestockModal(): void {
    this.showRestockModal = false;
    this.selectedItem = null;
    this.restockQuantity = 0;
    this.restockNotes = '';
  }

  confirmRestock(): void {
    if (!this.selectedItem || this.restockQuantity <= 0) {
      // Show validation error
      return;
    }

    // TODO: Call API to restock
    // this.inventoryService.restock(
    //   this.selectedItem.id,
    //   this.restockQuantity,
    //   this.restockNotes
    // ).subscribe(() => {
    //   this.loadInventory();
    //   this.closeRestockModal();
    //   // Show success message
    // });

    console.log('Restock:', {
      item: this.selectedItem,
      quantity: this.restockQuantity,
      notes: this.restockNotes
    });

    this.closeRestockModal();
  }

  // Action methods
  editProduct(item: InventoryResponseDTO): void {
    // TODO: Navigate to edit page or open edit modal
    console.log('Edit product:', item);
  }

  onAddProduct(): void {
    // TODO: Navigate to add product page or open add modal
    console.log('Add new product');
  }

  onImport(): void {
    // TODO: Open import dialog
    console.log('Import inventory');
  }

  exportData(): void {
    // TODO: Export inventory to CSV/Excel
    console.log('Export inventory');
  }

  printInventory(): void {
    // TODO: Print inventory report
    window.print();
  }

}
