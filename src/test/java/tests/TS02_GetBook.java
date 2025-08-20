package tests;

import static testUtils.LoggingMatcher.log;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pojos.Book;

/** Test cases for retrieving books via the API. */
@Epic("Book Management")
@Feature("Retrieve Book")
@Severity(SeverityLevel.CRITICAL)
public class TS02_GetBook extends BaseTest {

  /** Creates books before running get book tests. */
  @BeforeTest(alwaysRun = true)
  @Description("Creates a set of books before running get book tests to ensure data is available.")
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
  @Test(groups = {"smoke", "regression"})
  @Description("Retrieves books for default page 1 and verifies at least 10 books are returned.")
  public void shouldReturnBooksForDefaultPage1() {
    RestAssured.given()
        .when()
        .get()
        .then()
        .statusCode(200)
        .body("book.size()", log(logger, Matchers.greaterThanOrEqualTo(10)));
  }

  /** Should return books by page number. */
  @Test(groups = {"regression"})
  @Description("Retrieves books by page number and verifies correct books are returned.")
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
  @Test(groups = {"regression"})
  @Description(
      "Retrieves books by limit and verifies the number of books returned matches the limit.")
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
  @Test(groups = {"regression"})
  @Description("Retrieves books by limit and page and verifies correct books are returned.")
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
  @Test(groups = {"negative", "regression"})
  @Description(
      "Attempts to retrieve books with an out-of-range page number and expects no books returned.")
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
  @Test(groups = {"negative", "regression"})
  @Description(
      "Attempts to retrieve books with a negative page number and expects no books returned.")
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
  @Test(groups = {"negative", "regression"})
  @Description("Retrieves books with a negative limit and verifies books are still returned.")
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
  @Test(groups = {"smoke", "regression"})
  @Description("Retrieves a single book by ID and verifies the correct book is returned.")
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
  @Test(groups = {"negative", "regression"})
  @Description("Attempts to retrieve a book with an invalid string ID and expects a 404 error.")
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
  @Test(groups = {"negative", "regression"})
  @Description("Attempts to retrieve a book with a non-existent ID and expects a 404 error.")
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
  @Test(groups = {"regression"})
  @Description(
      "Retrieves all books when book ID is empty and verifies the response contains books.")
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
  @Test(groups = {"regression"})
  @Description(
      "Searches for books by author and verifies books containing the author are returned.")
  public void shouldReturnBooksContainsAuthor() {
    RestAssured.given()
        .queryParam("author", "Book Author")
        .when()
        .get("/search")
        .then()
        .statusCode(200)
        .body(
            "author",
            log(logger, Matchers.hasItem(log(logger, Matchers.containsString("Book Author")))));
  }

  /** Should return books containing the title. */
  @Test(groups = {"regression"})
  @Description("Searches for books by title and verifies books containing the title are returned.")
  public void shouldReturnBooksContainsTitle() {
    RestAssured.given()
        .queryParam("title", "Book Title")
        .when()
        .get("/search")
        .then()
        .statusCode(200)
        .body(
            "title",
            log(logger, Matchers.hasItem(log(logger, Matchers.containsString("Book Title")))));
  }

  /** Should return books containing both title and author. */
  @Test(groups = {"regression"})
  @Description(
      "Searches for books by both title and author and verifies matching books are returned.")
  public void shouldReturnBooksContainsTitleAndAuthor() {
    RestAssured.given()
        .queryParam("title", "Book Title")
        .queryParam("author", "book author")
        .when()
        .get("/search")
        .then()
        .statusCode(200)
        .body(
            "title",
            log(logger, Matchers.hasItem(log(logger, Matchers.containsString("Book Title")))))
        .body(
            "author",
            log(
                logger,
                Matchers.hasItem(log(logger, Matchers.containsStringIgnoringCase("book author")))));
  }

  /** Should return a single book with title and author. */
  @Test(groups = {"regression"})
  @Description(
      "Searches for a single book by title and author and verifies only one book is returned.")
  public void shouldReturnSingleBookWithTitleAndAuthor() {
    RestAssured.given()
        .queryParam("title", "get api test book title 10")
        .queryParam("author", "author 10")
        .when()
        .get("/search")
        .then()
        .statusCode(200)
        .body("$.size()", log(logger, Matchers.equalTo(1)))
        .body(
            "title",
            log(
                logger,
                Matchers.hasItem(
                    log(logger, Matchers.containsString("Get API Test Book Title 10")))))
        .body(
            "author",
            log(
                logger,
                Matchers.hasItem(
                    log(logger, Matchers.containsStringIgnoringCase("book author 10")))));
  }

  /** Should return 404 when book search is without author and title. */
  @Test(groups = {"negative", "regression"})
  @Description("Attempts to search for books without author and title and expects a 400 error.")
  public void shouldReturn404WhenBookSearchWithoutAuthorAndTitle() {
    RestAssured.given()
        .when()
        .get("/search")
        .then()
        .statusCode(400)
        .body(
            "error",
            log(logger, Matchers.equalTo("Please provide at least a title or author for search")));
  }

  /** Should return no books when book with title and author does not exist. */
  @Test(groups = {"negative", "regression"})
  @Description("Searches for a book with a non-existent title and author and expects a 404 error.")
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
  @Test(groups = {"negative", "regression"})
  @Description("Searches for a book with a non-existent author and expects a 404 error.")
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
  @Test(groups = {"negative", "regression"})
  @Description("Searches for a book with a non-existent title and expects a 404 error.")
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
