import { Injectable } from '@angular/core';
import { environment } from '../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Race } from '../interfaces/Race';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RaceService {

  private baseUrl = environment.baseUrl + '/api';

  constructor(private http: HttpClient) {}

  getNextRace(): Observable<Race> {
    return this.http.get<Race>(`${this.baseUrl}/races/next`);
  }

}
