import { Component, Input } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'ch-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.component.html',
  styles: ``
})
export class LoginComponent {
  username: string = "";
  password: string = "";

  login() {
    console.log("email: ", this.username);
  }
}
