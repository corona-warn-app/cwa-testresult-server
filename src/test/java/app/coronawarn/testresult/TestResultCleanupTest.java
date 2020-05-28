package app.coronawarn.testresult;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import rx.Single;

@RunWith(SpringRunner.class)
@SpringBootTest(
  properties = {
    "testresult.cleanup.rate=1000"
  }
)
@ContextConfiguration(classes = TestResultApplication.class)
public class TestResultCleanupTest {

  @Autowired
  private TestResultRepository testResultRepository;

  @Before
  public void before() {
    testResultRepository.deleteAll();
  }

  @Test
  public void shouldCleanupDatabase() {
    // data
    String resultId = "a".repeat(64);
    LocalDateTime resultDate = LocalDateTime.now().minus(Period.ofDays(14));
    // create
    TestResultEntity create = testResultRepository.save(new TestResultEntity()
      .setResult(1)
      .setResultId(resultId)
      .setResultDate(resultDate)
    );
    Assert.assertNotNull(create);
    Assert.assertEquals(resultId, create.getResultId());
    // find
    Optional<TestResultEntity> find = testResultRepository.findByResultId(resultId);
    Assert.assertTrue(find.isPresent());
    Assert.assertEquals(resultId, find.get().getResultId());
    Assert.assertEquals(resultDate, find.get().getResultDate());
    // wait
    Single.fromCallable(() -> true).delay(1, TimeUnit.SECONDS).toBlocking().value();
    // find
    find = testResultRepository.findByResultId(resultId);
    Assert.assertFalse(find.isPresent());
  }
}
