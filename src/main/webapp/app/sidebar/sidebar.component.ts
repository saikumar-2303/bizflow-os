import { Component, signal, inject, OnInit } from '@angular/core';
import { EntitySidebarItems } from './sidebar-items-entities';
import { LANGUAGES } from 'app/config/language.constants';
import { AccountService } from 'app/core/auth/account.service';
import SidebarItem from './sidebar-item.model';
import { StateStorageService } from 'app/core/auth/state-storage.service';
import { TranslateService } from '@ngx-translate/core';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'jhi-sidebar',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.scss',
})
export class SidebarComponent implements OnInit {
  inProduction?: boolean;
  isSidebarCollapsed = signal(true);
  languages = LANGUAGES;
  openAPIEnabled?: boolean;
  account = inject(AccountService).trackCurrentAccount();
  entitiesSidebarItems: SidebarItem[] = [];

  private readonly translateService = inject(TranslateService);
  private readonly router = inject(Router);
  private readonly stateStorageService = inject(StateStorageService);

  ngOnInit(): void {
    this.entitiesSidebarItems = EntitySidebarItems;
  }

  changeLanguage(languageKey: string): void {
    this.stateStorageService.storeLocale(languageKey);
    this.translateService.use(languageKey);
  }

  collapsesidebar(): void {
    this.isSidebarCollapsed.set(true);
  }

  toggleSidebar(): void {
    this.isSidebarCollapsed.update(isSidebarCollapsed => !isSidebarCollapsed);
  }
}
