package su.myexe.app.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.util.Date

@Service
class JwtService(
	@Value("\${app.security.jwt.secret}") private val secret: String,
	@Value("\${app.security.jwt.expiration-ms}") private val expirationMs: Long
) {

	private val key = Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))

	fun generateToken(userId: Long, login: String): String {
		val now = Date()
		val expiry = Date(now.time + expirationMs)

		return Jwts.builder()
			.subject(login)
			.issuedAt(now)
			.expiration(expiry)
			.claim("userId", userId)
			.signWith(key)
			.compact()
	}

	fun extractUserId(token: String): Long? =
		runCatching {
			Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.payload["userId"]?.toString()?.toLong()
		}.getOrNull()

	fun validate(token: String): Boolean =
		runCatching {
			val claims = Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.payload
			claims.expiration.after(Date())
		}.getOrDefault(false)
}
