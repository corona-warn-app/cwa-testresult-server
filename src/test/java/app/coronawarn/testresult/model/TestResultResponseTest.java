package app.coronawarn.testresult.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class TestResultResponseTest {

  @Test
  public void canBeSerializedToJson() throws Exception {
    var testResultResponse = TestResultResponse.builder().testResult(1).build();

    String json = new ObjectMapper().writeValueAsString(testResultResponse);

    JSONAssert.assertEquals("{\"testResult\":1}", json, true);
  }
}
