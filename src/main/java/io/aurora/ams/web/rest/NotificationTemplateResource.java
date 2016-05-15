package io.aurora.ams.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.aurora.ams.domain.NotificationTemplate;
import io.aurora.ams.repository.NotificationTemplateRepository;
import io.aurora.ams.repository.search.NotificationTemplateSearchRepository;
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
 * REST controller for managing NotificationTemplate.
 */
@RestController
@RequestMapping("/api")
public class NotificationTemplateResource {

    private final Logger log = LoggerFactory.getLogger(NotificationTemplateResource.class);
        
    @Inject
    private NotificationTemplateRepository notificationTemplateRepository;
    
    @Inject
    private NotificationTemplateSearchRepository notificationTemplateSearchRepository;
    
    /**
     * POST  /notification-templates : Create a new notificationTemplate.
     *
     * @param notificationTemplate the notificationTemplate to create
     * @return the ResponseEntity with status 201 (Created) and with body the new notificationTemplate, or with status 400 (Bad Request) if the notificationTemplate has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/notification-templates",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NotificationTemplate> createNotificationTemplate(@Valid @RequestBody NotificationTemplate notificationTemplate) throws URISyntaxException {
        log.debug("REST request to save NotificationTemplate : {}", notificationTemplate);
        if (notificationTemplate.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("notificationTemplate", "idexists", "A new notificationTemplate cannot already have an ID")).body(null);
        }
        NotificationTemplate result = notificationTemplateRepository.save(notificationTemplate);
        notificationTemplateSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/notification-templates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("notificationTemplate", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /notification-templates : Updates an existing notificationTemplate.
     *
     * @param notificationTemplate the notificationTemplate to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated notificationTemplate,
     * or with status 400 (Bad Request) if the notificationTemplate is not valid,
     * or with status 500 (Internal Server Error) if the notificationTemplate couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/notification-templates",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NotificationTemplate> updateNotificationTemplate(@Valid @RequestBody NotificationTemplate notificationTemplate) throws URISyntaxException {
        log.debug("REST request to update NotificationTemplate : {}", notificationTemplate);
        if (notificationTemplate.getId() == null) {
            return createNotificationTemplate(notificationTemplate);
        }
        NotificationTemplate result = notificationTemplateRepository.save(notificationTemplate);
        notificationTemplateSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("notificationTemplate", notificationTemplate.getId().toString()))
            .body(result);
    }

    /**
     * GET  /notification-templates : get all the notificationTemplates.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of notificationTemplates in body
     */
    @RequestMapping(value = "/notification-templates",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<NotificationTemplate> getAllNotificationTemplates() {
        log.debug("REST request to get all NotificationTemplates");
        List<NotificationTemplate> notificationTemplates = notificationTemplateRepository.findAll();
        return notificationTemplates;
    }

    /**
     * GET  /notification-templates/:id : get the "id" notificationTemplate.
     *
     * @param id the id of the notificationTemplate to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the notificationTemplate, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/notification-templates/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<NotificationTemplate> getNotificationTemplate(@PathVariable Long id) {
        log.debug("REST request to get NotificationTemplate : {}", id);
        NotificationTemplate notificationTemplate = notificationTemplateRepository.findOne(id);
        return Optional.ofNullable(notificationTemplate)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /notification-templates/:id : delete the "id" notificationTemplate.
     *
     * @param id the id of the notificationTemplate to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/notification-templates/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteNotificationTemplate(@PathVariable Long id) {
        log.debug("REST request to delete NotificationTemplate : {}", id);
        notificationTemplateRepository.delete(id);
        notificationTemplateSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("notificationTemplate", id.toString())).build();
    }

    /**
     * SEARCH  /_search/notification-templates?query=:query : search for the notificationTemplate corresponding
     * to the query.
     *
     * @param query the query of the notificationTemplate search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/notification-templates",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<NotificationTemplate> searchNotificationTemplates(@RequestParam String query) {
        log.debug("REST request to search NotificationTemplates for query {}", query);
        return StreamSupport
            .stream(notificationTemplateSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
