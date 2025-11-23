package su.myexe.app.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "user_credentials")
class UserCredential(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 50)
	val provider: CredentialProvider,

	@Column(nullable = false, length = 255)
	val login: String,

	@Column(nullable = true, length = 255)
	val password: String? = null,

	@Column(name = "refresh_token", length = 1024)
	val refreshToken: String? = null,

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	val user: User
)
