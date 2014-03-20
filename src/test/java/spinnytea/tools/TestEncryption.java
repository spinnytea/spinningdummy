package spinnytea.tools;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.util.Arrays;

import org.junit.Test;

public class TestEncryption
{
	@Test
	public void _byte()
	throws Exception
	{
		String password = "password";
		byte[] plaintext = new byte[10];
		for(byte b = 0; b < plaintext.length; b++)
			plaintext[b] = b;

		byte[] key = EncryptionUtils.generateKey(password);
		byte[] ciphertext = EncryptionUtils.encode(key, plaintext);

		assertFalse(Arrays.equals(plaintext, ciphertext));

		byte[] decrypted = EncryptionUtils.decode(key, ciphertext);

		assertArrayEquals(plaintext, decrypted);
	}

	@Test
	public void _file()
	{
		File file = new File("exports/test.enc");
		try
		{
			String password = "password";
			String data = "Hello World!";

			EncryptionUtils.EncryptFile(file, password, data);
			// TEST file contents
			String decrypt = EncryptionUtils.DecryptFile(file, password);

			assertEquals(data, decrypt);
		}
		finally
		{
			file.delete();
		}
	}
}
