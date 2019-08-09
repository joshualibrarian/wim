package zone.wim.library;

import java.nio.file.Path;

import java.util.*;
import javax.jdo.*;
import javax.jdo.identity.*;

import zone.wim.exception.*;
import zone.wim.exception.ItemException.NotFound;
import zone.wim.item.*;
import zone.wim.token.Token;

public class Store {
	
	private Properties properties;
	private PersistenceManagerFactory pmf;
	private String path = null;
	
	public Store() {
		configure();
	}
	
	public void open() {
		pmf = JDOHelper.getPersistenceManagerFactory("data/jdo-library.odb");
	}

	public boolean contains(Item item) {
		return false;	// TODO: stub
	}
	
	public void put(Item item) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
		    tx.begin();
		    pm.makePersistent(item);
		    tx.commit();
		    
		} finally {
		    if (tx.isActive()) {
		        tx.rollback();
		    }
		}
	}

	public Item get(String address) throws NotFound {
		return get(address, Item.class);
	}
	
	public Item get(String address, Class<? extends Item> clazz) throws NotFound {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		Object result = null;
		try {
			StringIdentity id = new StringIdentity(clazz, address);
		    result = pm.getObjectById(id);
		    
		} finally {
		    if (tx.isActive()) {
		        tx.rollback();
		    }
		    pm.close();
		}
		
		if (clazz.isInstance(result)) {
			return clazz.cast(result);
		} else throw new ItemException.NotFound(address);
	}

	public List<Item> find(String token) {
		return null;
	}


	public List<Item> findByWord(String word, Locale locale) {
		return null;
	}

	public void scan(Path p) {
		
	}

	public List<Item> find(String token, Class<? extends Token> type) {
		// TODO Auto-generated method stub
		return null;
	}
	
	void configure() {
		properties = new Properties();
	}
	
	public void close() {
		pmf.close();
	}

}
