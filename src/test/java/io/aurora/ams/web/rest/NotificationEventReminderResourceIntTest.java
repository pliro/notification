package io.aurora.ams.web.rest;

import io.aurora.ams.NotificationApp;
import io.aurora.ams.domain.NotificationEventReminder;
import io.aurora.ams.repository.NotificationEventReminderRepository;
import io.aurora.ams.repository.search.NotificationEventReminderSearchRepository;

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
 * Test class for the NotificationEventReminderResource REST controller.
 *
 * @see NotificationEventReminderResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = NotificationApp.class)
@WebAppConfiguration
@IntegrationTest
public class NotificationEventReminderResourceIntTest {

    private static final String DEFAULT_NOTIFICATION_EVENT_REMINDER_ID = "AAAAA";
    private static final String UPDATED_NOTIFICATION_EVENT_REMINDER_ID = "BBBBB";

    private static final Long DEFAULT_NOTIFICATION_EVENT_REMINDER_DATE_TIME = 1L;
    private static final Long UPDATED_NOTIFICATION_EVENT_REMINDER_DATE_TIME = 2L;

    @Inject
    private NotificationEventReminderRepository notificationEventReminderRepository;

    @Inject
    private NotificationEventReminderSearchRepository notificationEventReminderSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restNotificationEventReminderMockMvc;

    private NotificationEventReminder notificationEventReminder;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        NotificationEventReminderResource notificationEventReminderResource = new NotificationEventReminderResource();
        ReflectionTestUtils.setField(notificationEventReminderResource, "notificationEventReminderSearchRepository", notificationEventReminderSearchRepository);
        ReflectionTestUtils.setField(notificationEventReminderResource, "notificationEventReminderRepository", notificationEventReminderRepository);
        this.restNotificationEventReminderMockMvc = MockMvcBuilders.standaloneSetup(notificationEventReminderResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        notificationEventReminderSearchRepository.deleteAll();
        notificationEventReminder = new NotificationEventReminder();
        notificationEventReminder.setNotificationEventReminderId(DEFAULT_NOTIFICATION_EVENT_REMINDER_ID);
        notificationEventReminder.setNotificationEventReminderDateTime(DEFAULT_NOTIFICATION_EVENT_REMINDER_DATE_TIME);
    }

