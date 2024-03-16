# Simple-CAR-SHARING-Service

Car-Sharing-Service is a web application designed to solve car rental tasks.
The application is built on Spring Boot and utilizes Java,
adhering to the principles of the REST architectural style.

## Getting Started
* Clone the project repository to your local machine.
* Configure the database connection(your credentials) in the [resources/application.yaml](src/main/resources/application.yaml)  file.

Local build:
* Build the project: mvn compile
* Run application and visit [Swagger page](http://localhost:8080/swagger-ui/index.htm) for easy testing.

Docker:
* Ensure Docker is installed and running
* Set your credentials in [.env](../.env) file
* Build the project using the command: `mvn clean package`
* Run the command: `docker-compose up`
* Visit [Swagger page](http://localhost:8080/swagger-ui/index.htm) for easy testing.
