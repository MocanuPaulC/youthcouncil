package be.kdg.youthcouncil.persistence.youthcouncil.modules;

import be.kdg.youthcouncil.domain.youthcouncil.modules.ActionPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ActionPointRepository extends JpaRepository<ActionPoint, Long> {

	@Query (
			"SELECT ap FROM ActionPoint ap " +
					"JOIN FETCH ap.shares " +
					"WHERE ap = :actionPoint"
	)
	ActionPoint findWithShares(ActionPoint actionPoint);

}
