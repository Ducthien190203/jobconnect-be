# Repository Guidelines

These notes align contributors on how to evolve the JobConnect backend confidently and consistently.

## Project Structure & Module Organization
- Keep runtime source under `src/`, grouping code by feature (`src/modules/job`, `src/modules/applicant`) and isolating shared helpers in `src/common`.
- Infrastructure adapters (database, messaging, external APIs) belong in `src/infra` so they can be stubbed easily in tests.
- Store automated tests alongside code in mirrored folders under `tests/unit` and `tests/integration`; fixture data lives in `tests/fixtures`.
- Versioned schema, seed data, and reusable SQL live in `db/` (e.g., `db/migrations`, `db/seeds`). Secrets never belong in the repo—use `.env.local` for personal overrides.

## Build, Test, and Development Commands
- `npm install` — install backend dependencies (run after any lockfile change).
- `npm run dev` — start the API with hot reload and verbose logging; use this for day-to-day development.
- `npm run build` — emit the production bundle (TypeScript → JavaScript) and surface type errors.
- `npm run test` — execute the Jest/Vitest suite headless; pair with `CI=true npm run test` to mirror pipeline behavior.
- `npm run lint` / `npm run fmt` — enforce ESLint + Prettier before committing; the CI build rejects non-formatted diffs.

## Coding Style & Naming Conventions
- Use TypeScript, ES2022 modules, and 2-space indentation; prefer readonly/const where possible.
- File names are kebab-case (`job-service.ts`); classes use PascalCase, private fields `#camelCase`, DTOs end with `Dto`.
- Keep controller methods thin—delegate to services that encapsulate business rules. Shared response shapes belong in `src/contracts`.
- Run ESLint with the `--max-warnings=0` flag; format code via `npm run fmt` prior to pushing.

## Testing Guidelines
- Mirror the module under test in `tests/unit/<module>/<file>.test.ts`; integration specs go in `tests/integration` with the suffix `.spec.ts`.
- Use Jest/Vitest spies for outbound calls, falling back to in-memory adapters in `tests/doubles`.
- Aim for ≥80% line coverage on changed files; add regression tests whenever a bug is fixed to keep the suite authoritative.

## Commit & Pull Request Guidelines
- Follow Conventional Commits (`feat: add match endpoint`, `fix: correct pagination bounds`) so changelog tooling stays reliable.
- One feature/fix per branch; branch names use `type/topic` (e.g., `feat/job-matching`).
- PRs must include: summary of the change, testing notes (`npm run test` output), linked issue number, and screenshots for any externally visible contract change (e.g., API docs).
- Request at least one review; wait for green CI before merging via squash to keep history linear.
