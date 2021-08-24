package zone.wim.coding;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import zone.wim.exception.MustImplementStaticMethod;
import zone.wim.token.SelfCodingException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


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
public interface SelfCoding {

	// this is what it WOULD look like if this were allowed,
	// requiring at compile time that every implementer of this 
	// interface must implement this static function, which 
	// is not inherited:
//	abstract static SelfCoding decode(DecodeAdapter adapter);


	public static SelfCoding decode(DecodeAdapter adapter) throws Exception {
		SelfCoding result = null;

		Class<? extends SelfCoding> expectingType = adapter.expecting();

		// TODO we need to be caching these classes, and possibly pre-ordering the first several
		ClassInfoList selfCodingTypes = new ClassGraph().enableClassInfo()
				.scan().getClassesImplementing(expectingType.getCanonicalName());

		return result;
	}

	public static SelfCoding decode(String tokenText, Class<? extends SelfCoding> type) throws Exception {
		// TODO we need to be caching these classes, and possibly pre-ordering the first several
		ClassInfoList selfCodingTypes = new ClassGraph().enableClassInfo()
				.scan().getClassesImplementing(type.getCanonicalName());

		List<SelfCoding> matchingTypes = new ArrayList<>();
		for (ClassInfo classInfo : selfCodingTypes) {
			try {
				@SuppressWarnings("unchecked")
				Class<? extends SelfCoding> clazz = (Class<? extends SelfCoding>)classInfo.loadClass();
				if (clazz.isInterface()) {
					continue;
				}
				Method method = clazz.getMethod("decode", String.class);
				SelfCoding token = clazz.cast(method.invoke(null, tokenText));
				if (clazz.isInstance(token)) {
					matchingTypes.add(token);
					continue;
				}
			} catch (NoSuchMethodException e) {
				throw new MustImplementStaticMethod(e);
			} catch (InvocationTargetException e) {
				continue;
			}
		}

		if (matchingTypes.isEmpty()) {
			throw new SelfCodingException.Unknown(tokenText);
		} else if (matchingTypes.size() > 1) {
			// TODO: here, the authors of the token class in question should be reported to,
			// TODO: so that their parsing code may be improved if necessary
			throw new SelfCodingException.Ambiguous(tokenText, matchingTypes);
		}

		return matchingTypes.get(0);
	}
	
	void encode(EncodeAdapter adapter);
}
