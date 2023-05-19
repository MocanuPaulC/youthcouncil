package be.kdg.youthcouncil.persistence.youthcouncil.modules;

import be.kdg.youthcouncil.domain.youthcouncil.modules.ActionPoint;
import be.kdg.youthcouncil.domain.youthcouncil.modules.ActionPointBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ActionPointRepository extends JpaRepository<ActionPoint, Long> {

	@Query (
			"SELECT ap FROM ActionPoint ap " +
					"JOIN FETCH ap.shares " +
					"WHERE ap = :actionPoint"
	)
	ActionPoint findWithShares(ActionPoint actionPoint);

	@Query (
			"SELECT ap FROM ActionPoint ap " +
					"JOIN FETCH ap.subscriptions " +
					"WHERE ap.actionPointId = :actionPointId"
	)
	Optional<ActionPoint> findByIdWithSubscriptions(long actionPointId);

	@Query ("""
			SELECT a
			FROM ActionPoint a
			WHERE a.title = :title
			AND a.owningYouthCouncil.municipality.name = :municipality
			AND a.isDefault = false
			""")
	Optional<ActionPoint> findByTitleAndMunicipality(String title, String municipality);

	@Query ("""
			select a
			from ActionPoint a
			where a.title = :title
			and a.isDefault = true
			""")
	Optional<ActionPoint> findDefaultByTitle(String title);

	@Query ("""
			select a.actionPointBlocks 
			from ActionPoint a 
			where a.owningYouthCouncil.municipality.name = :municipality 
			and a.actionPointId = :actionPointId
			and a.isDefault = false
			""")
	List<ActionPointBlock> findActionPointBlocks(String municipality, long actionPointId);

	@Query ("""
			select a.actionPointBlocks
			from ActionPoint a
			where a.actionPointId = :actionPointId
			and a.isDefault = true
			""")
	List<ActionPointBlock> findActionPointBlocks(long actionPointId);

}
