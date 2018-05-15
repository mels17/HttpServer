package gameOfLife;

import java.util.HashSet;
import java.util.Set;

public class Neighbour {

    private static HashSet<Cell> getNeighbours(Cell centreCell, Cell[][] grid) {
        Set<Cell> cellNeighbours = new HashSet<Cell>();

        int worldEndRows = grid.length - 1;
        int worldEndColumns = grid[0].length - 1;
        int[]  possibleDirections = {-1, 0, 1};
        int neighbourRow;
        int neighbourColumn;

        for (int row : possibleDirections) {
            for (int column : possibleDirections) {
                neighbourRow = centreCell.getRowNumber() + row;
                neighbourColumn = centreCell.getColumnNumber() + column;
                try {
                    Cell cell = grid[neighbourRow][neighbourColumn];
                    cellNeighbours.add(cell);
                } catch (Exception e) {
                    neighbourRow = getValidCoordinate(worldEndRows, neighbourRow);
                    neighbourColumn = getValidCoordinate(worldEndColumns, neighbourColumn);
                    cellNeighbours.add(grid[neighbourRow][neighbourColumn]);
                }
            }
        }
        cellNeighbours.remove(centreCell);
        return (HashSet<Cell>) cellNeighbours;
    }


    private static int getValidCoordinate(int worldEnd, int coordinate) {
        int newCoordinate;
        if (coordinate > worldEnd) {
            newCoordinate = 0;
        } else if (coordinate < 0) {
            newCoordinate = worldEnd;
        } else {
            newCoordinate = coordinate;
        }
        return newCoordinate;
    }

    public static int getLivingNeighbours(Cell centreCell, Cell[][] worldGrid) {
        int livingNeighbours = 0;
        Set<Cell> neighbours = getNeighbours(centreCell, worldGrid);

        for (Cell cell : neighbours) {
            if (cell.isAlive()) {
                livingNeighbours++;
            }
        }
        return livingNeighbours;
    }
}
