package zone.wim.item;

import java.io.InputStream;
import java.util.Date;

import java.util.List;

import javax.jdo.annotations.EmbeddedOnly;

import zone.wim.library.EncodingAdapter;
import zone.wim.token.ComponentReference;

@EmbeddedOnly
public class Manifest extends ItemComponent {
	
	int index;
	private List<ComponentReference> references;
	
	public Manifest(Reference enclosingItem, int security, List<ComponentReference> references) {
		super(enclosingItem, security);
		this.references = references;
	}
	
	@Override
	public void parse(EncodingAdapter reader) {
		String token = reader.nextToken();
	}
	
	public char referenceCharacter() {
		return Item.MANIFEST_CHAR;	// DC1
	}

	public ByteBuffer serialize(boolean relative) {
		StringBuilder sb = new StringBuilder();
		if (!relative) {
			sb.append(this.address);
		}
		sb.append(Item.MANIFEST_CHAR);
		sb.append(Item.SPACE_CHAR);
		sb.append(Item)
	}
}
