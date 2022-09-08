package springboottesting.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import springboottesting.dto.request.CarRequest;
import springboottesting.model.Car;
import springboottesting.repository.CarRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@AllArgsConstructor
public class CarService {

    private CarRepository carRepository;

    public Car save(CarRequest carRequest) {

        Car newCar = new Car(carRequest);

        return carRepository.save(newCar);
    }

    public List<Car> findAll() {
        return carRepository.findAll();
    }

    public Car findById(Long carId) {
        return carRepository.findById(carId)
                .orElseThrow(EntityNotFoundException::new);
    }

    public void deleteById(Long carId) {

        if (!carRepository.existsById(carId)) {
            throw new EntityNotFoundException(
                    "Car not found!"
            );
        }

        carRepository.deleteById(carId);
    }

    public Car updateById(Long carId, CarRequest carRequest) {

        if (!carRepository.existsById(carId)) {
            throw new EntityNotFoundException(
                    "Car not found!"
            );
        }

        Car car = new Car(carId, carRequest);

        return carRepository.save(car);
    }

    public List<Car> findAll(String search) {
        return carRepository.findAll(
                search,
                Sort.by(Sort.Order.desc("price"))
        );
    }
}
