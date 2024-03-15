package com.employees.demo.utils;

import java.util.Objects;
import java.util.stream.Stream;

public enum Gender {

    MALE("M"), FEMALE("F");

    private  final String sex;

    Gender(final String sex) {
        this.sex = sex;
    }

    public String getSex() {
        return sex;
    }

    public static Gender getGenderByString(final String sex){
        Objects.requireNonNull(sex, "Gender string cannot be null");
       return Stream.of(values()).filter(v-> v.getSex().equalsIgnoreCase(sex)).findFirst().orElseThrow(()-> new IllegalArgumentException("Error , ["+sex+"] string is not a valid gender"));
    }
}
