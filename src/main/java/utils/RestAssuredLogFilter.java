package utils;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestResult;
import org.testng.Reporter;

/** A RestAssured filter for logging HTTP requests and responses during tests. */
public class RestAssuredLogFilter implements Filter {

  private static final Logger logger = LogManager.getLogger(RestAssuredLogFilter.class);

  /**
   * Filters and logs HTTP requests and responses for RestAssured.
   *
   * @param requestSpec the request specification
   * @param responseSpec the response specification
   * @param filterContext the filter context
   * @return the HTTP response
   */
  @Override
  public Response filter(
      FilterableRequestSpecification requestSpec,
      FilterableResponseSpecification responseSpec,
      FilterContext filterContext) {

    String requestBody =
        requestSpec.getBody() == null ? "No Payload" : requestSpec.getBody().toString();

    logger.info("Request: {} {}", requestSpec.getMethod(), requestSpec.getURI());
    logger.debug("Request Headers: {}", requestSpec.getHeaders());
    logger.debug("Request Body: {}", requestBody);

    Response response = filterContext.next(requestSpec, responseSpec);

    logger.info("Response StatusCode: {}", response.getStatusCode());
    logger.debug("Response Headers: {}", response.getHeaders());
    logger.debug("Response Body: {}", response.getBody().asPrettyString());
    response.then().log().ifValidationFails();

    ITestResult result = Reporter.getCurrentTestResult();
    if (result != null) {
      result.setAttribute("statusCode", response.getStatusCode());
      String retryAfter = response.getHeader("Retry-After");
      if (retryAfter != null) {
        result.setAttribute("retryAfter", Integer.parseInt(retryAfter));
      }
    }

    return response;
  }
}
