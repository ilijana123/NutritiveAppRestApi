Spring Boot REST API(Kotlin) for nutritive application. It includes JWT-based authentication (login/signup), features like creating a new product(if it doesn't exist), saving an existing product for future reference, endpoints for creating, updating, reading or deleting allergens, categories, tags, additives, countries, products and nutriments. The user can also add allergens(the ones he is allergic to) in order to receive notification for warning if the scanned product contains that allergens while using the Android Mobile application.

Tech Stack

Language / Framework: Kotlin / Spring Boot

Database: PostgreSQL on AWS
Endpoint for the db instance: nutritive-app.cnmymqma63x0.eu-north-1.rds.amazonaws.com
<img width="1596" height="748" alt="image" src="https://github.com/user-attachments/assets/bdc45620-bcd0-4fba-bab2-7ec59705bfb8" />

Storage / Buckets: AWS S3 buckets for seed data using dedicated endpoint for the data import.

Authentication: JWT

Deployment: AWS (EC2)
Spring Boot aplication as a service.

MapStruct is used to simplify the mapping process between Java bean types.

🔔 Firebase Cloud Messaging (FCM) Integration

The project integrates with Firebase Cloud Messaging (FCM) to deliver real-time push notifications to client devices.

Service: FcmService

The FcmService is responsible for sending notifications to user devices using their device tokens. It authenticates against Google APIs using a service account and obtains an OAuth2 access token to send messages securely.

Key Features

Send Notifications

sendNotification(targetToken, title, body)
Sends a generic push notification to a given device token.

Send Allergen Alerts

sendAllergenAlert(deviceToken, productName, allergens)
Specialized method for warning users if a product they scanned or selected contains allergens.
Example push notification:

⚠️ Allergen Alert
Product Snickers contains: peanuts, lactose


Authentication
Uses GoogleCredentials.getApplicationDefault() with the scope
https://www.googleapis.com/auth/firebase.messaging
to obtain a valid Bearer token before sending requests.

HTTP Client
Uses OkHttpClient to post requests to the FCM REST endpoint:

https://fcm.googleapis.com/v1/projects/nutritiveapp/messages:send

Architecture

The project follows Clean Architecture principles to ensure scalability, maintainability, and testability:

Domain Layer
Contains core business logic and models.
Independent of frameworks, databases, or external services.

Application Layer
Coordinates use cases.
Defines service interfaces and orchestrates workflows.

Infrastructure Layer
Provides implementations for database access (PostgreSQL), messaging (FCM), and integrations with AWS services (S3, RDS).

Presentation Layer
Exposes REST API endpoints to clients (the Android application).

Dependency Injection

The project leverages Spring Boot’s built-in Dependency Injection (DI).
All services, repositories, and controllers are managed as Spring beans.
This allows easy mocking in tests and clean separation of concerns.
