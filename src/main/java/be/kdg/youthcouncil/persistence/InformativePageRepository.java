package be.kdg.youthcouncil.persistence;


import be.kdg.youthcouncil.domain.youthCouncil.InformativePage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface InformativePageRepository extends JpaRepository<InformativePage, Long> {

}
