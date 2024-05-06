# Network_Attack_Monitor
Network_Attack_Monitor

A Network Attack Monitoring Tool that allows a user to monitor in real-time the status of each node of a
world map represented as a graph. There are 4 types of attacks: red, blue, yellow, and black, each of which injects a specific type of virus in a node. 
At any point in time, if more than 2 virus injections of the same type occur within 2 minutes at the same node, 
this node will generate an alert (in the form of a message showing the city name and the type of virus). 
If 4 or more injections of the same virus occur within 4 minutes at a node, then this node is considered to have an outbreak.
As a consequence of this outbreak, all adjacent nodes will receive one injection of the virus of the same type. 
Chain outbreak, that is an outbreak immediately leading to more outbreaks, is possible. 
Once a node accumulates 6 virus injections it is permanently put offline. 
This means all connections to its adjacent nodes are removed and the status of this node is set to “Inactive”. 
Such a node no longer gets affected by any future attacks.
Some nodes may be equipped with a Firewall. Those nodes are not infected by any attack. However, the firewall must keep a record of each attack it stops.
