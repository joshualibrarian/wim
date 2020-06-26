package zone.wim.library;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.nio.*;
import java.text.ParseException;
import java.text.BreakIterator;
import java.util.*;
import java.nio.charset.*;
import org.apache.commons.lang.StringUtils;
import com.ibm.icu.lang.UCharacter;

import com.ibm.icu.charset.*;

public class DecodeAdapter {
	
	public static String SHIFT_OUT = "\u000E";
	public static String SHIFT_IN = "\u000F";
	public static String BYTE_ORDER_MARK = "\uFEFF";
	public static char BOM = '\uFEFF';

    List<CharsetDecoder> charsetDecoders = new ArrayList<>();
    
	private ByteBuffer bytes = null;
	private CharBuffer chars = null;
	
	private CharSequence currentToken;
	private CharSequence currentChar;
	
	public DecodeAdapter(ByteBuffer buffer) {
		this.bytes = buffer;
		CharsetDecoder decoder = detectEncoding();
		
		charsetDecoders.add(decoder);
		
	}

	public void decode() {
		
	}
	
	private CharsetDecoder currentDecoder(ByteBuffer byteBuffer) {
		this.bytes = byteBuffer;
		return charsetDecoders.get(charsetDecoders.size() - 1);
		
	}

//	public CharSequence nextCodepointAsChars() {
//		char c = chars.get();
//		StringBuilder sb = new StringBuilder(c);
//		if (UCharacter.isLowSurrogate(c)) {
//			sb.append(chars.get());
//		}
//		return sb;
//	}
//	
//	public CharSequence nextGraphemeCluster() {
//		CharSequence c = nextCodepointAsChars();
//		
//	}
//	
//	public String nextToken() {
//		StringBuilder token = new String();
//		CharSequence character = null;
//		do {
//			character = nextCharacter();
//			token += character;
//			
//		} while (!character.matches("\\s"));
//
//		return token;
//	}
//	
	/** 
	 * This method examines the current location in the buffer for a Byte Order Mark (BOM) 
	 * in any supported encoding.  If one is found, then the correct decoder is returned.
	 * If none is found, the buffer is reset and the default of UTF-8 decoder is used.
	 * 
	 * @return CharacterDecoder
	 */
	private CharsetDecoder detectEncoding() {
		Charset charset = null;
		bytes.mark(); 		// mark the first byte so we can reset if no BOM found
		int[] b = new int[5];
		b[0] = bytes.get() & 0xFF;
		b[1] = bytes.get() & 0xFF;
		if (b[0] == 0xFE && b[1] == 0xFF) {			// UTF-16BE
			charset = CharsetICU.forNameICU("UTF-16BE");
		} else if (b[0] == 0xFF && b[1] == 0xFE) {	// could be UTF-16LE or UTF-32LE
			bytes.mark();
			b[2] = bytes.get() & 0xFF;
			b[3] = bytes.get() & 0xFF;
			
			if (b[2] == 0x00 && b[3] == 0x00) {		// UTF-32LE
				charset = CharsetICU.forNameICU("UTF-32LE");
			} else {										// UTF-16LE
				bytes.reset();	// set the buffer position to after the actual BOM
				charset = CharsetICU.forNameICU("UTF-16LE");
			}
		} else if (b[0] == 0x00 && b[1] == 0x00) {	// possibly UTF-32BE
			b[2] = bytes.get() & 0xFF;
			b[3] = bytes.get() & 0xFF;
			
			if (b[2] == 0xFE && b[3] == 0xFF) {
				charset = CharsetICU.forNameICU("UTF-32BE");
			} else {
				// TODO: throw MisplacedNullException (or some such)?
			}
		} else if (b[0] == 0xEF && b[1] == 0xBB) {	// possibly UTF-8 BOM
			b[3] = bytes.get() & 0xFF;
			
			if (b[3] == 0xBF) {
				charset = CharsetICU.forNameICU("UTF-8");
			} 
		} else if (b[0] == 0x2B && b[1] == 0x2F) { // possibly UTF-7
			b[2] = bytes.get() & 0xFF;
			
			if (b[2] == 0x76) {
				b[3] = bytes.get() & 0xFF;
				
				if (b[3] == 0x38 || b[3] == 0x39 || b[3] == 0x2B || b[3] == 0x2F) {
					charset = CharsetICU.forNameICU("UTF-7");
				} else if (b[3] == 0x38) {
					b[4] = bytes.get() & 0xFF;
					
					if (b[4] == 0x2D) {
						charset = CharsetICU.forNameICU("UTF-7");
					}
				}
			}
		}
		StandardCharsets a;
		if (charset == null) {	// there is no BOM here
			charset = CharsetICU.forNameICU("UTF-8");	// the default
			bytes.reset();
		}
		
		return charset.newDecoder();
	}
	
}