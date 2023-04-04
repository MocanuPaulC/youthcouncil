package be.kdg.youthcouncil.persistence.youthcouncil.modules.themes;

import be.kdg.youthcouncil.domain.youthcouncil.modules.themes.SubTheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubThemeRepository extends JpaRepository<SubTheme, Long> {
}
