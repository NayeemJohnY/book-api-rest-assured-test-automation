package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.Book;

public class JsonUtils {
  private static final Logger logger = LogManager.getLogger(JsonUtils.class);

  public static <T> String JsonStringify(T object) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      return objectMapper.writeValueAsString(object);
    } catch (IOException e) {
      logger.error("Exception occured while serializing Java Object ", e);
    }
    return null;
  }
}
