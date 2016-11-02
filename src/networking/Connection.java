package networking;

import org.hive2hive.core.api.H2HNode;
import org.hive2hive.core.api.configs.FileConfiguration;
import org.hive2hive.core.api.configs.NetworkConfiguration;
import org.hive2hive.core.api.interfaces.IFileConfiguration;
import org.hive2hive.core.api.interfaces.IH2HNode;
import org.hive2hive.core.security.H2HDefaultEncryption;
import org.hive2hive.core.serializer.IH2HSerialize;
import org.hive2hive.core.serializer.FSTSerializer;

import filemanager.FileEventListener;

import java.util.ArrayList;
import java.util.Enumeration;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * Implementation of communications interface
 * 
 * @author Andrew, Jonathan Song
 *
 */
public class Connection {
	public String localip;
	public ArrayList<String> lanscan;
	public int threadsfinished;
	public IH2HNode node;
	
	private Object mon;
	private ArrayList<Thread> scanthreads;
	private NetworkConfiguration netconfig;
	
	public Connection(IFileConfiguration fConfig, NetworkConfiguration nconfig){
		this.node = H2HNode.createNode(fConfig);
		this.localip = "";
		this.lanscan = new ArrayList<String>();
		this.threadsfinished = 0;
		this.mon = new Object();
		this.scanthreads = new ArrayList<Thread>();
		this.netconfig = nconfig;
	}
	
	//store copy of NetworkConfig for use
	public void setNetConfig(NetworkConfiguration nconfig){
		this.netconfig = nconfig;
	}
	
	//for taskscheduler buildnode first then connect
	public void buildNode(IFileConfiguration fileConfig) {
		IH2HSerialize serializer = new FSTSerializer();	//default encryption

		node = H2HNode.createNode(fileConfig, new H2HDefaultEncryption(serializer), serializer);
	}
	
	//connect via ip given return true if connection success
	//if initial pass in NetworkConfiguration.createInitial(nodeid) created config
	public boolean Connect(NetworkConfiguration nconfig){
		if (this.node.connect(nconfig)) {

			// connect the event bus
			this.node.getFileManager().subscribeFileEvents(new FileEventListener(node.getFileManager()));

			return true;
		} else {
			return false;
		}
	}
	
	//disconnect node
	public boolean Disconnect(){
		return this.node.disconnect();
	}
	
	//disconnect node while keeping session
	public boolean DisconnectKS(){
		return this.node.disconnectKeepSession();
	}
	
	//returns first LP peer it finds, requires network config with connection settings except ip
	public ArrayList<String> GetLanPeers(){
		
		ArrayList<String> tlanscan = this.ScanLAN(this);
		if(tlanscan.isEmpty() || this.node.isConnected()) return null;
		
		ArrayList<String> peerips = new ArrayList<String>();
		for (int i = 0; i < tlanscan.size(); i++){
			try {
				this.netconfig.setBootstrap(InetAddress.getByName(tlanscan.get(i)));
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(this.node.connect(this.netconfig)){
				peerips.add(tlanscan.get(i));
				this.node.disconnect();
			}
		}
		
		return peerips;
	}
	
	//scan lan for reachable connections
	private ArrayList<String> ScanLAN(Connection cnode){
		final int divider = 50;
		for (int i = 0; i <= divider; i++){
			final int start = i*(255/divider);
			final int stop = (i+1)*(255/divider);
			cnode.lanscan.clear();
			scanthreads.add(new Thread() {
			    public void run() {	    	
					try {
						cnode.GetReachableHosts(InetAddress.getByName(cnode.GetLocalIP()), start, stop, divider);
					} catch (SocketException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    }  
			});
//			System.out.println("start: " + start + " stop: " + stop);
			scanthreads.get(i).start();
		}
		
		cnode.waitscan();
		return cnode.lanscan;
	}
	
	public void waitscan(){
		synchronized (mon){
			try {
				mon.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
    private void GetReachableHosts(InetAddress inetAddress, int start, int stop, int divider) throws SocketException {
        if(start < 0) return;
    	String ipAddress = inetAddress.toString();
        ipAddress = ipAddress.substring(1, ipAddress.lastIndexOf('.')) + ".";
        for (int i = start; i < stop && i < 256; i++) {
            String otherAddress = ipAddress + String.valueOf(i);
//            System.out.println("try: " + i);
            try {
                if (InetAddress.getByName(otherAddress.toString()).isReachable(100)) {
                    System.out.println(otherAddress);
                    this.lanscan.add(otherAddress.toString());
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        this.threadsfinished++;
        if(threadsfinished >= divider){
        	synchronized(mon){
        		mon.notify();
        	}
        }
    }
	//external ip
    public static String getIp() throws Exception {
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            String ip = in.readLine();
            return ip;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //local ip
	public String GetLocalIP(){
		try {
//			System.out.println("External IP: " + getIp());
			ArrayList<String> lAddList = getLocalIp();
			lAddList.forEach((tadd) -> {
				if(tadd.indexOf('.') >= 0){
					System.out.println("Local IP: " + tadd);
					this.localip = tadd;
				}
			});
			
//			ShareExample.runShareTest();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return this.localip;
	}
	//returns list of local IPs
    private static ArrayList<String> getLocalIp() throws Exception {
    	try {
    	    Enumeration<NetworkInterface> nics = NetworkInterface
    	            .getNetworkInterfaces();
	        ArrayList<String> addresses = new ArrayList<String>();
    	    
    	    while (nics.hasMoreElements()) {
    	        NetworkInterface nic = nics.nextElement();
    	        if (!nic.isLoopback()) {
    	        	Enumeration<InetAddress> addrs = nic.getInetAddresses();
    	            while (addrs.hasMoreElements()) {
    	                InetAddress addr = addrs.nextElement();
    	                //System.out.printf("%s %s%n", nic.getName(),
    	                //        addr.getHostAddress());
    	                addresses.add(addr.getHostAddress());
    	            }
    	        }
    	    }
    	    return addresses;
    	} catch (SocketException e) {
    	    e.printStackTrace();
    	} 
    	return null;
    }
}
