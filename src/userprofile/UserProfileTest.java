package userprofile;

import org.hive2hive.core.api.configs.FileConfiguration;

import configurations.PersystConfiguration;

/**
 * This provides a test for the User Profile object
 * 
 * @author xv435
 *
 */
public class UserProfileTest {

	public static void main(String[] args) {
		PersystConfiguration config = new PersystConfiguration(FileConfiguration.createDefault());
		UserProfile user = new UserProfile("user1", "pass1", config);
		System.out.println(user.getUsername() + " has password " + user.getPassword() + " and PIN " + user.getPIN());
		//Add config tests below
	}

}
