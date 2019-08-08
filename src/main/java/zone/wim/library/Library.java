package zone.wim.library;

import static picocli.CommandLine.Option;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.*;
import java.security.Security;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.logging.*;

import javax.imageio.ImageIO;

import org.apache.commons.daemon.*;
import org.bouncycastle.jce.provider.*;
import javafx.application.Application;
import javafx.application.Platform;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import zone.wim.client.DesktopClient;
import zone.wim.exception.ItemException.*;
import zone.wim.exception.LibraryException.*;
import zone.wim.exception.*;
import zone.wim.item.*;
import zone.wim.language.Fragment;
import zone.wim.socket.*;
import zone.wim.token.*;
import zone.wim.library.Store.*;

@Command(name="wim")
public class Library implements Daemon, Runnable {
	private static Logger LOGGER = Logger.getLogger(Library.class.getCanonicalName());
	private static Library INSTANCE = null;

	public static List<Integer> PORTS = Arrays.asList(25, 465, 587, 2525, 25025);

	public static synchronized Library instance() throws NotInitialized {
		if (INSTANCE != null && INSTANCE.isInitialized) {
			return INSTANCE;
		} else throw new NotInitialized();
	}
	
	public static void main(String[] args) {
		LOGGER.info("main(): " + String.join(", ", args));
		INSTANCE = new Library();
		int exitCode = new CommandLine(INSTANCE).execute(args);
		LOGGER.info("EXIT CODE: " + exitCode);
		if (exitCode > 0) {
			System.exit(exitCode);
		}
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
    private boolean graphicalClient = true;
	
	@Option(names = { "-t", "--terminal-client" }, description = "activate the terminal client")
	private boolean terminalClient = false;
	
	@Option(names = { "-u", "--untrusted-host" }, description = "treat the local host as untrusted")
	private boolean untrustedHost = false;
	
	@Option(names = { "-h", "--host" }, description = "initial host to connect query")
	private String hostName = null;
	
	private boolean runningAsDaemon = false;
	private boolean isInitialized = false;
	
	private Store store;
	private SocketServer server;

	private Host localhost;
	
	private TrayMenu trayMenu = null;

	private Library() {
		LOGGER.info("Library()");
	}

	@Override
	public void run() {
		init();
	}
	
	@Override
	public void init(DaemonContext context) throws DaemonInitException {
		int exitCode = new CommandLine(this).execute(context.getArguments());
		if (exitCode > 0) {
			throw new DaemonInitException("init failed with code: " + exitCode);
		}
		
		runningAsDaemon = true;
		INSTANCE = this;
	}

	
	
	private void doSomething() {
		System.out.println("DO SOMETHING!");
	}
	
	private void init() {
		LOGGER.info("init()");
//		System.setSecurityManager(new SecurityManager());		
//		Security.addProvider(new BouncyCastleProvider());
		
//		trayMenu = TrayMenu.init(this);
		
		store = Store.getStore(storeType);
		server = new SocketServer();

		localhost = getLocalhost();
		
		Runtime.getRuntime().addShutdownHook(new Thread("app-shutdown-hook") {
			@Override
			public void run() {
				try {
					Library.this.stop();
					Library.this.destroy();
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
		if (localhost instanceof Host) {
			return localhost;
		}
		
		InetAddress netAddress = InetAddress.getLoopbackAddress();
		String address = netAddress.getHostAddress();
		Host host = null;
		try {
			host = (Host)store.get(address, Host.class);
		} catch (NotFound e) {
			e.printStackTrace();
		}
		
		if (host == null) {
			host = Host.create(netAddress);
		}
		
		return host;
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
		LOGGER.info("shutdown()");
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
		trayMenu.destroy();
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
	
//	public List<Item> getItemsByTokenType(Token token) {
		
//	}
	
	public List<Item> getItemsByGroup(Group group) {
		List<Item> items = group.getContents();
		return items;		
	}
	
	public void runQuery(Fragment fragment) {	// TODO: QueryOptions argument
		
	}
}
