package be.kdg.youthcouncil.persistence;


import be.kdg.youthcouncil.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	Optional<User> findByUsername(String username);

	@Query ("""
			select u from User u
			join fetch u.ideas
			where u.username = :username""")
	Optional<User> findByUsernameWithIdeas(String username);

	@Query ("""
			select u from User u
			left join fetch u.ideas""")
	List<User> findAllWithIdeas();
}
