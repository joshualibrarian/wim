#Text Encodings

Throughout the WIM, in the components of items themselves and in the network protocol, all complete unicode encodings are supported, and the same patterns are used to specify encodings, and even escape the encoding or shift into another.  Supported encodings are:

* UTF-8 (default)
* UTF-16le
* UTF-16be
* UTF-32le
* UTF-32be
* UTF-7

At the beginning of each file containing items or post-initiation communication of the protocol, a default of  UTF-8 encoding will be assumed  *unless* a `BYTE_ORDER_MARK` (`U+FEFF`) in any supported encoding is found at the beginning of the stream.  If the encoding that follows is a valid UTF encoding, then the `BOM` will indicate what encoding that is in only between two and four bytes of data and then that encoding will be used.  If the initial encoding is UTF-8, the `BOM` is not required, but it is allowed.  Any other encoding must be specified by beginning the stream with that character.

	this is UTF-8 text, because no BOM was specified
	<BOM>this text begins with a BOM, which signifies it's encoding between any UTF
	
If any other encodings are to be used throughout the WIM, they can be transitioned into by using the `SHIFT_OUT` character.  This character signifies that we are "shifting out" of the current encoding and into a different one.  If no other information is provided, then another `BOM` is expected immediately following the `SO` to indicate which other UTF encoding we are shifting into.  Of course, after proceeding in the new encoding for however long, a following `SHIFT_IN` character will shift us back into the previous encoding, whatever that had been.

	this stream is defaulted to UTF-8 because it started with no BOM but we can shift out<SO><BOM>into another encoding<SI> and then back to UTF-8
	<BOM>this utf16be text needed to be identified with the preceding BOM but <SI>here no BOM because UTF-8 and then<SI>we are returned to UTF-16BE for the duration
	
You cannot shift out to the same encoding you are already using, and unspecified with no BOM always means UTF-8.  The following are *illegal*:

	defaulting to UTF-8 because no BOM and then <SO>defaulting to the same again because no BOM, so illegal<SI>
	<BOM>this is UTF-32BE text here, and we'll do something bad <SO><BOM>like try to shift out into more UTF-32BE text<SI>

Alternatively to specifying a text encoding with the `BYTE_ORDER_MARK` character, it can be explicitly specified *preceding* the `SHIFT_OUT` in plain text (of the current encoding, not that being shifted into) and that, being clearly preceded by a whitespace (note that the space is emphasized by use of the "␣" character) or other word break character.  For example:

	some preceding text in default encoding␣utf16le<SO>no byte order mark is needed, this text can begin immediately in the specified encoding<SI>
	  
If an encoding sensitive to byte order is specified, but not specified fully enough to indicate the byte order, then the `BOM` is still required, to indicate byte order:

	initial text␣utf32<SO><BOM>that character is still required, since we still don't know big-endian or little-endian<SI>
	UTF-16<SO><BOM>again, still required to indicate byte order<SI>


# Binary Data

## Encoding

In certain parts of WIM items, such as signatures on item components, binary data may be embedded.  This can be done in either of several ways, all of which use the `BEL` (0x0007) control character.  If binary data is to be encoded as text, such as with `Base64`, `Base65536`, or `Ecoji`, this is specified *before* the `BEL`, with the content proceeding after the `BEL`, and then followed immediately by whitespace.

	␣Base64<BEL>YWJjMTIzIT8kKiYoKSctPUB+␣
	␣Base65536<BEL>𤇃𢊻𤄻嶜𤄋𤇁𡊻𤄛𤆬𠲻𤆻𠆜𢮻𤆻ꊌ𢪻𤆻邌𤆻𤊻𤅋␣

Supported encodings, particularly those appropriate ones with long names (like "Base65536") will have abbreviated names.  All "Base" encodings will be able to be represendted by their respective unicode codepoint.  So, `Base65536` can be shortened to the single `𐀀` and `Base64` becomes simply `d`:

	␣d<BEL>YWJjMTIzIT8kKiYoKSctPUB+␣
	␣𐀀<BEL>𤇃𢊻𤄻嶜𤄋𤇁𡊻𤄛𤆬𠲻𤆻𠆜𢮻𤆻ꊌ𢪻𤆻邌𤆻𤊻𤅋␣
	␣耀<BEL>怗膹䩈㭴䂊䫁輪黔␣

## Referencing

In the above cases, we are embedding encoded binary data into unicode and displaying it in the current encoding scheme, marked only with the `BEL` character.  With a slightly different syntax, the `BEL` control character can also be used to signify data elsewhere being linked to, such as in the content of an item, a large data file may be externalized and referred to in the content using the following syntax.  Note the `␣` characters is used only for emphasis that this token must be surrounded by whitespace to be valid.  To allow absolutely any characters to appear in that string (such as spaces in a filename), the `START_OF_TEXT` (`STX`) and `END_OF_TEXT` (`ETX`) characters can be used as quotes, surrounding the entire path:

	␣some/relative/file/path<BEL>␣
	␣/some/absolute/file/path<BEL>␣
	␣on\Windows\it\mightlook\this.way<BEL>␣
	␣<STX>if/some/filename/simply MUST have/spaces/in/them/they CAN use these as "quotes" like this/<ETX><BEL>␣
	␣http://urls.are.also/permitted_here<BEL>␣
	␣ftp://if.the.protocol/is/supported<BEL>␣
	
In the incredibly odd case that you have a file location that just HAS to have that VERY `END_OF_TEXT` character in it, you can escape the character with the `ESC` (U+001B) character:

	␣<STX>/simply MUST have/spaces/AND an end of text char <ESC><ETX>TOO!which doesn't end the path/because it's escaped<ETX><BEL>␣

## Embedding

Binary data can also be embedded directly into an item, without being encoded into a form compatable with unicode as above.  In such cases like the content of items, binary data can be stored directly in-line in the item.  This is done using a combination of the `BEL` and the `SHIFT_OUT`/`SHIFT_IN` characters by specifying the exact size of the data as follows:

	<BEL>9383<SO>pretend I am exactly 9383 bytes of binary data<SI>
	<BEL>0x9fc3<SO>hexadecimal numbers can be spcified using the "0x" prefix, again imagine exactly 0x9fc3 bytes of binary data<SI>

Potentially, other number bases could be represented by other "0x" type codes, such as "0z" or "0q", giving a much more efficient representation that is compatible with unicode.