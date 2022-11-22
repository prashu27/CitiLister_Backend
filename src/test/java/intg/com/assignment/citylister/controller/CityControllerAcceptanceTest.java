package com.assignment.citylister.controller;

import com.assignment.citylister.model.City;
import com.assignment.citylister.model.CityDto;
import com.assignment.citylister.utility.CsvUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.FileNotFoundException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
class CityControllerAcceptanceTest {

    @Autowired
    MockMvc mockMvc;

    String baseUrl = "/api/cities";
    @Autowired
    ObjectMapper objectMapper;


    @Test
    void shouldGetCitiesByName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/").
                        queryParam("name", "tokyo")).
                andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldFaildGetCitiesByName() throws Exception {
        Long id =1l;
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/"+id+"/marks")).
                andExpect(MockMvcResultMatchers.status().isOk ());


    }

    @Test
    void shouldGetCitiesById() throws Exception {
        long id = 1L;
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/" + id)).
                andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldFailGetCitiesById() throws Exception {
        long id = 1001L;
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/" + id)).
                andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void ShouldEditCity() throws Exception {
        long id = 1L;
        City c = new City(1L, "abc", "https://upload.wikimedia.org/wikipedia/commons/thumb/5/55/IN-DL.svg/439px-IN-DL.svg.png");
        String JsonRequestObj = objectMapper.writeValueAsString(c);
        mockMvc.perform(MockMvcRequestBuilders.put(baseUrl + "/" + id).
                        content(JsonRequestObj).contentType(MediaType.APPLICATION_JSON)).
                andDo(print()).andExpect(MockMvcResultMatchers.status().isAccepted());
    }

    @Test
    void ShouldFailEditCity() throws Exception {
        long id = 1L;
        City c = new City(1L, " ", "://upload.wikimedia.org/wikipedia/commons/thumb/5/55/IN-DL.svg/439px-IN-DL.svg.png");
        String JsonRequestObj = objectMapper.writeValueAsString(c);
        mockMvc.perform(MockMvcRequestBuilders.put(baseUrl + "/" + id).
                        content(JsonRequestObj).contentType(MediaType.APPLICATION_JSON)).
                andDo(print()).
                andExpect(MockMvcResultMatchers.status().isBadRequest()).
                andReturn().
                getResolvedException().
                getMessage().matches("Input Validation failed");


    }

    @Test
    void ShouldFailEditCity404() throws Exception {
        long id = 1L;
        City c = new City(10001L, " ", "://upload.wikimedia.org/wikipedia/commons/thumb/5/55/IN-DL.svg/439px-IN-DL.svg.png");
        String JsonRequestObj = objectMapper.writeValueAsString(c);
        mockMvc.perform(MockMvcRequestBuilders.put(baseUrl + "/" + id).
                        content(JsonRequestObj).
                        contentType(MediaType.APPLICATION_JSON)).
                andDo(print()).
                andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    void ShouldGetAllCities() throws Exception {
        List<City> cityList = getMockAllCity();
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl).
                        contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).
                andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(jsonPath("$.size()", is(cityList.size())));

    }

    @Test
    void shouldDeleteCity() throws Exception {
        long id = 10L;
        mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl + "/" + id)).
                andExpect(MockMvcResultMatchers.status().isAccepted());
    }

    @Test
    void shouldFailDeleteCity() throws Exception {
        long id = 10000000L;
        mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl + "/" + id)).
                andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void ShouldFailAddCity() throws Exception {
        CityDto requestObj = new CityDto(10000L, "prashansa", "www/sds.ssd.jpg");
        String jsonRequestString = objectMapper.writeValueAsString(requestObj);
        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/").
                  content(jsonRequestString).contentType(MediaType.APPLICATION_JSON)).
                 andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(print()).
                andReturn()
                .getResolvedException()
                .getMessage()
                .matches("Input Validation failed");
    }

    @Test
    void ShouldAddCity() throws Exception {
        City requestObj = new City(1L, "jakarta", "https://upload.wikimedia.org/wikipedia/commons/thumb/5/55/IN-DL.svg/439px-IN-DL.svg.png");
        String jsonRequestString = objectMapper.writeValueAsString(requestObj);
        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "/").
                        content(jsonRequestString).contentType(MediaType.APPLICATION_JSON)).
                andExpect(MockMvcResultMatchers.status().isCreated()).
                andExpect(MockMvcResultMatchers.jsonPath("$.name").value("jakarta"))
                .andDo(print());

    }
    private List<City> getMockAllCity() throws FileNotFoundException {
        return CsvUtil.mapCsvToList("classpath:cities.csv");
    }


}