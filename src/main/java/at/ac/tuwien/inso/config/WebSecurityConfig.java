package at.ac.tuwien.inso.config;

import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.core.*;
import org.springframework.security.web.authentication.*;

import java.util.*;
import java.util.stream.*;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserAccountService userAccountService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.csrf().ignoringAntMatchers("/rest/**")  //disable csrf for rest
				.and()
				.authorizeRequests()
				.antMatchers("/rest/**").permitAll()   //do not require passwords for rest
				.antMatchers("/min/**").permitAll()
				.antMatchers("/webjars/**").permitAll()
				.antMatchers("/admin/**").hasRole("ADMIN")
				.antMatchers("/lecturer/**").hasRole("LECTURER")
				.antMatchers("/student/**").hasRole("STUDENT")
				.anyRequest().authenticated()
				.and().exceptionHandling().accessDeniedHandler((req, resp, e) -> resp.sendRedirect("/login"))
				.and()
				.formLogin()
				.loginPage("/login")
				.failureUrl("/login?error")
				.successHandler(authenticationSuccessHandler())
				.permitAll()
				.and()
				.logout()
				.logoutSuccessUrl("/login")
				.permitAll();
	}

	private AuthenticationSuccessHandler authenticationSuccessHandler() {
		return (req, resp, auth) -> {
            List<String> roles = auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            if (roles.contains("ROLE_ADMIN")) {
				resp.sendRedirect("/admin/studyplans");
			} else if (roles.contains("ROLE_LECTURER")) {
				resp.sendRedirect("/lecturer/courses");
			} else {
				resp.sendRedirect("/student/courses");
			}
        };
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
				.userDetailsService(userAccountService)
				.passwordEncoder(UserAccount.PASSWORD_ENCODER);
	}

}
