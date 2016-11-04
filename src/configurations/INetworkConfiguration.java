package configurations;

import java.net.InetAddress;

import net.tomp2p.p2p.Peer;

import org.hive2hive.core.H2HConstants;

/**
 * Configuration of the network settings for a Hive2Hive node.
 * 
 * @author Christian, Nico, Seppi
 */
public interface INetworkConfiguration {

	/**
	 * Each node has a unique ID corresponding to the ID in the DHT.
	 * 
	 * @return the ID of this node.
	 */
	String getNodeID();

	/**
	 * The port this peer binds. It both binds UDP and TCP to the same port. If the port is smaller than 0,
	 * the port will automatically be assigned (starting from {@link H2HConstants#H2H_PORT}).
	 * 
	 * @return the port or a negative value to automatically detect the port
	 */
	int getPort();

	/**
	 * Returns whether this peer is initial. When a peer is initial, it is the first one in the network and
	 * does not try to bootstrap anywhere. In each network, only one initial peer needs to exist. When other
	 * peers have joined, the initial peer can also go offline.
	 * 
	 * @return true when this peer is initial.
	 */
	boolean isInitial();

	/**
	 * If this peer is not initial, it needs to bootstrap anywhere to connect to the p2p network.
	 * 
	 * @return the internet address to bootstrap to. Make sure the given address is reachable (firewalls).
	 */
	InetAddress getBootstrapAddress();

	/**
	 * Returns whether this peer running locally or not.
	 * 
	 * @return <code>true</code> when peer is locally running
	 */
	boolean isLocal();

	/**
	 * If this peer is bootstrapping to a local peer, it needs a peer reference.
	 * 
	 * @return the local peer to bootstrap to.
	 */
	Peer getBootstapPeer();

	/**
	 * The port to bootstrap to. This depends on the configuration of the network and possibly of
	 * port-forwarding. The default port of Hive2Hive is {@link H2HConstants#H2H_PORT}. This port is
	 * automatically increased in case it's already used; thus check with your peer to bootstrap to.
	 * 
	 * @return the port of the peer this node bootstraps to.
	 */
	int getBootstrapPort();

	/**
	 * Indicates that the peer is behind a NAT / firewall. If no port forwarding or other NAT traversal
	 * techniques are used, try it with {@link #tryUPnP()}.
	 * 
	 * @return <code>true</code> if the peer is firewalled, otherwise <code>false</code>
	 */
	boolean isFirewalled();

	/**
	 * Try to set up UPnP. Note that this may
	 * not work and depends on the router and its configuration.
	 * 
	 * @return <code>true</code> if the peer should try to use UPnP for port-forwarding.
	 */
	boolean tryUPnP();
}
