package zone.wim.codec;

//import java.nio.charset.Charset;
//import com.ibm.icu.charset.CharsetICU;

public abstract class Codec {
	
	public static String BOM = "\uFEFF";
	
	public static String DATE_TIME_FORMAT = "yyyymmddHHmmssZ";

//	public abstract Object decode(DecodeAdapter adapter);
	public abstract Decoder decoder();
	public abstract Encoder encoder();

	
}
/*
public enum Codec {
	UTF_8("UTF-8"),
	UTF_16("UTF-16"),
	UTF_16BE("UTF-16BE"),
	UTF_16LE("UTF-16LE"),
	UTF_32("UTF-32"),
	UTF_32BE("UTF-32BE"),
	UTF_32LE("UTF-32LE"),
	UTF_7("UTF-7");
	

	public static String formatString(String format) {
		if (format != null) {
			// TODO: figure out how to store time codecs
			
		}
		return DATE_TIME_FORMAT;
		
	}
	
	String name;
	
	Codec(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public Charset instance() {
		return CharsetICU.forNameICU(name);
	}
	
}
*/