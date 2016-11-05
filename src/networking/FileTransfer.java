package networking;

import java.util.ArrayList;

import org.hive2hive.processframework.ProcessComponent;
import org.hive2hive.processframework.composites.SyncProcess;
import org.hive2hive.processframework.interfaces.IProcessComponent;

import centralprocessor.PERSYSTSession;

public class FileTransfer {
	public ArrayList<IProcessComponent> processes;
	public ArrayList<String> files;
	
	public FileTransfer(){
		this.processes = new ArrayList<IProcessComponent>();
		this.files = new ArrayList<String>();
		
	}
	
	public void addProcess(IProcessComponent nprocess, String filename){
		this.processes.add(nprocess);
		this.files.add(filename);
		PERSYSTSession.comm.pgui.downView.getList().add(Integer.toString(this.processes.size()-1));
	}
	
	public ArrayList<Double> getProgress(){
		ArrayList<Double> prog = new ArrayList<Double>();
		
		for(int i = 0; i < this.processes.size(); i++)
			prog.add(new Double(this.processes.get(i).getProgress()));
		
		return prog;
	}
	
	public void removeComplete(){
		for(int i = 0; i < this.processes.size(); i++){;
			if(this.processes.get(i).getProgress() >= 100.0){
				this.processes.remove(i);
				this.files.remove(i);
				PERSYSTSession.comm.pgui.downView.getList().remove(i);
			}
		}
	}
	
	public void remove(int index){
		this.processes.remove(index);
		this.files.remove(index);
		PERSYSTSession.comm.pgui.downView.getList().remove(index);
	}
}
