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

import app.coronawarn.testresult.exception.TestResultException;
import app.coronawarn.testresult.model.TestResult;
import app.coronawarn.testresult.model.TestResultRequest;
import app.coronawarn.testresult.model.TestResultResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(TestResultController.class)
public class TestResultControllerTest {

  @Autowired
  private MockMvc mockMvc;

  private JacksonTester<List<TestResult>> jsonTestResult;
  private JacksonTester<TestResultRequest> jsonTestResultRequest;
  private JacksonTester<TestResultResponse> jsonTestResultResponse;

  @MockBean
  private TestResultService testResultService;

  @Before
  public void before() {
    JacksonTester.initFields(this, new ObjectMapper());
  }

  // ### results path ###
  @Test
  public void results_insertTestResultWithEmptyId_resultingInBadRequest() throws Exception {
    String emptyId = "";
    Integer validTestResultValue = 1;
    TestResult validTestResult = new TestResult().setId(emptyId).setResult(validTestResultValue);
    List<TestResult> invalidTestResults = Collections.singletonList(validTestResult);

    mockMvc
      .perform(
        MockMvcRequestBuilders.post("/api/v1/lab/results")
          .accept(MediaType.APPLICATION_JSON_VALUE)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .content(jsonTestResult.write(invalidTestResults).getJson()))
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.content().string(StringUtils.EMPTY))
      .andDo(MockMvcResultHandlers.print());

