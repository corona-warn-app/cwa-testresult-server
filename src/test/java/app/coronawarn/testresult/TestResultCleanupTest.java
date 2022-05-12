/*
 * Corona-Warn-App / cwa-testresult-server
 *
 * (C) 2020 - 2022, T-Systems International GmbH
 *
 * Deutsche Telekom AG and all other contributors /
 * copyright owners license this file to you under the Apache
 * License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package app.coronawarn.testresult;

import app.coronawarn.testresult.entity.TestResultEntity;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(
  properties = {
    "testresult.cleanup.redeem.days=21",
    "testresult.cleanup.redeem.cron=* * * * * *",
    "testresult.cleanup.delete.days=60",
    "testresult.cleanup.delete.cron=* * * * * *"
  }
)
@ContextConfiguration(classes = TestResultApplication.class)
public class TestResultCleanupTest {

  @Autowired
  private TestResultRepository testResultRepository;

  @Autowired
  private TestResultCleanup testResultCleanup;

  @BeforeEach
  public void before() {
    testResultRepository.deleteAll();
  }

  @Test
  public void shouldCleanupRedeem() {
    // prepare
    testResultRepository.deleteAll();
    // data
    String resultId = "a".repeat(64);
    Integer resultRedeemed = TestResultEntity.Result.REDEEMED.ordinal();
    LocalDateTime resultDate = LocalDateTime.now().minus(Period.ofDays(21));
    LocalDateTime createdAt = LocalDateTime.now().minus(Period.ofDays(21));
    // create
    TestResultEntity create = testResultRepository.save(new TestResultEntity()
      .setResult(1)
      .setResultId(resultId)
      .setResultDate(resultDate)
    );
    create.setCreatedAt(createdAt);
    testResultRepository.save(create);
    Assertions.assertNotNull(create);
    Assertions.assertEquals(createdAt, create.getCreatedAt());
    Assertions.assertEquals(resultId, create.getResultId());
    // find
    Optional<TestResultEntity> find = testResultRepository.findByResultId(resultId);
    Assertions.assertTrue(find.isPresent());
    Assertions.assertEquals(resultId, find.get().getResultId());
    Assertions.assertEquals(
      createdAt.truncatedTo(ChronoUnit.MILLIS), find.get().getCreatedAt().truncatedTo(ChronoUnit.MILLIS));
    Assertions.assertEquals(
      resultDate.truncatedTo(ChronoUnit.MILLIS), find.get().getResultDate().truncatedTo(ChronoUnit.MILLIS));
    // manually trigger cleanup
    testResultCleanup.redeem();
    testResultCleanup.delete();
    // find
    find = testResultRepository.findByResultId(resultId);
    Assertions.assertTrue(find.isPresent());
    Assertions.assertEquals(resultId, find.get().getResultId());
    Assertions.assertEquals(resultRedeemed, find.get().getResult());
  }

  @Test
  public void shouldCleanupDelete() {
    // prepare
    testResultRepository.deleteAll();
    // data
    String resultId = "d".repeat(64);
    LocalDateTime resultDate = LocalDateTime.now().minus(Period.ofDays(60));
    LocalDateTime createdAt = LocalDateTime.now().minus(Period.ofDays(60));
    // create
    TestResultEntity create = testResultRepository.save(new TestResultEntity()
      .setResult(1)
      .setResultId(resultId)
      .setResultDate(resultDate)
    );
    create.setCreatedAt(createdAt);
    testResultRepository.save(create);
    Assertions.assertNotNull(create);
    Assertions.assertEquals(createdAt, create.getCreatedAt());
    Assertions.assertEquals(resultId, create.getResultId());
    // find
    Optional<TestResultEntity> find = testResultRepository.findByResultId(resultId);
    Assertions.assertTrue(find.isPresent());
    Assertions.assertEquals(resultId, find.get().getResultId());
    Assertions.assertEquals(
      createdAt.truncatedTo(ChronoUnit.MILLIS), find.get().getCreatedAt().truncatedTo(ChronoUnit.MILLIS));
    Assertions.assertEquals(
      resultDate.truncatedTo(ChronoUnit.MILLIS), find.get().getResultDate().truncatedTo(ChronoUnit.MILLIS));
    // manually trigger cleanup
    testResultCleanup.redeem();
    testResultCleanup.delete();
    // find
    find = testResultRepository.findByResultId(resultId);
    Assertions.assertFalse(find.isPresent());
  }
}
