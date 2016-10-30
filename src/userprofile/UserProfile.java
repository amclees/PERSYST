package userprofile;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import filemanager.PersistentStorage;

public class UserProfile {
	private Map<String, Serializable> configMap;
	//Config object
	private String username;
	private String password;
	private String pin;
	
	public UserProfile(String username, String password/*, Configuration config*/) {
		this.configMap = new HashMap<String, Serializable>();
		this.username = username;
		this.password = password;
		//this.pin = new BigInteger(130, new SecureRandom()).toString(32);
		this.pin = "Default PIN";
	}
	
	public UserProfile(String username, String password, String pin/*, Configuration config*/) {
		this.configMap = new HashMap<String, Serializable>();
		this.username = username;
		this.password = password;
		this.pin = pin;
	}
	

	public Serializable getConfiguration(String configuration) {
		return configMap.get(configuration);
	}

	public void setConfiguration(String configuration, Serializable value) {
		//Make change to config
		configMap.put(configuration, value);
	}
	
	//public void setConfigurations(Config conf)
	//this.config = conf;
	//update this.configMap
	
	
	public byte[] getConfigData() {
		/*byte[] pin = this.pin.getBytes();
		byte[] config = PersistentStorage.toBytes( Config );
		
		byte[] data = new byte[pin.length + config.length];
		System.arraycopy(pin, 0, data, 0, pin.length);
		System.arraycopy(config, 0, data, pin.length, config.length);
		return data;*/
		return PersistentStorage.toBytes( null ); //Config bytes
		
	}
	
	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public String getPIN() {
		return pin;
	}
	
}
