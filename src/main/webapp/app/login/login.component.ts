import { AfterViewInit, Component, ElementRef, OnInit, inject, signal, viewChild } from '@angular/core';
import { ReactiveFormsModule, Validators, FormBuilder } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { LoginService } from 'app/login/login.service';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'jhi-login',
  standalone: true,
  imports: [SharedModule, ReactiveFormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export default class LoginComponent implements OnInit, AfterViewInit {
  username = viewChild.required<ElementRef>('username');

  authenticationError = signal(false);

  loginForm = this.fb.nonNullable.group({
    username: ['', Validators.required],
    password: ['', Validators.required],
    rememberMe: [false],
  });

  private readonly accountService = inject(AccountService);
  private readonly loginService = inject(LoginService);
  private readonly router = inject(Router);

  constructor(private fb: FormBuilder) {}
  ngOnInit(): void {
    // if already authenticated then navigate to home page
    this.accountService.identity().subscribe(() => {
      if (this.accountService.isAuthenticated()) {
        this.router.navigate(['/dashboard']);
      }
    });
  }

  ngAfterViewInit(): void {
    this.username().nativeElement.focus();
  }

  login(): void {
    this.loginService.login(this.loginForm.getRawValue()).subscribe({
      next: () => {
        this.authenticationError.set(false);

        if (!this.router.getCurrentNavigation()) {
          this.router.navigate(['/dashboard']);
        }
      },

      error: () => this.authenticationError.set(true),
    });
  }
}
