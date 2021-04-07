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

package app.coronawarn.testresult.model;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Response model of the test result.
 */
@Schema(
  description = "The test result response model."
)
@Getter
@ToString
@EqualsAndHashCode
public class TestResultResponse {

  /**
   * The test result value from test lab or quick test
   * 1: negative
   * 2: positive
   * 3: invalid
   * 4: redeemed
   * 5: quick-test-Pending
   * 6: quick-test-Negative
   * 7: quick-test-Positive
   * 8: quick-test-Invalid
   * 9: quick-test-Redeemed
   */
  @NotNull
  @Min(0)
  @Max(9)
  private Integer testResult;

  public TestResultResponse setTestResult(Integer testResult) {
    this.testResult = testResult;
    return this;
  }
}
