package app.coronawarn.testresult.mapper.model;

import app.coronawarn.testresult.mapper.Mapper;
import app.coronawarn.testresult.model.TestResult;
import app.coronawarn.testresult.model.TestResultResponse;
import org.springframework.stereotype.Component;

@Component
public class TestResultToTestResultResponse implements Mapper<TestResult, TestResultResponse> {

  @Override
  public TestResultResponse map(TestResult source) {
    TestResultResponse destination = new TestResultResponse();
    Integer testResultValue = source.getResult();
    destination.setTestResult(testResultValue);

    return destination;
  }

}
