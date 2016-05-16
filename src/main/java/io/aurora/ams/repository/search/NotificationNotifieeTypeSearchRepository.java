package io.aurora.ams.repository.search;

import io.aurora.ams.domain.NotificationNotifieeType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the NotificationNotifieeType entity.
 */
public interface NotificationNotifieeTypeSearchRepository extends ElasticsearchRepository<NotificationNotifieeType, Long> {
}
