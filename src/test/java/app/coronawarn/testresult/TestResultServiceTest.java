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

import app.coronawarn.testresult.exception.TestResultException;
import app.coronawarn.testresult.model.TestResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = TestResultApplication.class)
public class TestResultServiceTest {

  @Autowired
  private TestResultService testResultService;
  @Autowired
  private TestResultRepository testResultRepository;

  @Before
  public void before() {
    testResultRepository.deleteAll();
  }

  @Test
  public void insert() {
    // data
    String id = "a".repeat(64);
    Integer result = 1;
    TestResult testResult = new TestResult()
      .setId(id)
      .setResult(result);
    // insert
    testResult = testResultService.insertOrUpdate(testResult);
    Assert.assertNotNull(testResult);
    Assert.assertEquals(result, testResult.getResult());
    // get
    testResult = testResultService.get(id);
    Assert.assertNotNull(testResult);
    Assert.assertEquals(result, testResult.getResult());
  }

  @Test
  public void getNotFound() {
    // data
    String id = "b".repeat(64);
    // get
    try {
      testResultService.get(id);
      Assert.fail();
    } catch (TestResultException e) {
      Assert.assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
    }
  }

}
