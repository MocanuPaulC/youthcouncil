package be.kdg.youthcouncil.persistence.users;

import be.kdg.youthcouncil.domain.users.GeneralAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<GeneralAdmin, Long> {

	@Query (
			"SELECT a FROM GeneralAdmin a WHERE a.username = :username"
	)
	Optional<GeneralAdmin> findByUsername(String username);
}
