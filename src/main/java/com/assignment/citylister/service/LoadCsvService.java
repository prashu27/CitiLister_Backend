package com.assignment.citylister.service;

import com.assignment.citylister.model.City;

import java.io.FileNotFoundException;
import java.util.List;

public interface LoadCsvService {

    List<City> loadCityMetadata() throws FileNotFoundException;


}
