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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Request model of the test result.
 */
@Schema(
  description = "The test result request model."

)
@Getter
@ToString
@EqualsAndHashCode
public class TestResultRequest {

  /**
   * Hash (SHA256) of test result id (aka QR-Code, GUID) encoded as hex string.
   */
  @NotBlank
  @Pattern(regexp = "^[XxA-Fa-f0-9]([A-Fa-f0-9]{63})$")
  private String id;

  /**
   * Timestamp of the SampleCollection (sc).
   */
  private Long sc;

  /**
   * Default constructor with sc null.
   */
  public TestResultRequest setId(String id) {
    this.id = id;
    this.sc = null;
    return this;
  }

  /**
   * All args constructor with sc.
   */
  public TestResultRequest setId(String id, Long sc) {
    this.id = id;
    this.sc = sc;
    return this;
  }
}
