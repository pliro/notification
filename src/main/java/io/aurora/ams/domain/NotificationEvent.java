package io.aurora.ams.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import io.aurora.ams.domain.enumeration.NotificationEventName;

/**
 * An event on which the notification should be sent                           
 * 
 */
@ApiModel(description = ""
    + "An event on which the notification should be sent                      "
    + "")
@Entity
@Table(name = "notification_event")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "notificationevent")
public class NotificationEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * UUID - business id                                                      
     * 
     */
    @ApiModelProperty(value = ""
        + "UUID - business id                                                 "
        + "")
    @NotNull
    @Column(name = "notification_event_id", nullable = false)
    private String notificationEventId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "notification_event_name", nullable = false)
    private NotificationEventName notificationEventName;

    @OneToMany(mappedBy = "notificationEvent")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<NotificationTemplate> templates = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNotificationEventId() {
        return notificationEventId;
    }

    public void setNotificationEventId(String notificationEventId) {
        this.notificationEventId = notificationEventId;
    }

    public NotificationEventName getNotificationEventName() {
        return notificationEventName;
    }

    public void setNotificationEventName(NotificationEventName notificationEventName) {
        this.notificationEventName = notificationEventName;
    }

    public Set<NotificationTemplate> getTemplates() {
        return templates;
    }

    public void setTemplates(Set<NotificationTemplate> notificationTemplates) {
        this.templates = notificationTemplates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NotificationEvent notificationEvent = (NotificationEvent) o;
        if(notificationEvent.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, notificationEvent.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "NotificationEvent{" +
            "id=" + id +
            ", notificationEventId='" + notificationEventId + "'" +
            ", notificationEventName='" + notificationEventName + "'" +
            '}';
    }
}
