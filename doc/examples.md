# Example Use Cases



## Sending a Message

In it's simplest form, the uddb can function just like email, sending a message and facilitating replies.  That process goes something like this:

* A user (the sender) logs into their client, authenticating against their private key being held locally.

* Once authenticated, the sender then creates a new Document item with a security setting of *private* containing a simple text message as it's content block.

* If the intended recipient is a user on the uddb system and the sender has the recipient's User item in their library, they can simply drop the document onto the recipient's user item to generate a delivery.  Optionally, the delivery dialog may then appear to confirm, pre-set with default security settings.  Or the sender may open the delivery dialog from their authenticated user pane.  This dialog is similar to the query dialog, but containing an extra TO text field with accompanying clarifying pane.  The TO section is constructed just like a query, token by token.  The payload pane of the delivery dialog is just a simple container for any item to be dragged into, or an optional similar text input dialog can be activated.

* The user hits send and the delivery is generated.  If the users' security settings have specified the use of onion routing, some number of random hosts are chosen as intermediaries, and each layer of the onion is constructed and encrypted in turn invisibly to the user.

## Receiving a Message

When the user is logged in, their user window is always is accessible.  It is the visual representation of their user item and once authenticated, it will have a scrolling list of recently received deliveries.  If a response is received to the message sent from the previous example, whether from someone on uddb or otherwise on old style email, the message will appear in that list showing what part of it is new (ie the response).  The user must only click on it or drag it where they want it from there.  If said item is already on the screen in any form, it is automatically updated and shows a small notification, like a badge or more depending on the current size (state) of the item on the screen.

## A Game of Chess





