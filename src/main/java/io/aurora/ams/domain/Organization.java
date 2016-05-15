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

/**
 * A formally or informally recognized grouping of people or organizationsformed for the purpose of achieving some form of collective action.Includes companies, institutions, corporations, departments,community groups, healthcare practice groups, etc.
 * 
 */
@ApiModel(description = ""
    + "A formally or informally recognized grouping of people or organizationsformed for the purpose of achieving some form of collective action.Includes companies, institutions, corporations, departments,community groups, healthcare practice groups, etc."
    + "")
@Entity
@Table(name = "organization")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "organization")
public class Organization implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * UUID - business idIdentifies this organization across multiple systems  
     * 
     */
    @ApiModelProperty(value = ""
        + "UUID - business idIdentifies this organization across multiple systems"
        + "")
    @NotNull
    @Column(name = "organization_id", nullable = false)
    private String organizationId;

    /**
     * Name used for the organization                                          
     * 
     */
    @ApiModelProperty(value = ""
        + "Name used for the organization                                     "
        + "")
    @NotNull
    @Column(name = "organization_name", nullable = false)
    private String organizationName;

    @OneToMany(mappedBy = "organization")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<NotificationTemplate> templates = new HashSet<>();

    @OneToMany(mappedBy = "organization")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<NotificationEventReminder> reminders = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
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
        Organization organization = (Organization) o;
        if(organization.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, organization.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Organization{" +
            "id=" + id +
            ", organizationId='" + organizationId + "'" +
            ", organizationName='" + organizationName + "'" +
            '}';
    }
}
