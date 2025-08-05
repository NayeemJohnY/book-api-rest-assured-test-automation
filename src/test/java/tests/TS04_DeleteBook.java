package tests;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

public class TS04_DeleteBook extends BaseTest {

  protected static final String USER_ADMIN_TOKEN = "admin-token";

  @Test
  public void shouldReturn401WhenNoAuthTokenProvidedOnDelete() {

    RestAssured.given()
        .pathParam("bookId", 1)
        .when()
        .delete("/{bookId}")
        .then()
        .statusCode(401)
        .body("error", Matchers.equalTo("Unauthorized. No token provided."));
  }

  @Test
  public void shouldReturn403WhenUserAuthTokenIsProvidedOnDelete() {
    RestAssured.given()
        .auth()
        .oauth2(USER_AUTH_TOKEN)
        .pathParam("bookId", 1)
        .when()
        .delete("/{bookId}")
        .then()
        .statusCode(403)
        .body("error", Matchers.equalTo("Forbidden. Admin access required."));
  }

  @Test
  public void shouldDeleteBookWhenBookIdIsValid() {
    RestAssured.given()
        .auth()
        .oauth2(USER_ADMIN_TOKEN)
        .pathParam("bookId", 1)
        .when()
        .delete("/{bookId}")
        .then()
        .statusCode(204);
  }

  @Test(dependsOnMethods = "shouldDeleteBookWhenBookIdIsValid")
  public void shouldReturn404WhenBookIsAlreadyDeletedOrNotExists() {
    RestAssured.given()
        .auth()
        .oauth2(USER_ADMIN_TOKEN)
        .pathParam("bookId", 1)
        .when()
        .delete("/{bookId}")
        .then()
        .statusCode(404)
        .body("error", Matchers.equalTo("Book not found"));
  }
}
