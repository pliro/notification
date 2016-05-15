package io.aurora.ams.web.rest;

import io.aurora.ams.NotificationApp;
import io.aurora.ams.domain.NotificationTemplate;
import io.aurora.ams.repository.NotificationTemplateRepository;
import io.aurora.ams.repository.search.NotificationTemplateSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the NotificationTemplateResource REST controller.
 *
 * @see NotificationTemplateResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = NotificationApp.class)
@WebAppConfiguration
@IntegrationTest
public class NotificationTemplateResourceIntTest {

    private static final String DEFAULT_NOTIFICATION_TEMPLATE_ID = "AAAAA";
    private static final String UPDATED_NOTIFICATION_TEMPLATE_ID = "BBBBB";
    private static final String DEFAULT_NOTIFICATION_TEMPLATE = "AAAAA";
    private static final String UPDATED_NOTIFICATION_TEMPLATE = "BBBBB";

    @Inject
    private NotificationTemplateRepository notificationTemplateRepository;

    @Inject
    private NotificationTemplateSearchRepository notificationTemplateSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restNotificationTemplateMockMvc;

    private NotificationTemplate notificationTemplate;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        NotificationTemplateResource notificationTemplateResource = new NotificationTemplateResource();
        ReflectionTestUtils.setField(notificationTemplateResource, "notificationTemplateSearchRepository", notificationTemplateSearchRepository);
        ReflectionTestUtils.setField(notificationTemplateResource, "notificationTemplateRepository", notificationTemplateRepository);
        this.restNotificationTemplateMockMvc = MockMvcBuilders.standaloneSetup(notificationTemplateResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        notificationTemplateSearchRepository.deleteAll();
        notificationTemplate = new NotificationTemplate();
        notificationTemplate.setNotificationTemplateId(DEFAULT_NOTIFICATION_TEMPLATE_ID);
        notificationTemplate.setNotificationTemplate(DEFAULT_NOTIFICATION_TEMPLATE);
    }

