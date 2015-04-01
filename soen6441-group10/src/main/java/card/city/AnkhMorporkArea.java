package card.city;

import gameplay.BoardArea;
import gameplay.Game;
import gameplay.Player;
import io.TextUserInterface;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import card.Card;
import card.player.GreenPlayerCard;
import card.player.Symbol;


/**
 * An enumeration of all the areas available in the game. Each area is uniquely
 * identified by a number (which is visible on the physical game board).<br>
 * 
 * Note that this area implements the Card marker interface, because it serves as
 * the enumeration of all the City Area cards in the game while {@link CityAreaCard}
 * actually bears the state of the card (played or not, playable more than once etc.).
 * 
 * @author Team 10 - SOEN6441
 * @version 2.0
 */
public enum AnkhMorporkArea implements Card {

	DOLLY_SISTERS(1, 6, "Once per turn you can pay $3 and place one of your minions "
			+ "in Dolly Sisters or an adjacent area."),

	UNREAL_ESTATE(2, 18, "Once per turn you can take a card and then discard a card."),

	DRAGONS_LANDING(3, 12, "Once per turn you can take $2 from the bank."),

	SMALL_GODS(4, 18, "Whenever one of your pieces is affected by a random event, you"
			+ "can pay $3 to ignore the effect ($3 for each piece)."),

	THE_SCOURS(5, 6, "Once per turn you can discard one card and take $2 from the bank."),

	THE_HIPPO(6, 12, "Once per turn you can take $2 from the bank."),

	THE_SHADES(7, 6, "Once per turn your may place a trouble marker in or adjacent "
			+ "to the Shades - there must be at least one minion in the area where "
			+ "the marker is placed."),

	DIMWELL(8, 6, "Once per turn you can pay $3 and place one of your minions "
			+ "in Dimwell or an adjacent area."),

	LONGWALL(9, 12, "Once per turn you can take $1 from the bank."),

	ISLE_OF_GODS(10, 12, "Once per turn you can pay $2 and remove a trouble marker "
			+ "from the board."),

	SEVEN_SLEEPERS(11, 18, "Once per turn you can take $3 from the bank."),

	NAP_HILL(12, 12, "Once per turn you can take $1 from the bank.");

	private static final int MINION_COST = 3;
	
	protected static final Map<Integer, AnkhMorporkArea> codeToAreaMap = new HashMap<>();
	static {
		for (AnkhMorporkArea a : AnkhMorporkArea.values()) {
			codeToAreaMap.put(a.getAreaCode(), a);
		}
	}
	
