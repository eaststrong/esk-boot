package esk;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  public SecurityConfig() {
    super();
  }

  @SuppressWarnings("deprecation")
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> inMemoryUserDetailsManagerConfigurer = auth.inMemoryAuthentication();
    UserBuilder userBuilder = User.withDefaultPasswordEncoder();
    userBuilder = userBuilder.username("jim");
    userBuilder = userBuilder.password("demo");
    userBuilder = userBuilder.roles("ADMIN");
    UserDetails userDetails = userBuilder.build();
    inMemoryUserDetailsManagerConfigurer = inMemoryUserDetailsManagerConfigurer.withUser(userDetails);
    userBuilder = User.withDefaultPasswordEncoder();
    userBuilder = userBuilder.username("bob");
    userBuilder = userBuilder.password("demo");
    userBuilder = userBuilder.roles("USER");
    userDetails = userBuilder.build();
    inMemoryUserDetailsManagerConfigurer = inMemoryUserDetailsManagerConfigurer.withUser(userDetails);
    userBuilder = User.withDefaultPasswordEncoder();
    userBuilder = userBuilder.username("ted");
    userBuilder = userBuilder.password("demo");
    Set<String> roleSet = new HashSet<String>();
    roleSet.add("USER");
    roleSet.add("ADMIN");
    int size = roleSet.size();
    String[] tmp = new String[size];
    String[] roles = roleSet.toArray(tmp);
    userBuilder = userBuilder.roles(roles);
    userDetails = userBuilder.build();
    inMemoryUserDetailsManagerConfigurer.withUser(userDetails);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    FormLoginConfigurer<HttpSecurity> formLoginConfigurer = http.formLogin();
    formLoginConfigurer = formLoginConfigurer.loginPage("/login.html");
    formLoginConfigurer = formLoginConfigurer.failureUrl("/login-error.html");
    HttpSecurity httpSecurity = formLoginConfigurer.and();
    LogoutConfigurer<HttpSecurity> logoutConfigurer = httpSecurity.logout();
    logoutConfigurer = logoutConfigurer.logoutSuccessUrl("/index.html");
    httpSecurity = logoutConfigurer.and();
    ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry = httpSecurity.authorizeRequests();
    ExpressionUrlAuthorizationConfigurer<HttpSecurity>.AuthorizedUrl authorizedUrl = expressionInterceptUrlRegistry.antMatchers("/admin/**");
    expressionInterceptUrlRegistry = authorizedUrl.hasRole("ADMIN");
    authorizedUrl = expressionInterceptUrlRegistry.antMatchers("/user/**");
    expressionInterceptUrlRegistry = authorizedUrl.hasRole("USER");
    authorizedUrl = expressionInterceptUrlRegistry.antMatchers("/shared/**");
    Set<String> roleSet = new HashSet<String>();
    roleSet.add("USER");
    roleSet.add("ADMIN");
    int size = roleSet.size();
    String[] tmp = new String[size];
    String[] roles = roleSet.toArray(tmp);
    expressionInterceptUrlRegistry = authorizedUrl.hasAnyRole(roles);
    httpSecurity = expressionInterceptUrlRegistry.and();
    ExceptionHandlingConfigurer<HttpSecurity> exceptionHandlingConfigurer = httpSecurity.exceptionHandling();
    exceptionHandlingConfigurer.accessDeniedPage("/403.html");
  }

}
