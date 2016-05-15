package io.aurora.ams.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.aurora.ams.domain.NotificationEvent;
import io.aurora.ams.repository.NotificationEventRepository;
import io.aurora.ams.repository.search.NotificationEventSearchRepository;
import io.aurora.ams.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing NotificationEvent.
 */
@RestController
@RequestMapping("/api")
public class NotificationEventResource {

    private final Logger log = LoggerFactory.getLogger(NotificationEventResource.class);
        
    @Inject
    private NotificationEventRepository notificationEventRepository;
    
    @Inject
    private NotificationEventSearchRepository notificationEventSearchRepository;
    
    /**
     * POST  /notification-events : Create a new notificationEvent.
     *
     * @param notificationEvent the notificationEvent to create
     * @return the ResponseEntity with status 201 (Created) and with body the new notificationEvent, or with status 400 (Bad Request) if the notificationEvent has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/notification-events",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NotificationEvent> createNotificationEvent(@Valid @RequestBody NotificationEvent notificationEvent) throws URISyntaxException {
        log.debug("REST request to save NotificationEvent : {}", notificationEvent);
        if (notificationEvent.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("notificationEvent", "idexists", "A new notificationEvent cannot already have an ID")).body(null);
        }
        NotificationEvent result = notificationEventRepository.save(notificationEvent);
        notificationEventSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/notification-events/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("notificationEvent", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /notification-events : Updates an existing notificationEvent.
     *
     * @param notificationEvent the notificationEvent to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated notificationEvent,
     * or with status 400 (Bad Request) if the notificationEvent is not valid,
     * or with status 500 (Internal Server Error) if the notificationEvent couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/notification-events",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NotificationEvent> updateNotificationEvent(@Valid @RequestBody NotificationEvent notificationEvent) throws URISyntaxException {
        log.debug("REST request to update NotificationEvent : {}", notificationEvent);
        if (notificationEvent.getId() == null) {
            return createNotificationEvent(notificationEvent);
        }
        NotificationEvent result = notificationEventRepository.save(notificationEvent);
        notificationEventSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("notificationEvent", notificationEvent.getId().toString()))
            .body(result);
    }

    /**
     * GET  /notification-events : get all the notificationEvents.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of notificationEvents in body
     */
    @RequestMapping(value = "/notification-events",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<NotificationEvent> getAllNotificationEvents() {
        log.debug("REST request to get all NotificationEvents");
        List<NotificationEvent> notificationEvents = notificationEventRepository.findAll();
        return notificationEvents;
    }

    /**
     * GET  /notification-events/:id : get the "id" notificationEvent.
     *
     * @param id the id of the notificationEvent to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the notificationEvent, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/notification-events/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NotificationEvent> getNotificationEvent(@PathVariable Long id) {
        log.debug("REST request to get NotificationEvent : {}", id);
        NotificationEvent notificationEvent = notificationEventRepository.findOne(id);
        return Optional.ofNullable(notificationEvent)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /notification-events/:id : delete the "id" notificationEvent.
     *
     * @param id the id of the notificationEvent to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/notification-events/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteNotificationEvent(@PathVariable Long id) {
        log.debug("REST request to delete NotificationEvent : {}", id);
        notificationEventRepository.delete(id);
        notificationEventSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("notificationEvent", id.toString())).build();
    }

    /**
     * SEARCH  /_search/notification-events?query=:query : search for the notificationEvent corresponding
     * to the query.
     *
     * @param query the query of the notificationEvent search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/notification-events",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<NotificationEvent> searchNotificationEvents(@RequestParam String query) {
        log.debug("REST request to search NotificationEvents for query {}", query);
        return StreamSupport
            .stream(notificationEventSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
