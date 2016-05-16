package io.aurora.ams.repository.search;

import io.aurora.ams.domain.NotificationChannel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the NotificationChannel entity.
 */
public interface NotificationChannelSearchRepository extends ElasticsearchRepository<NotificationChannel, Long> {
}
