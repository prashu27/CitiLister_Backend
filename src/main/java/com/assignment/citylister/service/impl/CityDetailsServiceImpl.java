package com.assignment.citylister.service.impl;

import com.assignment.citylister.exception.CityNotFoundException;
import com.assignment.citylister.model.City;
import com.assignment.citylister.repository.CityRepository;
import com.assignment.citylister.service.CityDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CityDetailsServiceImpl implements CityDetailsService {

    private final CityRepository cityRepository;

    public CityDetailsServiceImpl(final CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }


    @Override
    public List<City> getCityByName(final String name) {
        log.info("Getting City data from repository");
        List<City> cities = cityRepository.findByNameContainsIgnoreCase(name);

        if (cities.isEmpty()) {
            throw new CityNotFoundException(name);
        }
        return cities;
    }

    @Override
    public List<City> listAllCities() {
        log.info("get all cities data");
        List<City> cityList = cityRepository.findAll();
        if (cityList.isEmpty()) {
            throw new CityNotFoundException();
        }
        return cityList;
    }

    @Override
    public City editCityDetails(final long id, final City city) {
        cityRepository.findById(id).orElseThrow(() -> new CityNotFoundException(id));
        return cityRepository.saveAndFlush(city);
    }

    @Override
    public City getCityById(final Long id) {
        return cityRepository.findById(id).orElseThrow(() -> new CityNotFoundException(id));
    }

    /**
     * @param city
     * @return
     */
    @Override
    public City addCity(City city) {
        Optional<City> optionalCity = Optional.ofNullable(cityRepository.save(city));
        optionalCity.orElseThrow(() -> new EntityExistsException("City Already exists"));
        return optionalCity.get();
    }

    /**
     * @param id
     */
    @Override
    public void deleteCityById(long id) {
        cityRepository.findById(id).orElseThrow(() -> new CityNotFoundException(id));
        cityRepository.deleteById(id);
    }

    /**
     * @param pageNum
     * @param pageSize
     * @param sortBy
     * @return
     */
    @Override
    public List<City> getCityByPaging(int pageNum, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(sortBy));
        Page<City> pageResult = cityRepository.findAll(pageable);
        if (pageResult.hasContent()) {
            return pageResult.getContent();
        } else {
            return Collections.emptyList();
        }
    }
}
