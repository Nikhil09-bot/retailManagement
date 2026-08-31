# Frontend session idle timeout

## Scope

This repository currently contains the Spring Boot backend only; it has no frontend source directory or frontend package manifest. Integrate the following component in the frontend application's authenticated root layout (for example, `App`, `AppLayout`, or `AuthenticatedLayout`) so it remains mounted while routes change.

The backend logout endpoint is `POST /api/auth/logout` because `server.servlet.context-path=/api` is configured. It invalidates the server HTTP session and clears `JSESSIONID`.

> **HTTP Basic authentication note:** server logout cannot revoke a Basic `Authorization` header that the client keeps sending. The client logout function must remove all credentials/tokens/auth state and must stop attaching the authorization header before redirecting to login.

## React implementation (no external packages)

Create `src/auth/IdleSessionTimeout.tsx` in the frontend project:

```tsx
import { useCallback, useEffect, useRef, useState } from "react";

/** Change this one value to configure the global idle threshold. */
export const IDLE_TIMEOUT_MS = 30_000;

type IdleSessionTimeoutProps = {
  /** True only while a user is authenticated. */
  isAuthenticated: boolean;
  /** Clear all frontend auth state, call server logout if appropriate, then navigate to login. */
  onLogout: () => void | Promise<void>;
};

const ACTIVITY_EVENTS: (keyof WindowEventMap)[] = [
  "mousemove",
  "click",
  "keydown",
  "scroll",
];

export function IdleSessionTimeout({
  isAuthenticated,
  onLogout,
}: IdleSessionTimeoutProps) {
  const [isIdlePromptOpen, setIsIdlePromptOpen] = useState(false);
  const timerIdRef = useRef<ReturnType<typeof window.setTimeout> | null>(null);
  const promptOpenRef = useRef(false);

  const clearTimer = useCallback(() => {
    if (timerIdRef.current !== null) {
      window.clearTimeout(timerIdRef.current);
      timerIdRef.current = null;
    }
  }, []);

  const startTimer = useCallback(() => {
    clearTimer();
    timerIdRef.current = window.setTimeout(() => {
      // The modal remains open until the user makes an explicit choice.
      promptOpenRef.current = true;
      setIsIdlePromptOpen(true);
      timerIdRef.current = null;
    }, IDLE_TIMEOUT_MS);
  }, [clearTimer]);

  useEffect(() => {
    if (!isAuthenticated) {
      clearTimer();
      promptOpenRef.current = false;
      setIsIdlePromptOpen(false);
      return;
    }

    const recordActivity = () => {
      // Deliberately ignore every event while the prompt is displayed.
      // Only the Continue button is allowed to restart the clock.
      if (!promptOpenRef.current) {
        startTimer();
      }
    };

    ACTIVITY_EVENTS.forEach((eventName) =>
      window.addEventListener(eventName, recordActivity, { passive: true }),
    );
    startTimer();

    return () => {
      ACTIVITY_EVENTS.forEach((eventName) =>
        window.removeEventListener(eventName, recordActivity),
      );
      clearTimer();
    };
  }, [clearTimer, isAuthenticated, startTimer]);

  const continueSession = () => {
    promptOpenRef.current = false;
    setIsIdlePromptOpen(false);
    startTimer();
  };

  const logout = () => {
    clearTimer();
    // Keep the prompt visible while onLogout clears credentials and redirects.
    void onLogout();
  };

  if (!isIdlePromptOpen) {
    return null;
  }

  return (
    <div
      role="dialog"
      aria-modal="true"
      aria-labelledby="idle-session-heading"
      style={backdropStyle}
    >
      <section style={modalStyle}>
        <h2 id="idle-session-heading">Session inactivity</h2>
        <p>You have been idle for a while. Do you want to continue or logout?</p>
        <div style={actionsStyle}>
          <button type="button" onClick={continueSession}>
            Continue
          </button>
          <button type="button" onClick={logout}>
            Logout
          </button>
        </div>
      </section>
    </div>
  );
}

const backdropStyle = {
  position: "fixed" as const,
  inset: 0,
  zIndex: 10000,
  display: "grid",
  placeItems: "center",
  padding: "1rem",
  background: "rgba(0, 0, 0, 0.5)",
};

const modalStyle = {
  width: "min(28rem, 100%)",
  padding: "1.5rem",
  borderRadius: "0.5rem",
  background: "#fff",
  boxShadow: "0 1rem 3rem rgba(0, 0, 0, 0.3)",
};

const actionsStyle = {
  display: "flex",
  justifyContent: "flex-end",
  gap: "0.75rem",
};
```

