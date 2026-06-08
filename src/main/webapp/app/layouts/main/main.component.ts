import { Component, OnInit, Renderer2, RendererFactory2, inject, signal, HostListener } from '@angular/core';

import { CommonModule } from '@angular/common';

import { NavigationEnd, Router, RouterOutlet } from '@angular/router';

import { LangChangeEvent, TranslateService } from '@ngx-translate/core';

import dayjs from 'dayjs/esm';

import { AccountService } from 'app/core/auth/account.service';
import { AppPageTitleStrategy } from 'app/app-page-title-strategy';

import FooterComponent from '../footer/footer.component';
import PageRibbonComponent from '../profiles/page-ribbon.component';
import { SidebarComponent } from 'app/sidebar/sidebar.component';

/* ADD THESE COMPONENTS */
// import SidebarComponent from '../sidebar/sidebar.component';
// import BreadcrumbComponent from '../breadcrumb/breadcrumb.component';

@Component({
  selector: 'jhi-main',
  templateUrl: './main.component.html',

  providers: [AppPageTitleStrategy],

  // imports: [RouterOutlet, FooterComponent, PageRibbonComponent, SidebarComponent, BreadcrumbComponent],
  imports: [RouterOutlet, FooterComponent, PageRibbonComponent, CommonModule, SidebarComponent],
  styleUrl: 'main.component.scss',
})
export default class MainComponent implements OnInit {
  isSidebarCollapsed = signal(false);
  title = '';
  private readonly renderer: Renderer2;

  private readonly router = inject(Router);

  private readonly appPageTitleStrategy = inject(AppPageTitleStrategy);

  private readonly accountService = inject(AccountService);

  private readonly translateService = inject(TranslateService);

  private readonly rootRenderer = inject(RendererFactory2);

  /* HIDE SIDEBAR/NAVBAR/FOOTER */
  // eslint-disable-next-line @typescript-eslint/member-ordering
  hideLayout = false;

  /* ROUTES WITHOUT SIDEBAR */
  // eslint-disable-next-line @typescript-eslint/member-ordering
  hiddenRoutes = ['/', '/login', '/home'];

  constructor() {
    this.renderer = this.rootRenderer.createRenderer(document.querySelector('html'), null);
  }

  ngOnInit(): void {
    /* AUTO LOGIN */
    this.accountService.identity().subscribe();

    /* LANGUAGE */
    this.translateService.onLangChange.subscribe((langChangeEvent: LangChangeEvent) => {
      this.appPageTitleStrategy.updateTitle(this.router.routerState.snapshot);

      dayjs.locale(langChangeEvent.lang);

      this.renderer.setAttribute(document.querySelector('html'), 'lang', langChangeEvent.lang);
    });
    this.checkScreenSize();
    /* ROUTE CHANGE */
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.hideLayout = this.hiddenRoutes.includes(this.router.url);
      }
    });
  }
  @HostListener('window:resize')
  onResize(): void {
    this.checkScreenSize();
  }
  checkScreenSize(): void {
    if (window.innerWidth < 992) {
      this.isSidebarCollapsed.set(true);
    } else {
      this.isSidebarCollapsed.set(false);
    }
  }
  collapsesidebar(): void {
    this.isSidebarCollapsed.set(true);
  }

  toggleSidebar(): void {
    this.isSidebarCollapsed.update(isSidebarCollapsed => !isSidebarCollapsed);
  }
}
