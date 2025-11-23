package su.myexe.app.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import su.myexe.app.repository.UserRepository

@Component
class JwtAuthenticationFilter(
	private val jwtService: JwtService,
	private val userRepository: UserRepository
) : OncePerRequestFilter() {

	override fun doFilterInternal(
		request: HttpServletRequest,
		response: HttpServletResponse,
		filterChain: FilterChain
	) {
		val authHeader = request.getHeader("Authorization")
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response)
			return
		}

		val token = authHeader.substring(7)
		if (jwtService.validate(token)) {
			val userId = jwtService.extractUserId(token)
			if (userId != null && SecurityContextHolder.getContext().authentication == null) {
				val user = userRepository.findById(userId).orElse(null)
				if (user != null) {
					val authentication = UsernamePasswordAuthenticationToken(
						user,
						null,
						emptyList()
					)
					authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
					SecurityContextHolder.getContext().authentication = authentication
				}
			}
		}

		filterChain.doFilter(request, response)
	}
}
