package be.kdg.youthcouncil.persistence.media;

import be.kdg.youthcouncil.domain.media.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
}
