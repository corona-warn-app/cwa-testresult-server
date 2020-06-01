package app.coronawarn.testresult.mapper.entity;

import app.coronawarn.testresult.entity.TestResultEntity;
import app.coronawarn.testresult.model.TestResult;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestResultEntityToTestResultTest {

  private Validator validator;
  private TestResultEntityToTestResult mapper;

  @Before
  public void setup() {
    mapper = new TestResultEntityToTestResult();

    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  @Test
  public void map_validTestResultEntity_returnsValidTestResult() {
    String validId = "a".repeat(64);
    Integer negativeTestResult = 1;
    TestResultEntity source =
      new TestResultEntity()
        .setResultId(validId)
        .setResult(negativeTestResult);
    TestResult expected = new TestResult().setId(validId).setResult(negativeTestResult);

    checkForValidationErrors(expected, 0);

    TestResult actual = mapper.map(source);
    checkForValidationErrors(actual, 0);
    Assert.assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
  }

  @Test
  public void map_invalidTestResultEntity_throwsContraintViolationError() {
    String validId = "a".repeat(66);
    Integer invalidTestResultValue = 0;
    TestResultEntity source =
      new TestResultEntity()
        .setResultId(validId)
        .setResult(invalidTestResultValue);
    TestResult expected = new TestResult().setId(validId).setResult(invalidTestResultValue);

    checkForValidationErrors(expected, 2);

    TestResult actual = mapper.map(source);
    checkForValidationErrors(actual, 2);
    Assert.assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
  }

  @Test
  public void map_emptyTestResultEntity_throwsContraintViolationError() {
    TestResultEntity source = new TestResultEntity();
    TestResult expected = new TestResult();

    checkForValidationErrors(expected, 2);

    TestResult actual = mapper.map(source);
    checkForValidationErrors(actual, 2);
    Assert.assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
  }

  @Test
  public void map_TestResultEntityNotInitilized_resultingInNullPointerException() {
    TestResultEntity source = null;

    Assert.assertThrows(NullPointerException.class, () -> mapper.map(source));
  }


  //### H E L P E R ###
  private <E> void checkForValidationErrors(E objectToValidate, int expectedErrors) {

    Set<ConstraintViolation<E>> violations = validator.validate(objectToValidate);
    Assert.assertEquals(expectedErrors, violations.size());
  }
}
