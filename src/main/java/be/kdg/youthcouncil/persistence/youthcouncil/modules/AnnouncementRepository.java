package be.kdg.youthcouncil.persistence.youthcouncil.modules;

import be.kdg.youthcouncil.domain.youthcouncil.modules.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
	@Query (
			"SELECT a FROM Announcement a WHERE a.owningYouthCouncil.municipality.name = :municipality"
	)
	List<Announcement> findByMunicipality(String municipality);


	@Query ("""
			SELECT a FROM Announcement  a
			WHERE a.owningYouthCouncil.municipality.name = :municipality
			AND a.announcementId = :id
			""")
	Optional<Announcement> findByIdAndMunicipality(String municipality, long id);
}
