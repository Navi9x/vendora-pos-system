import { Injectable } from '@angular/core';
import {BehaviorSubject} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MenuService {
  constructor() { }

  public isMenuOpen = new BehaviorSubject<boolean>(false);
  public opened = false;

  public toggle(){
    this.opened = !this.opened;
    this.isMenuOpen.next(this.opened);
  }

}
