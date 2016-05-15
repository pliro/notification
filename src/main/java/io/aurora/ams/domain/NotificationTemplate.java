package io.aurora.ams.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A notification template                                                     
 * 
 */
@ApiModel(description = ""
    + "A notification template                                                "
    + "")
@Entity
@Table(name = "notification_template")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "notificationtemplate")
public class NotificationTemplate implements Serializable {

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
    @Column(name = "notification_template_id", nullable = false)
    private String notificationTemplateId;

    /**
     * Template to be filled at runtime                                        
     * 
     */
    @ApiModelProperty(value = ""
        + "Template to be filled at runtime                                   "
        + "")
    @NotNull
    @Column(name = "notification_template", nullable = false)
    private String notificationTemplate;

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

    public String getNotificationTemplateId() {
        return notificationTemplateId;
    }

    public void setNotificationTemplateId(String notificationTemplateId) {
        this.notificationTemplateId = notificationTemplateId;
    }

    public String getNotificationTemplate() {
        return notificationTemplate;
    }

    public void setNotificationTemplate(String notificationTemplate) {
        this.notificationTemplate = notificationTemplate;
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
        NotificationTemplate notificationTemplate = (NotificationTemplate) o;
        if(notificationTemplate.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, notificationTemplate.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "NotificationTemplate{" +
            "id=" + id +
            ", notificationTemplateId='" + notificationTemplateId + "'" +
            ", notificationTemplate='" + notificationTemplate + "'" +
            '}';
    }
}
