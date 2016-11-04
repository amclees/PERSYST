package configurations;

import configuration.tomp2p.dht.PeerDHT;

public interface IPeerHolder {

	PeerDHT getPeer();
}
