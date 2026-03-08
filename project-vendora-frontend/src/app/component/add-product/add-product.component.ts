import { Component, OnInit } from '@angular/core';
import {
  CategoryDTO,
  InventorySaveDTO,
  ProductSaveDTO,
} from '../../model/inventory';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../service/user.service';
import {
  CurrencyPipe,
  Location,
  NgForOf,
  NgIf,
} from '@angular/common';
import { FormsModule } from '@angular/forms';
@Component({
  selector: 'app-add-product',
  imports: [CurrencyPipe, FormsModule, NgIf, NgForOf],
  templateUrl: './add-product.component.html',
  styleUrl: './add-product.component.scss',
})
export class AddProductComponent implements OnInit {
  isEditMode: boolean = false;
  productId: number | null = null;
  showPharmacyFields: boolean = false;

  // Categories list
  categories: CategoryDTO[] = [];

  // Product form data
  product: ProductSaveDTO = {} as ProductSaveDTO;

  //Inventory reuqest dto
  inventory: InventorySaveDTO = {} as InventorySaveDTO;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private location: Location,
    private userService: UserService,
  ) {}

  ngOnInit(): void {
    // Check if we're in edit mode
    this.route.params.subscribe((params) => {
      if (params['id']) {
        this.isEditMode = true;
        this.productId = +params['id'];
        this.loadProduct(this.productId);
      }
    }); 

    this.loadCategories();
  }

  loadCategories(): void {
    this.userService.getAllCategories().subscribe((categories) => {
      this.categories = categories;
      console.log(categories);
    });
  }

  loadProduct(id: number): void {
    // TODO: Replace with actual API call
    // this.productService.getProduct(id).subscribe(product => {
    //   this.product = { ...product };
    //   this.inventory = product.inventory;
    //   if (product.genericName || product.manufacturer) {
    //     this.showPharmacyFields = true;
    //   }
    // });

    console.log('Loading product:', id);
  }

  // Validation
  isFormValid(): boolean {
    return !!(
      this.product.name &&
      this.product.name.trim().length > 0 &&
      this.product.category &&
      this.product.unitType &&
      this.product.price > 0 &&
      this.inventory.quantity >= 0
    );
  }

  // Calculations
  getProfitMargin(): string {
    if (!this.product.price || !this.product.costPrice) return '-';

    const profit = this.product.price - this.product.costPrice;
    const margin = (profit / this.product.costPrice) * 100;

    return `${profit.toFixed(2)} (${margin.toFixed(1)}%)`;
  }

  getProfitPercentage(): number {
    if (!this.product.price || !this.product.costPrice) return 0;

    const profit = this.product.price - this.product.costPrice;
    const margin = (profit / this.product.costPrice) * 100;

    return Math.min(Math.max(margin, 0), 100);
  }

  getProfit(): number {
    if (!this.product.price || !this.product.costPrice) return 0;
    return this.product.price - this.product.costPrice;
  }

  getCategoryName(): string | null {
    if (!this.product.category?.name) return null;
    const category = this.categories.find(
      (c) => c.id === this.product.category?.id,
    );
    return category ? category.name : null;
  }

  // Image handling
  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      // Validate file size (max 5MB)
      if (file.size > 5 * 1024 * 1024) {
        alert('File size must be less than 5MB');
        return;
      }

      // Validate file type
      if (!file.type.startsWith('image/')) {
        alert('Please select an image file');
        return;
      }

      // Convert to base64 or upload to server
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.product.image = e.target.result;
      };
      reader.readAsDataURL(file);

      // TODO: Upload to server
      // const formData = new FormData();
      // formData.append('image', file);
      // this.productService.uploadImage(formData).subscribe(response => {
      //   this.product.image = response.imageUrl;
      // });
    }
  }

  removeImage(): void {
    this.product.image = null;
  }

  // Actions
  saveProduct(): void {
    console.log(this.product.category);
    if (!this.isFormValid()) {
      alert('Please fill in all required fields');
    }

    const productData = {
      product: this.product,
      inventory: this.inventory,
    };

    if (this.isEditMode) {
      // TODO: Update product
      // this.productService.updateProduct(this.productId!, productData)
      //   .subscribe({
      //     next: (response) => {
      //       console.log('Product updated:', response);
      //       this.router.navigate(['/inventory']);
      //     },
      //     error: (error) => {
      //       console.error('Error updating product:', error);
      //       alert('Failed to update product');
      //     }
      //   });

      // console.log('Update product:', productData);
      // alert('Product would be updated (API not connected)');
    } else {
      this.userService.saveProduct(productData).subscribe({
        next: (response) => {
          console.log('Product created:', response);
          this.router.navigate(['/inventory']);
        },
        error: (error) => {
          console.error('Error creating product:', error);
          alert('Failed to create product');
        },
      });
      // TODO: Create product
      // this.productService.createProduct(productData)
      //   .subscribe({
      //     next: (response) => {
      //       console.log('Product created:', response);
      //       this.router.navigate(['/inventory']);
      //     },
      //     error: (error) => {
      //       console.error('Error creating product:', error);
      //       alert('Failed to create product');
      //     }
      //   });

      console.log('Create product:', productData);
      alert('Product would be saved (API not connected)');
    }
  }

  cancel(): void {
    if (this.hasUnsavedChanges()) {
      if (
        confirm('You have unsaved changes. Are you sure you want to leave?')
      ) {
        this.goBack();
      }
    } else {
      this.goBack();
    }
  }

  goBack(): void {
    this.location.back();
    // Or navigate to specific route:
    // this.router.navigate(['/inventory']);
  }

  hasUnsavedChanges(): boolean {
    // Check if any form field has been modified
    return !!(
      this.product.name ||
      this.product.price > 0 ||
      this.inventory.quantity > 0
    );
  }
}
