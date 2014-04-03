package spinnytea.tools;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.SealedObject;

import org.apache.commons.io.FileUtils;
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
	throws IOException
	{
		File file = new File("exports/test.enc");
		try
		{
			String password = "password";
			String data = "Hello World!";

			EncryptionUtils.EncryptFile(file, password, data);
			assertEquals("茄蒲꠩躭ꎷ羷蘓劼诺몰ᢓᄾ拁祌", FileUtils.readFileToString(file, "UTF-16"));
			String decrypt = EncryptionUtils.DecryptFile(file, password);

			assertEquals(data, decrypt);
		}
		finally
		{
			file.delete();
		}
	}

	@Test
	public void _object1()
	throws Exception
	{
		String password = "password";
		String data = "Hello World!";
		SealedObject sealed = EncryptionUtils.EncryptObject(data, password);
		assertNotNull(sealed);
		String decrypt = (String) EncryptionUtils.DecryptObject(sealed, password);
		assertEquals(data, decrypt);
	}

	@Test
	public void _object2()
	throws Exception
	{
		String password = "password";
		Serializable data = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
		SealedObject sealed = EncryptionUtils.EncryptObject(data, password);
		assertNotNull(sealed);
		Object decrypt = EncryptionUtils.DecryptObject(sealed, password);
		assertEquals(data, decrypt);
	}
}
