package io.aurora.ams.web.rest;

import io.aurora.ams.NotificationApp;
import io.aurora.ams.domain.NotificationChannel;
import io.aurora.ams.repository.NotificationChannelRepository;
import io.aurora.ams.repository.search.NotificationChannelSearchRepository;

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

import io.aurora.ams.domain.enumeration.NotificationChannelName;

/**
 * Test class for the NotificationChannelResource REST controller.
 *
 * @see NotificationChannelResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = NotificationApp.class)
@WebAppConfiguration
@IntegrationTest
public class NotificationChannelResourceIntTest {

    private static final String DEFAULT_NOTIFICATION_CHANNEL_ID = "AAAAA";
    private static final String UPDATED_NOTIFICATION_CHANNEL_ID = "BBBBB";

    private static final NotificationChannelName DEFAULT_NOTIFICATION_CHANNEL_NAME = NotificationChannelName.SMS;
    private static final NotificationChannelName UPDATED_NOTIFICATION_CHANNEL_NAME = NotificationChannelName.EMAIL;

    @Inject
    private NotificationChannelRepository notificationChannelRepository;

    @Inject
    private NotificationChannelSearchRepository notificationChannelSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restNotificationChannelMockMvc;

    private NotificationChannel notificationChannel;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        NotificationChannelResource notificationChannelResource = new NotificationChannelResource();
        ReflectionTestUtils.setField(notificationChannelResource, "notificationChannelSearchRepository", notificationChannelSearchRepository);
        ReflectionTestUtils.setField(notificationChannelResource, "notificationChannelRepository", notificationChannelRepository);
        this.restNotificationChannelMockMvc = MockMvcBuilders.standaloneSetup(notificationChannelResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        notificationChannelSearchRepository.deleteAll();
        notificationChannel = new NotificationChannel();
        notificationChannel.setNotificationChannelId(DEFAULT_NOTIFICATION_CHANNEL_ID);
        notificationChannel.setNotificationChannelName(DEFAULT_NOTIFICATION_CHANNEL_NAME);
    }

    @Test
    @Transactional
    public void createNotificationChannel() throws Exception {
        int databaseSizeBeforeCreate = notificationChannelRepository.findAll().size();

        // Create the NotificationChannel

        restNotificationChannelMockMvc.perform(post("/api/notification-channels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(notificationChannel)))
                .andExpect(status().isCreated());

        // Validate the NotificationChannel in the database
        List<NotificationChannel> notificationChannels = notificationChannelRepository.findAll();
        assertThat(notificationChannels).hasSize(databaseSizeBeforeCreate + 1);
        NotificationChannel testNotificationChannel = notificationChannels.get(notificationChannels.size() - 1);
        assertThat(testNotificationChannel.getNotificationChannelId()).isEqualTo(DEFAULT_NOTIFICATION_CHANNEL_ID);
        assertThat(testNotificationChannel.getNotificationChannelName()).isEqualTo(DEFAULT_NOTIFICATION_CHANNEL_NAME);

        // Validate the NotificationChannel in ElasticSearch
        NotificationChannel notificationChannelEs = notificationChannelSearchRepository.findOne(testNotificationChannel.getId());
        assertThat(notificationChannelEs).isEqualToComparingFieldByField(testNotificationChannel);
    }

    @Test
    @Transactional
    public void checkNotificationChannelIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationChannelRepository.findAll().size();
        // set the field null
        notificationChannel.setNotificationChannelId(null);

        // Create the NotificationChannel, which fails.

        restNotificationChannelMockMvc.perform(post("/api/notification-channels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(notificationChannel)))
                .andExpect(status().isBadRequest());

        List<NotificationChannel> notificationChannels = notificationChannelRepository.findAll();
        assertThat(notificationChannels).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNotificationChannelNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationChannelRepository.findAll().size();
        // set the field null
        notificationChannel.setNotificationChannelName(null);

        // Create the NotificationChannel, which fails.

        restNotificationChannelMockMvc.perform(post("/api/notification-channels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(notificationChannel)))
                .andExpect(status().isBadRequest());

        List<NotificationChannel> notificationChannels = notificationChannelRepository.findAll();
        assertThat(notificationChannels).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNotificationChannels() throws Exception {
        // Initialize the database
        notificationChannelRepository.saveAndFlush(notificationChannel);

        // Get all the notificationChannels
        restNotificationChannelMockMvc.perform(get("/api/notification-channels?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(notificationChannel.getId().intValue())))
                .andExpect(jsonPath("$.[*].notificationChannelId").value(hasItem(DEFAULT_NOTIFICATION_CHANNEL_ID.toString())))
                .andExpect(jsonPath("$.[*].notificationChannelName").value(hasItem(DEFAULT_NOTIFICATION_CHANNEL_NAME.toString())));
    }

    @Test
    @Transactional
    public void getNotificationChannel() throws Exception {
        // Initialize the database
        notificationChannelRepository.saveAndFlush(notificationChannel);

        // Get the notificationChannel
        restNotificationChannelMockMvc.perform(get("/api/notification-channels/{id}", notificationChannel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(notificationChannel.getId().intValue()))
            .andExpect(jsonPath("$.notificationChannelId").value(DEFAULT_NOTIFICATION_CHANNEL_ID.toString()))
            .andExpect(jsonPath("$.notificationChannelName").value(DEFAULT_NOTIFICATION_CHANNEL_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingNotificationChannel() throws Exception {
        // Get the notificationChannel
        restNotificationChannelMockMvc.perform(get("/api/notification-channels/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNotificationChannel() throws Exception {
        // Initialize the database
        notificationChannelRepository.saveAndFlush(notificationChannel);
        notificationChannelSearchRepository.save(notificationChannel);
        int databaseSizeBeforeUpdate = notificationChannelRepository.findAll().size();

        // Update the notificationChannel
        NotificationChannel updatedNotificationChannel = new NotificationChannel();
        updatedNotificationChannel.setId(notificationChannel.getId());
        updatedNotificationChannel.setNotificationChannelId(UPDATED_NOTIFICATION_CHANNEL_ID);
        updatedNotificationChannel.setNotificationChannelName(UPDATED_NOTIFICATION_CHANNEL_NAME);

        restNotificationChannelMockMvc.perform(put("/api/notification-channels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedNotificationChannel)))
                .andExpect(status().isOk());

        // Validate the NotificationChannel in the database
        List<NotificationChannel> notificationChannels = notificationChannelRepository.findAll();
        assertThat(notificationChannels).hasSize(databaseSizeBeforeUpdate);
        NotificationChannel testNotificationChannel = notificationChannels.get(notificationChannels.size() - 1);
        assertThat(testNotificationChannel.getNotificationChannelId()).isEqualTo(UPDATED_NOTIFICATION_CHANNEL_ID);
        assertThat(testNotificationChannel.getNotificationChannelName()).isEqualTo(UPDATED_NOTIFICATION_CHANNEL_NAME);

        // Validate the NotificationChannel in ElasticSearch
        NotificationChannel notificationChannelEs = notificationChannelSearchRepository.findOne(testNotificationChannel.getId());
        assertThat(notificationChannelEs).isEqualToComparingFieldByField(testNotificationChannel);
    }

    @Test
    @Transactional
    public void deleteNotificationChannel() throws Exception {
        // Initialize the database
        notificationChannelRepository.saveAndFlush(notificationChannel);
        notificationChannelSearchRepository.save(notificationChannel);
        int databaseSizeBeforeDelete = notificationChannelRepository.findAll().size();

        // Get the notificationChannel
        restNotificationChannelMockMvc.perform(delete("/api/notification-channels/{id}", notificationChannel.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean notificationChannelExistsInEs = notificationChannelSearchRepository.exists(notificationChannel.getId());
        assertThat(notificationChannelExistsInEs).isFalse();

        // Validate the database is empty
        List<NotificationChannel> notificationChannels = notificationChannelRepository.findAll();
        assertThat(notificationChannels).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchNotificationChannel() throws Exception {
        // Initialize the database
        notificationChannelRepository.saveAndFlush(notificationChannel);
        notificationChannelSearchRepository.save(notificationChannel);

        // Search the notificationChannel
        restNotificationChannelMockMvc.perform(get("/api/_search/notification-channels?query=id:" + notificationChannel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificationChannel.getId().intValue())))
            .andExpect(jsonPath("$.[*].notificationChannelId").value(hasItem(DEFAULT_NOTIFICATION_CHANNEL_ID.toString())))
            .andExpect(jsonPath("$.[*].notificationChannelName").value(hasItem(DEFAULT_NOTIFICATION_CHANNEL_NAME.toString())));
    }
}
