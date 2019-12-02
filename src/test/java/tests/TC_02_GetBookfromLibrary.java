package tests;

import static io.restassured.RestAssured.given;

import java.util.List;

import org.testng.annotations.Test;

import base.APITestBase;
import beans.Book;
import io.restassured.response.Response;
import utils.JavaJsonUtils;;

public class TC_02_GetBookfromLibrary extends APITestBase {

	@Test(priority = 2)
	public void getBookbyAuthorName() {
		try {
			String author = "Nayeemjohn";
			List<String> bookname;
			Response response = given().queryParam("AuthorName", author).get("/GetBook.php");
			response.then().log().ifValidationFails().statusCode(200);
			bookname = response.jsonPath().get("book_name");
			log.info("Books of the Author:" + author + "\n" + bookname);
		} catch (Exception e) {
			log.debug("Exception occured while Executing test:\n" + e.getMessage());
		}
	}

	@Test(priority = 3)
	public void getBookbyID() {
		try {
			Response response = given().queryParam("ID", id).log().ifValidationFails().get("/GetBook.php");
			response.then().log().ifValidationFails().statusCode(200);
			List<Book> books = JavaJsonUtils.deSrializeJsontoJavaBook(response);
			for (Book book1 : books) {
				log.info("Below are the details of the book of ID:" + id);
				log.info("ISBN of Book : " + book1.getIsbn() + " Name of Book: " + book1.getBook_name()
						+ " Author of Book " + book1.getAuthor() + " Pages of Book " + book1.getAisle());
			}
		} catch (Exception e) {
			log.debug("Exception occured while Executing test:\n" + e.getMessage());
		}
	}
}
