package io.aurora.ams.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.aurora.ams.domain.NotificationEventReminder;
import io.aurora.ams.repository.NotificationEventReminderRepository;
import io.aurora.ams.repository.search.NotificationEventReminderSearchRepository;
import io.aurora.ams.web.rest.util.HeaderUtil;
import io.aurora.ams.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing NotificationEventReminder.
 */
@RestController
@RequestMapping("/api")
public class NotificationEventReminderResource {

    private final Logger log = LoggerFactory.getLogger(NotificationEventReminderResource.class);
        
    @Inject
    private NotificationEventReminderRepository notificationEventReminderRepository;
    
    @Inject
    private NotificationEventReminderSearchRepository notificationEventReminderSearchRepository;
    
    /**
     * POST  /notification-event-reminders : Create a new notificationEventReminder.
     *
     * @param notificationEventReminder the notificationEventReminder to create
     * @return the ResponseEntity with status 201 (Created) and with body the new notificationEventReminder, or with status 400 (Bad Request) if the notificationEventReminder has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/notification-event-reminders",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NotificationEventReminder> createNotificationEventReminder(@Valid @RequestBody NotificationEventReminder notificationEventReminder) throws URISyntaxException {
        log.debug("REST request to save NotificationEventReminder : {}", notificationEventReminder);
        if (notificationEventReminder.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("notificationEventReminder", "idexists", "A new notificationEventReminder cannot already have an ID")).body(null);
        }
        NotificationEventReminder result = notificationEventReminderRepository.save(notificationEventReminder);
        notificationEventReminderSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/notification-event-reminders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("notificationEventReminder", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /notification-event-reminders : Updates an existing notificationEventReminder.
     *
     * @param notificationEventReminder the notificationEventReminder to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated notificationEventReminder,
     * or with status 400 (Bad Request) if the notificationEventReminder is not valid,
     * or with status 500 (Internal Server Error) if the notificationEventReminder couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/notification-event-reminders",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NotificationEventReminder> updateNotificationEventReminder(@Valid @RequestBody NotificationEventReminder notificationEventReminder) throws URISyntaxException {
        log.debug("REST request to update NotificationEventReminder : {}", notificationEventReminder);
        if (notificationEventReminder.getId() == null) {
            return createNotificationEventReminder(notificationEventReminder);
        }
        NotificationEventReminder result = notificationEventReminderRepository.save(notificationEventReminder);
        notificationEventReminderSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("notificationEventReminder", notificationEventReminder.getId().toString()))
            .body(result);
    }

    /**
     * GET  /notification-event-reminders : get all the notificationEventReminders.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of notificationEventReminders in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/notification-event-reminders",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<NotificationEventReminder>> getAllNotificationEventReminders(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of NotificationEventReminders");
        Page<NotificationEventReminder> page = notificationEventReminderRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/notification-event-reminders");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /notification-event-reminders/:id : get the "id" notificationEventReminder.
     *
     * @param id the id of the notificationEventReminder to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the notificationEventReminder, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/notification-event-reminders/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NotificationEventReminder> getNotificationEventReminder(@PathVariable Long id) {
        log.debug("REST request to get NotificationEventReminder : {}", id);
        NotificationEventReminder notificationEventReminder = notificationEventReminderRepository.findOne(id);
        return Optional.ofNullable(notificationEventReminder)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /notification-event-reminders/:id : delete the "id" notificationEventReminder.
     *
     * @param id the id of the notificationEventReminder to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/notification-event-reminders/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteNotificationEventReminder(@PathVariable Long id) {
        log.debug("REST request to delete NotificationEventReminder : {}", id);
        notificationEventReminderRepository.delete(id);
        notificationEventReminderSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("notificationEventReminder", id.toString())).build();
    }

    /**
     * SEARCH  /_search/notification-event-reminders?query=:query : search for the notificationEventReminder corresponding
     * to the query.
     *
     * @param query the query of the notificationEventReminder search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/notification-event-reminders",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<NotificationEventReminder>> searchNotificationEventReminders(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of NotificationEventReminders for query {}", query);
        Page<NotificationEventReminder> page = notificationEventReminderSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/notification-event-reminders");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
