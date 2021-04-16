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

package app.coronawarn.testresult.service;

import app.coronawarn.testresult.TestResultRepository;
import app.coronawarn.testresult.entity.TestResultEntity;
import app.coronawarn.testresult.exception.TestResultException;
import app.coronawarn.testresult.model.QuickTestResult;
import app.coronawarn.testresult.model.TestResult;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TestResultService {

  private final TestResultRepository testResultRepository;
  private final HashingService hashingService;

  /**
   * Map the entity of a test result to a test result model.
   *
   * @param entity the test result entity to map
   * @return the mapped model from entity
   */
  public TestResult toModel(TestResultEntity entity) {
    return new TestResult()
      .setId(entity.getResultId())
      .setResult(entity.getResult());
  }

  /**
   * Map the model of a test result to a test result entity.
   * This will also set current date time as value for result date.
   *
   * @param model the test result model to map
   * @return the mapped entity from model
   */
  public TestResultEntity toEntity(TestResult model) {
    return new TestResultEntity()
      .setResult(model.getResult())
      .setResultId(model.getId())
      .setResultDate(LocalDateTime.now());
  }

  /**
   * Insert or update a test result to the repository.
   *
   * @param result the test result to insert or update
   */
  public TestResult createOrUpdate(final TestResult result) {
    try {
      final Optional<TestResultEntity> optional = testResultRepository.findByResultId(result.getId());
      TestResultEntity entity = optional.orElseGet(() -> {
        log.info("Creating test result in database.");
        return testResultRepository.save(toEntity(result));
      });
      if (optional.isPresent()) {
        if (TestResultEntity.Result.REDEEMED.ordinal() == entity.getResult()) {
          log.info("Updating test result is not possible because result is already redeemed.");
          return toModel(entity);
        }
        log.info("Updating test result in database.");
        entity.setResult(result.getResult())
          .setResultDate(LocalDateTime.now());
        entity = testResultRepository.save(entity);
      }
      return toModel(entity);
    } catch (Exception e) {
      log.error("Create or update test result failed. {}", e.getMessage());
      throw new TestResultException(HttpStatus.INTERNAL_SERVER_ERROR,
        "Failed to create or update test result.");
    }
  }

  /**
   * Get a test result by it's id or return default pending test result with passed id.
   *
   * @param id the test result id
   * @return the test result
   */
  public TestResult getOrCreate(final String id, boolean quicktest) {
    try {
      TestResultEntity entity = testResultRepository.findByResultId(id)
        .orElseGet(() -> {
          log.info("Get failed now creating test result in database.");
          TestResultEntity resultEntity = new TestResultEntity();
          if (quicktest) {
            resultEntity.setResult(TestResultEntity.Result.QUICK_PENDING.ordinal());
            resultEntity.setResultId(hashingService.sha256Hash(id));
          } else {
            resultEntity.setResult(TestResultEntity.Result.PENDING.ordinal());
            resultEntity.setResultId(id);
          }
          resultEntity.setResultDate(LocalDateTime.now());
          return testResultRepository.save(resultEntity);
        });
      return toModel(entity);
    } catch (Exception e) {
      log.error("Get or create test result failed. {}", e.getMessage());
      throw new TestResultException(HttpStatus.INTERNAL_SERVER_ERROR,
        "Failed to get or create test result.");
    }
  }

  /**
   * Converting a QuicktestResult to Testresult for saving.
   *
   * @param quickTestResult the Result to convert
   * @return the converted test result
   */
  public TestResult convertQuickTest(QuickTestResult quickTestResult) {
    TestResult testResult = new TestResult();
    testResult.setResult(quickTestResult.getResult());
    testResult.setId(hashingService.sha256Hash(quickTestResult.getId()));
    return testResult;
  }

}
