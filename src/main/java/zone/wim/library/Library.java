package zone.wim.library;

import static picocli.CommandLine.Option;

import java.net.*;
import java.security.Security;
import java.util.*;
import java.util.logging.*;
import org.apache.commons.daemon.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import javafx.application.Application;
import picocli.CommandLine;
import zone.wim.client.DesktopClient;
import zone.wim.exception.ItemException.*;
import zone.wim.exception.LibraryException.*;
import zone.wim.exception.*;
import zone.wim.item.*;
import zone.wim.socket.*;
import zone.wim.token.*;
import zone.wim.library.Store.*;

public class Library implements Daemon, Runnable {
	private static Logger LOGGER = Logger.getLogger(Library.class.getCanonicalName());
	public static List<Integer> PORTS = Arrays.asList(25, 465, 587, 2525, 25025);
	private static Library INSTANCE = null;
	
	public static synchronized Library instance() throws NotInitialized {
		if (INSTANCE != null && INSTANCE.isInitialized) {
			return INSTANCE;
		} else throw new NotInitialized();
	}
	
	public static void main(String[] args) {
		INSTANCE = new Library();
		CommandLine.run(INSTANCE, args);
		try {
			INSTANCE.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Option(names = { "-l", "--local-path" }, description = "specify the local filesystem path")
	private String localPath = "data/";
	
	@Option(names = { "-p", "--persistence-type" }, description = "select type of persistence store")
	private StoreType storeType = StoreType.JPA;
	
	@Option(names = { "-s", "--server" }, description = "run the local server and receive incoming connections")
	private boolean runServer = false;
	
	@Option(names = { "-g", "--graphical-client" }, description = "activate the graphical client")
    private boolean graphicalClient = false;
	
	@Option(names = { "-t", "--terminal-client" }, description = "activate the terminal client")
	private boolean terminalClient = false;
	
	@Option(names = { "-u", "--untrusted-host" }, description = "treat the local host as untrusted")
	private boolean untrustedHost = false;
	
	private boolean runningAsDaemon = false;
	private boolean isInitialized = false;
	
	private Store store;
	private SocketServer server;

//	private InetAddress localhost;
	private Host localhost;
	
	private Library() {
		LOGGER.info("Library()");
	}

	@Override
	public void run() {
		init();
	}
	
	@Override
	public void init(DaemonContext context) throws DaemonInitException {
		runningAsDaemon = true;
		INSTANCE = this;
		CommandLine.run(this,  context.getArguments());
	}

	private void init() {
		LOGGER.info("init()");
//		System.setSecurityManager(new SecurityManager());		
		Security.addProvider(new BouncyCastleProvider());

		store = Store.getStore(storeType);
		server = new SocketServer();

		localhost = getLocalhost();
		
		Runtime.getRuntime().addShutdownHook(new Thread("app-shutdown-hook") {
			@Override
			public void run() {
				try {
					Library.this.stop();
					destroy();
				} catch (ShutdownException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("Bye! 🙇‍♂️👋🖖");
			}
		});
		
		isInitialized = true;
	}
	
	public Host getLocalhost() {
		InetAddress host = InetAddress.getLoopbackAddress();
		try {
			String address = host.getHostAddress();
			return (Host) store.get(address, Host.class);
		} catch (NotFound e) {
			return Host.create(host);
		}
	}
	
	@Override
	public void start() throws Exception {
		LOGGER.info("start()");
		store.open();
		
		if (runServer) {
			server.start();
		}
		
		if (terminalClient) {
			LOGGER.info("SHOW TERMINAL CLIENT!");
		}

		if (graphicalClient) {
			LOGGER.info("SHOW GRAPHICAL CLIENT!");
			Application.launch(DesktopClient.class, new String[0]);
		}
	}

	public void shutdown() throws Exception {
		stop();
		destroy();
	}
	
	@Override
	public void stop() throws Exception {
		LOGGER.info("stop()");
		store.close();
		if (runServer) {
			server.stop();
		}
	}

	@Override
	public void destroy() {
		LOGGER.info("destroy()");
		isInitialized = false;
		INSTANCE = null;
	}
	
	public Item getItemByAddress(String address) {
		return null;
	}
	
	public List<Item> getItemsByWord(String word) {
		return Collections.emptyList();
	}
	
	public List<Item> getItemsByClass(Class<? extends Item> clazz) {
		return Collections.emptyList();
	}
}
