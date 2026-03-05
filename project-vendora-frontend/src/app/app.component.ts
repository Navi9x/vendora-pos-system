import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {CreateSaleComponent} from './component/create-sale/create-sale.component';
import {LoginComponent} from './component/login/login.component';
import {MatToolbarModule} from '@angular/material/toolbar';
import {HeaderComponent} from './component/header/header.component';
import {ContentComponent} from './component/content/content.component';
import {UserAuthService} from './service/user-auth.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, CreateSaleComponent, LoginComponent, MatToolbarModule, HeaderComponent, ContentComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'vendora-frontend';

  constructor(protected userAuthService: UserAuthService) {
  }
}
