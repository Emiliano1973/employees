package com.employees.demo.services.impl;

import com.employees.demo.dao.TitleDao;
import com.employees.demo.dtos.ResponseDto;
import com.employees.demo.services.TitleService;
import org.springframework.stereotype.Service;

@Service
public class TitleServiceImpl implements TitleService {
    private final TitleDao titleDao;

    public TitleServiceImpl(final TitleDao titleDao) {
        this.titleDao = titleDao;
    }


    @Override
    public ResponseDto getAllTitles() {
        return this.titleDao.getAllTitles();
    }
}
