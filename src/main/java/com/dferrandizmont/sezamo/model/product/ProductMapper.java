package com.dferrandizmont.sezamo.model.product;

import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    void updateProductFromDTO(ProductDTO productDTO, @MappingTarget Product product);
}
