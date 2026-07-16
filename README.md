# Hotel Management System

This project is a comprehensive Hotel Management System built in Java utilizing Swing for the GUI and PostgreSQL for the database. The application enables administrators and staff to manage hotels, rooms, and reservations effectively.

## Features

### For Admins:
- **User Management**: Admins can manage other users' accounts (create, edit, delete) and assign roles (admin or staff).
- Demo info: User: **admin** Password: **admin**
- **Hotel Management**: Admins can add new hotels, edit hotel details, manage seasons and amenities.

### For Staff:
- **Room Management**: Staff can manage rooms in hotels, including adding new rooms, updating room details, and managing room prices.
- **Reservation Management**: Staff can make reservations, and view, edit, and delete existing reservations.
- **Search Functionality**: Staff can search for rooms based on various filters, such as city, country, hotel name, guest count, and date range.

## Requirements
- Java 14 or newer
- PostgreSQL database

## Database Setup

Import `tourism.sql`, create a dedicated local database role, and keep its password outside the repository.

```sh
cp .env.example .env
# Fill in TOURISM_DB_PASSWORD and adjust the URL/user if needed.
set -a
source .env
set +a
```

The application requires `TOURISM_DB_URL`, `TOURISM_DB_USER`, and `TOURISM_DB_PASSWORD`. IntelliJ run configurations can instead set `tourism.db.url`, `tourism.db.user`, and `tourism.db.password` as Java system properties. Neither `.env` nor IDE run configurations should be committed.

## Build and Run

The following commands target macOS/Linux:

```sh
mkdir -p bin
find src -name '*.java' -print0 | xargs -0 javac --release 14 -cp "postgresql-42.7.3.jar:LGoodDatePicker-11.2.1.jar" -d bin
java -cp "bin:postgresql-42.7.3.jar:LGoodDatePicker-11.2.1.jar" App
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
- `view`: Contains the GUI components, further divided into different panels and views.

## Usage
1. **Login**:
    - Upon launching the application, you will see a login screen. The sample dump includes `admin` / `admin` for local evaluation only. Replace or remove that account before using the application outside a local development environment, and never expose the sample database to a network.

2. **Admin Functionalities**:
    - Upon successful login as an admin, you can manage users and hotels from the admin dashboard.

3. **Staff Functionalities**:
    - Upon successful login as a staff member, you can manage rooms and reservations, search for rooms, and make new reservations from the staff dashboard.
