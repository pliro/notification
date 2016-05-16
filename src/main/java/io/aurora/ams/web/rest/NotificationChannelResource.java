package io.aurora.ams.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.aurora.ams.domain.NotificationChannel;
import io.aurora.ams.repository.NotificationChannelRepository;
import io.aurora.ams.repository.search.NotificationChannelSearchRepository;
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
 * REST controller for managing NotificationChannel.
 */
@RestController
@RequestMapping("/api")
public class NotificationChannelResource {

    private final Logger log = LoggerFactory.getLogger(NotificationChannelResource.class);
        
    @Inject
    private NotificationChannelRepository notificationChannelRepository;
    
    @Inject
    private NotificationChannelSearchRepository notificationChannelSearchRepository;
    
    /**
     * POST  /notification-channels : Create a new notificationChannel.
     *
     * @param notificationChannel the notificationChannel to create
     * @return the ResponseEntity with status 201 (Created) and with body the new notificationChannel, or with status 400 (Bad Request) if the notificationChannel has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/notification-channels",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NotificationChannel> createNotificationChannel(@Valid @RequestBody NotificationChannel notificationChannel) throws URISyntaxException {
        log.debug("REST request to save NotificationChannel : {}", notificationChannel);
        if (notificationChannel.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("notificationChannel", "idexists", "A new notificationChannel cannot already have an ID")).body(null);
        }
        NotificationChannel result = notificationChannelRepository.save(notificationChannel);
        notificationChannelSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/notification-channels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("notificationChannel", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /notification-channels : Updates an existing notificationChannel.
     *
     * @param notificationChannel the notificationChannel to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated notificationChannel,
     * or with status 400 (Bad Request) if the notificationChannel is not valid,
     * or with status 500 (Internal Server Error) if the notificationChannel couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/notification-channels",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NotificationChannel> updateNotificationChannel(@Valid @RequestBody NotificationChannel notificationChannel) throws URISyntaxException {
        log.debug("REST request to update NotificationChannel : {}", notificationChannel);
        if (notificationChannel.getId() == null) {
            return createNotificationChannel(notificationChannel);
        }
        NotificationChannel result = notificationChannelRepository.save(notificationChannel);
        notificationChannelSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("notificationChannel", notificationChannel.getId().toString()))
            .body(result);
    }

    /**
     * GET  /notification-channels : get all the notificationChannels.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of notificationChannels in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/notification-channels",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<NotificationChannel>> getAllNotificationChannels(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of NotificationChannels");
        Page<NotificationChannel> page = notificationChannelRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/notification-channels");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /notification-channels/:id : get the "id" notificationChannel.
     *
     * @param id the id of the notificationChannel to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the notificationChannel, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/notification-channels/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NotificationChannel> getNotificationChannel(@PathVariable Long id) {
        log.debug("REST request to get NotificationChannel : {}", id);
        NotificationChannel notificationChannel = notificationChannelRepository.findOne(id);
        return Optional.ofNullable(notificationChannel)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /notification-channels/:id : delete the "id" notificationChannel.
     *
     * @param id the id of the notificationChannel to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/notification-channels/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteNotificationChannel(@PathVariable Long id) {
        log.debug("REST request to delete NotificationChannel : {}", id);
        notificationChannelRepository.delete(id);
        notificationChannelSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("notificationChannel", id.toString())).build();
    }

    /**
     * SEARCH  /_search/notification-channels?query=:query : search for the notificationChannel corresponding
     * to the query.
     *
     * @param query the query of the notificationChannel search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/notification-channels",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<NotificationChannel>> searchNotificationChannels(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of NotificationChannels for query {}", query);
        Page<NotificationChannel> page = notificationChannelSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/notification-channels");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
