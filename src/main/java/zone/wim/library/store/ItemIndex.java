package zone.wim.library.store;

import java.io.FileInputStream;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.jdo.JDOHelper;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;
import javax.jdo.identity.StringIdentity;

import zone.wim.item.Item;
import zone.wim.library.store.IndexException.*;

public class ItemIndex {

	private Properties options;
	private PersistenceManagerFactory pmf;
	private PersistenceManager pm;
	
	public ItemIndex(String url) {
		configure();
		if (url != null && !url.isBlank()) {
			options.setProperty("javax.jdo.option.ConnectionURL", url);
		}
	}
	
	public void open() {
		pmf = JDOHelper.getPersistenceManagerFactory(options);
		pm = pmf.getPersistenceManager();
	}

	public ItemEntry[] findByWord(String word) throws NotFound {
		Transaction tx = pm.currentTransaction();
		ItemEntry[] result = null;
		
		try {
			StringIdentity id = new StringIdentity(WordEntry.class, word);
		    result = (ItemEntry[])pm.getObjectById(id);
		    
		} catch(JDOObjectNotFoundException e) {
			throw new NotFound(word, e);
			
		} finally {
		    if (tx.isActive()) {
		    	System.out.println("ROLLING BACK!");
		        tx.rollback();
		    }
		}
		
		return result;
	}
	
	public ItemEntry findByAddress(String address) throws NotFound {
		Transaction tx = pm.currentTransaction();
		ItemEntry result = null;
		
		try {
			StringIdentity id = new StringIdentity(ItemEntry.class, address);
		    result = (ItemEntry)pm.getObjectById(id);
		    
		} catch(JDOObjectNotFoundException e) {
			throw new NotFound(address, e);
			
		} finally {
		    if (tx.isActive()) {
		    	System.out.println("ROLLING BACK!");
		        tx.rollback();
		    }
		}
		
		return result;
		
	}
	
	public void addWord(String word, String[] addresses) throws Duplicate {
		Transaction tx = pm.currentTransaction();
		WordIndex.WordEntry wordEntry = null;
		
		try {
			StringIdentity id = new StringIdentity(WordIndex.WordEntry.class, word);
			wordEntry = (WordIndex.WordEntry)pm.getObjectById(id);
			
			if (wordEntry instanceof WordIndex.WordEntry) {
				
			}
		    tx.begin();
		    pm.makePersistent(wordEntry);
		    tx.commit();
		    
		} finally {
		    if (tx.isActive()) {
		    	System.out.println("ROLLING BACK!");
		        tx.rollback();
		    }
		}
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
