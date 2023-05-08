package be.kdg.youthcouncil.persistence.youthcouncil.modules;


import be.kdg.youthcouncil.domain.youthcouncil.YouthCouncil;
import be.kdg.youthcouncil.domain.youthcouncil.modules.InformativePage;
import be.kdg.youthcouncil.domain.youthcouncil.modules.InformativePageBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InformativePageRepository extends JpaRepository<InformativePage, Long> {

	@Query ("""
			SELECT i 
			FROM InformativePage i 
			WHERE i.title = :title 
			AND i.owningYouthCouncil.municipality = :municipality
			AND i.isDefault = false
			""")
	Optional<InformativePage> findByTitleAndMunicipality(String title, String municipality);

	@Query ("""
			select i
			from InformativePage i
			where i.title = :title
			and i.isDefault = true 
			""")
	Optional<InformativePage> findDefaultByTitle(String title);

	List<InformativePage> findAllByTitle(String title);

	List<InformativePage> findByTitleAndOwningYouthCouncil(String title, YouthCouncil youthCouncil);

	@Query ("""
			select i.infoPageBlocks 
			from InformativePage i 
			where i.owningYouthCouncil.municipality = :municipality 
			and i.title = :title
			and i.isDefault = false
			""")
	List<InformativePageBlock> findInfoPageBlocks(String municipality, String title);

	@Query ("""
			select i.infoPageBlocks
			from InformativePage i
			where i.title = :title
			and i.isDefault = true
			""")
	List<InformativePageBlock> findInfoPageBlocks(String title);
}
