package esk.controller;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class Error {

  private static Logger logger = LoggerFactory.getLogger(Error.class);

  @ExceptionHandler(Throwable.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public String exception(final Throwable throwable, final Model model) {
    logger.error("Exception during execution of SpringSecurity application", throwable);
    boolean isNull = Objects.isNull(throwable);
    String errorMessage = isNull ? "Unknown error" : throwable.getMessage();
    model.addAttribute("errorMessage", errorMessage);
    return "error";
  }
}
