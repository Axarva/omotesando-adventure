package jp.ac.tsukuba.adventure;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

public class MapLoader {
    
    public static class MapData {
        public int[][] layout;
        public int startX;
        public int startY;
        
        public MapData(int[][] layout, int startX, int startY) {
            this.layout = layout;
            this.startX = startX;
            this.startY = startY;
        }
    }

    public static MapData loadMap(String filename) throws IOException {
        InputStream is = MapLoader.class.getClassLoader().getResourceAsStream(filename);
        if (is == null) throw new IOException("File not found: " + filename);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            // Map size read
            String line = br.readLine();
            if (line == null) throw new IOException("Map file is empty. Map size header is mandatory.");
            
            int width, height;
            try {
                String[] sizeTokens = line.split(",");
                if (sizeTokens.length < 2) throw new Exception();
                width = Integer.parseInt(sizeTokens[0].trim());
                height = Integer.parseInt(sizeTokens[1].trim());
            } catch (Exception e) {
                throw new IOException("Invalid Header: First line must be 'Width, Height, [Optional Text]'.", e);
            }

            // Optional initial position
            int startX = 1;
            int startY = 1;
            
            line = br.readLine();
            if (line == null) throw new IOException("Map file missing data.");


            boolean firstLineIsMapData = false;
            
            if (line.toLowerCase().contains("initial")) {
                try {
                    String[] initTokens = line.split(",");
                    startX = Integer.parseInt(initTokens[0].trim());
                    startY = Integer.parseInt(initTokens[1].trim());
                } catch (NumberFormatException e) {
                    System.err.println("Warning: 'Initial' header found but unreadable. Defaulting start to (1,1).");
                }
            } else {
                // If no initial keyword, this is first line
                firstLineIsMapData = true;
            }

            int[][] layout = new int[height][width];
            int currentRow = 0;

            if (firstLineIsMapData) {
                parseRow(layout, currentRow++, line, width);
            }

            // Process the rest of the file
            while (currentRow < height && (line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Skip empty lines
                parseRow(layout, currentRow++, line, width);
            }

            // Did we get enough rows?
            if (currentRow < height) {
                System.err.println("Warning: Map file has fewer rows than declared in header. Filling remaining with 0 (Walls).");
            }
            
            return new MapData(layout, startX, startY);
        }
    }

    // Helper to parse a single row of map data
    private static void parseRow(int[][] layout, int row, String line, int width) {
        String[] tokens = line.split(",");
        for (int col = 0; col < width; col++) {
            if (col < tokens.length) {
                try {
                    layout[row][col] = Integer.parseInt(tokens[col].trim());
                } catch (NumberFormatException e) {
                    layout[row][col] = 0; // Wall on bad data
                }
            } else {
                layout[row][col] = 0; // Wall if line is too short
            }
        }
    }
}