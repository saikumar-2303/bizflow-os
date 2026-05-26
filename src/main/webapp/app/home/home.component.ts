import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'jhi-home',
  standalone: true,
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export default class HomeComponent {
  private readonly router = inject(Router);

  login(): void {
    this.router.navigate(['/login']);
  }
}
