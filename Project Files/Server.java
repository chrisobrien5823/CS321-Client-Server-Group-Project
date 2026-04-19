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

    private static final int SIZE = 20;  // size of the canvas in pixels
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
                client.send(message); //send not implemented yet
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
            
            String msg = Protocol.encodeUpdate(x, y, color); //LIBRARY FUNCTION "UPDATE x y color"
            broadcast(msg); // send update to all clients
        }
    }

    //getCanvas TBA
    public static synchronized String getFullCanvas() 
    {
        return Protocol.encodeFull(canvas); //LIBRARY FUNCTION "FULL canvas"
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
                out.println("HELLO");

                // For now I am leaving the above HELLO connection
                // But continuing by grabbing a full canvas and sending it to the client
                send(getFullCanvas());

                // Wait until client disconnects
                while ((inputLine = in.readLine()) != null) {
                    //Now we can actually do something while running
                    //Listen for client messages and handle them accordingly

                    Message msg = Protocol.parseMessage(inputLine); //LIBRARY FUNCTION to parse a message into a Message object

                    if (msg != null && msg.type.equals("SET")) 
                    {
                        // Handle SET message
                        handleSet(msg.x, msg.y, msg.color); 
                    }
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
                synchronized(clients)
                {
                    clients.remove(this); // remove this handler from the clients list when done
                }
                
                try
                {
                    socket.close();
                }
                catch (IOException e)
                    {

                    }  // end catch
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