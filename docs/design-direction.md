# RobsCV Design Direction

## Product Intent

RobsCV is a native Android CV and portfolio app for Rob Gasparian. It should help recruiters
understand Rob quickly while giving Android engineers enough signal to recognize thoughtful modern
Android work.

The app should feel like a focused product, not a resume PDF wrapper, marketing site, dashboard, or
component showcase.

Official product and design-system names should remain `RobsCV` and `RobsCV DSM`. Code symbols
should avoid a blanket `Rcv` prefix and use it only where it prevents ambiguity with platform,
library, or similarly named project types.

## Personality

- Professional Android craft with calm confidence.
- Clear, useful, and easy to scan.
- Modern without chasing visual noise.
- Technical enough for engineers, approachable enough for recruiters.
- Personal without becoming playful or gimmicky.

## What To Avoid

- Website-style hero sections.
- Fake analytics dashboards.
- Skill percentage bars.
- Decorative gradients, blobs, or noisy backgrounds.
- Overly colorful card grids.
- Terminal/hacker clichés.
- Features that exist only to show off.

## Visual Direction

Use Material 3 as the foundation and bring in Material 3 Expressive ideas through spacing, motion,
shape, component states, and hierarchy rather than loud decoration.

The UI should be dense enough for CV content but never cramped. Cards and sections should support
scanning, comparison, and quick action.

## Color Direction

Support dynamic color when available, with a custom fallback palette that feels Android-native and
professional.

Fallback direction:

- Primary: calm teal or blue-green for action and identity.
- Secondary: muted blue/gray for supporting UI.
- Tertiary: restrained warm accent for highlights.
- Surfaces: neutral, layered, and readable in both light and dark mode.
- Status colors: subtle success, warning, and error roles for action feedback.

Light and dark themes should feel like the same product.

## Typography Direction

Use a clear Material-style type hierarchy:

- Profile name and major screen titles should be confident but not oversized.
- Section titles should be compact and scannable.
- Metadata such as dates, company names, and tech labels should remain readable.
- Body text should favor clarity over personality.

## Shape And Spacing

Use an 8dp spacing rhythm with smaller 4dp adjustments where needed.

Shape direction:

- Cards: modest rounded corners.
- Chips and compact labels: pill shapes.
- Buttons: rounded but not bubbly.
- Navigation containers: soft Material 3 shapes.

Avoid large nested cards. Prefer clear surfaces and sections.

## Motion Direction

Motion should be subtle, fast, and purposeful:

- Selection changes can animate.
- Cards can expand/collapse gently.
- Copy/share feedback should use snackbars or small confirmation states.
- Splash should use Android's system splash correctly, then optionally transition into a brief
  in-app motion.

Respect reduced motion.

## Responsive Direction

Mobile portrait:

- Bottom navigation.
- Single-column content.
- Primary actions near the top.

Mobile landscape:

- Use navigation rail when it improves space.
- Avoid cramped text.

Tablet:

- Navigation rail or side navigation.
- Two-column layouts for summary/detail content.

Foldable and large screen:

- Use list-detail and supporting panels where useful.
- Never simply stretch the mobile layout.

## Initial DSM Components

Build only the components needed for the first usable Home screen, then expand as screens require
more.

Initial components:

- `RcvTheme`
- `RcvAppScaffold`
- `RcvNavigationBar`
- `SectionHeader`
- `ProfileHeader`
- `PrimaryActionButton`
- `SecondaryActionButton`
- `SkillChip`
- `FeaturedProjectCard`
- `LinkAction`
- `LoadingState`
- `ErrorState`
- `EmptyState`

## First Screen Direction

Start with Home.

Home should show:

- Rob Gasparian
- Android Developer
- Short professional summary
- Contact, Resume, GitHub, and LinkedIn actions
- Featured project
- Compact skill snapshot
- Subtle "built with modern Android" credibility section

The Home screen should be useful first and impressive second.
