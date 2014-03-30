package spinnytea.tools;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EncryptionUtils
{
	private static final Logger logger = LoggerFactory.getLogger(EncryptionUtils.class);
	private static final int SALT = 128;

	public static byte[] generateKey(String password)
	throws Exception
	{
		byte[] keyStart = password.getBytes("UTF-16");
		KeyGenerator generator = KeyGenerator.getInstance("AES");
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		sr.setSeed(keyStart);
		generator.init(SALT, sr);
		SecretKey secretKey = generator.generateKey();
		return secretKey.getEncoded();
	}

	/** when you want to encode a string, make sure you call <code>String.getBytes(Charset.forName("UTF-16"))</code> */
	public static byte[] encode(byte[] key, byte[] plaintext)
	throws Exception
	{
		Key keySpec = new SecretKeySpec(key, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, keySpec);

		return cipher.doFinal(plaintext);
	}

	public static byte[] decode(byte[] key, byte[] ciphertext)
	throws Exception
	{
		Key keySpec = new SecretKeySpec(key, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, keySpec);

		return cipher.doFinal(ciphertext);
	}

	/** save the data into the file */
	public static boolean EncryptFile(File file, String password, String data)
	{
		BufferedOutputStream bos = null;
		try
		{
			bos = new BufferedOutputStream(new FileOutputStream(file));

			byte[] yourKey = generateKey(password);
			byte[] filesBytes = encode(yourKey, data.getBytes(Charset.forName("UTF-16")));

			bos.write(filesBytes);

			bos.flush();
			bos.close();
			return true;
		}
		catch(Exception e)
		{
			logger.error("Failed to save file.", e);
			file.delete();
			return false;
		}
		finally
		{
			if(bos != null)
				try
				{
					bos.close();
				}
				catch(Exception ignored)
				{

				}
		}
	}

	public static String DecryptFile(File file, String password)
	{
		RandomAccessFile raf = null;

		try
		{
			raf = new RandomAccessFile(file, "r");

			long length = file.length();
			if(length > Integer.MAX_VALUE)
				throw new IOException("File too long.");
			byte[] data = new byte[(int) length];
			raf.readFully(data);

			byte[] yourKey = generateKey(password);
			byte[] decodedData = decode(yourKey, data);

			return new String(decodedData, Charset.forName("UTF-16"));
		}
		catch(Exception e)
		{
			logger.error("Failed to load file.", e);
			return null;
		}
		finally
		{
			if(raf != null)
				try
				{
					raf.close();
				}
				catch(Exception ignored)
				{

				}
		}
	}
}
