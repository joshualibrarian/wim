package zone.wim.library;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.*;
import java.text.ParseException;
import java.text.BreakIterator;
import java.util.*;
import java.nio.charset.*;
import org.apache.commons.lang.StringUtils;
import com.ibm.icu.lang.UCharacter;

import io.netty.buffer.ByteBuf;
import zone.wim.exception.MustImplementStaticMethod;
import zone.wim.token.Token;

import com.ibm.icu.charset.*;

public class DecodeAdapter {
	
	public static int SHIFT_OUT = '\u000E';
	public static int SHIFT_IN = '\u000F';
	public static int BYTE_ORDER_MARK = '\uFEFF';

    List<CharsetDecoder> charsetDecoders = new ArrayList<>();
    
	private ByteBuf bytes = null;
	private CharBuffer chars = null;
	
	private CharSequence currentToken;
	private CharSequence currentChar;
	
	public DecodeAdapter(ByteBuf in) {
		this.bytes = in;
		CharsetDecoder decoder = detectEncoding();
		
		charsetDecoders.add(decoder);
		
	}

	public Object decode(Class<? extends SelfCoding> targetClass) 
			throws DecodeException, MustImplementStaticMethod {
		SelfCoding result = null;
		try {
			Method method = targetClass.getMethod("parse", DecodeAdapter.class);
			result = targetClass.cast(method.invoke(this));

		} catch (NoSuchMethodException e) {
			throw new MustImplementStaticMethod(e);
		} catch (SecurityException e) {
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
		}
		
		return result;
	
	}
	
	private CharsetDecoder currentDecoder(ByteBuf byteBuffer) {
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
	/** 
	 * This method examines the current location in the buffer for a Byte Order Mark (BOM) 
	 * in any supported encoding.  If one is found, then the correct decoder is returned.
	 * If none is found, the buffer is reset and the default of UTF-8 decoder is used.
	 * 
	 * @return CharacterDecoder
	 */
	private CharsetDecoder detectEncoding() {
		Charset charset = null;
		bytes.markReaderIndex(); 		// mark the first byte so we can reset if no BOM found
		int[] b = new int[5];
		b[0] = bytes.readByte() & 0xFF;
		b[1] = bytes.readByte() & 0xFF;
		if (b[0] == 0xFE && b[1] == 0xFF) {			// UTF-16BE
			charset = CharsetICU.forNameICU("UTF-16BE");
		} else if (b[0] == 0xFF && b[1] == 0xFE) {	// could be UTF-16LE or UTF-32LE
			bytes.markReaderIndex();
			b[2] = bytes.readByte() & 0xFF;
			b[3] = bytes.readByte() & 0xFF;
			
			if (b[2] == 0x00 && b[3] == 0x00) {		// UTF-32LE
				charset = CharsetICU.forNameICU("UTF-32LE");
			} else {										// UTF-16LE
				bytes.resetReaderIndex();	// set the buffer position to after the actual BOM
				charset = CharsetICU.forNameICU("UTF-16LE");
			}
		} else if (b[0] == 0x00 && b[1] == 0x00) {	// possibly UTF-32BE
			b[2] = bytes.readByte() & 0xFF;
			b[3] = bytes.readByte() & 0xFF;
			
			if (b[2] == 0xFE && b[3] == 0xFF) {
				charset = CharsetICU.forNameICU("UTF-32BE");
			} else {
				// TODO: throw MisplacedNullException (or some such)?
			}
		} else if (b[0] == 0xEF && b[1] == 0xBB) {	// possibly UTF-8 BOM
			b[3] = bytes.readByte() & 0xFF;
			
			if (b[3] == 0xBF) {
				charset = CharsetICU.forNameICU("UTF-8");
			} 
		} else if (b[0] == 0x2B && b[1] == 0x2F) { // possibly UTF-7
			b[2] = bytes.readByte() & 0xFF;
			
			if (b[2] == 0x76) {
				b[3] = bytes.readByte() & 0xFF;
				
				if (b[3] == 0x38 || b[3] == 0x39 || b[3] == 0x2B || b[3] == 0x2F) {
					charset = CharsetICU.forNameICU("UTF-7");
				} else if (b[3] == 0x38) {
					b[4] = bytes.readByte() & 0xFF;
					
					if (b[4] == 0x2D) {
						charset = CharsetICU.forNameICU("UTF-7");
					}
				}
			}
		}
		StandardCharsets a;
		if (charset == null) {	// there is no BOM here
			charset = CharsetICU.forNameICU("UTF-8");	// the default
			bytes.resetReaderIndex();
		}
		
		return charset.newDecoder();
	}
	
}