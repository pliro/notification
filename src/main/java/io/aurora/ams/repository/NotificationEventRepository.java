package io.aurora.ams.repository;

import io.aurora.ams.domain.NotificationEvent;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the NotificationEvent entity.
 */
@SuppressWarnings("unused")
public interface NotificationEventRepository extends JpaRepository<NotificationEvent,Long> {

}
