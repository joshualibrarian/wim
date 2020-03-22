
# Item
Items are the base unit of organization in the WIM.  An item is fundamentally a (unicode) text-based metadata wrapper format of four different elements: [manifests](manifest) which define an item's name and describes which other components are included, [content](content) components contain the actual data content of the item (be it a document, an application, or anything in-between), [relations](relations) describe a relationship to any item or part of an item or it's WIM metadata, and [summaries](summary) are generated from items and used to convey an overview of what components an item has.

As users interact with an item, it can gain many *relations*, both shared among all these users and private for any one of them or others will be created.  As large items are distributed, *summaries* will be created, to be transmitted in lieu of the bulk of item itself to give users information to decide if they want to download such larger items.

Through this document, a consistent syntax will be used.  Each token is separated in brackets for readability, but those brackets *are not* intended to be included in the actual item.  White space *is* included in the item as shown.  Each component of an item is terminated by a carriage return, shown as `CR`, though the system is tolerant of an extra `LF` preceding the proper `CR`:

- Angle brackets (`<>`) denote required tokens.
- Square brackets (`[]`) denote optional tokens.
- A plus sign (`+`) following a token denotes that multiple iterations of it are allowed.


Though this is all very valid unicode and mostly very readable text, many editors and terminals may balk at the generous use of control characters.  That's okay, these items aren't really meant to be read by regular users, and will generally be serialized when either sending to someone, or exporting from the system into a text file.  However, for developers, this text-based system should be very accessible for debugging in hex editors or slightly armored text editors.  A simple command line viewing utility (in the style of `less`) will be provided with the SDK.  

## Tokens
Several different kinds of token are common to each of the four components of an item.

### Addresses
Most of the items in this system are created by another item.  Only items that are *signers* can create other items, meaning they have cryptographic keypairs.  All users are signers, as well as hosts.  Items can support most any kind of address space, and there are two primary types.  One type encodes its creator's (signer's) address in its own address, and the other does not.  Generally, the former is preferable as it aids in avoiding namespace collisions, however either are possible.  If the signer's address is encoded into the item address, then `SIGNER` can be omitted from any of these tokens, even while the `~` and `SIGNATURE` remain.  If both the `SIGNER` and `SIGNATURE` are missing, the `~` still remains acting as a marker showing the signature was omitted and implying that the creator's address is encoded in the item address.  Even if the item is unsigned, the signer's address should still be present either encoded into the address, or preceding the `~`.

### Timestamp
Every component of an item can, and generally should, have a timestamp of the moment it was created.  This is prefixed with a `FILE_SEPARATOR` (`FS`, ) control character, immediately followed by the timestamp.

### Security

The security parameter is denoted with the `GROUP_SEPARATOR` (`GS`) character.

### Signature

Signatures throughout the item format are denoted with the `RECORD_SEPARATOR` (`RS`) character.

## Manifest

	<ADDRESS><DC1>[CREATOR] [<FS>INDEX] [<GS>SECURITY] [<RS>TIMESTAMP] <SPACE SEPARATED LIST> [[SIGNER]<RS>[SIGNATURE]]+<CR>
	
These tokens are each recognized by their place in the manifest, how they are formatted, and the use of three marker symbols (`!`, `#`, and `~`).  The required `<SPACE SEPARATED LIST>` can contain the identifier of any part of this item other than a manifest, denoted each by the format of its (relative, omitting the address part) reference, each with a unique character.  The signature validates a secure hash of the manifest itself, with each item part reference replaced with the actual content of that part.

## Summary

	<ADDRESS><DC2> [!TIMESTAMP] [$SECURITY] <ROOT_CONTENT_TYPE> <REFERENCE<VT>TOTAL>+ [[SIGNER]~[SIGNATURE]]+<CR>

## Content:

The content blocks are where all of the actual data that an item represents is stored, or at least referenced from.  If the content is included, it is always prefixed by it's size, and contained with `<SO>` and `<SI>` as described above.  In the case of content, a type is also required, this would generally be either a MIME type, or a Java class.  Content blocks each have a name, unique to this item.  A content name can have any characters excepting control characters and white space.  They may also have a short description blurb for further information.  If the content is encoded directly into the content block, then it is embedded at the end as shown above for binary content:

	[ADDRESS]<DC3>[CREATOR] <CONTENT_NAME> [!TIMESTAMP] [$SECURITY] [<STX>DESCRIPTION_BLURB<ETX>] [[SIGNER]~[SIGNATURE]]+ <TYPE> <SIZE><SO><ITEM_CONTENT><SI><CR>
	
