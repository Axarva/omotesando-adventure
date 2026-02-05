package jp.ac.tsukuba.adventure;

class Jibun extends Character {
    Jibun(String name, String display, int x, int y) { super(name, display, x, y, 200, 50); }
    @Override 
    public void interact(Character character) {} // Player cannot interact with self
    @Override
    public String getPlayerReaction() { return "I can't be talking to myself..."; }
    @Override
    public boolean onCombat(Jibun player) {
        // This should never be called by the game loop
        System.out.println("Wait, I can't fight myself!");
        return false; 
    }
}
