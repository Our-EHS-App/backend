package com.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.domain.LocationType} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LocationTypeDTO implements Serializable {

    private Long id;

    private String nameAr;

    private String nameAn;

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

    public String getNameAn() {
        return nameAn;
    }

    public void setNameAn(String nameAn) {
        this.nameAn = nameAn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LocationTypeDTO)) {
            return false;
        }

        LocationTypeDTO locationTypeDTO = (LocationTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, locationTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocationTypeDTO{" +
            "id=" + getId() +
            ", nameAr='" + getNameAr() + "'" +
            ", nameAn='" + getNameAn() + "'" +
            "}";
    }
}
