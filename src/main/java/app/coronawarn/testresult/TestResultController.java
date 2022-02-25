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

import app.coronawarn.testresult.model.PocNatResult;
import app.coronawarn.testresult.model.PocNatResultList;
import app.coronawarn.testresult.model.QuickTestResultList;
import app.coronawarn.testresult.model.TestResult;
import app.coronawarn.testresult.model.TestResultList;
import app.coronawarn.testresult.model.TestResultRequest;
import app.coronawarn.testresult.model.TestResultResponse;
import app.coronawarn.testresult.model.TestType;
import app.coronawarn.testresult.service.TestResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
public class TestResultController {

  private final TestResultService testResultService;

  /**
   * Get the test result response from a request containing the id.
   *
   * @param request the test result request with id
   * @return the test result response
   */
  @Operation(
    description = "Get test result for a given TestId (Hashed GUID)",
    summary = "Retrieve test results",
    requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = TestResultRequest.class))),
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Content exists",
        content = @Content(schema = @Schema(implementation = TestResultResponse.class))
      )
    }
  )
  @PostMapping(
    value = "/api/v1/app/result",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<TestResultResponse> result(
    @org.springframework.web.bind.annotation.RequestBody @Valid TestResultRequest request
  ) {
    log.info("Received test result request from app.");

    TestResult result = testResultService.getOrCreate(request.getId(), TestType.PCR, null);
    return ResponseEntity.ok(new TestResultResponse()
      .setLabId(result.getLabId())
      .setTestResult(testResultService.conversionCheck(result.getResult()), result.getSc())
    );
  }

  /**
   * Insert or update the test results.
   *
   * @param list the test result list request
   * @return the response
   */
  @Operation(
    description = "PCR testresults can be inserted.",
    summary = "PCR Testresults to be inserted by labs.",
    requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = TestResultRequest.class))),
    responses = {
      @ApiResponse(
        responseCode = "204",
        description = "No content, testresult successfully inserted"
      )
    }
  )
  @PostMapping(
    value = "/api/v1/lab/results",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Object> results(
    @org.springframework.web.bind.annotation.RequestBody @NotNull @Valid TestResultList list
  ) {
    log.info("Received {} test results to insert or update from lab.", list.getTestResults().size());

    list.getTestResults().stream()
      .map(tr -> tr.setLabId(list.getLabId()))
      .forEach(testResultService::createOrUpdate);

    return ResponseEntity.noContent().build();
  }

  /**
   * Get the test result response from a request containing the id.
   *
   * @param request the test result request with id
   * @return the test result response
   */
  @Operation(
    description = "The result and the sc (sample collection) timestamp of a RAT can be set.",
    summary = "Set the testresult for a Rapid Antigen Test.",
    requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = TestResultRequest.class))),
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Ok, RAT result inserted successfully."
      )
    }
  )
  @PostMapping(
    value = "/api/v1/quicktest/result",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<TestResultResponse> quickTestResult(
    @org.springframework.web.bind.annotation.RequestBody @Valid TestResultRequest request
  ) {
    log.info("Received test result request from Quicktest.");
    TestResult result = testResultService.getOrCreate(request.getId(), TestType.QUICKTEST, request.getSc());
    return ResponseEntity.ok(new TestResultResponse()
      .setLabId(result.getLabId())
      .setTestResult(result.getResult()));
  }

  /**
   * Insert or update the quick test.
   *
   * @param list the test result list request
   * @return the response
   */
  @Operation(
    description = "The result and the sc (sample collection) timestamp of a RAT can be set.",
    summary = "Set multiple testresults for a Rapid Antigen Test as an array.",
    requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = TestResultRequest.class))),
    responses = {
      @ApiResponse(
        responseCode = "204",
        description = "No content, RAT result(s) inserted successfully."
      )
    }
  )
  @PostMapping(
    value = "/api/v1/quicktest/results",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Object> quicktestResults(
    @org.springframework.web.bind.annotation.RequestBody @NotNull @Valid QuickTestResultList list
  ) {
    log.info("Received {} test result to insert or update from Quicktests. ", list.getTestResults().size());

    list.getTestResults().stream()
      .map(tr -> testResultService.convertQuickTest(tr, list.getLabId()))
      .forEach(testResultService::createOrUpdate);

    return ResponseEntity.noContent().build();
  }

  /**
   * Get the test result response from a request containing the id.
   *
   * @param request the test result request with id
   * @return the test result response
   */
  @Operation(
    description = "The result and the sc (sample collection) timestamp of a PoC-NAT can be set.",
    summary = "Set the testresult for a PoC-NAT.",
    requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = TestResultRequest.class))),
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Ok, PoC-NAT result inserted successfully."
      )
    }
  )
  @PostMapping(
    value = "/api/v1/pocnat/result",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<TestResultResponse> pocnatResult(
    @org.springframework.web.bind.annotation.RequestBody @Valid TestResultRequest request
  ) {
    log.info("Received test result request from PoC-NAT.");
    TestResult result = testResultService.getOrCreate(request.getId(), TestType.POCNAT, request.getSc());
    return ResponseEntity.ok(new TestResultResponse()
      .setLabId(result.getLabId())
      .setTestResult(result.getResult()));
  }

  /**
   * Insert or update the PoC-NAT.
   *
   * @param list the test result list request
   * @return the response
   */
  @Operation(
    description = "The result and the sc (sample collection) timestamp of a PoC-NAT can be set.",
    summary = "Set multiple testresults for a PoC-NAT as an array.",
    requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = PocNatResultList.class))),
    responses = {
      @ApiResponse(
        responseCode = "204",
        description = "No content, PoC-NAT result(s) inserted successfully."
      )
    }
  )
  @PostMapping(
    value = "/api/v1/pocnat/results",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<Void> pocnatResults(
    @org.springframework.web.bind.annotation.RequestBody @NotNull @Valid PocNatResultList list
  ) {
    log.info("Received {} test result to insert or update from PoC-NATs. ", list.getTestResults().size());

    list.getTestResults().stream()
      .map(tr -> testResultService.convertPocNat(tr, list.getLabId()))
      .forEach(testResultService::createOrUpdate);

    return ResponseEntity.noContent().build();
  }
}
