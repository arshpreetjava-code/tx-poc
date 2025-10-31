package com.testing.ex.domain.dto.request;

import com.testing.ex.domain.Category;
import java.math.BigDecimal;

/**
 * DTO for updating a product.
 *
 * @param name the name of the product
 * @param sku the stock keeping unit of the product
 * @param price  the price of the product
 * @param description the description of the product
 * @param category the category of the product
 * @param features the features of the product
 */
public record UpdateProduct(
    String name,
    String sku,
    BigDecimal price,
    String description,
    Category category,
    UpdateFeature features
) {
}
