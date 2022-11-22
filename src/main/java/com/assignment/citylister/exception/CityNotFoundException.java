package com.assignment.citylister.exception;

public class CityNotFoundException extends RuntimeException {
    public CityNotFoundException(String name) {
        super("City not found with name: " + name);
    }

    public CityNotFoundException(Long id) {
        super("City not found with id :" + id);
    }

    public CityNotFoundException() {
        super("No record Found");
    }

}
