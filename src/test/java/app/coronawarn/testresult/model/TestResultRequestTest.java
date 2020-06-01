package app.coronawarn.testresult.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

public class TestResultRequestTest {

  @Test
  public void canBeDeSerializedFromJson() throws Exception {
    String json = "{\"id\":\"c8aefb946227866a1172b76c03103acf0ced8a356098bfabd0b4e10665331654\"}";
    var testResultRequest = new ObjectMapper().readValue(json, TestResultRequest.class);

    Assert.assertEquals(
        TestResultRequest.builder()
            .id("c8aefb946227866a1172b76c03103acf0ced8a356098bfabd0b4e10665331654")
            .build(),
        testResultRequest);
  }
}
