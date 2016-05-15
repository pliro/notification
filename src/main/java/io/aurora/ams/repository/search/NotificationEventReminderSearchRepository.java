package io.aurora.ams.repository.search;

import io.aurora.ams.domain.NotificationEventReminder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the NotificationEventReminder entity.
 */
public interface NotificationEventReminderSearchRepository extends ElasticsearchRepository<NotificationEventReminder, Long> {
}
