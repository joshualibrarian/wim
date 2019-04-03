
# Item
Items are the fundamental unit of organization in this sytem.  An item is fundamentally a metadata format, a (mostly) text-based wrapper.  The item, as contained in a file, or streaming over a network, surrounds the actual user data in sections called *content blocks*, which can contain any kind of data (or reference its actual location elsewhere).  Another part of the item, the *manifest*, identifies this file as an item, giving it an address, and should begin the item.  As users interact with an item, it can gain many *relations*, both shared among all these users and private for any one of them or others will be created.  As large items are distributed, *summaries* will be created, to be transmitted in leiu of the bulk of item itself to give users information to decide if they want to download such larger items.

Through this document, a consistent syntax will be used.  Each token is separated in brackets for readability, but those brackets *are not* intended to be included in the actual item.  White space *is* included in the item as shown.  Each component of an item is terminated by a carriage return, shown as `CR`, though the system is tolerant of an extra `LF` preceeding the proper `CR`:

- Angle brackets (`<>`) denote required tokens.
- Square brackets (`[]`) denote optional tokens.
- A plus sign (`+`) following a token denotes that multiple iterations of it are allowed.

Items may be encoded in any unicode encoding which allows for the entire range (UTF-8, UTF-16 or UTF-32).  We say mostly text, because binary data can be contained in this file by making use of a simple pattern:

	<BASE_10_SIZE><SO><BINARY_DATA><SI>

The unicode (and ASCII) code points of for *shift out* (`SO`, U+000E) and *shift in* (`SI`, U+000F) are rarely-used, can be encoded in a single byte (in UTF-8), and are perfect for this use.  The text encoded (decimal) size (in bytes) of the binary data is given as a prefix before `SHIFT_OUT`, avoiding any accidental endings in the binary data.  If the size prefix is missing, this implies that the binary data will continue until the stream ends, though should still be followed by a `SHIFT_OUT`.  Binary data used in this way is only allowed in certain parts of the item.  These parts of the item are the content and signatures.

A similar format may also be used to specify other encodings than the default of UTF-8, with *encoding* meaning the text name of the encoding (ie, `utf16le`).  This format utilizes the *start of text* and *end of text* control characters (`STX` and `ETX`):

	<ENCODING><STX><ENCODED_TEXT_DATA><ETX>

## Tokens
Several different kinds of token are common to each of the four components of an item.

### Addresses
Most of the items in this system are created by another item.  Only items that are *signers* can create other items, meaning they have cryptographic keypairs.  All users are signers, as well as hosts.  Items can support most any kind of address space, and there are two primary types.  One type encodes its creator's (signer's) address in its own address, and the other does not.  Generally, the former is preferable as it aids in avoiding namespace collisions, however either are possible.  If the signer's address is encoded into the item address, then `SIGNER` can be omitted from any of these tokens, even while the `~` and `SIGNATURE` remain.  If both the `SIGNER` and `SIGNATURE` are missing, the `~` still remains acting as a marker showing the signature was omitted and implying that the creator's address is encoded in the item address.  Even if the item is unsigned, the signer's address should still be present either encoded into the address, or preceding the `~`.

### Timestamp
Every component of an item can, and generally should, have a timestamp of the moment it was created.  This is prefixed with an exclamation point (`!`) character and immediately followed by the timestamp.

### Security

### Signature

### Termination

An item should be terminated by a File Separator (`<FS>`, U+001C) on the bottom line by itself.  This ensures that during transmision or storage we are aware if some data is lost.
  
 
## Manifest

	<ADDRESS><DC1> [!TIMESTAMP] [$SECURITY] <SPACE SEPARATED LIST> [[SIGNER]~[SIGNATURE]]+<CR>
	
These tokens are each recognized by their place in the manifest, how they are formatted, and the use of three marker symbols (`!`, `#`, and `~`).  The required `<SPACE SEPARATED LIST>` can contain the identifier of any part of this item other than a manifest, denoted each by the format of its (relative, omitting the address part) reference, each with a unique character.  The signature validates a secure hash of the manifest itself, with each item part reference replaced with the actual content of that part.

## Summary

	<ADDRESS><DC2> [!TIMESTAMP] [$SECURITY] <ROOT_CONTENT_TYPE> <REFERENCE :TOTAL>+ [[SIGNER]~[SIGNATURE]]+<CR>

## Content:

The content blocks are where all of the actual data that an item represents is stored, or at least referenced from.  If the content is included, it is always prefixed by it's size, and contained with `<SO>` and `<SI>` as described above.  In the case of content, a type is also required, this would generally be either a MIME type, or a Java class.  Content blocks each have a name, unique to this item.  A content name can have any characters excepting control characters and white space.  They may also have a short description blurb for further information.  If the content is encoded directly into the content block, then it is embedded at the end as shown above for binary content:

	[ADDRESS]<DC3>[CREATOR] <CONTENT_NAME> [!TIMESTAMP] [$SECURITY] [<STX>DESCRIPTION_BLURB<ETX>] [[SIGNER]~[SIGNATURE]]+ <TYPE> <SIZE><SO><ITEM_CONTENT><SI><CR>
	
If the actual content of the content block is actually stored elsewhere, then that location replaces the `SIZE` token omitting the control characters, giving a local filesystem path or URL to the referenced data:

	[ADDRESS]<DC3>[CREATOR] <CONTENT_NAME> [!TIMESTAMP] [$SECURITY] [<STX>DESCRIPTION_BLURB<ETX>] [[SIGNER]~[SIGNATURE]]+ <TYPE> <DATA_PATH><CR>
	
Though they can be anything, content names can look like standard filesystem paths, even mirroring them for imported data.  There are some reserved content names, with one in particular, the *root content*, denoted with a familiar solitary `/`.  This means that this content block holds the primary content of the item.  If this item is a document, the type would be a MIME type and the data would be a document of said type.  If the item is represented by a particular class, that classname is used and the root content holds the item state itself, as clarified by each item subclass's developer.
	
## Relation:


	[ADDRESS]<DC4><RELATION_CREATOR> <INDEX> [!TIMESTAMP] [$SECURITY] <VERB_OR_ADJECTIVE_REFERENCE>+ [| OBJECT_REFERENCE] [[SIGNER]~[SIGNATURE]]+<CR>



	

