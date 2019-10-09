package zone.wim.item;

import java.lang.reflect.Constructor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.*;
import java.security.spec.*;

import io.netty.buffer.ByteBuf;
import javafx.scene.layout.Pane;
import zone.wim.exception.ItemException.SignersOnly;
import zone.wim.token.*;
import zone.wim.token.AddressException.Invalid;

public abstract class Signer extends BaseItem implements Group {
	
	private KeyStore keystore;
	private PublicKey publicKey;
	private PrivateKey privateKey;
	
	transient private boolean isAuthenticated = false;
	
	@SuppressWarnings("unused")
	private Signer() {
		super();
	}
	
	public Signer(Address address) throws SignersOnly {
		super(address);
		generateKeypair();
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
			privateKey = kp.getPrivate();
			publicKey = kp.getPublic();
			
		} catch (Exception exception) {
			System.out.println("CAUGHT EXCEPTION!" + exception);
		}
	}
	
	public Item createItem(String name, Class<? extends Item> clazz) throws Invalid {
		ItemType type = new ClassItemType(clazz);
		Address a = generateAddress(name, type);
		
		return createItem(a, type);
	}
	
	
	public Item createItem(String name, ItemType type) throws Invalid {
		Address a = generateAddress(name, type);
		
		return createItem(a, type);
	}
	
	public Item createItem(Address address, ItemType type) {
		Item item = null;
		
		try {
			Class<? extends Item> clazz = type.getClazz();
			Constructor<? extends Item> constructor = clazz.getConstructor(new Class[]{Address.class});
			item = (Item) constructor.newInstance(address);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return item;
	}
	
	public Address generateAddress(String name, ItemType type) throws Invalid {
		return address.generate(this,  name, type);
	}

}