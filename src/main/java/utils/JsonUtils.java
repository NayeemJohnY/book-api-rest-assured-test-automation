package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Utility class for JSON serialization and deserialization. */
public class JsonUtils {
  private static final Logger logger = LogManager.getLogger(JsonUtils.class);

  /**
   * Serializes a Java object to its JSON string representation.
   *
   * @param object the object to serialize
   * @param <T> the type of the object
   * @return the JSON string, or null if serialization fails
   */
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
