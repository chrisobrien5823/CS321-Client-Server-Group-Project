public class Protocol implements Canvas {

    @Override
    public Message parse(String input) {
        if (input == null || input.isEmpty()) return null;
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
                    return new Message("UNKNOWN", 0, 0, ' ');
            }
        } catch (Exception e) {
            return null; // Bad formatting from Ben's client
        }
    }

    @Override
    public String encodeUpdate(int x, int y, char color) {
        return String.format("UPDATE %d %d %c", x, y, color);
    }

    @Override
    public String formatFull(char[][] canvas) {
        StringBuilder sb = new StringBuilder("FULL ");
        for (char[] row : canvas) {
            sb.append(new String(row));
        }
        return sb.toString();
    }

    @Override
    public String formatDelete(int x, int y) {
        return String.format("UPDATE %d %d .", x, y);
    }

    @Override
    public String formatReset() {
        return "RESET";
    }
}
