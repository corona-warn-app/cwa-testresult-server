package app.coronawarn.testresult.mapper.model;

import app.coronawarn.testresult.model.TestResult;
import app.coronawarn.testresult.model.TestResultResponse;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestResultToTestResultResponseTest {

  private Validator validator;
  private TestResultToTestResultResponse mapper;

  @Before
  public void setup() {
    mapper = new TestResultToTestResultResponse();

    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @Test
  public void map_validTestResultWithValue_returnsValidTestResultResponse() {
    String validId = "a".repeat(64);
    Integer negativeTestResult = 1;
    TestResult source = new TestResult().setId(validId).setResult(negativeTestResult);
    TestResultResponse expected = new TestResultResponse().setTestResult(negativeTestResult);

    checkForValidationErrors(source, 0);
    checkForValidationErrors(expected, 0);

    TestResultResponse actual = mapper.map(source);
    checkForValidationErrors(actual, 0);
    Assert.assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
  }

  @Test
  public void map_TestResultNotInitilized_resultingInNullPointerException() {
    TestResult source = null;

    Assert.assertThrows(NullPointerException.class, () -> mapper.map(source));
  }

  @Test
  public void map_validTestResultWithoutValue_returnsValidTestResultResponseWithoutValue() {
    String validId = "a".repeat(64);
    Integer noTestResult = null;
    TestResult source = new TestResult().setId(validId).setResult(noTestResult);
    TestResultResponse expected = new TestResultResponse().setTestResult(noTestResult);

    TestResultResponse actual = mapper.map(source);

    checkForValidationErrors(actual, 1);
    Assert.assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
  }

  //### H E L P E R ###
  private <E> void checkForValidationErrors(E objectToValidate, int expectedErrors) {

    Set<ConstraintViolation<E>> violations = validator.validate(objectToValidate);
    Assert.assertEquals(expectedErrors, violations.size());
  }

}
