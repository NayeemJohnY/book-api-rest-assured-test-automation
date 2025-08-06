package testUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.annotations.ITestAnnotation;

public class AnnotationTransformer implements IAnnotationTransformer{
    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
       
        Class<? extends IRetryAnalyzer> retryAnalyzer = annotation.getRetryAnalyzerClass();
        if (retryAnalyzer == null || retryAnalyzer.getName().equals("org.testng.internal.annotations.DisabledRetryAnalyzer")){
            annotation.setRetryAnalyzer(RetryAnalyzer.class);
        }
    }
}
