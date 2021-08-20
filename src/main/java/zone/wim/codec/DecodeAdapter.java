package zone.wim.codec;

import java.nio.*;
import java.nio.charset.CharacterCodingException;
import java.util.*;
//import java.nio.charset.*;
import com.ibm.icu.lang.UCharacter;

import io.netty.buffer.ByteBuf;
import zone.wim.codec.text.TextCodec;
import zone.wim.codec.text.TextDecoder;
import zone.wim.codec.text.unicode.UTF_8;
import zone.wim.codec.text.unicode.UnicodeCodec;
import zone.wim.item.Item;

public class DecodeAdapter {
	
	public static int SHIFT_OUT = '\u000E';
	public static int SHIFT_IN = '\u000F';
	public static int BYTE_ORDER_MARK = '\uFEFF';
	public static int[] ESCAPE_CHARS = { Item.SPACE_CHAR, SHIFT_OUT, Item.BINARY_CHAR };
	
    List<TextDecoder> textDecoders = new ArrayList<>();
    
	private ByteBuf bytes = null;
	private CharBuffer chars = null;
	private List<String> tokens;
	
	private CharSequence currentToken;
	private CharSequence currentChar;
	private int currentCodepoint = -1;
	private int startIndexOfCurrentToken = 0;	//TODO: correct for BOM
	private int endIndexOfCurrentToken = -1;
	
	
	public DecodeAdapter(ByteBuf in) {
		this.bytes = in;
		TextDecoder decoder = detectEncoding();
		textDecoders.add(decoder);
		try {
			// this convenient method is used only the first time 
			// to create the output buffer for us
			chars = decoder.decode(bytes.nioBuffer());
		} catch (CodingException.Escaped e) {
			// this is to be expected, and would only *not* occur if
			// there is only a single token in the data before any escapes
			currentCodepoint = e.escapedCodepoint;
			currentChar = UCharacter.toString(e.escapedCodepoint);
			
			if (currentCodepoint == Item.SPACE_CHAR) {
				endIndexOfCurrentToken = chars.position() - 1;
			} else {
//				currentToken = chars.get
			}
			int l = chars.position() - startIndexOfCurrentToken;
			char[] c = new char[l];
			chars.mark();
			chars.position(startIndexOfCurrentToken);
			chars.get(c, 0, l);
			
			
		} catch (CharacterCodingException e) {
			//TODO: here, there is an actual coding error
		}
		
	}
	
	protected void shiftOut() {
	}
	
//	protected 
	
	protected TextDecoder currentDecoder() {
		return textDecoders.get(textDecoders.size() - 1);
	}
	
	protected void decodeToken() {
		currentDecoder().decode(bytes, chars, false);
	}

	public CharSequence currentToken() {
		return currentToken;
	}
	
//	public CharSequence nextToken() {
//		while (bytes.read)
//	}
	
//	public Object decode(Class<? extends SelfCoding> targetClass) 
//			throws DecodeException, MustImplementStaticMethod {
//		SelfCoding result = null;
//		try {
//			Method method = targetClass.getMethod("parse", DecodeAdapter.class);
//			result = targetClass.cast(method.invoke(this));
//
//		} catch (NoSuchMethodException e) {
//			throw new MustImplementStaticMethod(e);
//		} catch (SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return result;
//	
//	}
	
	private TextDecoder currentDecoder(ByteBuf byteBuffer) {
		this.bytes = byteBuffer;
		return textDecoders.get(textDecoders.size() - 1);
		
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
	private TextDecoder detectEncoding() {
		TextCodec charset = null;
		bytes.markReaderIndex(); 		// mark the first byte so we can reset if no BOM found
		int[] b = new int[5];
		b[0] = bytes.readByte() & 0xFF;
		b[1] = bytes.readByte() & 0xFF;
		if (b[0] == 0xFE && b[1] == 0xFF) {			// UTF-16BE
			charset = UnicodeCodec.UTF_16BE;
		} else if (b[0] == 0xFF && b[1] == 0xFE) {	// could be UTF-16LE or UTF-32LE
			bytes.markReaderIndex();
			b[2] = bytes.readByte() & 0xFF;
			b[3] = bytes.readByte() & 0xFF;
			
			if (b[2] == 0x00 && b[3] == 0x00) {		// UTF-32LE
				charset = UnicodeCodec.UTF_32LE;
			} else {										// UTF-16LE
				bytes.resetReaderIndex();	// set the buffer position to after the actual BOM
				charset = UnicodeCodec.UTF_16LE;
			}
		} else if (b[0] == 0x00 && b[1] == 0x00) {	// possibly UTF-32BE
			b[2] = bytes.readByte() & 0xFF;
			b[3] = bytes.readByte() & 0xFF;
			
			if (b[2] == 0xFE && b[3] == 0xFF) {
				charset = UnicodeCodec.UTF_32BE;
			} else {
				// TODO: throw MisplacedNullException (or some such)?
			}
		} else if (b[0] == 0xEF && b[1] == 0xBB) {	// possibly UTF-8 BOM
			b[3] = bytes.readByte() & 0xFF;
			
			if (b[3] == 0xBF) {
				charset = UnicodeCodec.UTF_8;
			} 
		} else if (b[0] == 0x2B && b[1] == 0x2F) { // possibly UTF-7
			b[2] = bytes.readByte() & 0xFF;
			
			if (b[2] == 0x76) {
				b[3] = bytes.readByte() & 0xFF;
				
				if (b[3] == 0x38 || b[3] == 0x39 || b[3] == 0x2B || b[3] == 0x2F) {
					charset = UnicodeCodec.UTF_7;
				} else if (b[3] == 0x38) {
					b[4] = bytes.readByte() & 0xFF;
					
					if (b[4] == 0x2D) {
						charset = UnicodeCodec.UTF_7;
					}
				}
			}
		}
//		StandardCharsets a;
		if (charset == null) {	// there is no BOM here
//			charset = CharsetICU.forNameICU("UTF-8");	// the default
			bytes.resetReaderIndex();
		}
		charset = new UTF_8();
		
		return (TextDecoder)charset.decoder();
	}
	
}