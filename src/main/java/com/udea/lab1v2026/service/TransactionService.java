package com.udea.lab1v2026.service;

import com.udea.lab1v2026.DTO.TransactionDTO;
import com.udea.lab1v2026.entity.Customer;
import com.udea.lab1v2026.entity.Transaction;
import com.udea.lab1v2026.repository.CustomerRepository;
import com.udea.lab1v2026.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              CustomerRepository customerRepository) {
        this.transactionRepository = transactionRepository;
        this.customerRepository = customerRepository;
    }

    public TransactionDTO transferMoney(TransactionDTO transactionDTO) {
        // ... (igual que lo tienes, pero cambia el final)

        transaction = transactionRepository.save(transaction);
        return toDTO(transaction);
    }

    public List<TransactionDTO> getTransactionsForAccount(String accountNumber) {
        return transactionRepository
                .findBySenderAccountNumberOrReceiverAccountNumber(accountNumber, accountNumber)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private TransactionDTO toDTO(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setSenderAccountNumber(transaction.getSenderAccountNumber());
        dto.setReceiverAccountNumber(transaction.getReceiverAccountNumber());
        dto.setAmount(transaction.getAmount());
        dto.setTimestamp(transaction.getTimestamp());
        return dto;
    }
}