import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'dateFormat',
  standalone: true
})
export class DateFormatPipe implements PipeTransform {

  transform(dateString: string, ...args: string[]): string {
    const date = new Date(dateString);

    const localDate = date.toLocaleDateString(undefined, {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });

    const localTime = date.toLocaleTimeString(undefined, {
      hour: '2-digit',
      minute: '2-digit'
    });;

    return `${localDate}, ${localTime}`;
  }

}
