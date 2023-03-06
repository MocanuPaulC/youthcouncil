package be.kdg.youthcouncil.domain.moduleItems;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "newsfeed")
public class NewsFeed {

    @Transient
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @OneToMany
    private List<Announcement> announcementList;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public Announcement createAnnouncement(String title, String content) {
        logger.debug("Creating announcement " + title + " with content " + content);
        Announcement announcement = new Announcement(title, content);
        announcementList.add(announcement);
        return announcement;
    }

    public void removeAnnouncement(Announcement announcement) {
        logger.debug("Removing announcement " + announcement.getTitle() + " from newsfeed");
        announcementList.remove(announcement);
    }

}
