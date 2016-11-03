package configurations;

import java.io.Serializable;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.UUID;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

import net.tomp2p.p2p.Peer;

import configurations.H2HConstants;
import configurations.IFileConfiguration;
import configurations.INetworkConfiguration;


	/**
	 * A file configuration such that the peers know how to handle file uploads, chunking and cleanups. This
	 * configuration must be constant for all peers in the DHT.
	 * 
	 * @author Nico
	 * 
	 */
public class NetworkConfiguration implements INetworkConfiguration, Serializable {
	

	public static final int AUTO_PORT = -1;

	public String nodeID = UUID.randomUUID().toString();
	public int port = AUTO_PORT;
	public InetAddress bootstrapAddress = null;
	public boolean isLocal = false;
	public Peer bootstrapPeer = null;
	public int bootstrapPort = H2HConstants.H2H_PORT;
	public boolean isFirewalled = false;
	public boolean tryUpnp = false;

	/**
	 * @param nodeID defines the location of the peer in the DHT. Should not be null
	 * @return this instance
	 */
	public NetworkConfiguration setNodeId(String nodeID) {
		this.nodeID = nodeID;
		return this;
	}

	/**
	 * @param port defines the port to bind. Should be free or negative (autodetect)
	 * @return this instance
	 */
	public NetworkConfiguration setPort(int port) {
		this.port = port;
		return this;
	}

	/**
	 * @param bootstrapAddress the address to bootstrap to. If it is <code>null</code>, the peer is 'initial'
	 *            and does not bootstrap to any other peer.
	 * @return this instance
	 */
	public NetworkConfiguration setBootstrap(InetAddress bootstrapAddress) {
		return setBootstrap(bootstrapAddress, H2HConstants.H2H_PORT);
	}

	/**
	 * @param bootstrapPort the port to bootstrap to
	 * @return this instance
	 */
	public NetworkConfiguration setBootstrapPort(int bootstrapPort) {
		this.bootstrapPort = bootstrapPort;
		return this;
	}

	/**
	 * 
	 * @param bootstrapAddress the address to bootstrap to. If it is <code>null</code>, the peer is 'initial'
	 *            and does not bootstrap to any other peer.
	 * @param bootstrapPort the port to bootstrap to
	 * @return this instance
	 */
	public NetworkConfiguration setBootstrap(InetAddress bootstrapAddress, int bootstrapPort) {
		this.bootstrapAddress = bootstrapAddress;
		this.bootstrapPort = bootstrapPort;
		return this;
	}

	/**
	 * @param bootstrapPeer the initial local peer to bootstrap to. Note: this is just for testings
	 * @return this instance
	 */
	public NetworkConfiguration setBootstrapLocal(Peer bootstrapPeer) {
		this.bootstrapPeer = bootstrapPeer;
		return setLocal();
	}

	/**
	 * Set the peer to only connect locally
	 * 
	 * @return this instance
	 */
	public NetworkConfiguration setLocal() {
		this.isLocal = true;
		return this;
	}

	/**
	 * Set whether this peer is firewalled (or behind a NAT) or not.
	 * 
	 * @return this instance
	 */
	public NetworkConfiguration setFirewalled(boolean isFirewalled) {
		this.isFirewalled = isFirewalled;
		return this;
	}

	/**
	 * If this peer is {@link #isFirewalled}, you could use UPnP to configure the port mapping at the NAT
	 * device.
	 * 
	 * @return this instance
	 */
	public NetworkConfiguration tryUPnP(boolean tryUpnp) {
		this.tryUpnp = tryUpnp;
		return this;
	}

	/**
	 * Create network configuration for initial peer with random node id
	 * 
	 * @return the network configuration
	 */
	public static NetworkConfiguration createInitial() {
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
	        System.out.printf("Display name: %s\n", netint.getDisplayName());
	        System.out.printf("Name: %s\n", netint.getName());
	        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
	        for (InetAddress inetAddress : Collections.list(inetAddresses)) {
	        	System.out.printf("InetAddress: %s\n", inetAddress);
	        }
	        System.out.printf("\n");
		
	    }
	
	@Override
	public String getNodeID() {
		return nodeID;
	}

	@Override
	public boolean isInitial() {
		return bootstrapAddress == null;
	}

	@Override
	public InetAddress getBootstrapAddress() {
		return bootstrapAddress;
	}

	@Override
	public Peer getBootstapPeer() {
		return bootstrapPeer;
	}

	@Override
	public int getBootstrapPort() {
		return bootstrapPort;
	}

	@Override
	public boolean isLocal() {
		return isLocal;
	}

	@Override
	public int getPort() {
		return port;
	}

	@Override
	public boolean isFirewalled() {
		return isFirewalled;
	}

	@Override
	public boolean tryUPnP() {
		return tryUpnp;
	}
	
}


   class FileConfiguration implements IFileConfiguration {

	private final BigInteger maxFileSize;
	private final int maxNumOfVersions;
	private final BigInteger maxSizeOfAllVersions;
	private final int chunkSize;

	private FileConfiguration(BigInteger maxFileSize, int maxNumOfVersions, BigInteger maxSizeAllVersions, int chunkSize) {
		assert maxFileSize.signum() == 1;
		assert maxNumOfVersions > 0;
		assert maxSizeAllVersions.signum() == 1;
		assert chunkSize > 0;

		this.maxFileSize = maxFileSize;
		this.maxNumOfVersions = maxNumOfVersions;
		this.maxSizeOfAllVersions = maxSizeAllVersions;
		this.chunkSize = chunkSize;
	}

	/**
	 * Creates a default file configuration
	 * 
	 * @return the file configuration
	 */
	public static IFileConfiguration createDefault() {
		return new FileConfiguration(H2HConstants.DEFAULT_MAX_FILE_SIZE, H2HConstants.DEFAULT_MAX_NUM_OF_VERSIONS,
				H2HConstants.DEFAULT_MAX_SIZE_OF_ALL_VERSIONS, H2HConstants.DEFAULT_CHUNK_SIZE);
	}

	/**
	 * Create a file configuration with the given parameters
	 * 
	 * @param maxFileSize the maximum file size (in bytes)
	 * @param maxNumOfVersions the allowed number of versions
	 * @param maxSizeAllVersions the maximum file size when summing up all versions (in bytes)
	 * @param chunkSize the size of a chunk (in bytes)
	 */
	public static IFileConfiguration createCustom(BigInteger maxFileSize, int maxNumOfVersions,
			BigInteger maxSizeAllVersions, int chunkSize) {
		return new FileConfiguration(maxFileSize, maxNumOfVersions, maxSizeAllVersions, chunkSize);
	}

	@Override
	public BigInteger getMaxFileSize() {
		return maxFileSize;
	}

	@Override
	public int getMaxNumOfVersions() {
		return maxNumOfVersions;
	}

	@Override
	public BigInteger getMaxSizeAllVersions() {
		return maxSizeOfAllVersions;
	}

	@Override
	public int getChunkSize() {
		return chunkSize;
	}
}




