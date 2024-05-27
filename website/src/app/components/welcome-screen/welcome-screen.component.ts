import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { RaceService } from '../../services/race-service.service';
import { Race } from '../../interfaces/Race';
import { DateFormatPipe } from '../../pipes/date-format.pipe';

@Component({
  selector: 'ch-welcome-screen',
  standalone: true,
  imports: [RouterLink, DateFormatPipe],
  templateUrl: './welcome-screen.component.html',
  styles: ``
})
export class WelcomeScreenComponent implements OnInit {
  nextRace: Race | null = null;

  constructor(private raceService: RaceService) {}

  ngOnInit(): void {
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
