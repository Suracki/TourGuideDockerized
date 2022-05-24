package rewardsDocker.remote.user.model.inputEntities;

import gpsUtil.location.Attraction;

public class UserAndReward {

    public String userName;
    public VisitedLocation visitedLocation;
    public Attraction attraction;
    public int rewardPoints;

    public UserAndReward(){};

    public UserAndReward(String userName, gpsUtil.location.VisitedLocation visitedLocation, Attraction attraction, int rewardPoints) {
        this.userName = userName;
        this.visitedLocation = new VisitedLocation(visitedLocation);
        this.attraction = attraction;
        this.rewardPoints = rewardPoints;
    }
}

