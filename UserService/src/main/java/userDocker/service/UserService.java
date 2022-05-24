package userDocker.service;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import userDocker.remote.user.model.inputEntities.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import userDocker.remote.gps.GpsRetro;
import userDocker.remote.user.model.User;
import userDocker.remote.user.model.UserReward;
import userDocker.remote.user.model.outputEntities.UserLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Service
public class UserService {
	private Logger logger = LoggerFactory.getLogger(UserService.class);
	private ConcurrentMap<String, User> usersByName;
	private final GpsRetro gpsRetro;

	public UserService(GpsRetro gpsRetro) {
		this.gpsRetro = gpsRetro;
		final int CAPACITY = 100;
		usersByName = new ConcurrentHashMap<String, User>(CAPACITY);
	}

	public boolean addUser(User user){
		logger.info("addUser called");
		if(!usersByName.containsKey(user.getUserName())) {
			usersByName.put(user.getUserName(), user);
			logger.info("userName not already in map, adding user. New collection size: " + usersByName.size());
			logger.debug("UUID added: " + user.getUserId());
			return true;
		}
		logger.info("userName is already in map, failed to add user");
		return false;
	}

	//TODO: add fail case for user not found
	public String addToVisitedLocations(VisitedLocation visitedLocation, String userName) {
		logger.debug("addToVisitedLocations called");
		usersByName.get(userName).addToVisitedLocations(visitedLocation);
		return userName;
	}

	public List<UserLocation> getAllCurrentLocations() {
		logger.debug("getAllCurrentLocations called");
		List<UserLocation> userLocations = new ArrayList<>();
		usersByName.forEach((k,v)-> {
			userLocations.add(new UserLocation(v.getUserId(), v.getLastVisitedLocation()));
		});
		logger.debug("returning " + userLocations.size() + " UserLocations");
		return userLocations;
	}

	//TODO: add fail case for user not found
	public boolean addUserReward(String userName, VisitedLocation visitedLocation, Attraction attraction, int rewardPoints) {
		System.out.println("ADD USER REWARD");
		logger.debug("addUserReward called");
		User user = getUserByUsername(userName);
		user.addUserReward(new UserReward(visitedLocation(visitedLocation), attraction, rewardPoints));
		return true;
	}

	private gpsUtil.location.VisitedLocation visitedLocation(VisitedLocation visitedLocation) {
		return new gpsUtil.location.VisitedLocation(visitedLocation.userId, gpsLocation(visitedLocation.location), visitedLocation.timeVisited);
	}

	private Location gpsLocation(userDocker.remote.user.model.inputEntities.Location location) {
		return new Location(location.latitude, location.longitude);
	}

	public List<User> getAllUsers() {
		logger.debug("getAllUsers called");
		logger.debug("returning " + usersByName.size() + " Users");
		return usersByName.values().stream().collect(Collectors.toList());
	}

	//TODO: add fail case for user not found
	public User getUserByUsername(String userName) {
		logger.debug("getUserByUsername called");
		return usersByName.get(userName);
	}

	//TODO: add fail case for user not found
	public VisitedLocation getLastVisitedLocationByName(String userName) {
		logger.debug("getLastVisitedLocationByName called");
		logger.debug("UUID found: " + usersByName.get(userName).getUserId());
		return getUserByUsername(userName).getLastVisitedLocation();
	}

	//TODO: add fail case for user not found
	public List<VisitedLocation> getVisitedLocationsByUsername(String userName) {
		logger.debug("getVisitedLocationsByUsername called");
		logger.debug("UUID found: " + usersByName.get(userName).getUserId());
		logger.debug("Locations: " + getUserByUsername(userName).getVisitedLocations().size());
		return getUserByUsername(userName).getVisitedLocations();
	}

	//TODO: add fail case for user not found
	public List<UserReward> getUserRewardsByUsername(String userName){
		logger.debug("getUserRewardsByUsername called");
		System.out.println(userName + " User Rewards Size CURRENT: " + getUserByUsername(userName).getUserRewards().size());
		return getUserByUsername(userName).getUserRewards();
	}

	//TODO: add fail case for user not found
	public UUID getUserIdByUsername(String userName) {
		logger.debug("getUserIdByUsername called");
		return getUserByUsername(userName).getUserId();
	}

	//TODO: add fail case
	public boolean trackAllUserLocations() {
		logger.debug("trackAllUserLocations called");

		List<User> allUsers = getAllUsers();
		ArrayList<Thread> threads = new ArrayList<>();

		logger.debug("Creating threads for " + allUsers.size() + " user(s)");

		allUsers.forEach((n)-> {
			threads.add(
					new Thread( ()-> {
						addToVisitedLocations(toNewVisitedLocation(gpsRetro.getUserLocation(n.getUserId())), n.getUserName());
					})
			);
		});

		logger.debug("Threads created: " + threads.size() + ", calling start()...");
		threads.forEach((n)->n.start());
		threads.forEach((n)-> {
			try {
				n.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		logger.debug("Threads join()ed, returning.");
		return true;
	}

	private VisitedLocation toNewVisitedLocation(gpsUtil.location.VisitedLocation userLocation) {
		return new VisitedLocation(userLocation);
	}

	public int getUserCount() {
		logger.debug("getUserCount called");
		return usersByName.size();
	}

	public List<String> getAllUserNames() {
		logger.debug("getAllUserNames called");
		logger.debug("returning " + usersByName.size() + " UserNames");
		return usersByName.values().stream().map(User::getUserName).collect(Collectors.toList());
	}
}
