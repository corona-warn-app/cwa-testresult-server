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
import app.coronawarn.testresult.exception.TestResultException;
import app.coronawarn.testresult.model.TestResult;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TestResultService {

  private final TestResultRepository testResultRepository;

  /**
   * Insert or update a test result to the repository.
   *
   * @param result the test result to insert or update
   */
  public TestResult insertOrUpdate(final TestResult result) {
    try {
      TestResultEntity entity = testResultRepository.findByResultId(result.getId())
        .orElseGet(() ->
          testResultRepository.save(new TestResultEntity()
            .setResult(result.getResult())
            .setResultId(result.getId())
            .setResultDate(LocalDateTime.now())
          )
        );
      entity.setResult(result.getResult())
        .setResultDate(LocalDateTime.now());
      entity = testResultRepository.save(entity);
      return new TestResult()
        .setId(entity.getResultId())
        .setResult(entity.getResult());
    } catch (Exception e) {
      throw new TestResultException(HttpStatus.INTERNAL_SERVER_ERROR,
        "Failed to insert or update test result.");
    }
  }

  /**
   * Get a test result by it's id or return default pending test result with passed id.
   *
   * @param id the test result id
   * @return the test result
   */
  public TestResult getOrInsert(final String id) {
    try {
      TestResultEntity entity = testResultRepository.findByResultId(id)
        .orElseGet(() ->
          testResultRepository.save(new TestResultEntity()
            .setResult(TestResultEntity.Result.PENDING.ordinal())
            .setResultId(id)
            .setResultDate(LocalDateTime.now())
          )
        );
      return new TestResult()
        .setId(entity.getResultId())
        .setResult(entity.getResult());
    } catch (Exception e) {
      throw new TestResultException(HttpStatus.INTERNAL_SERVER_ERROR,
        "Failed to get test result.");
    }
  }

}
