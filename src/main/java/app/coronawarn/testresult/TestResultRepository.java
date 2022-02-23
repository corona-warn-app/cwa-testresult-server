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
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TestResultRepository extends JpaRepository<TestResultEntity, Long> {

  Optional<TestResultEntity> findByResultId(String resultId);

  @Modifying
  @Query("update TestResultEntity t set t.result = ?1 where t.result != ?1 and t.createdAt < ?2")
  Integer updateResultByCreatedAtBefore(Integer result, LocalDateTime before);

  @Modifying
  @Query("update TestResultEntity t set t.result = ?1 where t.result != ?1 and t.resultDate < ?2")
  Integer updateResultByResultDateBefore(Integer result, LocalDateTime before);

  @Modifying
  @Query("delete from TestResultEntity t where t.createdAt < ?1")
  Integer deleteByCreatedAtBefore(LocalDateTime before);

  @Modifying
  @Query("delete from TestResultEntity t where t.resultDate < ?1")
  Integer deleteByResultDateBefore(LocalDateTime before);
}
