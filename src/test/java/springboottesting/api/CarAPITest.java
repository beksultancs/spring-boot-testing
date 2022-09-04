package springboottesting.api;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import springboottesting.dto.request.CarRequest;
import springboottesting.model.Car;
import springboottesting.repository.CarRepository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CarAPITest {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CarRepository carRepository;

    private String baseUrl;

    @Test
    void save() {

        this.baseUrl = "http://localhost:" + port + "/api/cars";
        // given
        CarRequest carRequest = new CarRequest("Tesla", "Model Y", new BigDecimal("40000.00"));

        ResponseEntity<Car> response = restTemplate.postForEntity(
                baseUrl + "/save",
                carRequest,
                Car.class,
                Map.of(
                        "Content-Type", "application/json"
                ));

        // when
        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(response.getBody(), "response is null!");

        Car car = response.getBody();

        assertNotNull(car.getId(), "car id is null!");

        // then

        Optional<Car> carOptional = carRepository.findById(car.getId());

        assertTrue(carOptional.isPresent(), "Car not found!");

        assertEquals(
                new Car(car.getId(), carRequest),
                carOptional.get()
        );
    }

    @Test
    void findAll() {
    }

    @Test
    void findById() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void update() {
    }
}