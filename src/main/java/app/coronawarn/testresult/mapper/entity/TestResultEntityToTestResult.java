package app.coronawarn.testresult.mapper.entity;

import app.coronawarn.testresult.entity.TestResultEntity;
import app.coronawarn.testresult.mapper.Mapper;
import app.coronawarn.testresult.model.TestResult;
import org.springframework.stereotype.Component;

@Component
public class TestResultEntityToTestResult implements Mapper<TestResultEntity, TestResult> {

  @Override
  public TestResult map(TestResultEntity source) {
    TestResult destination = new TestResult();
    
    return destination
      .setId(source.getResultId())
      .setResult(source.getResult());
  }
}
