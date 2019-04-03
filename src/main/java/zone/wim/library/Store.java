package zone.wim.library;

import java.nio.file.Path;

import java.util.*;

import zone.wim.exception.ItemException.NotFound;
import zone.wim.item.*;
import zone.wim.token.Token;

public interface Store {

	public static enum StoreType {
		JDO, JPA
	}
	
	public static Store getStore(StoreType storeType) {
		Store store = null;
		
		switch(storeType) {
		case JDO:
			store = new JdoStore();
			break;
		case JPA:
			store = new JpaStore();
			break;
		}
		
		return store;
	}

	public void open();
	
	public void put(Item item);
	
	public Item get(String address) throws NotFound;

	public Item get(String address, Class<? extends Item> clazz) throws NotFound;
	
	public List<Item> find(String token);
	
	public List<Item> find(String token, Class<? extends Token> type);

	public List<Item> findByWord(String word, Locale locale);

	public void scan(Path p);
	
	public void close();
	
}
