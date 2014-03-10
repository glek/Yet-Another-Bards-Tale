package yetanotherbardtale.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import yetanotherbardtale.entity.Enemy;
import yetanotherbardtale.entity.item.Item;
import yetanotherbardtale.entity.item.Weapon;
import yetanotherbardtale.entity.room.Exit;
import yetanotherbardtale.entity.room.Room;
import yetanotherbardtale.event.room.RoomAddItemCommand;
import yetanotherbardtale.event.room.RoomExamineCommand;
import yetanotherbardtale.event.room.RoomItemExamineCommand;
import yetanotherbardtale.event.room.RoomItemUsedCommand;
import yetanotherbardtale.event.room.RoomItemUsedOnCreatureCommand;
import yetanotherbardtale.event.room.RoomRemoveItemCommand;
import yetanotherbardtale.view.TextView;
import yetanotherbardtale.view.View;

/**
 * 
 * @author Andrew O'Hara (zalpha314)
 * @version 0.1.1
 */
public class RoomTest {
	
	private Room room1, room2, room3;
	private Enemy alien;
	private Item tube, phone;
	private Weapon lightSaber;
	private View view;
	
	@Before
	public void setup(){
		view = new TextView();
		
		room1 = new Room("Office", "There are clickety sounds");
		room2 = new Room("man cave", "There is a sleeping bear inside");
		room3 = new Room("kitchen", "This is where the dragons are cooked");
		
		alien = new Enemy(room1, "alien", "a black, lizard-like fiend", 9000);
		alien.setView(view);
		
		tube = new yetanotherbardtale.entity.item.BasicItem("tube", "it is cylindrical", 0.5, view);
		phone = new yetanotherbardtale.entity.item.BasicItem("phone", "with a vibrant 32x16 display!", 4.0, view);
		lightSaber = new Weapon("lightSaber", "a red one...", 100.0, 1000000.0);
	}

	@Test
	public void enemyTest() {
		assertNull("Make sure that Room gracefully handles no enemy in room", room1.getEnemy());
		room1.setEnemy(alien);
		assertTrue("Now that enemy has been added, make sure room knows there is an enemy", room1.hasEnemy());
		assertEquals("make sure that the room returns the same enemy that was added", alien, room1.getEnemy());
	}
	
	@Test
	public void itemTest(){
		// Test gracefully finds no results
		assertFalse("Room knows there are no items in room", room1.containsName(tube.getName()));
		assertNull("Gracefully handles searching for item that doesn't exist", room1.findItemByName(tube.getName()));
		
		// With one item
		room1.addItem(tube);		
		assertTrue("Room knows the tube is in the room", room1.containsName(tube.getName()));
		assertEquals("Room can retrieve the tube", tube, room1.findItemByName(tube.getName()));
		
		// With two items
		room1.addItem(phone);
		assertTrue("Room knows the tube is in the room", room1.containsName(tube.getName()));
		assertEquals("Room can retrieve the tube", tube, room1.findItemByName(tube.getName()));
		assertTrue("Room knows the phone is in the room", room1.containsName(phone.getName()));
		assertEquals("Room can retrieve the phone", phone, room1.findItemByName(phone.getName()));
		
		// Remove one item
		room1.removeItem(tube);
		assertTrue("Room knows the phone is in the room", room1.containsName(phone.getName()));
		assertEquals("Room can retrieve the phone", phone, room1.findItemByName(phone.getName()));
		
		// Remove both items
		assertFalse("Room knows there are no items in room", room1.containsName(tube.getName()));
		assertNull("Gracefully handles searching for item that doesn't exist", room1.findItemByName(tube.getName()));
	}
	
	@Test
	public void noExitsTest(){
		assertNull("Room gracefully handles no exit found", room1.getExit("south"));
		assertNull("Room gracefully handles no random direction found", room1.getRandomUnlockedDirection());
	}
	
	private void testExit(String exit, Room origin, Room dest){		
		// Test Exit find methods
		assertTrue("Room1 knows the exit exists", origin.containsExit(exit));
		assertTrue("Room1 knows the exit exists with caps", origin.containsExit(exit.toUpperCase()));
		assertEquals("Room1 can find the exit", dest, origin.getExit(exit).getRoom());
		assertEquals("Room1 can find the exit with caps", dest, origin.getExit(exit.toUpperCase()).getRoom());
		
		// Test generic find methods
		assertTrue("Room1 knows the exit exists from generic search method", origin.containsName(exit));
		assertTrue("Room1 knows the exit exists from generic search method with caps", origin.containsName(exit.toUpperCase()));
		assertEquals("Room1 can find the exit from generic find method", dest, ((Exit)origin.findItemByName(exit)).getRoom());
		assertEquals("Room1 can find the exit from generic find method with caps", dest, ((Exit)origin.findItemByName(exit.toUpperCase())).getRoom());
		
		// Test get unlocked direction
		assertNotNull("Room can find random unlocked direction", origin.getRandomUnlockedDirection());		
		origin.setLockAllExits(true);
		assertNull("Room can not find random unlocked direction after locking all", origin.getRandomUnlockedDirection());
		origin.setLockAllExits(false);
		assertNotNull("Room can find random unlocked direction after unlocking all", origin.getRandomUnlockedDirection());	
	}
	
