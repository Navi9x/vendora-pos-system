import { Component, OnInit } from '@angular/core';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MenuService } from '../../service/menu.service';
import { ContentComponent } from '../content/content.component';
import { DashboardComponent } from '../dashboard/dashboard.component';
import { UserService } from '../../service/user.service';
import { Router } from '@angular/router';
import { UserAuthService } from '../../service/user-auth.service';

@Component({
  selector: 'app-header',
  imports: [
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    DashboardComponent,
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss',
})
export class HeaderComponent implements OnInit {
  constructor(
    private menuService: MenuService,
    private userService: UserService,
    private router: Router,
    private userAuthService: UserAuthService,
  ) {}

  ngOnInit() {
    if (this.userAuthService.isLoggedIn()) {
      this.userService.isTokenValid().subscribe(
        (isValid) => {
          if (!isValid) {
            this.router.navigate(['/login']);
          }
        },
        (error) => {
          this.router.navigate(['/forbidden']);
        },
      );
    }
  }

  toggleMenu() {
    this.menuService.toggle();
  }
}
