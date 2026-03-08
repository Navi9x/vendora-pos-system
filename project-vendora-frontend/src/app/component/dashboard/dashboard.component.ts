import { Component, OnInit } from '@angular/core';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MenuService } from '../../service/menu.service';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { UserService } from '../../service/user.service';
import { SaleResponse } from '../../model/SaleResponse';
import { InventoryResponseDTO } from '../../model/inventory';
import { SalesChartItem } from '../../model/SalesChartItem';

interface TopProduct {
  id: number;
  name: string;
  image: string;
  soldQuantity: number;
  revenue: number;
}

interface CategoryStat {
  name: string;
  amount: number;
  percentage: number;
}

interface PaymentMethod {
  name: string;
  transactions: number;
  amount: number;
  color: string;
}

interface StockAlert {
  productId: number;
  productName: string;
  quantity: number;
  minStock: number;
}

interface Transaction {
  id: number;
  invoiceNumber: string;
  customerName: string | null;
  items: number;
  time: string;
  amount: number;
  status: string;
}

@Component({
  selector: 'app-dashboard',
  imports: [
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    CurrencyPipe,
    FormsModule,
    CommonModule,
    RouterModule,
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent implements OnInit {
  // Chart data
  salesChartType: 'revenue' | 'transactions' = 'revenue';
  salesChartData: SalesChartItem[] = [];
  chartLinePoints = '';
  chartAreaPoints = '';
  chartDots: { x: number; y: number }[] = [];

  // Top products
  topProducts: TopProduct[] = [];

  // Category statistics
  categoryStats: CategoryStat[] = [];

  // Payment methods
  paymentMethods: PaymentMethod[] = [
    {
      name: 'Cash',
      transactions: 89,
      amount: 26780.5,
      color: 'linear-gradient(135deg, #10b981 0%, #059669 100%)',
    },
    {
      name: 'Card',
      transactions: 52,
      amount: 15234.9,
      color: 'linear-gradient(135deg, #3b82f6 0%, #2563eb 100%)',
    },
    {
      name: 'Digital Wallet',
      transactions: 15,
      amount: 3663.5,
      color: 'linear-gradient(135deg, #8b5cf6 0%, #7c3aed 100%)',
    },
  ];

  // Stock alerts
  stockAlerts: StockAlert[] = [];

  // Recent transactions
  recentTransactions: Transaction[] = [];

  selectedPeriod: string = 'this_month';

  // Main stats
  totalSales: number = 0;
  netRevenue: number = 0;
  productsSold: number = 0;
  avgTransaction: number = 0;
  totalTransactions: number = 0;
  sales: SaleResponse[] = [];
  inventoryItems: InventoryResponseDTO[] = [];

  ngOnInit(): void {
    this.loadDashboardData();
    this.loadDashboardData();
  }

  constructor(
    private menuService: MenuService,
    private userService: UserService,
  ) {}

  loadSales(): void {
    this.userService.loadAllSalesHistory(this.selectedPeriod).subscribe({
      next: (res: SaleResponse[]) => {
        this.sales = res ?? [];
        this.calculateSummary();

        // id: number;
        // invoiceNumber: string;
        // customerName: string | null;
        // items: number;
        // time: string;
        // amount: number;
        // status: string;

        //Recenlty transaction
        this.stockAlerts = this.inventoryItems
          .filter((item) => item.quantity === 0 || ( item.minStockLevel !=null ? item.minStockLevel > item.quantity:true))
          .map((item) => ({
            productId: item.product.id,
            productName: item.product.name,
            quantity: item.quantity,
            minStock: item.quantity,
          }))
          .slice(0,4);
          
        this.recentTransactions = this.sales
          .map((sale) => ({
            id:sale.id,
            invoiceNumber: sale.invoiceNumber,
            customerName:sale.customer !=null ? sale.customer.name : 'Walk in customer',
            items:sale.items.length,
            time:sale.createdAt,
            amount:sale.subtotal,
            status:sale.status
          }))
          .slice(0, 5);

        this.sales.forEach((sale) => {
          sale.items.forEach((item) => {
            console.log(item.productDTO.category);

            //Sort top categories
            const existingCategory = this.categoryStats.find(
              (c) => c.name === item.productDTO.category,
            );

            if (existingCategory) {
              existingCategory.amount += item.totalPrice;
            } else {
              this.categoryStats.push({
                name: item.productDTO.category,
                amount: item.totalPrice,
                percentage: 0,
              });
            }

            let categoryTotal: number = 0;

            this.categoryStats.forEach((ct) => {
              categoryTotal += ct.amount;
            });

            this.categoryStats.forEach((ct2) => {
              ct2.percentage =
                Math.round((ct2.amount / categoryTotal) * 100 * 10) / 10;
            });

            //Sort top products
            const existingProduct = this.topProducts.find(
              (p) => Number(p.id) === Number(item.productDTO.id),
            );
            if (existingProduct) {
              existingProduct.soldQuantity += item.quantity;
              existingProduct.revenue += item.totalPrice;
            } else {
              this.topProducts.push({
                id: item.productDTO.id,
                name: item.productDTO.name,
                image: item.productDTO.image,
                soldQuantity: item.quantity,
                revenue: item.totalPrice,
              });
            }
            this.topProducts = this.topProducts
              .sort((a, b) => b.soldQuantity - a.soldQuantity)
              .slice(0, 5);
          });
        });
      },
      error: (err) => {
        console.error('Failed to load sales history:', err);
      },
    });
  }

  //   productId: number;
  // productName: string;
  // quantity: number;
  // minStock: number;

  loadInventory(): void {
    this.userService.loadAllInventory().subscribe({
      next: (res: InventoryResponseDTO[]) => {
        this.inventoryItems = res ?? [];
        //Stock alert

        const outOfStock = this.inventoryItems.filter(
          (item) => item.quantity === 0,
        );

        const lowStock = this.inventoryItems.filter(
          (item) =>
            item.quantity > 0 &&
            item.minStockLevel != null &&
            item.quantity < item.minStockLevel,
        );

        const combined = [...outOfStock, ...lowStock];

        this.stockAlerts = combined.slice(0, 4).map((item) => ({
          productId: item.product.id,
          productName: item.product.name,
          quantity: item.quantity,
          minStock: item.minStockLevel != null ? item.minStockLevel : 0,
        }));
      },
      error: (err) => {
        console.error('Failed to load sales history:', err);
      },
    });
  }

  loadSalesChart(): void {
    this.userService.loadSalesChart(this.selectedPeriod).subscribe({
      next: (res: SalesChartItem[]) => {
        this.salesChartData = res ?? [];
        this.buildChart();
      },
      error: (err) => {
        console.error('Failed to load sales chart:', err);
      },
    });
  }

  buildChart(): void {
    if (!this.salesChartData.length) {
      this.chartLinePoints = '';
      this.chartAreaPoints = '';
      this.chartDots = [];
      return;
    }

    const width = 800;
    const height = 240;
    const paddingTop = 20;
    const pointCount = this.salesChartData.length;

    const values = this.salesChartData.map((item) =>
      this.salesChartType === 'revenue' ? item.revenue : item.transactions,
    );

    const maxValue = Math.max(...values, 1);
    const stepX = pointCount > 1 ? width / (pointCount - 1) : width;

    this.chartDots = values.map((value, index) => {
      const x = index * stepX;
      const y = height - (value / maxValue) * (height - paddingTop);
      return { x, y };
    });

    this.chartLinePoints = this.chartDots.map((p) => `${p.x},${p.y}`).join(' ');

    this.chartAreaPoints =
      `0,${height} ` +
      this.chartDots.map((p) => `${p.x},${p.y}`).join(' ') +
      ` ${width},${height}`;
  }

  setChartType(type: 'revenue' | 'transactions'): void {
    this.salesChartType = type;
    this.buildChart();
  }

  calculateSummary(): void {
    this.totalSales += this.sales.length;
    this.netRevenue = this.sales.reduce(
      (sum, s) => sum + (Number(s.total) || 0),
      0,
    );
    this.productsSold = this.inventoryItems.reduce(
      (sum, i) => sum + (i.quantity || 0),
      0,
    );
    this.totalTransactions = this.sales.length;
    this.avgTransaction =
      this.totalTransactions > 0
        ? Number((this.netRevenue / this.totalTransactions).toFixed(2))
        : 0;
  }

  loadDashboardData(): void {
    this.loadSales();
    this.loadInventory();
    this.loadSalesChart();
  }

  onPeriodChange(): void {
    this.loadDashboardData();
  }

  getCategoryColor(category: CategoryStat): string {
    const colors = [
      'linear-gradient(135deg, #3b82f6 0%, #2563eb 100%)',
      'linear-gradient(135deg, #10b981 0%, #059669 100%)',
      'linear-gradient(135deg, #f59e0b 0%, #f97316 100%)',
      'linear-gradient(135deg, #8b5cf6 0%, #7c3aed 100%)',
      'linear-gradient(135deg, #64748b 0%, #475569 100%)',
    ];

    const index = this.categoryStats.indexOf(category);
    return colors[index % colors.length];
  }

  // Navigation methods
  viewAllCategories(): void {
    // Navigate to categories report
    console.log('View all categories');
  }

  restockProduct(alert: StockAlert): void {
    // Navigate to inventory and open restock modal
    console.log('Restock product:', alert);
  }

  // Quick actions
  newSale(): void {
    // Navigate to POS/sales page
    // this.router.navigate(['/sales']);
    console.log('New sale');
  }

  addProduct(): void {
    // Navigate to add product page
    // this.router.navigate(['/inventory/add']);
    console.log('Add product');
  }

  viewInventory(): void {
    // Navigate to inventory page
    // this.router.navigate(['/inventory']);
    console.log('View inventory');
  }

  generateReport(): void {
    // Navigate to reports page
    // this.router.navigate(['/reports']);
    console.log('Generate report');
  }

  manageCustomers(): void {
    // Navigate to customers page
    // this.router.navigate(['/customers']);
    console.log('Manage customers');
  }

  viewSettings(): void {
    // Navigate to settings page
    // this.router.navigate(['/settings']);
    console.log('View settings');
  }
}
