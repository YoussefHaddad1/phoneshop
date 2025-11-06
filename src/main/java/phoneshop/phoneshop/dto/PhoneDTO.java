package phoneshop.phoneshop.dto;

import java.math.BigDecimal;
import phoneshop.phoneshop.model.Phone;

public record PhoneDTO(
        Long id,
        String model,
        BigDecimal price,
        String specs,
        Integer stock,
        BrandSummary brand
) {
    public static PhoneDTO from(Phone p) {
        return new PhoneDTO(
                p.getId(),
                p.getModel(),
                p.getPrice(),
                p.getSpecs(),
                p.getStock(),
                new BrandSummary(p.getBrand().getId(), p.getBrand().getName())
        );
    }
}