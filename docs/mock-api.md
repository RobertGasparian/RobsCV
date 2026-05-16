# Mock API Responses

The local mock responses live in `core/data/src/main/assets/mock-api/v1`.

These files model future backend endpoints while keeping the app local-first for early development.

## Endpoints

- `GET /v1/profile` -> `profile.json`
- `GET /v1/skills` -> `skills.json`
- `GET /v1/experience` -> `experience.json`
- `GET /v1/education` -> `education.json`
- `GET /v1/milestones` -> `milestones.json`

## Notes

- `profile`, `skills`, and `experience` are close to the source CV structure.
- `education` is intentionally more normalized, with reusable institution records and education items that reference them by id.
- `milestones` stores current learning/professional focus and recent milestones that do not belong to one specific employer or school.
- When the backend is added, Retrofit DTOs can mirror these JSON shapes.
- Room entities do not need to match these responses exactly; map API DTOs into local entities through the data layer.
