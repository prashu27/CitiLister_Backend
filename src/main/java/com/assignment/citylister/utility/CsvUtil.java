package com.assignment.citylister.utility;

import com.assignment.citylister.model.City;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.List;

public class CsvUtil {
    public static List<City> mapCsvToList(final String filepath) throws FileNotFoundException {

       File file = ResourceUtils.getFile(filepath);
       Reader reader = new BufferedReader(new FileReader(file));

        CsvToBean  csvReader = new CsvToBeanBuilder(reader).
                withType(City.class).
                withSeparator(',')
                .withIgnoreLeadingWhiteSpace(true)
                .withIgnoreEmptyLine(true).build();
        return csvReader.parse();

    }
    }
