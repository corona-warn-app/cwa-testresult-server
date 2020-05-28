package app.coronawarn.testresult;

import java.time.LocalDateTime;
import java.time.Period;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class TestResultCleanup {

  private final TestResultConfig testResultConfig;
  private final TestResultRepository testResultRepository;

  /**
   * All test results that are older than configured days should get deleted.
   */
  @Scheduled(
    fixedDelayString = "${testresult.cleanup.rate}"
  )
  @Transactional
  public void cleanup() {
    log.info("cleanup()");
    testResultRepository.deleteByResultDateBefore(LocalDateTime.now()
      .minus(Period.ofDays(testResultConfig.getCleanup().getDays())));
  }
}
