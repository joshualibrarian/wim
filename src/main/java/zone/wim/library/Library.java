package zone.wim.library;

import static picocli.CommandLine.Option;


import java.security.Security;

import java.net.*;
import java.util.*;
import java.util.logging.*;

import org.apache.commons.daemon.*;
import org.bouncycastle.jce.provider.*;

import com.kstruct.gethostname4j.Hostname;

import io.netty.util.concurrent.Future;
import javafx.application.Application;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import zone.wim.ui.graphical.GraphicalClient;
import zone.wim.coding.text.TextCodec;
import zone.wim.coding.text.unicode.UnicodeCodec;
import zone.wim.exception.LibraryException.*;
import zone.wim.item.*;
import zone.wim.item.components.ItemComponent;
import zone.wim.language.Fragment;
import zone.wim.library.store.*;
import zone.wim.socket.protocol.Request;
import zone.wim.socket.*;

@Command(name="wim")
public class Library implements Daemon, Runnable {
	private static Logger LOGGER = Logger.getLogger(Library.class.getCanonicalName());
	private static Library INSTANCE = null;

	public static synchronized Library local() {
		try {
			Library library = instance();
			return library;
		} catch (NotInitialized nie) {
			// TODO: here is where we report the connectivity issue or whatever
			nie.printStackTrace();
		}
		return null;
	}

	private static synchronized Library instance() throws NotInitialized {
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
	
//	@Option(names = { "-u", "--system-user" },
//			description = "")
//	private String systemUser = System.getProperty("user.name");
	
	@Option(names = { "-l", "--local-path" },
			description = "specify the local system filesystem path to store library data")
	private String localPath = null;
	
	@Option(names = { "-p", "--ports" }, 
			description = "use the specified ports")
	private int[] ports = { 25, 465, 587, 2525, 25025 };
	
	@Option(names = { "-s", "--server" }, 
			description = "run the local server and receive incoming connections")
	private boolean runServer = false;
	
	@Option(names = { "-g", "--graphical-client" }, 
			description = "activate the graphical client")
    private boolean graphicalClient = false;
	
	@Option(names = { "-t", "--terminal-client" }, 
			description = "activate the terminal client")
	private boolean terminalClient = false;
	
	@Option(names = { "-u", "--untrusted-host" }, 
			description = "treat the local host device as untrusted")
	private boolean untrustedHost = false;
	
	@Option(names = { "-h", "--host" }, 
			description = "initial host to connect query")
	private String hostName = null;
	
	@Option(names = { "-c", "--charset" }, 
			description = "set preferred text encoding")
	private TextCodec textEncoding = UnicodeCodec.UTF_8;
	public TextCodec textEncoding() {
		return textEncoding;
	}
	
	private boolean runningAsDaemon = false;
	private boolean isInitialized = false;
	
	private ItemStore store;
	private SocketServer server;

	private Host localHost;
	
	private TrayMenu trayMenu = null;

	private Library() { }
	
	@Override
	public void init(DaemonContext context) throws DaemonInitException {
		runningAsDaemon = true;
		INSTANCE = this;

		int exitCode = new CommandLine(INSTANCE).execute(context.getArguments());
		if (exitCode > 0) {
			throw new DaemonInitException("init failed with code: " + exitCode);
		}
	}
	
	/**
	 * This is where the actual initialize code for the library is run from.
	 */
	@Override
	public void run() {
		LOGGER.info("run()");
		Runtime.getRuntime().addShutdownHook(new ShutdownThread()); 
		
//		System.setSecurityManager(new SecurityManager());		
		Security.addProvider(new BouncyCastleProvider());

//		trayMenu = TrayMenu.init(this);
		
		store = new ItemStore(localPath);
		server = new SocketServer(ports);
		
		isInitialized = true; 
	}
	
	@Override
	public void start() throws Exception {
		LOGGER.info("start()");
		
//		store.open();
		localHost = getLocalhost();

		if (runServer) {
			server.start();
		}
		
		if (terminalClient) {
			LOGGER.info("SHOW TERMINAL CLIENT!");
		}

		if (graphicalClient) {
			LOGGER.info("SHOW GRAPHICAL CLIENT!");
			Application.launch(GraphicalClient.class, new String[0]);
		}
	}

	public void shutdown() throws Exception {
		LOGGER.info("shutdown()");
		System.out.println("shutdown()");
		stop();
		destroy();
	}
	
	@Override
	public void stop() throws Exception {
		LOGGER.info("stop()");
		System.out.println("stop()");
		
//		index.close();
//		store.close();
		
		if (runServer) {
			server.stop();
		}
	}

	@Override
	public void destroy() {
		LOGGER.info("destroy()");
		System.out.println("destroy()");
		isInitialized = false;
		INSTANCE = null;
//		trayMenu.destroy();
	}
	
	public Future request(InetAddress host, Request request) {
		return null;
		
	}
	
	private void configure() {
		
	}
	
	/**
	 * Am `ItemComponent` is the smallest unit that can be received independently, 
	 * and so when newly received components arrive from a peer or even from a 
	 * previously unknown disk, this is the function which processes them and integrates
	 * them into the local library.
	 *  
	 * @param component
	 */
	public void integrate(ItemComponent component) {
//		ItemEntry entry =
	}
	
	public void integrate(Item item) {
		
	}
	
//	private boolean isExistingLocalServerRunning() {
		
//	}
	
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
	
	public Host getLocalhost() {
		if (!(localHost instanceof Host)) {
			String hostname = Hostname.getHostname();
			InetAddress netAddress = InetAddress.getLoopbackAddress();
//			try {
//				localHost = (Host)store.get(netAddress.getHostAddress(), Host.class);
//			} catch (NotFound e) {
//				localHost = Host.create(netAddress);
//				integrate(localHost);
//			}
		}
		return localHost;
	}
	
	class ShutdownThread extends Thread {
		ShutdownThread() {
			super("app-shutdown-hook");
		}
		
		@Override
		public void run() {
			try {
				shutdown();
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("Bye! 🙇‍♂️👋🖖");
		}
	}
}
