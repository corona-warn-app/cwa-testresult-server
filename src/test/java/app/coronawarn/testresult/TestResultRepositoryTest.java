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
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@ContextConfiguration(classes = TestResultApplication.class)
public class TestResultRepositoryTest {

  @Autowired
  private TestResultRepository testResultRepository;

  @BeforeEach
  public void before() {
    testResultRepository.deleteAll();
  }

  @Test
  public void createAndFindByResultId() {
    // data
    Integer result = 1;
    String resultId = "a".repeat(64);
    LocalDateTime resultDate = LocalDateTime.now();
    // create
    TestResultEntity create = testResultRepository.save(new TestResultEntity()
      .setResult(result)
      .setResultId(resultId)
      .setResultDate(resultDate)
    );
    Assertions.assertNotNull(create);
    Assertions.assertEquals(resultId, create.getResultId());
    // find
    Optional<TestResultEntity> find = testResultRepository.findByResultId(resultId);
    Assertions.assertTrue(find.isPresent());
    Assertions.assertEquals(result, find.get().getResult());
    Assertions.assertEquals(resultId, find.get().getResultId());
    Assertions.assertEquals(
      resultDate.truncatedTo(ChronoUnit.MILLIS), find.get().getResultDate().truncatedTo(ChronoUnit.MILLIS));
    Assertions.assertNotNull(find.get().getCreatedAt());
    Assertions.assertNotNull(find.get().getUpdatedAt());
    Assertions.assertNotNull(find.get().getVersion());
  }

}
