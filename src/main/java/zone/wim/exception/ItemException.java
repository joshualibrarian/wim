package zone.wim.exception;

@SuppressWarnings("serial")
public class ItemException extends Exception {
	
	public static class SignersOnly extends Exception {
		public SignersOnly(String address) {
			super(address);
		}
	}
	
	public static class HostsOnly extends SignersOnly {
		public HostsOnly(String address) {
			super(address);
		}
	}
	
	public static class SitesOnly extends HostsOnly {
		public SitesOnly(String address) {
			super(address);
		}
		
	}
	
	public static class EndOfItemReached extends Exception {
		public EndOfItemReached() {
			super();
		}
	}

}
