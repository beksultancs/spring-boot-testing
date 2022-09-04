package springboottesting.model;

import lombok.*;
import springboottesting.dto.request.CarRequest;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "cars")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;

    private String model;

    private BigDecimal price;

    public Car(CarRequest carRequest) {
        this.brand = carRequest.brand();
        this.model = carRequest.model();
        this.price = carRequest.price();
    }

    public Car(Long carId, CarRequest carRequest) {
        this.id = carId;
        this.brand = carRequest.brand();
        this.model = carRequest.model();
        this.price = carRequest.price();
    }
}
