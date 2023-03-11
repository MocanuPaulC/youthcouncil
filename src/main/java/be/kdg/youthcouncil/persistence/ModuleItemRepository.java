package be.kdg.youthcouncil.persistence;

import be.kdg.youthcouncil.domain.ModuleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleItemRepository extends JpaRepository<ModuleItem, Long> {

}
