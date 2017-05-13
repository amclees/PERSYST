package configurations;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;

import org.hive2hive.core.H2HConstants;
import org.hive2hive.core.api.configs.FileConfiguration;
import org.hive2hive.core.api.interfaces.IFileConfiguration;

/**
 * 
 * Provides an interface to the H2H configurations that the user needs to change
 * 
 * @author xv435, Wilson
 *
 */
public class PersystConfiguration implements Serializable {
  private static final long serialVersionUID = 7165537949083966221L;

  private transient IFileConfiguration fileConfig;
  // public File rootFolder;

  public PersystConfiguration(IFileConfiguration fileConfig) {
    this.fileConfig = fileConfig;
    // this.rootFolder = PERSYSTSession.rootFolder;
  }

  public FileConfiguration getFileConfig() {
    return (FileConfiguration) this.fileConfig;
  }

  private void writeObject(java.io.ObjectOutputStream out) throws IOException {
    // note, here we don't need out.defaultWriteObject(); because
    // MyClass has no other state to serialize
    out.defaultWriteObject();
    out.writeLong(fileConfig.getMaxFileSize().longValueExact());
  }

  private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
    // note, here we don't need in.defaultReadObject();
    // because MyClass has no other state to deserialize
    in.defaultReadObject();
    BigInteger bigint = BigInteger.valueOf(in.readLong());
    this.fileConfig = FileConfiguration.createCustom(bigint, H2HConstants.DEFAULT_MAX_NUM_OF_VERSIONS,
        H2HConstants.DEFAULT_MAX_SIZE_OF_ALL_VERSIONS, H2HConstants.DEFAULT_CHUNK_SIZE);
  }

  // Networking Configuration
  /*
   * private static final int AUTO_PORT = -1;
   * 
   * private String nodeID = UUID.randomUUID().toString(); private int port =
   * AUTO_PORT; private InetAddress bootstrapAddress = null; private boolean
   * isLocal = false; private Peer bootstrapPeer = null; private int
   * bootstrapPort = H2HConstants.H2H_PORT; private boolean isFirewalled =
   * false; private boolean tryUpnp = false;
   */
  // End Networking Configuration

  // File Configuration
  // public final int maxNumOfVersions;
  // public final BigInteger maxSizeOfAllVersions;
  // public final int chunkSize;
  // End

}