package su.myexe.app.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import su.myexe.app.security.JwtAuthenticationFilter
import su.myexe.app.security.JwtAuthenticationEntryPoint

@Configuration
@EnableMethodSecurity
class SecurityConfig(
	private val jwtAuthenticationFilter: JwtAuthenticationFilter,
	private val authenticationEntryPoint: JwtAuthenticationEntryPoint
) {

	@Bean
	fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
		http.csrf { it.disable() }
			.sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
			.authorizeHttpRequests { auth ->
				auth.requestMatchers("/api/auth/login").permitAll()
					.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
					.requestMatchers(HttpMethod.GET, "/api/**").permitAll()
					.anyRequest().authenticated()
			}
			.exceptionHandling { it.authenticationEntryPoint(authenticationEntryPoint) }
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

		return http.build()
	}

	@Bean
	fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

	@Bean
	fun authenticationManager(configuration: AuthenticationConfiguration): AuthenticationManager =
		configuration.authenticationManager
}