If the actual content of the content block is actually stored elsewhere, then that location replaces the `SIZE` token omitting the control characters, giving a local filesystem path or URL to the referenced data:

	[ADDRESS]<DC3>[CREATOR] <CONTENT_NAME> [!TIMESTAMP] [$SECURITY] [<STX>DESCRIPTION_BLURB<ETX>] [[SIGNER]~[SIGNATURE]]+ <TYPE> <DATA_PATH><CR>
	
Though they can be anything, content names can look like standard filesystem paths, even mirroring them for imported data.  There are some reserved content names, with one in particular, the *root content*, denoted with a familiar solitary `/`.  This means that this content block holds the primary content of the item.  If this item is a document, the type would be a MIME type and the data would be a document of said type.  If the item is represented by a particular class, that class name is used and the root content holds the item state itself, as clarified by each item subclass's developer.
	
## Relation:

Relations are the heart of this system, they are the point of it.  We define this thing called an item so it can be addressed and related to.  A relation has several primary components in addition to those housekeeping ones common to all components of an item: a *subject*, and *object* and some number of *sememes*. Each of these tokens are *references to items*, meaning they have an item address and optionally some additional accompanying information.  The object may be a *literal* field rather than a reference and is entirely optional.  The full set of components for a relation are: 

	[ADDRESS]<DC4><RELATION_CREATOR> <#INDEX> [!TIMESTAMP] [$SECURITY] <VERB_OR_ADJECTIVE_REFERENCE>+ [| OBJECT_REFERENCE] [[SIGNER]~[SIGNATURE]]+<CR>

A sememe is a particular type of item, and they must be used in order to create a relation.  There will be a common initial set of these items, many of which to be distributed with the system.  Though users (or more likely, developers) will be able to add sememes to the system, it is intended that if a relevant sememe already exists, you should use it.  A sememe represents one *meaning*, one concept as it would be expressed by a word or words in a language.  Contained within a sememe item is all of the words that may express it, indexed by language and their types, which are indexed by the WIM library.  This is how items are found in the system, by this *word lookup* mechanism, and so when entering queries, these sememes will come up to be used by everyone.  These items are the shared indexes of the WIM and the initial distributed set will include the base 117597 "content words" (nouns, verbs, adjectives and adverbs), but also all the various prepositions and pronouns and such.  When a relation is created, each item involved is updated, *excepting "non-content" sememes*.  So the object, the subject, and all the significant connecting words will get a copy of each relation.

An example of a simple relation will apply an attribute to the subject item.  Let's say the item in question is a product in your online store, a shirt.  You want users to know that this shirt comes in red, you want them to be able to find it when they are searching for red shirts.  So you would apply a simple relation to it letting us know this item represents something that is red, something like (shown here omitting timestamp, security, and signer:

	@store.com/shirts/354337<DC4>@store.com #24 @wim.zone/color.red

You can see that `@store.com` both created the item that is the *subject* of this relation and also the relation itself.  The attribute (red) however came from the shared index that is red.  Of course every copy of every sememe item doesn't have every single relation that everything has to red!  Using the security parameter, and the WIMs distributed trust network facility, users that create items can use the *security* parameter to specify how widely into the world a component of an item should be allowed.  For public items, on which these components will be quite numerous, will be indexed by those who are interested in the indexed part, and others will index others.  Through the WIM protocol, hosts may query each other to expand the range of users searches, and each query is configurable as such.  See the documents on networking and querying for more details.  

A more involved relation may be a review on that shirt.  Unlike on most comment systems, in the WIM, you cannot add *only* a comment, it has to be connected with at least one meaning, fetched by a word.  Perhaps that word is: ugly, hideous, disgusting, or gross?  These words have similar meanings, and these relationships between words are encoded in the WIM with relations, so they can be grouped by similar kind.  Your word will resolve to a list of meanings (or a single) which you will choose from.  Each word of a relation will resolve to a meaning, then the object must be indicated specifically, to provide clarity, since a relation may have no object and any number of relations.  A comment as such might look like this:

	@store.com/shirts/354337<DC4>someuser@somesite.net #2 @wim.zone/adj.flimsy | <STX>I bought this shirt and it fell apart in weeks!<ETX> ~
	
Note the use of the *start of text* and *end of text* control characters, which take only a single byte and cannot be confused with anything else, are used throughout the WIM when a text literal is to be embedded as a token in either an item or in the network protocol.  Inside this block, any single unicode character (see the unescaped single quote) is permitted, and even the <ETX> character itself *can* (but why would you want to?) be embedded with the use of <ESC>, however that is the only character that can be escaped.  Any other unicode character can be entered directly.  Again, all the optional portions of the relation have been omitted for clarity, so in the wild it would likely have all of those parts.  The object, properly marked with a `|`, is a literal in this case, but could otherwise have been another item of any kind.  The object of a relation may be another relation, either on this object or another, by using the same *reference syntax* as described in the [addressing](addressing) document.










	

