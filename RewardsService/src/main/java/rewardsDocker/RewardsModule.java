package rewardsDocker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rewardCentral.RewardCentral;

@Configuration
public class RewardsModule {

    @Bean
    public RewardCentral getRewardCentral() {
        return new RewardCentral();
    }

}
