/*
 * Copyright 2019,2022 agwlvssainokuni
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

import org.springframework.context.annotation.Bean;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.userdetails.UserDetailsResourceFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

@EnableWebSecurity
public class SpringSecurityConfig {

	@Bean
	public UserDetailsManager userDetailsManager() throws Exception {
		UserDetailsResourceFactoryBean factoryBean = UserDetailsResourceFactoryBean
				.fromResourceLocation("classpath:user.properties");
		return new InMemoryUserDetailsManager(factoryBean.getObject());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		var isLocal = new OrRequestMatcher(
				new IpAddressMatcher("::1"),
				new IpAddressMatcher("127.0.0.1"));
		http.authorizeHttpRequests() //
				.mvcMatchers("/exit").access((auth, req) -> {
					var isGranted = isLocal.matches(req.getRequest());
					return new AuthorizationDecision(isGranted);
				}) //
				.anyRequest().authenticated();
		http.formLogin().loginPage("/login/start").loginProcessingUrl("/login/execute").permitAll() //
				.usernameParameter("loginId").passwordParameter("password") //
				.defaultSuccessUrl("/", false) //
				.failureUrl("/login/start?loginFailed");
		http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")).permitAll() //
				.logoutSuccessUrl("/login/start?loggedOut") //
				.invalidateHttpSession(true);
		return http.build();
	}

}
