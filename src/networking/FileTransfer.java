package networking;

import java.util.ArrayList;
import org.hive2hive.processframework.interfaces.IProcessComponent;

public class FileTransfer {
	ArrayList<IProcessComponent> processes;
	
	public FileTransfer(){
		this.processes = new ArrayList<IProcessComponent>();
	}
	
	public void addProcess(IProcessComponent nprocess){
		this.processes.add(nprocess);
	}
	
	public ArrayList<Double> getProgress(){
		ArrayList<Double> prog = new ArrayList<Double>();
		
		for(int i = 0; i < this.processes.size(); i++)
			prog.add(new Double(this.processes.get(i).getProgress()));
		
		return prog;
	}
	
	public void removeComplete(){
		for(int i = 0; i < this.processes.size(); i++){
			if(this.processes.get(i).getProgress() >= 100.0)
				this.processes.remove(i);
		}
	}
}
