package com.repository;

import com.domain.Form;
import com.service.dto.LocationFormDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Form entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormRepository extends JpaRepository<Form, Long> {
    // todo add pagination
    Page<Form> findAllByOrganizationTemplate_Organization_Id(Long id, Pageable pageable);
    List<Form> findAllByOrganization_Id(Long id);
    Optional<Form> findByIdAndListStatus_Id(Long formId, Long statusId);
    Optional<Form> findFirstByTemplate_idAndOrganization_idOrderByCreatedDateDesc(Long templateId, Long orgId);

    @Query(value = "select new com.service.dto.LocationFormDTO(f.location, f.listStatus.id, count(f.id)) from Form f" +
        " where f.organization.id =:orgId" +
        " group by f.location.id, f.listStatus.id")
    List<LocationFormDTO> getLocationsDashboard( @Param("orgId") Long orgId);

}
