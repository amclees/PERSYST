package filemanager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * Code sourced from Hive2Hive client on GitHub. 
 * Available at https://github.com/Hive2Hive/Hive2Hive/tree/master/org.hive2hive.client/src/main/java/org/hive2hive/client/util
 * Slightly modified as necessary to meet the requirements.
 * 
 * Default implementation of a file observer. Internally uses the Apache Commons IO
 * {@link FileAlterationObserver} and {@link FileAlterationMonitor}.
 * 
 * @author Christian
 * 
 */
public class FileObserver {

	private static final Logger logger = LoggerFactory.getLogger(FileObserverListener.class);
	private final FileAlterationObserver observer;
	private final FileAlterationMonitor monitor;

	private boolean isRunning;

	/**
	 * A file observer that uses the specified interval to check for file changes.
	 * 
	 * @param rootDirectory
	 * @param ms
	 */
	public FileObserver(File rootDirectory, long ms) {
		this.observer = new FileAlterationObserver(rootDirectory);
		this.monitor = new FileAlterationMonitor(ms, observer);
	}

	/**
	 * A file observer that uses the default interval to check for file changes.
	 * 
	 * @param rootDirectory
	 */
	public FileObserver(File rootDirectory) {
		this(rootDirectory, 1000);
	}

	public void start() throws Exception {
		if (!isRunning) {
			monitor.start();
			isRunning = true;
		}
	}

	public void stop() throws Exception {
		stop(0);
	}

	public void stop(long ms) throws Exception {
		if (isRunning) {
			monitor.stop(ms);
			isRunning = false;
		}
	}

	public void addFileObserverListener(FileObserverListener listener) {
		observer.addListener(listener);
	}

	public void removeFileObserverListener(FileObserverListener listener) {
		observer.removeListener(listener);
	}

	public List<FileObserverListener> getFileObserverListeners() {
		List<FileObserverListener> listeners = new ArrayList<FileObserverListener>();

		// Added a check if this interface casting is allowed
		// @author Andrew
		for (FileAlterationListener listener : observer.getListeners()) {
			try {
				listeners.add((FileObserverListener) listener);
			} catch(ClassCastException e) {
				logger.debug("Using a FileAlterationListener that in not a FileObserverListener");
			}
		}
		return listeners;
	}

	public long getInterval() {
		return monitor.getInterval();
	}

	public boolean isRunning() {
		return isRunning;
	}

}