    BDDMockito.verify(testResultService, Mockito.never()).insertOrUpdate(Mockito.any());
  }

  @Test
  public void results_insertTestResultWithOutOfRangeTestResultValue_resultingInBadRequest()
    throws Exception {
    String validId = "a".repeat(64);
    Integer outOfRangeTestResult = 0;
    TestResult validTestResult = new TestResult().setId(validId).setResult(outOfRangeTestResult);
    List<TestResult> invalidTestResults = Collections.singletonList(validTestResult);

    mockMvc
      .perform(
        MockMvcRequestBuilders.post("/api/v1/lab/results")
          .accept(MediaType.APPLICATION_JSON_VALUE)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .content(jsonTestResult.write(invalidTestResults).getJson()))
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.content().string(StringUtils.EMPTY))
      .andDo(MockMvcResultHandlers.print());

    BDDMockito.verify(testResultService, Mockito.never()).insertOrUpdate(Mockito.any());
  }

  @Test
  public void results_insertTestResultWithIdPatternNotMatching_resultingInBadRequest()
    throws Exception {
    String idPatternNotMatching = "a";
    Integer validTestResultValue = 1;
    TestResult validTestResult = new TestResult().setId(idPatternNotMatching)
      .setResult(validTestResultValue);
    List<TestResult> invalidTestResults = Collections.singletonList(validTestResult);

    mockMvc
      .perform(
        MockMvcRequestBuilders.post("/api/v1/lab/results")
          .accept(MediaType.APPLICATION_JSON_VALUE)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .content(jsonTestResult.write(invalidTestResults).getJson()))
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.content().string(StringUtils.EMPTY))
      .andDo(MockMvcResultHandlers.print());

    BDDMockito.verify(testResultService, Mockito.never()).insertOrUpdate(Mockito.any());
  }

  @Test
  public void results_insertTestResultWithNoTestResultValue_resultingInCreatingEntity()
    throws Exception {
    String validId = "a".repeat(64);
    Integer nullingTestResult = null;
    TestResult validTestResult = new TestResult().setId(validId).setResult(nullingTestResult);
    List<TestResult> invalidTestResults = Collections.singletonList(validTestResult);

    mockMvc
      .perform(
        MockMvcRequestBuilders.post("/api/v1/lab/results")
          .accept(MediaType.APPLICATION_JSON_VALUE)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .content(jsonTestResult.write(invalidTestResults).getJson()))
      .andExpect(MockMvcResultMatchers.status().isNoContent())
      .andExpect(MockMvcResultMatchers.content().string(StringUtils.EMPTY))
      .andDo(MockMvcResultHandlers.print());

    BDDMockito.verify(testResultService, Mockito.times(1)).insertOrUpdate(Mockito.any());
  }

  @Test
  public void results_insertEmptyTestResultList_resultingInBadRequest() throws Exception {
    List<TestResult> emptyTestResults = Collections.emptyList();

    mockMvc
      .perform(
        MockMvcRequestBuilders.post("/api/v1/lab/results")
          .accept(MediaType.APPLICATION_JSON_VALUE)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .content(jsonTestResult.write(emptyTestResults).getJson()))
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.content().string(StringUtils.EMPTY))
      .andDo(MockMvcResultHandlers.print());

    BDDMockito.verify(testResultService, Mockito.never()).insertOrUpdate(Mockito.any());
  }

  @Test
  public void results_insertWithoutRequestBody_resultingInBadRequest() throws Exception {
    mockMvc
      .perform(
        MockMvcRequestBuilders.post("/api/v1/lab/results")
          .accept(MediaType.APPLICATION_JSON_VALUE)
          .contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.content().string(StringUtils.EMPTY))
      .andDo(MockMvcResultHandlers.print());

    BDDMockito.verify(testResultService, Mockito.never()).insertOrUpdate(Mockito.any());
  }

  @Test
  public void results_insertWrongContentType_resultingInUnsupportedMediaType() throws Exception {
    mockMvc
      .perform(
        MockMvcRequestBuilders.post("/api/v1/lab/results")
          .accept(MediaType.APPLICATION_JSON_VALUE)
          .contentType(MediaType.APPLICATION_XML_VALUE)
          .content("fake xml since it does not get picked up."))
      .andExpect(MockMvcResultMatchers.status().isUnsupportedMediaType())
      .andExpect(MockMvcResultMatchers.content().string(StringUtils.EMPTY))
      .andDo(MockMvcResultHandlers.print());

    BDDMockito.verify(testResultService, Mockito.never()).insertOrUpdate(Mockito.any());
  }

  @Test
  public void results_ServiceThrowingTestResultException_resultingInInternalServerError()
    throws Exception {
    String validId = "a".repeat(64);
    Integer validTestResultValue = 1;
    TestResult validTestResult = new TestResult().setId(validId).setResult(validTestResultValue);
    List<TestResult> invalidTestResults = Collections.singletonList(validTestResult);
    BDDMockito
      .given(testResultService.insertOrUpdate(BDDMockito.any()))
      .willThrow(new TestResultException(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "Failed to insert or update test result."
      ));

    mockMvc
      .perform(
        MockMvcRequestBuilders.post("/api/v1/lab/results")
          .accept(MediaType.APPLICATION_JSON_VALUE)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .content(jsonTestResult.write(invalidTestResults).getJson()))
      .andExpect(MockMvcResultMatchers.status().isInternalServerError())
      .andExpect(MockMvcResultMatchers.content().string(StringUtils.EMPTY))
      .andDo(MockMvcResultHandlers.print());
    BDDMockito.verify(testResultService, Mockito.times(1))
      .insertOrUpdate(validTestResult);
  }

  @Test
  public void results_insertValidTestResult_proceedsCorrectly() throws Exception {
    String validId = "a".repeat(64);
    Integer validTestResultValue = 2;
    TestResult validTestResult = new TestResult().setId(validId).setResult(validTestResultValue);
    List<TestResult> invalidTestResults = Collections.singletonList(validTestResult);

    mockMvc
      .perform(
        MockMvcRequestBuilders.post("/api/v1/lab/results")
          .accept(MediaType.APPLICATION_JSON_VALUE)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .content(jsonTestResult.write(invalidTestResults).getJson()))
      .andExpect(MockMvcResultMatchers.status().isNoContent())
      .andExpect(MockMvcResultMatchers.content().string(StringUtils.EMPTY))
      .andDo(MockMvcResultHandlers.print());

    BDDMockito.verify(testResultService, Mockito.times(1))
      .insertOrUpdate(validTestResult);
  }

  //### result path ###
  @Test
  public void result_invalidTestResultRequest_proceedsCorrectly() {
    String invalidIdPattern = "a".repeat(63);
    String nullId = null;
    String whitespaceId = StringUtils.SPACE;
    List<TestResultRequest> invalidTestRequests =
      Stream.of(invalidIdPattern, nullId, whitespaceId)
        .map(string -> new TestResultRequest().setId(string))
        .collect(Collectors.toList());

    invalidTestRequests.forEach(this::performMockMvcWithTryCatch);
    BDDMockito.verify(testResultService, Mockito.never()).getOrInsert(Mockito.any());
  }

  @Test
  public void result_validTestResultRequestNotInDatabase_resultingOk() throws Exception {
    String validId = "a".repeat(64);
    TestResultRequest validTestRequest = new TestResultRequest().setId(validId);
    Integer testResultNotConfirmed = 0;
    TestResult validTestResult = new TestResult().setId(validId).setResult(testResultNotConfirmed);
    BDDMockito.given(testResultService.getOrInsert(validId)).willReturn(validTestResult);
    TestResultResponse resultResponse = new TestResultResponse()
      .setTestResult(testResultNotConfirmed);

    mockMvc
      .perform(
        MockMvcRequestBuilders.post("/api/v1/app/result")
          .accept(MediaType.APPLICATION_JSON_VALUE)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .content(jsonTestResultRequest.write(validTestRequest).getJson()))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.content()
        .string(jsonTestResultResponse.write(resultResponse).getJson()))
      .andDo(MockMvcResultHandlers.print());

    BDDMockito.verify(testResultService, Mockito.times(1)).getOrInsert(Mockito.any());
  }

  @Test
  public void result_validTestResultRequestWithNoTestResultConfirmationYet_returnsTestResultResponse()
    throws Exception {
    String validId = "a".repeat(64);
    TestResultRequest validTestRequest = new TestResultRequest().setId(validId);
    Integer testResultNotConfirmed = 0;
    TestResult validTestResult = new TestResult().setId(validId).setResult(testResultNotConfirmed);
    BDDMockito.given(testResultService.getOrInsert(validId)).willReturn(validTestResult);
    TestResultResponse resultResponse =
      new TestResultResponse().setTestResult(testResultNotConfirmed);

    mockMvc
      .perform(
        MockMvcRequestBuilders.post("/api/v1/app/result")
          .accept(MediaType.APPLICATION_JSON_VALUE)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .content(jsonTestResultRequest.write(validTestRequest).getJson()))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.content()
        .string(jsonTestResultResponse.write(resultResponse).getJson()))
      .andDo(MockMvcResultHandlers.print());
  }

  @Test
  public void result_validTestResultRequestWithPositiveTestResult_returnsPositiveTestResultResponse()
    throws Exception {
    String validId = "a".repeat(64);
    TestResultRequest validTestRequest = new TestResultRequest().setId(validId);
    Integer positiveTestResult = 2;
    TestResult validTestResult = new TestResult().setId(validId).setResult(positiveTestResult);
    BDDMockito.given(testResultService.getOrInsert(validId)).willReturn(validTestResult);
    TestResultResponse resultResponse = new TestResultResponse().setTestResult(positiveTestResult);

    mockMvc
      .perform(
        MockMvcRequestBuilders.post("/api/v1/app/result")
          .accept(MediaType.APPLICATION_JSON_VALUE)
          .contentType(MediaType.APPLICATION_JSON_VALUE)
          .content(jsonTestResultRequest.write(validTestRequest).getJson()))
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.content()
        .string(jsonTestResultResponse.write(resultResponse).getJson()))
      .andDo(MockMvcResultHandlers.print());
  }

  @Test
  public void result_testResultRequestWithoutBody_resultingInBadRequest() throws Exception {
    mockMvc
      .perform(
        MockMvcRequestBuilders.post("/api/v1/app/result")
          .accept(MediaType.APPLICATION_JSON_VALUE)
          .contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(MockMvcResultMatchers.status().isBadRequest())
      .andExpect(MockMvcResultMatchers.content().string(StringUtils.EMPTY))
      .andDo(MockMvcResultHandlers.print());

    BDDMockito.verify(testResultService, Mockito.never()).getOrInsert(Mockito.any());
  }

  @Test
  public void result_testResultRequestWithWrongContentType_resultingInUnsupportedMediaType()
    throws Exception {
    mockMvc
      .perform(
        MockMvcRequestBuilders.post("/api/v1/app/result")
          .accept(MediaType.APPLICATION_JSON_VALUE)
          .contentType(MediaType.APPLICATION_XML_VALUE)
          .content("fake xml since it does not get picked up."))
      .andExpect(MockMvcResultMatchers.status().isUnsupportedMediaType())
      .andExpect(MockMvcResultMatchers.content().string(StringUtils.EMPTY))
      .andDo(MockMvcResultHandlers.print());

    BDDMockito.verify(testResultService, Mockito.never()).getOrInsert(Mockito.any());
  }

  // ### H E L P E R ###
  private void performMockMvcWithTryCatch(TestResultRequest request) {
    try {
      mockMvc
        .perform(
          MockMvcRequestBuilders.post("/api/v1/app/result")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(jsonTestResultRequest.write(request).getJson()))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().string(StringUtils.EMPTY))
        .andDo(MockMvcResultHandlers.print());
    } catch (Exception e) {
      Assert.fail();
    }
  }

}
