package app.coronawarn.testresult.mapper.entity;

import app.coronawarn.testresult.entity.TestResultEntity;
import app.coronawarn.testresult.mapper.Mapper;
import app.coronawarn.testresult.model.TestResult;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class TestResultToTestResultEntity implements Mapper<TestResult, TestResultEntity> {

  @Override
  public TestResultEntity map(TestResult source) {
    TestResultEntity destination = new TestResultEntity();
    
    return destination
      .setResult(source.getResult())
      .setResultId(source.getId())
      .setResultDate(LocalDateTime.now());
  }
}
