package zone.wim.library;

import java.nio.file.Path;

import java.util.*;
import java.util.logging.*;

import javax.persistence.*;

import zone.wim.item.*;
import zone.wim.token.Token;
import zone.wim.exception.ItemException.*;

public class JpaStore implements Store {
	private static Logger LOGGER = Logger.getLogger(JpaStore.class.getCanonicalName());

	private Path path;
	private EntityManagerFactory emf;
	private EntityManager em;
	
	public JpaStore() {
		configure();
	}
	
	@Override
	public void open() {
		emf = Persistence.createEntityManagerFactory("data/jpa-library.odb");
		em = emf.createEntityManager();
	}

	@Override
	public void put(Item item) {
		em.getTransaction().begin();
		em.persist(item);
		em.getTransaction().commit();
	}
	
	@Override
	public Item get(String address) throws NotFound {
		try {
			return em.find(Item.class, address);
		} catch (EntityNotFoundException e) {
			throw new NotFound(address);
		}
	}
	
	@Override
	public List<Item> find(String token) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Item> findByWord(String word, Locale locale) {
		ArrayList<Item> items = new ArrayList<>();
		
		return items;
	}
	
	@Override
	public void scan(Path p) {
		
	}

	@Override
	public Item get(String address, Class<? extends Item> clazz) {
		return null;
	}

	@Override
	public List<Item> find(String token, Class<? extends Token> type) {
		return null;
	}

	@Override
	public void close() {
		em.close();
		emf.close();
	}

	private void configure() {
		
	}
}
