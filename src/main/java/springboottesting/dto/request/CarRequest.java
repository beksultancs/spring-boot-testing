package springboottesting.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CarRequest(
        @NotBlank(message = "brand must not be null or empty!") String brand,
        @NotBlank(message = "model must not be null or empty!") String model,
        @NotNull(message = "price must not be null!") BigDecimal price) {}
