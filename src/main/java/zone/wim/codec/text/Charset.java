package zone.wim.codec.text;

//import java.nio.charset.Charset;

import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.charset.spi.CharsetProvider;
import java.util.Objects;

import zone.wim.codec.Codec;
import zone.wim.codec.EscapeCharsetProvider;

public abstract class Charset extends Codec implements Comparable<Charset> {
	
	public static Charset UTF_8 = forName("UTF-8");
	public static Charset UTF_16LE = forName("UTF-16");
	public static Charset UTF_16BE = forName("UTF-16");
	public static Charset UTF_32LE = forName("UTF-32");
	public static Charset UTF_32BE = forName("UTF-32");
	public static Charset UTF_7 = forName("UTF-7");
	
	// Cache of the most-recently-returned charsets,
    // along with the names that were used to find them
    //
    private static volatile Object[] cache1; // "Level 1" cache
    private static volatile Object[] cache2; // "Level 2" cache

    private static void cache(String charsetName, Charset cs) {
        cache2 = cache1;
        cache1 = new Object[] { charsetName, cs };
    }
    
    /* The standard set of charsets */
//    private static final CharsetProvider standardProvider
//        = new sun.nio.cs.StandardCharsets();

	/**
     * Returns a charset object for the named charset.
     *
     * @param  charsetName
     *         The name of the requested charset; may be either
     *         a canonical name or an alias
     *
     * @return  A charset object for the named charset
     *
     * @throws  IllegalCharsetNameException
     *          If the given charset name is illegal
     *
     * @throws  IllegalArgumentException
     *          If the given {@code charsetName} is null
     *
     * @throws  UnsupportedCharsetException
     *          If no support for the named charset is available
     *          in this instance of the Java virtual machine
     */
    public static Charset forName(String charsetName) {
        Charset cs = lookup(charsetName);
        if (cs != null)
            return cs;
        throw new UnsupportedCharsetException(charsetName);
    }

    private static Charset lookup(String charsetName) {
        if (charsetName == null)
            throw new IllegalArgumentException("Null charset name");
        Object[] a;
        if ((a = cache1) != null && charsetName.equals(a[0]))
            return (Charset)a[1];
        // We expect most programs to use one Charset repeatedly.
        // We convey a hint to this effect to the VM by putting the
        // level 1 cache miss code in a separate method.
        return lookup2(charsetName);
    }

    private static Charset lookup2(String charsetName) {
        Object[] a;
        if ((a = cache2) != null && charsetName.equals(a[0])) {
            cache2 = cache1;
            cache1 = a;
            return (Charset)a[1];
        }
        Charset cs;
        cs = EscapeCharsetProvider.charsetForName(charsetName);
//        if ((cs = standardProvider.charsetForName(charsetName)) != null ||
//            (cs = lookupExtendedCharset(charsetName))           != null ||
//            (cs = lookupViaProviders(charsetName))              != null)
//        {
//            cache(charsetName, cs);
//            return cs;
//        }

        /* Only need to check the name if we didn't find a charset for it */
//        checkName(charsetName);
        return null;
    }
	
	protected String name;
	protected String[] aliases;
	/**
     * Initializes a new charset with the given canonical name and alias
     * set.
     *
     * @param  canonicalName
     *         The canonical name of this charset
     *
     * @param  aliases
     *         An array of this charset's aliases, or null if it has no aliases
     *
     * @throws IllegalCharsetNameException
     *         If the canonical name or any of the aliases are illegal
     */
    protected Charset(String canonicalName, String[] aliases) {
//        String[] as = Objects.requireNonNullElse(aliases, zeroAliases);

        // Skip checks for the standard, built-in Charsets we always load
        // during initialization.
//        if (canonicalName != "ISO-8859-1"
//                && canonicalName != "US-ASCII"
//                && canonicalName != "UTF-8") {
//            checkName(canonicalName);
//            for (int i = 0; i < as.length; i++) {
//                checkName(as[i]);
//            }
//        }
    	// TODO: actually check that the name is valid here
        this.name = canonicalName;
        this.aliases = aliases;
    }
    
	
	
	public String name() {
		return name;
	}
	
	/**
     * Constructs a new decoder for this charset.
     *
     * @return  A new decoder for this charset
     */
//    public abstract CharsetDecoder newDecoder();

    /**
     * Constructs a new encoder for this charset.
     *
     * @return  A new encoder for this charset
     *
     * @throws  UnsupportedOperationException
     *          If this charset does not support encoding
     */
//    public abstract CharsetEncoder newEncoder();

    
    /**
     * Compares this charset to another.
     *
     * <p> Charsets are ordered by their canonical names, without regard to
     * case. </p>
     *
     * @param  that
     *         The charset to which this charset is to be compared
     *
     * @return A negative integer, zero, or a positive integer as this charset
     *         is less than, equal to, or greater than the specified charset
     */
    public final int compareTo(Charset that) {
        return (name().compareToIgnoreCase(that.name()));
    }
    
    /**
     * Computes a hashcode for this charset.
     *
     * @return  An integer hashcode
     */
    public final int hashCode() {
        return name().hashCode();
    }

    /**
     * Tells whether or not this object is equal to another.
     *
     * <p> Two charsets are equal if, and only if, they have the same canonical
     * names.  A charset is never equal to any other type of object.  </p>
     *
     * @return  {@code true} if, and only if, this charset is equal to the
     *          given object
     */
    public final boolean equals(Object ob) {
        if (!(ob instanceof Charset))
            return false;
        if (this == ob)
            return true;
        return name.equals(((Charset)ob).name());
    }

    /**
     * Returns a string describing this charset.
     *
     * @return  A string describing this charset
     */
    public final String toString() {
        return name();
    }

}
