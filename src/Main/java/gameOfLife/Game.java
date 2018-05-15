package gameOfLife;

import java.util.List;

/**
 * Example:
 * 0 1 0 1 0
   0 0 0 1 0
   0 1 1 1 0
   0 1 1 1 0
 */
public class Game {

    private static final int THREAD_SLEEP_SECONDS = 500;

    // Get user's input - could be 0 and 1's

    private static void run (List<String> userInput, IPrinter printer, int worldColumns) {
        World world = new World(userInput, worldColumns);
        while(!world.worldIsDead()) {
            world = NextGeneration.getNextWorld(world);
            printer.print(world);
            try {
                Thread.sleep(THREAD_SLEEP_SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Entry Point of the game.
     * @param reader - Read input object
     * @param printer - Object of the printer class
     */
    public static void enterGame (Reader reader, IPrinter printer) {
        int worldColumns = 0;
        String input = "";

        while(worldColumns < 2) {
            System.out.print("Enter a 2-d array of zeros and ones with spaces:\n");
            input = reader.readInput();
            worldColumns = DataParser.splitStringWhereSpaces(input).length;
            if(input.equals("q")) {
                System.out.println("Game Over.");
                return;
            }
        }

        List<String> output = DataParser.getListOfUserInputs(reader, input, worldColumns);
        if(output.isEmpty()) {
            return;
        }
        run(output, printer, worldColumns);
    }


}

