package filemanager;

import java.io.File;
import java.io.IOException;

import net.engio.mbassy.listener.Handler;
import net.engio.mbassy.listener.Listener;
import net.engio.mbassy.listener.References;

import org.apache.commons.io.FileUtils;
import org.hive2hive.core.api.interfaces.IFileManager;
import org.hive2hive.core.events.framework.interfaces.IFileEventListener;
import org.hive2hive.core.events.framework.interfaces.file.IFileAddEvent;
import org.hive2hive.core.events.framework.interfaces.file.IFileDeleteEvent;
import org.hive2hive.core.events.framework.interfaces.file.IFileMoveEvent;
import org.hive2hive.core.events.framework.interfaces.file.IFileShareEvent;
import org.hive2hive.core.events.framework.interfaces.file.IFileUpdateEvent;
import org.hive2hive.core.exceptions.NoPeerConnectionException;
import org.hive2hive.core.exceptions.NoSessionException;
import org.hive2hive.processframework.exceptions.InvalidProcessStateException;
import org.hive2hive.processframework.exceptions.ProcessExecutionException;
import org.hive2hive.processframework.interfaces.IProcessComponent;

import centralprocessor.PERSYSTSession;

@Listener(references = References.Strong)
/**
 * Code sourced from Hive2Hive client on GitHub. 
 * Available at https://github.com/Hive2Hive/Hive2Hive/tree/master/org.hive2hive.client/src/main/java/org/hive2hive/client/util
 * Slightly modified as necessary to meet the requirements.
 * 
 * Note that the file listener will call the H2H processes itself.
 * 
 * @author Hive2Hive team
 */
public class FileEventListener implements IFileEventListener {

	private final IFileManager fileManager;

	public FileEventListener(IFileManager fileManager) {
		this.fileManager = fileManager;
	}
	
	/**
	 * This method provides a single point to change PERSYST handling of file updates.
	 * If sending them through the Central Processor module is necessary, this is where to change them.
	 * There are no other spots that would make sense to change.
	 * 
	 * @param file The file to create the sync (download) process for
	 * 
	 * @author Andrew
	 */
	private void downloadProcessPersystWrapper(File file) throws InvalidProcessStateException, ProcessExecutionException, NoSessionException, NoPeerConnectionException{
		IProcessComponent<Void> downloadProcess = fileManager.createDownloadProcess(file);
		PERSYSTSession.comm.ftrans.addProcess(downloadProcess, file.getName());
		downloadProcess.execute();
	}

	@Override
	@Handler
	public void onFileAdd(IFileAddEvent fileEvent) {
		try {
			this.downloadProcessPersystWrapper(fileEvent.getFile());
		} catch (InvalidProcessStateException | ProcessExecutionException | NoSessionException | NoPeerConnectionException e) {
			System.err.println("Cannot download the new file " + fileEvent.getFile());
		}
	}

	@Override
	@Handler
	public void onFileUpdate(IFileUpdateEvent fileEvent) {
		try {
			this.downloadProcessPersystWrapper(fileEvent.getFile());
		} catch (InvalidProcessStateException | ProcessExecutionException | NoSessionException | NoPeerConnectionException e) {
			System.err.println("Cannot download the updated file " + fileEvent.getFile());
		}
	}

	@Override
	@Handler
	public void onFileDelete(IFileDeleteEvent fileEvent) {
		if (fileEvent.getFile().delete()) {
			System.out.println("Deleted file " + fileEvent.getFile());
		} else {
			System.err.println("Could not delete file " + fileEvent.getFile());
		}
	}

	@Override
	@Handler
	public void onFileMove(IFileMoveEvent fileEvent) {
		try {
			if (fileEvent.isFile()) {
				FileUtils.moveFile(fileEvent.getSrcFile(), fileEvent.getDstFile());
			} else {
				FileUtils.moveDirectory(fileEvent.getSrcFile(), fileEvent.getDstFile());
			}
		} catch (IOException e) {
			System.err.println("Cannot move the file / folder " + fileEvent.getFile());
		}
	}

	@Override
	@Handler
	public void onFileShare(IFileShareEvent fileEvent) {
		// ignore because it will trigger onFileAdd for every file anyhow
	}

}
