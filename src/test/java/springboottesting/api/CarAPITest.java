package springboottesting.api;

import org.junit.jupiter.api.AfterEach;
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
import org.springframework.test.context.jdbc.Sql;
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

    @BeforeEach
    void beforeEach() {
        this.baseUrl = "http://localhost:" + port + "/api/cars";
    }

    @AfterEach
    void afterEach() {
        carRepository.deleteAll();
    }

    @Test
    void save() {
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

    // TODO: 5/9/22 should throw an exception when we give invalid data

    @Test
    void shouldThrowAndExceptionWhenGivingInvalidData() {
        // invalid brand
        Car testCar = new Car(null, "", "RS Q8", new BigDecimal("50000.00"));

        ResponseEntity<Car> response = restTemplate.postForEntity(
                baseUrl + "/save",
                testCar,
                Car.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // invalid model
        // invalid price
    }

    @Test
    @Sql("/new-cars.sql")
    void findAll() {

        ResponseEntity<Car[]> response = restTemplate.getForEntity(
                baseUrl + "/all",
                Car[].class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status code not as expected!");
    }

    @Test
    void findById() {
        // given
        Car expected = saveNewCar();

        // when
        ResponseEntity<Car> response = restTemplate.getForEntity(
                baseUrl + "/find/" + expected.getId(),
                Car.class
        );
        // then

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response status code not as expected!");

        assertEquals(
                expected,
                response.getBody(),
                "api find by id do not work correctly!"
        );
    }

    // TODO: 5/9/22 should throw an exception when trying to get non existing object

    private Car saveNewCar() {
        return carRepository.save(
                new Car(null, "Audi", "RS Q8", new BigDecimal("50000.00"))
        );
    }

    @Test
    void deleteById() {
        // given
        Car newSavedCar = saveNewCar();

        // when
        restTemplate.delete(
                baseUrl + "/delete/" + newSavedCar.getId()
        );

        // then
        assertFalse(
                carRepository.existsById(newSavedCar.getId()),
                "Delete method do not works correctly!"
        );
    }

    // TODO: 5/9/22 shoult throw an exception when trying to delete not existing object

    @Test
    void update() {
        // given
        Car newSavedCar = saveNewCar();

        // when
        CarRequest carRequest = new CarRequest("Volkswagen", "Tiguan", new BigDecimal("10000.00"));

        restTemplate.put(
                baseUrl + "/update/" + newSavedCar.getId(),
                carRequest
        );

        // then
        Optional<Car> carOptional = carRepository.findById(newSavedCar.getId());

        assertTrue(carOptional.isPresent());

        Car car = carOptional.get();

        assertEquals(
                new Car(newSavedCar.getId(), carRequest),
                car
        );
    }
}