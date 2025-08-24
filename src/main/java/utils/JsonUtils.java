package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Utility class for JSON serialization and deserialization. */
public class JsonUtils {
  private static final ObjectMapper objectMapper = new ObjectMapper();
  private static final Logger logger = LogManager.getLogger(JsonUtils.class);

  /**
   * Serializes a Java object to its JSON string representation.
   *
   * @param object the object to serialize
   * @param <T> the type of the object
   * @return the JSON string, or null if serialization fails
   */
  public static <T> String JsonStringify(T object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (IOException e) {
      logger.error("Exception occurred while serializing Java Object ", e);
    }
    return null;
  }

  /**
   * Converts a Map to a JSON string.
   *
   * @param map the Map
   * @return a JSOn String representation of the Map, or an empty string if conversion fails
   */
  public static String mapToJson(Map<String, Object> map) {
    try {
      return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
    } catch (JsonProcessingException e) {
      logger.error("Error occurred while converting Map to JSON", e);
    }
    return "";
  }

  /**
   * Converts a JSON file from the classpath to a Map.
   *
   * @param filename the name of the JSON file in the classpath
   * @return a Map representation of the JSON, or an empty map if conversion fails
   */
  public static Map<String, Object> jsonFileToMap(String filename) {
    try (InputStream inputStream = JsonUtils.class.getClassLoader().getResourceAsStream(filename)) {

      if (inputStream == null) {
        logger.error("Resource not found: {}", filename);
        return Collections.emptyMap();
      }
      return objectMapper.readValue(inputStream, new TypeReference<Map<String, Object>>() {});
    } catch (IOException e) {
      logger.error("Error occurred while accessing Stream", e);
    }
    return Collections.emptyMap();
  }

  /**
   * Reads and converts a JSON file from the classpath to an object of the specified class.
   *
   * @param <T> the type of object to convert to
   * @param filename the name of the JSON file in the classpath
   * @param clazz the class of the target object
   * @param isFile whether the input is a file (unused parameter)
   * @return an instance of the target class, or null if the file is not found or conversion fails
   */
  public static <T> T fromJson(String filename, Class<T> clazz, boolean isFile) {
    InputStream inputStream = JsonUtils.class.getClassLoader().getResourceAsStream(filename);
    if (inputStream == null) {
      logger.error("Resource not found: {}", filename);
      return null;
    }

    try {
      return objectMapper.readValue(inputStream, clazz);
    } catch (IOException e) {
      logger.error("Error occurred while accessing Stream", e);
    }
    return null;
  }
}
