package gameOfLife;

import java.util.Scanner;

public class App {
    public static void main(String... args) {
        Reader scannerReader = () -> { Scanner scanner = new Scanner(System.in); return scanner.nextLine();};
        IPrinter printer = (World world) -> {
            System.out.print(World.getStringRepresentationOfWorld(world) + "\n");};
        Game.enterGame(scannerReader, printer);
    }
}
