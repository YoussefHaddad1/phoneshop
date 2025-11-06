package phoneshop.phoneshop.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderItemRequest(
    @NotNull Long phoneId,
    @Min(1) Integer quantity
) {}
