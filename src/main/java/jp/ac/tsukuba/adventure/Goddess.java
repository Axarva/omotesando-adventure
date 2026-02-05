package jp.ac.tsukuba.adventure;

import java.util.Scanner;

public class Goddess extends Character {
    
    public Goddess(String name, int x, int y) {
        // High HP, 0 Attack, Display "GD"
        super(name, "GD", x, y, 999, 0);
    }

    @Override
    public void interact(Character character) {
        System.out.println(name + ": You look tired. Let me heal your wounds.");
        Scanner scan = new Scanner(System.in);
        System.out.print("Heal your wounds? (Y/N): ");
        String input = scan.nextLine().toUpperCase();
        if ("Y".equals(input)) {
            heal(character);
            return;
        } else {
            System.out.println(name + ": So be it.");
            return;
        }
        

    }

    @Override
    public String getPlayerReaction() {
        return "I feel a divine presence...";
    }

    @Override
    public boolean onCombat(Jibun player) {
        System.out.println(name + ": Violence is not the answer.");
        return false;
    }
    
    public void heal(Character character) {
        int healAmount = 50;
        character.takeDamage(-healAmount); // Negative damage = Heal
        System.out.println(">>> " + character.name + " recovered " + healAmount + " HP! <<<");
    }
}