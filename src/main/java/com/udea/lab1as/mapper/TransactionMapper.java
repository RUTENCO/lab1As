package com.udea.lab1as.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.udea.lab1as.dto.TransactionDto;
import com.udea.lab1as.entity.Transaction;

@Mapper(componentModel = "spring") // para que Spring gestione el mapper
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class); // obtener la instancia del mapper
    TransactionDto toDto(Transaction transaction); // convertir entidad a DTO
    Transaction toEntity(TransactionDto transactionDto); // convertir DTO a entidad
}
