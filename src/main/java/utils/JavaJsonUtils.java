package utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import base.APITestBase;
import beans.Book;
import io.restassured.response.Response;

public class JavaJsonUtils extends APITestBase{

	public static <T> String serializeJavatoJson(T object) {
		ObjectMapper objectMapper= new ObjectMapper();
		String json = "";
		try {
			json = objectMapper.writeValueAsString(object);
			log.info("The Serialized JSON String is:\n"+json);
		} catch (IOException e) {
			e.printStackTrace();
			log.error("Exception occured while Creating JSON from JAVA Object"+e.getMessage());
		}
		return json;
	}
	
	public static List<Book> deSrializeJsontoJavaBook(Response response) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		List<Book> listofBooks = new ArrayList<Book>();
		try {
			listofBooks = objectMapper.readValue(response.asString(), new TypeReference<List<Book>>() {});
			log.info("The Deserialization is Successfull");
		} catch (IOException e) {
			e.printStackTrace();
			log.error("Exception occured while Creating JAVA Object from JSON"+e.getMessage());
		}
		return listofBooks;
	}
}
