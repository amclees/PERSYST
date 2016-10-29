package encryption;

import java.security.GeneralSecurityException;

import javax.crypto.SecretKey;

import org.hive2hive.core.H2HConstants;
import org.hive2hive.core.security.BCSecurityClassProvider;
import org.hive2hive.core.security.BCStrongAESEncryption;
import org.hive2hive.core.security.EncryptionUtil;
import org.hive2hive.core.security.EncryptionUtil.AES_KEYLENGTH;
import org.hive2hive.core.security.IStrongAESEncryption;
import org.hive2hive.core.security.PasswordUtil;

/**
 * This class provides an interface for the encryption and decryption of the
 * configurations/user profile file. It uses the H2H EncryptionUtil to 
 * ensure consistency in encryption throughout PERSYST.
 * 
 * It only supports AES
 * Note that the initialization vector does NOT need to be kept secret.
 * 
 * @author Andrew
 */
public final class PersystEncryption {
	private PersystEncryption() {}
	
	/**
	 * Decrypts data using the user's password and pin.
	 * Returns empty byte[] for incorrect password/pin combination.
	 * 
	 * @param data A byte array of unencrypted data to be encrypted
	 * @param initVector A vector used to begin encryption.
	 * @param password The user's password
	 * @param pin The user's pin
	 */
	public static byte[] encryptAES(byte[] data, byte[] initVector, String password, String pin) {
		AES_KEYLENGTH keyLength = H2HConstants.KEYLENGTH_USER_PROFILE;
		SecretKey key = PasswordUtil.generateAESKeyFromPassword(password, pin, keyLength);
		return encryptAES(data, initVector, key);
	}
	
	/**
	 * Decrypts data using the user's password and pin.
	 * Returns empty byte[] for incorrect password/pin combination.
	 * 
	 * @param data A byte array of encrypted data to be decrypted
	 * @param initVector A vector used to begin decryption.
	 * @param password The user's password
	 * @param pin The user's pin
	 */
	public static byte[] decryptAES(byte[] data, byte[] initVector, String password, String pin) {
		AES_KEYLENGTH keyLength = H2HConstants.KEYLENGTH_USER_PROFILE;
		SecretKey key = PasswordUtil.generateAESKeyFromPassword(password, pin, keyLength);
		return decryptAES(data, initVector, key);
	}
	
	/**
	 * Encrypts data using the specified key
	 * Be sure to store the initVector in unencrypted form when calling this.
	 * 
	 * @param data A byte array of unencrypted data to be encrypted
	 * @param initVector A vector used to begin encryption.
	 * @param key A Java Crypto SecretKey
	 */
	public static byte[] encryptAES(byte[] data, byte[] initVector, SecretKey key) {
		String securityProvider = new BCSecurityClassProvider().getSecurityProvider();
		IStrongAESEncryption strongAES = new BCStrongAESEncryption();
		
		byte[] toReturn;
		try {
			toReturn = EncryptionUtil.encryptAES(data, key, initVector, securityProvider, strongAES);
		} catch (GeneralSecurityException e) {
			toReturn = new byte[0];
		}
		return toReturn;
	}
	
	/**
	 * Decrypts data using the specified key
	 * Returns empty byte[] for incorrect password/pin combination.
	 * 
	 * @param data A byte array of encrypted data to be decrypted
	 * @param initVector A vector used to begin decryption.
	 * @param key A Java Crypto SecretKey
	 */
	public static byte[] decryptAES(byte[] data, byte[] initVector, SecretKey key) {
		String securityProvider = new BCSecurityClassProvider().getSecurityProvider();
		IStrongAESEncryption strongAES = new BCStrongAESEncryption();
		
		byte[] toReturn;
		try {
			toReturn = EncryptionUtil.decryptAES(data, key, initVector, securityProvider, strongAES);
		} catch (GeneralSecurityException e) {
			toReturn = new byte[0];
		}
		return toReturn;
	}
}
