# Postly

Postly is an Instagram-style social media app built natively for Android with **Kotlin and Jetpack Compose**. Share photos and updates, follow people, like and comment, bookmark posts, and get real-time push notifications for activity on your account.

## Backend

Postly's mobile client talks to a RESTful API built with **Node.js, Express, and MongoDB**.

**[Postly Backend Repository](https://github.com/bavlysafwatt/postly-backend)**

The backend repository contains the API, database integration, authentication, image uploads, and push notification dispatch used by this app.

## Features

- **Auth** — register with a profile photo, log in, forgot/reset password via emailed OTP
- **Feed** — paginated post feed, create and edit posts with up to 5 photos, like, comment, and bookmark, with optimistic UI updates
- **Search** — debounced live user search with a locally-persisted recent-searches list
- **Notifications** — paginated in-app activity feed (likes, comments, follows) plus real push notifications via Firebase Cloud Messaging, delivered even when the app is closed, with tap-to-navigate deep linking
- **Profile** — your own and other users' profiles, a photo-grid posts tab, a private bookmarks tab, follow/unfollow, followers/following lists, profile editing
- **Settings** — light/dark/system theme, push notification opt-in with runtime permission handling, change password, logout

## Tech stack

| Layer | Choice |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose, Material 3 |
| Architecture | Clean Architecture + MVVM, feature-based modules |
| DI | Hilt |
| Networking | Retrofit, OkHttp, Gson |
| Async | Kotlin Coroutines + Flow |
| Navigation | Navigation Compose (type-safe routes) |
| Pagination | Paging 3 (feed, comments, notifications); manual pagination (profile posts) |
| Local storage | Room (recent searches), DataStore (session & preferences) |
| Images | Coil |
| Push notifications | Firebase Cloud Messaging |
| Theming | MaterialKolor (dynamic Material 3 palette generated from a single seed color) |

## Getting started

1. Clone the repo.
2. Add your own `app/google-services.json` (from a Firebase project with Cloud Messaging enabled) — required for the project to build.
3. Open in Android Studio and let Gradle sync.
4. The app points at the deployed backend by default (`BuildConfig.BASE_URL`, set in `app/build.gradle.kts`) — no local backend setup needed to run it.
5. Run on a device or emulator, API 24+.

Building a signed release additionally requires a local `keystore.properties` (gitignored, not included in the repo).
