package com.michael.marketprotfoliosystem.repo;

import com.michael.marketprotfoliosystem.model.Option;
import com.michael.marketprotfoliosystem.model.Security;
import com.michael.marketprotfoliosystem.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SecurityRepository extends JpaRepository<Security, Long> {

    @Query("SELECT s FROM Stock s")
    List<Stock> findAllStocks();

    @Query("SELECT o FROM Option o")
    List<Option> findAllOptions();
}