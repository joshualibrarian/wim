package zone.wim.library;

import java.net.InetAddress;

import java.net.UnknownHostException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.security.Security;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.apache.commons.cli.*;
import org.apache.commons.daemon.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javafx.application.Application;
import zone.wim.client.DesktopClient;
import zone.wim.exception.ShutdownException;
import zone.wim.item.*;
import zone.wim.socket.*;
import zone.wim.library.Store.*;

public class Library implements Daemon {
	private static Logger LOGGER = Logger.getLogger(Library.class.getCanonicalName());
	public static List<Integer> PORTS = Arrays.asList(25, 465, 587, 2525, 25025);

	private static String[] ARGS;
	private static Library INSTANCE;

	private static Store.StoreType STORE_TYPE = StoreType.JDO;
	private static boolean RUNNING_AS_DAEMON = false;
	private static boolean GRAPHICAL_CLIENT;
	private static boolean TERMINAL_CLIENT;
	private static Path LOCAL_PATH;
	
	public static Library instance() {
		if (!(INSTANCE instanceof Library)) {
			INSTANCE = new Library();
		}
		return INSTANCE;
	}

	private Store store;
	private InetAddress localhost;
	private HostItem localhostItem;
	private ExecutorService executorService = Executors.newCachedThreadPool();
	
	public Library() {
		LOGGER.info("Library()");
	}

	@Override
	public void init(DaemonContext context) throws DaemonInitException {
		ARGS = context.getArguments();
		RUNNING_AS_DAEMON = true;
		init();
	}

	private void init() {
		configure();
		
		store = Store.getStore(StoreType.JPA);
//		System.setSecurityManager(new SecurityManager());		
		Security.addProvider(new BouncyCastleProvider());

		try {
			localhost = InetAddress.getLocalHost();
			
		} catch (UnknownHostException uhe) {
			// TODO how can this ever happen with localhost?
			uhe.printStackTrace();
		}

		Runtime.getRuntime().addShutdownHook(new Thread("app-shutdown-hook") {
			@Override
			public void run() {
				try {
					shutdown();
					destroy();
				} catch (ShutdownException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Bye! 🙇‍♂️👋🖖");
			}
		});
	}
	
	private void configure() {
		parseOptions();
	}
	
	private void parseOptions() {
		Options options = new Options();
		options.addOption("p", "path", true, "local path to library data");
		options.addOption("t", "terminal", false, "show the local terminal client");
		options.addOption("g", "graphic", true, "show the local graphical client");
		options.addOption("o", "outgoing", false, "allow only outgoing connections");
		options.addOption("u", "untrusted", false, "treat the current host client as untrusted");
		
		CommandLineParser parser = new DefaultParser();

		try {
			CommandLine cmd = parser.parse(options, ARGS);
			String pathString = Optional.ofNullable(cmd.getOptionValue("p")).orElse(System.getProperty("user.home"));
			LOCAL_PATH = resolvePath(pathString);
		} catch (ParseException pe) {
			// show help text here
			System.exit(0);
		} 
	}
	
	private Path resolvePath(String pathString) {
		return FileSystems.getDefault().getPath(pathString);
	}
	
	public void start() throws Exception {
		LOGGER.info("Starting up daemon.");
		store.open();
		executorService.execute(new SocketServer());
	}

	public static void main(String[] args) {
		Library.ARGS = args;
		Library.instance().init();
		
		try {
			Library.instance().start();
		} catch (Exception e) {
			// TODO something here
			e.printStackTrace();
		}
		
		if (TERMINAL_CLIENT) {
			System.out.println("SHOW TERMINAL!");
//			TerminalClient tc = new TerminalClient();
		}

		if (GRAPHICAL_CLIENT) {
			Application.launch(DesktopClient.class, args);
		}
	}

	public void shutdown() throws ShutdownException {
		try {
			stop();
			destroy();
		} catch (Exception e) {
			// TODO lots of error handling
			e.printStackTrace();
		}
	}
	
	@Override
	public void stop() throws Exception {
		LOGGER.info("Stopping daemon.");

	}

	@Override
	public void destroy() {
		LOGGER.info("destroy()");
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
