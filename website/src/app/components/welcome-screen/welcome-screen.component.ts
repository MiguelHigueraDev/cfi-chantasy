import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { RaceService } from '../../services/race-service.service';
import { Race } from '../../interfaces/Race';
import { DateFormatPipe } from '../../pipes/date-format.pipe';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'ch-welcome-screen',
  standalone: true,
  imports: [RouterLink, DateFormatPipe],
  templateUrl: './welcome-screen.component.html',
  styles: ``
})
export class WelcomeScreenComponent implements OnInit {
  nextRace: Race | null = null;
  isAuthenticated: boolean = false;

  constructor(private raceService: RaceService, private authService: AuthService) {}

  ngOnInit(): void {
    this.authService.isAuthenticated$.subscribe(authenticated => {
      this.isAuthenticated = authenticated;
    });
    
    this.raceService.getNextRace().subscribe({
      next: (data: Race) => {
        this.nextRace = data;
      },
      error: (error) => {
        console.error('Error obteniendo datos de la última carrera: ', error);
      }
    });
  }

}
