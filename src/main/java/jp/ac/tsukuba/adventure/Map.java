package jp.ac.tsukuba.adventure;

import java.util.List;

// Map
class Map {
    private final int[][] layout;
    private final int[][] vst;

    Map(int[][] layout) {
        this.layout = layout;
        this.vst = new int[layout.length][layout[0].length];
    }

    public boolean statusBlank(int x, int y) {
        if (y < 0 || y >= layout.length || x < 0 || x >= layout[0].length) return false;
        return layout[y][x] != 0;
    }

    public void validateAndMove(Character c, int tx, int ty) throws MovementException {
        if (statusBlank(tx, ty)) {
            c.setPos(tx, ty);
            vst[ty][tx] = 1; 
        } else {
            throw new MovementException("Wall detected at (" + tx + "," + ty + ")");
        }
    }

    public boolean isGoal(int x, int y) { return layout[y][x] == 2; }
    public void setVisited(int x, int y) { vst[y][x] = 1; }

    // Refactored render uses polymorphism to show any Character type
    public void render(Jibun player, List<Character> entities) {
        for (int i = 0; i < layout.length; i++) {
            for (int j = 0; j < layout[i].length; j++) {
                // Print character displays
                if (i == player.getY() && j == player.getX()) {
                    System.out.print(player.display);
                } else {
                    Character found = null;
                    for (Character e : entities) {
                        if (e.getY() == i && e.getX() == j) {
                            found = e;
                            break;
                        }
                    }

                    if (found != null) {
                        System.out.print(found.display);
                    } else {
                        // Fog Logic
                        boolean visible = false;
                        for (int di = -1; di <= 1; di++) {
                            for (int dj = -1; dj <= 1; dj++) {
                                int cy = i + di; int cx = j + dj;
                                if (cy >= 0 && cy < vst.length && cx >= 0 && cx < vst[0].length) {
                                    if (vst[cy][cx] == 1) visible = true;
                                }
                            }
                        }
                        if (visible) {
                            if (layout[i][j] == 0) System.out.print("##");
                            else if (layout[i][j] == 2) System.out.print(" G");
                            else System.out.print("  ");
                        } else {
                            System.out.print("--");
                        }
                    }
                }
            }
            System.out.println();
        }
    }
}

