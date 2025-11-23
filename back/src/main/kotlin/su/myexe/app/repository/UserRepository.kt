package su.myexe.app.repository

import org.springframework.data.jpa.repository.JpaRepository
import su.myexe.app.model.User

interface UserRepository : JpaRepository<User, Long>
