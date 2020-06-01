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
import lombok.Builder;
import lombok.Value;

/**
 * Response model of the test result.
 */
@Schema(
  description = "The test result response model."
)
@Value
@Builder
public class TestResultResponse {

  /**
   * The test result value from test lab.
   * Pending  = 0 : The test result does not exist yet
   * Negative = 1 : No indication for COVID-19
   * Positive = 2 : The test result indicates infection with COVID-19
   * Invalid  = 3 : The test result is invalid due to unknown reason
   * Redeemed = 4 : The test result is redeemed by time
   */
  @NotNull
  @Min(0)
  @Max(4)
  Integer testResult;
}
