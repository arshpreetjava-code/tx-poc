package com.testing.ex.domain.dto.request;

import com.testing.ex.domain.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * DTO for creating a new product.
 *
 * @param name the name of the product
 * @param sku the stock keeping unit of the product
 * @param price  the price of the product
 * @param description the description of the product
 * @param category the category of the product
 * @param features the features of the product
 */
public record CreateProduct(
    @NotBlank String name,
    @NotBlank String sku,
    @NotNull BigDecimal price,
    String description,
    @NotNull Category category,
    @NotNull CreateFeature features
) {
}
