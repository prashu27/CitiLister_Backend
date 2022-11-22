package com.assignment.citylister.controller;

import com.assignment.citylister.exception.CityNotFoundException;
import com.assignment.citylister.model.City;
import com.assignment.citylister.service.CityDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CityController.class)
class CityControllerTest {
    final String baseUrl = "/api/cities";
    final List<City> cityList = Arrays.asList(
            new City(1L, "abc", "https://upload.wikimedia.org/wikipedia/commons/thumb/5/55/IN-DL.svg/439px-IN-DL.svg.png"),
            new City(2L, "xyz", "https://upload.wikimedia.org/wikipedia/commons/thumb/5/55/IN-DL.ssa/439px-IN-DL.svg.png")
    );
    @Autowired
    MockMvc mockMvc;
    @MockBean
    CityDetailsService cityDetailsService;
    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {

        MockitoAnnotations.openMocks(this);
    }


    @Test
    void shouldGetCitiesByName() throws Exception {

        Mockito.when(cityDetailsService.getCityByName(anyString())).thenReturn(cityList);
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/").queryParam("name", "tokyo")).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldFailGetCitiesByName() throws Exception {
        String name = "www";
        Mockito.when(cityDetailsService.getCityByName(anyString())).thenThrow(new CityNotFoundException(name));
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/").queryParam("name", name)).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldGetCitiesById() throws Exception {
        long id = 1L;
        Mockito.when(cityDetailsService.getCityById(anyLong())).thenReturn(new City(1L, "abc", "https://upload.wikimedia.org/wikipedia/commons/thumb/5/55/IN-DL.svg/439px-IN-DL.svg.png"));
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/" + id)).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldFailGetCitiesById() throws Exception {
        long id = 1001L;
        Mockito.when(cityDetailsService.getCityById(anyLong())).thenThrow(new CityNotFoundException(anyLong()));
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/" + id)).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void ShouldEditCity() throws Exception {
        long id = 1L;
        City actualObj = new City(1L, "abc", "https://upload.wikimedia.org/wikipedia/commons/thumb/5/55/IN-DL.svg/439px-IN-DL.svg.png");
        City expectedObj = new City(1L, "xyz", "https://upload.wikimedia.org/wikipedia/commons/thumb/5/55/IN-DL.svg/439px-IN-DL.svg.png");
        Mockito.when(cityDetailsService.editCityDetails(anyLong(), any(City.class))).thenReturn(expectedObj);
        String JsonRequestObj = objectMapper.writeValueAsString(actualObj);
        mockMvc.perform(MockMvcRequestBuilders.put(baseUrl + "/" + id).content(JsonRequestObj).contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(MockMvcResultMatchers.status().isAccepted());
    }


    @Test
    void ShouldFailEditCity404() throws Exception {
        long id = 1L;
        City c = new City(10001L, " ", "://upload.wikimedia.org/wikipedia/commons/thumb/5/55/IN-DL.svg/439px-IN-DL.svg.png");
        Mockito.when(cityDetailsService.getCityById(anyLong())).thenThrow(CityNotFoundException.class);
        String JsonRequestObj = objectMapper.writeValueAsString(c);
        mockMvc.perform(MockMvcRequestBuilders.put(baseUrl + "/" + id).content(JsonRequestObj).contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    void ShouldGetAllCities() throws Exception {
        Mockito.when(cityDetailsService.listAllCities()).thenReturn(cityList);
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl).
                        contentType(MediaType.APPLICATION_JSON)).
                andDo(print()).
                andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andExpect(jsonPath("$.size()", is(cityList.size())));
    }

    @Test
    void ShouldFailGetAllCities() throws Exception {
        Mockito.when(cityDetailsService.listAllCities()).thenThrow(CityNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl).
                        contentType(MediaType.APPLICATION_JSON)).
                andDo(print()).
                andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
}