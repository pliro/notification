package io.aurora.ams.domain;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A NotificationEventReminder.
 */
@Entity
@Table(name = "notification_event_reminder")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "notificationeventreminder")
public class NotificationEventReminder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "notification_event_reminder_id", nullable = false)
    private String notificationEventReminderId;

    /**
     * The number of milliseconds before the appointment when the notificationshould be sent.
     * 
     */
    @ApiModelProperty(value = ""
        + "The number of milliseconds before the appointment when the notificationshould be sent."
        + "")
    @NotNull
    @Column(name = "notification_event_reminder_date_time", nullable = false)
    private Long notificationEventReminderDateTime;

    @ManyToOne
    private NotificationChannel notificationChannel;

    @ManyToOne
    private NotificationEvent notificationEvent;

    @ManyToOne
    private NotificationNotifieeType notificationNotifieeType;

    @ManyToOne
    private Organization organization;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNotificationEventReminderId() {
        return notificationEventReminderId;
    }

    public void setNotificationEventReminderId(String notificationEventReminderId) {
        this.notificationEventReminderId = notificationEventReminderId;
    }

    public Long getNotificationEventReminderDateTime() {
        return notificationEventReminderDateTime;
    }

    public void setNotificationEventReminderDateTime(Long notificationEventReminderDateTime) {
        this.notificationEventReminderDateTime = notificationEventReminderDateTime;
    }

    public NotificationChannel getNotificationChannel() {
        return notificationChannel;
    }

    public void setNotificationChannel(NotificationChannel notificationChannel) {
        this.notificationChannel = notificationChannel;
    }

    public NotificationEvent getNotificationEvent() {
        return notificationEvent;
    }

    public void setNotificationEvent(NotificationEvent notificationEvent) {
        this.notificationEvent = notificationEvent;
    }

    public NotificationNotifieeType getNotificationNotifieeType() {
        return notificationNotifieeType;
    }

    public void setNotificationNotifieeType(NotificationNotifieeType notificationNotifieeType) {
        this.notificationNotifieeType = notificationNotifieeType;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NotificationEventReminder notificationEventReminder = (NotificationEventReminder) o;
        if(notificationEventReminder.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, notificationEventReminder.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "NotificationEventReminder{" +
            "id=" + id +
            ", notificationEventReminderId='" + notificationEventReminderId + "'" +
            ", notificationEventReminderDateTime='" + notificationEventReminderDateTime + "'" +
            '}';
    }
}
