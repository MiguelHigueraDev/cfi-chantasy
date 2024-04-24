import { Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { WelcomeScreenComponent } from './welcome-screen/welcome-screen.component';

export const routes: Routes = [
    { path: '', component: WelcomeScreenComponent },
    { path: 'login', component: LoginComponent },
    { path: '**', redirectTo: '' }
];
