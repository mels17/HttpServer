package gameOfLife;

public class Cell {
    public static final boolean ALIVE = true;
    public static final boolean DEAD = false;

    private int rowNumber;
    private int columnNumber;

    private boolean state;
    public Cell(int rowNumber, int columnNumber, boolean state) {
        this.rowNumber = rowNumber;
        this.columnNumber = columnNumber;

        this.state = state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public boolean isAlive() {
        return state;
    }

    public int getRowNumber() {
        return this.rowNumber;
    }

    public int getColumnNumber() {
        return this.columnNumber;
    }
}
