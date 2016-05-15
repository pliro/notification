package io.aurora.ams.repository.search;

import io.aurora.ams.domain.NotificationEvent;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the NotificationEvent entity.
 */
public interface NotificationEventSearchRepository extends ElasticsearchRepository<NotificationEvent, Long> {
}
