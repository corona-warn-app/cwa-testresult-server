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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Model of the test result.
 */
@Schema(
  description = "The test result model."
)
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
public class QuickTestResult {

  /**
   * Hash (SHA256) of test result id (aka QR-Code, GUID) encoded as hex string.
   */
  @NotBlank
  @Pattern(regexp = "^([A-Fa-f0-9]{2}){32}$")
  private String id;

  /**
   * The test result.
   * 5: quick-test-Pending
   * 6: quick-test-Negative
   * 7: quick-test-Positive
   * 8: quick-test-Invalid
   * 9: quick-test-Redeemed
   */
  @Min(5)
  @Max(9)
  @NotNull
  private Integer result;

  /**
   * Timestamp of the SampleCollection (sc).
   */
  private Long sc;
}
