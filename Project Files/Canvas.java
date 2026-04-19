public interface Canvas {
    //Convert client string to a readable format for output
    Message parse(String input);

    //Format a single pixel change (Used for SET/UPDATE)
    String formatUpdate(int x, int y, char color);

    // 3. Format the full canvas string (For new clients)
    String formatFull(char[][] canvas);

    // 4. Format a deletion (Placeholder)
    String formatDelete(int x, int y);

    // 5. Format a reset request (Placeholder)
    String formatReset();
}