package centralprocessor;

import java.io.File;

import configurations.PersystConfiguration;
import userprofile.UserProfile;

public class PERSYSTSession {
  public static CommunicationsInterface comm;
  public static UserProfile usr; // Initialize in Central Processor
  public static PersystConfiguration config; // Initialize in Central Processor
  public static File rootFolder;
}
