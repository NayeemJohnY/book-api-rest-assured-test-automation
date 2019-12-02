package base;

import static io.restassured.RestAssured.baseURI;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

import beans.Book;

public class APITestBase {

	public static String id = "";
	protected static Logger log	= LogManager.getLogger();

	@BeforeSuite
	public void setBaseURI() {
		baseURI = "http://216.10.245.166/Library";
		log.info("Base URI is set to: "+baseURI);
	}

	@DataProvider(name = "TestData_Library")
	public Object[] LibraryBookData() {
		List<Book> bookdetails = new ArrayList<Book>();
		bookdetails.add(new Book("GST", RandomStringUtils.randomAlphanumeric(5), "John", 100));
		bookdetails.add(new Book("Skills", RandomStringUtils.randomAlphanumeric(5), "Nayeem", 50));
		bookdetails.add(new Book("Way", RandomStringUtils.randomAlphanumeric(5), "Vinoth", 150));
		bookdetails.add(new Book("The Hundered", RandomStringUtils.randomAlphanumeric(5), "Willaim", 500));
		return bookdetails.toArray();

	}
}
