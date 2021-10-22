package app.coronawarn.testresult.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "testresult")
public class TestResultConfig {

  private String allowedClientCertificates;

  private Cleanup cleanup;

  @Getter
  @Setter
  public static class Cleanup {

    private Scheduled redeem;
    private Scheduled delete;

  }

  @Getter
  @Setter
  public static class Scheduled {

    private Integer days;

  }

}
