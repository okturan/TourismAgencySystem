# Security policy

## Supported code

Security fixes apply to the current `master` branch. This is a local learning application rather than a hosted service, and no historical release line is maintained.

The main trust boundaries are:

- PostgreSQL connection values supplied through the environment or Java system properties
- administrator provisioning and role-based login
- versioned PBKDF2 password hashes and legacy-password migration
- JDBC queries and updates across users, hotels, rooms, pricing, and reservations
- the sanitized fresh-install schema in `database/schema.sql`

The repository does not ship default application credentials. The current schema creates no users, hotels, rooms, reservations, prices, seasons, or customer records.

## Reporting a vulnerability

Use GitHub's **Report a vulnerability** form in the repository Security tab. Please do not open a public issue for a suspected vulnerability.

Include the affected path, the behavior you observed, reproduction steps using synthetic data, and the security impact. Do not include real credentials, personal information, reservation data, or database exports.

You can expect an initial acknowledgement within seven days. Valid reports will be investigated and fixed on the current branch; disclosure timing will be coordinated after a fix is available.

## Scope and limitations

Reports about authentication, password handling, SQL behavior, authorization boundaries, or accidental sensitive-data exposure are useful. Local PostgreSQL administration, operating-system compromise, unsupported third-party modifications, and denial of service against a developer's own machine are outside this project's maintained boundary.

This learning project does not operate a bug-bounty program and cannot offer payment for reports.
