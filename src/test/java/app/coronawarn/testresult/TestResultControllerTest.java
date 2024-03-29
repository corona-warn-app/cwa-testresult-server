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

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import app.coronawarn.testresult.model.PocNatResult;
import app.coronawarn.testresult.model.PocNatResultList;
import app.coronawarn.testresult.model.QuickTestResult;
import app.coronawarn.testresult.model.QuickTestResultList;
import app.coronawarn.testresult.model.TestResult;
import app.coronawarn.testresult.model.TestResultList;
import app.coronawarn.testresult.model.TestResultRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = TestResultApplication.class)
public class TestResultControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private TestResultRepository testResultRepository;

  @BeforeEach
  public void before() {
    testResultRepository.deleteAll();
  }

  @Test
  public void insertInvalidIdShouldReturnBadRequest() throws Exception {
    // data
    String id = "";
    Integer result = 0;
    // create
    List<TestResult> invalid = Collections.singletonList(
      new TestResult().setId("").setResult(0)
    );
    mockMvc.perform(MockMvcRequestBuilders
      .post("/api/v1/lab/results")
      .accept(MediaType.APPLICATION_JSON_VALUE)
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .content(objectMapper.writeValueAsString(invalid)))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  public void insertInvalidResultShouldReturnBadRequest() throws Exception {
    // data
    String id = "a".repeat(64);
    // create
    TestResultList invalid = new TestResultList().setTestResults(Collections.singletonList(
      new TestResult().setId(id)
    ));
    mockMvc.perform(MockMvcRequestBuilders
      .post("/api/v1/lab/results")
      .accept(MediaType.APPLICATION_JSON_VALUE)
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .content(objectMapper.writeValueAsString(invalid)))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isBadRequest());
    // create
    invalid = new TestResultList().setTestResults(Collections.singletonList(
      new TestResult().setId(id).setResult(10)
    ));
    mockMvc.perform(MockMvcRequestBuilders
      .post("/api/v1/lab/results")
      .accept(MediaType.APPLICATION_JSON_VALUE)
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .content(objectMapper.writeValueAsString(invalid)))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  public void insertValidShouldReturnNoContentWithoutLabId() throws Exception {
    // data
    String id = "b".repeat(64);
    Integer result = 1;
    // create
    TestResultList valid = new TestResultList().setTestResults(Collections.singletonList(
      new TestResult().setId(id).setResult(result)
    ));
    mockMvc.perform(MockMvcRequestBuilders
      .post("/api/v1/lab/results")
      .accept(MediaType.APPLICATION_JSON_VALUE)
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .content(objectMapper.writeValueAsString(valid)))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @Test
  public void insertValidShouldReturnNoContentWithLabId() throws Exception {
    // data
    String id = "b".repeat(64);
    String labId = "l".repeat(64);
    Integer result = 1;
    // create
    TestResultList valid = new TestResultList()
      .setLabId(labId)
      .setTestResults(Collections.singletonList(
      new TestResult().setId(id).setResult(result)
    ));
    mockMvc.perform(MockMvcRequestBuilders
      .post("/api/v1/lab/results")
      .accept(MediaType.APPLICATION_JSON_VALUE)
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .content(objectMapper.writeValueAsString(valid)))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @Test
  public void insertValidDobHashShouldReturnNoContentWithLabId() throws Exception {
    // data
    String id = "x" + "b".repeat(63);
    String labId = "l".repeat(64);
    Integer result = 1;
    // create
    TestResultList valid = new TestResultList()
      .setLabId(labId)
      .setTestResults(Collections.singletonList(
        new TestResult().setId(id).setResult(result)
      ));
    mockMvc.perform(MockMvcRequestBuilders
      .post("/api/v1/lab/results")
      .accept(MediaType.APPLICATION_JSON_VALUE)
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .content(objectMapper.writeValueAsString(valid)))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @Test
  public void insertValidAndGetShouldReturnOkWithoutLabId() throws Exception {
    // data
    String id = "c".repeat(64);
    Integer result = 1;
    // create
    TestResultList valid = new TestResultList().setTestResults(Collections.singletonList(
      new TestResult().setId(id).setResult(result)
    ));
    mockMvc.perform(MockMvcRequestBuilders
      .post("/api/v1/lab/results")
      .accept(MediaType.APPLICATION_JSON_VALUE)
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .content(objectMapper.writeValueAsString(valid)))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isNoContent());
    // get
    TestResultRequest request = new TestResultRequest()
      .setId(id);
    mockMvc.perform(MockMvcRequestBuilders
      .post("/api/v1/app/result")
      .accept(MediaType.APPLICATION_JSON_VALUE)
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .content(objectMapper.writeValueAsString(request)))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(jsonPath("labId").doesNotExist());

  }

  @Test
  public void insertValidAndGetShouldReturnOkWithLabId() throws Exception {
    // data
    String id = "c".repeat(64);
    String labId = "l".repeat(64);
    Integer result = 1;
    // create
    TestResultList valid = new TestResultList()
      .setLabId(labId)
      .setTestResults(Collections.singletonList(
      new TestResult().setId(id).setResult(result)
    ));
    mockMvc.perform(MockMvcRequestBuilders
      .post("/api/v1/lab/results")
      .accept(MediaType.APPLICATION_JSON_VALUE)
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .content(objectMapper.writeValueAsString(valid)))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isNoContent());
    // get
    TestResultRequest request = new TestResultRequest()
      .setId(id);
    mockMvc.perform(MockMvcRequestBuilders
      .post("/api/v1/app/result")
      .accept(MediaType.APPLICATION_JSON_VALUE)
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .content(objectMapper.writeValueAsString(request)))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(jsonPath("labId", is(labId)));
  }

  @Test
  public void notExistingTestResultShouldReturnOk() throws Exception {
    // data
    String id = "d".repeat(64);
    Integer result = 1;
    // create
    List<TestResult> valid = Collections.singletonList(
      new TestResult().setId(id).setResult(result)
    );
    // get
    TestResultRequest request = new TestResultRequest()
      .setId(id);
    mockMvc.perform(MockMvcRequestBuilders
      .post("/api/v1/app/result")
      .accept(MediaType.APPLICATION_JSON_VALUE)
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .content(objectMapper.writeValueAsString(request)))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk());
  }


  @Test
  public void quickInsertValidWithCsShouldReturnNoContent() throws Exception {
    // data
    String id = "b".repeat(64);
    Integer result = 5;
    // create
    QuickTestResultList valid = new QuickTestResultList();
    valid.setTestResults(Collections.singletonList(
      new QuickTestResult().setId(id).setResult(result).setSc(System.currentTimeMillis())
    ));
    mockMvc.perform(MockMvcRequestBuilders
      .post("/api/v1/quicktest/results")
      .accept(MediaType.APPLICATION_JSON_VALUE)
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .content(objectMapper.writeValueAsString(valid)))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @Test
  public void quickInsertValidWithDobHashAndWithCsShouldReturnNoContent() throws Exception {
    // data
    String id = "x" + "b".repeat(63);
    Integer result = 5;
    // create
    QuickTestResultList valid = new QuickTestResultList();
    valid.setTestResults(Collections.singletonList(
      new QuickTestResult().setId(id).setResult(result).setSc(System.currentTimeMillis())
    ));
    mockMvc.perform(MockMvcRequestBuilders
      .post("/api/v1/quicktest/results")
      .accept(MediaType.APPLICATION_JSON_VALUE)
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .content(objectMapper.writeValueAsString(valid)))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @Test
  public void quickInsertValidWithCsShouldReturnNoContentWithLabId() throws Exception {
    // data
    String id = "b".repeat(64);
    String labId = "l".repeat(64);
    Integer result = 5;
    // create
    QuickTestResultList valid = new QuickTestResultList();
    valid.setLabId(labId);
    valid.setTestResults(Collections.singletonList(
      new QuickTestResult().setId(id).setResult(result).setSc(System.currentTimeMillis())
    ));
    mockMvc.perform(MockMvcRequestBuilders
      .post("/api/v1/quicktest/results")
      .accept(MediaType.APPLICATION_JSON_VALUE)
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .content(objectMapper.writeValueAsString(valid)))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @Test
  public void quickInsertValidWithCsShouldReturnNoContentAndQueryResult() throws Exception {
    // data
    String id = "b".repeat(64);
    Integer result = 5;
    // create
    QuickTestResultList valid = new QuickTestResultList();
    valid.setTestResults(Collections.singletonList(
      new QuickTestResult().setId(id).setResult(result).setSc(System.currentTimeMillis())
    ));
    mockMvc.perform(MockMvcRequestBuilders
      .post("/api/v1/quicktest/results")
      .accept(MediaType.APPLICATION_JSON_VALUE)
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .content(objectMapper.writeValueAsString(valid)))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isNoContent());

    TestResultRequest request = new TestResultRequest()
      .setId(id);
    mockMvc.perform(MockMvcRequestBuilders
      .post("/api/v1/app/result")
      .accept(MediaType.APPLICATION_JSON_VALUE)
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .content(objectMapper.writeValueAsString(request)))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andReturn()
      .getResponse()
      .getContentAsString()
      .contains("cs");

    request = new TestResultRequest()
      .setId(id);
    mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/quicktest/result")
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(request)))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andReturn()
      .getResponse()
      .getContentAsString()
      .contains("cs");
  }

  @Test
  public void quickInsertValidShouldReturnNoContent() throws Exception {
    // data
    String id = "b".repeat(64);
    Integer result = 5;
    // create
    QuickTestResultList valid = new QuickTestResultList();
    valid.setTestResults(Collections.singletonList(
      new QuickTestResult().setId(id).setResult(result)
    ));
    mockMvc.perform(MockMvcRequestBuilders
      .post("/api/v1/quicktest/results")
      .accept(MediaType.APPLICATION_JSON_VALUE)
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .content(objectMapper.writeValueAsString(valid)))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @Test
  public void quickInsertInValidShouldReturnUnprocessableEntity() throws Exception {
    // data
    String id = "b".repeat(64);
    Integer result = 4;
    // create
    QuickTestResultList valid = new QuickTestResultList();
    valid.setTestResults(Collections.singletonList(
      new QuickTestResult().setId(id).setResult(result)
    ));
    mockMvc.perform(MockMvcRequestBuilders
      .post("/api/v1/quicktest/results")
      .accept(MediaType.APPLICATION_JSON_VALUE)
      .contentType(MediaType.APPLICATION_JSON_VALUE)
      .content(objectMapper.writeValueAsString(valid)))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  public void pocnatInsertValidWithCsShouldReturnNoContent() throws Exception {
    // data
    String id = "b".repeat(64);
    Integer result = 10;
    // create
    PocNatResultList valid = new PocNatResultList();
    valid.setTestResults(Collections.singletonList(
      new PocNatResult().setId(id).setResult(result).setSc(System.currentTimeMillis())
    ));
    mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/pocnat/results")
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(valid)))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @Test
  public void pocnatInsertValidWithDobHashAndWithCsShouldReturnNoContent() throws Exception {
    // data
    String id = "x" + "b".repeat(63);
    Integer result = 10;
    // create
    PocNatResultList valid = new PocNatResultList();
    valid.setTestResults(Collections.singletonList(
      new PocNatResult().setId(id).setResult(result).setSc(System.currentTimeMillis())
    ));
    mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/pocnat/results")
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(valid)))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @Test
  public void pocnatInsertValidWithCsShouldReturnNoContentWithLabId() throws Exception {
    // data
    String id = "b".repeat(64);
    String labId = "l".repeat(64);
    Integer result = 10;
    // create
    PocNatResultList valid = new PocNatResultList();
    valid.setLabId(labId);
    valid.setTestResults(Collections.singletonList(
      new PocNatResult().setId(id).setResult(result).setSc(System.currentTimeMillis())
    ));
    mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/pocnat/results")
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(valid)))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @Test
  public void pocnatInsertValidWithCsShouldReturnNoContentAndQueryResult() throws Exception {
    // data
    String id = "b".repeat(64);
    Integer result = 10;
    // create
    PocNatResultList valid = new PocNatResultList();
    valid.setTestResults(Collections.singletonList(
      new PocNatResult().setId(id).setResult(result).setSc(System.currentTimeMillis())
    ));
    mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/pocnat/results")
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(valid)))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isNoContent());

    TestResultRequest request = new TestResultRequest()
      .setId(id);
    mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/app/result")
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(request)))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andReturn()
      .getResponse()
      .getContentAsString()
      .contains("cs");

    request = new TestResultRequest()
      .setId(id);
    mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/pocnat/result")
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(request)))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andReturn()
      .getResponse()
      .getContentAsString()
      .contains("cs");
  }

  @Test
  public void pocnatInsertValidShouldReturnNoContent() throws Exception {
    // data
    String id = "b".repeat(64);
    Integer result = 10;
    // create
    PocNatResultList valid = new PocNatResultList();
    valid.setTestResults(Collections.singletonList(
      new PocNatResult().setId(id).setResult(result)
    ));
    mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/pocnat/results")
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(valid)))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isNoContent());
  }

  @Test
  public void pocnatInsertInValidShouldReturnUnprocessableEntity() throws Exception {
    // data
    String id = "b".repeat(64);
    Integer result = 9;
    // create
    PocNatResultList valid = new PocNatResultList();
    valid.setTestResults(Collections.singletonList(
      new PocNatResult().setId(id).setResult(result)
    ));
    mockMvc.perform(MockMvcRequestBuilders
        .post("/api/v1/pocnat/results")
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(valid)))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

}
