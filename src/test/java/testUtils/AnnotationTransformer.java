package testUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.annotations.ITestAnnotation;

/** TestNG annotation transformer to set a custom retry analyzer. */
public class AnnotationTransformer implements IAnnotationTransformer {
  /**
   * Sets the custom retry analyzer for TestNG tests if not already set.
   *
   * @param annotation the test annotation
   * @param testClass the test class
   * @param testConstructor the test constructor
   * @param testMethod the test method
   */
  @Override
  public void transform(
      ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
    Class<? extends IRetryAnalyzer> retryAnalyzer = annotation.getRetryAnalyzerClass();
    if (retryAnalyzer == null
        || retryAnalyzer
            .getName()
            .equals("org.testng.internal.annotations.DisabledRetryAnalyzer")) {
      annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }
  }
}
