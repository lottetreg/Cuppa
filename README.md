# HTTP Server

An HTTP 1.0 Server in Java

## Installation

Clone the repo and then build with gradle:

`gradle build`

This places the generated JAR file called `httpserver-1.0-SNAPSHOT.jar` in a `/jar` directory that will be ignored by git.

## Running the Server

To run the server, use the following command from within the root project directory:

`java -jar jar/httpserver-1.0-SNAPSHOT.jar`

## Using the Server

The server runs on port 5000. Once you have it running locally, you can make a GET request with `curl`:

`curl 127.0.0.1:5000`

This GET request currently returns a 200 response status and an empty body.

You can also make a POST request that responds with the request's body:

`curl -d "something" 127.0.0.1:5000`
