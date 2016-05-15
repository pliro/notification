package io.aurora.ams.repository;

import io.aurora.ams.domain.NotificationTemplate;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the NotificationTemplate entity.
 */
@SuppressWarnings("unused")
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate,Long> {

}
