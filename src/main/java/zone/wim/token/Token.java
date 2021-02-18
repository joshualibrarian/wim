package zone.wim.token;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.jdo.annotations.EmbeddedOnly;

import zone.wim.codec.DecodeAdapter;
import zone.wim.exception.*;
import zone.wim.token.TokenException.*;
import io.github.classgraph.*;

/**
 * 
 * This is the root interface for a pattern of classes that implement *their own* 
 * encoding and decoding.  This keeps all code that relates to a given class inside that class.  
 * When processing encoded data (either in a text encoding or 
 * some binary one), an `EncodingAdapter` or `DecodingAdapter` is used and passed around 
 * from processor to processor as additional instances are encoded.  In addition to the regular 
 * abstract `encode` function, implementing this class requires creating a static `decode` 
 * function which returns an instance of that class.
 * 
 * When you use the decode functionality of a Token, you call the `decode` static function with 
 * the `DecodeAdapter` currently positioned where you expect such data to be located.  The `decode` 
 * functions of abstract `Token` classes are used to parse data streams to determine which subclass 
 * should be invoked to decode it.
 * 
 * This is the root interface that acts as a local repository of text identification code.  
 * It can be easily plugged into simply by extending some descendant of `Token` and making 
 * sure the class is accessible to the running application.
 * 
 * @author joshua
 *
 */
@EmbeddedOnly
public interface Token {
	
	public static Logger LOGGER = Logger.getLogger(Token.class.getCanonicalName());
		
	/**
	 * 
	 * @param adapter
	 * @return
	 */
	public static Token decode(DecodeAdapter adapter) {
		
	}
	
	public static Token decode(DecodeAdapter adapter, Class<? extends Token> type) {
		
		
		
	}
	
	/**
	 * Java really should just have abstract static methods!
	 * And abstract constructor declarations while we're at it.

	 * @param tokenText
	 * @return
	 * @throws Throwable
	 */
	public static Token decode(String tokenText) throws Exception {
		return parse(tokenText, Token.class);
	}
	
	public static Token parse(String tokenText, Class<? extends Token> type) throws Exception {
		// TODO we need to be caching these classes, and possibly pre-ordering the first several
		ClassInfoList tokenTypes = new ClassGraph().enableClassInfo()
			.scan().getClassesImplementing(type.getCanonicalName());
		
		List<Token> matchingTokens = new ArrayList<>();
		for (ClassInfo classInfo : tokenTypes) {
			try {
				@SuppressWarnings("unchecked")
				Class<? extends Token> clazz = (Class<? extends Token>)classInfo.loadClass();
				if (clazz.isInterface()) {
					continue;
				}
				Method method = clazz.getMethod("decode", String.class);
				Token token = clazz.cast(method.invoke(null, tokenText));
				if (clazz.isInstance(token)) {
					matchingTokens.add(token);
					continue;
				}
			} catch (NoSuchMethodException e) {
				throw new MustImplementStaticMethod(e);
			} catch (InvocationTargetException e) {
				continue;
			} 
		}
		
		if (matchingTokens.isEmpty()) {
			throw new Unknown(tokenText);
		} else if (matchingTokens.size() > 1) {
			// TODO: here, the authors of the token class in question should be reported to, 
			// TODO: so that their parsing code may be improved if necessary
			throw new Ambiguous(tokenText, matchingTokens);
		}
		
		return matchingTokens.get(0);
	}
	
	public String getText();
	public void setText(String text) throws Exception;
	
}
