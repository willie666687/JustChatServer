package willie.util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

public class KeyUtils{
	protected static PrivateKey privateKey;
	public static PublicKey publicKey;

	public static void generateKey(){
		KeyPairGenerator keyPairGenerator;
		try{
			keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		}catch(NoSuchAlgorithmException e){
			throw new RuntimeException(e);
		}
		keyPairGenerator.initialize(2048);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		publicKey = keyPair.getPublic();
		privateKey = keyPair.getPrivate();
	}

	public static String encrypt(PublicKey publicKey, String message) throws Exception{
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);

		return Base64.getEncoder().encodeToString(cipher.doFinal(message.getBytes(StandardCharsets.UTF_8)));
	}

	public static String decrypt(String encryptedMessage){
		Cipher cipher = null;
		try{
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedMessage)), StandardCharsets.UTF_8);
		}catch(NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException |
		       InvalidKeyException e){
			throw new RuntimeException(e);
		}
	}

	public static String[] decryptMessages(String[] messages){
		String[] decryptedMessages = new String[messages.length];
		for(int i = 0; i < messages.length; i++){
			decryptedMessages[i] = decrypt(messages[i]);
		}
		return decryptedMessages;
	}
}
