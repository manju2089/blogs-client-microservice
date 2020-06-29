# blogs-client-microservice

Eureka Service Set Up:

Registers itself as Eureka server for the client discovery

Git Clone /eureka-service mvn clean install mvn spring-boot:run Navigate to http://127.0.0.1:8761/ - Eureka server is seen running up

Article Client Microservice Setup: (port :9091 -we dont directly use it)

Git clone articles-client-microservice mvn clean install mvn spring-boot:run

Blog Client Microservice Setup: (port:9092)

Git clone blogs-client-microservice mvn clean install mvn spring-boot:run Navigate to http://127.0.0.1:8761/ - two instances should spin up

Execution:

Get Blog - Access the http://localhost:9092/blogs -GET request to fetch all blog information

Create Blog - /blogs - POST with body { "blogName":"blog1" }

Update Blog - /blogs/{id} - PUT with updated blogName { "blogName":"blog2" }

Delete Blog - /blogs/{id} - Delete request with no body

Create Articles - /blogs/{id}/articles- POST to create article for a blogId { "articleName":"article1" }

Fetch Articles for a Blog - /blogs/{id}/articles - GET to see all the articles of a blog

Update Article - /blogs/{id}/articles/{articleId} - PUT request with updated JSON body

Delete Article - /blogs/{id}/articles/{articleId} - Delete request with no body The article linked to a blog is removed and no more seen in fetch request
