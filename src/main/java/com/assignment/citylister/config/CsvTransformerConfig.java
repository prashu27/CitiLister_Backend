package com.assignment.citylister.config;

import com.assignment.citylister.service.LoadCsvService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;

@Configuration
@Slf4j
public class CsvTransformerConfig {
    @Autowired
    LoadCsvService service;

    @PostConstruct
    public void initLoader() throws FileNotFoundException {
        log.info("fetch data from Csv file");
        service.loadCityMetadata();
    }
}
