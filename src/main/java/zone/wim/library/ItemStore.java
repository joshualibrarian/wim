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

public class ItemStore {
	
	private Properties options;
	private PersistenceManagerFactory pmf;
	private PersistenceManager pm;
	
	public ItemStore(String url) {
		configure();
		if (url != null && !url.isBlank()) {
			options.setProperty("javax.jdo.option.ConnectionURL", url);
		}
	}
	
	public void open() {
		pmf = JDOHelper.getPersistenceManagerFactory(options);
		pm = pmf.getPersistenceManager();
	}

	public boolean contains(Item item) {
		return false;	// TODO: stub
	}
	
	public void put(Item item) {
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
	}

	public Item get(String address) throws NotFound {
		return get(address, Item.class);
	}
	
	public Item get(String address, Class<? extends Item> clazz) throws NotFound {
		Transaction tx = pm.currentTransaction();
		Object result = null;
		
		try {
			StringIdentity id = new StringIdentity(clazz, address);
		    result = pm.getObjectById(id);
		    
		} catch(JDOObjectNotFoundException e) {
			throw new NotFound(address, e);
			
		} finally {
		    if (tx.isActive()) {
		    	System.out.println("ROLLING BACK!");
		        tx.rollback();
		    }
		}
		
		return clazz.cast(result);
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void close() {
		pm.close();
		pmf.close();
	}
}
