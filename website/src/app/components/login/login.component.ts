import { Component } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService, Credentials } from '../../services/auth.service';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'ch-login',
  standalone: true,
  imports: [RouterLink, ReactiveFormsModule, MatInputModule, MatFormFieldModule, MatButtonModule, MatSnackBarModule],
  templateUrl: './login.component.html',
  styles: ``,
})
export class LoginComponent {
  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  loginForm = this.formBuilder.group({
    username: ['', Validators.required],
    password: ['', Validators.required],
  });

  login() {

    if (this.loginForm.invalid) {
      this.snackBar.open("Ingrese un nombre de usuario y contraseña.", "Cerrar", {
        duration: 10000,
      });
      return;
    }

    this.authService.login(this.loginForm.value as Credentials).subscribe({
      next: () => {
        this.snackBar.open("Sesión iniciada correctamente.", "Cerrar", {
          duration: 10000,
        });
        this.router.navigate(['/']);
      },
      error: (error) => {
        if (error.status === 401) {
          this.snackBar.open("Usuario o contraseña incorrectos.", "Cerrar", {
            duration: 10000,
          });
        } else {
          this.snackBar.open("Ocurrió un error. Por favor, intente nuevamente.", "Cerrar", {
            duration: 10000,
          });
        }
        console.error('Error: ', error);
      }
    });
  }
}
