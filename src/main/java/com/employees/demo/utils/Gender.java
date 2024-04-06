package com.employees.demo.utils;

import java.util.Objects;
import java.util.stream.Stream;

public enum Gender {

    MALE("M"), FEMALE("F");

    private  final String gender;

    Gender(final String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public static Gender getGenderByString(final String gender){
        Objects.requireNonNull(gender, "Gender string cannot be null");
       return Stream.of(values()).filter(v-> v.getGender().equalsIgnoreCase(gender)).findFirst().orElseThrow(()->
               new IllegalArgumentException("Error , ["+gender+"] string is not a valid gender"));
    }
}
