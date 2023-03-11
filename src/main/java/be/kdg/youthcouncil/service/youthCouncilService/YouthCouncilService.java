package be.kdg.youthcouncil.service.youthCouncilService;

import be.kdg.youthcouncil.domain.moduleItems.ActionPoint;
import be.kdg.youthcouncil.domain.youthCouncil.YouthCouncil;

import java.util.List;

public interface YouthCouncilService {
    List<YouthCouncil> getAllYouthCouncils();

    List<ActionPoint> getFilteredActionPoints(String municipalityName, String theme, String label);
}
