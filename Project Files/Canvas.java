public interface Canvas {
    //Convert client string to a readable format for output
    static Message parse(String input) {
        return null;
    }

    //Format a single pixel change (Used for SET/UPDATE)
    static String formatUpdate(int x, int y, char color) {
        return null;
    }

    //Format the full canvas string (For new clients)
    static String formatFull(char[][] canvas) {
        return null;
    }

    //Format a deletion (Placeholder)
    String formatDelete(int x, int y);

    //Format a reset request (Placeholder)
    String formatReset();
}