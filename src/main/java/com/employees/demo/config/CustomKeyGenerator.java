package com.employees.demo.config;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component("customKeyGenerator")
public class CustomKeyGenerator implements KeyGenerator {
    private static final String DELIMITER="_";
    @Override
    public Object generate(final Object target,
                           final Method method, final Object... params) {
       final  StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(target.getClass().getName()).append(DELIMITER)
                .append(method.getName());
        if( Objects.isNull(params) || params.length==0){
            return  stringBuilder.toString();
        }
        stringBuilder.append(DELIMITER)
                .append(Stream.of(params).filter(Objects::nonNull)
                .map(Object::toString).collect(Collectors.joining(DELIMITER)));
        return stringBuilder.toString();
    }
}
