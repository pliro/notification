package io.aurora.ams.repository.search;

import io.aurora.ams.domain.NotificationTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the NotificationTemplate entity.
 */
public interface NotificationTemplateSearchRepository extends ElasticsearchRepository<NotificationTemplate, Long> {
}
