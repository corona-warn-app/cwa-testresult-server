package app.coronawarn.testresult.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

public class TestResultTest {

  @Test
  public void canBeDeSerializedFromJson() throws Exception {
    String json =
        "{\"id\":\"c8aefb946227866a1172b76c03103acf0ced8a356098bfabd0b4e10665331654\",\"result\":1}";

    var testResult = new ObjectMapper().readValue(json, TestResult.class);

    Assert.assertEquals(
        TestResult.builder()
            .id("c8aefb946227866a1172b76c03103acf0ced8a356098bfabd0b4e10665331654")
            .result(1)
            .build(),
        testResult);
  }
}
