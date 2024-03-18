# Simple-CAR-SHARING-Service

Car-Sharing-Service is a web application designed to solve car rental tasks.
The application is built on Spring Boot and utilizes Java,
adhering to the principles of the REST architectural style.

## Getting Started
* Clone the project repository to your local machine.
* Configure the database connection(your credentials) in the [resources/application.yaml](backend/src/main/resources/application.yaml)  file.
* You need to get a token to create a Telegram-bot https://t.me/BotFather

Local build:
* Build the project: mvn compile
* Run application and visit [Swagger page](http://localhost:8080/swagger-ui/index.htm) for easy testing.

Docker:
* Ensure Docker is installed and running
* Set your credentials in [.env](.env) file
* Build the project using the command: `mvn clean package`
* Run the command: `docker-compose up`
* Visit [Swagger page](http://localhost:8080/swagger-ui/index.htm) for easy testing.

# Stripe Payment Integration with Java and Spring Boot

This repository provides a simple implementation of Stripe payments in a Java and Spring Boot application.
It includes a single microservice dedicated to handling Stripe payments.
The project is built using Maven and demonstrates the integration of the Stripe Java library.

## Prerequisites

Before starting, make sure you have a Stripe Secret Key.
Additionally, Stripe offers a Test mode for developers to simulate transactions without real money.

## Getting Started

Clone the repository:

```git clone git@github.com:YaroslavVoronovskyi/SpringBootStripePaymentDemoApplication.git```

## API Endpoints
### Create Payment
#### Endpoint: /api/v1/stripe/create-payment
##### Method: POST
#### Request Body:

```
{
  "amount": 1000,
  "quantity": 1,
  "currency": "USD",
  "name": "Product Name",
  "successUrl": "http://localhost:3000/success",
  "cancelUrl": "http://localhost:3000/cancel"
}
```
#### Response:
```
{
  "status": "SUCCESS",
  "message": "Payment session created successfully",
  "httpStatus": 200,
  "data": {
    "sessionId": "stripe-session-id",
    "sessionUrl": "checkout-url"
  }
}
```

### Capture Payment
#### Endpoint: /api/v1/stripe/capture-payment
##### Method: GET
#### Request Parameter:
```
sessionId: The ID of the payment session
```
#### Response:
```
{
  "status": "SUCCESS",
  "message": "Payment successfully captured.",
  "httpStatus": 200,
  "data": {
    "sessionId": "stripe-session-id",
    "sessionStatus": "complete",
    "paymentStatus": "paid"
  }
}
```

### 🚀 Used technologies
* Java 17
* Apache Maven
* Spring Boot
* Spring Security
* Hibernate
* MySQL
* Liquibase
* Stripe
* Telegram Bots
* Docker
* Swagger

### 👨‍👩‍👧‍👦 Our team:
_Vasylchuk Vasyl_, _Uliana Chorna_, _Yaroslav Voronovskyi_