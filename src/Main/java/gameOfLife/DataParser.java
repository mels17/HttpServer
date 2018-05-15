package gameOfLife;

import java.util.ArrayList;
import java.util.List;

public class DataParser {
    private static final String QUIT = "q";
    private static final String EMPTY_STRING = "";
    private static final int MIN_WORLD_SIZE = 2;

    public static String[] splitStringWhereSpaces (String input) {
        return input.split("\\s+");
    }

    public static List<String> getListOfUserInputs(Reader reader, String input, int worldColumns) {
        List<String> inputStrings = new ArrayList<String>();
        while(!input.equals(QUIT) && worldColumns >= MIN_WORLD_SIZE) {
            checkValidity(inputStrings, input, worldColumns);

            input = reader.readInput();

            if(input.equals(QUIT) && inputStrings.size() < MIN_WORLD_SIZE) {
                input = EMPTY_STRING;
                System.out.print("World length should be atleast two in both dimensions.");
                worldColumns = 0;
                inputStrings = new ArrayList<String>();
            }
        }
        return inputStrings;
    }

    private static boolean[] convertStringToArrayOfBooleans (String input) {
        String[] splitString = splitStringWhereSpaces(input);
        boolean[] booleanArray = new boolean[splitString.length];

        for (int i = 0; i < splitString.length; i++) {
            booleanArray[i] = splitString[i].equals("0") ? Cell.DEAD : Cell.ALIVE;
        }
        return booleanArray;
    }

    // VALIDITY
    private static boolean isValidInput (String input, int worldColumns) {
        return input.split("\\s+").length == worldColumns;
    }


    private static void checkValidity(List<String> inputStrings, String input, int worldcolumns) {
        if (isValidInput(input, worldcolumns)) {
            inputStrings.add(input);
        } else {
            System.out.print("Invalid entry. Try again..\n");
        }
    }
}
