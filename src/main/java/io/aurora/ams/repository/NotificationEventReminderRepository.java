package io.aurora.ams.repository;

import io.aurora.ams.domain.NotificationEventReminder;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the NotificationEventReminder entity.
 */
@SuppressWarnings("unused")
public interface NotificationEventReminderRepository extends JpaRepository<NotificationEventReminder,Long> {

}
