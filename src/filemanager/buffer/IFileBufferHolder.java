package filemanager.buffer;

import java.io.File;
import java.util.List;
import java.util.Set;

import org.hive2hive.core.processes.files.list.FileNode;

/**
* Code sourced from Hive2Hive client on GitHub. 
* Available at https://github.com/Hive2Hive/Hive2Hive/tree/master/org.hive2hive.client/src/main/java/org/hive2hive/client/util
* Slightly modified as necessary to meet the requirements.
* 
* @author Hive2Hive Team
*/
public interface IFileBufferHolder {

	/**
	 * Get the flat set of files which are in sync with the DHT (use it to filter your files in the buffer)
	 */
	public Set<FileNode> getSyncFiles();

	/**
	 * Get the list of files in the buffer
	 */
	public List<File> getFileBuffer();
}
