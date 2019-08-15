package zone.wim.item;

import java.util.Date;
import java.util.List;
import javax.jdo.annotations.EmbeddedOnly;

@EmbeddedOnly
public abstract class ItemComponent {
	
	private Reference enclosingItem;
	private Date timestamp;
	private int security = 0;
	
	private List<Signature> signatures;

	protected ItemComponent(Reference enclosingItem, int security) {
		this.enclosingItem = enclosingItem;
		this.security = security;
		this.timestamp = new Date();
	}
	
	public Reference getEnclosingItem() {
		return enclosingItem;
	}
	
	public void setEnclosingItem(Reference enclosingItem) {
		this.enclosingItem = enclosingItem;
	}

	public Date getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public int getSecurity() {
		return security;
	}
	
	public void setSecurity(int security) {
		this.security = security;
	}
	
	public List<Signature> getSignatures() {
		return signatures;
	}
	
	public void setSignatures(List<Signature> signatures) {
		this.signatures = signatures;
	}
}
