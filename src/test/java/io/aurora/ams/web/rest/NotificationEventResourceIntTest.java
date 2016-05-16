package io.aurora.ams.web.rest;

import io.aurora.ams.NotificationApp;
import io.aurora.ams.domain.NotificationEvent;
import io.aurora.ams.repository.NotificationEventRepository;
import io.aurora.ams.repository.search.NotificationEventSearchRepository;

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

import io.aurora.ams.domain.enumeration.NotificationEventName;

/**
 * Test class for the NotificationEventResource REST controller.
 *
 * @see NotificationEventResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = NotificationApp.class)
@WebAppConfiguration
@IntegrationTest
public class NotificationEventResourceIntTest {

    private static final String DEFAULT_NOTIFICATION_EVENT_ID = "AAAAA";
    private static final String UPDATED_NOTIFICATION_EVENT_ID = "BBBBB";

    private static final NotificationEventName DEFAULT_NOTIFICATION_EVENT_NAME = NotificationEventName.APPOINTEMENT_CONFIRMED;
    private static final NotificationEventName UPDATED_NOTIFICATION_EVENT_NAME = NotificationEventName.APPOINTMENT_CANCELLED;

    @Inject
    private NotificationEventRepository notificationEventRepository;

    @Inject
    private NotificationEventSearchRepository notificationEventSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restNotificationEventMockMvc;

    private NotificationEvent notificationEvent;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        NotificationEventResource notificationEventResource = new NotificationEventResource();
        ReflectionTestUtils.setField(notificationEventResource, "notificationEventSearchRepository", notificationEventSearchRepository);
        ReflectionTestUtils.setField(notificationEventResource, "notificationEventRepository", notificationEventRepository);
        this.restNotificationEventMockMvc = MockMvcBuilders.standaloneSetup(notificationEventResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        notificationEventSearchRepository.deleteAll();
        notificationEvent = new NotificationEvent();
        notificationEvent.setNotificationEventId(DEFAULT_NOTIFICATION_EVENT_ID);
        notificationEvent.setNotificationEventName(DEFAULT_NOTIFICATION_EVENT_NAME);
    }

    @Test
    @Transactional
    public void createNotificationEvent() throws Exception {
        int databaseSizeBeforeCreate = notificationEventRepository.findAll().size();

        // Create the NotificationEvent

        restNotificationEventMockMvc.perform(post("/api/notification-events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(notificationEvent)))
                .andExpect(status().isCreated());

        // Validate the NotificationEvent in the database
        List<NotificationEvent> notificationEvents = notificationEventRepository.findAll();
        assertThat(notificationEvents).hasSize(databaseSizeBeforeCreate + 1);
        NotificationEvent testNotificationEvent = notificationEvents.get(notificationEvents.size() - 1);
        assertThat(testNotificationEvent.getNotificationEventId()).isEqualTo(DEFAULT_NOTIFICATION_EVENT_ID);
        assertThat(testNotificationEvent.getNotificationEventName()).isEqualTo(DEFAULT_NOTIFICATION_EVENT_NAME);

        // Validate the NotificationEvent in ElasticSearch
        NotificationEvent notificationEventEs = notificationEventSearchRepository.findOne(testNotificationEvent.getId());
        assertThat(notificationEventEs).isEqualToComparingFieldByField(testNotificationEvent);
    }

    @Test
    @Transactional
    public void checkNotificationEventIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationEventRepository.findAll().size();
        // set the field null
        notificationEvent.setNotificationEventId(null);

        // Create the NotificationEvent, which fails.

        restNotificationEventMockMvc.perform(post("/api/notification-events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(notificationEvent)))
                .andExpect(status().isBadRequest());

        List<NotificationEvent> notificationEvents = notificationEventRepository.findAll();
        assertThat(notificationEvents).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNotificationEventNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationEventRepository.findAll().size();
        // set the field null
        notificationEvent.setNotificationEventName(null);

        // Create the NotificationEvent, which fails.

        restNotificationEventMockMvc.perform(post("/api/notification-events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(notificationEvent)))
                .andExpect(status().isBadRequest());

        List<NotificationEvent> notificationEvents = notificationEventRepository.findAll();
        assertThat(notificationEvents).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNotificationEvents() throws Exception {
        // Initialize the database
        notificationEventRepository.saveAndFlush(notificationEvent);

        // Get all the notificationEvents
        restNotificationEventMockMvc.perform(get("/api/notification-events?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(notificationEvent.getId().intValue())))
                .andExpect(jsonPath("$.[*].notificationEventId").value(hasItem(DEFAULT_NOTIFICATION_EVENT_ID.toString())))
                .andExpect(jsonPath("$.[*].notificationEventName").value(hasItem(DEFAULT_NOTIFICATION_EVENT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getNotificationEvent() throws Exception {
        // Initialize the database
        notificationEventRepository.saveAndFlush(notificationEvent);

        // Get the notificationEvent
        restNotificationEventMockMvc.perform(get("/api/notification-events/{id}", notificationEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(notificationEvent.getId().intValue()))
            .andExpect(jsonPath("$.notificationEventId").value(DEFAULT_NOTIFICATION_EVENT_ID.toString()))
            .andExpect(jsonPath("$.notificationEventName").value(DEFAULT_NOTIFICATION_EVENT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingNotificationEvent() throws Exception {
        // Get the notificationEvent
        restNotificationEventMockMvc.perform(get("/api/notification-events/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNotificationEvent() throws Exception {
        // Initialize the database
        notificationEventRepository.saveAndFlush(notificationEvent);
        notificationEventSearchRepository.save(notificationEvent);
        int databaseSizeBeforeUpdate = notificationEventRepository.findAll().size();

        // Update the notificationEvent
        NotificationEvent updatedNotificationEvent = new NotificationEvent();
        updatedNotificationEvent.setId(notificationEvent.getId());
        updatedNotificationEvent.setNotificationEventId(UPDATED_NOTIFICATION_EVENT_ID);
        updatedNotificationEvent.setNotificationEventName(UPDATED_NOTIFICATION_EVENT_NAME);

        restNotificationEventMockMvc.perform(put("/api/notification-events")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedNotificationEvent)))
                .andExpect(status().isOk());

        // Validate the NotificationEvent in the database
        List<NotificationEvent> notificationEvents = notificationEventRepository.findAll();
        assertThat(notificationEvents).hasSize(databaseSizeBeforeUpdate);
        NotificationEvent testNotificationEvent = notificationEvents.get(notificationEvents.size() - 1);
        assertThat(testNotificationEvent.getNotificationEventId()).isEqualTo(UPDATED_NOTIFICATION_EVENT_ID);
        assertThat(testNotificationEvent.getNotificationEventName()).isEqualTo(UPDATED_NOTIFICATION_EVENT_NAME);

        // Validate the NotificationEvent in ElasticSearch
        NotificationEvent notificationEventEs = notificationEventSearchRepository.findOne(testNotificationEvent.getId());
        assertThat(notificationEventEs).isEqualToComparingFieldByField(testNotificationEvent);
    }

    @Test
    @Transactional
    public void deleteNotificationEvent() throws Exception {
        // Initialize the database
        notificationEventRepository.saveAndFlush(notificationEvent);
        notificationEventSearchRepository.save(notificationEvent);
        int databaseSizeBeforeDelete = notificationEventRepository.findAll().size();

        // Get the notificationEvent
        restNotificationEventMockMvc.perform(delete("/api/notification-events/{id}", notificationEvent.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean notificationEventExistsInEs = notificationEventSearchRepository.exists(notificationEvent.getId());
        assertThat(notificationEventExistsInEs).isFalse();

        // Validate the database is empty
        List<NotificationEvent> notificationEvents = notificationEventRepository.findAll();
        assertThat(notificationEvents).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchNotificationEvent() throws Exception {
        // Initialize the database
        notificationEventRepository.saveAndFlush(notificationEvent);
        notificationEventSearchRepository.save(notificationEvent);

        // Search the notificationEvent
        restNotificationEventMockMvc.perform(get("/api/_search/notification-events?query=id:" + notificationEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificationEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].notificationEventId").value(hasItem(DEFAULT_NOTIFICATION_EVENT_ID.toString())))
            .andExpect(jsonPath("$.[*].notificationEventName").value(hasItem(DEFAULT_NOTIFICATION_EVENT_NAME.toString())));
    }
}
