package tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.annotations.Test;
import pojos.Book;

public class TS03_UpdateBook extends BaseTest {

  @Test
  public void shouldUpdateBookAuthor() {
    RestAssured.given()
        .contentType(ContentType.JSON)
        .auth()
        .oauth2(USER_AUTH_TOKEN)
        .pathParam("bookId", 1)
        .body("{\"author\": \"Updated Author Name\"}")
        .when()
        .put("/{bookId}")
        .then()
        .log()
        .ifValidationFails()
        .statusCode(200)
        .body("id", Matchers.equalTo(1))
        .body("author", Matchers.equalTo("Updated Author Name"));
  }

  @Test
  public void shouldUpdateBookTitle() {
    RestAssured.given()
        .contentType(ContentType.JSON)
        .auth()
        .oauth2(USER_AUTH_TOKEN)
        .pathParam("bookId", 1)
        .body("{\"title\": \"Updated Book Title\"}")
        .when()
        .put("/{bookId}")
        .then()
        .log()
        .ifValidationFails()
        .statusCode(200)
        .body("id", Matchers.equalTo(1))
        .body("author", Matchers.equalTo("Updated Author Name"));
  }

  @Test
  public void shouldReturn401WhenNoAuthIsProvidedOnUpdateBook() {
    RestAssured.given()
        .contentType(ContentType.JSON)
        .pathParam("bookId", 1)
        .when()
        .put("/{bookId}")
        .then()
        .log()
        .ifValidationFails()
        .statusCode(401)
        .body("error", Matchers.equalTo("Unauthorized. No token provided."));
  }

  @Test
  public void shouldReturn404WhenBookWithIdIsNotExists() {
    RestAssured.given()
        .contentType(ContentType.JSON)
        .auth()
        .oauth2(USER_AUTH_TOKEN)
        .pathParam("bookId", 890761)
        .when()
        .put("/{bookId}")
        .then()
        .log()
        .ifValidationFails()
        .statusCode(404)
        .body("error", Matchers.equalTo("Book not found"));
  }

  @Test
  public void shouldReturn400WhenDifferentBookIdIsGivenInBody() {
    RestAssured.given()
        .contentType(ContentType.JSON)
        .auth()
        .oauth2(USER_AUTH_TOKEN)
        .pathParam("bookId", 1)
        .body("{\"author\": \"Updated Author Name\", \"id\": 345}")
        .when()
        .put("/{bookId}")
        .then()
        .log()
        .ifValidationFails()
        .statusCode(400)
        .body("error", Matchers.equalTo("Updating book ID is not allowed."));
  }

  @Test
  public void shouldUpdateBookWhenSameBookIdIsGivenInBody() {
    Book book = new Book(1, "Updated Book Title", "Updated Author Name");
    Book responseBook =
        RestAssured.given()
            .contentType(ContentType.JSON)
            .auth()
            .oauth2(USER_AUTH_TOKEN)
            .pathParam("bookId", 1)
            .body(book)
            .when()
            .put("/{bookId}")
            .then()
            .log()
            .ifValidationFails()
            .statusCode(200)
            .extract()
            .as(Book.class);
    Assert.assertEquals(book.getId(), responseBook.getId());
    Assert.assertEquals(book.getAuthor(), responseBook.getAuthor());
    Assert.assertEquals(book.getTitle(), responseBook.getTitle());
  }
}
