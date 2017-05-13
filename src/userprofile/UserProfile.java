package userprofile;

import java.io.File;
import java.io.Serializable;
import java.math.BigInteger;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.hive2hive.core.H2HConstants;
import org.hive2hive.core.api.configs.FileConfiguration;

import centralprocessor.PERSYSTSession;
import configurations.PersystConfiguration;
import filemanager.PersistentStorage;

/**
 * Object to store persistent data about the current user and distribute the
 * data to the other modules. Includes methods for accessing configurations and
 * user credentials.
 * 
 * @author xv435
 *
 */
public class UserProfile {
  private Map<String, Serializable> configMap;
  private PersystConfiguration config;
  private String username;
  private String password;
  private String pin;
  public File rootFolder;
  private LinkedList<InetAddress> ipList; // Not list because list doesn't
  // implement serializable

  /**
   * This method updates the User Profile Configurations map so that is is in
   * sync with the config object.
   */
  private void updateMap() {
    configMap = new HashMap<String, Serializable>();

    // These four are the only configurations that need to have
    // getter/setters as of now
    // TODO Add getters from config to the map rather than null
    configMap.put("rootfolder", this.rootFolder);
    // configMap.put("port", null);
    configMap.put("maxfilesize", config.getFileConfig().getMaxFileSize());
    // configMap.put("iplist", this.ipList);
    // this.config.rootFolder = this.rootFolder;
    PERSYSTSession.rootFolder = this.rootFolder;
    PERSYSTSession.config = this.config;
  }

  /**
   * This constructor creates a User Profile from a username, password, and
   * configurations object
   * 
   * @param username
   *          The user's username
   * @param password
   *          The user's password
   * @param config
   *          A configuration object to use
   */
  public UserProfile(String username, String password, PersystConfiguration config) {
    this.username = username;
    this.password = password;
    // this.pin = new BigInteger(130, new SecureRandom()).toString(32);
    this.pin = "Default PIN";
    this.config = config;
    // PERSYSTSession.rootFolder = config.rootFolder;
    this.rootFolder = PERSYSTSession.rootFolder;
    // config.rootFolder = PERSYSTSession.rootFolder;
    updateMap();

  }

  /**
   * This method gets the configuration with the specified name
   * 
   * @param configuration
   *          The name of the configuration
   * @return The value of the configuration with the specified name
   */
  public Serializable getConfiguration(String configuration) {
    return configMap.get(configuration);
  }

  /**
   * This method sets a configuration to the specified value
   * 
   * @param configuration
   *          The name of the configuration to set
   * @param value
   *          A serializable object to set
   */
  public void setConfiguration(String configuration, Serializable value) {
    if (configuration.equals("rootfolder")) {
      // this.config.rootFolder = (File) value;
      PERSYSTSession.rootFolder = (File) value;
      this.rootFolder = (File) value;
    } else if (configuration.equals("maxfilesize")) {
      try {
        BigInteger bigint = BigInteger.valueOf(Long.parseLong((String) value));
        this.config = new PersystConfiguration(
            FileConfiguration.createCustom(bigint, H2HConstants.DEFAULT_MAX_NUM_OF_VERSIONS,
                H2HConstants.DEFAULT_MAX_SIZE_OF_ALL_VERSIONS, H2HConstants.DEFAULT_CHUNK_SIZE));

      } catch (Exception e) {
        System.out.println("Failed to change max file size due to " + e.getMessage());
      }
    } /*
       * else if (configuration.equals("iplist")) {
       * 
       * }
       */ else {
      return;
    }

    updateMap();
    // configMap.put(configuration, value);
  }

  /**
   * This method sets the user configurations to a PersystConfiguration object
   * 
   * @param config
   *          Object to set configurations to
   */
  public void setConfigurations(PersystConfiguration config) {
    this.config = config;
    PERSYSTSession.config = config;
    updateMap();
  }

  /**
   * This method converts the config object to bytes for storage.
   * 
   * @return a byte[] corresponding to a PersystConfiguration object
   */
  public byte[] getConfigData() {
    // This code is for storing the pin
    /*
     * byte[] pin = this.pin.getBytes(); byte[] config =
     * PersistentStorage.toBytes( Config );
     * 
     * byte[] data = new byte[pin.length + config.length]; System.arraycopy(pin,
     * 0, data, 0, pin.length); System.arraycopy(config, 0, data, pin.length,
     * config.length); return data;
     */
    System.out.println(this.config.getFileConfig().getMaxFileSize());
    return PersistentStorage.toBytes(this.config);

  }

  /**
   * @return The user's password
   */
  public String getPassword() {
    return password;
  }

  /**
   * @return The user's username
   */
  public String getUsername() {
    return username;
  }

  /**
   * @return The user's PIN
   */
  public String getPIN() {
    return pin;
  }

}
