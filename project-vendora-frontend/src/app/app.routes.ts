import { Routes } from '@angular/router';
import {CreateSaleComponent} from './component/create-sale/create-sale.component';
import {LoginComponent} from './component/login/login.component';
import {HeaderComponent} from './component/header/header.component';
import {ContentComponent} from './component/content/content.component';
import {ProductsComponent} from './component/products/products.component';
import {SalesHistoryComponent} from './component/sales-history/sales-history.component';
import { DashboardComponent } from './component/dashboard/dashboard.component';
import { AddProductComponent } from './component/add-product/add-product.component';

export const routes: Routes = [  
  {path:'',redirectTo:'dashboard',pathMatch:'full'},
  {path:"dashboard",component:DashboardComponent},
  {path:'create-sale',component:CreateSaleComponent},
  {path: 'login', component: LoginComponent },
  {path:"content", component: ContentComponent},
  {path:"products", component: ProductsComponent},
  {path:"sales-history", component: SalesHistoryComponent},
  {
    path: 'inventory/add',
    component: AddProductComponent
  },
  {
    path: 'inventory/edit/:id',
    component: AddProductComponent
  }
];
