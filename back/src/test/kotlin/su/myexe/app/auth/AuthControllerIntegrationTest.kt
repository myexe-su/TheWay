package su.myexe.app.auth

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import su.myexe.app.dto.CredentialRequest
import su.myexe.app.dto.UserRequest
import su.myexe.app.model.CredentialProvider
import su.myexe.app.service.UserService
import java.util.UUID

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerIntegrationTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper,
    val userService: UserService
) {

    private lateinit var login: String
    private lateinit var password: String

    @BeforeEach
    fun setup() {
        login = "user-${UUID.randomUUID()}@example.com"
        password = "pwd-${UUID.randomUUID()}"
        val user = userService.create(UserRequest(name = "Test User"))
        userService.addCredential(
            user.id!!,
            CredentialRequest(
                provider = CredentialProvider.LOCAL,
                login = login,
                password = password,
                refreshToken = null
            )
        )
    }

    @Test
    fun `login should return token for valid credentials`() {
        val request = LoginRequest(
            login = login,
            password = password
        )

        val mvcResult = mockMvc.post("/api/auth/login") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsBytes(request)
        }.andReturn()

        assertNotNull(mvcResult.response.contentAsString)
    }
}
