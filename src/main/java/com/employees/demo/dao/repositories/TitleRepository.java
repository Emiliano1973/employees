package com.employees.demo.dao.repositories;

import com.employees.demo.entities.Title;
import com.employees.demo.entities.pk.TitlePk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TitleRepository  extends JpaRepository<Title, TitlePk> {
}
