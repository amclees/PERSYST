package configurations;

import java.io.Serializable;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.UUID;

import org.hive2hive.core.H2HConstants;
import net.tomp2p.p2p.Peer;

public class PersystConfiguration implements Serializable {
	
		
	private static final int AUTO_PORT = -1;

	//Networking Configuration
	public String nodeID = UUID.randomUUID().toString();
	public int port = AUTO_PORT;
	public InetAddress bootstrapAddress = null;
	public boolean isLocal = false;
	public Peer bootstrapPeer = null;
	public int bootstrapPort = H2HConstants.H2H_PORT;
	public boolean isFirewalled = false;
	public boolean tryUpnp = false;
    //End	
	
	
	//File Configuration
	public final BigInteger maxFileSize;
	public final int maxNumOfVersions;
	public final BigInteger maxSizeOfAllVersions;
	public final int chunkSize;
    //End
	
	
	public PersystConfiguration(BigInteger maxFileSize, int maxNumOfVersions, BigInteger maxSizeAllVersions, int chunkSize) {
		assert maxFileSize.signum() == 1;
		assert maxNumOfVersions > 0;
		assert maxSizeAllVersions.signum() == 1;
		assert chunkSize > 0;

		this.maxFileSize = maxFileSize;
		this.maxNumOfVersions = maxNumOfVersions;
		this.maxSizeOfAllVersions = maxSizeAllVersions;
		this.chunkSize = chunkSize;
	}
	

	//Networking Configuration
	public void PerystConfiguration (String nodeID, int port, InetAddress bootstrapAddress, boolean isLocal, int bootstrapPort, Peer bootstrapPeer, boolean isFirewalled, boolean tryUpnp){
		this.nodeID = nodeID;
		this.port = port;
		this.bootstrapAddress = bootstrapAddress;
		this.bootstrapPort = bootstrapPort;
		this.isLocal = isLocal;
		this.bootstrapPeer = bootstrapPeer;
		this.bootstrapPort = bootstrapPort;
		this.isFirewalled = isFirewalled;
		this.tryUpnp = tryUpnp;
				
	}

	//File Configuration
	public static PersystConfiguration createDefault() {
		return new PersystConfiguration(H2HConstants.DEFAULT_MAX_FILE_SIZE, H2HConstants.DEFAULT_MAX_NUM_OF_VERSIONS,
				H2HConstants.DEFAULT_MAX_SIZE_OF_ALL_VERSIONS, H2HConstants.DEFAULT_CHUNK_SIZE);
	}
	
	public static PersystConfiguration createCustom(BigInteger maxFileSize, int maxNumOfVersions,
			BigInteger maxSizeAllVersions, int chunkSize) {
		return new PersystConfiguration(maxFileSize, maxNumOfVersions, maxSizeAllVersions, chunkSize);
	}
	//End
	
/**
	//NetworkConfiguration
	public NetworkConfiguration setBootstrap(InetAddress bootstrapAddress, int h2hPort) {
		return setBootstrap(bootstrapAddress, H2HConstants.H2H_PORT);
	}
	
	public static NetworkConfiguration createInitial() {
		return createInitial(UUID.randomUUID().toString());
	}
	
	public static NetworkConfiguration createInitial(String nodeID) {
		return new NetworkConfiguration().setNodeId(nodeID).setPort(AUTO_PORT);
	}
	
	public static NetworkConfiguration create(String string, InetAddress bootstrapAddress) {
		return create(UUID.randomUUID().toString(), bootstrapAddress);
	}
	
	public static NetworkConfiguration createLocalPeer(String nodeID, Peer initialPeer) {
		return new NetworkConfiguration().setNodeId(nodeID).setPort(AUTO_PORT).setBootstrapLocal(initialPeer);
	}
	
	public static NetworkConfiguration createInitialLocalPeer(String nodeID) {
		return new NetworkConfiguration().setNodeId(nodeID).setPort(AUTO_PORT).setLocal();
	}
	

	//Shows IP Addresses
	public static void main(String args[]) throws SocketException {
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netint : Collections.list(nets))
            displayInterfaceInformation(netint);
    }

    static void displayInterfaceInformation(NetworkInterface netint) throws SocketException {
        out.printf("Display name: %s\n", netint.getDisplayName());
        out.printf("Name: %s\n", netint.getName());
        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        for (InetAddress inetAddress : Collections.list(inetAddresses)) {
            out.printf("InetAddress: %s\n", inetAddress);
        }
        out.printf("\n");
	
	
	

	//Getters and Setters
	public String getNodeID() {
		return nodeID;
	}


	public void setNodeID(String nodeID) {
		this.nodeID = nodeID;
	}


	public int getPort() {
		return port;
	}


	public void setPort(int port) {
		this.port = port;
	}


	public InetAddress getBootstrapAddress() {
		return bootstrapAddress;
	}


	public void setBootstrapAddress(InetAddress bootstrapAddress) {
		this.bootstrapAddress = bootstrapAddress;
	}


	public boolean isLocal() {
		return isLocal;
	}


	public void setLocal(boolean isLocal) {
		this.isLocal = isLocal;
	}


	public Peer getBootstrapPeer() {
		return bootstrapPeer;
	}


	public void setBootstrapPeer(Peer bootstrapPeer) {
		this.bootstrapPeer = bootstrapPeer;
	}


	public int getBootstrapPort() {
		return bootstrapPort;
	}


	public void setBootstrapPort(int bootstrapPort) {
		this.bootstrapPort = bootstrapPort;
	}


	public boolean isFirewalled() {
		return isFirewalled;
	}


	public void setFirewalled(boolean isFirewalled) {
		this.isFirewalled = isFirewalled;
	}


	public boolean isTryUpnp() {
		return tryUpnp;
	}


	public void setTryUpnp(boolean tryUpnp) {
		this.tryUpnp = tryUpnp;
	}


	public static int getAutoPort() {
		return AUTO_PORT;
	}


	public BigInteger getMaxFileSize() {
		return maxFileSize;
	}


	public int getMaxNumOfVersions() {
		return maxNumOfVersions;
	}


	public BigInteger getMaxSizeOfAllVersions() {
		return maxSizeOfAllVersions;
	}


	public int getChunkSize() {
		return chunkSize;
	}
	
	
*/
}
