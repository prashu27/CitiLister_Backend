package com.assignment.citylister.repository;

import com.assignment.citylister.model.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@EnableJpaRepositories
public interface CityRepository extends JpaRepository<City, Long> {
    List<City> findByNameContainsIgnoreCase(String name);

    @Override
    City save(City city);

    @Override
    void deleteById(Long aLong);

    Page<City> findAll(Pageable pageable);
}

