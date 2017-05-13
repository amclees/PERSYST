package encryption;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.IOUtils;
import org.hive2hive.core.security.EncryptionUtil;

/**
 * This class tests the methods for encryption from EncryptConfig to ensure that
 * they work. It uses simple console output to show this. It also tests using a
 * file for this.
 * 
 * @author Andrew
 */
public class EncryptionTest {
  public static void main(String[] args) throws IOException {
    System.out.println("Begin test with no files.");
    noFileTest();
    System.out.println("End test with no files.");

    System.out.printf("%n%nBegin file based test:%n");
    fileTest();
  }

  public static void fileTest() throws IOException {
    String toEncrypt = "This is a test string to be encypted.";
    String password = "pass1";
    String pin = "pin1";

    byte[] toEncryptBytes = toEncrypt.getBytes();
    byte[] initVector = EncryptionUtil.generateIV();
    byte[] encrypted = PersystEncryption.encryptAES(toEncryptBytes, initVector, password, pin);
    ByteBuffer intToByte = ByteBuffer.allocate(4);
    intToByte.putInt(initVector.length);
    byte[] initVectorLength = intToByte.array();

    File file = new File("encrypted");
    OutputStream out = new FileOutputStream(file);
    out.write(initVectorLength);
    out.write(initVector);
    out.write(encrypted);
    out.close();

    InputStream in = new FileInputStream(file);
    byte[] fileBytes = IOUtils.toByteArray(in);
    int initVectorLengthRead = ByteBuffer.wrap(Arrays.copyOfRange(fileBytes, 0, 4)).getInt();
    byte[] initVectorRead = Arrays.copyOfRange(fileBytes, 4, 4 + initVectorLengthRead);
    byte[] encryptedRead = Arrays.copyOfRange(fileBytes, 4 + initVectorLengthRead, fileBytes.length);
    byte[] decrypted = PersystEncryption.decryptAES(encryptedRead, initVectorRead, password, pin);
    System.out.println("Decrypted is " + new String(decrypted, "UTF-8"));
  }

  public static void noFileTest() throws UnsupportedEncodingException {
    String toEncrypt = "This is a test string to be encypted.";
    String password = "pass1";
    String pin = "pin1";

    byte[] toEncryptBytes = toEncrypt.getBytes();
    System.out.println("Encrypting/decrypting: " + toEncrypt + " bytes are ");
    System.out.println(DatatypeConverter.printBase64Binary(toEncryptBytes));
    byte[] initVector = EncryptionUtil.generateIV();
    System.out.println("Init vector has " + initVector.length + " bytes");

    byte[] encrypted = PersystEncryption.encryptAES(toEncryptBytes, initVector, password, pin);

    String encryptedOut = DatatypeConverter.printBase64Binary(encrypted);
    System.out.println("Encrypted bytes are ");
    System.out.println(encryptedOut);

    byte[] decryptedBytes = PersystEncryption.decryptAES(encrypted, initVector, password, pin);

    String decrypted = new String(decryptedBytes, "UTF-8");
    System.out.println("Final result: " + decrypted);
    System.out.println("Bytes are ");
    System.out.println(DatatypeConverter.printBase64Binary(decryptedBytes));
  }
}
