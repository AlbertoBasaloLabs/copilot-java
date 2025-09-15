---
ID: 'PRACTICE_ACTIVITY-BOOKINGS'
Curso: 'Copilot-Java'
Arquetipo: 'Java-API'
Autor: 'Students'
---
# PRD — Activity Bookings API

## 1. Executive Summary

REST API to manage bookings for tourist activities. The goal is for students to implement a basic CRUD (Create, Read, Update, Delete) and persist data to files (JSON, CSV, or XML). The solution will not require authentication, third-party services, or a database.

### 1.1 Goals and Metrics

  * Build a functional REST API with all endpoints working locally.
  * Ensure the application can be run with `java -jar <jar-name>.jar`.
  * Implement unit and integration tests to validate API behavior.

### 1.2 Audience

  * Students: to gain hands-on experience.
  * Instructors: to use as a guide and assess learning.

### 1.3 Technologies
  * Language: Java 21
  * Libraries: Minimal dependencies, preferably Spring Boot.
  * Tools: Visual Studio Code or IntelliJ IDEA.
  * Deployment: Service runnable on localhost via `java -jar <jar-name>.jar`.

## 2. Scope

  * Included: REST API with a basic CRUD backed by JSON, CSV, or XML files.
  * Excluded: Authentication, payments, notifications, databases.

### 2.1 User Stories

  * As a user, I want to list all offered activities.
  * As a user, I want to see details of a specific activity.
  * As a user, I want to create a new booking for an activity.
  * As a user, I want to see all my bookings.
  * As a user, I want to cancel an existing booking.

### 2.2 Functional Requirements

1. RF-1: `GET /activities` - List all available activities.
2. RF-2: `GET /activities/{id}` - Show details for a specific activity.
3. RF-3: `POST /bookings` - Create a new booking.
4. RF-4: `GET /bookings` - List all existing bookings.
5. RF-5: `DELETE /bookings/{id}` - Cancel a booking.

### 2.3 Additional Functional Requirements

6. RF-6: `GET /activities/{id}/bookings` - List all bookings for a specific activity.
7. RF-7: `PUT /bookings/{id}` - Update an existing booking.
8. RF-8: `GET /activities?status={status}` - Filter activities by status (e.g., `available`, `finished`).

### 2.4 Non-Functional Requirements

  * Simplicity: Prioritize clarity and simplicity in the implementation.
  * Design: API should follow RESTful design with clear, consistent routes.
  * Performance: API responses should be fast, preferably under 1 second.
  * Persistence: Use JSON, CSV, or XML files to store data.
  * Technologies: Use Java 21 and minimal dependencies.

## 3. Data Model and Business Rules

### 3.1 Data Schema

  * Activity:

      * `id` (int)
      * `name` (string)
      * `price` (float)
      * `date` (datetime)
      * `status` (string: `available`, `finished`, `cancelled`, `full`)
      * `capacity` (optional, int): maximum number of bookings.
      * `quorum` (optional, int): minimum number of bookings required for the activity to take place.

  * Booking:

      * `id` (int)
      * `activityId` (int)
      * `customerName` (string)
      * `bookingDate` (datetime)
      * `status` (string: `confirmed`, `cancelled`)

### 3.2 Relationships

  * An Activity can have multiple Bookings.
  * A Booking is associated with a single Activity.

### 3.3 Business Rules

  * Activities with status `finished` or `full` cannot be booked.
  * A booking cannot be cancelled if fewer than 48 hours remain before the activity starts.
  * If an activity reaches its maximum `capacity`, its status should change to `full`.
  * If the `quorum` is not reached 48 hours before the activity, the activity should be cancelled automatically and users notified (simulated via a log).

## 4. Acceptance Criteria and Risks

### 4.1 Acceptance Criteria

  * Each endpoint returns readable JSON data.
  * The API can be executed without errors.
  * Unit and integration tests pass.
  * I/O errors (file read/write) are handled appropriately.

### 4.2 Risks

  * File handling errors (read/write) that could corrupt data.

## 5. Example Data (Seed Data)

### 5.1 Activities

```json
[
  {"id":1, "name":"City Tour", "price":50.0, "date":"2026-07-15T10:00:00", "status":"available", "capacity": 10},
  {"id":2, "name":"Mountain Hike", "price":80.0, "date":"2026-07-20T08:00:00", "status":"available", "quorum": 5},
  {"id":3, "name":"Museum Visit", "price":30.0, "date":"2024-07-18T14:00:00", "status":"finished"}
]
```

```csv
id,name,price,date,status,capacity,quorum
1,City Tour,50.0,2026-07-15T10:00:00,available,10,
2,Mountain Hike,80.0,2026-07-20T08:00:00,available,,5
3,Museum Visit,30.0,2024-07-18T14:00:00,finished,,
```

```xml
<activities>
  <activity>
    <id>1</id>
    <name>City Tour</name>
    <price>50.0</price>
    <date>2026-07-15T10:00:00</date>
    <status>available</status>
    <capacity>10</capacity>
  </activity>
  <activity>
    <id>2</id>
    <name>Mountain Hike</name>
    <price>80.0</price>
    <date>2026-07-20T08:00:00</date>
    <status>available</status>
    <quorum>5</quorum>
  </activity>
  <activity>
    <id>3</id>
    <name>Museum Visit</name>
    <price>30.0</price>
    <date>2024-07-18T14:00:00</date>
    <status>finished</status>
  </activity>
</activities>
```

### 5.2 Bookings

```json
[
  {"id":1, "activityId":1, "customerName":"Juan Pérez", "bookingDate":"2025-06-01T12:00:00", "status":"confirmed"},
  {"id":2, "activityId":2, "customerName":"María Gómez", "bookingDate":"2025-06-02T15:30:00", "status":"confirmed"},
  {"id":3, "activityId":1, "customerName":"Luis Martínez", "bookingDate":"2025-06-03T09:45:00", "status":"cancelled"}
]
```

```csv
id,activityId,customerName,bookingDate,status
1,1,Juan Pérez,2025-06-01T12:00:00,confirmed
2,2,María Gómez,2025-06-02T15:30:00,confirmed
3,1,Luis Martínez,2025-06-03T09:45:00,cancelled
```

```xml  
<bookings>
  <booking>
    <id>1</id>
    <activityId>1</activityId>
    <customerName>Juan Pérez</customerName>
    <bookingDate>2025-06-01T12:00:00</bookingDate>
    <status>confirmed</status>
  </booking>
  <booking>
    <id>2</id>
    <activityId>2</activityId>
    <customerName>María Gómez</customerName>
    <bookingDate>2025-06-02T15:30:00</bookingDate>
    <status>confirmed</status>
  </booking>
  <booking>
    <id>3</id>
    <activityId>1</activityId>
    <customerName>Luis Martínez</customerName>
    <bookingDate>2025-06-03T09:45:00</bookingDate>
    <status>cancelled</status>
  </booking>
</bookings>
```