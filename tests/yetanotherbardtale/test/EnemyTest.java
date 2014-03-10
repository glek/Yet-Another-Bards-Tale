package yetanotherbardtale.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import yetanotherbardtale.entity.Enemy;
import yetanotherbardtale.entity.item.Potion;
import yetanotherbardtale.entity.item.Weapon;
import yetanotherbardtale.entity.room.Room;
import yetanotherbardtale.event.creature.*;
import yetanotherbardtale.view.TextView;
import yetanotherbardtale.view.View;

public class EnemyTest {
	private Room pigSty;
	private Room slaughterHouse;
	private Enemy piggy;
	private MoveCommand move;
	private View view;
	
	
	@Before
	public void setup(){
		view = new TextView();
		pigSty = new Room(view, "pig sty", "lots of oink sounds");
		slaughterHouse = new Room(view, "slaughter house", "the glorious place where the bacon comes out!");
		pigSty.setExit("trapdoor", slaughterHouse);
		
		piggy = new Enemy(view, pigSty, "Piggy", "It has a curly tail", 5);
		pigSty.setEnemy(piggy);
		
		move = new MoveCommand("", "");
	}

	@Test
	public void testMove() {		
		// Gracefully handle erreneous case where piggy not in room.
		pigSty.setEnemy(null);
		assertTrue("Gracefully handle moveCommand when not in room", piggy.canExecute(move));
		piggy.execute(move); // Should not throw exception
		
		// Randomly move
		pigSty.setEnemy(piggy);
		assertTrue("Piggy can move", piggy.canExecute(move));
		piggy.execute(move);
		assertEquals("Piggy moved to only available room", piggy, slaughterHouse.getEnemy());
		
		// try to go back through trapdoor
		piggy.execute(move);
		assertEquals("Piggy could not move", piggy, slaughterHouse.getEnemy());
		
		// Ensure piggy cannot go through locked door
		piggy.setRoom(pigSty);
		pigSty.setEnemy(piggy);
		slaughterHouse.setEnemy(null);
		pigSty.setLockAllExits(true);
		piggy.execute(move);		
		assertEquals("Piggy could not move", piggy, pigSty.getEnemy());
	}
	
	@Test
	public void testUndoMove(){	
		//Gracefully handle case when cannot undo command
		assertFalse("Piggy cannot undo move when has not moved", piggy.canReverse(move));
		
		// Move, then try to reverse
		piggy.execute(move);
		assertTrue("Piggy can now undo move", piggy.canExecute(move));
		piggy.unExecute(move);
		assertEquals("Piggy now back where it started", piggy, pigSty.getEnemy());
	}
	
	@Test
	public void testDamage(){
		Weapon stick = new Weapon("stick", "it hurts", 0.5, 0.2);
		Weapon axe = new Weapon("axe", "it chops", 10, 5);
		
		ItemUsedOnCreatureCommand hurtStick = new ItemUsedOnCreatureCommand("", stick);
		ItemUsedOnCreatureCommand chopAxe = new ItemUsedOnCreatureCommand("", axe);	
		
		// hurt piggy
		assertTrue("Piggy can be hurt by stick", piggy.canExecute(hurtStick));
		piggy.execute(hurtStick);
		assertEquals("Piggy was damaged by stick", piggy.getMaxHealth() - stick.getDamage(), piggy.getHealth(), 0);
		
		//undo hurt piggy
		assertTrue("can undo hurt event", piggy.canReverse(hurtStick));
		piggy.unExecute(hurtStick);
		assertEquals("Piggy's health was fixed", piggy.getMaxHealth(), piggy.getHealth(), 0);
		
		//kill piggy and make sure dead
		piggy.execute(chopAxe);
		assertEquals("piggy health doesn't go below 0", 0, piggy.getHealth(), 0);
		assertTrue("piggy is dead", piggy.isDead());
	}
	
	@Test
	public void testHeal(){
		Potion potion = new Potion("banana smoothie", "yummy", 10, 1);
		ItemUsedOnCreatureCommand givePotion = new ItemUsedOnCreatureCommand("", potion);
		
		piggy.hurt(2);
		
		// Heal piggy
		assertTrue("Piggy can use potion", piggy.canExecute(givePotion));
		piggy.execute(givePotion);
		assertEquals("Piggy healed, but not over max health", piggy.getMaxHealth(), piggy.getHealth(), 0);
		
		// undo heal piggy
		assertTrue("Piggy can undo healing", piggy.canReverse(givePotion));
		piggy.unExecute(givePotion);
		assertEquals("Piggy undid healing, but did not undo too much healing", 
				piggy.getMaxHealth() - 2, piggy.getHealth(), 0);
	}

}
