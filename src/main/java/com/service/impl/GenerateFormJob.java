package com.service.impl;

import com.domain.Form;
import com.domain.OrganizationTemplate;
import com.repository.FormRepository;
import com.repository.OrganizationTemplateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@EnableAsync
@Service
public class GenerateFormJob {
    private final Logger log = LoggerFactory.getLogger(GenerateFormJob.class);
    private final OrganizationTemplateRepository organizationTemplateRepository;
    private final FormServiceImpl formService;
    private final FormRepository formRepository;

    public GenerateFormJob(OrganizationTemplateRepository organizationTemplateRepository, FormServiceImpl formService, FormRepository formRepository) {
        this.organizationTemplateRepository = organizationTemplateRepository;
        this.formService = formService;
        this.formRepository = formRepository;
    }

// Every minute!
//    @Scheduled(cron = " */30 * * * * ?")
    @Scheduled(cron = "@daily")
    @EventListener(ApplicationReadyEvent.class)
    public void generateForms() {
        log.info("Generate forms job started");
        // todo Don't duplicate, Generate if latest form submitted or expired
        // todo Check frequency
        List<OrganizationTemplate> organizationTemplateList = organizationTemplateRepository.findAll();
        organizationTemplateList
            .forEach(formService::generateForm);
        log.info("Generate form job finished");
        // todo send notification

    }

}
