# Chipotle

##  Overview
This is a Spring Boot-based REST API for creating and serving orders for a fake Chipotle restaurant. 
The key idea for this project is to get used to the software development process (design + implementation) and gain
experience with the Spring Boot framework. The application provides a REST API for user interaction such as placing orders,
viewing the available ingredients and payment transactions. A database layer is used for storage purposes and events are
used for notifications between the different domain objects. For the time being, the application is implemented as a 
monolithic application and has some basic HTTP security built into it to restrict access to protected URL paths of the 
application.

## Key Functional Components
1. RawIngredient
2. PreppedIngredient
3. MealIngredient
4. Order
5. Payment
6. Chef
7. SousChef
8. Transaction

## Key Infrastructure Components
1. Application
2. Database
3. Event Management
4. Security

## Application Design
In this section, the interaction between different functional components with each other and the layering of the 
application is detailed. Specific attention is drawn to the Security and Event Management components of the application. 

## Typical Workflows
1. Normal Order Placement
2. Order Placement with insufficient Ingredients
3. Interaction between Order and Chef
4. Interaction between Chef and Sous Chef

## FAQs
1. How do I view the API specs?
    
   We use Swagger to detail all the specifications required to interact with the API: http://localhost:8080/swagger-ui.html#/
   
2. How do I run the application on my local machine?
   
   If you would like to run the entire application on the local machine including the database and event management 
   components, the local-dev branch of this application should be cloned from GitHub. All the files needed to run the 
   application locally are located in the run/ directory. The run/ directory contains the following:
   - **Dockerfile** - used for building the image containing all the tools necessary to run the application.
   - **docker-compose.yml** - used to setup the volumes and network settings needed to run the application. 
   - **app/** - this directory contains the JAR file of the application (the JAR needs to be named `chipotle.jar`). 
   - **chipotle_db/** - this directory contains the mongodump with some sample data.
   - **entryscript.sh** - the script that will be executed at ENTRYPOINT of the Dockerfile.  
   - **jar-build.sh** - builds the jar file of the application and places it into the app/ directory.
   
   Steps to run the application:
   1. cd into run/ directory
   2. run **./jar-build.sh** -- creates the JAR file and places it in app/ as `chipotle.jar`
   3. run **docker-compose up** -- builds the Docker image on the first run; subsequent runs use the built image to run the application.
   4. **hit Ctrl-C** and run **docker-compose down --rmi all** -- stops the container from running and removes all containers and images.
   
   Alternatively, a pre-built image is located on DockerHub at `ramcharans/chipotle:local-dev`. However, this image may not be the most 
   updated version and may lead to errors. The steps previously outlined is the recommended way to run the application.  
   
   If you would like to run only the application on the location but use the remaining components (DB + Events) from 
   AWS, then please follow the following steps: <the URL of the running application>
  
3. Which Java version should be used?
   
   At least Java 8 should be used since Java 8 specific features are used throughout the application. Later versions 
   of Java may work but we leave it to the user to figure out if any problems arise. Versions of other tools/frameworks 
   are located in the pom.xml file.  