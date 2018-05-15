package gameOfLife;

import java.util.List;

public class World {
    private Cell[][] worldGrid;

    private static final String DEAD_CELL_REP = "0";
    private static final String LIVE_CELL_REP = "1";

    /**
     * First constructor of world can be called with boolean 2D array.
     * @param booleanGrid
     */
    public World(boolean[][] booleanGrid) {
        this.worldGrid = initializeCellGridGivenBooleanArray(booleanGrid);
    }

    private Cell[][] initializeCellGridGivenBooleanArray(boolean[][] booleanGrid) {
        Cell[][] cellGrid = new Cell[booleanGrid.length][booleanGrid[0].length];
        for (int row = 0; row < booleanGrid.length; row++) {
            for (int column = 0; column < booleanGrid[row].length; column++) {
                cellGrid[row][column] = new Cell(row, column, booleanGrid[row][column]);
            }
        }
        return cellGrid;
    }

    /**
     * Second constructor
     * @param worldGrid
     */
    public World(Cell[][] worldGrid) {
        this.worldGrid = worldGrid;
    }

    /**
     * Third constructor of world can be called with the list of string inputs and initializes cell grid.
     * @param userInput
     * @param worldColumns
     */
    public World(List<String> userInput, int worldColumns) {
        this.worldGrid = storeValueInto2DCellArray(userInput, worldColumns);
    }

    public static Cell[][] storeValueInto2DCellArray(List<String> input, int worldColumns) {
        Cell[][] worldGrid = new Cell[input.size()][worldColumns];

        for(int row = 0; row < input.size(); row++) {
            boolean[] array = getStateOfCells(input.get(row));
            for(int column = 0; column < worldColumns; column++) {
                worldGrid[row][column] = new Cell(row, column, array[column]);
            }
        }
        return worldGrid;
    }

    private static boolean[] getStateOfCells (String input) {
        String[] splitString = input.split("\\s+");
        boolean[] booleanArray = new boolean[splitString.length];

        for (int i = 0; i < splitString.length; i++) {
            booleanArray[i] = splitString[i].equals("0") ?  Cell.DEAD: Cell.ALIVE;
        }
        return booleanArray;
    }

    /**
     * Fourth Constructor - Not given any parameters, it initializes the grid to all dead.
     */
    public World(int noOfRows, int noOfColumns) {
        this.worldGrid = populateWithDeadCells(noOfRows, noOfColumns);
    }

    private static Cell[][] populateWithDeadCells(int noOfRows, int noOfColumns) {
        Cell[][] cells = new Cell[noOfRows][noOfColumns];

        for(int row = 0; row < noOfRows; row++) {
            for (int column = 0; column < noOfColumns; column++) {
                cells[row][column] = new Cell(row, column, Cell.DEAD);
            }
        }
        return cells;
    }

    /**
     * Gets a string representation of the world given the world.
     * @param world
     * @return
     */
    public static String getStringRepresentationOfWorld(World world) {
        Cell[][] worldGrid = world.getWorldGrid();
        String worldString = "";

        for(int row = 0; row < worldGrid.length; row++) {
            for (int column = 0; column < worldGrid[row].length; column++) {
                if (worldGrid[row][column].isAlive()) {
                    worldString = worldString + LIVE_CELL_REP + " ";
                } else {
                    worldString = worldString + DEAD_CELL_REP + " ";
                }
            }
            worldString += "\n";
        }
        return worldString;
    }

    public Cell[][] getWorldGrid() {
        return worldGrid;
    }

    public void addLivingCellAt(int rowNumber, int columnNumber) {
        // Get cell from the world grid and make it living cell.
        worldGrid[rowNumber][columnNumber].setState(Cell.ALIVE);
    }

    public Cell getCellAt(int rowNumber, int columnNumber) {
        return worldGrid[rowNumber][columnNumber];
    }

    public boolean worldIsDead() {
        Cell[][] worldGrid = this.getWorldGrid();
        int noOfDeadCells = 0;
        for (int row = 0; row < worldGrid.length; row++) {
            for(int column = 0; column < worldGrid[row].length; column++) {
                if(!worldGrid[row][column].isAlive()) {
                    noOfDeadCells++;
                }
            }
        }
        return noOfDeadCells == (worldGrid.length) * (worldGrid[0].length);
    }

    public int getNoOfRows() {
        return this.worldGrid.length;
    }

    public int getNoOfColumns() {
        return this.worldGrid[0].length;
    }
}
