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

package app.coronawarn.testresult.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * This class represents the test result entity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "test_result")
public class TestResultEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  @CreatedDate
  @Column(name = "created_at")
  private LocalDateTime createdAt;
  @LastModifiedDate
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
  @Version
  @Column(name = "version")
  private Long version;
  @Column(name = "result")
  private Integer result;
  @Column(name = "result_id")
  private String resultId;
  @Column(name = "result_date")
  private LocalDateTime resultDate;

  @Column(name = "lab_id")
  private String labId;

  public TestResultEntity setResult(Integer result) {
    this.result = result;
    return this;
  }

  public TestResultEntity setResultId(String resultId) {
    this.resultId = resultId;
    return this;
  }

  public TestResultEntity setResultDate(LocalDateTime resultDate) {
    this.resultDate = resultDate;
    return this;
  }

  public TestResultEntity setLabId(String labId) {
    this.labId = labId;
    return this;
  }

  public enum Result {
    PENDING, NEGATIVE, POSITIVE, INVALID, REDEEMED,
    QUICK_PENDING, QUICK_NEGATIVE, QUICK_POSITIVE, QUICK_INVALID, QUICK_REDEEMED,
    POCNAT_PENDING, POCNAT_NEGATIVE, POCNAT_POSITIVE, POCNAT_INVALID, POCNAT_REDEEMED
  }
}
