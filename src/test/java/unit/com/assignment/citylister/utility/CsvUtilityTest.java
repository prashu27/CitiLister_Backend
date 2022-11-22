package com.assignment.citylister.utility;

import com.assignment.citylister.model.City;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@TestPropertySource(properties = {
        "cities.filepath = C:/Users/v-prasshukla/Downloads/test_task/cities.csv"})
@SpringBootTest
class CsvUtilityTest {

    @Value("${cities.filepath}")
    private String filepath;

    @Test
    void convertCsvToList() throws FileNotFoundException {
        List<City> cityList = new ArrayList<>();
        City expectedObj = new City(1L, "Tokyo", "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b2/Skyscrapers_of_Shinjuku_2009_January.jpg/500px-Skyscrapers_of_Shinjuku_2009_January.jpg"
        );
        cityList.add(expectedObj);
        List<City> cities = CsvUtil.mapCsvToList(filepath);
        assert (cities.get(0).getName().equals(cityList.get(0).getName()));
    }

}