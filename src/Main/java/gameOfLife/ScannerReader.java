package gameOfLife;

import java.util.Scanner;

public class ScannerReader implements Reader {
    @Override
    public String readInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}
