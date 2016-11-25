package at.ac.tuwien.inso.config;

import at.ac.tuwien.inso.entity.Role;
import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.web.authentication.*;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserAccountService userAccountService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.headers().frameOptions().disable();
		http
				.csrf().ignoringAntMatchers("/rest/**") //disable csrf for rest
				.ignoringAntMatchers("/console/**") //disable the database
				.and()
				.authorizeRequests()
				.antMatchers("/rest/**").permitAll()   //do not require passwords for rest
				.antMatchers("/min/**").permitAll()
				.antMatchers("/webjars/**").permitAll()
				.antMatchers("/console/**").permitAll()
				.antMatchers("/account_activation/**").permitAll()
				.antMatchers("/admin/**").hasRole(Role.ADMIN.name())
				.antMatchers("/lecturer/**").hasRole(Role.LECTURER.name())
				.antMatchers("/student/**").hasRole(Role.STUDENT.name())
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
			UserAccount userAccount = (UserAccount) auth.getPrincipal();

			if (userAccount.hasRole(Role.ADMIN)) {
				resp.sendRedirect("/admin/studyplans");
			} else if (userAccount.hasRole(Role.LECTURER)) {
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
