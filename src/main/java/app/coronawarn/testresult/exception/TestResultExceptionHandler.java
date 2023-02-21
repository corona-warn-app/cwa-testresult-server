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

package app.coronawarn.testresult.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class TestResultExceptionHandler {

  /**
   * Validation exception handler.
   *
   * @return the map containing validation errors
   */
  @ExceptionHandler({
    MethodArgumentNotValidException.class,
    ConstraintViolationException.class
  })
  public ResponseEntity<Object> handleValidationExceptions() {
    log.warn("Request contains invalid arguments or constraint violations in body.");
    return ResponseEntity.badRequest().build();
  }

  @ExceptionHandler(TestResultException.class)
  public ResponseEntity<Object> handleTestResultExceptions(
    TestResultException exception
  ) {
    log.warn("Request produced a test result exception with status {}.", exception.getStatus());
    return ResponseEntity.status(exception.getStatus()).build();
  }

}
