package zone.wim.coding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.*;
import java.util.*;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import zone.wim.coding.text.TextDecoder;
import zone.wim.coding.token.CodingToken;
import zone.wim.library.Library;
import zone.wim.coding.SelfCodingException.*;

public class DecodeAdapter {

	public static final int SHIFT_OUT = '\u000E';
	public static final int SHIFT_IN = '\u000F';

	@Getter @Setter private int defaultTokenizers[] = { ' ' };
	@Setter @Getter public int[] textTokenizers;
	@Setter @Getter private Codec defaultCodec;
	@Getter @Setter private int defaultCharBufferSize = 1024;
	@Getter @Setter private int defaultByteBufferSize = 1024;


	@Getter @Setter private int shiftOutChar = SHIFT_OUT;
	@Getter @Setter private int shiftInChar = SHIFT_IN;


	public static DecodeAdapter adapterFor(ByteBuf in, Codec codec) {
		DecodeAdapter adapter = new DecodeAdapter(in, codec);
		adapter.initialize();

		return adapter;
	}

	public static DecodeAdapter adapterFor(ByteBuf in) {
		return DecodeAdapter.adapterFor(in, Library.local().textEncoding());
	}

	@Getter @Setter private Stack<Object> preCoded;
	private Stack<Decoder> decoderStack;

//    private Stack<Expecting> expectingStack;
//	private Stack<SelfCoding> foundStack;
//	private Stack<CodingToken> tokenStack;

	@Getter @Setter private ByteBuf sourceBytes;
	@Getter @Setter private CharBuffer activeCharBuffer;
	private List<String> tokens;
	
	private CharSequence currentToken;
	private CharSequence currentChar;

	private int readIndex = 0;
	private int currentCodepoint = -1;
	private int startIndexOfCurrentToken = 0;	//TODO: correct for BOM
	private int endIndexOfCurrentToken = -1;

	private int decodeCountdown = -1;

	protected DecodeAdapter(ByteBuf in, Codec defaultOverride) {
		defaultCodec = defaultOverride;
		sourceBytes = in;
//		foundStack = new Stack<>();
//		expectingStack = new Stack<>();

		decoderStack = new Stack<>();

//		textTokenizers = new ArrayList<>();
//		textTokenizers.add(shiftOutChar);

		preCoded = new Stack<>();
	}

	protected void initialize() {
		Codec detectedCodec = defaultCodec.detectEncoding(sourceBytes);
		Decoder decoder = detectedCodec.decoder(sourceBytes, activeCharBuffer);
		decoderStack.add(decoder);

		// even if the default codec is not a text codec, one might eventually be
		// and we don't want to be stuck without a charbuffer to use for the
		// next TextDecoder to come around
		// TODO: allow more fine control of default buffer size
		activeCharBuffer =  CharBuffer.allocate(defaultCharBufferSize);

		decoder.escapeHandler((lastDecoded) -> shouldEscape(lastDecoded));
	}

	protected boolean doCountdown() {
		if (decodeCountdown > -1) {
			if(decodeCountdown >= 0) {
				decodeCountdown--;
			}
			if (decodeCountdown == -1) {
				return true;
			}
		}
		return false;
	}

	protected boolean isCodecShift(int codepoint) {
		if (codepoint == shiftInChar) {
//			shiftIn()
		}
		if (codepoint == shiftOutChar) {

		}
	}

	protected CoderResult shouldEscape(Object lastDecoded) {

		if(doCountdown()) {
			return CoderResult.escaped(-1);
		}

		return CoderResult.escaped((Integer)lastDecoded);
	}

	public int lastDecodedChar() {
		//TODO: make unicode-safe
		return activeCharBuffer.get(activeCharBuffer.position());
	}

	/**
	 * expecting codepoints
	 *
	 * @return
	 */

	public int expectCodepoint() {
		TextDecoder decoder = (TextDecoder)decoderStack.peek();
		decoder.countdown(1);
		decoder.decode(false);
		return activeCharBuffer.get();
	}

	public int expectCodepointOfValue(int value) throws IncorrectlyCoded {
		int codepoint = expectCodepoint();
		if (value != codepoint) {
			throw new IncorrectlyCoded(value, codepoint);
		} else {
			return codepoint;
		}
	}

	/**
	 *
	 * various expect string methods
	 *
	 */

	public String expectString() {
		TextDecoder decoder = (TextDecoder)decoderStack.peek();
		CoderResult r = decoder.decode(false);
		if (r.isEscaped()) {
			r.length();
		}
	}

	public String expectStringOfLength(int length) {
		TextDecoder decoder = (TextDecoder)decoderStack.peek();
		decoder.countdown(length);
		decoder.decode(false);
		char[] chars = new char[length];
		activeCharBuffer.get(chars);
		return chars.toString();
	}

	public String expectStringOfValue(String value) throws IncorrectlyCoded {
		String s = expectString();
		if (s != value) {
			throw new IncorrectlyCoded(value, s);
		} else {
			return s;
		}
	}

	public SelfCoding expect(Class<? extends SelfCoding> expected) {
		return expect(expected, defaultTokenizers);
	}

	public SelfCoding expect(Class<? extends SelfCoding> expected, int endingWith) {
		return expect(expected, new int[] { endingWith });
	}

	public SelfCoding expect(Class<? extends SelfCoding> expected,
							 int endingWith, SelfCoding preCoded) {
		this.preCoded.add(preCoded);
		return expect(expected, new int[] { endingWith });
	}

	public SelfCoding expect(Class<? extends SelfCoding> expected,
							 int endingWith, SelfCoding[] preCoded) {
		this.preCoded.addAll(Arrays.asList(preCoded));
		return expect(expected, new int[] { endingWith });
	}

	public SelfCoding expect(Class<? extends SelfCoding> expected, int[] endingWith) {

//		CodingToken token = new CodingToken()
		textTokenizers = endingWith;

		SelfCoding result = null;
		try {
			Method decodeMethod = expected.getMethod("decode",
					new Class[] {DecodeAdapter.class});
			result = (SelfCoding)decodeMethod.invoke(this);
		} catch (NoSuchMethodException nsme) {
			//TODO: throw new proper exception here
			nsme.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return result;

	}


	public int expectInteger(int radix) {
		CoderResult coderResult = decoderStack.peek().decode(false);
		int i = coderResult.


	}

	public void shiftIn(Codec codec) {

	}

	public void shiftOut(Codec codec) {

	}

	@Data
	@AllArgsConstructor
	private class Expecting {
		int sourceIndex;
		Class<? extends SelfCoding> expectingClass;
		int[] escapes;
	}

	class DecodeHandler {
		Decoder decoder;
		int sourceIndex;

		public CoderResult shouldStop(int lastDecoded) {

		}
	}

	
}