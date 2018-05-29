package gameOfLife;

public class NextGeneration {
    // Determine if a cell is live or dead in the next generation.
    // It also gives the next world.

    private static boolean computeNextGenForCell(Cell cell, int noOfLivingNeighbours) {
        return cell.isAlive() ? aliveCellGetNextState(noOfLivingNeighbours) : deadCellGetNextState(noOfLivingNeighbours);
    }

    private static boolean deadCellGetNextState(int noOfLivingNeighbours) {
        return noOfLivingNeighbours == 3;
    }

    private static boolean aliveCellGetNextState(int noOfLivingNeighbours) {
        return noOfLivingNeighbours == 2 || noOfLivingNeighbours == 3;
    }

    public static World getNextWorld(World world) {
        World nextWorld = new World(world.getNoOfRows(), world.getNoOfColumns());

        for (int row = 0; row < world.getNoOfRows(); row++) {
            for (int column = 0; column < world.getNoOfColumns(); column++) {
                nextWorld.getCellAt(row, column).setState(computeNextGenForCell(world.getCellAt(row, column),
                        Neighbour.getLivingNeighbours(world.getCellAt(row, column), world.getWorldGrid())));
            }
        }
        return nextWorld;
    }
}


