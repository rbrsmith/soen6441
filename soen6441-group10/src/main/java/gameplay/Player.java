package gameplay;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import util.Color;
import card.city.CityAreaCard;
import card.personality.PersonalityCard;
import card.player.GreenPlayerCard;

/**
 * This class represents the players participating in the game, including the
 * pieces they hold as well as their hands.
 * 
 * It is important to note that a player's color uniquely identifies the player.
 * 
 * @author Team 10 - SOEN6441
 * @version 1.0
 */
public class Player {

	public static final int TOTAL_MINIONS = 12;
	
	public static final int TOTAL_BUILDINGS = 6;
	
	public static final int PLAYER_MAX_HAND_SIZE = 5;

	private String name;

	private PersonalityCard personality;

	private Color color;

	private int money;

	private int minions;
	
	private int buildings;
	
	private List<CityAreaCard> cityCards = new ArrayList<>();
	
	/**
	 * This is the player's hand. It is implemented as a set because we can
	 * invoke the cards' actions statically (since they are contained in the 
	 * {@link GreenPlayerCard} enum).
	 */
	private Set<GreenPlayerCard> playerCards = new HashSet<>();

	public Player() {
		this.money = 0;
		this.minions = TOTAL_MINIONS;
		this.buildings = TOTAL_BUILDINGS;
	}

	/**
	 * Sets player's name for the current object.
	 * 
	 * @param name
	 *            the name of player
	 * @return true if the name is valid
	 */
	public boolean setName(String name) {
		// Checks for a valid name. Valid name contains only letter.
		if (isAlpha(name)) {
			this.name = name;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks criteria to see if the entry is a valid name.
	 * 
	 * @param name
	 *            the name entered by player
	 * @return true if the entry is a valid name
	 */
	private boolean isAlpha(String name) {
		return name.matches("^[a-zA-Z0-9_]*$");
	}

	/**
	 * Get player's name.
	 * 
	 * @return the player's name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * This method assigns the personality card to the player.
	 * 
	 * @param personality
	 *            the personality card
	 */
	public void setPersonality(PersonalityCard personality) {
		this.personality = personality;
	}

	/**
	 * Get the personality card of the player.
	 * 
	 * @return the assigned personality card
	 */
	public PersonalityCard getPersonality() {
		return this.personality;
	}

	/**
	 * This method prints the player's turn
	 */
	public void printTurn() {
		System.out.println(this.getName() + " turn");
	}

	/**
	 * This method increases the player's money
	 * 
	 * @param amount
	 *            the amount of money
	 * @return true if it increases the player's money successfully
	 */
	public boolean increaseMoney(int amount) {
		this.money = this.money + amount;
		return true;
	}

	/**
	 * This method decreases the player's money.
	 * 
	 * @param amount
	 *            the amount of money
	 * @return true if it decreases the player's money successfully
	 */
	public boolean decreaseMoney(int amount) {
		if (this.money - amount < 0) {
			return false;
		} else {
			this.money = this.money - amount;
			return true;
		}
	}

	/**
	 * Get the total amount of money the player currently has.
	 * 
	 * @return the amount
	 */
	public int getMoney() {
		return money;
	}

	/**
	 * Get the total number of minions the player currently has.
	 * 
	 * @return the minions
	 */
	public int getMinionCount() {
		return this.minions;
	}

	// Decrease the number of minions by one
	/**
	 * This method decrements the player's minions.
	 * 
	 * @return true if it decrements successfully (there is any minion to be
	 *         decremented).
	 */
	public boolean decreaseMinion() {
		if ((this.minions - 1) >= 0) {
			this.minions = this.minions - 1;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method increments the player's minions.
	 * 
	 * @return true if it increments successfully (the number of minions
	 *         wouldn't pass the upper bound).
	 */
	public boolean increaseMinion() {
		if (this.minions < 12) {
			this.minions = this.minions + 1;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Get the total number of buildings the player currently has
	 * 
	 * @return the total number of buildings the player currently has
	 */
	public int getBuildings() {
		return this.buildings;
	}

	/**
	 * This method decrements the player's building.
	 * 
	 * @return true if it decrements successfully (there is any building to be
	 *         decremented).
	 */
	public boolean decreaseBuilding() {
		if ((this.buildings - 1) >= 0) {
			this.buildings = this.buildings - 1;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method increments the player's building.
	 * 
	 * @return true if it increments successfully (there is any building to be
	 *         incremented).
	 */
	public boolean increaseBuildings() {
		if ((this.buildings + 1) < 6) {
			this.buildings = this.buildings + 1;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method adds player's cards one by one.
	 * 
	 * @param card
	 *            player card
	 * @return true when adds a player card
	 */
	public boolean addPlayerCard(GreenPlayerCard card) {
		this.playerCards.add(card);
		return true;
	}

	/**
	 * Removes the given player card from the player's hand.
	 * 
	 * @return true if the player card was removed successfully, false otherwise.
	 */
	public boolean removePlayerCard(GreenPlayerCard card) {
		return playerCards.remove(card);
	}

	/**
	 * Get the set of the player's cards
	 * 
	 * @return the player's cards
	 */
	public Set<GreenPlayerCard> getPlayerCards() {
		return playerCards;
	}
	
	/**
	 * Returns the number of cards in the player's hand.
	 * @return the number of cards in the player's hand.
	 */
	public int getHandSize() {
		return playerCards.size();
	}

	/**
	 * Set Color for Player's pieces
	 * 
	 * @param color_
	 *            the color
	 */
	public void setColor(Color color_) {
		color = color_;
	}

	/**
	 * Get Player's color
	 * 
	 * @return the Player's color
	 */
	public Color getColor() {
		return this.color;
	}

	/**
	 * Net worth = cash + monetary cost of each owned building - 12 * loans
	 * taken
	 * 
	 * @return the player's net worth as described by the above logic.
	 */
	public int getTotalWorth() {
		// TODO Implement this method
		return 0;
	}
	
	/**
	 * Retrieves the amount owed by the player due to having "loan" cards
	 * ({@link GreenPlayerCard#MR_BENT}, {@link GreenPlayerCard#THE_BANK_OF_ANKH_MORPORK}).
	 * @return
	 */
	public int getLoanBalance() {
		int balance = 0;
		if (playerCards.contains(GreenPlayerCard.MR_BENT)) {
			balance -= 12;
		}
		if (playerCards.contains(GreenPlayerCard.THE_BANK_OF_ANKH_MORPORK)) {
			balance -= 12;
		}
		return balance;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Player)) {
			return false;
		}
		Player other = (Player) obj;
		if (color != other.color) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Player[ name=" + name + ", color=" + color + " ]";
	}

}