	private static final Map<AnkhMorporkArea, BiConsumer<Player, Game>> cityCardFunctionMap
		= new HashMap<>();
	static { 
		
		// 1. DOLLY_SISTERS
		cityCardFunctionMap.put(DOLLY_SISTERS, (player, game) -> {
			payAndAddMinion(DOLLY_SISTERS, player, game);
		}); 
		
		// 2. UNREAL ESTATE
		cityCardFunctionMap.put(UNREAL_ESTATE, (player, game) -> {
			// Draw one card and then discard one card
			if(game.addPlayerCard(player)) {
				System.out.println("Added new player card");
				TextUserInterface UI = TextUserInterface.getUI();
				GreenPlayerCard discardCard = UI.getCardChoice(player.getPlayerCards(), 
					"Choose a card to discard: ");
				game.discardCard(discardCard, player);
				System.out.println("Discarded player card");
			}
		});
		
		// 3. DRAGON'S LANDING
		cityCardFunctionMap.put(DRAGONS_LANDING, (player, game) -> {
			System.out.println("Giving player 2$");
			game.givePlayerMoneyFromBank(player, 2);
		});
		
		// 4. SMALL GODS
		cityCardFunctionMap.put(SMALL_GODS, (player, game) -> {
			TextUserInterface UI = TextUserInterface.getUI();
			boolean protect = UI.getUserYesOrNoChoice("Protect your piece/building using \"Small Gods\"?" +
					"(Cost: $3)?");
			if (protect && player.decreaseMoney(3)) {
				game.getBank().increaseBalance(3);
				System.out.println("Piece/building protected.");
			} else {
				System.out.println("Not enough money to protect your piece/building.");
			}
		});
		
		// 5. THE SCOURS
		cityCardFunctionMap.put(THE_SCOURS, (player, game) -> {
			TextUserInterface UI = new TextUserInterface();
			Collection<GreenPlayerCard> playerCards = player.getPlayerCards();
			GreenPlayerCard discardCard = UI.getCardChoice(playerCards, "Choose a card to discard: ");
			player.removePlayerCard(discardCard);
			System.out.println(discardCard + " removed.");

			game.givePlayerMoneyFromBank(player, 2);
		});
		
		// 6. THE HIPPO
		cityCardFunctionMap.put(THE_HIPPO, (player, game) -> {
			game.givePlayerMoneyFromBank(player, 2);
		});
		
		// 7. THE SHADES
		cityCardFunctionMap.put(THE_SHADES, (player, game) -> {
			TextUserInterface UI = new TextUserInterface();
			Collection<AnkhMorporkArea> shadesAndAdjacentWithMinions = 
					getAreaAndAdjacentAreas(THE_SHADES)
						.stream()
						.filter(a -> game.getMinionCountForArea(a) > 0)
						.collect(Collectors.toList());
			AnkhMorporkArea area = 
					UI.getAreaChoice(shadesAndAdjacentWithMinions, "Place a trouble marker in "
							+ "an area already containing at least one minion.",
							"Choose an area: ");
			game.addTroubleMarker(area.getAreaCode()); 
			System.out.println("Troublemarker added.");
		});
		
		// 8. DIMWELL
		cityCardFunctionMap.put(DIMWELL, (player, game) -> {
			payAndAddMinion(DIMWELL, player, game);
		});
		
		// 9. LONGWALL
		cityCardFunctionMap.put(LONGWALL, (player, game) -> {
			game.givePlayerMoneyFromBank(player, 1);
		});
		
		// 10. ISLE OF GODS
		cityCardFunctionMap.put(ISLE_OF_GODS, (player, game) -> {
			TextUserInterface UI = new TextUserInterface();
			if (player.hasMoney(2) && UI.getUserYesOrNoChoice("Pay $2 to remove a trouble marker?")) {
				game.giveBankMoneyFromPlayer(player, 2);
				Symbol.REMOVE_TROUBLE_MARKER.getGameAction().accept(player, game);
			}
		});
		
		// 11. SEVEN SLEEPERS
		cityCardFunctionMap.put(DRAGONS_LANDING, (player, game) -> {
			game.givePlayerMoneyFromBank(player, 3);
		});
		
		// 12. NAP HILL
		cityCardFunctionMap.put(LONGWALL, (player, game) -> {
			game.givePlayerMoneyFromBank(player, 1);
		});
	}

