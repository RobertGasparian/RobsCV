# RobsCV Project Rules

## Product Direction
Build RobsCV as a polished native Android portfolio app, not a resume PDF wrapper or API showcase.

Use modern Android features only when they improve the user experience or demonstrate Android judgment naturally. Avoid adding features purely because they are technically possible.

The app should help a recruiter, hiring manager, or Android engineer understand Rob quickly, while the implementation demonstrates thoughtful modern Android development.

## Technical Direction
Use Kotlin, Jetpack Compose, Material 3, Material 3 Expressive, single-activity architecture, MVI, Clean Architecture, ViewModels, UI state, and a clear data layer.

Prefer Material 3 Expressive components, add-ons, and motion APIs when they are available and fit the screen's purpose. Use expressive navigation, transitions, interactive states, and animations to make the app feel modern and polished, while keeping the experience professional and avoiding effects that distract from the CV content.

Use local static data first when it is enough. Use DataStore for small persistent data such as preferences or UI settings. Use Room when the app has larger structured data that benefits from querying, relations, or offline persistence.

Prefer KMP-friendly libraries, APIs, and architecture choices so the project can move toward Kotlin Multiplatform later with minimal churn. Break this rule only when the KMP-friendly option creates real inconvenience, weak Android ergonomics, or a worse user/developer experience.

For future networking, prefer Ktor Client over Retrofit because Ktor is Kotlin Multiplatform-friendly. Use Retrofit only if an Android-only integration becomes clearly simpler and the tradeoff is worth documenting.

Keep the core networking module endpoint-agnostic. It should provide reusable HTTP client configuration, result/error handling, and typed request helpers, but feature/data modules should own their own endpoint constants, DTOs, request functions, and mapping logic.

Structure feature code by Clean Architecture layers. Each substantial feature should be split into `domain`, `data`, `presentation`, and `ui` modules: domain owns business models and repository interfaces, data owns repository implementations plus data sources/DTOs/mappers, presentation owns state holders and UI state, and ui owns Compose screens/components. Dependency direction should point inward toward domain; domain must not depend on data, network, Android UI, or framework details.

Use a consistent stateful/stateless Compose screen pattern. `*Screen` composables are the stateful route-level boundary: collect state from ViewModels, connect lifecycle-aware effects, handle one-off effects, and translate UI events into callbacks for navigation or presentation logic. Keep them in the feature UI module's `ui.screen` package. `*Component` composables are stateless renderers: they take UI state/data, render the combined screen UI, and hoist user events upward without owning ViewModels, navigation state, repositories, or side effects. Keep them in the feature UI module's `ui.component` package. Child composables should follow the same event-hoisting style.

Create previews for stateless composables. For small reusable stateless composables that are not full-screen components, keep the preview in the same file. For full-screen stateless `*Component` composables, create a dedicated preview file and cover meaningful variations across mobile, foldable, and tablet layouts in both light and dark themes. Each UI state type should expose `initialState()` for the default runtime state and `preview()` for representative preview/demo data.

Use Navigation 3 as the default navigation approach. Keep navigation shared-friendly by modeling destinations as serializable route/state objects, avoiding Android `Context`, `Intent`, or framework objects in route definitions, and keeping navigation decisions in state/logic layers where practical rather than burying them inside UI components.

Include meaningful unit tests and UI tests. Prefer Paparazzi for screenshot/UI testing where it gives fast, reliable feedback.

Keep shared and future-KMP tests platform-friendly. Use MockK only for Android/JVM-specific tests where JVM mocking is appropriate. For shared/domain/common-style tests, prefer constructor-injected interfaces, hand-written fakes, lightweight test doubles, Ktor MockEngine for network behavior, and deterministic Flow/coroutine testing. Avoid designing production code around JVM-only mocking capabilities such as static mocking, final-class tricks, or runtime bytecode manipulation.

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
