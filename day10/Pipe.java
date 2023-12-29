package day10;

public enum  Pipe {
    GROUND('.',false, false, false, false),
    START('S', true, true, true, true),
    NORTH_EAST('L',true, true, false, false),
    NORTH_SOUTH('|', true, false, true, false),
    NORTH_WEST('J',true, false, false, true),
    EAST_WEST('-', false, true, false, true),
    SOUTH_EAST('F', false, true, true, false),
    SOUTH_WEST('7',false, false, true, true);

    private final char symbol;
    private final boolean checkNorth;
    private final boolean checkEast;
    private final boolean checkSouth;
    private final boolean checkWest;

    Pipe(char symbol, boolean checkNorth, boolean checkEast, boolean checkSouth, boolean checkWest) {
        this.symbol = symbol;
        this.checkNorth = checkNorth;
        this.checkEast = checkEast;
        this.checkSouth = checkSouth;
        this.checkWest = checkWest;
    }

    public static Pipe getPipe(char c) {
        for (Pipe p : Pipe.values()) {
            if (p.symbol == c) {
                return p;
            }
        }
        return null;
    }

    public boolean checkNorth() {
        return checkNorth;
    }

    public boolean checkEast() {
        return checkEast;
    }

    public boolean checkSouth() {
        return checkSouth;
    }

    public boolean checkWest() {
        return checkWest;
    }

    @Override
    public String toString() {
        return String.valueOf(symbol);
    }
}
