package zone.wim.library;

import java.nio.charset.Charset;

import com.ibm.icu.charset.CharsetICU;

public enum Codec {
	UTF_8("UTF-8"),
	UTF_16("UTF-16"),
	UTF_16BE("UTF-16BE"),
	UTF_16LE("UTF-16LE"),
	UTF_32("UTF-32"),
	UTF_32BE("UTF-32BE"),
	UTF_32LE("UTF-32LE"),
	UTF_7("UTF-7");
	
	public static String BOM = "\uFEFF";
	
	public static String DATE_TIME_FORMAT = "yyyymmddHHmmssZ";

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
