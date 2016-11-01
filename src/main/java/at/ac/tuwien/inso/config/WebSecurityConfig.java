package at.ac.tuwien.inso.config;

import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.csrf().ignoringAntMatchers("/rest/**")  //disable csrf for rest
				.and()
				.authorizeRequests()
				.antMatchers("/").permitAll()
				.antMatchers("/rest/**").permitAll()   //do not require passwords for rest
				.antMatchers("/min/**").permitAll()
				.antMatchers("/webjars/**").permitAll()
				.anyRequest().authenticated()
				.and()
				.formLogin()
				.loginPage("/login")
				.permitAll()
				.and()
				.logout()
				.permitAll();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
				.inMemoryAuthentication()
				.withUser("user").password("pass").roles("USER")
				.and()
				.withUser("admin").password("pass").roles("ADMIN");
	}

}
