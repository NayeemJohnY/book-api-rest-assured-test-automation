package tests;

import static testUtils.LoggingMatcher.log;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pojos.Book;

/** Test cases for retrieving books via the API. */
public class TS02_GetBook extends BaseTest {

  /** Creates books before running get book tests. */
  @BeforeTest
  public void createBooksBeforeGetBookTest() {
    List<Book> books = new ArrayList<>();
    for (int i = 1; i <= 10; i++) {
      books.add(new Book("Get API Test Book Title " + i, "Get API Test Book Author " + i));
    }
    for (Book book : books) {
      retryRequest(
              () ->
                  RestAssured.given()
                      .auth()
                      .oauth2(USER_AUTH_TOKEN)
                      .contentType(ContentType.JSON)
                      .body(book)
                      .when()
                      .post())
          .then()
          .statusCode(201)
          .body("id", log(logger, Matchers.notNullValue()))
          .body("title", log(logger, Matchers.equalTo(book.getTitle())))
          .body("author", log(logger, Matchers.equalTo(book.getAuthor())));
    }
  }

  /** Should return books for default page 1. */
  @Test
  public void shouldReturnBooksForDefaultPage1() {
    RestAssured.given()
        .when()
        .get()
        .then()
        .statusCode(200)
        .body("book.size()", log(logger, Matchers.greaterThanOrEqualTo(10)));
  }

  /** Should return books by page number. */
  @Test
  public void shouldReturnBooksByPageNumber() {
    RestAssured.given()
        .queryParam("page", "2")
        .when()
        .get()
        .then()
        .statusCode(200)
        .body("id", log(logger, Matchers.hasItem(log(logger, Matchers.greaterThan(10)))));
  }

  /** Should return books by limit. */
  @Test
  public void shouldReturnBooksByLimit() {
    RestAssured.given()
        .queryParam("limit", "5")
        .when()
        .get()
        .then()
        .statusCode(200)
        .body("$.size()", log(logger, Matchers.equalTo(5)));
  }

  /** Should return books by limit and page. */
  @Test
  public void shouldReturnBooksByLimitAndPage() {
    RestAssured.given()
        .queryParam("page", 3)
        .queryParam("limit", 5)
        .when()
        .get()
        .then()
        .statusCode(200)
        .body("id", log(logger, Matchers.hasItem(log(logger, Matchers.greaterThan(10)))));
  }

  /** Should return no books if page number is not in range. */
  @Test
  public void shouldReturnNoBooksIfPageNumberIsNotInRange() {
    RestAssured.given()
        .queryParam("page", "3")
        .when()
        .get()
        .then()
        .statusCode(200)
        .body("$", log(logger, Matchers.empty()));
  }

  /** Should return no books on negative page. */
  @Test
  public void shouldReturnNoBooksOnNegativePage() {
    RestAssured.given()
        .queryParam("page", -3)
        .when()
        .get()
        .then()
        .statusCode(200)
        .body("$", log(logger, Matchers.empty()));
  }

  /** Should return books excluding last limit on negative limit. */
  @Test
  public void shouldReturnBooksExcludingLastLimitOnNegativeLimit() {
    RestAssured.given()
        .queryParam("limit", -3)
        .when()
        .get()
        .then()
        .statusCode(200)
        .body("$", log(logger, Matchers.not(log(logger, Matchers.empty()))));
  }

  /** Should return a single book by ID. */
  @Test
  public void shouldReturnSingleBookByID() {
    RestAssured.given()
        .pathParam("bookId", 10)
        .when()
        .get("/{bookId}")
        .then()
        .statusCode(200)
        .body("id", log(logger, Matchers.equalTo(10)));
  }

  /** Should not return a book when book ID is an invalid string. */
  @Test
  public void shouldNotReturnBookWhenBookIdIsInvalidString() {
    RestAssured.given()
        .pathParam("bookId", "invalid-book-id")
        .when()
        .get("/{bookId}")
        .then()
        .statusCode(404)
        .body("error", log(logger, Matchers.equalTo("Book not found")));
  }

