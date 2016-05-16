package io.aurora.ams.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.aurora.ams.domain.NotificationNotifieeType;
import io.aurora.ams.repository.NotificationNotifieeTypeRepository;
import io.aurora.ams.repository.search.NotificationNotifieeTypeSearchRepository;
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
 * REST controller for managing NotificationNotifieeType.
 */
@RestController
@RequestMapping("/api")
public class NotificationNotifieeTypeResource {

    private final Logger log = LoggerFactory.getLogger(NotificationNotifieeTypeResource.class);
        
    @Inject
    private NotificationNotifieeTypeRepository notificationNotifieeTypeRepository;
    
    @Inject
    private NotificationNotifieeTypeSearchRepository notificationNotifieeTypeSearchRepository;
    
    /**
     * POST  /notification-notifiee-types : Create a new notificationNotifieeType.
     *
     * @param notificationNotifieeType the notificationNotifieeType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new notificationNotifieeType, or with status 400 (Bad Request) if the notificationNotifieeType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/notification-notifiee-types",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NotificationNotifieeType> createNotificationNotifieeType(@Valid @RequestBody NotificationNotifieeType notificationNotifieeType) throws URISyntaxException {
        log.debug("REST request to save NotificationNotifieeType : {}", notificationNotifieeType);
        if (notificationNotifieeType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("notificationNotifieeType", "idexists", "A new notificationNotifieeType cannot already have an ID")).body(null);
        }
        NotificationNotifieeType result = notificationNotifieeTypeRepository.save(notificationNotifieeType);
        notificationNotifieeTypeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/notification-notifiee-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("notificationNotifieeType", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /notification-notifiee-types : Updates an existing notificationNotifieeType.
     *
     * @param notificationNotifieeType the notificationNotifieeType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated notificationNotifieeType,
     * or with status 400 (Bad Request) if the notificationNotifieeType is not valid,
     * or with status 500 (Internal Server Error) if the notificationNotifieeType couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/notification-notifiee-types",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NotificationNotifieeType> updateNotificationNotifieeType(@Valid @RequestBody NotificationNotifieeType notificationNotifieeType) throws URISyntaxException {
        log.debug("REST request to update NotificationNotifieeType : {}", notificationNotifieeType);
        if (notificationNotifieeType.getId() == null) {
            return createNotificationNotifieeType(notificationNotifieeType);
        }
        NotificationNotifieeType result = notificationNotifieeTypeRepository.save(notificationNotifieeType);
        notificationNotifieeTypeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("notificationNotifieeType", notificationNotifieeType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /notification-notifiee-types : get all the notificationNotifieeTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of notificationNotifieeTypes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/notification-notifiee-types",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<NotificationNotifieeType>> getAllNotificationNotifieeTypes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of NotificationNotifieeTypes");
        Page<NotificationNotifieeType> page = notificationNotifieeTypeRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/notification-notifiee-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /notification-notifiee-types/:id : get the "id" notificationNotifieeType.
     *
     * @param id the id of the notificationNotifieeType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the notificationNotifieeType, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/notification-notifiee-types/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NotificationNotifieeType> getNotificationNotifieeType(@PathVariable Long id) {
        log.debug("REST request to get NotificationNotifieeType : {}", id);
        NotificationNotifieeType notificationNotifieeType = notificationNotifieeTypeRepository.findOne(id);
        return Optional.ofNullable(notificationNotifieeType)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /notification-notifiee-types/:id : delete the "id" notificationNotifieeType.
     *
     * @param id the id of the notificationNotifieeType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/notification-notifiee-types/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteNotificationNotifieeType(@PathVariable Long id) {
        log.debug("REST request to delete NotificationNotifieeType : {}", id);
        notificationNotifieeTypeRepository.delete(id);
        notificationNotifieeTypeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("notificationNotifieeType", id.toString())).build();
    }

    /**
     * SEARCH  /_search/notification-notifiee-types?query=:query : search for the notificationNotifieeType corresponding
     * to the query.
     *
     * @param query the query of the notificationNotifieeType search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/notification-notifiee-types",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<NotificationNotifieeType>> searchNotificationNotifieeTypes(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of NotificationNotifieeTypes for query {}", query);
        Page<NotificationNotifieeType> page = notificationNotifieeTypeSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/notification-notifiee-types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