	private static final int[][] ADJACENCY_MATRIX = new int[][] {
			{ 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, // DOLLY_SISTERS
			{ 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1 }, // UNREAL_ESTATE
			{ 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0 }, // DRAGONS_LANDING
			{ 0, 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0 }, // SMALL_GODS
			{ 0, 0, 0, 1, 1, 1, 1, 1, 0, 1, 0, 0 }, // THE_SCOURS
			{ 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0 }, // THE_HIPPO
			{ 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0 }, // THE_SHADES
			{ 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0 }, // DIMWELL
			{ 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0 }, // LONGWALL
			{ 0, 0, 1, 1, 0, 1, 0, 0, 1, 1, 1, 0 }, // ISLE_OF_GODS
			{ 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1 }, // SEVEN_SLEEPERS
			{ 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1 }  // NAP_HILL
	};

	private final int areaCode;

	private final int buildingCost;
	
	private final String cityCardDescription;

	private AnkhMorporkArea(int code, int cost, String cityCardDesc) {
		areaCode = code;
		buildingCost = cost;
		cityCardDescription = cityCardDesc;
	}

	public int getAreaCode() {
		return areaCode;
	}

	public int getBuildingCost() {
		return buildingCost;
	}
	
	public String getCityCardDescription() {
		return cityCardDescription;
	}
	
	public static AnkhMorporkArea forCode(int areaCode) {
		return codeToAreaMap.get(areaCode);
	}
	
	public static BiConsumer<Player, Game> getAreaAction(AnkhMorporkArea a) {
		return cityCardFunctionMap.get(a);
	}

	public boolean isNeighboringWith(AnkhMorporkArea otherArea) {
		return ADJACENCY_MATRIX[getAreaCode() - 1][otherArea.getAreaCode() - 1] == 1;
	}
	
	public static boolean areAreasAdjacent(int firstAreaCode, int secondAreaCode) {
		return ADJACENCY_MATRIX[firstAreaCode - 1][secondAreaCode - 1] == 1;
	}
	
	public static List<AnkhMorporkArea> getAreaAndAdjacentAreas(AnkhMorporkArea a) {
		return codeToAreaMap.keySet().stream().filter(i -> (ADJACENCY_MATRIX[a.getAreaCode() - 1][i - 1] == 1))
				.map(i -> AnkhMorporkArea.forCode(i)).collect(Collectors.toList());
	}
	
	public static List<AnkhMorporkArea> getAdjacentAreas(AnkhMorporkArea a) {
		List<AnkhMorporkArea> areaAndAdjacent = getAreaAndAdjacentAreas(a);
		areaAndAdjacent.remove(a);
		return areaAndAdjacent;
	}
	
	public boolean isAdjacentToRiver() {
		return this != THE_HIPPO && this != SMALL_GODS && this != DRAGONS_LANDING;
	}
	
	private static void payAndAddMinion(AnkhMorporkArea a, Player player, Game game) {
		TextUserInterface UI = TextUserInterface.getUI();
		Map<Integer, BoardArea> gameBoard = game.getGameBoard();

		if (player.decreaseMoney(MINION_COST)) {

			int availableMinions = player.getMinionCount();
			List<AnkhMorporkArea> dollySistersAndNeighbors = 
					getAreaAndAdjacentAreas(a);

			if (availableMinions == Player.TOTAL_MINIONS) {
				AnkhMorporkArea chosenArea = UI.getAreaChoice(dollySistersAndNeighbors, 
						"All minions available. Select area to place minion",
						"Choose Area: ");
				gameBoard.get(chosenArea).addMinion(player);
			} else if (availableMinions == 0) {

				// Get the areas where the player has minions
				Map<Integer, BoardArea> subGameBoard = game.getAreasWithPlayerMinions(player);
				AnkhMorporkArea chosenArea = UI.getAreaChoice(subGameBoard.values().stream().map(BoardArea::getArea)
						.collect(Collectors.toList()), "All minions in play. Select an area to remove minion.",
									"Choose Area: ");
				subGameBoard.get(chosenArea.getAreaCode()).removeMinion(player);

				// Get the areas on which the player can put a minion
				chosenArea = UI.getAreaChoice(dollySistersAndNeighbors,
						"Select an area to place the removed minion.", 
						"Choose Area: ");
				subGameBoard.get(chosenArea.getAreaCode()).addMinion(player);
				
			} else {
				AnkhMorporkArea chosenArea = UI.getAreaChoice(dollySistersAndNeighbors, 
						"Unused minions available. Select an area to place a new minion.",
						"Choose Area: ");
				gameBoard.get(chosenArea.getAreaCode()).addMinion(player);	
			}
		} else {
			System.out.println(player.getName()
					+ " doesn't have enough money to place a minion!");
		}
	}

	@Override
	public String toString() {
		return name() + " [code = " + areaCode + ", buildingCost = " + buildingCost 
				+ ", cityCardAction = " + cityCardDescription + "]";
	}
}
