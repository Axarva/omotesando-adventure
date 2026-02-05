package jp.ac.tsukuba.adventure;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void testMapLoading() {
        // Verify map loads without crashing
        assertDoesNotThrow(() -> MapLoader.loadMap("map.txt"));
    }

    @Test
    void testPlayerDamage() {
        Jibun hero = new Jibun("Hero", "H", 0, 0);
        int startHp = hero.getHp(); // 200
        hero.takeDamage(50);
        assertEquals(startHp - 50, hero.getHp(), "HP should decrease by damage amount");
    }
    
    @Test
    void testMonsterCombatLogic() {
        Monster slime = new Monster("Slime", 0, 0);
        Jibun hero = new Jibun("Hero", "H", 0, 0);
        
        // Simulating combat logic
        boolean defeat = slime.onCombat(hero);
        
        // Hero takes 20 dmg, Slime takes 50 dmg
        assertEquals(180, hero.getHp());
        assertEquals(50, slime.getHp());
        assertFalse(defeat, "Slime should still be alive");
    }
}