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
import app.coronawarn.testresult.model.PocNatResult;
import app.coronawarn.testresult.model.QuickTestResult;
import app.coronawarn.testresult.model.TestResult;
import app.coronawarn.testresult.model.TestType;
import app.coronawarn.testresult.service.TestResultService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = TestResultApplication.class)
public class TestResultServiceTest {

  @Autowired
  private TestResultService testResultService;
  @Autowired
  private TestResultRepository testResultRepository;

  @BeforeEach
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
    Assertions.assertNotNull(entity);
    Assertions.assertEquals(id, entity.getResultId());
    Assertions.assertEquals(result, entity.getResult());
    Assertions.assertEquals(labId, entity.getLabId());
    // to model
    model = testResultService.toModel(entity);
    Assertions.assertNotNull(model);
    Assertions.assertEquals(id, model.getId());
    Assertions.assertEquals(result, model.getResult());
    Assertions.assertEquals(labId, model.getLabId());
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
    Assertions.assertNotNull(create);
    Assertions.assertEquals(result, create.getResult());
    Assertions.assertEquals(labId, create.getLabId());
    // get
    TestResult get = testResultService.getOrCreate(id, TestType.PCR, 0L);
    Assertions.assertNotNull(get);
    Assertions.assertEquals(result, get.getResult());
    Assertions.assertEquals(labId, get.getLabId());
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
    Assertions.assertNotNull(create);
    Assertions.assertEquals(resultCreate, create.getResult());
    Assertions.assertEquals(labId, create.getLabId());
    // get
    TestResult get = testResultService.getOrCreate(id, TestType.PCR, 0L);
    Assertions.assertNotNull(get);
    Assertions.assertEquals(resultCreate, get.getResult());
    Assertions.assertEquals(labId, get.getLabId());
    // update
    TestResult update = new TestResult()
      .setId(id)
      .setResult(resultUpdate)
      .setLabId(labId)
      .setSc(Instant.now().getEpochSecond());
    update = testResultService.createOrUpdate(update);
    Assertions.assertNotNull(update);
    Assertions.assertEquals(resultUpdate, update.getResult());
    Assertions.assertEquals(labId, update.getLabId());
    // get
    get = testResultService.getOrCreate(id, TestType.PCR, 0L);
    Assertions.assertNotNull(get);
    Assertions.assertEquals(resultUpdate, get.getResult());
    Assertions.assertEquals(labId, get.getLabId());
  }

  @Test
  public void getOrCreate() {
    // data
    String id = "a".repeat(64);
    Integer result = 0;
    // get
    TestResult get = testResultService.getOrCreate(id, TestType.PCR, 0L);
    Assertions.assertNotNull(get);
    Assertions.assertEquals(result, get.getResult());
    Assertions.assertNull(get.getLabId());

    id = "b".repeat(64);
    result = 5;
    get = testResultService.getOrCreate(id, TestType.QUICKTEST, null);
    Assertions.assertNotNull(get);
    Assertions.assertEquals(result, get.getResult());
    Assertions.assertNull(get.getLabId());

    id = "c".repeat(64);
    result = 10;
    get = testResultService.getOrCreate(id, TestType.POCNAT, 0L);
    Assertions.assertNotNull(get);
    Assertions.assertEquals(result, get.getResult());
    Assertions.assertNull(get.getLabId());
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
    Assertions.assertNotNull(create);
    Assertions.assertEquals(resultCreate, create.getResult());
    // redeem
    testResultRepository.findByResultId(id)
      .ifPresent(u -> testResultRepository.save(u
        .setResult(TestResultEntity.Result.REDEEMED.ordinal())));
    // update
    TestResult update = new TestResult()
      .setId(id)
      .setResult(resultUpdate);
    update = testResultService.createOrUpdate(update);
    Assertions.assertNotNull(update);
    Assertions.assertNotEquals(resultUpdate, update.getResult());
  }

  @Test
  public void conversionCheckIsPocNat() {
    Assertions.assertEquals(TestResultEntity.Result.PENDING.ordinal(), (int) testResultService.conversionCheck(TestResultEntity.Result.POCNAT_PENDING.ordinal()));
    Assertions.assertEquals(TestResultEntity.Result.REDEEMED.ordinal(), (int) testResultService.conversionCheck(TestResultEntity.Result.POCNAT_REDEEMED.ordinal()));
    Assertions.assertEquals(TestResultEntity.Result.INVALID.ordinal(), (int) testResultService.conversionCheck(TestResultEntity.Result.POCNAT_INVALID.ordinal()));
    Assertions.assertEquals(TestResultEntity.Result.NEGATIVE.ordinal(), (int) testResultService.conversionCheck(TestResultEntity.Result.POCNAT_NEGATIVE.ordinal()));
    Assertions.assertEquals(TestResultEntity.Result.POSITIVE.ordinal(), (int) testResultService.conversionCheck(TestResultEntity.Result.POCNAT_POSITIVE.ordinal()));
  }

  @Test
  public void conversionCheckNotPocNat() {
    Assertions.assertEquals(TestResultEntity.Result.PENDING.ordinal(), (int) testResultService.conversionCheck(TestResultEntity.Result.PENDING.ordinal()));
    Assertions.assertEquals(TestResultEntity.Result.REDEEMED.ordinal(), (int) testResultService.conversionCheck(TestResultEntity.Result.REDEEMED.ordinal()));
    Assertions.assertEquals(TestResultEntity.Result.INVALID.ordinal(), (int) testResultService.conversionCheck(TestResultEntity.Result.INVALID.ordinal()));
    Assertions.assertEquals(TestResultEntity.Result.NEGATIVE.ordinal(), (int) testResultService.conversionCheck(TestResultEntity.Result.NEGATIVE.ordinal()));
    Assertions.assertEquals(TestResultEntity.Result.POSITIVE.ordinal(), (int) testResultService.conversionCheck(TestResultEntity.Result.POSITIVE.ordinal()));
    Assertions.assertEquals(TestResultEntity.Result.QUICK_PENDING.ordinal(), (int) testResultService.conversionCheck(TestResultEntity.Result.QUICK_PENDING.ordinal()));
    Assertions.assertEquals(TestResultEntity.Result.QUICK_REDEEMED.ordinal(), (int) testResultService.conversionCheck(TestResultEntity.Result.QUICK_REDEEMED.ordinal()));
    Assertions.assertEquals(TestResultEntity.Result.QUICK_INVALID.ordinal(), (int) testResultService.conversionCheck(TestResultEntity.Result.QUICK_INVALID.ordinal()));
    Assertions.assertEquals(TestResultEntity.Result.QUICK_NEGATIVE.ordinal(), (int) testResultService.conversionCheck(TestResultEntity.Result.QUICK_NEGATIVE.ordinal()));
    Assertions.assertEquals(TestResultEntity.Result.QUICK_POSITIVE.ordinal(), (int) testResultService.conversionCheck(TestResultEntity.Result.QUICK_POSITIVE.ordinal()));
  }

  @Test
  public void convertQuickTest() {
    String id = "b".repeat(64);
    QuickTestResult quicktest = new QuickTestResult();
    quicktest.setResult(5);
    quicktest.setId(id);
    quicktest.setSc(Instant.now().getEpochSecond());
    TestResult result = testResultService.convertQuickTest(quicktest,"TestId");

    Assertions.assertEquals(quicktest.getResult(), result.getResult());
    Assertions.assertEquals(quicktest.getSc(), result.getSc());
    Assertions.assertNotEquals(quicktest.getId(), result.getId());
  }

  @Test
  public void convertPocNat() {
    String id = "b".repeat(64);
    PocNatResult pocnat = new PocNatResult();
    pocnat.setResult(5);
    pocnat.setId(id);
    pocnat.setSc(Instant.now().getEpochSecond());
    TestResult result = testResultService.convertPocNat(pocnat,"TestId");

    Assertions.assertEquals(pocnat.getResult(), result.getResult());
    Assertions.assertEquals(pocnat.getSc(), result.getSc());
    Assertions.assertNotEquals(pocnat.getId(), result.getId());
  }
}
