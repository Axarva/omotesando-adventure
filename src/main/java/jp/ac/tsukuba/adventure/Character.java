package jp.ac.tsukuba.adventure;

// Characters
abstract class Character {
    protected int x, y, hp, atk;
    protected String name, display;

    Character(String name, String display, int x, int y, int hp, int atk) {
        this.name = name; 
        this.display = display;
        this.x = x; 
        this.y = y;
        this.hp = hp; 
        this.atk = atk;
    }

    // Method for meeting entities 
    public abstract void interact(Character character);

    // Method for entities to get player reaction
    public abstract String getPlayerReaction();

    // Fight
    public abstract boolean onCombat(Jibun player);
    
    public void setPos(int x, int y) { this.x = x; this.y = y; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getHp() { return hp; }
    public void takeDamage(int d) { this.hp -= d; }
    public boolean isAlive() { return hp > 0; }
}


