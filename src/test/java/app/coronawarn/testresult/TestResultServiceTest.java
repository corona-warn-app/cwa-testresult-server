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
import app.coronawarn.testresult.model.PocNatResult;
import app.coronawarn.testresult.model.QuickTestResult;
import app.coronawarn.testresult.model.TestResult;
import app.coronawarn.testresult.model.TestType;
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

import java.time.Instant;

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
    String labId = "l".repeat(64);
    Integer result = 1;
    // to entity
    TestResult model = new TestResult()
      .setId(id)
      .setLabId(labId)
      .setResult(result);
    TestResultEntity entity = testResultService.toEntity(model);
    Assert.assertNotNull(entity);
    Assert.assertEquals(id, entity.getResultId());
    Assert.assertEquals(result, entity.getResult());
    Assert.assertEquals(labId, entity.getLabId());
    // to model
    model = testResultService.toModel(entity);
    Assert.assertNotNull(model);
    Assert.assertEquals(id, model.getId());
    Assert.assertEquals(result, model.getResult());
    Assert.assertEquals(labId, model.getLabId());
  }

  @Test
  public void insertOrUpdate() {
    // data
    String id = "a".repeat(64);
    String labId = "l".repeat(64);
    Integer result = 1;
    TestResult create = new TestResult()
      .setId(id)
      .setResult(result)
      .setLabId(labId);
    // create
    create = testResultService.createOrUpdate(create);
    Assert.assertNotNull(create);
    Assert.assertEquals(result, create.getResult());
    Assert.assertEquals(labId, create.getLabId());
    // get
    TestResult get = testResultService.getOrCreate(id, TestType.PCR, 0L);
    Assert.assertNotNull(get);
    Assert.assertEquals(result, get.getResult());
    Assert.assertEquals(labId, get.getLabId());
  }

  @Test
  public void insertAndUpdate() {
    // data
    String id = "a".repeat(64);
    String labId = "l".repeat(64);
    Integer resultCreate = 1;
    Integer resultUpdate = 2;
    TestResult create = new TestResult()
      .setId(id)
      .setResult(resultCreate)
      .setLabId(labId);
    // create
    create = testResultService.createOrUpdate(create);
    Assert.assertNotNull(create);
    Assert.assertEquals(resultCreate, create.getResult());
    Assert.assertEquals(labId, create.getLabId());
    // get
    TestResult get = testResultService.getOrCreate(id, TestType.PCR, 0L);
    Assert.assertNotNull(get);
    Assert.assertEquals(resultCreate, get.getResult());
    Assert.assertEquals(labId, get.getLabId());
    // update
    TestResult update = new TestResult()
      .setId(id)
      .setResult(resultUpdate)
      .setLabId(labId)
      .setSc(Instant.now().getEpochSecond());
    update = testResultService.createOrUpdate(update);
    Assert.assertNotNull(update);
    Assert.assertEquals(resultUpdate, update.getResult());
    Assert.assertEquals(labId, update.getLabId());
    // get
    get = testResultService.getOrCreate(id, TestType.PCR, 0L);
    Assert.assertNotNull(get);
    Assert.assertEquals(resultUpdate, get.getResult());
    Assert.assertEquals(labId, get.getLabId());
  }

  @Test
  public void getOrCreate() {
    // data
    String id = "a".repeat(64);
    Integer result = 0;
    // get
    TestResult get = testResultService.getOrCreate(id, TestType.PCR, 0L);
    Assert.assertNotNull(get);
    Assert.assertEquals(result, get.getResult());
    Assert.assertNull(get.getLabId());

    id = "b".repeat(64);
    result = 5;
    get = testResultService.getOrCreate(id, TestType.QUICKTEST, null);
    Assert.assertNotNull(get);
    Assert.assertEquals(result, get.getResult());
    Assert.assertNull(get.getLabId());

    id = "c".repeat(64);
    result = 10;
    get = testResultService.getOrCreate(id, TestType.POCNAT, 0L);
    Assert.assertNotNull(get);
    Assert.assertEquals(result, get.getResult());
    Assert.assertNull(get.getLabId());
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

  @Test
  public void conversionCheckIsPocNat() {
    Assert.assertEquals(TestResultEntity.Result.PENDING.ordinal(), (int) testResultService.conversionCheck(TestResultEntity.Result.POCNAT_PENDING.ordinal()));
    Assert.assertEquals(TestResultEntity.Result.REDEEMED.ordinal(), (int) testResultService.conversionCheck(TestResultEntity.Result.POCNAT_REDEEMED.ordinal()));
    Assert.assertEquals(TestResultEntity.Result.INVALID.ordinal(), (int) testResultService.conversionCheck(TestResultEntity.Result.POCNAT_INVALID.ordinal()));
    Assert.assertEquals(TestResultEntity.Result.NEGATIVE.ordinal(), (int) testResultService.conversionCheck(TestResultEntity.Result.POCNAT_NEGATIVE.ordinal()));
    Assert.assertEquals(TestResultEntity.Result.POSITIVE.ordinal(), (int) testResultService.conversionCheck(TestResultEntity.Result.POCNAT_POSITIVE.ordinal()));
  }

  @Test
  public void conversionCheckNotPocNat() {
    Assert.assertEquals(TestResultEntity.Result.PENDING.ordinal(), (int) testResultService.conversionCheck(TestResultEntity.Result.PENDING.ordinal()));
    Assert.assertEquals(TestResultEntity.Result.REDEEMED.ordinal(), (int) testResultService.conversionCheck(TestResultEntity.Result.REDEEMED.ordinal()));
    Assert.assertEquals(TestResultEntity.Result.INVALID.ordinal(), (int) testResultService.conversionCheck(TestResultEntity.Result.INVALID.ordinal()));
    Assert.assertEquals(TestResultEntity.Result.NEGATIVE.ordinal(), (int) testResultService.conversionCheck(TestResultEntity.Result.NEGATIVE.ordinal()));
    Assert.assertEquals(TestResultEntity.Result.POSITIVE.ordinal(), (int) testResultService.conversionCheck(TestResultEntity.Result.POSITIVE.ordinal()));
    Assert.assertEquals(TestResultEntity.Result.QUICK_PENDING.ordinal(), (int) testResultService.conversionCheck(TestResultEntity.Result.QUICK_PENDING.ordinal()));
    Assert.assertEquals(TestResultEntity.Result.QUICK_REDEEMED.ordinal(), (int) testResultService.conversionCheck(TestResultEntity.Result.QUICK_REDEEMED.ordinal()));
    Assert.assertEquals(TestResultEntity.Result.QUICK_INVALID.ordinal(), (int) testResultService.conversionCheck(TestResultEntity.Result.QUICK_INVALID.ordinal()));
    Assert.assertEquals(TestResultEntity.Result.QUICK_NEGATIVE.ordinal(), (int) testResultService.conversionCheck(TestResultEntity.Result.QUICK_NEGATIVE.ordinal()));
    Assert.assertEquals(TestResultEntity.Result.QUICK_POSITIVE.ordinal(), (int) testResultService.conversionCheck(TestResultEntity.Result.QUICK_POSITIVE.ordinal()));
  }

  @Test
  public void convertQuickTest() {
    String id = "b".repeat(64);
    QuickTestResult quicktest = new QuickTestResult();
    quicktest.setResult(5);
    quicktest.setId(id);
    quicktest.setSc(Instant.now().getEpochSecond());
    TestResult result = testResultService.convertQuickTest(quicktest,"TestId");

    Assert.assertEquals(quicktest.getResult(), result.getResult());
    Assert.assertEquals(quicktest.getSc(), result.getSc());
    Assert.assertNotEquals(quicktest.getId(), result.getId());
  }

  @Test
  public void convertPocNat() {
    String id = "b".repeat(64);
    PocNatResult pocnat = new PocNatResult();
    pocnat.setResult(5);
    pocnat.setId(id);
    pocnat.setSc(Instant.now().getEpochSecond());
    TestResult result = testResultService.convertPocNat(pocnat,"TestId");

    Assert.assertEquals(pocnat.getResult(), result.getResult());
    Assert.assertEquals(pocnat.getSc(), result.getSc());
    Assert.assertNotEquals(pocnat.getId(), result.getId());
  }
}
