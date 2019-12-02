package tests;

import static io.restassured.RestAssured.given;

import org.testng.annotations.Test;

import base.APITestBase;
import beans.Book;
import utils.JavaJsonUtils;

public class TC_01_AddNewBookinLibrary extends APITestBase {


	@Test(priority = 1,dataProvider = "TestData_Library")
	public void addBookinLibrary(Book book) {
		try {
		String payload = JavaJsonUtils.serializeJavatoJson(book).replace("book_name", "name");
		log.debug("Data for Executinng Test:\n"+payload);
		id = given().
				body(payload). // new File("D:\\Users\\njohn\\..Nayeem\\JSON.json"))
				when().post("/Addbook.php").
				then().
				assertThat().log().ifValidationFails().statusCode(200).
				extract().body().jsonPath().get("ID");
		log.info("Book Successfully added in Library with id: "+id);
		}
		catch (Exception e) {
			log.debug("Exception occured while Executing test:\n"+e.getMessage());
	}
	}

}
