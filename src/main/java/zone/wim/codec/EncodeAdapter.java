package zone.wim.codec;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.text.Normalizer.Form;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.netty.buffer.ByteBuf;
import zone.wim.item.*;
import zone.wim.item.tokens.Security;

/**
 * The `EncodeAdapater` is used by `Token` and its subclasses to serialize the content of
 * a given set of tokens in a given set of codecs.
 * 
 * @author joshua
 *
 */

public class EncodeAdapter {
	SelfCoding in;
	ByteBuf out;
	Codec charset = Codec.UTF_8;
	List<Codec> codecStack;
	
	public EncodeAdapter(SelfCoding in) {
		this.in = in;
		codecStack = new ArrayList<>();
	}
	
	public void encode(ByteBuf out) {
		this.out = out;
		signifyEncoding();
		in.encode(this);
	}
	
	public void writeElement(String element) {
		
	}
	
	public void write(char c) {
		out.writeBytes(charset().encode(String.valueOf(c)));
	}
	
	public void write(String text) {
		out.writeBytes(charset().encode(text));
	}
	
	public void write(CharBuffer chars) {
		out.writeBytes(charset().encode(chars));
	}
	
	public void write(Date datetime) {
		SimpleDateFormat format = new SimpleDateFormat(Codec.DATE_TIME_FORMAT);
		String dateString = format.format(datetime);
		out.writeBytes(charset().encode(dateString));
	}
	
	public void write(Security security) {
		
	}
	
	private void signifyEncoding() {
		if (charset != Codec.UTF_8) {
			out.writeBytes(charset().encode(Codec.BOM));
		}
		
	}
	
	private Charset charset() {
		return charset.instance();
	}
}