	@Test
	public void testExits(){
		// Test with one exit
		room1.setExit("South", room2);		
		testExit("South", room1, room2);
		
		// Test with two exits
		room2.setExit("East", room3);
		testExit("East", room2, room3);
		testExit("South", room1, room2);
		
		// Test with three exits
		room3.setExit("cannon", room1);
		testExit("East", room2, room3);
		testExit("Cannon", room3, room1);
		testExit("South", room1, room2);
	}
	
	/**
	 * This test makes sure that having both items and exits at 
	 * the same time will not mess up the search functios
	 */
	@Test
	public void itemExitConflictTest(){
		Item bowl = new yetanotherbardtale.entity.item.BasicItem("bowl", "with ice cream in it", 0.5, view);
		
		room1.setExit("west", room2);
		room2.setExit("east", room1);
		room1.addItem(phone);
		room1.addItem(tube);
		room2.addItem(bowl);
		
		// Test exits with items in room
		testExit("west", room1, room2);
		testExit("east", room2, room1);
		
		assertEquals("Try to get tube while there are exits in room", tube, room1.findItemByName(tube.getName()));
		assertEquals("Try to get phone while there are exits in room", phone, room1.findItemByName(phone.getName()));
		assertEquals("Try to get bowl while there are exits in room", bowl, room2.findItemByName(bowl.getName()));
	}
	
	@Test
	public void testExecute(){
		// Declare events
		RoomAddItemCommand addTube = new RoomAddItemCommand("", tube);	
		RoomItemExamineCommand examineTube = new RoomItemExamineCommand("", tube.getName());
		RoomItemExamineCommand examinePhone = new RoomItemExamineCommand("", phone.getName());		
		RoomRemoveItemCommand removeTube = new RoomRemoveItemCommand("", tube);
		RoomRemoveItemCommand removePhone = new RoomRemoveItemCommand("", phone);
		RoomExamineCommand examineRoom = new RoomExamineCommand("");
		
		assertTrue("Room can be examined", room1.canExecute(examineRoom));
		room1.execute(examineRoom);

		assertFalse("Room cannot examine item when room is empty", room1.canExecute(examineTube));
		assertFalse("Room cannot remove tube when room is empty", room1.canExecute(removeTube));
		
		assertTrue("Room can add tube", room1.canExecute(addTube));
		room1.execute(addTube);
		
		assertTrue("After adding tube, Room can examine tube", room1.canExecute(examineTube));
		room1.execute(examineTube);
		assertFalse("Room cannot examine phone, which is still not there", room1.canExecute(examinePhone));
		
		assertFalse("Room cannot remove phone, which is still not there", room1.canExecute(removePhone));		
		assertTrue("Room can remove tube", room1.canExecute(removeTube));
		room1.execute(removeTube);
		
		assertFalse("Room can no longer examine tube, which has been removed", room1.canExecute(examineTube));
	}
	
	@Test
	public void testExecuteUseItem(){
		// Declare events
		RoomAddItemCommand addLightSaber = new RoomAddItemCommand("", lightSaber);
		RoomItemUsedCommand useLightSaber = new RoomItemUsedCommand("", lightSaber);
		RoomItemUsedOnCreatureCommand lightSaberOnAlien = new RoomItemUsedOnCreatureCommand("", alien, lightSaber);
		
		//Initial graceful fail tests
		assertFalse("cannot use lightSaber since it is not in room", room1.canExecute(useLightSaber));
		assertFalse("cannot use lightSaber on alien since they are not in the room", room1.canExecute(lightSaberOnAlien));
		
		// Add Lightsaber
		assertTrue("Can add lightSaber", room1.canExecute(addLightSaber));
		room1.execute(addLightSaber);
		assertFalse("cannot use lightSaber since it is not directly usable by player", room1.canExecute(useLightSaber));
		assertFalse("cannot use lightSaber on alien since alien is not in the room", room1.canExecute(lightSaberOnAlien));
		
		// Use lightsaber on enemy
		room1.setEnemy(alien);
		assertTrue("can use lightSaber on alien since alien is now in the room", room1.canExecute(lightSaberOnAlien));
		room1.execute(lightSaberOnAlien);
		assertEquals("lightsaber hurt alien", alien.getMaxHealth() - lightSaber.getDamage(), alien.getHealth(), 0);
	}
	
	@Test
	public void testUnExecute(){
		// Declare events
		RoomAddItemCommand addLightSaber = new RoomAddItemCommand("", lightSaber);
		RoomItemUsedOnCreatureCommand lightSaberOnAlien = new RoomItemUsedOnCreatureCommand("", alien, lightSaber);
		
		assertFalse("cannot undo add lightSaber", room1.canReverse(addLightSaber));		
		room1.execute(addLightSaber);			
		
		//Undo lightSaberOnAlien
		assertTrue("cannot undo lightsaberOnAlien since alien not in room", room1.canReverse(lightSaberOnAlien));
		room1.setEnemy(alien);
		room1.execute(lightSaberOnAlien);
		assertTrue("cannot undo lightsaberOnAlien since already undone", room1.canReverse(lightSaberOnAlien));
		
		// Undo add lightSaber
		assertTrue("Can undo add lightSaber", room1.canReverse(addLightSaber));
		room1.unExecute(addLightSaber);
		assertNull("LightSaber removed succesfully", room1.findItemByName(lightSaber.getName()));
		assertFalse("cannot undo add lightSaber", room1.canReverse(addLightSaber));	
	}

}
