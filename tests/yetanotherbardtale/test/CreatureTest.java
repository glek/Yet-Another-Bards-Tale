package yetanotherbardtale.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import yetanotherbardtale.entity.Creature;
import yetanotherbardtale.entity.room.Room;
import yetanotherbardtale.event.GameEvent;

public class CreatureTest {
	private Creature creature;
	
	private class CreatureExtension extends Creature{
		
		private CreatureExtension(Room startingRoom, double maxHealth){
			super(startingRoom, maxHealth);
		}
		
		@Override public boolean canReverse(GameEvent e) {return false;}
		@Override public void execute(GameEvent e){}
		@Override public boolean unExecute(GameEvent e) { return false; }
		@Override public boolean canExecute(GameEvent e) { return false; }
		@Override public void triggerDeath() {}
	}
	
	@Before
	public void setup(){
		creature = new CreatureExtension(null, 100.0);
	}
	
	@Test
	public void testHealth(){
		assertEquals("Test creature's current health", 100.0, creature.getHealth(), 0);
		assertEquals("Test creature's max health", 100.0, creature.getMaxHealth(), 0);
		assertFalse("Make sure creature is not dead", creature.isDead());
		
		creature.hurt(3.0);
		
		assertEquals("Test creature's health after taking 3.0 damage", 97.0, creature.getHealth(), 0);
		assertEquals("Make sure creature's max health is unchanged", 100.0, creature.getMaxHealth(), 0);
		assertFalse("Make sure creature is not dead", creature.isDead());
		
		creature.hurt(7.0);
		
		assertEquals("Test creature's health after taking 3.0 damage", 90.0, creature.getHealth(), 0);
		
		creature.heal(10.0);
		
		assertEquals("Test creature's health after being healed for 10.0 damage", 100.0, creature.getHealth(), 0);
		
		creature.setMaxHealth(150.0);
		
		assertEquals("Test creature's max health after being set to 150", 150.0, creature.getMaxHealth(), 0);
		assertEquals("Make sure creature's max health is unchanged", 100,0, creature.getHealth());
		
		creature.hurt(100.0);
		
		assertEquals("Test creature's health after taking 100.0 damage", 0.0, creature.getHealth(), 0);
		assertTrue("Make sure creature is dead after taking 100.0 damage", creature.isDead());		
	}

	/**
	 * Tests movement to rooms, and also tests for case insensitivity
	 * Does not test for locked doors, or null rooms, as that is not the responsibility of Creature
	 */
	@Test
	public void testMove() {
		Room room1 = new Room("office", "there are clikkety sounds");
		Room room2 = new Room("man cave", "there is a sleeping bear");
		
		room1.setExit("East", room2);
		room2.setExit("West", room1);
		
		assertNull("Make sure creature can gracefully handle the case when it has no room", creature.getCurrentRoom());		
		creature.setRoom(room1);		
		assertEquals("Make sure that creature is in room1", room1, creature.getCurrentRoom());		
		creature.move("East");
		assertEquals("Make sure that creature has moved to room2", room2, creature.getCurrentRoom());
		creature.move("WEST");
		assertEquals("Make sure that creature is back in room1 after using capitalized direction", room1, creature.getCurrentRoom());
	}

	

}
