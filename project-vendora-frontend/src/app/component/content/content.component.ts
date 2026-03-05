import { Component } from '@angular/core';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatButtonModule} from '@angular/material/button';
import {MatList, MatListItem, MatNavList} from '@angular/material/list';
import {RouterLink, RouterLinkActive, RouterOutlet} from '@angular/router';
import {MatIcon} from '@angular/material/icon';
import {MenuService} from '../../service/menu.service';
import {HeaderComponent} from '../header/header.component';

@Component({
  selector: 'app-content',
  imports: [MatSidenavModule, MatButtonModule, MatList, MatListItem, RouterLinkActive, RouterOutlet, MatIcon, RouterLink, MatNavList, HeaderComponent],
  templateUrl: './content.component.html',
  styleUrl: './content.component.scss'
})
export class ContentComponent {
  opened = false;

  constructor(private menuService: MenuService) {
    this.menuService.isMenuOpen.subscribe(data => {
      this.opened = data;
    })
  }
}
