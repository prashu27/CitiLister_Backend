package com.assignment.citylister.service;

import com.assignment.citylister.model.City;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CityDetailsService {

    List<City> getCityByName(String name);

    List<City> listAllCities();

    City editCityDetails(long id, City city);

    City getCityById(Long id);

    City addCity(City city);

    void deleteCityById(long id);

    List<City> getCityByPaging(int pageNum, int pageSize, String sortBy);
}
