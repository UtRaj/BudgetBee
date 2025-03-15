package com.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.backend.model.Expense;
import com.backend.dto.ExpenseDTO;

import java.util.ArrayList;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    ArrayList<Expense> findByUserEmail(String email);
}
