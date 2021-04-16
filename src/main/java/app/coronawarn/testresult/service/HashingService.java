package app.coronawarn.testresult.service;

import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HashingService {

  /**
   * Creates a SHA256 hash of the given id.
   *
   * @param id String to hash
   * @return the hash
   */
  public String sha256Hash(String id) {
    return Hashing.sha256()
      .hashString(id, StandardCharsets.UTF_8)
      .toString();
  }
}
