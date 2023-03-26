package com.repository;

import com.domain.Template;

import java.util.*;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class TemplateRepositoryWithBagRelationshipsImpl implements TemplateRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Template> fetchBagRelationships(Optional<Template> template) {
        return template.map(this::fetchFields);
    }

    @Override
    public Page<Template> fetchBagRelationships(Page<Template> templates) {
        return new PageImpl<>(fetchBagRelationships(templates.getContent()), templates.getPageable(), templates.getTotalElements());
    }

    @Override
    public List<Template> fetchBagRelationships(List<Template> templates) {
        return Optional.of(templates).map(this::fetchFields).orElse(Collections.emptyList());
    }

    Template fetchFields(Template result) {
        return entityManager
            .createQuery(
                "select template from Template template left join fetch template.fields where template is :template",
                Template.class
            )
            .setParameter("template", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Template> fetchFields(List<Template> templates) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, templates.size()).forEach(index -> order.put(templates.get(index).getId(), index));
        List<Template> result = entityManager
            .createQuery(
                "select distinct template from Template template left join fetch template.fields where template in :templates",
                Template.class
            )
            .setParameter("templates", templates)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        result.sort(Comparator.comparingInt(o -> order.get(o.getId())));
        return result;
    }
}