    @Test
    @Transactional
    public void createNotificationEventReminder() throws Exception {
        int databaseSizeBeforeCreate = notificationEventReminderRepository.findAll().size();

        // Create the NotificationEventReminder

        restNotificationEventReminderMockMvc.perform(post("/api/notification-event-reminders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(notificationEventReminder)))
                .andExpect(status().isCreated());

        // Validate the NotificationEventReminder in the database
        List<NotificationEventReminder> notificationEventReminders = notificationEventReminderRepository.findAll();
        assertThat(notificationEventReminders).hasSize(databaseSizeBeforeCreate + 1);
        NotificationEventReminder testNotificationEventReminder = notificationEventReminders.get(notificationEventReminders.size() - 1);
        assertThat(testNotificationEventReminder.getNotificationEventReminderId()).isEqualTo(DEFAULT_NOTIFICATION_EVENT_REMINDER_ID);
        assertThat(testNotificationEventReminder.getNotificationEventReminderDateTime()).isEqualTo(DEFAULT_NOTIFICATION_EVENT_REMINDER_DATE_TIME);

        // Validate the NotificationEventReminder in ElasticSearch
        NotificationEventReminder notificationEventReminderEs = notificationEventReminderSearchRepository.findOne(testNotificationEventReminder.getId());
        assertThat(notificationEventReminderEs).isEqualToComparingFieldByField(testNotificationEventReminder);
    }

    @Test
    @Transactional
    public void checkNotificationEventReminderIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationEventReminderRepository.findAll().size();
        // set the field null
        notificationEventReminder.setNotificationEventReminderId(null);

        // Create the NotificationEventReminder, which fails.

        restNotificationEventReminderMockMvc.perform(post("/api/notification-event-reminders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(notificationEventReminder)))
                .andExpect(status().isBadRequest());

        List<NotificationEventReminder> notificationEventReminders = notificationEventReminderRepository.findAll();
        assertThat(notificationEventReminders).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNotificationEventReminderDateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationEventReminderRepository.findAll().size();
        // set the field null
        notificationEventReminder.setNotificationEventReminderDateTime(null);

        // Create the NotificationEventReminder, which fails.

        restNotificationEventReminderMockMvc.perform(post("/api/notification-event-reminders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(notificationEventReminder)))
                .andExpect(status().isBadRequest());

        List<NotificationEventReminder> notificationEventReminders = notificationEventReminderRepository.findAll();
        assertThat(notificationEventReminders).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNotificationEventReminders() throws Exception {
        // Initialize the database
        notificationEventReminderRepository.saveAndFlush(notificationEventReminder);

        // Get all the notificationEventReminders
        restNotificationEventReminderMockMvc.perform(get("/api/notification-event-reminders?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(notificationEventReminder.getId().intValue())))
                .andExpect(jsonPath("$.[*].notificationEventReminderId").value(hasItem(DEFAULT_NOTIFICATION_EVENT_REMINDER_ID.toString())))
                .andExpect(jsonPath("$.[*].notificationEventReminderDateTime").value(hasItem(DEFAULT_NOTIFICATION_EVENT_REMINDER_DATE_TIME.intValue())));
    }

    @Test
    @Transactional
    public void getNotificationEventReminder() throws Exception {
        // Initialize the database
        notificationEventReminderRepository.saveAndFlush(notificationEventReminder);

        // Get the notificationEventReminder
        restNotificationEventReminderMockMvc.perform(get("/api/notification-event-reminders/{id}", notificationEventReminder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(notificationEventReminder.getId().intValue()))
            .andExpect(jsonPath("$.notificationEventReminderId").value(DEFAULT_NOTIFICATION_EVENT_REMINDER_ID.toString()))
            .andExpect(jsonPath("$.notificationEventReminderDateTime").value(DEFAULT_NOTIFICATION_EVENT_REMINDER_DATE_TIME.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingNotificationEventReminder() throws Exception {
        // Get the notificationEventReminder
        restNotificationEventReminderMockMvc.perform(get("/api/notification-event-reminders/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNotificationEventReminder() throws Exception {
        // Initialize the database
        notificationEventReminderRepository.saveAndFlush(notificationEventReminder);
        notificationEventReminderSearchRepository.save(notificationEventReminder);
        int databaseSizeBeforeUpdate = notificationEventReminderRepository.findAll().size();

        // Update the notificationEventReminder
        NotificationEventReminder updatedNotificationEventReminder = new NotificationEventReminder();
        updatedNotificationEventReminder.setId(notificationEventReminder.getId());
        updatedNotificationEventReminder.setNotificationEventReminderId(UPDATED_NOTIFICATION_EVENT_REMINDER_ID);
        updatedNotificationEventReminder.setNotificationEventReminderDateTime(UPDATED_NOTIFICATION_EVENT_REMINDER_DATE_TIME);

        restNotificationEventReminderMockMvc.perform(put("/api/notification-event-reminders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedNotificationEventReminder)))
                .andExpect(status().isOk());

        // Validate the NotificationEventReminder in the database
        List<NotificationEventReminder> notificationEventReminders = notificationEventReminderRepository.findAll();
        assertThat(notificationEventReminders).hasSize(databaseSizeBeforeUpdate);
        NotificationEventReminder testNotificationEventReminder = notificationEventReminders.get(notificationEventReminders.size() - 1);
        assertThat(testNotificationEventReminder.getNotificationEventReminderId()).isEqualTo(UPDATED_NOTIFICATION_EVENT_REMINDER_ID);
        assertThat(testNotificationEventReminder.getNotificationEventReminderDateTime()).isEqualTo(UPDATED_NOTIFICATION_EVENT_REMINDER_DATE_TIME);

        // Validate the NotificationEventReminder in ElasticSearch
        NotificationEventReminder notificationEventReminderEs = notificationEventReminderSearchRepository.findOne(testNotificationEventReminder.getId());
        assertThat(notificationEventReminderEs).isEqualToComparingFieldByField(testNotificationEventReminder);
    }

    @Test
    @Transactional
    public void deleteNotificationEventReminder() throws Exception {
        // Initialize the database
        notificationEventReminderRepository.saveAndFlush(notificationEventReminder);
        notificationEventReminderSearchRepository.save(notificationEventReminder);
        int databaseSizeBeforeDelete = notificationEventReminderRepository.findAll().size();

        // Get the notificationEventReminder
        restNotificationEventReminderMockMvc.perform(delete("/api/notification-event-reminders/{id}", notificationEventReminder.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean notificationEventReminderExistsInEs = notificationEventReminderSearchRepository.exists(notificationEventReminder.getId());
        assertThat(notificationEventReminderExistsInEs).isFalse();

        // Validate the database is empty
        List<NotificationEventReminder> notificationEventReminders = notificationEventReminderRepository.findAll();
        assertThat(notificationEventReminders).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchNotificationEventReminder() throws Exception {
        // Initialize the database
        notificationEventReminderRepository.saveAndFlush(notificationEventReminder);
        notificationEventReminderSearchRepository.save(notificationEventReminder);

        // Search the notificationEventReminder
        restNotificationEventReminderMockMvc.perform(get("/api/_search/notification-event-reminders?query=id:" + notificationEventReminder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificationEventReminder.getId().intValue())))
            .andExpect(jsonPath("$.[*].notificationEventReminderId").value(hasItem(DEFAULT_NOTIFICATION_EVENT_REMINDER_ID.toString())))
            .andExpect(jsonPath("$.[*].notificationEventReminderDateTime").value(hasItem(DEFAULT_NOTIFICATION_EVENT_REMINDER_DATE_TIME.intValue())));
    }
}
