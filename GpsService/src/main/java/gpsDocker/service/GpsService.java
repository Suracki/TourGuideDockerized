package gpsDocker.service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class GpsService {
    private Logger logger = LoggerFactory.getLogger(GpsService.class);
    private final GpsUtil gpsUtil;

    public GpsService(GpsUtil gpsUtil) {
        this.gpsUtil = gpsUtil;
    }

    public VisitedLocation getUserLocation(UUID userId) {
        logger.info("getUserLocation called");
        return gpsUtil.getUserLocation(userId);
    }

    public List<Attraction> getAttractions() {
        logger.info("getAttractions called");
        return new ArrayList<Attraction>(gpsUtil.getAttractions());
    }

}
