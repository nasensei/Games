package citadels;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    public void testInitialPlayerProperties() {
        // Test: A new player should have 0 gold, no crown, and an empty hand
        Player p = new Player(1);
        assertEquals(1, p.getPlayerNo());
        assertEquals(0, p.getGold());
        assertFalse(p.isCrown());
        assertTrue(p.getDistricts().isEmpty());
    }

    @Test
    public void testGoldSetterAndGetter() {
        // Test: Setting and retrieving gold amount
        Player p = new Player(2);
        p.setGold(5);
        assertEquals(5, p.getGold());
    }

    @Test
    public void testCrownSetterAndGetter() {
        // Test: Setting and checking crown status
        Player p = new Player(3);
        p.setIsCrown(true);
        assertTrue(p.isCrown());
    }

    @Test
    public void testAddDistrictCardToHand() {
        // Test: Adding district card to hand should increase hand size
        Player p = new Player(4);
        DistrictCard card = new DistrictCard("Watchtower", "red", 1, "");
        p.setHand(card);
        List<DistrictCard> hand = p.getDistricts();
        assertEquals(1, hand.size());
        assertEquals("Watchtower", hand.get(0).getName());
    }

    @Test
    public void testCharacterAssignment() {
        // Test: Assigning and retrieving a character
        Player p = new Player(5);
        Character thief = new Character("Thief", "Steals gold", 2);
        p.setCharacter(thief);
        assertEquals("Thief", p.getCharacter().getName());
    }

    @Test
    public void testKilledStatus() {
        // Test: Setting and checking killed status
        Player p = new Player(6);
        assertFalse(p.isKilled());
        p.setKilled(true);
        assertTrue(p.isKilled());
    }
}