    @Test
    @Transactional
    public void createNotificationTemplate() throws Exception {
        int databaseSizeBeforeCreate = notificationTemplateRepository.findAll().size();

        // Create the NotificationTemplate

        restNotificationTemplateMockMvc.perform(post("/api/notification-templates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(notificationTemplate)))
                .andExpect(status().isCreated());

        // Validate the NotificationTemplate in the database
        List<NotificationTemplate> notificationTemplates = notificationTemplateRepository.findAll();
        assertThat(notificationTemplates).hasSize(databaseSizeBeforeCreate + 1);
        NotificationTemplate testNotificationTemplate = notificationTemplates.get(notificationTemplates.size() - 1);
        assertThat(testNotificationTemplate.getNotificationTemplateId()).isEqualTo(DEFAULT_NOTIFICATION_TEMPLATE_ID);
        assertThat(testNotificationTemplate.getNotificationTemplate()).isEqualTo(DEFAULT_NOTIFICATION_TEMPLATE);

        // Validate the NotificationTemplate in ElasticSearch
        NotificationTemplate notificationTemplateEs = notificationTemplateSearchRepository.findOne(testNotificationTemplate.getId());
        assertThat(notificationTemplateEs).isEqualToComparingFieldByField(testNotificationTemplate);
    }

    @Test
    @Transactional
    public void checkNotificationTemplateIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationTemplateRepository.findAll().size();
        // set the field null
        notificationTemplate.setNotificationTemplateId(null);

        // Create the NotificationTemplate, which fails.

        restNotificationTemplateMockMvc.perform(post("/api/notification-templates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(notificationTemplate)))
                .andExpect(status().isBadRequest());

        List<NotificationTemplate> notificationTemplates = notificationTemplateRepository.findAll();
        assertThat(notificationTemplates).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNotificationTemplateIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationTemplateRepository.findAll().size();
        // set the field null
        notificationTemplate.setNotificationTemplate(null);

        // Create the NotificationTemplate, which fails.

        restNotificationTemplateMockMvc.perform(post("/api/notification-templates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(notificationTemplate)))
                .andExpect(status().isBadRequest());

        List<NotificationTemplate> notificationTemplates = notificationTemplateRepository.findAll();
        assertThat(notificationTemplates).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNotificationTemplates() throws Exception {
        // Initialize the database
        notificationTemplateRepository.saveAndFlush(notificationTemplate);

        // Get all the notificationTemplates
        restNotificationTemplateMockMvc.perform(get("/api/notification-templates?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(notificationTemplate.getId().intValue())))
                .andExpect(jsonPath("$.[*].notificationTemplateId").value(hasItem(DEFAULT_NOTIFICATION_TEMPLATE_ID.toString())))
                .andExpect(jsonPath("$.[*].notificationTemplate").value(hasItem(DEFAULT_NOTIFICATION_TEMPLATE.toString())));
    }

    @Test
    @Transactional
    public void getNotificationTemplate() throws Exception {
        // Initialize the database
        notificationTemplateRepository.saveAndFlush(notificationTemplate);

        // Get the notificationTemplate
        restNotificationTemplateMockMvc.perform(get("/api/notification-templates/{id}", notificationTemplate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(notificationTemplate.getId().intValue()))
            .andExpect(jsonPath("$.notificationTemplateId").value(DEFAULT_NOTIFICATION_TEMPLATE_ID.toString()))
            .andExpect(jsonPath("$.notificationTemplate").value(DEFAULT_NOTIFICATION_TEMPLATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingNotificationTemplate() throws Exception {
        // Get the notificationTemplate
        restNotificationTemplateMockMvc.perform(get("/api/notification-templates/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNotificationTemplate() throws Exception {
        // Initialize the database
        notificationTemplateRepository.saveAndFlush(notificationTemplate);
        notificationTemplateSearchRepository.save(notificationTemplate);
        int databaseSizeBeforeUpdate = notificationTemplateRepository.findAll().size();

        // Update the notificationTemplate
        NotificationTemplate updatedNotificationTemplate = new NotificationTemplate();
        updatedNotificationTemplate.setId(notificationTemplate.getId());
        updatedNotificationTemplate.setNotificationTemplateId(UPDATED_NOTIFICATION_TEMPLATE_ID);
        updatedNotificationTemplate.setNotificationTemplate(UPDATED_NOTIFICATION_TEMPLATE);

        restNotificationTemplateMockMvc.perform(put("/api/notification-templates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedNotificationTemplate)))
                .andExpect(status().isOk());

        // Validate the NotificationTemplate in the database
        List<NotificationTemplate> notificationTemplates = notificationTemplateRepository.findAll();
        assertThat(notificationTemplates).hasSize(databaseSizeBeforeUpdate);
        NotificationTemplate testNotificationTemplate = notificationTemplates.get(notificationTemplates.size() - 1);
        assertThat(testNotificationTemplate.getNotificationTemplateId()).isEqualTo(UPDATED_NOTIFICATION_TEMPLATE_ID);
        assertThat(testNotificationTemplate.getNotificationTemplate()).isEqualTo(UPDATED_NOTIFICATION_TEMPLATE);

        // Validate the NotificationTemplate in ElasticSearch
        NotificationTemplate notificationTemplateEs = notificationTemplateSearchRepository.findOne(testNotificationTemplate.getId());
        assertThat(notificationTemplateEs).isEqualToComparingFieldByField(testNotificationTemplate);
    }

    @Test
    @Transactional
    public void deleteNotificationTemplate() throws Exception {
        // Initialize the database
        notificationTemplateRepository.saveAndFlush(notificationTemplate);
        notificationTemplateSearchRepository.save(notificationTemplate);
        int databaseSizeBeforeDelete = notificationTemplateRepository.findAll().size();

        // Get the notificationTemplate
        restNotificationTemplateMockMvc.perform(delete("/api/notification-templates/{id}", notificationTemplate.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean notificationTemplateExistsInEs = notificationTemplateSearchRepository.exists(notificationTemplate.getId());
        assertThat(notificationTemplateExistsInEs).isFalse();

        // Validate the database is empty
        List<NotificationTemplate> notificationTemplates = notificationTemplateRepository.findAll();
        assertThat(notificationTemplates).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchNotificationTemplate() throws Exception {
        // Initialize the database
        notificationTemplateRepository.saveAndFlush(notificationTemplate);
        notificationTemplateSearchRepository.save(notificationTemplate);

        // Search the notificationTemplate
        restNotificationTemplateMockMvc.perform(get("/api/_search/notification-templates?query=id:" + notificationTemplate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificationTemplate.getId().intValue())))
            .andExpect(jsonPath("$.[*].notificationTemplateId").value(hasItem(DEFAULT_NOTIFICATION_TEMPLATE_ID.toString())))
            .andExpect(jsonPath("$.[*].notificationTemplate").value(hasItem(DEFAULT_NOTIFICATION_TEMPLATE.toString())));
    }
}
