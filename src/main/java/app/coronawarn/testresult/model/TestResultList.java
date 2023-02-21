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

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Model of the test result list.
 */
@Schema(
  description = "The test result list model."
)
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
public class TestResultList {

  /**
   * The test result entries.
   */
  @NotNull
  @NotEmpty
  @Schema(description = "array of PCR test results", requiredMode = REQUIRED)
  private List<@Valid TestResult> testResults;

  /**
   * The labId of the uploader.
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Schema(description = "The labid that identifys a lab. Every lab can choose its own labid, "
    + "but it must be unique over all labs, should be generated once via cryptographic hash function",
    requiredMode = REQUIRED, maxLength = 64)
  private String labId;

}
