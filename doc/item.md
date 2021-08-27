
# Item
Items are the base unit of organization in the WIM.  An item is fundamentally a (unicode) text-based metadata wrapper format.  There are four different types of component that make up an item: 

* [Manifests](manifest) are exactly what they sound like, they define an item's name and describes which other components are included.  Without a manifest, there can be no actual item.

* [Content](content) components contain the actual data content of the item (be it a document, an application, or anything in-between).

* [Relations](relations) describe a relationship to any item or part of an item or it's WIM metadata.  An item will gain relations throughout it's life as users interact with it.

* [Summaries](summary) are generated from items and used to convey an overview of what components an item has.  They can be sent in lieu of actual items to act as a sort of preview, to decide if items are to be fetched.

## Tokens

Each of the four different *item components* shares a similar format and several common parts.  They are all stored as a list of tokens, separated by spaces (`U+0020`, specifically), some with particular control characters denoting them.  The tokens which are common to all components, and the basic format of an item component is:

	[ITEM ADDRESSES]+<COMPONENT_IDENTIFYING_CHAR> <CREATOR_ADDRESS><<FS>INDEX> <<US>VERSION> [<GS>SECURITY] [<RS>TIMESTAMP] <ITEM_SPECIFIC_TOKENS>+ [[SIGNER]<US><SIGNATURE>]+<CR>
	
As you can see, each component can begin with the address of that component followed by a required `<COMPONENT_IDENTIFYING_CHAR>`, each a different control character identifying one of the four types.  The `INDEX`, `SECURITY`, `TIMESTAMP`, and `SIGNATURE` tokens each have their own identifying control characters and syntax as well, as do most of the tokens specific to each item component.  Each is addressed individually:

### Item Address

Each item component begins with the address of that item.  On a *manifest* this is always required, however, this can be excluded on other item components if stored in the context of an entire item.  So, if the item is stored in a file which includes a manifest to denote the address of the item, then the item address can be omitted from further components to save space.  These components can only be expressed this way when in relation to a complete item.  In another context, like being transmitted from one host to another without an enclosing item, then the address must always be included.

### Component Identifying Character

Each of the four different types of components has their own unique control character which identifies it.  These each are one of the four `DEVICE_CONTROL` characters, see the details of each component below for specifics.

### Creator Address

This is the address of the user that created this particular component of the item.  If included, it follows the component identifying char immediately, with no preceding space.  For some component types, the creator of this component may be different from the user that created the item.  The creator address may be omitted only if the creator of this component is the same as the creator of the item, and that creator's address can be extracted from the item address (again, see [addressing](addressing.md)).

### Index


### Security

The security parameter is denoted by the preceding `GROUP_SEPARATOR` (`GS`) character.

### Timestamp

Every component of an item can, and generally should, have a timestamp of the moment it was created.  This is prefixed with a `FILE_SEPARATOR` (`FS`, ) control character, immediately followed by the timestamp.  The default format for the timestamp is in plain text format, with all padded numbers in the format: `yyyymmddHHmmssZ` (or subsets of this).  Other formats can be determined and used later by using some kind of prefix indicator before the `FS` character to describe the format, ideally in only a single byte.

### Signature

Signatures throughout the item format are denoted with the `RECORD_SEPARATOR` (`RS`) character, which is always followed by the actual signature itself.  The signature can be encoded in a number of ways, as described in the [encoding](encoding.md) document.  The signer, which precedes the control character, *may* be omitted only if the address of the signer is the creator, whose address is included, or is otherwise inferred from the item address (see the accompanying [addressing](addressing.md) document).

## Manifest

	<ADDRESS><DC1> [CREATOR][<FS>INDEX] [<GS>SECURITY] [<RS>TIMESTAMP] <SPACE_SEPARATED_LIST_OF_ITEM_COMPONENTS> [[SIGNER]<US><SIGNATURE>]+<CR>

## Content

The content blocks are where all of the actual data that an item represents is stored, or at least referenced from.  If the content is included, it is always prefixed by it's size, and contained with `<SO>` and `<SI>` as described above.  In the case of content, a type is also required, this would generally be either a MIME type, or a Java class.  Content blocks each have a name, unique to this item.  A content name can have any characters excepting control characters and white space.  They may also have a short description blurb for further information.  If the content is encoded directly into the content block, then it is embedded at the end as shown above for binary content:

	[ADDRESS]<DC3> [CREATOR][<FS>CONTENT_NAME] [<GS>SECURITY] [<RS>TIMESTAMP] [<STX>DESCRIPTION_BLURB<ETX>] [[SIGNER]~[SIGNATURE]]+ <TYPE> <SIZE><SO><ITEM_CONTENT><SI><CR>
	
If the actual content of the content block is actually stored elsewhere, then that location replaces the `SIZE` token omitting the control characters, giving a local filesystem path or URL to the referenced data:

	[ADDRESS]<DC3> [CREATOR][<FS>CONTENT_NAME] [<GS>SECURITY] [<RS>TIMESTAMP] [<STX>DESCRIPTION_BLURB<ETX>] [[SIGNER]~[SIGNATURE]]+ <TYPE> <DATA_PATH><CR>
	
