# Hotel Management System

This project is a comprehensive Hotel Management System built in Java utilizing Swing for the GUI and PostgreSQL for the database. The application enables administrators and staff to manage hotels, rooms, and reservations effectively.

## Features

### For Admins:
- **User Management**: Admins can manage other users' accounts and roles without exposing stored password hashes.
- **Hotel Management**: Admins can add new hotels, edit hotel details, manage seasons and amenities.

### For Staff:
- **Room Management**: Staff can manage rooms in hotels, including adding new rooms, updating room details, and managing room prices.
- **Reservation Management**: Staff can make reservations, and view, edit, and delete existing reservations.
- **Search Functionality**: Staff can search for rooms based on various filters, such as city, country, hotel name, guest count, and date range.

## Requirements
- Java 14 or newer
- PostgreSQL database and client tools (`createuser`, `createdb`, and `psql`)

## Fresh Database Setup

`tourism.sql` is reviewable plain SQL for a newly created empty database. It contains the complete schema and only the fixed board-type and amenity lookups used by the UI—no users, hotels, rooms, reservations, prices, seasons, or customer records.

Create a dedicated application owner on the same host and port used in `.env.example`. Run the first two commands with an existing local PostgreSQL administrator; the tools prompt for passwords instead of placing them in shell history.

```sh
createuser --host=localhost --port=5433 --username=postgres \
  --pwprompt tourism_app
createdb --host=localhost --port=5433 --username=postgres \
  --owner=tourism_app tourism_agency
psql --host=localhost --port=5433 --username=tourism_app \
  --dbname=tourism_agency --set=ON_ERROR_STOP=1 --file=tourism.sql
```

The schema is executed while connected as `tourism_app`, so that role owns its tables and sequences and needs no superuser runtime access or follow-up grants.

Copy the environment template and use the same password entered for `tourism_app`:

```sh
cp .env.example .env
# Fill in TOURISM_DB_PASSWORD and adjust the URL/user if needed.
set -a
source .env
set +a
```

The application requires `TOURISM_DB_URL`, `TOURISM_DB_USER`, and `TOURISM_DB_PASSWORD`. IntelliJ run configurations can instead set `tourism.db.url`, `tourism.db.user`, and `tourism.db.password` as Java system properties. When both forms are present, environment variables take precedence over `-D` properties. Neither `.env` nor IDE run configurations should be committed.

## Build and Run

The following commands target macOS/Linux:

```sh
mkdir -p bin
find src -name '*.java' -print0 | xargs -0 javac --release 14 -cp "postgresql-42.7.3.jar:LGoodDatePicker-11.2.1.jar" -d bin
```

Provision the first administrator from an interactive terminal. The password is read without echo, must be 12–256 characters, and is stored only as a salted PBKDF2-HMAC-SHA256 hash:

```sh
java -cp "bin:postgresql-42.7.3.jar:LGoodDatePicker-11.2.1.jar" App --provision-admin
```

Then launch the desktop application normally:

```sh
java -cp "bin:postgresql-42.7.3.jar:LGoodDatePicker-11.2.1.jar" App
```

Normal startup refuses to open the login screen until an administrator exists. There are no default application credentials.

Password hashes use a unique 16-byte salt, a 256-bit key, and 600,000 PBKDF2-HMAC-SHA256 iterations, matching the [OWASP Password Storage Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Password_Storage_Cheat_Sheet.html) recommendation for PBKDF2-HMAC-SHA256.

## Existing Database Migration

Do not run the fresh-install `tourism.sql` against an existing database. Existing installations keep their current schema. On a successful login, a legacy plaintext password is compared without logging and conditionally replaced with a versioned PBKDF2 hash; the conditional update refuses to overwrite a concurrent password change. New and changed passwords are always hashed before persistence.

The former custom-format database archive was removed from the current branch because it contained plaintext user passwords and reservation-shaped personal data. Those bytes remain in earlier Git history; this change does not claim a destructive history rewrite or credential rotation.

## Verification

Compile the full source and regression tests, then run the pure security checks:

```sh
mkdir -p /tmp/tourism-agency-tests
find src test -name '*.java' -print0 | xargs -0 javac --release 14 \
  -cp "postgresql-42.7.3.jar:LGoodDatePicker-11.2.1.jar" \
  -d /tmp/tourism-agency-tests
java -ea -cp "/tmp/tourism-agency-tests:postgresql-42.7.3.jar:LGoodDatePicker-11.2.1.jar" security.PasswordHasherTest
java -ea -cp "/tmp/tourism-agency-tests:postgresql-42.7.3.jar:LGoodDatePicker-11.2.1.jar" business.AppUserPresentationTest
java -ea -cp "/tmp/tourism-agency-tests:postgresql-42.7.3.jar:LGoodDatePicker-11.2.1.jar" core.DatabaseConfigTest
```

## Libraries Used
- **Swing**: For GUI components.
- **LGoodDatePicker**: For date picker components in the user interface.
- **JDBC**: For database connectivity.

## Project Structure
- `core`: Contains core functionality such as database management and helper utility functions.
- `dao`: Contains Data Access Objects (DAOs) that handle database operations.
- `entity`: Contains entity classes representing the business objects (e.g., `Hotel`, `Room`, `Reservation`, `AppUser`).
- `business`: Contains manager classes that handle business logic.
- `security`: Contains password policy, hashing, and verification.
- `tools`: Contains the interactive first-administrator provisioner.
- `view`: Contains the GUI components, further divided into different panels and views.

## Usage
1. **Login**:
    - Provision the first administrator, then launch the application and sign in with that account.

2. **Admin Functionalities**:
    - Upon successful login as an admin, you can manage users and hotels from the admin dashboard.

3. **Staff Functionalities**:
    - Upon successful login as a staff member, you can manage rooms and reservations, search for rooms, and make new reservations from the staff dashboard.
