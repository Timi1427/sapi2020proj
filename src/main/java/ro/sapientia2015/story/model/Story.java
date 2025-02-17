package ro.sapientia2015.story.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import ro.sapientia2015.scrum.model.Scrum;

import java.util.List;

import javax.persistence.*;

/**
 * @author Kiss Tibor
 */
@Entity
@Table(name="story")
public class Story {

    public static final int MAX_LENGTH_DESCRIPTION = 500;
    public static final int MAX_LENGTH_TITLE = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "creation_time", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime creationTime;

    @Column(name = "description", nullable = true, length = MAX_LENGTH_DESCRIPTION)
    private String description;

    @Column(name = "modification_time", nullable = false)
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime modificationTime;

    @Column(name = "title", nullable = false, length = MAX_LENGTH_TITLE)
    private String title;

    @JoinColumn(name="assigned_team_join")
    @ManyToOne()
    private Scrum assignedTeam;
    
    @Version
    private long version;

    public Story() {

    }

    public static Builder getBuilder(String title) {
        return new Builder(title);
    }

    public Long getId() {
        return id;
    }

    public DateTime getCreationTime() {
        return creationTime;
    }

    public String getDescription() {
        return description;
    }

    public DateTime getModificationTime() {
        return modificationTime;
    }

    public String getTitle() {
        return title;
    }

    public long getVersion() {
        return version;
    }

    public Scrum getAssignedTeam() {
		return assignedTeam;
	}

	@PrePersist
    public void prePersist() {
        DateTime now = DateTime.now();
        creationTime = now;
        modificationTime = now;
    }

    @PreUpdate
    public void preUpdate() {
        modificationTime = DateTime.now();
    }

    public void update(String description, String title) {
        this.description = description;
        this.title = title;
    }
    
    public void update(String description, String title, Scrum assignedTeam) {
        this.description = description;
        this.title = title;
        this.assignedTeam = assignedTeam;
    }

    public static class Builder {

        private Story built;

        public Builder(String title) {
            built = new Story();
            built.title = title;
        }

        public Story build() {
            return built;
        }

        public Builder description(String description) {
            built.description = description;
            return this;
        }
        
        public Builder assignedTeam(Scrum scrum) {
            built.assignedTeam = scrum;
            return this;
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

	public static Scrum filterScrumByName(List<Scrum> scrums, String selectedScrum) {
		for (Scrum s : scrums) {
			if (s.getTitle().equals(selectedScrum)) {
				return s;
			}
		}
		return null;
	}
}
