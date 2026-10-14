package com.udea.lab1v2026;

import com.udea.lab1v2026.DTO.TransactionDTO;
import com.udea.lab1v2026.controller.TransactionController;
import com.udea.lab1v2026.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @Test
    void transferMoney_exitosa_devuelve200() {
        TransactionDTO dto = new TransactionDTO();
        when(transactionService.transferMoney(dto)).thenReturn(dto);

        ResponseEntity<?> response = transactionController.transferMoney(dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(dto, response.getBody());
    }

    @Test
    void transferMoney_conError_devuelve400ConMensaje() {
        TransactionDTO dto = new TransactionDTO();
        when(transactionService.transferMoney(dto))
                .thenThrow(new IllegalArgumentException("Saldo insuficiente"));

        ResponseEntity<?> response = transactionController.transferMoney(dto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Saldo insuficiente", response.getBody());
    }

    @Test
    void getTransactionsByAccount_devuelveLista() {
        when(transactionService.getTransactionsForAccount("123"))
                .thenReturn(List.of(new TransactionDTO()));

        List<TransactionDTO> result = transactionController.getTransactionsByAccount("123");

        assertEquals(1, result.size());
    }
}