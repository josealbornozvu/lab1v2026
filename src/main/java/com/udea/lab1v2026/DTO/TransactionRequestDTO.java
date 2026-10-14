package com.udea.lab1v2026.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequestDTO {
    private String senderAccountNumber;
    private String receiverAccountNumber;
    private Double amount;
}