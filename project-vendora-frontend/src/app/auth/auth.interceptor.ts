import {HttpErrorResponse, HttpInterceptorFn} from '@angular/common/http';
import {UserAuthService} from '../service/user-auth.service';
import {inject} from '@angular/core';
import {Router} from '@angular/router';
import {catchError, throwError} from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {

  const authService= inject(UserAuthService);
  const router = inject(Router);

  if (req.headers.get('No-Auth') === 'True') {
    return next(req);
  }

  const token = authService.getToken();

  const authReq = token
    ? req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    })
    : req;

  return next(authReq).pipe(
    catchError((err: HttpErrorResponse) => {

      if (err.status === 401) {
        authService.clear();
        router.navigate(['/login']);
      }

      if (err.status === 403) {
        router.navigate(['/forbidden']);
      }

      return throwError(() => err);
    })
  );

};
