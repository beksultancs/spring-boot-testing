package springboottesting.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import springboottesting.model.Car;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.domain.Sort.Order.*;

// hello guys it's not unit test!

@DataJpaTest
@Transactional
@ActiveProfiles(value = "test")
@Slf4j
class CarRepositoryTest {

    @Autowired
    private CarRepository carRepository;

    @Test
    @Sql("/new-cars.sql")
    void findAll() {

        String search = null;

        List<Car> cars = carRepository.findAll(search, Sort.by(desc("price")));

        assertEquals(5, cars.size());

        assertTrue(() -> {
            List<Car> carsContainsT = carRepository.findAll("T", Sort.by(desc("price")));
            for (Car car : carsContainsT) {
                if (car.getModel().toUpperCase().contains("T") || car.getBrand().toUpperCase().contains("T")) {
                    log.info("{}", car);
                } else {
                    return false;
                }
            }
            return true;
        });

        List<Car> expected = carRepository.findAll(Sort.by(desc("price")));
        List<Car> result = carRepository.findAll(search, Sort.by(desc("price")));

        assertEquals(expected, result);
    }
}

