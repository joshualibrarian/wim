# World Information Map (WIM)

*(or come up with a better name, I dare ya, please!)*

**IMPORTANT NOTES**:
* This is very much a work in progress, there are still many details to be fleshed out conceptually and even more details that I've worked out but haven't written up yet.
* Anyone who is inspired by this vision is invited to dive right in.

## The Problem

The current world wide web of (largely dynamically generated) hyperlinked HTML files traveling over HTTP(S) is dominated by enormous corporate server farms serving (often redundant) data to mostly passive clients.  We fetch the same data from the same servers over and over, with the entire experience with increasingly powerful clients doing little more than a little visual JS and sending requests for more data back to the server.

## Items

The WIM is indeed yet another peer to peer system, which has at its core a novel data structure, called an [*item*](doc/item.md).  This is a sort of data-wrapper consisting of a collection of components which validate and contain the content of the item, its properties, and *relationships* to other items, serving as a universal mechanism to add properties, comments, and connections to other items and this is done so in a way that is indexed by natural language.  These items are then passed around the network, changing the paradigm of "going to" a particular resource, to one of "having" a given resource, which may be receiving ongoing updates.

Items in the WIM use a kind of content-addressing, though one with human-readable addresses which can be chosen by users.  Each address always referring to a particular item, no matter what host we find on.  While any address space may be added and used (.onion, IPFS hashes, local device names, etc), the default address space is human-readable and backwards-compatible with our current email and DNS system, which is herein called *"at domain"* addressing.

The familiar structure of "sites" are used to mirror the domain space and act in the WIM like a sort of organizational account which creates regular user accounts, just like in today's email structure.  Items to represent each host device, including the local one, are self-created and generate strong key pairs.  These *host* items then can create sites they want to host, with their own strong key pairs, which can be signed by the host.  Sites are then used to create users with their key pairs, all of which can generate various kinds of certificates to indicate types and levels of trust for each item.  Any of these *signer* type items may create regular (non-signer) items.

Every node (device) in the WIM stores a local library of core items that it needs or its users want, which may vary greatly for different types of systems.  For a user's personal system, it may be mostly their media library, documents, games, with the sememes needed to describe them and their relations.  For a tiny IOT sensor it could be nothing more than a host that it off-loads data to, its data log, and a few sememes to describe it.  A big store might have items for all its customers, employees, products, warehouses, etc.

All signer items (including users, sites, hosts, and others yet to be imagined) maintain a key-ring with keys and certificates for all other signers it has had contact with.  For each of these, trust metrics are calculated, based on the various endorsements through certificate signing as well as direct user choice.  These metrics take the form of both "validity", meaning how certain we are the key itself belongs to that specific user, which is mostly related to how the key was obtained, and "trust", which is about users' relationships to each other directly.  This can be calculated based on the interactions between their items and the kinds of sememes used to connect them, or set by users directly.

Absolutely anything and everything that is addressable in the WIM is represented by an item, including:

* hosts
* sites
* users
* documents (of all types)
* queries
* arbitrary logical groupings of other items (social group, project, etc)
* applications (sand-boxed)

All items consist of four different types of components.  When an item is published, it contains a fixed set of core item components, signed by the creator and immutable.  However, any user can add components to any item, and determine how public or private they want that component to be, as well as how far, and to whom, they want those components to propagate.

## Item Components

Each of the four types of items serves a specific purpose:

### Manifest

A [manifest](doc/item.md#manifest) is generated when you create an item and contains references to the other components of that item which are its required for it to be complete, with a signature that validates the actual content of all those components and of itself.

### Content

An item may have any number of [content](doc/item.md#content) blocks.  These blocks describe and then contain the actual content that this item is wrapping.  This could be text data, video data, application data, JSON, XML, a GIT repository, or any other type of data.

### Relation

The real power of the WIM is in the items' [relation](doc/item.md#relation) components.  The WIM comes equipped with a core "vocabulary" of items, including a fairly large initial set of small items called *sememes*.  There is a single shared sememe item for each *meaning* in any given language.  The initial set for English is imported from the [WordNet](https://wordnet.princeton.edu/), with all semantic and lexical relationships intact.  These sememe items are used as indexes, giving *meaning* to the relations between items, and other items and even parts of items.  Sememe items are mostly used for their relations, but also contain all lexical data in content blocks, one for each language that is supported.  This *lexeme* is a collection containing all the "words" that describe this sememe in a given language.

### Summary

Items are often passed around from host to host, sometimes because they have been requested specifically, or sometimes because they have been found in a query.  If the host sending the item is not certain that the querying host really wants an item, or even how mucƒh of an item it may want, it may send only a [summary](doc/item.md#summary), which is the fourth type of item component.  These components are routinely created and re-created by hosts which store these files as they grow (mostly due to relations being added).  Using the summary to decide, a host can request the entire item, or certain requested parts from the hosts that have it.

## Why Java?

This seems to be many people's first question when they hear about this project.  There are several very good reasons:

Since the WIM aims to ditch the web browser in the entirety, and would rather not reinvent all the wheels, we need a client framework to build on that is gives us access to many client platforms.  That is clearly Java, with the new-ish (and incredibly powerful) JavaFX, we can run one single desktop application on any computer of that class.  There are even Java Virtual Machines for Android, iOS, embedded systems, and just about everything else.  So rather than five different web-browsers to support, application developers in this system only have a single open source one, with most client classes usable on every platform.

And frankly, it just has everything we need, from encrypted, portable persistence, networking, a dynamic class-loading architecture, to a well defined security consciousness, with everything we need to build our sandbox already in place.  Java just makes the most sense for this project, at least initially.

## Networking

The WIM uses a relatively simple [network protocol](doc/protocol.md), which allows items or parts of items to be transferred, allows queries to be made and facilities anonymity with Tor-style onion routing.  Though a completely custom protocol, it uses the same ports as SMTP, and the system recognizes such services and interacts with them according, allowing the WIM to take over the email namespace slowly over time.

## Documentation

Through these documents, a consistent syntax will be used.  Each token may separated in brackets for readability, but those brackets *are not* intended to be included in the actual item.  White space *is* included as shown.

- Angle brackets (`<>`) denote required tokens.
- Square brackets (`[]`) denote optional tokens.
- A plus sign (`+`) following a token denotes that multiple iterations of it are allowed.

