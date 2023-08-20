package com.repository;

import com.domain.Category;
import com.service.dto.CategoryDashboardDTO;
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
            "group by c.id ")
    List<CategoryDashboardDTO> categoryDashboard(@Param("orgId") Long orgId);


}
