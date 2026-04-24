/************************************************************/
/* Author: Rafay Akbani                                     */
/* Major: Computer Science                                  */
/* Creation Date: April 19th, 2026                          */
/* Due Date: April 24th, 2026                               */
/* Course: CS321                                            */
/* Professor Name: Prof. Shimkanon                          */
/* Assignment: #2                                           */
/* Filename: Canvas.java                                    */
/* Purpose: Interface Class with sets of                    */
/* required operations for                                  */
/* canvas manipulation and defining protocol formatting.    */
/************************************************************/

public interface Canvas {
    //Convert client string to a readable format for output
    static Message parse(String input) {
        return null;
    }

    //Update a single pixel output based on the changes made by the client
    //(Used for SET/UPDATE)
    static String formatUpdate(int x, int y, char color) {
        return null;
    }

    //Format the full canvas string for current and new clients
    static String formatFull(char[][] canvas) {
        return null;
    }

    //Placeholder implementation of deleting a single pixel in the Canvas to all clients
    String formatDelete(int x, int y);

    //Placeholder implementation of resetting the entire Canvas to all clients
    String formatReset();
}