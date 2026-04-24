/************************************************************/
/* Author: Chris O'Brien */
/* Major: Computer Science */
/* Creation Date: March 26th, 2026 */
/* Due Date: April 2nd, 2026 */
/* Course: CS321 */
/* Professor Name: Prof. Shimkanon */
/* Assignment: #1 */
/* Filename: Server.java */
/* Purpose: This program will open/listen to connection for */
/* a client and respond when connected */
/************************************************************/

import java.io.*;
import java.net.*;
import java.util.*;


public class Server
{  

    private static final int SIZE = 8;  // size of the canvas in pixels
    private static char[][] canvas = new char[SIZE][SIZE];  // the canvas itself
    private static List<Handler> clients = new ArrayList<>();  // list of connected clients
    /***************************************************************************/
    /* Function name: main */
    /* Description: Starts the server and listens for client connections */
    /* Parameters: args – command line arguments (port number) */
    /* Return Value: none */
    /***************************************************************************/
    public static void main(String[] args) throws IOException
    {
       if (args.length != 1)
       {
            System.err.println("Usage: java Server <port number>");
            System.exit(1);
       }  // end if usage clause

       int portNumber = Integer.parseInt(args[0]);
       ServerSocket serverSocket = new ServerSocket(portNumber);

       // Initialize the canvas with blank spaces
       for (int i = 0; i < SIZE; i++) 
       {
            Arrays.fill(canvas[i], '.');
       }

       try
       {
            while(true)
            {
                // spawn a handler thread for client connection
                Handler handler = new Handler(serverSocket.accept());
                // synchronize access to the clients list when adding a new handler
                synchronized(clients) {
                    clients.add(handler);
                    System.out.println(clients.size());
                }
                handler.start();
            }  // end while
        }
        finally
        {
            serverSocket.close();
        } // end finally
    }  // end function main

    
    /***************************************************************************/
    /* Function name: broadcast */
    /* Description: Sends a message to all connected clients */
    /* Parameters: message – the message to send */
    /* Return Value: none */
    /***************************************************************************/
    public static void broadcast(String message) 
    {
        synchronized(clients)
        {
            for (Handler client : clients) 
            {
                //client.send(message); //send not implemented yet (Ben: Client does not need the message response)
                client.send(getFullCanvas());
            }
        }
    }

    /***************************************************************************/
    /* Function name: handleSet */
    /* Description: Updates the canvas and notifies all clients */
    /* Parameters: x – the x-coordinate, y – the y-coordinate, c – the color to set */
    /* Return Value: none */
    /***************************************************************************/
    public static synchronized void handleSet(int x, int y, char color) 
    {
        if (x >= 0 && x < SIZE && y >= 0 && y < SIZE) // so long as the pixel is within the canvas
        {
            canvas[y][x] = color;
            
            String msg = Protocol.formatUpdate(x, y, color); //LIBRARY FUNCTION "UPDATE x y color"
            broadcast(msg); // send update to all clients
        }
    }

    //getCanvas TBA
    public static synchronized String getFullCanvas() 
    {
        return Protocol.formatFull(canvas); //LIBRARY FUNCTION "FULL canvas"
    }



     private static class Handler extends Thread
    {
        private Socket socket;   // socket to use to connect to clients
        private PrintWriter out;
        private BufferedReader in;
        private String inputLine;

        // Construct a handler thread
        public Handler(Socket socket)
        {
            this.socket = socket;
        }  // end Handler

        /***************************************************************************/
        /* Function name: send */
        /* Description: Sends a message to the client */
        /* Parameters: message – the message to send */
        /* Return Value: none */
        /***************************************************************************/
        public void send(String message)
        {
            out.println(message);
        }

        // do the thread processing
        /***************************************************************************/
        /* Function name: run */
        /* Description: general main function for the server side including all */
        /* input, output, connection, and idsconnection of the sockets */
        /* Parameters: N/A */
        /* Return Value: while no return value, the program ends by closing a socket */
        /***************************************************************************/
        public void run()
        {
            try
            {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // Print connection message
                System.out.println("Connecting to " + socket.getInetAddress());

                // Send HELLO to client

                // For now I am leaving the above HELLO connection
                // But continuing by grabbing a full canvas and sending it to the client

                System.out.println(getFullCanvas());

                send(getFullCanvas());
                
                byte[] buf = new byte[1024];
                // Wait until client disconnects
                    //Now we can actually do something while running
                    //Listen for client messages and handle them accordingly


                    // Get raw bytes


                boolean exit = false;
                while(exit == false) {
                    socket.getInputStream().read(buf);

                    //Ben - https://stackoverflow.com/questions/18367539/slicing-byte-arrays-in-java
                    byte[] check_for_exit = Arrays.copyOfRange(buf, 0, 4);



                    if(buf[0] != 0) {
                            // System.out.println("check exit: " + new String(check_for_exit));
                            // System.out.println("Equal?: " + (new String(check_for_exit).compareTo("exit")));

                            if((new String(check_for_exit)).compareTo("exit") == 0)  {
                                exit = true;
                                break;
                            } else {    
                                System.out.println("Got message!");

                                Message msg = Protocol.parse(buf); //LIBRARY FUNCTION to parse a message into a Message object
                                if (msg != null && msg.type.equals("SET")) 
                                {
                                    // Handle SET message
                                    handleSet(msg.x, msg.y, msg.color); 
                                    System.out.println(getFullCanvas());

                                }
                            }

                    }

                    buf = new byte[1024];
                        
                }

                // Print disconnect message
        System.out.println("BYE " + socket.getInetAddress());

        }
        catch (IOException e)
        {
            System.out.println(e);
        }
        finally
        {
            try {
                socket.close();
            } 
            catch(IOException e) {
                System.out.println(e);
            }
            synchronized(clients)
            {
                clients.remove(this); // remove this handler from the clients list when done
            }

            
            }  // end finally
        }  // end function run
    }  // end class Handler
}   // end class Server




// Addtional message for closing a client specifically

//LIBRARY METHODS INCLUDE
// SET a pixel
// UPDATE a pixel
// FULL (the entire canvas for a new client)

/*********************
 * Right now I need encodeUpdate, encodeFull (canvas), and ParseMessage
 */

//Message
