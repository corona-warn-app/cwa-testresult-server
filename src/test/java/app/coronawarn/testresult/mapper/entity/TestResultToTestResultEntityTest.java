package app.coronawarn.testresult.mapper.entity;

import app.coronawarn.testresult.entity.TestResultEntity;
import app.coronawarn.testresult.model.TestResult;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestResultToTestResultEntityTest {

  private TestResultToTestResultEntity mapper;

  @Before
  public void setup() {
    mapper = new TestResultToTestResultEntity();
  }

  @Test
  public void map_validTestResultEntity_returnsValidTestResult() {
    String validId = "a".repeat(64);
    Integer negativeTestResult = 1;
    TestResult source = new TestResult().setId(validId).setResult(negativeTestResult);
    TestResultEntity expected =
      new TestResultEntity()
        .setResultId(validId)
        .setResult(negativeTestResult);

    TestResultEntity actual = mapper.map(source);
    Assert.assertTrue(EqualsBuilder.reflectionEquals(expected, actual, "resultDate"));
  }

  @Test
  public void map_TestResultNotInitilized_resultingInNullPointerException() {
    TestResult source = null;

    Assert.assertThrows(NullPointerException.class, () -> mapper.map(source));
  }

}
