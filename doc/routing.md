Every host in the WIM, whether in some geek's basement or in a giant data center can run the same code core, called a *library*, meaning the local system with it's local data store.  This service, which can be run as a system daemon, or directly, acts as both "server" and "client" as it goes about processing requests and deliveries, some of which may be actually intended for other servers, peers that we know, and perhaps have some relationships with.

Each of these hosts maintains it's own list of known hosts, and to do this it employs the WIM's own item system, creating an "item" for each host, using the IP namespace directly.