Mount the component once in the authenticated root layout, not inside an individual route/page:

```tsx
import { IdleSessionTimeout } from "./auth/IdleSessionTimeout";

export function AppLayout() {
  const { isAuthenticated, clearAuth } = useAuth();
  const navigate = useNavigate();

  const logout = async () => {
    // Clear UI credentials first so no later request can re-authenticate through Basic Auth.
    clearAuth(); // Adapt this to clear the real token/credential store.
    await fetch("/api/auth/logout", {
      method: "POST",
      credentials: "include",
    }).catch(() => {
      // Local logout and redirect still must complete if the API is unavailable.
    });
    navigate("/login", { replace: true });
  };

  return (
    <>
      <IdleSessionTimeout isAuthenticated={isAuthenticated} onLogout={logout} />
      <Outlet />
    </>
  );
}
```

If the frontend uses a different API base URL, replace `/api/auth/logout` with its configured API URL. If it uses a token, remove it from the correct store (`localStorage`, `sessionStorage`, cookie, state manager, or all applicable stores) in `clearAuth()`.

## Behaviour checklist

- A single timer starts when an authenticated root layout mounts.
- `mousemove`, `click`, `keydown`, and `scroll` restart the timer during normal use.
- At `IDLE_TIMEOUT_MS` (30 seconds), the modal opens with no countdown and no automatic close.
- Events while the modal is open are ignored; only **Continue** resets the timer.
- **Continue** does not reload the page or change application/session state.
- **Logout** clears client-side auth state, calls `POST /api/auth/logout`, and redirects to `/login`.
- No cross-tab communication is implemented.

## Prompts for an existing non-React frontend

### Angular

> In this Angular application, add a singleton `IdleSessionService` provided in root. Define `IDLE_TIMEOUT_MS = 30_000`. Start it only after authentication and stop it on logout. Use `Renderer2.listen` or `fromEvent(window, ...)` to globally observe `mousemove`, `click`, `keydown`, and `scroll`, resetting a single timeout unless the idle dialog is already open. At expiry, use Angular Material `MatDialog` (disable close/backdrop close) or the existing application modal to show exactly: `You have been idle for a while. Do you want to continue or logout?` with Continue and Logout buttons. Keep it open indefinitely; do not reset activity while it is open. Continue closes it and restarts the timer without altering application state. Logout clears the real authentication store, calls `POST /api/auth/logout` with credentials, and navigates to `/login`. Wire the service at `AppComponent` or the authenticated shell so route changes do not unmount it. Do not add multi-tab synchronization. Preserve existing styling and auth APIs.

### Vue

> In this Vue application, add a composable or Pinia-backed global idle-session controller with `IDLE_TIMEOUT_MS = 30_000`. Mount it once in `App.vue` or the authenticated root layout, not in views. Register and clean up global `window` listeners for `mousemove`, `click`, `keydown`, and `scroll`. Any of these resets a single timeout while the modal is closed. On expiry, set a reactive idle-modal flag and show exactly `You have been idle for a while. Do you want to continue or logout?` with Continue and Logout controls. The dialog must not auto-close, have a countdown, or restart the timer due to activity while open. Continue is the only action that closes it and restarts the timer without refreshing/changing session data. Logout clears the existing auth/token storage, sends `POST /api/auth/logout` including cookies if used, and routes to `/login`. Do not implement cross-tab synchronization. Reuse the project’s modal and router conventions.

