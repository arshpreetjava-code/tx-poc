package com.testing.ex.domain.dto.response;

import java.math.BigDecimal;

/**
 * DTO for product response.
 *
 * @param id the product ID
 * @param name the product name
 * @param sku the product SKU
 * @param category the product category
 * @param price the product price
 * @param response the product features
 * @param description the product description
 */
public record ProductResponse(
    Long id,
    String name,
    String sku,
    String category,
    BigDecimal price,
    FeatureResponse response,
    String description
) {
}