The content name acts as the index for this particular content component within the item, which uses the same character as other components, `<FS>`, to denote the index.  In other components of an item, the index is often an integer, which is incremented from the last component of that type created by that user on that item.  
Though they can be anything, content names can look like standard filesystem paths, even mirroring them for imported data.  The *root content*, which holds the primary content of the item, is denoted by having no name whatsoever, but still inclusdes the `<FS>` character.  If this item is a document, the type would be a MIME type and the data would be a document of said type.  If the item is represented by a particular class, that class name is used and the root content holds the item state itself, as implemented by each item subclass' developer.
	
## Relation

Relations are the heart of this system, _they are the point of it_.  We define this thing called an item so it can be addressed and related to.  A relation has several primary components in addition to those housekeeping ones common to all components of an item: a *subject*, and *object* and some number of *sememes*. Each of these tokens are *references to items*, meaning they have an item address and optionally some additional accompanying information.  The object may be a *literal* field rather than a reference and is entirely optional.  The full set of components for a relation are: 

	[ITEM_ADDRESS]<DC4> <RELATION_CREATOR><FS><INDEX> [<GS>SECURITY] [<RS>TIMESTAMP] <VERB_OR_ADJECTIVE_REFERENCE>+ [<SOH>OBJECT_REFERENCE] [[SIGNER]<US>[SIGNATURE]]+<CR>

A sememe is a particular type of item, and they must be used in order to create a relation.  There will be a common initial set of these items, many of which to be distributed with the system.  Though users (or more likely, developers) will be able to add sememes to the system, it is intended that if a relevant sememe already exists, you should use it.  A sememe represents one *meaning*, one concept as it would be expressed by a word or words in a language.  Contained within a sememe item is all of the words that may express it, indexed by language and their types, which are indexed by the WIM library.  This is how items are found in the system, by this *word lookup* mechanism, and so when entering queries, these sememes will come up to be used by everyone.  These items are the shared indexes of the WIM and the initial distributed set will include the base 117597 "content words" (nouns, verbs, adjectives and adverbs), but also all the various prepositions and pronouns and such.  When a relation is created, each item involved is updated, *excepting "non-content" sememes*.  So the object, the subject, and all the significant connecting words will get a copy of each relation.

An example of a simple relation will apply an attribute to the subject item.  Let's say the item in question is a product in your online store, a shirt.  You want users to know that this shirt comes in red, you want them to be able to find it when they are searching for red shirts.  So you would apply a simple relation to it letting us know this item represents something that is red, something like (shown here omitting timestamp, security, and signature:

	@store.com/shirts/354337<DC4> @store.com<FS>24 <GS>pub @wim.zone/color.red

You can see that the signer `@store.com` both created the item that is the *subject* of this relation and also the relation itself.  The attribute (red) however came from the shared sememe item that is red.  Of course every copy of every sememe item doesn't have every single relation that everything has to red!  Using the security parameter, and the WIMs distributed trust network facility, users that create items can use the *security* parameter to specify how widely into the world a component of an item should be allowed.  For public items, on which these components will be quite numerous, will be indexed by those who are interested in the indexed part, and others will index others.  Through the WIM protocol, hosts may query each other to expand the range of users searches, and each query is configurable as such.  See the documents on networking and querying for more details.  

A more involved relation may be a review on that shirt.  Unlike on most comment systems, in the WIM, you cannot add *only* a comment, it has to be connected with at least one meaning, fetched by a word.  Perhaps that word is: ugly, hideous, disgusting, or gross?  These words have similar meanings, and these relationships between words are encoded in the WIM with relations, so they can be grouped by similar kind.  Your word will resolve to a list of meanings (or a single) which you will choose from.  Each word of a relation will resolve to a meaning, then the object must be indicated specifically, to provide clarity, since a relation may have no object and any number of relations.  A comment as such might look like this:

	@store.com/shirts/354337<DC4>someuser@somesite.net #2 @wim.zone/adj.flimsy <SOH><STX>I bought this shirt and it fell apart in weeks!<ETX> ~
	
Note the use of the *start of text* and *end of text* control characters, which take only a single byte and cannot be confused with anything else, are used throughout the WIM when a text literal is to be embedded as a token in either an item or in the network protocol.  Inside this block, any single unicode character (see the unescaped single quote) is permitted, and even the `<ETX>` character itself *can* (but why would you want to?) be embedded with the use of `<ESC>`, however that is the only character that can be escaped.  Any other unicode character can be entered directly.  Again, all the optional portions of the relation have been omitted for clarity, so in the wild it would likely have all of those parts.  The object, properly marked with a `|`, is a literal in this case, but could otherwise have been another item of any kind.  The object of a relation may be another relation, either on this object or another, by using the same *reference syntax* as described in the [addressing](addressing) document.


## Summary

	<ADDRESS><DC4>[CREATOR][<FS>INDEX] [<GS>SECURITY] [<RS>TIMESTAMP] <ROOT_CONTENT_TYPE> <REFERENCE<VT>TOTAL>+ [[SIGNER]<US><SIGNATURE>]+<CR>







	

