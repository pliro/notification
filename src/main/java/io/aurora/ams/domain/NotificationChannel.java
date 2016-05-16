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

import io.aurora.ams.domain.enumeration.NotificationChannelName;

/**
 * A channel through which a participant can be notified                       
 * 
 */
@ApiModel(description = ""
    + "A channel through which a participant can be notified                  "
    + "")
@Entity
@Table(name = "notification_channel")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "notificationchannel")
public class NotificationChannel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * UUID - business idA unique idd of the notification channel              
     * 
     */
    @ApiModelProperty(value = ""
        + "UUID - business idA unique idd of the notification channel         "
        + "")
    @NotNull
    @Column(name = "notification_channel_id", nullable = false)
    private String notificationChannelId;

    /**
     * Name of the channel                                                     
     * 
     */
    @ApiModelProperty(value = ""
        + "Name of the channel                                                "
        + "")
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "notification_channel_name", nullable = false)
    private NotificationChannelName notificationChannelName;

    @OneToMany(mappedBy = "notificationChannel")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<NotificationTemplate> templates = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNotificationChannelId() {
        return notificationChannelId;
    }

    public void setNotificationChannelId(String notificationChannelId) {
        this.notificationChannelId = notificationChannelId;
    }

    public NotificationChannelName getNotificationChannelName() {
        return notificationChannelName;
    }

    public void setNotificationChannelName(NotificationChannelName notificationChannelName) {
        this.notificationChannelName = notificationChannelName;
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
        NotificationChannel notificationChannel = (NotificationChannel) o;
        if(notificationChannel.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, notificationChannel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "NotificationChannel{" +
            "id=" + id +
            ", notificationChannelId='" + notificationChannelId + "'" +
            ", notificationChannelName='" + notificationChannelName + "'" +
            '}';
    }
}
