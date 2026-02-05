package jp.ac.tsukuba.adventure;

class Murabito extends Character {
    private String hint;
    Murabito(String name, int x, int y, String hint) {
        super(name, "MM", x, y, 100, 0);
        this.hint = hint;
    }
    @Override
    public void interact(Character character) { System.out.println(name + ": " + hint); }
    @Override
    public String getPlayerReaction() { return "I've got to get out of here."; }   
    @Override
    public boolean onCombat(Jibun player) {
        System.out.println(name + ": Wait! I'm just a villager! Don't hit me!");
        return false; // Villagers don't die in combat
    }
}

