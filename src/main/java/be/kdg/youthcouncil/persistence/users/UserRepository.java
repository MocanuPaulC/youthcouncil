package be.kdg.youthcouncil.persistence.users;


import be.kdg.youthcouncil.domain.users.PlatformUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<PlatformUser, Long> {

	Optional<PlatformUser> findByEmail(String email);

	Optional<PlatformUser> findByUsername(String username);

	@Query ("""
			select u from PlatformUser u
			join fetch u.ideas
			where u.username = :username""")
	Optional<PlatformUser> findByUsernameWithIdeas(String username);

	@Query ("""
			select u from PlatformUser u
			left join fetch u.ideas""")
	List<PlatformUser> findAllWithIdeas();

	@Query (
			"SELECT u FROM PlatformUser u " +
					"LEFT JOIN FETCH u.youthCouncilSubscriptions " +
					"WHERE u.username = :username"
	)
	Optional<PlatformUser> findWithSubscriptions(String username);
}
