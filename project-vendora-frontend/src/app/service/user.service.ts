import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {UserAuthService} from './user-auth.service';
import {NgForm} from '@angular/forms';
import {Observable} from 'rxjs';
import {Customer} from '../model/Customer';
import {Category} from '../model/Category';
import {SaleResponse} from '../model/SaleResponse';
import {Cashier} from '../model/Cashier';
import {InventoryResponseDTO} from '../model/inventory';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  BASE_URL = 'http://localhost:8080';

  requestHeaders = new HttpHeaders({
    'Content-Type': 'application/json',
    'No-Auth': 'True'
  })

  constructor(private http:HttpClient,private userAuthService:UserAuthService) { }

  public login(loginForm:any){
    return this.http.post(this.BASE_URL+'/authentication',loginForm,{headers:this.requestHeaders});
  }

  public getAllProducts(businessId:string):Observable<Object>{
    return this.http.get(this.BASE_URL+'/api/v1/sales/'+businessId)
  }

  public countAllSales():Observable<Object>{
    return this.http.get(this.BASE_URL+'/api/v1/sales/total')
  }

  public saveSales(saleData:any){
    return this.http.post(this.BASE_URL+'/api/v1/sales/save',saleData);
  }

  public getAllCustomers() {
    return this.http.get<Customer[]>(this.BASE_URL + '/api/v1/customers/get-all');
  }

  public getAllCategories(){
    return this.http.get<Category[]>(this.BASE_URL + '/api/v1/products/categories');
  }

  public getAllCashiers():Observable<Cashier[]>{
    return this.http.get<Cashier[]>(this.BASE_URL + '/user/get-all');
  }

  public loadInventory():Observable<InventoryResponseDTO[]>{
    return this.http.get<InventoryResponseDTO[]>(this.BASE_URL+'/api/v1/inventory/loadAll');
  }

  public loadSalesHistory(page: number, size: number,selectedDate?: Date | null,
                          cashierId?: number | null, saleStatus?:string | null,paymentStatus?:string|null){

    const params:any={
      page,
      size,
    }

    if (selectedDate) {
      params.date = selectedDate.toISOString().split('T')[0];
    }

    if (cashierId !== null && cashierId !== undefined) {
      params.cashierId = cashierId;
    }

    if (saleStatus !== null && saleStatus !== undefined) {
      params.saleStatus = saleStatus;
    }

    if (paymentStatus !== null && saleStatus !== undefined) {
      params.paymentStatus = paymentStatus;
    }

    return this.http.get<SaleResponse[]>(`${this.BASE_URL}/api/v1/sales-history/load`,{params});
  }

}
