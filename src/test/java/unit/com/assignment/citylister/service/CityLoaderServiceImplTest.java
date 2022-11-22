package com.assignment.citylister.service;

import com.assignment.citylister.model.City;
import com.assignment.citylister.repository.CityRepository;
import com.assignment.citylister.service.impl.LoadCsvServiceImpl;
import com.assignment.citylister.utility.CsvUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CityLoaderServiceImplTest {
    @Mock
    CityRepository cityRepository;
    @InjectMocks
    LoadCsvServiceImpl cityLoaderService;
    final List<City> cityList = List.of(
            new City(1L, "Tokyo", "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b2/Skyscrapers_of_Shinjuku_2009_January.jpg/500px-Skyscrapers_of_Shinjuku_2009_January.jpg"
            )
    );
    @Value("${cities.filepath}")
    private String filepath;

    @Test
    void shouldLoadCityMetadata() {
        try (MockedStatic mocked = mockStatic(CsvUtil.class)) {
            mocked.when( ()->CsvUtil.mapCsvToList(anyString())).thenReturn(cityList);
            Mockito.when(cityRepository.saveAllAndFlush(cityList)).thenReturn(cityList);
            List<City> cityList1 = cityLoaderService.loadCityMetadata();
            assert (cityList1.get(0).getName().equals(cityList.get(0).getName()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}