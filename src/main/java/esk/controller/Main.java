package esk.controller;

import java.util.Locale;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.unbescape.html.HtmlEscape;

@Controller
public class Main {

  @RequestMapping("/")
  public String root(Locale locale) {
    return "redirect:/index.html";
  }

  @RequestMapping("/index.html")
  public String index() {
    return "index";
  }

  @RequestMapping("/user/index.html")
  public String userIndex() {
    return "user/index";
  }

  @RequestMapping("/admin/index.html")
  public String adminIndex() {
    return "admin/index";
  }

  @RequestMapping("/shared/index.html")
  public String sharedIndex() {
    return "shared/index";
  }

  @RequestMapping("/login.html")
  public String login() {
    return "login";
  }

  @RequestMapping("/login-error.html")
  public String loginError(Model model) {
    model.addAttribute("loginError", true);
    return "login";
  }

  @RequestMapping("/simulateError.html")
  public void simulateError() {
    throw new RuntimeException("This is a simulated error message");
  }

  @RequestMapping("/error.html")
  public String error(HttpServletRequest request, Model model) {
    Object object = request.getAttribute("javax.servlet.error.status_code");
    model.addAttribute("errorCode", "Error " + object);
    Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("<ul>");
    String string = "";
    boolean isNull = Objects.isNull(throwable);

    while (! isNull) {
      stringBuilder = stringBuilder.append("<li>");
      string = throwable.getMessage();
      string = HtmlEscape.escapeHtml5(string);
      stringBuilder = stringBuilder.append(string);
      stringBuilder = stringBuilder.append("</li>");
      throwable = throwable.getCause();
      isNull = Objects.isNull(throwable);
    }

    stringBuilder = stringBuilder.append("</ul>");
    string = stringBuilder.toString();
    model.addAttribute("errorMessage", string);
    return "error";
  }

  @RequestMapping("/403.html")
  public String forbidden() {
    return "403";
  }
  
}
