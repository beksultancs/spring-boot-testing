package springboottesting;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import springboottesting.dto.request.AuthRequest;
import springboottesting.dto.response.AuthResponse;
import springboottesting.model.Authority;
import springboottesting.model.User;
import springboottesting.repository.UserRepository;
import springboottesting.util.JwtUtil;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Slf4j
public class AuthApiIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        User newUser = new User(
                null,
                "testName",
                "javafive@gmail.com",
                passwordEncoder.encode("javafive"),
                Authority.USER,
                new ArrayList<>()
        );
        userRepository.save(newUser);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void testAuthentication() {

        String url = "http://localhost:" + port + "/api/authentication";

        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
                url,
                new AuthRequest("javafive@gmail.com", "javafive"),
                AuthResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());

        AuthResponse authResponse = response.getBody();

        assertNotNull(authResponse.getJwt());

        assertDoesNotThrow(() -> {
            jwtUtil.verifyJwtTokenAndGetEmail(authResponse.getJwt());
        },"generated invalid jwt");
    }

}
