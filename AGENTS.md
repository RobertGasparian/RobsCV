# RobsCV Project Rules

## Product Direction
Build RobsCV as a polished native Android portfolio app, not a resume PDF wrapper or API showcase.

Use modern Android features only when they improve the user experience or demonstrate Android judgment naturally. Avoid adding features purely because they are technically possible.

The app should help a recruiter, hiring manager, or Android engineer understand Rob quickly, while the implementation demonstrates thoughtful modern Android development.

## Technical Direction
Use Kotlin, Jetpack Compose, Material 3, Material 3 Expressive, single-activity architecture, MVI, Clean Architecture, ViewModels, UI state, and a clear data layer.

Use local static data first when it is enough. Use DataStore for small persistent data such as preferences or UI settings. Use Room when the app has larger structured data that benefits from querying, relations, or offline persistence.

Include meaningful unit tests and UI tests. Prefer Paparazzi for screenshot/UI testing where it gives fast, reliable feedback.

Experimental Android and Jetpack APIs are welcome when they improve the app or demonstrate modern Android skill. Avoid only those libraries or APIs that have obvious bugs, severe instability, or would create more maintenance burden than value.

Use `Rcv` as the code symbol prefix for project-specific classes, composables, functions, variables, files, tokens, and helpers. Keep official names as `RobsCV`, including the app name, repository name, user-facing text, documentation titles, Android resource names, and the design system name unless the user explicitly asks to rename them.

## UX Direction
Prioritize clarity, speed, accessibility, adaptive layouts, dark mode, dynamic color, edge-to-edge, and tasteful motion.

Design the app to work well on phones, tablets, and foldable devices. Larger screens should feel intentionally designed, not merely stretched.

Avoid gimmicks, fake backend features, unnecessary widgets, and over-engineered abstractions.

## Workflow
Before implementation, maintain a project plan document with screens, phases, and decisions.

Keep changes small and reviewable. Add tests, previews, and screenshot coverage where they meaningfully reduce risk.

When choosing between multiple valid approaches, prefer the one that best balances user value, Android best practices, maintainability, and portfolio signal.

## Local Android Skills
Project-local copies of Google's Android skills live in `.codex/skills`. Use them when working on matching Android topics such as edge-to-edge, Navigation 3, R8 analysis, AGP upgrades, CameraX migration, Play Billing upgrades, Perfetto SQL, Android CLI usage, XML-to-Compose migration, and XR display glasses with Jetpack Compose Glimmer.
