# Networking Protocol

The WIM networking protocol is a (unicode) text-based TCP/IP protocol.  

## Initialization

This protocol begins its initialization process exactly as the SMTP protocol does.  Optionally (but active by default) with TLS, an (initially ASCII) text-based TCP/IP connection is made and from a client to a server.  Since "servers" routinely make outgoing connections to other hosts and clients can receive connections if they are network accessible, in the context of this document, *client* refers always to the host initiating the connection, and *server*, to the host on the receiving end of that connection.

When initially connecting to a new host, the client does not know if it will find another WMM peer or an actual SMTP server.  The server response line includes a marker, as any SMTP client would, signifying what kind of host they've found:

	220 foo.net ESMTP WIM <version>; <timestamp>

If an actual SMTP client has connected to our server to deliver some mail from outside the system, it will think nothing amiss and proceed with its delivery.  If a WIM client connects to a host whose marker is ambiguous, it can probe the waters with an empty request (`\r`, see below), which fails cleanly with a `500` error if it has indeed found an actual SMTP server, it can then carry on as SMTP.



If so, we will know for certain and behave accordingly, making it's deliveries packaged up as standard emails, with attachments as needed, and not making any requests.  If our server is connected to by an SMTP server with a delivery, it will receive that delivery and import it as a simple item.

When we do know that we have connected with a WMM host, we can use that *hello* command to exchange information about the hosts directly, which consists of an *item* that represents that host, whose name is the IP address of that hostmainly of the host's public key, and a set of signatures of it's trusted peers, to vouch for it.

Each host keeps track of all other hosts it has communicated with, and uses the item library mechanism to store data on each host, using the IP address namespace.  

## COMMANDS

As you'll see in the following breakdown of the protocol, it is simple and consistent, with the real power in the items themselves, and the protocol just authenticating, requesting items and delivering items.  Throughout the protocol, the format is (with square brackets denoting optional elements):

	\COMMAND [TO t\] [FROM f\] PAYLOAD [\[e] SIGNATURE] [w\] 

Other parameters to the command can be specified with additional `?\` sub-commands if required, such as a `s\` for security setting.


All commands in this protocol are short and designed to minimize wasted bytes, with clear defaults for omitted tokens.  The networking protocol in the WMM is a very simple one.  Once it's initialized, it contains only two commands: *DELIVERY* and *REQUEST*, marked by a `\` (backslash) *proceeding* the command code which is followed by the content of the command.


### DELIVERY

The payload of a delivery consists of one or more *items*, either whole, or just some part.  Proceeding the payload it may have a `FROM` and a `TO` field, and an optional signature, which authenticates its initiator.  The payload of an item may be encrypted, and when decrypted may contain another nested delivery for some other recipient.  If a host recieves a delivery that is not addressed to it, either directly or wrapped in another delivery, it should forward that delivery to the listed recipient unchanged.

	\d [TO t\] [FROM f\] PAYLOAD \ [[SIGNER]~SIGNATURE]+

#### encrypted payload

The empty `\` signifies the end of the payload and may only be followed by the optional signature.  If the payload is encrypted, then the payload marker is not the naked backslash, but the encrypted (`e\`) subcommand:

	\d [TO t\] [FROM f\] PAYLOAD e\ [~SIGNATURE]

If the payload is encrypted

#### FROM

#### TO



### REQUEST

An empty request results in a delivery containing 

	\r <SPACE_SEPARATED_LIST>
	
A request consists primarily of a space-separated list of tokens, which the recipient of the request will attempt to resolve, including 



## WAIT

	w\


Every instance of the WMM library is capable of performing both client and server functions.  Any client may activate it's networking component, however if located (like many workstation computers today) behind routers and Network Address Translation and firewalls, they are unable to be contacted directly from the outside.  In this case, the WAIT parameter is used at the end of the client's REQUEST command, causing the server to maintain the connection in place to allow for further commands from either side.  In such a case, the system can do most of it's communication through the single connection if it wants to, using that system as an intermediary to all other systems.

If the wait command is not used, either no reply is expected or the system acting as client has a running and accessible server, so the connection need not be maintained if the other side can initiate one.  However, each connection does come with the overhead of the (optional) establishment of the SSL connection.

## DELIVERY

This will doubtlessly be the most used command in the protocol, as any time that anyone updates any public item in a public way, everyone who is connected with that item will receive a delivery such as this.  Whenever a message is sent from one person to another, or a status is updated, or a meme is commented on, or pretty much anything happens at all (except for the few other commands yet to come)

	\d [TO t\] [FROM f\] PAYLOAD [\ ~SIGNATURE]
	
The signature is optional for deliveries, in which case they will be considered anonymous deliveries.  However, because security levels can be evaluated with no user to evaluate against, this will come across as untrusted, and depending on the users' settings might get filtered out as likely spam.  In the same way the delivery of those whose keys are signed directly, are indirectly by those we trust can be given priority, so important communications are prominent.

If you would like to encrypt an entire payload from end to end, the delivery must us the `e\` sub-command.  Only the payload may be encrypted, and the `e\` which replaces the plain `\` after the payload and before the signature.  If using onion routing, this payload will contain another delivery (designated with a new `\d`) with a new destination and the FROM being the previous envelope's TO.  The new destination will take it to another server or host which may be the final user, or may not, being only another hop in a series of a quantity known only to the initiator of the delivery.

When using the *encryption*, the payload must be encrypted with the key of the TO user, though each hop along the way, this will be the address of a given server or host, and then finally on the last hop will be the address of the actual intended recipient.

In the case of all the following commands, the use of the *t\* section is optional, and if omitted, the implication is that the machine receiving it is the intended recipient and the request can be processed locally.  However this value can be any combination (space separated) of: user addresses, ip addresses (v4 or v6), or hostnames (designated as naked domains, without any @, designating them from a `Server` instance of `Item`).
## REQUEST


There are several sub-commands, most of which are common to both.

## TO

	t\


## FROM

	f\


## PAYLOAD

	\


or for an encrypted payload

	e\


## SIGNATURE

	s\

