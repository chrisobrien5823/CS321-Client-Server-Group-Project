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

public class Server
{  
    /***************************************************************************/
    /* Function name: getToken */
    /* Description: Retrieves a character from an interpreter to parse */
    /* into a “token” item */
    /* Parameters: object – token: the character object to be retrieved */
    /* Return Value: char – the token after being parsed and error-checked */
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
       try
       {
            while(true)
            {
                // spawn a handler thread for client connection
                new Handler(serverSocket.accept()).start();
            }  // end while
        }
        finally
        {
            serverSocket.close();
        } // end finally
    }  // end function main


     private static class Handler extends Thread
    {
        private Socket socket;   // socket to use to connect to clients
        private PrintWriter out;
        private BufferedReader in;
        private String inputLine, outputLine;
        private String[] operands;
        private Integer sum;

        // Construct a handler thread
        public Handler(Socket socket)
        {
            this.socket = socket;
        }  // end Handler


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

                // Wait until client disconnects
                while (in.readLine() != null) {
                    // Do nothing, just keep connection alive
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


