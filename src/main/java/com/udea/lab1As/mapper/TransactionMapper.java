package com.udea.lab1As.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.udea.lab1As.dto.TransactionDto;
import com.udea.lab1As.entity.Transaction;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);
    TransactionDto toDto(Transaction transaction);
    Transaction toEntity(TransactionDto transactionDto);
}
