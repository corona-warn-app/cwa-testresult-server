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

package app.coronawarn.testresult.model;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
public class TestResult {

  /**
   * Hash (SHA256) of test result id (aka QR-Code, GUID) encoded as hex string.
   */
  @NotBlank
  @Pattern(regexp = "^[XxA-Fa-f0-9]([A-Fa-f0-9]{63})$")
  private String id;

  /**
   * The test result.
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
  @Min(1)
  @Max(3)
  @NotNull
  @Schema(description = "the result of the PCR test", requiredMode = REQUIRED)
  private Integer result;

  /**
   * Timestamp of the SampleCollection (sc).
   */
  @Schema(description = "time of sample collection (sc) in unix epoch format. "
     + "If not set time of insert will be used instead",
    defaultValue = "Current time")
  private Long sc;

  @JsonIgnore
  @Schema(description = "The id that identifies a lab. Every lab can choose its own labid, "
    + "but it must be unique over all labs, should be generated once via cryptographic hash function",
    requiredMode = REQUIRED, maxLength = 64)
  private String labId;
}
