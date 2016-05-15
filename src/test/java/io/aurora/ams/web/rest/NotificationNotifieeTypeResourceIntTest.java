package io.aurora.ams.web.rest;

import io.aurora.ams.NotificationApp;
import io.aurora.ams.domain.NotificationNotifieeType;
import io.aurora.ams.repository.NotificationNotifieeTypeRepository;
import io.aurora.ams.repository.search.NotificationNotifieeTypeSearchRepository;

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

import io.aurora.ams.domain.enumeration.NotificationNotifieeTypeName;

/**
 * Test class for the NotificationNotifieeTypeResource REST controller.
 *
 * @see NotificationNotifieeTypeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = NotificationApp.class)
@WebAppConfiguration
@IntegrationTest
public class NotificationNotifieeTypeResourceIntTest {

    private static final String DEFAULT_NOTIFICATION_NOTIFIEE_TYPE_ID = "AAAAA";
    private static final String UPDATED_NOTIFICATION_NOTIFIEE_TYPE_ID = "BBBBB";

    private static final NotificationNotifieeTypeName DEFAULT_NOTIFICATION_NOTIFIEE_TYPE_NAME = NotificationNotifieeTypeName.PATIENT;
    private static final NotificationNotifieeTypeName UPDATED_NOTIFICATION_NOTIFIEE_TYPE_NAME = NotificationNotifieeTypeName.DOCTOR;

    @Inject
    private NotificationNotifieeTypeRepository notificationNotifieeTypeRepository;

    @Inject
    private NotificationNotifieeTypeSearchRepository notificationNotifieeTypeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restNotificationNotifieeTypeMockMvc;

    private NotificationNotifieeType notificationNotifieeType;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        NotificationNotifieeTypeResource notificationNotifieeTypeResource = new NotificationNotifieeTypeResource();
        ReflectionTestUtils.setField(notificationNotifieeTypeResource, "notificationNotifieeTypeSearchRepository", notificationNotifieeTypeSearchRepository);
        ReflectionTestUtils.setField(notificationNotifieeTypeResource, "notificationNotifieeTypeRepository", notificationNotifieeTypeRepository);
        this.restNotificationNotifieeTypeMockMvc = MockMvcBuilders.standaloneSetup(notificationNotifieeTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        notificationNotifieeTypeSearchRepository.deleteAll();
        notificationNotifieeType = new NotificationNotifieeType();
        notificationNotifieeType.setNotificationNotifieeTypeId(DEFAULT_NOTIFICATION_NOTIFIEE_TYPE_ID);
        notificationNotifieeType.setNotificationNotifieeTypeName(DEFAULT_NOTIFICATION_NOTIFIEE_TYPE_NAME);
    }

    @Test
    @Transactional
    public void createNotificationNotifieeType() throws Exception {
        int databaseSizeBeforeCreate = notificationNotifieeTypeRepository.findAll().size();

        // Create the NotificationNotifieeType

        restNotificationNotifieeTypeMockMvc.perform(post("/api/notification-notifiee-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(notificationNotifieeType)))
                .andExpect(status().isCreated());

        // Validate the NotificationNotifieeType in the database
        List<NotificationNotifieeType> notificationNotifieeTypes = notificationNotifieeTypeRepository.findAll();
        assertThat(notificationNotifieeTypes).hasSize(databaseSizeBeforeCreate + 1);
        NotificationNotifieeType testNotificationNotifieeType = notificationNotifieeTypes.get(notificationNotifieeTypes.size() - 1);
        assertThat(testNotificationNotifieeType.getNotificationNotifieeTypeId()).isEqualTo(DEFAULT_NOTIFICATION_NOTIFIEE_TYPE_ID);
        assertThat(testNotificationNotifieeType.getNotificationNotifieeTypeName()).isEqualTo(DEFAULT_NOTIFICATION_NOTIFIEE_TYPE_NAME);

        // Validate the NotificationNotifieeType in ElasticSearch
        NotificationNotifieeType notificationNotifieeTypeEs = notificationNotifieeTypeSearchRepository.findOne(testNotificationNotifieeType.getId());
        assertThat(notificationNotifieeTypeEs).isEqualToComparingFieldByField(testNotificationNotifieeType);
    }

    @Test
    @Transactional
    public void checkNotificationNotifieeTypeIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationNotifieeTypeRepository.findAll().size();
        // set the field null
        notificationNotifieeType.setNotificationNotifieeTypeId(null);

        // Create the NotificationNotifieeType, which fails.

        restNotificationNotifieeTypeMockMvc.perform(post("/api/notification-notifiee-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(notificationNotifieeType)))
                .andExpect(status().isBadRequest());

        List<NotificationNotifieeType> notificationNotifieeTypes = notificationNotifieeTypeRepository.findAll();
        assertThat(notificationNotifieeTypes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNotificationNotifieeTypeNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationNotifieeTypeRepository.findAll().size();
        // set the field null
        notificationNotifieeType.setNotificationNotifieeTypeName(null);

        // Create the NotificationNotifieeType, which fails.

        restNotificationNotifieeTypeMockMvc.perform(post("/api/notification-notifiee-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(notificationNotifieeType)))
                .andExpect(status().isBadRequest());

        List<NotificationNotifieeType> notificationNotifieeTypes = notificationNotifieeTypeRepository.findAll();
        assertThat(notificationNotifieeTypes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNotificationNotifieeTypes() throws Exception {
        // Initialize the database
        notificationNotifieeTypeRepository.saveAndFlush(notificationNotifieeType);

        // Get all the notificationNotifieeTypes
        restNotificationNotifieeTypeMockMvc.perform(get("/api/notification-notifiee-types?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(notificationNotifieeType.getId().intValue())))
                .andExpect(jsonPath("$.[*].notificationNotifieeTypeId").value(hasItem(DEFAULT_NOTIFICATION_NOTIFIEE_TYPE_ID.toString())))
                .andExpect(jsonPath("$.[*].notificationNotifieeTypeName").value(hasItem(DEFAULT_NOTIFICATION_NOTIFIEE_TYPE_NAME.toString())));
    }

    @Test
    @Transactional
    public void getNotificationNotifieeType() throws Exception {
        // Initialize the database
        notificationNotifieeTypeRepository.saveAndFlush(notificationNotifieeType);

        // Get the notificationNotifieeType
        restNotificationNotifieeTypeMockMvc.perform(get("/api/notification-notifiee-types/{id}", notificationNotifieeType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(notificationNotifieeType.getId().intValue()))
            .andExpect(jsonPath("$.notificationNotifieeTypeId").value(DEFAULT_NOTIFICATION_NOTIFIEE_TYPE_ID.toString()))
            .andExpect(jsonPath("$.notificationNotifieeTypeName").value(DEFAULT_NOTIFICATION_NOTIFIEE_TYPE_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingNotificationNotifieeType() throws Exception {
        // Get the notificationNotifieeType
        restNotificationNotifieeTypeMockMvc.perform(get("/api/notification-notifiee-types/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNotificationNotifieeType() throws Exception {
        // Initialize the database
        notificationNotifieeTypeRepository.saveAndFlush(notificationNotifieeType);
        notificationNotifieeTypeSearchRepository.save(notificationNotifieeType);
        int databaseSizeBeforeUpdate = notificationNotifieeTypeRepository.findAll().size();

        // Update the notificationNotifieeType
        NotificationNotifieeType updatedNotificationNotifieeType = new NotificationNotifieeType();
        updatedNotificationNotifieeType.setId(notificationNotifieeType.getId());
        updatedNotificationNotifieeType.setNotificationNotifieeTypeId(UPDATED_NOTIFICATION_NOTIFIEE_TYPE_ID);
        updatedNotificationNotifieeType.setNotificationNotifieeTypeName(UPDATED_NOTIFICATION_NOTIFIEE_TYPE_NAME);

        restNotificationNotifieeTypeMockMvc.perform(put("/api/notification-notifiee-types")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedNotificationNotifieeType)))
                .andExpect(status().isOk());

        // Validate the NotificationNotifieeType in the database
        List<NotificationNotifieeType> notificationNotifieeTypes = notificationNotifieeTypeRepository.findAll();
        assertThat(notificationNotifieeTypes).hasSize(databaseSizeBeforeUpdate);
        NotificationNotifieeType testNotificationNotifieeType = notificationNotifieeTypes.get(notificationNotifieeTypes.size() - 1);
        assertThat(testNotificationNotifieeType.getNotificationNotifieeTypeId()).isEqualTo(UPDATED_NOTIFICATION_NOTIFIEE_TYPE_ID);
        assertThat(testNotificationNotifieeType.getNotificationNotifieeTypeName()).isEqualTo(UPDATED_NOTIFICATION_NOTIFIEE_TYPE_NAME);

        // Validate the NotificationNotifieeType in ElasticSearch
        NotificationNotifieeType notificationNotifieeTypeEs = notificationNotifieeTypeSearchRepository.findOne(testNotificationNotifieeType.getId());
        assertThat(notificationNotifieeTypeEs).isEqualToComparingFieldByField(testNotificationNotifieeType);
    }

    @Test
    @Transactional
    public void deleteNotificationNotifieeType() throws Exception {
        // Initialize the database
        notificationNotifieeTypeRepository.saveAndFlush(notificationNotifieeType);
        notificationNotifieeTypeSearchRepository.save(notificationNotifieeType);
        int databaseSizeBeforeDelete = notificationNotifieeTypeRepository.findAll().size();

        // Get the notificationNotifieeType
        restNotificationNotifieeTypeMockMvc.perform(delete("/api/notification-notifiee-types/{id}", notificationNotifieeType.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean notificationNotifieeTypeExistsInEs = notificationNotifieeTypeSearchRepository.exists(notificationNotifieeType.getId());
        assertThat(notificationNotifieeTypeExistsInEs).isFalse();

        // Validate the database is empty
        List<NotificationNotifieeType> notificationNotifieeTypes = notificationNotifieeTypeRepository.findAll();
        assertThat(notificationNotifieeTypes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchNotificationNotifieeType() throws Exception {
        // Initialize the database
        notificationNotifieeTypeRepository.saveAndFlush(notificationNotifieeType);
        notificationNotifieeTypeSearchRepository.save(notificationNotifieeType);

        // Search the notificationNotifieeType
        restNotificationNotifieeTypeMockMvc.perform(get("/api/_search/notification-notifiee-types?query=id:" + notificationNotifieeType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificationNotifieeType.getId().intValue())))
            .andExpect(jsonPath("$.[*].notificationNotifieeTypeId").value(hasItem(DEFAULT_NOTIFICATION_NOTIFIEE_TYPE_ID.toString())))
            .andExpect(jsonPath("$.[*].notificationNotifieeTypeName").value(hasItem(DEFAULT_NOTIFICATION_NOTIFIEE_TYPE_NAME.toString())));
    }
}
