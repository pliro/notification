package io.aurora.ams.repository;

import io.aurora.ams.domain.NotificationNotifieeType;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the NotificationNotifieeType entity.
 */
@SuppressWarnings("unused")
public interface NotificationNotifieeTypeRepository extends JpaRepository<NotificationNotifieeType,Long> {

}
