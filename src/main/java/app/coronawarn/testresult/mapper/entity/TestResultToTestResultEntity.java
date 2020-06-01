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

  /**
   * Mapping to {@link TestResultEntity} with a specific test result value to be inserted into
   * database.
   *
   * @param source          {@link TestResult} from a request incoming
   * @param testResultValue refer to {@link TestResultEntity.Result} - insert this as {@code
   *                        ordinal()}
   * @return Entity to be save into database
   */
  public TestResultEntity mapWithResultValue(TestResult source, int testResultValue) {
    TestResultEntity destination = new TestResultEntity();

    return
      destination.setResult(testResultValue)
        .setResultId(source.getId())
        .setResultDate(LocalDateTime.now());
  }
}
