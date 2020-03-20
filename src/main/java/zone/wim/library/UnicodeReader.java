package zone.wim.library;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import org.apache.commons.lang.StringUtils;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.charset.*;

public class UnicodeReader<T extends SelfParsing> {
	
	public static String SHIFT_OUT = "\u000E";
	public static String SHIFT_IN = "\u000F";
	public static String BYTE_ORDER_MARK = "\uFEFF";

    List<CharacterDecoder> characterDecoders = new ArrayList<>();
    Map<String, Integer> controlCharacters;
    
    private Class<T> clazz;
	private ByteBuffer buffer = null;
	
	private String currentCharacter;
	private String currentToken;
	
	public UnicodeReader() {
		
	}
	
	public UnicodeReader(Class<T> clazz) {
		this.clazz = clazz;
//		characterDecoders.add(new UTF_8_CharacterDecoder());
	}
	
	private CharacterDecoder currentDecoder() {
		return characterDecoders.get(characterDecoders.size() - 1);
	}
	
	public T parse(ByteBuffer buffer) {
		characterDecoders.add(detectEncoding());
		
		T element;
		try {
			element = clazz.getConstructor().newInstance();
			element.parse(this);
			return element;

		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String nextCharacter() {
		currentCharacter = currentDecoder().nextCharacter();
		if (currentCharacter.matches(SHIFT_OUT)) {
			
		}
		return currentCharacter;
	}
	
	public String nextToken() {
		String token = new String();
		String character = null;
		do {
			character = nextCharacter();
			token += character;
			
		} while (!character.matches("\\s"));

		return token;
	}
	
	/** 
	 * This method examines the current location in the buffer for a Byte Order Mark (BOM) 
	 * in any supported encoding.  If one is found, then the correct decoder is returned.
	 * If none is found, the buffer is reset and the default of UTF-8 decoder is used.
	 * 
	 * @return CharacterDecoder
	 */
	private CharacterDecoder detectEncoding() {
		CharacterDecoder decoder = null;
		Charset charset;
		buffer.mark(); 		// mark the first byte so we can reset if no BOM found
		int[] bytes = new int[5];
		bytes[0] = buffer.get() & 0xFF;
		bytes[1] = buffer.get() & 0xFF;
		if (bytes[0] == 0xFE && bytes[1] == 0xFF) {			// UTF-16BE
			decoder = new UTF16CharacterDecoder(ByteOrder.BIG_ENDIAN);
			charset = StandardCharsets.UTF_16BE;
		} else if (bytes[0] == 0xFF && bytes[1] == 0xFE) {	// could be UTF-16LE or UTF-32LE
			buffer.mark();
			bytes[2] = buffer.get() & 0xFF;
			bytes[3] = buffer.get() & 0xFF;
			
			if (bytes[2] == 0x00 && bytes[3] == 0x00) {		// UTF-32LE
				decoder = new UTF32CharacterDecoder(ByteOrder.LITTLE_ENDIAN);
				charset = Charset.forName("UTF-32LE");
			} else {										// UTF-16LE
				buffer.reset();	// set the buffer position after the actual BOM
				decoder = new UTF16CharacterDecoder(ByteOrder.LITTLE_ENDIAN);
				charset = StandardCharsets.UTF_16LE;
			}
		} else if (bytes[0] == 0x00 && bytes[1] == 0x00) {	// possibly UTF-32BE
			bytes[2] = buffer.get() & 0xFF;
			bytes[3] = buffer.get() & 0xFF;
			
			if (bytes[2] == 0xFE && bytes[3] == 0xFF) {
				decoder = new UTF32CharacterDecoder(ByteOrder.BIG_ENDIAN);
				charset = Charset.forName("UTF-32BE");
			} else {
				// TODO: throw MisplacedNullException (or some such)?
			}
		} else if (bytes[0] == 0xEF && bytes[1] == 0xBB) {	// possibly UTF-8 BOM
			bytes[3] = buffer.get() & 0xFF;
			
			if (bytes[3] == 0xBF) {
				decoder = new UTF8CharacterDecoder();
				charset = StandardCharsets.UTF_8;
			} 
		} else if (bytes[0] == 0x2B && bytes[1] == 0x2F) { // possibly UTF-7
			bytes[2] = buffer.get() & 0xFF;
			
			if (bytes[2] == 0x76) {
				bytes[3] = buffer.get() & 0xFF;
				
				if (bytes[3] == 0x38 || bytes[3] == 0x39 || bytes[3] == 0x2B || bytes[3] == 0x2F) {
//					decoder = new UTF_7CharacterDecoder();	// TODO: might be needed on 7 bit mail servers
				} else if (bytes[3] == 0x38) {
					bytes[4] = buffer.get() & 0xFF;
					
					if (bytes[4] == 0x2D) {
//						decoder = new UTF_7_CharacterDecoder();	// TODO: implement this later
					}
				}
			}
		}
		if (decoder == null) {
			buffer.reset();	// there is no BOM here
			return new UTF8CharacterDecoder();
		}
		
		if (charset == null) {
			charset = StandardCharsets.UTF_8;
			buffer.reset();
		}
		
		return decoder;
	}
	
	
	private abstract class CharacterDecoder {
		ByteOrder byteOrder = null;

		CharacterDecoder() {}
		CharacterDecoder(ByteOrder byteOrder) {
			if (byteOrder instanceof ByteOrder) {
				buffer.order(byteOrder);
				this.byteOrder = byteOrder;
			} else {
				this.byteOrder = buffer.order();
			}
		}
		
		abstract String nextCharacter();
		abstract int nextCodepoint();
		abstract Charset charset();
	}
	
	private class UTF8CharacterDecoder extends CharacterDecoder {

		String nextCharacter() {
			byte b = buffer.get();
			short byteCount = getCharacterSize(b);
			byte[] bytes = new byte[byteCount];
			bytes[0] = b;
			if (byteCount > 1) {
				buffer.get(bytes, 1, byteCount - 1);
			}
			return new String(bytes, charset());			
		}
		
		short getCharacterSize(byte b) throws IllegalArgumentException {
			if (b >= 0) return 1;                             // Pattern is 0xxxxxxx.
	        if (b >= 0b11000000 && b <= 0b11011111) return 2; // Pattern is 110xxxxx.
	        if (b >= 0b11100000 && b <= 0b11101111) return 3; // Pattern is 1110xxxx.
	        if (b >= 0b11110000 && b <= 0b11110111) return 4; // Pattern is 11110xxx.
	        throw new IllegalArgumentException(); // Invalid first byte for UTF-8 character.
	    }

		@Override
		Charset charset() {
			return StandardCharsets.UTF_8;
		}
	}
	
	private class UTF16CharacterDecoder extends CharacterDecoder {
		
		UTF16CharacterDecoder(ByteOrder byteOrder) {
			super(byteOrder);
		}
		
		int nextCodepoint() {
			char c = buffer.getChar();
			if (UCharacter.isHighSurrogate(c)) {
				char s = buffer.getChar();
				UCharacter.toCodePoint(c, s);
			}
		}
		
		String nextCharacter() {
			char c = buffer.getChar();
			if ((int)c >= 0xDB00 && (int)c <= 0xDBFF) {
				char c2 = buffer.getChar();
				UCharacter.toCodePoint(c, c2);
				return new String(new char[]{c, c2});
			}
			return new String(new char[]{c});
		}
		
		@Override
		Charset charset() {
			if (byteOrder == ByteOrder.BIG_ENDIAN) {
				return StandardCharsets.UTF_16BE;
			} else if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
				return StandardCharsets.UTF_16LE;
			} 
			
			return StandardCharsets.UTF_16;
		}
	}
	
	private class UTF32CharacterDecoder extends CharacterDecoder {
		
		UTF32CharacterDecoder(ByteOrder byteOrder) {
			super(byteOrder);
		}
		
		String nextCharacter() {
			byte[] bytes = new byte[4];
			buffer.get(bytes, 0, 4);
			return new String(bytes, charset());
		}
		@Override
		Charset charset() {
			String charsetName = "UTF-32";
			if (byteOrder == ByteOrder.BIG_ENDIAN) {
				charsetName += "BE";
			} else if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
				charsetName += "LE";
			}
			
			return Charset.forName(charsetName);
		}
	}
}