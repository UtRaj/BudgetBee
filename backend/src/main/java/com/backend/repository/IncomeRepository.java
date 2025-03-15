package com.backend.repository;

import com.backend.model.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {
    ArrayList<Income> findByUserEmail(String email);
}
