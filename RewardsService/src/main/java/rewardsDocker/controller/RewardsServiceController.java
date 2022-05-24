package rewardsDocker.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rewardsDocker.remote.user.gson.MoneyTypeAdapterFactory;


import java.util.UUID;

@RestController
public class RewardsServiceController {

    private Logger logger = LoggerFactory.getLogger(RewardsServiceController.class);
    private rewardsDocker.service.RewardsService rewardsService;

    Gson gson = new Gson();

    public RewardsServiceController(rewardsDocker.service.RewardsService rewardsService){
        this.rewardsService = rewardsService;
        gson = new GsonBuilder().registerTypeAdapterFactory(new MoneyTypeAdapterFactory()).create();
    }

    @GetMapping("/rewards/getRewardValue")
    public String getRewardValue(@RequestParam UUID attractionId, @RequestParam UUID userid) {
        logger.info("/getRewardValue endpoint called");
        return gson.toJson(rewardsService.getRewardValue(attractionId, userid));
    }

    @PostMapping("/rewards/calculateRewardsByUsername")
    public String calculateRewardsByUsername(@RequestParam String userName) {
        System.out.println("TEST HERE");
        logger.info("/calculateRewardsByUsername endpoint called");
        return gson.toJson(rewardsService.calculateRewardsByUsername(userName));
    }

}
