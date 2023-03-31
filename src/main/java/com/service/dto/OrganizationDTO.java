package com.service.dto;

import com.web.rest.vm.ManagedUserVM;

public class OrganizationDTO {

    private Long id;
    private String nameAr;
    private String nameEn;
    private String email;
    private Long organizationTypeId;
    private ManagedUserVM mainUser;

    public OrganizationDTO(Long id, String nameAr, String nameEn, String email, Long organizationTypeId) {
        this.id = id;
        this.nameAr = nameAr;
        this.nameEn = nameEn;
        this.email = email;
        this.organizationTypeId = organizationTypeId;
    }


    public OrganizationDTO() {
    }

    public ManagedUserVM getMainUser() {
        return mainUser;
    }

    public void setMainUser(ManagedUserVM mainUser) {
        this.mainUser = mainUser;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getOrganizationTypeId() {
        return organizationTypeId;
    }

    public void setOrganizationTypeId(Long organizationTypeId) {
        this.organizationTypeId = organizationTypeId;
    }

    @Override
    public String toString() {
        return "OrgRegReqDTO{" +
            "nameAr='" + nameAr + '\'' +
            ", nameEn='" + nameEn + '\'' +
            ", email='" + email + '\'' +
            ", organizationTypeId=" + organizationTypeId +
            '}';
    }
}
