package springboottesting.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import springboottesting.model.Car;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class CarRepositoryTest {

    @Autowired
    private CarRepository carRepository;

    @Test
    @Sql(scripts = "/scripts/save-cars.sql")
    void findAll() {

        List<Car> cars = carRepository.findAll("Model A", Sort.by("price"));

        cars.forEach(System.out::println);

        assertEquals(2L, cars.size());
    }
}