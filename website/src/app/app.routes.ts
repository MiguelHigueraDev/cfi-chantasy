import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { WelcomeScreenComponent } from './components/welcome-screen/welcome-screen.component';
import { RegisterComponent } from './components/register/register.component';

export const routes: Routes = [
    { path: '', component: WelcomeScreenComponent, title: 'Chantasy League' },
    { path: 'login', component: LoginComponent, title: 'Chantasy League | Iniciar sesión' },
    { path: 'register', component: RegisterComponent, title: 'Chantasy League | Registrarse' },
    { path: '**', redirectTo: '/login' }
];
