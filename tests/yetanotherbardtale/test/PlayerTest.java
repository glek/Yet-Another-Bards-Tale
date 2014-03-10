package yetanotherbardtale.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import yetanotherbardtale.entity.Enemy;
import yetanotherbardtale.entity.Player;
import yetanotherbardtale.entity.item.Potion;
import yetanotherbardtale.entity.item.Weapon;
import yetanotherbardtale.entity.room.*;
import yetanotherbardtale.event.RedoCommand;
import yetanotherbardtale.event.UndoCommand;
import yetanotherbardtale.event.creature.MoveCommand;
import yetanotherbardtale.event.player.*;
import yetanotherbardtale.view.TextView;
import yetanotherbardtale.view.View;

/**
 * Tests all of the commands that the player is given
 * @author Andrew O'Hara (zalpha314)
 * @version 0.1.0
 */
public class PlayerTest {
	private Player player;
	private Enemy enemy;
	private Room room, nextRoom;
	private Potion potion;
	private Weapon weapon;
	PickupCommand pickupWeapon, pickupPotion;
	
	@Before
	public void setup(){
		View view = new TextView();
		player = new Player(view);
		enemy = new Enemy(view, room, "enemy", "a generic enemy", 20);
		room = new Room(view, "room", "a really boring room");
		nextRoom = new Room(view, "nextRoom", "a slightly more interesting room");
		potion = new Potion("monster drink", "lots of caffeine", 5, 0.5);
		weapon = new Weapon("weapon", "a generic weapon", 5, 1); 
		
		player.setRoom(room);
		room.setExit("south", nextRoom);
		nextRoom.setExit("north", room);
		
		pickupWeapon = new PickupCommand("", weapon.getName());
		pickupPotion = new PickupCommand("", potion.getName());
	}

	@Test
	public void testLookAt() {
		ExamineCommand lookAt = new ExamineCommand("", weapon.getName());
		
		// Gracefully handle case when item is not in room
		assertFalse("Player cannot look at weapon", player.canExecute(lookAt));
		
		// Player can look at item
		room.addItem(weapon);
		assertTrue("player can look at weapon", player.canExecute(lookAt));
		player.execute(lookAt);
	}
	
	@Test
	public void testLook(){
		ExamineCommand lookNorth = new ExamineCommand("", "north");
		ExamineCommand lookSouth = new ExamineCommand("", "South");
		
		// Gracefully handle case when there is no room to the north
		assertFalse("no exit to the north", player.canExecute(lookNorth));
		
		// look south
		assertTrue("look south", player.canExecute(lookSouth));
		player.execute(lookSouth);
		
		assertFalse("can't undo NonDelegatable", player.canReverse(lookSouth));
	}
	
	@Test
	public void testMove(){
		MoveCommand moveNorth = new MoveCommand("", "north");
		MoveCommand moveSouth = new MoveCommand("", "South");
		
		// Gracefully handle case when there is no exit to the north
		assertFalse("no exit to the north", player.canExecute(moveNorth));
		
		// move south
		assertTrue("move south", player.canExecute(moveSouth));
		player.execute(moveSouth);
		assertEquals("player in nextRoom", nextRoom, player.getCurrentRoom());
		
		// try undoing
		assertTrue("can undo move south", player.canReverse(moveSouth));
		player.unExecute(moveSouth);
		assertEquals("After undoing, player back at start", room, player.getCurrentRoom());
	}
	
	@Test
	public void testPickup(){		
		// gracefully handle pickup up weapon not in room
		assertFalse("can't pickup weapon since it's not in room", player.canExecute(pickupWeapon));
		
		//add weapon to room, and pick up
		room.addItem(weapon);
		assertTrue("can now pick up weapon", player.canExecute(pickupWeapon));
		player.execute(pickupWeapon);
		assertFalse("weapon no longer in room", room.containsName(weapon.getName()));
		assertFalse("Still cannot pick up potion", player.canExecute(pickupPotion));
		
		//undo pickup
		assertFalse("can't undo pickup potion since didn't pick it up", player.canReverse(pickupPotion));
		assertTrue("can undo pickup weapon", player.canReverse(pickupWeapon));
		player.unExecute(pickupWeapon);
		assertTrue("weapon back in room", room.containsName(weapon.getName()));		
	}
	
