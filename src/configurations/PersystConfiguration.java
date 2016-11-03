package configurations;

import java.io.Serializable;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.UUID;

import org.hive2hive.core.H2HConstants;
import org.hive2hive.core.api.configs.FileConfiguration;
import org.hive2hive.core.api.configs.NetworkConfiguration;
import org.hive2hive.core.api.interfaces.PersystConfiguration;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

import net.tomp2p.p2p.Peer;



public class PersystConfiguration implements Serializable {
	
		
	
	//Networking Configuration
	private static final int AUTO_PORT = -1;

	private String nodeID = UUID.randomUUID().toString();
	private int port = AUTO_PORT;
	private InetAddress bootstrapAddress = null;
	private boolean isLocal = false;
	private Peer bootstrapPeer = null;
	private int bootstrapPort = H2HConstants.H2H_PORT;
	private boolean isFirewalled = false;
	private boolean tryUpnp = false;
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
	

	//NetworkConfiguration
	/**
	 * Create network configuration for initial peer with random node id
	 * 
	 * @return the network configuration
	 */
	public static PersystConfiguration createInitial() {
		return createInitial(UUID.randomUUID().toString());
	}

	/**
	 * Create network configuration for initial peer with given node id.
	 * 
	 * @param nodeID defines the location of the peer in the DHT
	 * @return the network configuration
	 */
	public static NetworkConfiguration createInitial(String nodeID) {
		return new NetworkConfiguration().setNodeId(nodeID).setPort(AUTO_PORT);
	}

	/**
	 * Create network configuration for 'normal' peer with random node id. The bootstrapping happens at the
	 * default port {@link H2HConstants#H2H_PORT}.
	 * 
	 * @param bootstrapAddress the address to bootstrap to. This can be address of the initial peer or any
	 *            other peer connected to the DHT.
	 * @return the network configuration
	 */
	public static NetworkConfiguration create(InetAddress bootstrapAddress) {
		return create(UUID.randomUUID().toString(), bootstrapAddress);
	}

	/**
	 * Create network configuration for 'normal' peer. The bootstrapping happens at the default port
	 * {@link H2HConstants#H2H_PORT}.
	 * 
	 * @param nodeID defines the location of the peer in the DHT. Should not be null
	 * @param bootstrapAddress the address to bootstrap to. This can be address of the initial peer or any
	 *            other peer connected to the DHT.
	 * @return the network configuration
	 */
	public static NetworkConfiguration create(String nodeID, InetAddress bootstrapAddress) {
		return new NetworkConfiguration().setNodeId(nodeID).setPort(AUTO_PORT)
				.setBootstrap(bootstrapAddress, H2HConstants.H2H_PORT);
	}

	/**
	 * Create network configuration for 'normal' peer. The bootstrapping happens to the specified address and
	 * port
	 * 
	 * @param nodeID defines the location of the peer in the DHT. Should not be null
	 * @param bootstrapAddress the address to bootstrap to. This can be address of the initial peer or any
	 *            other peer connected to the DHT.
	 * @param bootstrapPort the port to bootstrap
	 * @return the network configuration
	 */
	public static NetworkConfiguration create(String nodeID, InetAddress bootstrapAddress, int bootstrapPort) {
		return new NetworkConfiguration().setNodeId(nodeID).setPort(AUTO_PORT).setBootstrap(bootstrapAddress, bootstrapPort);
	}

	/**
	 * Creates a local peer that is only able to bootstrap to a peer running on the same host.
	 * 
	 * @param nodeID the id of the peer to create
	 * @param initialPeer the peer to bootstrap to
	 * @return the network configuration for local peers
	 */
	public static NetworkConfiguration createLocalPeer(String nodeID, Peer initialPeer) {
		return new NetworkConfiguration().setNodeId(nodeID).setPort(AUTO_PORT).setBootstrapLocal(initialPeer);
	}

	/**
	 * Create a local initial peer. Regard that bootstrapping may only work for peers running on the same
	 * host.
	 * 
	 * @param nodeID the id of the initial peer
	 * @return the network configuration for local peers (initial)
	 */
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
	
    }
	

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
	
	

}
