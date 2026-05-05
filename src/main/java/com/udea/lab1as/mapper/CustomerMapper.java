package com.udea.lab1as.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.udea.lab1as.dto.CustomerDto;
import com.udea.lab1as.entity.Customer;

@Mapper(componentModel = "spring") // para que Spring gestione el mapper
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class); // obtener la instancia del mapper
    CustomerDto toDto(Customer customer); // convertir entidad a DTO
    Customer toEntity(CustomerDto customerDto); // convertir DTO a entidad
}
