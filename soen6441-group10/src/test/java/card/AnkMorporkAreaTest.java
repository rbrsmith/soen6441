package card;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gameplay.BoardArea;
import gameplay.Die;
import gameplay.Game;
import gameplay.Player;
import io.TextUserInterface;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import card.city.AnkhMorporkArea;
import card.personality.PersonalityCard;
import card.player.GreenPlayerCard;
import card.random.RandomEventCard;
import util.Color;

public class AnkMorporkAreaTest {

	

	Game game;
	Player player;
	Player player2;
	Player player3;
	Map<Integer, BoardArea> gameBoard;
	
	@BeforeClass
	public static void setUpClass() throws Exception {
		// Code executed before the first test method
	}

	@Before
	public void setUp() throws Exception {
		game = new Game();
		String[] playerNames = { "Ross", "Smith", "Rocco"};
		try {
			game.setUp(playerNames.length, playerNames);
		} catch (Exception e) {
			fail("Exception caught");
		}
		player = game.getPlayerOfColor(Color.RED);
		player2 = game.getPlayerOfColor(Color.YELLOW);
		player3 = game.getPlayerOfColor(Color.GREEN);
		
		gameBoard = game.getGameBoard();
		TextUserInterface UI = TextUserInterface.getUI();
		UI.setGame(game);
	}
	
	/**
	 * Test the neighbour rereival
	 * Expecting DOLLY SISTERS, DRAGONS LANDING, UNREAL ESTATE, NAP HILL
	 */
	//@Test
	public void neighbourTest() {
		for(AnkhMorporkArea a: AnkhMorporkArea.getAdjacentAreas(AnkhMorporkArea.DOLLY_SISTERS)) {
			System.out.println(a);
		}
		
		System.out.println("~~~~~~~~~~");
		BoardArea ds = gameBoard.get(AnkhMorporkArea.DOLLY_SISTERS.getAreaCode());
		for(BoardArea ba : game.getNeighbours(ds).values()) { 
			System.out.println(ba.getArea());
		}
		
	}
	
	//@Test
	public void dollySistersTest() {
		System.out.println("~~~TESTING DOLLY SISTERS~~~");
	/* TODO	
		// No area should have minion placed because player cant afford it
		AnkhMorporkArea.getAreaAction(AnkhMorporkArea.DOLLY_SISTERS).accept(player, game);
		BoardArea ds = gameBoard.get(AnkhMorporkArea.DOLLY_SISTERS.getAreaCode());
		for(BoardArea ba : game.getNeighbours(ds).values()) {
			System.out.println(ba.getArea());
			assertEquals(ba.getMinionCount(), 0);
		}
		// Now player can afford it
		player.increaseMoney(3);
		AnkhMorporkArea.getAreaAction(AnkhMorporkArea.DOLLY_SISTERS).accept(player, game);
	*/	
		
	}
	
	//@Test
	public void unrealEstateTest() {
		System.out.println("~~~TESTING UNREAL ESTATE TEST~~~");
		
		AnkhMorporkArea.getAreaAction(AnkhMorporkArea.UNREAL_ESTATE).accept(player, game);
		// Player should have got a card, and lost a card....bringing his card count
		// back to 0
		assertTrue(player.getPlayerCards().size() == 0);	
	}
	
	//@Test
	public void dragonsLandingTest() {
		System.out.println("~~~DRAGONS LANDING TEST~~~");
		AnkhMorporkArea.getAreaAction(AnkhMorporkArea.DRAGONS_LANDING).accept(player, game);
//		assertEquals(player.getMoney(), 2);
		
	}
	
	@Test
	public void smallGodTest() {
		System.out.println("~~~SMALL GODS TEST~~~");
		
		// Give player small gods
		player.addCityCard(gameBoard.get(4).getArea());
		Die.getDie().setCheat(1);
		game.addMinion(gameBoard.get(1).getArea().getAreaCode(), player);
		RandomEventCard.DRAGON.getGameAction().accept(game, player);
		
		
	}
	
	
	
}
	
	