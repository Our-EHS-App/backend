package com.service;

import com.repository.CategoryRepository;
import com.repository.FormRepository;
import com.service.dto.CategoryDashboardDTO;
import com.service.dto.CategoryDetailsDashboardDTO;
import com.service.mapper.CategoryMapper;
import com.service.util.TokenUtils;
import com.web.rest.errors.CustomException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DashboardService {
private final FormRepository formRepository;
private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

private final TokenUtils tokenUtils;

    public DashboardService(FormRepository formRepository, CategoryRepository categoryRepository, CategoryMapper categoryMapper, TokenUtils tokenUtils) {
        this.formRepository = formRepository;
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.tokenUtils = tokenUtils;
    }

    public List<CategoryDetailsDashboardDTO> categoryDashboard(HttpServletRequest request){
        List<CategoryDashboardDTO> dtos = categoryRepository.categoryDashboard(Long.valueOf(tokenUtils.getOrgId(request)));
        List<CategoryDashboardDTO> submittedDtos = categoryRepository.categoryDashboardByFormStatus(Long.valueOf(tokenUtils.getOrgId(request)),4L);

         return dtos.stream()
             .map(i -> {
                 for(CategoryDashboardDTO submittedDto : submittedDtos){
                     if(i.getCategoryId().equals(submittedDto.getCategoryId())) {
                         submittedDto.getCategoryId().equals(i.getCategoryId());
                         Double percentage = submittedDto.getFormCount().doubleValue() / i.getFormCount().doubleValue() * 100;
                         i.setPercentage(percentage);
                     }else{
                         i.setPercentage(0D);
                     }

                 }
                 return i;
             })
            .map(this::toDTODetails)
            .collect(Collectors.toList());
    }

    private CategoryDetailsDashboardDTO toDTODetails(CategoryDashboardDTO dto){
        CategoryDetailsDashboardDTO detailsDashboardDTO = new CategoryDetailsDashboardDTO();
        detailsDashboardDTO.setCategoryDTO(categoryRepository.findById(dto.getCategoryId()).map(categoryMapper::toDto)
            .orElseThrow(() -> new CustomException("Not found!","غير موجود!","not.found")));
        DecimalFormat df = new DecimalFormat("0.00");
        detailsDashboardDTO.setPercentage(df.format(dto.getPercentage()));
        return detailsDashboardDTO;
    }


}
