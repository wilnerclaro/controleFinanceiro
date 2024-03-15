package br.com.wilner.controleFinanceiro.repositories;

import br.com.wilner.controleFinanceiro.entities.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Transactional
    List<Transaction> findByUserId(Long userId);

    @Query("SELECT t FROM Transaction t WHERE t.category.name = :categoryName AND t.transactionDate BETWEEN :startDate AND :endDate")
    List<Transaction> findByCategoryIdAndTransactionDateBetween(String categoryName, LocalDate startDate, LocalDate endDate);

    @Transactional
    List<Transaction> findByUserName(String name);

    @Query("SELECT t FROM Transaction t WHERE t.category.name = :categoryName AND t.transactionDate BETWEEN :startOfDay AND :endOfDay")
    List<Transaction> findByCategoryNameAndTransactionDateBetween(String categoryName, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
