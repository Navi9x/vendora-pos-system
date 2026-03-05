import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UserAuthService {

  constructor() { }

  public setUser(user:string){
    localStorage.setItem('user',JSON.stringify(user));
  }

  public getUser():[]{
    return JSON.parse(<string>localStorage.getItem('user'));
  }

  public setBusinesses(businesses:any[]){
    localStorage.setItem('businesses',JSON.stringify(businesses));
  }

  public getBusinesses():any[]{
    return JSON.parse(<any>localStorage.getItem('businesses'));
  }

  public setToken(jwt:string){
    localStorage.setItem("jwt",jwt);
  }

  public getToken():string{
    return <string>localStorage.getItem('jwt');
  }

  public clear(){
    localStorage.clear();
  }

  public isLoggedIn (){
    return this.getUser() && this.getToken();
  }
}
