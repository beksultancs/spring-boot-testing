package springboottesting.api;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import springboottesting.dto.request.CarRequest;
import springboottesting.model.Car;
import springboottesting.repository.CarRepository;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Slf4j
class CarAPITest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CarRepository carRepository;

    @Test
    @Order(1)
    void save() {

        long first = carRepository.count();

        String url = "http://localhost:" + port + "/api/cars";

        ResponseEntity<Car> response = restTemplate.postForEntity(
                url,
                new CarRequest("Tesla", "Model Y", BigDecimal.valueOf(40000.00)),
                Car.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Car responseBody = response.getBody();

        System.out.println("responseBody = " + responseBody);

        assertNotNull(responseBody);

        long second = carRepository.count();

        assertEquals(first + 1, second);
    }

    @Test
    @Order(2)
    void findById() {

        Car saved = carRepository.save(
                new Car(
                        null,
                        "Mercedes",
                        "S Class",
                        new BigDecimal("100000.00")
                )
        );

        String url = "http://localhost:" + port + "/api/cars/";

        ResponseEntity<Car> response = restTemplate.getForEntity(
                url + saved.getId(),
                Car.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        ResponseEntity<Car> response2 = restTemplate.getForEntity(
                url + 123454L,
                Car.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response2.getStatusCode());
    }
}