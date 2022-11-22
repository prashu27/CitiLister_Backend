package com.assignment.citylister.model.mapper;

import com.assignment.citylister.model.City;
import com.assignment.citylister.model.CityDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CityDtoToCityMapper {

    CityDtoToCityMapper INSTANCE = Mappers.getMapper(CityDtoToCityMapper.class);

    City mapCityDtoTOCity(CityDto cityDto);

    CityDto mapCityToCityDto(City city);

    List<City> mapCityDtoListToCityList(List<CityDto> cityDtoList);

    List<CityDto> mapCityListToCityDtoList(List<City> city);

}
