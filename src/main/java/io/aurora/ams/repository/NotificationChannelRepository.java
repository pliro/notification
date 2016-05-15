package io.aurora.ams.repository;

import io.aurora.ams.domain.NotificationChannel;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the NotificationChannel entity.
 */
@SuppressWarnings("unused")
public interface NotificationChannelRepository extends JpaRepository<NotificationChannel,Long> {

}
