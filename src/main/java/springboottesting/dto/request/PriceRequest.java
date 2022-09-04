package springboottesting.dto.request;

import java.math.BigDecimal;

public record PriceRequest(BigDecimal from, BigDecimal to) {
}
