package jp.ac.tsukuba.adventure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {
    private final Map map;
    private final Jibun player;
    private final List<Character> entities = new ArrayList<>();
    
    private boolean isGoal = false;
    private boolean isRunning = true;
    
    // UI State: Stores the last action to display after screen wipe
    private String lastLog = "Welcome to Omotesando. Navigate with N/S/E/W.";

    public Game(String mapFilePath) {
        MapLoader.MapData data = null;
        try {
            data = MapLoader.loadMap(mapFilePath);
        } catch (IOException e) {
            System.err.println("FATAL: Could not load map.");
            e.printStackTrace();
            System.exit(1);
        }

        this.map = new Map(data.layout);
        this.player = new Jibun("", "OO", data.startX, data.startY);
        this.map.setVisited(data.startX, data.startY);
        
        entities.add(new Murabito("Old Man", 8, 4, "The exit is to the North-East!"));
        entities.add(new Murabito("Guard", 3, 1, "Don't get lost in the dark."));
        entities.add(new Monster("Slime", 3, 2));
        entities.add(new Monster("Goblin", 5, 6));
        entities.add(new Goddess("Athena", 1, 1));
    }

    // --- VISUAL HELPERS ---

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void printSeparator() {
        System.out.println("+--------------------------------------------------+");
    }

    private void printOpening() {
        clearScreen();
        printSeparator();
        System.out.println("|                                                  |");
        System.out.println("|       O M O T E S A N D O   A D V E N T U R E    |");
        System.out.println("|                                                  |");
        System.out.println("|               - The Escape -                     |");
        System.out.println("|                                                  |");
        printSeparator();
        System.out.println("  [Press Enter to Begin]");
        try { System.in.read(); } catch (Exception e) {}
    }

    private void printEnding(boolean success) {
        clearScreen();
        printSeparator();
        System.out.println("|                                                  |");
        if (success) {
            System.out.println("|           M I S S I O N   C O M P L E T E        |");
            System.out.println("|                                                  |");
            System.out.println("|         You have escaped the dungeon!            |");
        } else {
            System.out.println("|               G A M E   O V E R                  |");
            System.out.println("|                                                  |");
            System.out.println("|        The dungeon claims another soul...        |");
        }
        System.out.println("|                                                  |");
        printSeparator();
    }

    private void printHUD() {
        printSeparator();
        // Fixed width formatting for clean alignment
        System.out.printf("| PLAYER: %-12s HP: %-3d  LOC: (%2d, %2d)   |\n", 
                          player.name, player.getHp(), player.getX(), player.getY());
        printSeparator();
        System.out.println("| LOG: " + lastLog);
        printSeparator();
    }

    // --- GAME LOGIC ---

    void processTurn(String input) {
        lastLog = ""; // Reset log for new turn

        if ("Q".equals(input)) {
            lastLog = "Quitting game...";
            isRunning = false;
            return;
        }
        if ("F".equals(input)) { 
            handleCombat(); 
            return; 
        }

        int tx = player.getX();
        int ty = player.getY();
        String moveName = "";

        switch (input) {
            case "N": ty--; moveName = "North"; break;
            case "S": ty++; moveName = "South"; break;
            case "E": tx++; moveName = "East";  break;
            case "W": tx--; moveName = "West";  break;
            default: 
                lastLog = "Unknown command. Use N, S, E, W, F, or Q.";
                return;
        }

        try {
            map.validateAndMove(player, tx, ty);
            lastLog = "Moved " + moveName + ".";
            
            for (Character e : entities) {
                if (e.getX() == player.getX() && e.getY() == player.getY()) {
                    System.out.println(player.name + ": " + e.getPlayerReaction());
                    
                    // Interaction
                    e.interact(player);
                    
                    if (e instanceof Monster) {
                        lastLog = "Encountered " + e.name + "!";
                    } else if (e instanceof Goddess) {
                        lastLog = "You spoke with the Goddess.";
                    } else {
                        lastLog = "You spoke with " + e.name + ".";
                    }
                }
            }

            if (map.isGoal(player.getX(), player.getY())) {
                lastLog = "GOAL REACHED!";
                isGoal = true;
            }
        } catch (MovementException e) {
            lastLog = "BONK! " + e.getMessage();
        }
    }

    private void handleCombat() {
        boolean monsterFound = false;
        for (int i = 0; i < entities.size(); i++) {
            Character e = entities.get(i);
            if (e.getX() == player.getX() && e.getY() == player.getY()) {
                monsterFound = true;
                boolean shouldRemove = e.onCombat(player);
                if (shouldRemove) {
                    entities.remove(i);
                    lastLog = "Victory! The " + e.name + " was defeated.";
                } else {
                    lastLog = "You fought " + e.name + ". HP is now " + player.getHp();
                }
                break; 
            }
        }
        
        if (!monsterFound) {
            lastLog = "There is no monster here to fight.";
        }
    }

    public void run() {
        Scanner scan = new Scanner(System.in);
        printOpening();

        // Name Entry Loop
        while (player.name.length() == 0) {
            System.out.print("Enter Your Name: ");
            String nameInput = scan.nextLine().trim();
            System.out.print("So your name is "+ nameInput + "? (Y/N)");
            String confirm = scan.nextLine().trim().toUpperCase();
            if (confirm.equals("Y")) {
                if (!nameInput.isEmpty()) {
                    player.name = nameInput;
                }
            }         
        }

        // Main Game Loop
        while (!isGoal && player.isAlive() && isRunning) {
            
            System.out.println(); 
            map.render(player, entities);
            System.out.println();
            
            printHUD();
            
            System.out.print("Action [N/S/E/W/F/Q]: ");
            String input = scan.nextLine().toUpperCase().trim();
            clearScreen();
            processTurn(input);
        }
        
        printEnding(isGoal && player.isAlive());
        scan.close();
    }
}