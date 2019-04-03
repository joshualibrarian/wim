### Addressing

The uddb system is flexible enough to allow for almost *any* address space, and can be extended as such.  The only characters now allowed in an address are white spaces and control chacters.  Low order control characters (below U+0020) are used heavily in the item format and the protocol to avoid having reserved printable characters, since these codepoints are just lying around anyway and can be represented in a single byte (in the default of UTF-8).

The primary namespace of the uddb is based on the traditional email namespace, which is in turn based on the domain name system.  We are using that system as it is, taking over the role of email for messages sent as such, but adding a path to the end of that email, allowing each user to create arbitrary items inside their own personal namespace, in which everything is addressable by a unique address which is is prefixed by the address of its publisher, that user having control over their own namespace.  The user addressing system in uddb is intended to take over that of the current email address space.  The uddb operates on the *same ports* as an SMTP/MTA server, detecting actual such servers and automatically importing and exporting emails into simple message objects.  Every domain addressable address that is not a user is to some other kind of item (a document OR a program) and contains a *path*.  Paths can be completely arbitrary, can be generated automatically based on its type and content, or chosen by the user upon creation of an item.  Note that addresses, for the most part can contain any valid unicode character, excepting the specific use of `@` , `/` , and `.` with all other codepoints being valid.

	joe@funk.net
	@myserver.space
	@somestore.com/shirts.a357j3bl-pink
	sally@coolcompany.com/reports.budget.annual.2017
	🙇@this.that/music.dead.79.denver
	joshua@readingrainbow.space/chess.432

However, the system can support other namespaces and does so to manage *hosts*.  Each physical computer, with an IP address (including localhost) that the system communicates with gets an item generated to represent it.  These *host items* use the IP address namespace directly and are used to store key information, connection history, domain data, and anything else.

#### Users

The address itself will indicate some things about what kind of item this is, if it has a username before the `@` then it is of class `Account`, and if it begins with the `@` then it is a `Server`, and so if no path follows, then we have an item representing that user themself, and is the basis for communicating with them.

	joe@acme.com
	@github.net
	help@mycompany.com
	joe@whatever.domain.tld

#### Paths

Paths denote any item that is not a User.  The `/` character has no meaning in the path and can be included, as well as any other valid unicode codepoint.  The `.` character is used as the default separater.  The optional ending numberal on any non `User` item is used in the back-end by some `Item` subclasses to automatically create items, but when naming items manually, none is needed.

	joshualibrarian@github.com/libnet
	@foo.net/word.adj.zestilly
	account@server.ie.domain.tld/some.kind.of.path.4

#### Ranges

A certain part of an item can be addressed directly by appending a *range* onto an item address using the square-bracket `[]` syntax.  An item may have any number of *content*  portions, and each has its own name which can be referenced in the brackets optionally followed by a range or further specification of the reference:

	@foo.net/essay.34[fig.4]
	account@server.ie.domain.tld/some.kind.of.path.4[p.4-6]
	joshua@fee.com/my.cool.new.book[chapter.4:p32-45]
	me@you.we.net/my.cool.video[3:15-6:33]

Different kinds of content use different kinds of ranges, which are defined in the items that represent those file types.  When addressing a relation, that address builds on the address of the item that is the *"subject"* of the relation, and adding the creator of the relation and the id *unique to that user in this file*.  This mechanism can be used by Item subclass authors to communicate directly with their application in just about any way, passing data directly from one instance of that particular item to another.


#### Relations

Another way that addresses can refer to only a portion of an item is by referencing a certain *relation* of that item by using the `;` syntax followed by the unique identifier of the relation relative to the item.  Both range and relation cannot be present in one address, though a relation can refer to only a part of its subject such is not reflected in the address of the relation.

	@foo.net/essay.about.something;some@guy.who.commented#3
	@foo.net/essay.about.something;some@other.commenter#524
	@bobross.love;professor@fancy.edu#3[p.3-4]
	
Note that when a range is used after a relation address, it means that this reference refers to a part *of that relation*, which implies that there is some content (either a literal, an object, or both) associated with it, or there would be no part to refer to.
	
#### References 
 
A reference, meaning an address that refers to a specific location, to a user, or to an item, or to a relation of an item, or to a portion of a relation of an item, in addition to the preceeding elements, can include an "as entered" portion.  This ammendment, designated with the parentheses `()`, is used primarily to display to the user how this reference was actually entered (if it was entered via text that is, if it was entered via a mouseclick there may be no as entered).  Word that are synonyms can yield the same sememe (meaning) object that is used to do the actual linking, and the as entered text shows which of potentially many words (or phrases) was used to call up this item in this instance.  This data will additionally prove useful as the system matures to help make text processing better and better, so wherever possible, these references should be maintained.

The `Reference` class encapusates a reference, and is used extensively throughout the `Item` class, because if item variables were stored directly as `Item` objects, then we would be loading a whole lot more items into memory which we may not actually need as we're processing these and loading items off disk.  Using the `Reference`, we know what the address of the item is, so can load it if we need it.  It also provides a place to keep the as-entered portion.  It can also store a literal string, which is used in some cases.

In cases like the as entered text, if you need to enter a parentheses into it, it can be escaped with an ESC character (U+001B).  This is used as an escape character anywhere escape characters are needed in this project (almost like they were made for the purpose or something 🤔).