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

import app.coronawarn.testresult.entity.TestResultEntity;
import app.coronawarn.testresult.model.TestResult;
import app.coronawarn.testresult.service.TestResultService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
  public void toEntityAndToModel() {
    // data
    String id = "a".repeat(64);
    Integer result = 1;
    // to entity
    TestResult model = new TestResult()
      .setId(id)
      .setResult(result);
    TestResultEntity entity = testResultService.toEntity(model);
    Assert.assertNotNull(entity);
    Assert.assertEquals(id, entity.getResultId());
    Assert.assertEquals(result, entity.getResult());
    // to model
    model = testResultService.toModel(entity);
    Assert.assertNotNull(model);
    Assert.assertEquals(id, model.getId());
    Assert.assertEquals(result, model.getResult());
  }

  @Test
  public void insertOrUpdate() {
    // data
    String id = "a".repeat(64);
    Integer result = 1;
    TestResult create = new TestResult()
      .setId(id)
      .setResult(result);
    // create
    create = testResultService.createOrUpdate(create);
    Assert.assertNotNull(create);
    Assert.assertEquals(result, create.getResult());
    // get
    TestResult get = testResultService.getOrCreate(id,false);
    Assert.assertNotNull(get);
    Assert.assertEquals(result, get.getResult());
  }

  @Test
  public void insertAndUpdate() {
    // data
    String id = "a".repeat(64);
    Integer resultCreate = 1;
    Integer resultUpdate = 2;
    TestResult create = new TestResult()
      .setId(id)
      .setResult(resultCreate);
    // create
    create = testResultService.createOrUpdate(create);
    Assert.assertNotNull(create);
    Assert.assertEquals(resultCreate, create.getResult());
    // get
    TestResult get = testResultService.getOrCreate(id,false);
    Assert.assertNotNull(get);
    Assert.assertEquals(resultCreate, get.getResult());
    // update
    TestResult update = new TestResult()
      .setId(id)
      .setResult(resultUpdate);
    update = testResultService.createOrUpdate(update);
    Assert.assertNotNull(update);
    Assert.assertEquals(resultUpdate, update.getResult());
    // get
    get = testResultService.getOrCreate(id,false);
    Assert.assertNotNull(get);
    Assert.assertEquals(resultUpdate, get.getResult());
  }

  @Test
  public void getOrCreate() {
    // data
    String id = "a".repeat(64);
    Integer result = 0;
    // get
    TestResult get = testResultService.getOrCreate(id,false);
    Assert.assertNotNull(get);
    Assert.assertEquals(result, get.getResult());
  }

  @Test
  public void createAndDenyRedeemedUpdate() {
    // data
    String id = "a".repeat(64);
    Integer resultCreate = 1;
    Integer resultUpdate = 2;
    TestResult create = new TestResult()
      .setId(id)
      .setResult(resultCreate);
    // create
    create = testResultService.createOrUpdate(create);
    Assert.assertNotNull(create);
    Assert.assertEquals(resultCreate, create.getResult());
    // redeem
    testResultRepository.findByResultId(id)
      .ifPresent(u -> testResultRepository.save(u
        .setResult(TestResultEntity.Result.REDEEMED.ordinal())));
    // update
    TestResult update = new TestResult()
      .setId(id)
      .setResult(resultUpdate);
    update = testResultService.createOrUpdate(update);
    Assert.assertNotNull(update);
    Assert.assertNotEquals(resultUpdate, update.getResult());
  }
}
