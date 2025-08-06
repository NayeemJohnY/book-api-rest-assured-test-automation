package tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pojos.Book;

public class TS02_GetBook extends BaseTest {

  @BeforeTest
  public void createBooksBeforeGetBookTest() {
    List<Book> books = new ArrayList<>();
    for (int i = 1; i <= 10; i++) {
      books.add(new Book("Get API Test Book Title " + i, "Get API Test Book Author " + i));
    }
    for (Book book : books) {
      RestAssured.given()
          .auth()
          .oauth2(USER_AUTH_TOKEN)
          .contentType(ContentType.JSON)
          .body(book)
          .when()
          .post()
          .then()
          .statusCode(201)
          .body("id", Matchers.notNullValue())
          .body("title", Matchers.equalTo(book.getTitle()))
          .body("author", Matchers.equalTo(book.getAuthor()));
    }
  }

  @Test
  public void shouldReturnBooksForDefaultPage1() {
    RestAssured.given()
        .when()
        .get()
        .then()
        .statusCode(200)
        .body("book.size()", Matchers.greaterThanOrEqualTo(10));
  }

  @Test
  public void shouldReturnBooksByPageNumber() {
    RestAssured.given()
        .queryParam("page", "2")
        .when()
        .get()
        .then()
        .statusCode(200)
        .body("id", Matchers.hasItem(Matchers.greaterThan(10)));
  }

  @Test
  public void shouldReturnBooksByLimit() {
    RestAssured.given()
        .queryParam("limit", "5")
        .when()
        .get()
        .then()
        .statusCode(200)
        .body("$.size()", Matchers.equalTo(5));
  }

  @Test
  public void shouldReturnBooksByLimitAndPage() {
    RestAssured.given()
        .queryParam("page", 3)
        .queryParam("limit", 5)
        .when()
        .get()
        .then()
        .statusCode(200)
        .body("id", Matchers.hasItem(Matchers.greaterThan(10)));
  }

  @Test
  public void shouldReturnNoBooksIfPageNumberIsNotInRange() {
    RestAssured.given()
        .queryParam("page", "3")
        .when()
        .get()
        .then()
        .statusCode(200)
        .body("$", Matchers.empty());
  }

  @Test
  public void shouldReturnNoBooksOnNegativePage() {
    RestAssured.given()
        .queryParam("page", -3)
        .when()
        .get()
        .then()
        .statusCode(200)
        .body("$", Matchers.empty());
  }

  @Test
  public void shouldReturnBooksExcludingLastLimitOnNegativeLimit() {
    RestAssured.given()
        .queryParam("limit", -3)
        .when()
        .get()
        .then()
        .statusCode(200)
        .body("$", Matchers.not(Matchers.empty()));
  }

  @Test
  public void shouldReturnSingleBookByID() {
    RestAssured.given()
        .pathParam("bookId", 10)
        .when()
        .get("/{bookId}")
        .then()
        .statusCode(200)
        .body("id", Matchers.equalTo(10));
  }

  @Test
  public void shouldNotReturnBookWhenBookIdIsInvalidString() {
    RestAssured.given()
        .pathParam("bookId", "invalid-book-id")
        .when()
        .get("/{bookId}")
        .then()
        .statusCode(404)
        .body("error", Matchers.equalTo("Book not found"));
  }

  @Test
  public void shouldNotReturnBookWhenBookIdNotExists() {
    RestAssured.given()
        .pathParam("bookId", 112345)
        .when()
        .get("/{bookId}")
        .then()
        .statusCode(404)
        .body("error", Matchers.equalTo("Book not found"));
  }

  @Test
  public void shouldReturnAllBooksWhenBookIdisEmpty() {
    RestAssured.given()
        .pathParam("bookId", "")
        .when()
        .get("/{bookId}")
        .then()
        .statusCode(200)
        .body("$.size()", Matchers.greaterThanOrEqualTo(10));
  }

  @Test
  public void shouldReturnBooksContainsAuthor() {
    RestAssured.given()
        .queryParam("author", "Book Author")
        .when()
        .get("/search")
        .then()
        .statusCode(200)
        .body("author", Matchers.hasItem(Matchers.containsString("Book Author")));
  }

  @Test
  public void shouldReturnBooksContainsTitle() {
    RestAssured.given()
        .queryParam("title", "Book Title")
        .when()
        .get("/search")
        .then()
        .statusCode(200)
        .body("title", Matchers.hasItem(Matchers.containsString("Book Title")));
  }

  @Test
  public void shouldReturnBooksContainsTitleAndAuthor() {
    RestAssured.given()
        .queryParam("title", "Book Title")
        .queryParam("author", "book author")
        .when()
        .get("/search")
        .then()
        .statusCode(200)
        .body("title", Matchers.hasItem(Matchers.containsString("Book Title")))
        .body("author", Matchers.hasItem(Matchers.containsStringIgnoringCase("book author")));
  }

  @Test
  public void shouldReturnSingleBookWithTitleAndAuthor() {
    RestAssured.given()
        .queryParam("title", "get api test book title 10")
        .queryParam("author", "author 10")
        .when()
        .get("/search")
        .then()
        .statusCode(200)
        .body("$.size()", Matchers.equalTo(1))
        .body("title", Matchers.hasItem(Matchers.containsString("Get API Test Book Title 10")))
        .body("author", Matchers.hasItem(Matchers.containsStringIgnoringCase("book author 10")));
  }

  @Test
  public void shouldReturn404WhenBookSearchWithoutAuthorAndTitle() {
    RestAssured.given()
        .when()
        .get("/search")
        .then()
        .statusCode(400)
        .body("error", Matchers.equalTo("Please provide at least a title or author for search"));
  }

  @Test
  public void shouldReturnNoBooksWhenBookWithTitleAndAuthorNotExists() {
    RestAssured.given()
        .queryParam("title", "Get API Test Book Title 1")
        .queryParam("author", "Get API Test Book Author 2")
        .when()
        .get("/search")
        .then()
        .statusCode(404)
        .body("error", Matchers.equalTo("Books not found for search"));
  }

  @Test
  public void shouldReturnNoBooksWhenBookWithAuthorNotExists() {
    RestAssured.given()
        .queryParam("author", "Get API Test Book Author 222222")
        .when()
        .get("/search")
        .then()
        .statusCode(404)
        .body("error", Matchers.equalTo("Books not found for search"));
  }

  @Test
  public void shouldReturnNoBooksWhenBookWithTitleNotExists() {
    RestAssured.given()
        .queryParam("title", "Get API Test Book Title 11111")
        .when()
        .get("/search")
        .then()
        .statusCode(404)
        .body("error", Matchers.equalTo("Books not found for search"));
  }
}
