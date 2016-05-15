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

import io.aurora.ams.domain.enumeration.NotificationNotifieeTypeName;

/**
 * The type of the notifiee; which is reciepent of the notification.           
 * 
 */
@ApiModel(description = ""
    + "The type of the notifiee; which is reciepent of the notification.      "
    + "")
@Entity
@Table(name = "notification_notifiee_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "notificationnotifieetype")
public class NotificationNotifieeType implements Serializable {

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
    @Column(name = "notification_notifiee_type_id", nullable = false)
    private String notificationNotifieeTypeId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "notification_notifiee_type_name", nullable = false)
    private NotificationNotifieeTypeName notificationNotifieeTypeName;

    @OneToMany(mappedBy = "notificationNotifieeType")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<NotificationTemplate> templates = new HashSet<>();

    @OneToMany(mappedBy = "notificationNotifieeType")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<NotificationEventReminder> reminders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNotificationNotifieeTypeId() {
        return notificationNotifieeTypeId;
    }

    public void setNotificationNotifieeTypeId(String notificationNotifieeTypeId) {
        this.notificationNotifieeTypeId = notificationNotifieeTypeId;
    }

    public NotificationNotifieeTypeName getNotificationNotifieeTypeName() {
        return notificationNotifieeTypeName;
    }

    public void setNotificationNotifieeTypeName(NotificationNotifieeTypeName notificationNotifieeTypeName) {
        this.notificationNotifieeTypeName = notificationNotifieeTypeName;
    }

    public Set<NotificationTemplate> getTemplates() {
        return templates;
    }

    public void setTemplates(Set<NotificationTemplate> notificationTemplates) {
        this.templates = notificationTemplates;
    }

    public Set<NotificationEventReminder> getReminders() {
        return reminders;
    }

    public void setReminders(Set<NotificationEventReminder> notificationEventReminders) {
        this.reminders = notificationEventReminders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NotificationNotifieeType notificationNotifieeType = (NotificationNotifieeType) o;
        if(notificationNotifieeType.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, notificationNotifieeType.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "NotificationNotifieeType{" +
            "id=" + id +
            ", notificationNotifieeTypeId='" + notificationNotifieeTypeId + "'" +
            ", notificationNotifieeTypeName='" + notificationNotifieeTypeName + "'" +
            '}';
    }
}
