/*
 * Copyright 2019 agwlvssainokuni
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cherry.gallery;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.userdetails.UserDetailsResourceFactoryBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> c = auth.inMemoryAuthentication()
				.passwordEncoder(new BCryptPasswordEncoder());
		for (UserDetails u : UserDetailsResourceFactoryBean.fromResourceLocation("classpath:user.properties")
				.getObject()) {
			c.withUser(u);
		}
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests() //
				.mvcMatchers("/exit").access("hasIpAddress('127.0.0.1') or hasIpAddress('::1')") //
				.anyRequest().authenticated();
		http.formLogin().loginPage("/login/start").loginProcessingUrl("/login/execute").permitAll() //
				.usernameParameter("loginId").passwordParameter("password") //
				.defaultSuccessUrl("/", false) //
				.failureUrl("/login/start?loginFailed");
		http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")).permitAll() //
				.logoutSuccessUrl("/login/start?loggedOut") //
				.invalidateHttpSession(true);
	}

}
