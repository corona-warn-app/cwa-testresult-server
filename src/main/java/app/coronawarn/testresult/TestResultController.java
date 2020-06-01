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

package app.coronawarn.testresult;

import app.coronawarn.testresult.mapper.model.TestResultRequestToTestResult;
import app.coronawarn.testresult.mapper.model.TestResultToTestResultResponse;
import app.coronawarn.testresult.model.TestResult;
import app.coronawarn.testresult.model.TestResultRequest;
import app.coronawarn.testresult.model.TestResultResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
public class TestResultController {

  private final TestResultService testResultService;
  private final TestResultRequestToTestResult toTestResultMapper;
  private final TestResultToTestResultResponse toTestResultResponseMapper;

  /**
   * Get the test result response from a request containing the id.
   *
   * @param request the test result request with id
   * @return the test result response
   */
  @Operation(
    description = "Get test result response from request."
  )
  @PostMapping(
    value = "/api/v1/app/result",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<TestResultResponse> result(
    @RequestBody @Valid TestResultRequest request
  ) {
    TestResult toSearch = toTestResultMapper.map(request);
    TestResult result = testResultService.insertAndUpdate(toSearch);
    TestResultResponse response = toTestResultResponseMapper.map(result);
    return ResponseEntity.ok(response);
  }

  /**
   * Insert or update the test results.
   *
   * @param request the test result collection request
   * @return the response
   */
  @Operation(
    description = "Create test results from collection."
  )
  @PostMapping(
    value = "/api/v1/lab/results",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<?> results(
    @RequestBody @NotEmpty List<@Valid TestResult> request
  ) {
    request.forEach(testResultService::getOrInsert);
    return ResponseEntity.noContent().build();
  }
}
