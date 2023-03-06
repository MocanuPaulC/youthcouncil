package be.kdg.youthcouncil.utility;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@Getter
@Setter
public class PageVisit {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private String page;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public PageVisit(String page, LocalDateTime startTime, LocalDateTime endTime) {
        logger.debug("Creating pagevisit for page " + page + " from " + startTime + " to " + endTime);
        this.page = page;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public PageVisit() {
        logger.debug("Creating empty pagevisit");
    }
}
