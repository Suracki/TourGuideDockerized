package gpsDocker.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gpsDocker.service.GpsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class GpsServiceController {

    private Logger logger = LoggerFactory.getLogger(GpsServiceController.class);
    @Autowired
    private GpsService gpsService;

    Gson gson = new GsonBuilder().create();

//    //@Autowired
//    public GpsServiceController(){
//        logger.info("GpsServiceController initialized");
////        gson = new GsonBuilder().create();
//    }

    @GetMapping("/gps/getUserLocation")
    public String getUserLocation(@RequestParam UUID userId) {
        logger.info("/getUserLocation endpoint called");
        return gson.toJson(gpsService.getUserLocation(userId));
    }

    @GetMapping("/gps/getAttractions")
    public String getAttractions() {
        logger.info("/getAttractions endpoint called");
        return gson.toJson(gpsService.getAttractions());
    }

}
