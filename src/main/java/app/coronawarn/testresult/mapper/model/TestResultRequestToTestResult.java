package app.coronawarn.testresult.mapper.model;

import app.coronawarn.testresult.mapper.Mapper;
import app.coronawarn.testresult.model.TestResult;
import app.coronawarn.testresult.model.TestResultRequest;
import org.springframework.stereotype.Component;

@Component
public class TestResultRequestToTestResult implements Mapper<TestResultRequest, TestResult> {

  private static final Integer NO_TEST_RESULT_EVALUATED = 0;

  @Override
  public TestResult map(TestResultRequest source) {
    TestResult destination = new TestResult();

    return
      destination
        .setId(source.getId())
        .setResult(NO_TEST_RESULT_EVALUATED);
  }
}
