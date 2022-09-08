package springboottesting.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import springboottesting.dto.request.CarRequest;
import springboottesting.dto.response.ExceptionResponse;
import springboottesting.model.Car;
import springboottesting.service.CarService;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/cars")
@AllArgsConstructor
@Getter
@Setter
public class CarAPI {

    private final CarService carService;

    // save
    @PostMapping
    Car save(@RequestBody @Valid CarRequest carRequest) {
        return carService.save(carRequest);
    }

    // find all
    @GetMapping("/all")
    List<Car> findAll(@RequestParam(required = false) String search) {
        return carService.findAll(search);
    }

    // find by id
    @GetMapping("/{carId}")
    Car findById(@PathVariable Long carId) {
        return carService.findById(carId);
    }

    // delete
    @DeleteMapping("/delete/{carId}")
    void deleteById(@PathVariable Long carId) {
        carService.deleteById(carId);
    }

    // update
    @PutMapping("/update/{carId}")
    Car update(@PathVariable Long carId,
               @RequestBody CarRequest carRequest) {
        return carService.updateById(carId, carRequest);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ExceptionResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {
        return new ExceptionResponse(methodArgumentNotValidException.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ExceptionResponse handleEntityNotFoundExceptionException(EntityNotFoundException entityNotFoundException) {
        return new ExceptionResponse(
                "not found"
        );
    }
}
