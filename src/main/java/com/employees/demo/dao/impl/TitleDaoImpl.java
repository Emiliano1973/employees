package com.employees.demo.dao.impl;

import com.employees.demo.dao.TitleDao;
import com.employees.demo.dtos.DropDownDto;
import com.employees.demo.dtos.ResponseDto;
import com.employees.demo.entities.Title;
import com.employees.demo.entities.Title_;
import com.employees.demo.entities.pk.TitlePk_;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public class TitleDaoImpl implements TitleDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public ResponseDto getAllTitles() {
        CriteriaBuilder cb=this.em.getCriteriaBuilder();
        CriteriaQuery<DropDownDto> criteriaQuery = cb.createQuery(DropDownDto.class);
        Root<Title> titleRoot = criteriaQuery.from(Title.class);
        criteriaQuery.distinct(true).multiselect( titleRoot.get(Title_.titleId).get(TitlePk_.title),
                        titleRoot.get(Title_.titleId).get(TitlePk_.title))
                .orderBy(cb.asc(titleRoot.get(Title_.titleId).get(TitlePk_.title)));
        TypedQuery<DropDownDto> typedQuery=this.em.createQuery(criteriaQuery);
        Collection<DropDownDto> dropDownDtos=typedQuery.getResultList();
        return new ResponseDto(dropDownDtos.size(), dropDownDtos);
    }
}
