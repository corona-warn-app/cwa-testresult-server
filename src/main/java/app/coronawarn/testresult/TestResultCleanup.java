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

import app.coronawarn.testresult.config.TestResultConfig;
import app.coronawarn.testresult.entity.TestResultEntity;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.Period;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class TestResultCleanup {

  private final TestResultConfig testResultConfig;
  private final TestResultRepository testResultRepository;

  /**
   * All test results that are older than configured days should be marked as redeemed.
   */
  @Scheduled(
    cron = "${testresult.cleanup.redeem.cron}"
  )
  @SchedulerLock(name = "TestresultCleanupService_redeem", lockAtLeastFor = "PT0S",
    lockAtMostFor = "${testresult.cleanup.redeem.locklimit}")
  @Transactional
  public void redeem() {
    Integer redeemed = testResultRepository.updateResultByCreatedAtBefore(
      TestResultEntity.Result.REDEEMED.ordinal(),
      LocalDateTime.now().minus(Period.ofDays(testResultConfig.getCleanup().getRedeem().getDays())));
    log.info("Cleanup redeemed {} test results.", redeemed);
  }

  /**
   * All test results that are older than configured days should get deleted.
   */
  @Scheduled(
    cron = "${testresult.cleanup.delete.cron}"
  )
  @SchedulerLock(name = "TestresultCleanupService_delete", lockAtLeastFor = "PT0S",
    lockAtMostFor = "${testresult.cleanup.delete.locklimit}")
  @Transactional
  public void delete() {
    Integer deleted = testResultRepository.deleteByCreatedAtBefore(
      LocalDateTime.now().minus(Period.ofDays(testResultConfig.getCleanup().getDelete().getDays())));
    log.info("Cleanup deleted {} test results.", deleted);
  }
}
