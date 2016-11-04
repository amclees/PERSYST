package configurations;

import java.io.File;
import java.io.Serializable;

import org.hive2hive.core.api.configs.FileConfiguration;
import org.hive2hive.core.api.interfaces.IFileConfiguration;

import centralprocessor.PERSYSTSession;

/**
 * 
 * Provides an interface to the H2H configurations that the user needs to change
 * 
 * @author xv435, Wilson
 *
 */
public class PersystConfiguration implements Serializable {
	private static final long serialVersionUID = 7165537949083966221L;

	private IFileConfiguration fileConfig;
	public File rootFolder;

	public PersystConfiguration(IFileConfiguration fileConfig) {
		this.fileConfig = fileConfig;
		this.rootFolder = PERSYSTSession.rootFolder;
	}

	public FileConfiguration getFileConfig() {
		return (FileConfiguration) this.fileConfig;
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
