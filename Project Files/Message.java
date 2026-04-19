public class Message {
    public String type;  // SET, UPDATE, DELETE, RESET, FULL
    public int x;
    public int y;
    public char color;
    public String data;  // For the FULL canvas string

    // Constructor for coordinate-based messages (SET, UPDATE, DELETE)
    public Message(String type, int x, int y, char color) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    // Constructor for non-coordinate messages (RESET, FULL)
    public Message(String type, String data) {
        this.type = type;
        this.data = data;
    }
}
