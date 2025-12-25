# Spring Boot REST API(Kotlin) for nutritive application. 
- JWT-based authentication (login/signup)
- Creating a new product(if it doesn't exist)
- Saving an existing product for future reference
- Endpoints for creating, updating, reading or deleting allergens, categories, tags, additives, countries, products and nutriments
- The user can also add allergens(the ones he is allergic to) in order to receive notification for warning if the scanned product contains that allergens while using the Android Mobile application.

## Tech Stack
- Language / Framework: Kotlin / Spring Boot
- Database: PostgreSQL on AWS
Endpoint for the db instance: nutritive-app.cnmymqma63x0.eu-north-1.rds.amazonaws.com
- Storage / Buckets: AWS S3 buckets for seed data using dedicated endpoint for the data import.
- Authentication: JWT
- Deployment: AWS (EC2) - Spring Boot aplication as a service.
- MapStruct
<img width="1596" height="748" alt="image" src="https://github.com/user-attachments/assets/bdc45620-bcd0-4fba-bab2-7ec59705bfb8" />

## Firebase Cloud Messaging (FCM) Integration
### Service: FcmService
- The FcmService is responsible for sending notifications to user devices using their device tokens. It authenticates against Google APIs using a service account and obtains an OAuth2 access token to send messages securely.
- Specialized method for warning users if a product they scanned or selected contains allergens.
- Example push notification:
- ⚠️ Allergen Alert
Product Snickers contains: peanuts, lactose

## Architecture
- The project follows Clean Architecture principles to ensure scalability, maintainability, and testability:
- Provides implementations for database access (PostgreSQL), messaging (FCM), and integrations with AWS services (S3, RDS).
- Exposes REST API endpoints to clients (the Android application).
- Dependency Injection
> The project leverages Spring Boot’s built-in Dependency Injection (DI).

## [Postman Testing](https://ws3333-3182.postman.co/workspace/WS-Workspace~1c54f13a-b90a-410f-bfc1-876b5c0badd3/collection/41348129-316e3283-5150-4877-b85c-00094f221f8c?action=share&creator=41348129&active-environment=41348129-567601e0-9947-491f-97b3-4df49b209a7b)
