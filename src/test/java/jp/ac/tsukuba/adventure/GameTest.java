package jp.ac.tsukuba.adventure;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;

class GameTest {

    // Map loading and I/O

    @Test
    void testMapLoadingValid() {
        // Should not throw exception
        assertDoesNotThrow(() -> MapLoader.loadMap("map.txt"));
    }

    @Test
    void testMapLoadingInvalidFile() {
        // Should throw IOException for missing file
        assertThrows(IOException.class, () -> MapLoader.loadMap("does_not_exist.txt"));
    }

    // Player stuff

    @Test
    void testPlayerInitialStats() {
        Jibun hero = new Jibun("Hero", "H", 1, 1);
        assertEquals(200, hero.getHp());
        assertEquals("Hero", hero.name);
    }

    @Test
    void testPlayerDamage() {
        Jibun hero = new Jibun("Hero", "H", 0, 0);
        hero.takeDamage(50);
        assertEquals(150, hero.getHp());
    }

    @Test
    void testPlayerDeath() {
        Jibun hero = new Jibun("Hero", "H", 0, 0);
        hero.takeDamage(200); // Fatal damage
        assertFalse(hero.isAlive(), "Hero should be dead at 0 HP");
    }

    @Test
    void testPlayerHealing() {
        Jibun hero = new Jibun("Hero", "H", 0, 0);
        hero.takeDamage(100); // 100 HP left
        
        Goddess goddess = new Goddess("Athena", 0, 0);
        goddess.heal(hero); // Heals 50
        
        assertEquals(150, hero.getHp(), "Heal should restore HP");
    }

    // Combat

    @Test
    void testMonsterCreation() {
        Monster mob = new Monster("Orc", 5, 5);
        assertEquals(100, mob.getHp());
        assertTrue(mob.isAlive());
    }

    @Test
    void testMonsterTakesDamage() {
        Monster mob = new Monster("Orc", 0, 0);
        
        // Hero hitting monster logic manually
        mob.takeDamage(50); // Direct damage test
        assertEquals(50, mob.getHp());
    }

    @Test
    void testCombatFlow() {
        Monster mob = new Monster("Slime", 0, 0);
        Jibun hero = new Jibun("Hero", "H", 0, 0);
        
        boolean dead = mob.onCombat(hero);
        
        // Hero takes 20 (Monster ATK), Monster takes 50 (Hero ATK)
        assertEquals(180, hero.getHp()); 
        assertEquals(50, mob.getHp());
        assertFalse(dead, "Monster still has HP");
    }

    @Test
    void testMonsterDefeat() {
        Monster mob = new Monster("Weakling", 0, 0);
        Jibun hero = new Jibun("Hero", "H", 0, 0);
        
        mob.takeDamage(90); // 10 HP left
        boolean dead = mob.onCombat(hero); // Hero hits for 50
        
        assertTrue(dead, "Monster should be defeated");
        assertFalse(mob.isAlive());
    }

    // NPC test

    @Test
    void testGoddessNonHostile() {
        Goddess g = new Goddess("Athena", 0, 0);
        Jibun hero = new Jibun("Hero", "H", 0, 0);
        
        // Combat should return false (no fight)
        assertFalse(g.onCombat(hero));
    }

    @Test
    void testMurabitoNonHostile() {
        Murabito m = new Murabito("Villager", 0, 0, "Hello");
        Jibun hero = new Jibun("Hero", "H", 0, 0);
        
        assertFalse(m.onCombat(hero));
    }

    // Map tests

    @Test
    void testMapMovement() {
        // 3x3 empty map
        int[][] layout = { {1,1,1}, {1,1,1}, {1,1,1} };
        Map map = new Map(layout);
        Jibun hero = new Jibun("H", "H", 1, 1);
        
        assertDoesNotThrow(() -> map.validateAndMove(hero, 1, 2));
        assertEquals(2, hero.getY());
    }

    @Test
    void testMapCollision() {
        // 0 = Wall
        int[][] layout = { {0,0}, {0,0} };
        Map map = new Map(layout);
        Jibun hero = new Jibun("H", "H", 0, 0); // Technically inside a wall, but trying to move
        
        assertThrows(MovementException.class, () -> map.validateAndMove(hero, 0, 1));
    }

    @Test
    void testGoalDetection() {
        // 2 = Goal
        int[][] layout = { {2} };
        Map map = new Map(layout);
        assertTrue(map.isGoal(0, 0));
    }
}