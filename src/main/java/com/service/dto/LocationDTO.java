package com.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.domain.Location} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LocationDTO implements Serializable {

    private Long id;

    private String nameAr;

    private String nameEn;

    private LocationTypeDTO locationType;

    private LocationStatusDTO locationStatus;

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

    public LocationTypeDTO getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationTypeDTO locationType) {
        this.locationType = locationType;
    }

    public LocationStatusDTO getLocationStatus() {
        return locationStatus;
    }

    public void setLocationStatus(LocationStatusDTO locationStatus) {
        this.locationStatus = locationStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LocationDTO)) {
            return false;
        }

        LocationDTO locationDTO = (LocationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, locationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocationDTO{" +
            "id=" + getId() +
            ", nameAr='" + getNameAr() + "'" +
            ", nameEn='" + getNameEn() + "'" +
            ", locationType=" + getLocationType() +
            ", locationStatus=" + getLocationStatus() +
            "}";
    }
}
