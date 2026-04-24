/************************************************************/
/* Author: Rafay Akbani                                     */
/* Major: Computer Science                                  */
/* Creation Date: April 19th, 2026                          */
/* Due Date: April 24th, 2026                               */
/* Course: CS321                                            */
/* Professor Name: Prof. Shimkanon                          */
/* Assignment: #2                                           */
/* Filename: Protocol.java                                  */
/* Purpose: This class is to provide static utility         */
/* functions to parse and format messages between           */
/* the Client and the Server.                               */
/* This is the "middle-man" connection                      */
/************************************************************/

public class Protocol {

    /***************************************************************************/
    /* Function name: parse                                                    */
    /* Description: Converts raw bytes from a socket into a Message object     */
    /* Parameters: "in", this is the byte array received from the network      */
    /* Return Value: Display Message object or null if formatting is invalid   */
    /***************************************************************************/
    public static Message parse(byte[] in) {
        if (in == null) return null;

        System.out.println("Processing message");

        String input = new String(in);
        
        System.out.println(input);

        String[] parts = input.split(" ");
        String cmd = parts[0].toUpperCase();


        try {
            switch (cmd) {
                case "SET":
                    return new Message("SET", Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), parts[3].charAt(0));
                case "DELETE":
                    // Deleting (1,1) is basically just setting (1,1) to '.'
                    return new Message("UPDATE", Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), '.');
                case "RESET":
                    return new Message("RESET", 0, 0, ' ');
                default:
                    //Otherwise return to default values if no parameters are set
                    return new Message("UNKNOWN", 0, 0, ' ');
            }
        } catch (Exception e) {
            return null; //If bad formatting occurs, throw an exception
        }
    }

    /***************************************************************************/
    /* Function name: formatUpdate                                             */
    /* Description: Formats a pixel update into a protocol-compliant string    */
    /* Parameters: coordinates "x" and "y", "color" - the new pixel character  */
    /* Return Value: String formatted as "UPDATE x y color" to output          */
    /***************************************************************************/
    public static String formatUpdate(int x, int y, char color) {
        return String.format("UPDATE %d %d %c", x, y, color);
    }

    /***************************************************************************/
    /* Function name: formatFull                                               */
    /* Description: Serializes the entire 2D canvas into a single sync string  */
    /* Parameters: "canvas", the 2D char array representing the board          */
    /* Return Value: String formatted as "FULL <canvas_data>" to output        */
    /***************************************************************************/
    public static String formatFull(char[][] canvas) {
        StringBuilder sb = new StringBuilder("FULL ");
        for (char[] row : canvas) {
            sb.append(new String(row));
        }
        return sb.toString();
    }

    //Currently a placeholder function to delete a pixel in the Canvas for all clients
    public String formatDelete(int x, int y) {
        return String.format("UPDATE %d %d .", x, y);
    }

    //Currently a placeholder function to reset the Canvas for all clients
    public String formatReset() {
        return "RESET";
    }
}
