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

import app.coronawarn.testresult.model.TestResult;
import app.coronawarn.testresult.model.TestResultList;
import app.coronawarn.testresult.model.TestResultRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
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

  @Before
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
      new TestResult().setId(id).setResult(4)
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
  public void insertValidShouldReturnNoContent() throws Exception {
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
  public void insertValidAndGetShouldReturnOk() throws Exception {
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
      .andExpect(MockMvcResultMatchers.status().isOk());
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

}
