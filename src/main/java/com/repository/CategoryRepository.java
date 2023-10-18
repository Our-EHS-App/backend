package com.repository;

import com.domain.Category;
import com.service.dto.CategoryDashboardDTO;
import com.service.dto.ValueDashboardByCategoryDTO;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the Category entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(
        value = "select new com.service.dto.CategoryDashboardDTO(c.id , count(f.id) )  from OrganizationTemplate ot " +
            "join Form f on f.organizationTemplate  = ot " +
            "join Template t on t=f.template " +
            "join Category c on c =t.subCategory " +
            "where ot.organization.id =:orgId " +
            "group by c.id")
    List<CategoryDashboardDTO> categoryDashboard(@Param("orgId") Long orgId);

    @Query(
        value = "select new com.service.dto.CategoryDashboardDTO(c.id , count(f.id) )  from OrganizationTemplate ot " +
            "join Form f on f.organizationTemplate  = ot " +
            "join Template t on t=f.template " +
            "join Category c on c =t.subCategory " +
            "where ot.organization.id =:orgId " +
            "and f.listStatus.id = :formStatusId " +
            "group by c.id ")
    List<CategoryDashboardDTO> categoryDashboardByFormStatus(@Param("orgId") Long orgId, @Param("formStatusId") Long formStatusId);

    @Query(
        value = "select new com.service.dto.ValueDashboardByCategoryDTO(c.id  , AVG(CASE " +
            "  WHEN fv.value  = 'true' THEN 1 " +
            "  WHEN fv.value  = 'false' THEN 0 " +
            "  ELSE 0 " +
            "END) * 100 ) from  FormValues fv " +
            "join Form f on f = fv.form " +
            "join Template t on t = f.template " +
            "join Category c on c =t.subCategory " +
            "where f.organization.id = :orgId " +
            "group by  c.id ")
    List<ValueDashboardByCategoryDTO> valueDashboardByCategory(@Param("orgId") Long orgId);

}
