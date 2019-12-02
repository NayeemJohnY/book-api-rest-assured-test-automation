***API Testing Framework***
In this project, the Library API is taken as example and the Testing Framework is designed.

**Library API Details**
1. AddBook endpoint - To add the New Book in to Library with Name, ISBN, Pages and author details-*POST* Request
2. GetBook Endpoint - Retrieve the book by author name/ID-*GET* Request
3. DeleteBook Endpoint - To delete the Book by ID-*DELETE* Request

**Dependencies used**
1. RestAssured- TO hit API and get the response
2. Jackson - To parse the response and request- Serialization & Deserialization
3. TestNG- unit Testing Framework
4. Log4j2- For Logging the results

*Data Provider* used to add the books into library with different details

Logs were found under *target/logs*
It is Maven Project- SUrefire plugin is set up to launch the *testng.xml* file





