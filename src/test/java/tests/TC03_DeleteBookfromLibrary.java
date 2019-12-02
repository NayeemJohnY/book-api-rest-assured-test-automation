package tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.testng.annotations.Test;

import base.APITestBase;

public class TC03_DeleteBookfromLibrary extends APITestBase {

	@Test(priority = 4)
	public void deleteBookfromLibrary() {
		try {
			given().body("{\"ID\":\"" + id + "\"}").delete("/DeleteBook.php").then().log().ifValidationFails()
					.assertThat().statusCode(200).and().body("msg", is("book is successfully deleted"));
			log.info("Book with id: " + id + " is Deleted Successfully");
		} catch (Exception e) {
			log.debug("Exception occured while Executing test:\n" + e.getMessage());
		}
	}
}
