package zone.wim.item;

import java.security.*;
import java.security.spec.*;

import io.netty.buffer.ByteBuf;
import zone.wim.exception.ItemException.SignersOnly;
import zone.wim.token.Address;

public abstract class Signer extends Item {
	
//	private KeyStore keystore;
//	private PublicKey publicKey;
//	private PrivateKey privateKey;
	
	transient private boolean isAuthenticated = false;
	
	public Signer() {}
	
	public Signer(Address address) throws SignersOnly {
		super(address);
	}
	
	private void generateKeypair() {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC", "SunEC");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			keyGen.initialize(192, random);

			ECGenParameterSpec ecsp;
			ecsp = new ECGenParameterSpec("sect163k1");
			keyGen.initialize(ecsp);
			
			KeyPair kp = keyGen.genKeyPair();
//			privateKey = kp.getPrivate();
//			publicKey = kp.getPublic();
			
		} catch (Exception exception) {
			System.out.println("CAUGHT EXCEPTION!" + exception);
		}
	}
	
	
}