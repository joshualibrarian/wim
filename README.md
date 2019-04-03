# WIM
# World Information Map

*(or come up with a better name, I dare ya, please!)*


**IMPORTANT NOTES**:
* This is very much a work in progress, there are still many details to be fleshed out conceptually and even more details that I've worked out but haven't written up yet.
* Anyone who is inspired by this vision is invited to dive right in.


The current world wide web of (largely dynamically generated) hyperlinked HTML files traveling over HTTP(S) is dominated by big companies serving bits of data to mostly passive clients.  We fetch the same data from the same servers over and over, with the entire experience dominated by the server and without that server, the client is useless.  We have no coherent base level method of saving data with its relations to other data intact.

In the WIM, everything is addressable, and those addresses are text and familiar and can be human-readable.  All items are findable by their relationships to other items, all indexed by other items representing meanings themselves (semantic units, or *sememes*), starting out with the entire English language imported from wordnet with all of its syntactic and lexical data.  You can attach any item to another item *through language* itself in a way that leads directly to natural language queries.  This mechanism is used to attach any kind of attributes (including literals or relations to other items) on any item in the system, regardless of its nature.  These sememes and the lexemes they contain act as a kind of shared vocabulary, with every connection between items taking place through one of these, they function as shared indexes of other items.

The WIM also provides a heightened level of accountability and of security.  Here, we resurrect the highly functional but never-effectively-implemented OpenPGP (GnuPG) system with public/private keypairs, and a distributed web of trust and build our system around it making it seamless and nearly invisible to the user (except to decide their preferred security levels).

When a copy of these items is updated, other users who are connected, or whose connections are connected to those items can be notified (depending on their settings).  The items synchronize across peers, while updating each other usually with lightweight summaries or added relationships, and then sending whole items when requested.  All users have strong keypairs and all items and relations are signed by their creators.  The same item, in the possession of multiple users, communicates with itself, thereby creating a connection between users.  When an item is executed on a user's client system, of course it will run inside of a sandbox using a custom SecurityManager, keeping it isolated from the filesystem, networking, etc of the host system, but can access other items through the WIM API.

The structure of these "relations" for each item is a table of simple "sentences" made up of references (actual address of each item) to a series of specific types of items, creating a syntactic and lexical relationship between those items.  Each relationship is signed by it's creator and can connect the "subject" item in question to either a series of adjectives (giving the syntactic meaning "I say this item [sucks, is awesome, is hilarious, etc]." or you can connect a verb with an option series of adverbs connecting an "object" item of any kind.  It could be another document, a person, or just about anything else at all.  In either case, an additional literal string can be stored with the relationship, which is meant for simple comments about the relationship.  You can connect any item to any item, but you must do it through a verb!  When these relation tables are stored in the items themselves, they are duplicated once for each item that is involved in the relation, so at a minimum two items may be involved in a simple relation, so that relation would be stored in the tables of both of those items.  If there were three or four items involved, then that is how many times the relation would be duplicated, always with the same unique id identifying it.

Through the entire system, only unicode encodings are supported.  ASCII will be interpreted as UTF-8 and all other regional encodings are not permitted.  The default is UTF-8. 

## Why Java?

This seems to be many people's first question when they hear about this project.  There are several very good reasons:

Since the WIM aims to ditch the web browser in the entirety, and would rather not reinvent all the wheels, we need a client framework to build on that is gives us access to many client platforms.  That is clearly Java, with the new-ish (and incredibly powerful) JavaFX, we can run one single desktop application on any computer of that class.  There are even Java Virtual Machines for Android, iOS, embedded systems, and just about everything else.  So rather than five different web-browsers to support, application developers in this system only have a single open source one, with most client classes usable on every platform.

And frankly, it just has everything we need, from encrypted, portable persistence, networking, a dynamic class-loading architecture, to a well defined security consciousness, with everything we need to build our sandbox already in place.  Java just makes the most sense for this project, at least initially.

## Item 

The fundamental unit of organization, of addressability, is called an item.  Any kind of data can be encapsulated in an item, from document to program or anything else.  These items can be related *through* any item(s), *to* any item.  It is a coherent unit that can be shared and stays selectively synchronized with other copies of itself.  It has a unique identifier across the system and forms the base addressable unit.  Almost everything you'll encounter as a user on this system is an item of one kind or another.  See the [specification](doc/item.md) for details.

## Networking

The WIM uses a relatively simple [network protocol](doc/protocol.md), which allows items or parts of items to be transferred, allows queries to be made and facilities anonymity with Tor-style onion routing.  Though a completely custom protocol, it uses the same ports as SMTP, and the system recognizes such services and interacts with them according, allowing the WIM to take over the email namespace slowly over time.

## Addressing

Items are each uniquely addressable across the system.  Though backwards-compatible to the email namespace, the WIM is flexible to allow for almost any namespace.  The [addressing scheme](doc/addressing.md) is very robust and allows for referencing not just items, but parts of items.