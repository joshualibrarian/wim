package zone.wim.library;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import javax.jdo.*;
import javax.jdo.identity.*;
import zone.wim.exception.*;
import zone.wim.exception.StoreException.NotFound;
import zone.wim.item.*;
import zone.wim.token.Token;

public class Store {
	
	private Properties options;
	private PersistenceManagerFactory pmf;
	
	public Store(String url) {
		configure();
		if (url != null && !url.isBlank()) {
			options.setProperty("javax.jdo.option.ConnectionURL", url);
		}
		System.out.println("Options: " + options);
	}
	
	public void open() {
		pmf = JDOHelper.getPersistenceManagerFactory(options);

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
		    	System.out.println("ROLLING BACK!");
		        tx.rollback();
		    }
		}
		pm.close();
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
		}
		
	    pm.close();

		if (clazz.isInstance(result)) {
			return clazz.cast(result);
		} else throw new StoreException.NotFound(address);
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
		options = new Properties();
		try {
			options.load(new FileInputStream("persistence.properties"));
			
			System.out.println("OPTIONS: " + options);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void close() {
		pmf.close();
	}

}
