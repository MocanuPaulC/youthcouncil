package be.kdg.youthcouncil.persistence;

import be.kdg.youthcouncil.domain.moduleItems.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

	@Query (
			"SELECT a FROM Announcement a WHERE a.owningYouthCouncil.municipality = :municipality"
	)
	List<Announcement> findByMunicipality(String municipality);
}
