package su.myexe.app.model

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "users")
class User(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long? = null,

	@Column(nullable = false, length = 255)
	var name: String,

	@OneToMany(
		mappedBy = "user",
		cascade = [CascadeType.ALL],
		orphanRemoval = true,
		fetch = FetchType.LAZY
	)
	val credentials: MutableList<UserCredential> = mutableListOf()
)
