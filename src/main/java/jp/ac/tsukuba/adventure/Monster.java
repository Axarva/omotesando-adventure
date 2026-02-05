package jp.ac.tsukuba.adventure;

class Monster extends Character {
    Monster(String name, int x, int y) {
        super(name, "XX", x, y, 100, 20);
    }
    @Override
    public void interact(Character character) { System.out.println(name + ": GRRRRR!"); }
    @Override
    public String getPlayerReaction() { return "There is a monster here!"; }
    @Override
    public boolean onCombat(Jibun player) {
        System.out.println("Fight!");
        // Monster hits player
        player.takeDamage(this.atk); 
        System.out.println(player.name + " took " + this.atk + " damage!");
        
        // Player hits monster
        this.takeDamage(player.atk);
        System.out.println(this.name + " took " + player.atk + " damage!");

        System.out.println(player.name + "'s HP: " + player.getHp());
        System.out.println(this.name + "'s HP: " + this.getHp());

        if (!this.isAlive()) {
            System.out.println(this.name + " was defeated!");
            return true; // Signal to Game to remove this monster
        }
        return false;
    }
}
