package be.kdg.youthcouncil.persistence.youthcouncil.modules;

import be.kdg.youthcouncil.domain.youthcouncil.modules.CallForIdea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CallForIdeaRepository extends JpaRepository<CallForIdea, Long> {
	@Query ("""
			SELECT c FROM CallForIdea c
			LEFT JOIN FETCH c.ideas
			WHERE c.callForIdeaId = :id
			""")
	Optional<CallForIdea> findWithIdeas(long id);
}
