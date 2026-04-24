/************************************************************/
/* Author: Rafay Akbani                                     */
/* Major: Computer Science                                  */
/* Creation Date: April 19th, 2026                          */
/* Due Date: April 24th, 2026                               */
/* Course: CS321                                            */
/* Professor Name: Prof. Shimkanon                          */
/* Assignment: #2                                           */
/* Filename: Message.java                                   */
/* Purpose: Container of data representing in the Canvas to */
/* define command and updates, including                    */
/* new Message object creation                              */
/************************************************************/

public class Message {
    public String type;  // SET, UPDATE, DELETE, RESET, FULL
    public int x;
    public int y;
    public char color;
    public String data;  // For the FULL canvas string

    /***************************************************************************/
    /* Function name: Message (Coordinate-based)                               */
    /* Description: Constructur for message, doing pixel-specific actions      */
    /* Parameters: type - command type; x, y - coordinates; color - pixel char */
    /* Return Value: N/A                                                       */
    /***************************************************************************/
    public Message(String type, int x, int y, char color) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    /***************************************************************************/
    /* Function name: Message (Data-based)                                     */
    /* Description: Constructor for message for state-wide actions(FULL/RESET) */
    /* Parameters: type - command type; data - raw canvas string               */
    /* Return Value: N/A                                                       */
    /***************************************************************************/
    public Message(String type, String data) {
        this.type = type;
        this.data = data;
    }
}
