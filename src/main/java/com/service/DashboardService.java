package com.service;

import com.domain.Form;
import com.domain.Location;
import com.repository.CategoryRepository;
import com.repository.FormRepository;
import com.repository.LocationRepository;
import com.repository.OrganizationTemplateRepository;
import com.service.dto.CategoryDashboardDTO;
import com.service.dto.CategoryDetailsDashboardDTO;
import com.service.dto.LocationDashboardDTO;
import com.service.dto.LocationFormDTO;
import com.service.mapper.CategoryMapper;
import com.service.util.TokenUtils;
import com.web.rest.errors.CustomException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DashboardService {
    private final FormRepository formRepository;
    private final LocationRepository locationRepository;
    private final OrganizationTemplateRepository organizationTemplateRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    private final TokenUtils tokenUtils;

    public DashboardService(FormRepository formRepository, LocationRepository locationRepository, OrganizationTemplateRepository organizationTemplateRepository, CategoryRepository categoryRepository, CategoryMapper categoryMapper, TokenUtils tokenUtils) {
        this.formRepository = formRepository;
        this.locationRepository = locationRepository;
        this.organizationTemplateRepository = organizationTemplateRepository;
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.tokenUtils = tokenUtils;
    }

    public List<CategoryDetailsDashboardDTO> categoryDashboard(HttpServletRequest request) {
        List<CategoryDashboardDTO> dtos = categoryRepository.categoryDashboard(Long.valueOf(tokenUtils.getOrgId(request)));
        List<CategoryDashboardDTO> submittedDtos = categoryRepository.categoryDashboardByFormStatus(Long.valueOf(tokenUtils.getOrgId(request)), 4L);

        return dtos.stream()
            .map(i -> {
                for (CategoryDashboardDTO submittedDto : submittedDtos) {
                    if (i.getCategoryId().equals(submittedDto.getCategoryId())) {
                        submittedDto.getCategoryId().equals(i.getCategoryId());
                        Double percentage = submittedDto.getFormCount().doubleValue() / i.getFormCount().doubleValue() * 100;
                        i.setPercentage(percentage);
                    } else {
                        i.setPercentage(0D);
                    }

                }
                return i;
            })
            .map(this::toDTODetails)
            .collect(Collectors.toList());
    }

    private CategoryDetailsDashboardDTO toDTODetails(CategoryDashboardDTO dto) {
        CategoryDetailsDashboardDTO detailsDashboardDTO = new CategoryDetailsDashboardDTO();
        detailsDashboardDTO.setCategoryDTO(categoryRepository.findById(dto.getCategoryId()).map(categoryMapper::toDto)
            .orElseThrow(() -> new CustomException("Not found!", "غير موجود!", "not.found")));
        DecimalFormat df = new DecimalFormat("0.00");
        detailsDashboardDTO.setPercentage(df.format(dto.getPercentage()));
        return detailsDashboardDTO;
    }

    public List<LocationDashboardDTO> locationDashboard(HttpServletRequest request){
        List<LocationFormDTO> dtos = formRepository.getLocationsDashboard(Long.valueOf(tokenUtils.getOrgId(request)));

        return toLocationDashboardDTO(dtos);

    }

    private List<LocationDashboardDTO> toLocationDashboardDTO(List<LocationFormDTO> locationFormDTOS){
        List<LocationDashboardDTO> dtos = new ArrayList<>();
        for(LocationFormDTO lf : locationFormDTOS){
            if(dtos.stream().noneMatch(d -> d.getId().equals(lf.getLocation().getId()))){
                LocationDashboardDTO dto = new LocationDashboardDTO(lf.getLocation());
                dtos.add(dto);
            }
        }

        dtos.forEach(dto -> {
                locationFormDTOS
                    .stream()
                    .filter(d -> d.getLocation().getId().equals(dto.getId()))
                    .forEach(m-> dto.getCounts().put(getStatus(m.getListStatusId()), Math.toIntExact(m.getCount())));
            });
        return dtos;
    }

    private String getStatus(Long statusId){
          Optional<FormStatusEnum> statusEnum=Arrays.stream(FormStatusEnum.values())
            .filter(e-> e.id.equals(statusId)).findFirst();

        return statusEnum.map(Enum::name).orElse("");
    }


}