  /** Should not return a book when book ID does not exist. */
  @Test
  public void shouldNotReturnBookWhenBookIdNotExists() {
    RestAssured.given()
        .pathParam("bookId", 112345)
        .when()
        .get("/{bookId}")
        .then()
        .statusCode(404)
        .body("error", log(logger, Matchers.equalTo("Book not found")));
  }

  /** Should return all books when book ID is empty. */
  @Test
  public void shouldReturnAllBooksWhenBookIdisEmpty() {
    RestAssured.given()
        .pathParam("bookId", "")
        .when()
        .get("/{bookId}")
        .then()
        .statusCode(200)
        .body("$.size()", log(logger, Matchers.greaterThanOrEqualTo(10)));
  }

  /** Should return books containing the author. */
  @Test
  public void shouldReturnBooksContainsAuthor() {
    RestAssured.given()
        .queryParam("author", "Book Author")
        .when()
        .get("/search")
        .then()
        .statusCode(200)
        .body("author", log(logger, Matchers.hasItem(log(logger, Matchers.containsString("Book Author")))));
  }

  /** Should return books containing the title. */
  @Test
  public void shouldReturnBooksContainsTitle() {
    RestAssured.given()
        .queryParam("title", "Book Title")
        .when()
        .get("/search")
        .then()
        .statusCode(200)
        .body("title", log(logger, Matchers.hasItem(log(logger, Matchers.containsString("Book Title")))));
  }

  /** Should return books containing both title and author. */
  @Test
  public void shouldReturnBooksContainsTitleAndAuthor() {
    RestAssured.given()
        .queryParam("title", "Book Title")
        .queryParam("author", "book author")
        .when()
        .get("/search")
        .then()
        .statusCode(200)
        .body("title", log(logger, Matchers.hasItem(log(logger, Matchers.containsString("Book Title")))))
        .body("author", log(logger, Matchers.hasItem(log(logger, Matchers.containsStringIgnoringCase("book author")))));
  }

  /** Should return a single book with title and author. */
  @Test
  public void shouldReturnSingleBookWithTitleAndAuthor() {
    RestAssured.given()
        .queryParam("title", "get api test book title 10")
        .queryParam("author", "author 10")
        .when()
        .get("/search")
        .then()
        .statusCode(200)
        .body("$.size()", log(logger, Matchers.equalTo(1)))
        .body("title", log(logger, Matchers.hasItem(log(logger, Matchers.containsString("Get API Test Book Title 10")))))
        .body("author", log(logger, Matchers.hasItem(log(logger, Matchers.containsStringIgnoringCase("book author 10")))));
  }

  /** Should return 404 when book search is without author and title. */
  @Test
  public void shouldReturn404WhenBookSearchWithoutAuthorAndTitle() {
    RestAssured.given()
        .when()
        .get("/search")
        .then()
        .statusCode(400)
        .body("error", log(logger, Matchers.equalTo("Please provide at least a title or author for search")));
  }

  /** Should return no books when book with title and author does not exist. */
  @Test
  public void shouldReturnNoBooksWhenBookWithTitleAndAuthorNotExists() {
    RestAssured.given()
        .queryParam("title", "Get API Test Book Title 1")
        .queryParam("author", "Get API Test Book Author 2")
        .when()
        .get("/search")
        .then()
        .statusCode(404)
        .body("error", log(logger, Matchers.equalTo("Books not found for search")));
  }

  /** Should return no books when book with author does not exist. */
  @Test
  public void shouldReturnNoBooksWhenBookWithAuthorNotExists() {
    RestAssured.given()
        .queryParam("author", "Get API Test Book Author 222222")
        .when()
        .get("/search")
        .then()
        .statusCode(404)
        .body("error", log(logger, Matchers.equalTo("Books not found for search")));
  }

  /** Should return no books when book with title does not exist. */
  @Test
  public void shouldReturnNoBooksWhenBookWithTitleNotExists() {
    RestAssured.given()
        .queryParam("title", "Get API Test Book Title 11111")
        .when()
        .get("/search")
        .then()
        .statusCode(404)
        .body("error", log(logger, Matchers.equalTo("Books not found for search")));
  }
}
