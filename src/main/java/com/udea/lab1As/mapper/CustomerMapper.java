package com.udea.lab1As.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.udea.lab1As.dto.CustomerDto;
import com.udea.lab1As.entity.Customer;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);
    CustomerDto toDto(Customer customer);
    Customer toEntity(CustomerDto customerDto);
}
