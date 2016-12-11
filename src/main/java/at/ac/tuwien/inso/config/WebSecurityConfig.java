package at.ac.tuwien.inso.config;

import at.ac.tuwien.inso.entity.Role;
import at.ac.tuwien.inso.entity.*;
import at.ac.tuwien.inso.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.method.configuration.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
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
				.antMatchers("/public/**").permitAll()
				.antMatchers("/min/**").permitAll()
				.antMatchers("/webjars/**").permitAll()
				.antMatchers("/node_modules/**").permitAll()
				.antMatchers("/console/**").permitAll()
				.antMatchers("/account_activation/**").permitAll()
				.antMatchers("/admin/**").hasRole(Role.ADMIN.name())
				.antMatchers("/lecturer/**").hasRole(Role.LECTURER.name())
				.antMatchers("/student/**").hasRole(Role.STUDENT.name())
				.anyRequest().authenticated()
				.and()
				.formLogin()
				.loginPage("/login")
				.defaultSuccessUrl("/")
				.failureUrl("/login?error")
				.permitAll()
				.and()
				.logout()
				.logoutSuccessUrl("/login")
				.permitAll();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
				.userDetailsService(userAccountService)
				.passwordEncoder(UserAccount.PASSWORD_ENCODER);
	}
}
