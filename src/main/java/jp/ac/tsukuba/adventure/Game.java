package jp.ac.tsukuba.adventure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Game
class Game {
    private final Map map;
    private final Jibun player;
    private final List<Character> entities = new ArrayList<>(); // Unified list for all characters except player
    public boolean isGoal = false;

    Game(String mapFilePath) {
        MapLoader.MapData data = null;
        
        try {
            data = MapLoader.loadMap(mapFilePath);
        } catch (IOException e) {
            System.err.println("CRITICAL ERROR: Could not load game map.");
            e.printStackTrace();
            System.exit(1);
        }

        this.map = new Map(data.layout);
        this.player = new Jibun("", "OO", data.startX, data.startY); // Set from file!
        this.map.setVisited(data.startX, data.startY);
        
        entities.add(new Murabito("Old Man", 8, 4, "The exit is to the North-East!"));
        entities.add(new Murabito("Guard", 3, 1, "Don't get lost in the dark."));
        entities.add(new Monster("Slime", 3, 2));
        entities.add(new Monster("Goblin", 5, 6));
    }

    void processTurn(String input) {
        if ("F".equals(input)) { 
            handleCombat(); 
            return; 
        }

        int tx = player.getX();
        int ty = player.getY();
        String dir = "";

        switch (input) {
            case "N": ty--; dir = "North"; break;
            case "S": ty++; dir = "South"; break;
            case "E": tx++; dir = "East";  break;
            case "W": tx--; dir = "West";  break;
            default: return;
        }

        try {
            map.validateAndMove(player, tx, ty);
            System.out.println("Go " + dir + "!");
            
            // Single loop for both Murabitos and Monsters 
            for (Character e : entities) {
                if (e.getX() == player.getX() && e.getY() == player.getY()) {
                    System.out.println(player.name + ": " + e.getPlayerReaction()); // Polymorphic reaction
                    e.interact(); // Polymorphic call
                }
            }

            if (map.isGoal(player.getX(), player.getY())) {
                System.out.println("GOAL!");
                isGoal = true;
            }
        } catch (MovementException e) {
            // Specific handling for hitting a wall
            System.out.println("THUD! " + e.getMessage()); 
        }
    }

    private void handleCombat() {
        boolean monsterFound = false;
        for (int i = 0; i < entities.size(); i++) {
            Character e = entities.get(i);
            // Check if player is on the same spot as the entity
            if (e.getX() == player.getX() && e.getY() == player.getY()) {
                monsterFound = true;
                
                // The entity handles its own combat
                boolean shouldRemove = e.onCombat(player);
                
                if (shouldRemove) {
                    entities.remove(i); // Remove from map 
                }
                break; 
            }
        }
        
        if (!monsterFound) {
            System.out.println("There is no monster to fight here.");
        }
    }

    public void run() {
        Scanner scan = new Scanner(System.in);
        // Name setup logic 
        while (player.name.length() == 0) {
            System.out.println("Welcome to your maze adventure!");
            System.out.println("First, tell us your name!");
            String nameInput = scan.nextLine();
            System.out.println("So your name is " + nameInput + "? (Y/N)");
            if ("Y".equalsIgnoreCase(scan.nextLine())) {
                player.name = nameInput;
            }
        }

        while (!isGoal && player.isAlive()) {
            map.render(player, entities);
            System.out.println("Health point = " + player.getHp());
            System.out.println("Location: (" + player.getX() + "," + player.getY() + ")");
            System.out.print("Input (N,E,W,S,F): ");
            processTurn(scan.nextLine().toUpperCase());
        }
        if (!player.isAlive()) System.out.println("GAME OVER...");
        scan.close();
    }
}