	@Test
	public void testDrop(){
		DropCommand dropWeapon = new DropCommand("", weapon.getName());
		DropCommand dropPotion = new DropCommand("", potion.getName());
		
		// Gracefully handle case when trying to drop items which are not carried
		assertFalse("Can't drop weapon", player.canExecute(dropWeapon));
		assertFalse("Can't drop potion", player.canExecute(dropPotion));
		
		// Pickup potion and try to drop
		room.addItem(potion);
		player.execute(pickupPotion);
		assertTrue("Player can drop potion", player.canExecute(dropPotion));
		assertFalse("Can't drop weapon", player.canExecute(dropWeapon));
		player.execute(dropPotion);
		assertTrue("Make sure player dropped potion", room.containsName(potion.getName()));
		
		// Undo dropping potion
		assertTrue("can undo drop", player.canReverse(dropPotion));
		player.unExecute(dropPotion);
		assertFalse("Make sure player pickup up potion", room.containsName(potion.getName()));	
	}
	
	@Test
	public void testUseOn(){
		// Test keys
		Key fakeKey = new Key("fake key", "doesn't open anything", 0.1, "LOL", new TextView());
		Key nextRoomKey = new Key("nextRoom key", "opens the door to the next room", 0.1, "ABCD", new TextView());
		Exit exit = room.getExit("south");
		exit.setCode("ABCD");
		
		UseItemOnCommand useFakeKey = new UseItemOnCommand("", fakeKey.getName(), "south");
		UseItemOnCommand useCorrectKey = new UseItemOnCommand("", nextRoomKey.getName(), "south");
		PickupCommand pickupFakeKey = new PickupCommand("", fakeKey.getName());
		PickupCommand pickupCorrectKey = new PickupCommand("", nextRoomKey.getName());
		
		// Gracefully handle case when player does not have keys
		assertFalse("can't use fake key on door", player.canExecute(useFakeKey));
		assertFalse("can't use correct key on door", player.canExecute(useCorrectKey));
		
		// Test if can unlock doors
		room.addItem(fakeKey);
		room.addItem(nextRoomKey);
		player.execute(pickupCorrectKey);
		player.execute(pickupFakeKey);
		assertFalse("can't use fake key on door", player.canExecute(useFakeKey));
		assertFalse("can't use correct key on door since it is unlocked", player.canExecute(useCorrectKey));
		
		exit.setLocked(true);
		assertTrue("can use correct key on door", player.canExecute(useCorrectKey));
		player.execute(useCorrectKey);
		assertFalse("Exit is now unlocked", exit.isLocked());
		//FIXME IllegalAccessException within the execute.  It will not unlock the door
		
		//TODO maybe add weapons in here later
	}
	
	@Test
	public void testUse(){
		UseItemCommand usePotion = new UseItemCommand("", potion.getName());
		UseItemCommand useWeapon = new UseItemCommand("", weapon.getName());
		
		// Gracefully handle don't have items
		assertFalse("cannot use potion when don't have it", player.canExecute(usePotion));
		assertFalse("Cannot use weapon when don't have it", player.canExecute(useWeapon));
		
		// pick up items and try to use
		room.addItem(weapon);
		room.addItem(potion);
		player.execute(pickupWeapon);
		player.execute(pickupPotion);
		player.hurt(5);
		assertFalse("Player can't use a weapon on itself", player.canExecute(useWeapon));
		assertTrue("Player can use a potion on itself", player.canExecute(usePotion));
		player.execute(usePotion);
		assertEquals("See if the potion worked", player.getMaxHealth(), player.getHealth(), 0);
	}
	
	@Test
	public void testUndoRedo(){
		UndoCommand undo = new UndoCommand("");
		RedoCommand redo = new RedoCommand("");
		ExamineCommand lookSouth = new ExamineCommand("", "south");
		
		assertFalse("Player can't undo since it has done nothing", player.canReverse(undo));
		
		// player can't undo look
		player.execute(lookSouth);
		assertFalse("player can't undo look", player.canReverse(lookSouth));		
		
		// player can undo pickup
		room.addItem(potion);
		player.execute(pickupPotion);
		assertTrue("Player can undo pickup", player.canExecute(undo));
		player.execute(undo);
		assertTrue("potion was dropped", room.containsName(potion.getName()));
		assertFalse("Player cannot undo any more", player.canExecute(undo));
		
		// player can redo the undid pickup
		assertTrue("player can redo the undid pickup", player.canExecute(redo));
		player.execute(redo);
		
		assertFalse("player can't redo any more", player.canExecute(redo));
	}


}
