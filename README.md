# gmdb-monolithd - Refactored for production

## Tag line
A monolithic movie database application that is in dire need of modernization.

## Introduction
The GMDB application was supposed to be a prototype, thrown together in a few days in order to give stakeholders a quick peek of what was possible in the world of movie databases.

It's key features are the ability to search, by title, for movies contained in the database, and, for logged in users, the ability to leave reviews about the movies.

## Architecture
The application uses SpringBoot 2.1.3.  There is a single Service class that manages all service level business logic, including access to the MySQL database using JPA.  Each table has its own repository, and all DB access happens in the service class.

The single controller class manages all interactions with the front end ThymeLeaf based templates.  The application allows users to create accounts, manage password resets, and add reviews to movies.  Anyone can access the index page without an account in 
order to search for movies.

## New Feature Backlog
The stakeholder community has requested a number of new features be added to the application including:
- Support for registered users to create lists of movies (e.g. "wish list", "seen", etc.).  Each user can create as many lists as they want, and the lists will be private by default, but will be shareable as well.
- The ability of logged in users to add a star rating to their review.
- A user account management page for updating ScreenName, email address, List visibility and other things about their accounts.
- The addition of an Admin role that will allow administrators to add, update or delete movies (using either a form or CSV file).

Before the architecture committee will allow new features to be added they are requiring that the following changes be made:
- The applicaiton needs to have a modern Javascript based front end (using Angular or React) and a Java Microservices backend.
- Security needs to be fixed so that it uses JWT/Spring Security with hashed passwords stored in a database.
- Users will need to verify that their email address is valid.

## Notes
Test data is contained in the csv files. These are formatted for import using MySQL Workbench


In order to build this project, you must have the Lombok plugin installed to process annotations before compiling the code.

If you are using IDEA, you can find the plugin in the marketplace under Preferences > Plugins
When running from IDEA, you can enable annotation processing under Preferences > Build, Execution, Deployment > Compiler > Annotation Processors by checking the box labeled 'Enable annotation processing'

Alternatively, if you are building from the command line using gradle, add the following two lines under the dependencies section in your build.gradle file in the project:
```
compileOnly 'org.projectlombok:lombok:1.18.6'
annotationProcessor 'org.projectlombok:lombok:1.18.6'
```

## Modifications made to make production ready
* Added test.properties and h2 db for testing.  Implemented in GmdbMonolithApplicationTests
* Added environment vars for database connection
  * DB_HOST
  * DB_PORT
  * DB_USER
  * DB_PASSWORD
* Added Dockerfile to deploy in docker container
* Added Jenkinsfile for automated builds & deployment (CI / CD)
* Added jococo for code coverage 

### To Dockerize
* NOTE: MySql must be running and contain a database named 'gmdb' (can be empty), and if in another container, must be on the same network specified below
1. Build the docker image: ` docker build -t gmdb/monolith . `
1. Run the docker image
```
    docker run -d -p 8080:8080 \
        -e DB_HOST=[db host, or, docker db container name or id] \
        -e DB_PORT=[db port (internal if running in a container)] \
        -e DB_USER=[db username] \
        -e DB_PASSWORD=[db password] \
        --network [name of docker network db is on] \
        --name gmdb-monolith \
        gmdb/monolith
``` 
