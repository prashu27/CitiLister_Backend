package com.assignment.citylister.controller;

import com.assignment.citylister.model.City;
import com.assignment.citylister.model.CityDto;
import com.assignment.citylister.model.mapper.CityDtoToCityMapper;
import com.assignment.citylister.service.CityDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping("api/cities")
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Validated
public class CityController {
    @Autowired
    CityDetailsService cityDetailsService;

    @GetMapping("")
    public ResponseEntity<List<City>> getAllCities() {
        log.info("Received request for  getAll Cities");
        List<City> cities = cityDetailsService.listAllCities();
        return new ResponseEntity<>(cities, HttpStatus.OK);
    }

    @GetMapping(value = "/", params = {"name"})
    public ResponseEntity<List<CityDto>> getCitiesByName(@NotBlank @RequestParam("name") String name) {
        log.info("Received communication request for city by {}", name);
        List<City> cities = cityDetailsService.getCityByName(name);
        List<CityDto> cityDtoList = CityDtoToCityMapper.INSTANCE.mapCityListToCityDtoList(cities);
        return new ResponseEntity<>(cityDtoList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<City> getCitiesById(@PathVariable Long id) {
        log.info("Received communication request for city by {}", id);
        City city = cityDetailsService.getCityById(id);
        CityDtoToCityMapper.INSTANCE.mapCityToCityDto(city);
        return new ResponseEntity<>(city, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<City> editCity(@PathVariable long id, @Valid @RequestBody CityDto cityDto) {
        log.info("Received edit city details request for city id : {} and RequestObject : {}", id, cityDto);
        City city = CityDtoToCityMapper.INSTANCE.mapCityDtoTOCity(cityDto);
        City updatedCity = cityDetailsService.editCityDetails(id, city);
        CityDtoToCityMapper.INSTANCE.mapCityToCityDto(updatedCity);
        log.info("edite city request completed and updated object: {}", updatedCity);
        return new ResponseEntity<>(updatedCity, HttpStatus.ACCEPTED);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("/")
    public ResponseEntity<CityDto> addCity(@RequestBody @Valid CityDto cityDto) {
        log.info("Communication request to add city : {}", cityDto);
        City requestObj = CityDtoToCityMapper.INSTANCE.mapCityDtoTOCity(cityDto);
        City responseObj = cityDetailsService.addCity(requestObj);
        log.info("Response object :{}", responseObj);
        return new ResponseEntity<>(CityDtoToCityMapper.INSTANCE.mapCityToCityDto(responseObj), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCityById(@PathVariable long id) {
        log.info("Communication request to delete city with id : {}", id);
        cityDetailsService.deleteCityById(id);
        log.info("city has been deleted with id : {}", id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/")
    public ResponseEntity<List<CityDto>> getAllCitiesWithPagination(
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "name") String sortBy
    ) {
        List<City> cityList = cityDetailsService.getCityByPaging(pageNum, pageSize, sortBy);
        return new ResponseEntity<>(CityDtoToCityMapper.INSTANCE.mapCityListToCityDtoList(cityList), HttpStatus.ACCEPTED);
    }


}
