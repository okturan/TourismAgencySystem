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
- Java 8 or above
- PostgreSQL database

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
    - Upon launching the application, you will see a login screen. Use the credentials assigned to you (there should be an existing admin account to start with).

2. **Admin Functionalities**:
    - Upon successful login as an admin, you can manage users and hotels from the admin dashboard.

3. **Staff Functionalities**:
    - Upon successful login as a staff member, you can manage rooms and reservations, search for rooms, and make new reservations from the staff dashboard.