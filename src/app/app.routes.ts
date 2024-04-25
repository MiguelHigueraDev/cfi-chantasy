import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { WelcomeScreenComponent } from './welcome-screen/welcome-screen.component';
import { RegisterComponent } from './register/register.component';

export const routes: Routes = [
    { path: '', component: WelcomeScreenComponent },
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
    { path: '**', redirectTo: '' }
];
