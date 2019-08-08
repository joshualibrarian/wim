package zone.wim.token;

import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.annotations.EmbeddedOnly;
import javax.persistence.Embeddable;

import zone.wim.exception.MustImplementStaticMethod;
import zone.wim.exception.TokenException.*;
import io.github.classgraph.*;

@Embeddable
@EmbeddedOnly
public interface Token {
	public static Logger LOGGER = Logger.getLogger(Token.class.getCanonicalName());
	
	/**
	 * Java really should just have abstract static methods!
	 * And abstract constructor declarations while we're at it.

	 * @param tokenText
	 * @return
	 * @throws Throwable
	 */
	public static List<Token> parse(String tokenText) throws Exception {
		return parse(tokenText, Token.class);
	}
	
	public static List<Token> parse(String tokenText, Class<? extends Token> type) throws Exception {
//		if (type == null) {
//			type = Token.class;
//		}

		// TODO we need to be caching these classes, and possibly pre-ordering the first several
		ClassInfoList tokenTypes = new ClassGraph().enableClassInfo().scan()
			.getClassesImplementing(type.getCanonicalName());
		
		List<Token> matchingTokens = new ArrayList<>();
		for (ClassInfo classInfo : tokenTypes) {
			try {
				@SuppressWarnings("unchecked")
				Class<? extends Token> clazz = (Class<? extends Token>)classInfo.loadClass();
				if (clazz.isInterface()) {
					continue; 
				}
				Method method = clazz.getMethod("parse", String.class);
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
		}
		
		return matchingTokens;
	}
	
	public abstract String get();
	
}
