/**
 * Class represents the bulk of the games actions
 * 
 * Class sets up the game, and gets all needed components for the controller.
 * 
 */

package gameplay;

import java.util.Optional;

import util.Color;
import card.CityCard;
import card.CityDeck;
import card.PersonalityCard;
import card.PersonalityDeck;
import card.PlayerDeck;
import card.RandomEventDeck;
import error.InvalidGameStateException;

public class Game {

	// Game Components.
	private Bank gameBank;
	private Player[] players;
	private PersonalityDeck personality;
	private PlayerDeck player;
	private CityDeck cities;
	private RandomEventDeck events;
	// Identifies general game status, if it is initiated or not
	private int status;
	// Identifies whose turn it is, uses index in the player array.
	private int currentTurn;

	/**
	 * Set status to 0. No game has started.
	 */
	public Game() {
		// Set game status as uninitiated
		this.status = 0;
	}

	/**
	 * Set up game. Game has not started, but the deck are set up.
	 * 
	 * @param: numberOfPlayers how many people will be playing game
	 * @param: playerNames array of what everybodys name is
	 * @Exception: if invalid number of players
	 */
	public void setUp(int numberOfPlayers, String[] playerNames)
			throws InvalidGameStateException {
		// Create players.
		if (playerNames.length != numberOfPlayers)
			throw new InvalidGameStateException(
					"Number of players and player names not matching");

		// Make sure players are between 2 and 4.
		if (numberOfPlayers > 4 || numberOfPlayers < 2) {
			throw new InvalidGameStateException("Incorrect player number");
		} else {
			this.gameBank = new Bank();
			this.players = new Player[numberOfPlayers];

			for (int i = 0; i < this.players.length; ++i) {
				Player p = new Player();
				p.setName(playerNames[i]);
				p.setColor(Color.forCode(i));
				this.players[i] = p;
			}
		}

		// Initialize deck of cards.
		this.personality = new PersonalityDeck();
		this.player = new PlayerDeck();
		this.cities = new CityDeck();
		this.events = new RandomEventDeck();

		// Set game status as ready to start.
		this.status = 1;
	}

	/**
	 * Start a new game.
	 */
	public void init() {
		// Give each player their money, personality and initial minions.
		
		// Cards that have an initial state.
		CityCard Shades = (this.cities.getCard("The Shades"));
		CityCard theScoures = (this.cities.getCard("The Scoures"));
		CityCard dollySisters = (this.cities.getCard("Dolly Sisters"));
		
		for(int i=0; i<this.players.length; ++i) {
			// Deal out 10 dollars.
			this.players[i].increaseMoney(10);
			this.gameBank.decreaseBalance(10);

			Optional<PersonalityCard> popped = this.personality.drawCard();
			// TODO We have to have a check for an empty deck somewhere here
			this.players[i].setPersonality(popped.get());
			
			// Add minions to shades, sources and dolly sister regions.
			Shades.addMinion(this.players[i]);
			theScoures.addMinion(this.players[i]);
			dollySisters.addMinion(this.players[i]);
			
		}
		
		// Starter regions also have trouble.
		Shades.addTrouble();
		theScoures.addTrouble();
		dollySisters.addTrouble();
		
		// Set turn to first in game.
		this.currentTurn = 0;
		
		// Set game status as playing.
		this.status = 2;
	}

	/**
	 * Move the game forward by one turn.
	 */
	public void turn() {
		int current = this.currentTurn;
		this.players[current].turn();
		if ((current + 1) == this.players.length) {
			this.currentTurn = 0;
		} else {
			this.currentTurn = current + 1;
		}
	}

	/**
	 * @return: Player[] Array of players in game
	 */
	public Player[] getPlayers() {
		return this.players;
	}

	/**
	 * 
	 * @return Player class of next turn
	 */
	public Player getCurrentTurn() {
		return this.players[this.currentTurn];
	}

	/**
	 * @return: Deck of all personalities
	 */
	public PersonalityDeck getPersonalityDeck() {
		return this.personality;
	}

	/**
	 * @return Bank class used in the game.
	 */
	public Bank getBank() {
		return this.gameBank;
	}

	/**
	 * 
	 * @return current game status
	 */
	public int getState() {
		return this.status;
	}

	/**
	 * 
	 * @return deck of city cards
	 */
	public CityDeck getCities() {
		return this.cities;
	}
	
	/**
	 * Gets the Player of the given color.
	 * @param c - the expected Player color
	 * @return
	 */
	public Player getPlayerOfColor(Color c) {
		// TODO Change the players from an array to a list and then change this
		//		to an HOO call
		for (Player p : players) {
			if (p.getColor() == c) {
				return p;
			}
		}
		
		return null;
	}

	/**
	 * Each players turn has not been implemented so this actions A series of
	 * potential game maneuvers.
	 */
	void simulate() {
		 this.cities.getCard("Small Gods").addTrouble();
		 this.cities.getCard("Nap Hill").incTrolls();
		 
		 this.cities.getCard("The Hippo").addMinion(this.players[1]);
		 this.cities.getCard("The Hippo").addMinion(this.players[1]);
		 
		 this.cities.getCard("Nap Hill").setBuilding(this.players[1]);
		 
		 this.players[0].addPlayerCard(this.player.drawCard().get());
		 this.players[0].addPlayerCard(this.player.drawCard().get());
		 
		 this.players[1].addPlayerCard(this.player.drawCard().get());
	}
}