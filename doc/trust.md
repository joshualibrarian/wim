# Trust

Trust in the WIM is developed between those items that are signers (users, sites, hosts) with each other, and by extension used to determine how much any given item or relation created by said user should be trusted.  This metric is arrived at indivdually *by* each signer, based on the data they have available, in relation *to* each other signer, and may be based on a wide range of factors, is extensible, and is user-customizable.

## Key Trust

What most key systems mean when they say *trust* is simply "does this public key really belong to this 
user?"  This is an important question, which the WIM may use various methods (QR Codes, NFC, etc.) to answer, but may fall back on asking the user some questions (did you speak with this user face to face, via videoconference, via voice call, or not at all, etc).  This metric should play a large part in the overall trust metric between all signers.

When one device on the WIM meets a new device it has not yet encountered, they exchange information about themselves.  This information may include various types of *reputation* data, including endorsements by other devices that have interacted with them before.  Various tokens, like something resembling TrustChain or other similar blocks may be included inform the evaluation of trust between hosts themselves.

## Trust Between Hosts

Hosts in the WIM are signers, and are needed to even create other signers of any kind, as without some kind of device it's hard to get on any network.  Every host maintains an *item* representing each other host that it knows about.  Throughout the WIM this same data structure is used to store almost everything addressable so that the powerful relations can be used so that data is findable through the common mechanisms.

The WIM is designed to be a diverse space and there may be many different kinds of hosts including:

* personal computers (desktops & laptops)
* mobile devices
* big corporate servers
* giant shared data centers
* mid-size hosting providers
* embedded devices

These different kinds of devices may employ different kinds of network behaviors because they have different purposes or different resources.  To provide anyonymity to their users, hosts are routinely requesting that other hosts relay messages for them, by delivering to them an 

* sharing resources
* 

Like everything else in the WIM, the trust system is built largely on the [relation](doc/relations.md) system.  Since every relation must have a "qualifier", a sememe that describes the relationship, numerical formulas can be associated with each sememe, creating algorithms that can be configured in user-space for themselves.

Much as happens currently, on various "social media" platforms, users of the WIM are expected to "react" to the various content they find.  Each of these reactions may include additional elements, but they all include at least one *sememe*.  When used in this way, these sememes are "qualifiers" for this particular relationship.  They can be most any word, an adjective: 

* good
* bad
* untrue
* true
* sad
* spam
* funny
* sexy
* violent
* embarassing



* agree
* disagree



## Identity

The foundation of the trust system is what we are calling *indentity*.  There are not only many meanings and types of identity, but there are several entire context to be talking about it at all.  However, the core meaning that is common to them all in the context of the WIM is that the identity means the *posession* of the private key.  If you have the private key to a keypair, then that public key is an identity you can use.

# The Identity of Users

When we talk about users, we are talking about real people, who exist in the physical world.  While someone could (and probably will) user automate user accounts ("bots"), that is not an intended use, and the various trust systems described herein should mediate this potential problem.  Though we are talking about real people, not every user on the system need be associated with that person's true identity.  An identity in the WIM primarily means 

This word can have a lot of different meanings in a lot of different contexts, and there different types of itentity.  There are "real identities" of the kind of thing a government or a bank, or even Twitter, might be concerned with, but there are other kinds of itendity 



### The identity of hosts

In the WIM, a *hosts* is any actual physical node on the system of any kind.  This can mean computers, phones, embedded devices, cybogs, whatever.  Every host in the WIM maintains a set of crytographic keypairs which are used to encrypt and sign its data and communication.  


## Reputation


* I requested something that it had



