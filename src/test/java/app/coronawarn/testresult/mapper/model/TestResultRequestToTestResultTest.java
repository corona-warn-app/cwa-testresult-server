package app.coronawarn.testresult.mapper.model;

import app.coronawarn.testresult.model.TestResult;
import app.coronawarn.testresult.model.TestResultRequest;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestResultRequestToTestResultTest {

  private Validator validator;
  private TestResultRequestToTestResult mapper;

  @Before
  public void setup() {
    mapper = new TestResultRequestToTestResult();

    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @Test
  public void map_validTestResultRequestWithValue_returnsValidTestResult() {
    String validId = "a".repeat(64);
    Integer noTestResultEvaluated = 0;
    TestResultRequest source = new TestResultRequest().setId(validId);
    TestResult expected = new TestResult().setId(validId).setResult(noTestResultEvaluated);

    checkForValidationErrors(source, 0);
    // change to 0 expected errors after PR#56 was merged
    checkForValidationErrors(expected, 1);

    TestResult actual = mapper.map(source);
    // change to 0 expected errors after PR#56 was merged
    checkForValidationErrors(actual, 1);
    Assert.assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
  }

  @Test
  public void map_TestResultNotInitilized_resultingInNullPointerException() {
    TestResultRequest source = null;

    Assert.assertThrows(NullPointerException.class, () -> mapper.map(source));
  }

  @Test
  public void map_invalidTestResulRequest_throwsValidationErrors() {
    TestResultRequest source = new TestResultRequest();

    TestResult actual = mapper.map(source);

    checkForValidationErrors(actual, 2);
  }

  //### H E L P E R ###
  private <E> void checkForValidationErrors(E objectToValidate, int expectedErrors) {

    Set<ConstraintViolation<E>> violations = validator.validate(objectToValidate);
    violations.forEach(System.out::println);
    Assert.assertEquals(expectedErrors, violations.size());
  }

}
