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
  description = "The PoC-NAT result model."
)
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
public class PocNatResult {

  /**
   * Hash (SHA256) of test result id (aka QR-Code, GUID) encoded as hex string.
   */
  @NotBlank
  @Pattern(regexp = "^[XxA-Fa-f0-9]([A-Fa-f0-9]{63})$")
  @Schema(description = "the testId (Hashed GUID")
  private String id;

  /**
   * The test result.
   * 10: PoC-NAT-Pending
   * 11: PoC-NAT-Negative
   * 12: PoC-NAT-Positive
   * 13: PoC-NAT-Invalid
   * 14: PoC-NAT-Redeemed
   */
  @Min(10)
  @Max(14)
  @NotNull
  @Schema(description = "the result of the PoC-NAT", required = true)
  private Integer result;

  /**
   * Timestamp of the SampleCollection (sc).
   */
  @Schema(description = "the timestamp of the sample collection (sc) in unix epoch format. If not set,"
    + " the time of insertion will be used instead")
  private Long sc;
}
