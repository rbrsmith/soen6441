package card;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import gameplay.BoardArea;
import gameplay.Game;
import gameplay.Player;

import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import util.Color;
import card.player.Symbol;

public class SymbolTest {

	Game game;
	Player player;
	Map<Integer, BoardArea> gameBoard;

	@BeforeClass
	public static void setUpClass() throws Exception {
		// Code executed before the first test method
	}

	@Before
	public void setUp() throws Exception {
		game = new Game();
		String[] playerNames = { "Ross", "Smith" };
		try {
			game.setUp(playerNames.length, playerNames);
		} catch (Exception e) {
			fail("Exception caught");
		}
		player = game.getPlayerOfColor(Color.RED);
		gameBoard = game.getGameBoard();
	}

	/**
	 * Test PLACE_MINION symbol
	 * Condition that player has no free minions
	 */
//	@Test
	public void placeMinionNoMinionsTest() {

		System.out.println("~~PLACE MINION NO MINIONS TEST~~");

		Symbol placeMinionSymbol = Symbol.PLACE_MINION;
		// Fill up an area so player has no minions
		BoardArea secondArea = gameBoard.get(1);
		while (player.getMinionCount() != 0) {
			secondArea.addMinion(player);
		}
		int secondAreaMinionsBefore = secondArea.getMinionCountForPlayer(player);
		placeMinionSymbol.getGameAction().accept(player, game);
		int secondAreaMinionsAfter = secondArea.getMinionCountForPlayer(player);
		assertEquals(secondAreaMinionsAfter, secondAreaMinionsBefore - 1);
	}

	
	/**
	 * Test PLACE_MINION symbol
	 * Condition that player has only free minions
	 */
//	@Test
	public void placeMinionFullMinionsTest() {

		System.out.println("~~PLACE MINION FULL MINIONS TEST~~");

		Symbol placeMinionSymbol = Symbol.PLACE_MINION;
		// Make Sure player has all his minions
		assertTrue(player.getMinionCount() == Player.TOTAL_MINIONS);
		placeMinionSymbol.getGameAction().accept(player, game);
		assertEquals(player.getMinionCount(), Player.TOTAL_MINIONS - 1);

	}

	/**
	 * Test PLACE_MINIONS symbol
	 * Condition that payer has some free minions
	 */
//	@Test
	public void placceMinionPartialMinionsTest() {

		System.out.println("~~PLACE MINION PARTIAL MINIONS TEST~~");

		Symbol placeMinionTest = Symbol.PLACE_MINION;

		// Take away some of the players minions
		BoardArea area = gameBoard.get(1);
		area.addMinion(player);
		assertEquals(player.getMinionCount(), Player.TOTAL_MINIONS - 1);
		placeMinionTest.getGameAction().accept(player, game);
		assertEquals(player.getMinionCount(), Player.TOTAL_MINIONS - 2);
	}

	/**
	 * Test symbol place a building if player has no free buildings to place
	 */
	@Test
	public void placeBuildingNoFreeBuildingsTest() {
		
		System.out.println("~~PLACE BUILDING NO BUILDINGS TEST~~");
		
		// Remove all of players buildings
		int i = 1;
		player.increaseMoney(10000);
		int previousPlayerBalance = player.getMoney();
		while(player.getBuildings() != 0) {
			game.addBuilding(player, gameBoard.get(i));
			i++;
		}
		
		Symbol.PLACE_A_BUILDING.getGameAction().accept(player, game);
		
		// Ensure player has not lost any buildings
		assertEquals(player.getBuildings(), 0);
		// Ensure building cost something
		assertTrue(player.getMoney() < previousPlayerBalance);
		
		
	}
	
	/**
	 * Test symbol place a building if player has free buildings to place
	 */
	@Test
	public void placeBuildingFreeBuildingsTest() {
		
		System.out.println("~~PLACE BUILDING FREE BUILDINGS TEST~~");
		
		// Give player unlimited funds
		player.increaseMoney(10000);
		int previousPlayerBalance = player.getMoney();
		
		Symbol.PLACE_A_BUILDING.getGameAction().accept(player, game);
		// Ensure that the players buildings have decreased
		assertEquals(player.getBuildings(), Player.TOTAL_BUILDINGS - 1);
		// Ensure building cost something
		assertTrue(player.getMoney() < previousPlayerBalance);
		
	}
	
	
	@After
	public void tearDown() throws Exception {
		// Code executed after each test
		System.out.println();
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		// Code executed after the last test method
	}
}
