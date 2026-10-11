import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from './auth.service';

/** Attaches the Basic Auth header (when logged in) to every outgoing request. */
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService);
  const authHeader = auth.authHeader;

  if (!authHeader) {
    return next(req);
  }

  return next(
    req.clone({
      setHeaders: { Authorization: authHeader },
    }),
  );
};

