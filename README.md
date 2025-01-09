# Task Manager API

Task Manager API is a multi-module Spring Boot project designed to provide a robust and secure API for managing tasks. The project is organized into several modules to promote scalability, maintainability, and separation of concerns. It leverages Lombok to reduce boilerplate code and integrates Spring Security for authentication and authorization.

## Table of Contents

- [Project Structure](#project-structure)
- [Modules](#modules)
    - [core](#core)
    - [api](#api)
    - [security](#security)
    - [app](#app)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Building the Project](#building-the-project)
- [Running the Application](#running-the-application)
- [Testing](#testing)
- [Configuration](#configuration)
- [Usage](#usage)
    - [API Endpoints](#api-endpoints)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

## Project Structure

The Task Manager API project is organized into the following Maven modules:

task-manager-api (parent pom)  
├── core  
│ └── src/main/java/dev/core  
├── api  
│ └── src/main/java/dev/api/controller  
├── security  
│ └── src/main/java/dev/security/dto  
│ └── src/main/java/dev/security/service  
└── app  
    └── src/main/java/dev/app  


- **core**: Contains the core functionalities, including models, repositories, and common utilities.
- **api**: Hosts the REST controllers and API-related components.
- **security**: Manages authentication, authorization, and JWT token services.
- **app**: The main Spring Boot application module that ties all other modules together.

## Modules

### core

- **Description**: This module encapsulates the core business logic, entities, repositories, and utility classes.
- **Key Components**:
    - **Entities**: Define the data models (e.g., `Task`, `User`).
    - **Repositories**: Interfaces for data access using Spring Data JPA.
    - **Services**: Business logic implementations.

### api

- **Description**: Contains the REST API controllers and related components.
- **Key Components**:
    - **Controllers**: Handle HTTP requests and responses.
    - **DTOs**: Data Transfer Objects for API communication.

### security

- **Description**: Manages security aspects of the application, including authentication and authorization mechanisms.
- **Key Components**:
    - **DTOs**: Data Transfer Objects for security-related operations (e.g., `LoginDTO`, `TokenDTO`).
    - **Services**: JWT token generation and validation.
    - **Configuration**: Spring Security configurations.

### app

- **Description**: The entry point of the application. This module bootstraps the Spring Boot application.
- **Key Components**:
    - **Main Application Class**: Contains the `main` method to run the application.

## Prerequisites

- **Java Development Kit (JDK) 17**: Ensure that JDK 17 is installed and configured.
- **Maven 3.9.9**: The project uses Maven for build automation.
- **IDE**: IntelliJ IDEA Ultimate (recommended) with Lombok plugin installed.

## Installation

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/yourusername/task-manager-api.git
   cd task-manager-api

2. **Import the Project into Your IDE**:

Open IntelliJ IDEA.  
Select File -> Open.  
Navigate to the cloned repository directory and select the pom.xml file.
Ensure that all modules (core, api, security, app) are imported correctly.

3. **Configure Lombok in Your IDE**:

IntelliJ IDEA:  
Go to File -> Settings -> Plugins.  
Search for Lombok and install the plugin if not already installed.  
Enable annotation processing:  
Navigate to File -> Settings -> Build, Execution, Deployment -> Compiler -> Annotation Processors.  
Check the box Enable annotation processing.  

## Building the Project
To build the entire project, navigate to the root directory (task-manager-api) and run:

```bash
mvn clean install  
```

**This command will**:

Clean any previous builds.
Compile all modules.
Run tests.
Package the modules accordingly.
Install the artifacts into your local Maven repository.

Running the Application
The app module contains the main Spring Boot application. To run the application:

Navigate to the app Module:

bash
Копировать код
cd app
Run the Application Using Maven:

```bash
mvn spring-boot:run
```
Alternatively, you can run the TaskManagerApplication class directly from your IDE.

Access the Application: Once the application starts, it will be accessible at http://localhost:8080.
