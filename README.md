# Pixel Canvas Project
## CS321-Client-Server-Group-Project
### Authors
* **Chris**: Server
* **Ben**: Client
* **Rafay**: Library
#
**Course:** *CS321-01 – Spring 2026*   
**Project Start Date:** *April 24, 2026*  

---

## Project Description

The concept of this MVP project is a concurrent, connection-oriented network application that allows multiple users to draw on a shared 20x20 pixel canvas. The Pixel Canvas system features a multi-threaded server written in Java and a C-based client. Users with authorized access to the Canvas can set pixels to specific colors and z, y coordinates. For example: a User X decides to update y coordinate 3 to 5, with an updated color of green. As this is updated, the other users who have access can see the Canvas with the updates from User X. Actions are synced across all connected clients in real-time and allow seamless transitions without any delays or interruptions.

---

## Compile & Run Instructions

A Makefile is added to streamline the build process and make it work efficiently.

### Creation and Running the Pixel Canvas

Before beginning the Canvas system, both the Java server and C client must be compiled first. We use Bash and run the command:

```bash
make all
```

### Running the Java Server

The server must start first to listen for incoming connections, make sure in Bash that the following command is typed:

```bash
java Server (whatever Port Number)
```

### Running the C Client

In a new terminal in Bash, run the client.

```bash
./client [port number][size of port number] 
```

**Note:** The port is currently hardcoded to 4008 for testing, but typically accepts a command-line argument. The client number currently auto defaults to 127.0.0.1. Also if the user includes too few arguemnts 

---

## Project Design Overview

The Canvas Pixel application follows a Client-Server architecture which is utilized by a custom Library Protocol containing a set of methods for communication between the Server and Client, this ensures that the gap between Java and C is seamless and communicative. The following bullet points listed are as follows:

- **Server (Java):** Uses ServerSocket and a Handler thread for every client connection to allow for simultaneous users. Managing the 2D canvas array and broadcasting any changes to the entire clients list.

- **Client (C):** A multi-threaded terminal application. It uses pthread to run two tasks at once: one thread handles the User Menu (in this case: sending data), and the other thread listens for server broadcasts to update the local board view for everyone who has access. Note: If the user includes too few arguemnts, it will display it in the terminal.

- **Library (Java):** The "Middle Man" of the application. It contains the Message object and Protocol logic that turns raw bytes from the network into actionable commands for the server.

---

## Library Type & Justification

In this MVP project, a Source-Level Library was implemented.

The reason for this is that in a cross-language project (Java and C), sharing binary files like .dll or .so is complex. By creating a source-level protocol library, it ensured that both the Java server and C client follow a strict, human-readable text format. This makes memory management safer and debugging easier across different operating systems.

---

## Protocol Specifications

The protocol uses a newline-terminated text system. Both the client and server know they have received all data when they encounter the end of a message string.

| Command | Syntax | Origin | Description |
|---|---|---|---|
| Handshake | HELLO | Server | Confirms the connection is established. |
| Initial Sync | FULL \<data> | Server | Sends the current state of the 20x20 canvas to a new client. |
| Request Change | SET \<x> \<y> \<color> | Client | Asks the server to update a pixel. |
| Broadcast | UPDATE \<x> \<y> \<color> | Server | Notifies all clients to update their local display. |
| Disconnect | BYE | Client/Server | Signals that the connection is closing. |

---

## Current Known Issues

As in every MVP project, there are some issues that have been identified and tested. The list of issues is as follows:

- **Port Parsing:** The parse_port_number_from_args function in the C client has a known de-referencing quirk and is currently bypassed with a hardcoded port for stable testing.

- **Reset and Delete Logic:** Both formatReset and the formatDelete methods are placeholders in the Canvas instance class. Because of time restraints and spending more time on debugging and testing, it was decided that it should not be implemented in the Protocol class now for Phase 2.

- **Buffer Cleanup:** The buf in the Server Handler needs careful clearing to prevent "ghost" characters from previous long messages.
