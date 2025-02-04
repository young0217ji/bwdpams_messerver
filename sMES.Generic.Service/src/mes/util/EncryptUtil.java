package mes.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import kr.co.mesframe.exception.MESFrameException;

/**
 * @author LSMESSolution
 * 
 * @since 2020.05.12 
 * 
 * @see
 */
public class EncryptUtil
{
	private static final String keyString = "onomatopoeia";
	private static final int bitKeyLen = 128;
	/**
	 * 
	 * 바이트수가 bitKeyLen(128)인 key를 생성합니다
	 *
	 * @param
	 * @return byte[]
	 *
	 */
	public static byte[] generateKey() throws Exception
	{		
		return generateKey(bitKeyLen);
	}
	/**
	 * 
	 * 바이트수가 bitKeyLen인 key를 생성합니다
	 *
	 * @param bitKeyLen
	 * @return byte[]
	 *
	 */
	public static byte[] generateKey(int bitKeyLen) throws Exception
	{
		int byteKeyLen = bitKeyLen/8;
		
	    byte[] passwordBytes = keyString.getBytes("UTF-8");
	    int len = passwordBytes.length;
	    byte[] keyBytes = new byte[16];
	    
	    if (len > byteKeyLen)
	    {
	        System.arraycopy(passwordBytes, 0, keyBytes, 0, byteKeyLen);
	    }
	    else
	    {
	        System.arraycopy(passwordBytes, 0, keyBytes, 0, len);
	    }
    	
        return keyBytes;
    }
	/**
	 * 
	 * encryptText를 암호화합니다
	 *
	 * @param encryptText
	 * @return String
	 * @throws NoSuchAlgorithmException, RuntimeException, MESFrameException
	 *
	 */
	public static String encrypt(String encryptText)
	{
		String resultEncryptText = null;
		
		try
		{
			byte[] keyBytes = generateKey();
			
		    String transformation = "AES/CBC/PKCS5Padding";
		    
		    Cipher cipher = Cipher.getInstance(transformation);
			
		    SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
		    
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

			byte[] plain = encryptText.getBytes("UTF-8");
	    	byte[] encryptResult = cipher.doFinal(plain);
	    	
	    	resultEncryptText = DatatypeConverter.printBase64Binary(encryptResult);
		}
		catch (NoSuchAlgorithmException nsae)
		{
			throw new RuntimeException("NoSuchAlgorithmException occurred", nsae);
		}
		catch (NoSuchPaddingException nspe)
		{
			throw new RuntimeException("NoSuchPaddingException occurred", nspe);
		}
		catch (InvalidKeyException ike)
		{
			throw new RuntimeException("InvalidKeyException occurred", ike);			
		}
		catch (IllegalBlockSizeException ibse)
		{
			throw new RuntimeException("IllegalBlockSizeException occurred", ibse);
		}
		catch (BadPaddingException bpe)
		{
			throw new RuntimeException("BadPaddingException occurred", bpe);
		}
		catch (Exception e)
		{
			throw new MESFrameException(e);
		}
		
	    return resultEncryptText;
	}
	/**
	 * 
	 * decryptText를 복호화합니다
	 *
	 * @param decryptText
	 * @return String
	 * @throws NoSuchAlgorithmException, RuntimeException, MESFrameException
	 *
	 */
	public static String decrypt(String decryptText)
	{
		String resultDecryptText = null;
		
		try
		{
			byte[] keyBytes = generateKey();
			
		    String transformation = "AES/CBC/PKCS5Padding";
		    
		    Cipher cipher = Cipher.getInstance(transformation);
			
		    SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
		    
			cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

	    	byte[] decryptResult = cipher.doFinal(DatatypeConverter.parseBase64Binary(decryptText));
	    	
	    	resultDecryptText = new String(decryptResult, "UTF-8");
		}
		catch (NoSuchAlgorithmException nsae)
		{
			throw new RuntimeException("NoSuchAlgorithmException occurred", nsae);
		}
		catch (NoSuchPaddingException nspe)
		{
			throw new RuntimeException("NoSuchPaddingException occurred", nspe);
		}
		catch (InvalidKeyException ike)
		{
			throw new RuntimeException("InvalidKeyException occurred", ike);			
		}
		catch (IllegalBlockSizeException ibse)
		{
			throw new RuntimeException("IllegalBlockSizeException occurred", ibse);
		}
		catch (BadPaddingException bpe)
		{
			throw new RuntimeException("BadPaddingException occurred", bpe);
		}
		catch (Exception e)
		{
			throw new MESFrameException(e);
		}
		
	    return resultDecryptText;
	}
}

