import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { inject } from '@angular/core';
import { map, take } from 'rxjs';

/**
 * A guard that checks if the user is authenticated before allowing access to a route.
 * If the user is not authenticated, it redirects to the home page.
 *
 * @param route - The route being activated.
 * @param state - The current router state.
 * @returns An observable that emits a boolean indicating whether the user is authenticated or not.
 */
export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return authService.isAuthenticated$.pipe(
    take(1),
    map(isAuthenticated => {
      if (!isAuthenticated) {
        return true;
      }
      router.navigate(['/']);
      return false;
    })
  );
};
