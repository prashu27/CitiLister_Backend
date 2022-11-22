package com.assignment.citylister.service.impl;

import com.assignment.citylister.exception.CityNotFoundException;
import com.assignment.citylister.model.City;
import com.assignment.citylister.repository.CityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@SpringBootTest
@ExtendWith( MockitoExtension.class)
class CityDetailsServiceImplTest {

    @MockBean
    CityRepository cityRepository;

    @InjectMocks
    CityDetailsServiceImpl cityDetailsService;

    @Test
    void shouldGetCityByName() {
        List<City> cities = new ArrayList<>();
        cities.add(new City(1L, "Tokyo", "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b2/Skyscrapers_of_Shinjuku_2009_January.jpg/500px-Skyscrapers_of_Shinjuku_2009_January.jpg"));
        Mockito.when(cityRepository.findByNameContainsIgnoreCase(any())).thenReturn(cities);
        List<City> cities1 = cityDetailsService.getCityByName("Tokyo");
        assert (cities1.size() == cities.size());
    }

    @Test
    void shouldFailGetCityByName() {
        List<City> cities = new ArrayList<>();
        Mockito.when(cityRepository.findByNameContainsIgnoreCase(any())).thenReturn(cities);
        assertThrows(CityNotFoundException.class, () -> cityDetailsService.getCityByName("XX"));
    }

    @Test
    void shouldRetrievelistAllCities() {
        List<City> cities = new ArrayList<>();
        cities.add(new City(1L, "Tokyo", "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b2/Skyscrapers_of_Shinjuku_2009_January.jpg/500px-Skyscrapers_of_Shinjuku_2009_January.jpg"));
        Mockito.when(cityRepository.findAll()).thenReturn(cities);
        List<City> cities1 = cityDetailsService.listAllCities();
        assert (cities1.size() == cities.size());

    }

    @Test
    void shouldNotRetrievelistAllCities() {
        List<City> cities = new ArrayList<>();
        Mockito.when(cityRepository.findAll()).thenReturn(cities);
        assertThrows(CityNotFoundException.class, () -> cityDetailsService.listAllCities());

    }

    @Test
    void shouldEditCityDetails() {
        City actual = new City(1L, "Tokyo", "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b2/Skyscrapers_of_Shinjuku_2009_January.jpg/500px-Skyscrapers_of_Shinjuku_2009_January.jpg");
        City updated = new City(1L, "Japan", "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b2/Skyscrapers_of_Shinjuku_2009_January.jpg/500px-Skyscrapers_of_Shinjuku_2009_January.jpg");
        Mockito.when(cityRepository.findById(anyLong())).thenReturn(Optional.of(actual));
        Mockito.when(cityRepository.saveAndFlush(any(City.class))).thenReturn(updated);
        City city = cityDetailsService.editCityDetails(1L, actual);
        System.out.println("City obj"+ city);
        assertEquals(city, updated);
    }

    @Test
    void shouldNotEditCityDetails() {
        City updated = new City(1L, "Japan", "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b2/Skyscrapers_of_Shinjuku_2009_January.jpg/500px-Skyscrapers_of_Shinjuku_2009_January.jpg");
        Mockito.when(cityRepository.findById(anyLong())).thenThrow(CityNotFoundException.class);
        assertThrows(CityNotFoundException.class, () -> cityDetailsService.editCityDetails(21L, updated));
    }

}