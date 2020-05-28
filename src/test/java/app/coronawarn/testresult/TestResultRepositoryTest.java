/*
 * Corona-Warn-App / cwa-testresult-server
 *
 * (C) 2020, T-Systems International GmbH
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

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = TestResultApplication.class)
public class TestResultRepositoryTest {

  @Autowired
  private TestResultRepository testResultRepository;

  @Before
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
    Assert.assertNotNull(create);
    Assert.assertEquals(resultId, create.getResultId());
    // find
    Optional<TestResultEntity> find = testResultRepository.findByResultId(resultId);
    Assert.assertTrue(find.isPresent());
    Assert.assertEquals(result, find.get().getResult());
    Assert.assertEquals(resultId, find.get().getResultId());
    Assert.assertEquals(resultDate, find.get().getResultDate());
    Assert.assertNotNull(find.get().getCreatedAt());
    Assert.assertNotNull(find.get().getUpdatedAt());
    Assert.assertNotNull(find.get().getVersion());
  }

}
