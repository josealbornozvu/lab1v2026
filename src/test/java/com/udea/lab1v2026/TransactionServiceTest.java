package com.udea.lab1v2026;

import com.udea.lab1v2026.DTO.TransactionDTO;
import com.udea.lab1v2026.entity.Customer;
import com.udea.lab1v2026.entity.Transaction;
import com.udea.lab1v2026.repository.CustomerRepository;
import com.udea.lab1v2026.repository.TransactionRepository;
import com.udea.lab1v2026.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Customer customer(String account, double balance) {
        Customer c = new Customer();
        c.setAccountNumber(account);
        c.setBalance(balance);
        return c;
    }

    @Test
    void transferMoney_conRemitenteNulo_lanzaExcepcion() {
        TransactionDTO dto = new TransactionDTO(null, null, "456", 100.0, null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> transactionService.transferMoney(dto));

        assertEquals("Los números de cuenta del remitente y receptor son obligatorios.", ex.getMessage());
    }

    @Test
    void transferMoney_conReceptorNulo_lanzaExcepcion() {
        TransactionDTO dto = new TransactionDTO(null, "123", null, 100.0, null);

        assertThrows(IllegalArgumentException.class, () -> transactionService.transferMoney(dto));
    }

    @Test
    void transferMoney_remitenteInexistente_lanzaExcepcion() {
        TransactionDTO dto = new TransactionDTO(null, "123", "456", 100.0, null);
        when(customerRepository.findByAccountNumber("123")).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> transactionService.transferMoney(dto));

        assertEquals("La cuenta del remitente no existe.", ex.getMessage());
    }

    @Test
    void transferMoney_receptorInexistente_lanzaExcepcion() {
        TransactionDTO dto = new TransactionDTO(null, "123", "456", 100.0, null);
        when(customerRepository.findByAccountNumber("123"))
                .thenReturn(Optional.of(customer("123", 500.0)));
        when(customerRepository.findByAccountNumber("456")).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> transactionService.transferMoney(dto));

        assertEquals("La cuenta del receptor no existe.", ex.getMessage());
    }

    @Test
    void transferMoney_saldoInsuficiente_lanzaExcepcionYNoGuarda() {
        TransactionDTO dto = new TransactionDTO(null, "123", "456", 1000.0, null);
        when(customerRepository.findByAccountNumber("123"))
                .thenReturn(Optional.of(customer("123", 500.0)));
        when(customerRepository.findByAccountNumber("456"))
                .thenReturn(Optional.of(customer("456", 0.0)));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> transactionService.transferMoney(dto));

        assertEquals("Saldo insuficiente en la cuenta del remitente.", ex.getMessage());
        verify(customerRepository, never()).save(any());
        verifyNoInteractions(transactionRepository);
    }

    @Test
    void transferMoney_exitosa_actualizaSaldosYAsignaTimestamp() {
        Customer sender = customer("123", 500.0);
        Customer receiver = customer("456", 50.0);
        TransactionDTO dto = new TransactionDTO(null, "123", "456", 100.0, null);

        when(customerRepository.findByAccountNumber("123")).thenReturn(Optional.of(sender));
        when(customerRepository.findByAccountNumber("456")).thenReturn(Optional.of(receiver));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(inv -> {
            Transaction t = inv.getArgument(0);
            t.setId(1L);
            return t;
        });

        TransactionDTO result = transactionService.transferMoney(dto);

        assertEquals(400.0, sender.getBalance(), 0.001);
        assertEquals(150.0, receiver.getBalance(), 0.001);
        assertEquals(1L, result.getId());
        assertEquals("123", result.getSenderAccountNumber());
        assertEquals("456", result.getReceiverAccountNumber());
        assertEquals(100.0, result.getAmount(), 0.001);
        assertNotNull(result.getTimestamp());
        verify(customerRepository, times(2)).save(any(Customer.class));
    }

    @Test
    void transferMoney_conTimestampDelCliente_loConserva() {
        LocalDateTime fecha = LocalDateTime.of(2026, 1, 15, 10, 30);
        TransactionDTO dto = new TransactionDTO(null, "123", "456", 100.0, fecha);

        when(customerRepository.findByAccountNumber("123"))
                .thenReturn(Optional.of(customer("123", 500.0)));
        when(customerRepository.findByAccountNumber("456"))
                .thenReturn(Optional.of(customer("456", 0.0)));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));

        TransactionDTO result = transactionService.transferMoney(dto);

        assertEquals(fecha, result.getTimestamp());
    }

    @Test
    void getTransactionsForAccount_devuelveListaDeDTOs() {
        LocalDateTime fecha = LocalDateTime.of(2026, 1, 15, 10, 30);
        Transaction t = new Transaction(1L, "123", "456", 100.0, fecha);
        when(transactionRepository.findBySenderAccountNumberOrReceiverAccountNumber("123", "123"))
                .thenReturn(List.of(t));

        List<TransactionDTO> result = transactionService.getTransactionsForAccount("123");

        assertEquals(1, result.size());
        assertEquals("456", result.get(0).getReceiverAccountNumber());
        assertEquals(fecha, result.get(0).getTimestamp());
    }
}