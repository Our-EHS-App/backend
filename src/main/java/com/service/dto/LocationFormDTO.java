package com.service.dto;

import com.domain.Location;

public class LocationFormDTO {
    private Location location;
    private Long listStatusId;
    private Long count;

    public LocationFormDTO(Location locationId, Long listStatusId, Long count) {
        this.location = locationId;
        this.listStatusId = listStatusId;
        this.count = count;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Long getListStatusId() {
        return listStatusId;
    }

    public void setListStatusId(Long listStatusId) {
        this.listStatusId = listStatusId;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
