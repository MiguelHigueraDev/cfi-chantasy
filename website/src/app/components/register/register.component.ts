import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'ch-register',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './register.component.html',
  styles: ``
})
export class RegisterComponent {
  registerCode: string = "";
  username: string = "";
  password: string = "";

  register() {
    console.log("email: ", this.username);
  }
}
