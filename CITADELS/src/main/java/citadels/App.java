package citadels;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import java.io.*;
import java.util.*;

public class App {
	
	//private File cardsFile;
    private List<Player> players = new ArrayList<>();
    private List<DistrictCard> deck = new ArrayList<>();
    private List<Character> characters = new ArrayList<>();
    private Player crownedPlayer;

	/*public App() {
		try {
            cardsFile = new File(URLDecoder.decode(this.getClass().getResource("cards.tsv").getPath(), StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
	}*/

    private void initialSetup() {
        Scanner scanner = new Scanner(System.in);
        int numPlayers = 0;

        //Loading full deck of cards
        for (FullDeck type : FullDeck.values()) {
            for(int i = 0; i < type.getQuantity(); i++) {
                deck.add(new DistrictCard(type.getName(), type.getColor(), type.getCost(), type.getAbility()));
            }
        }


        do {
            System.out.print("Enter how many players [4-7]: ");
            if (scanner.hasNextInt()) {
                numPlayers = scanner.nextInt();
            } else {
                return;
            }
        } while (numPlayers < 4 || numPlayers > 7);

        //Shuffle
        Collections.shuffle(deck);
        System.out.println("Shuffling deck...");

        //Adding characters
        for (int i = 1; i <= numPlayers; i++) {
            players.add(new Player(i));
        }
        System.out.println("Adding characters...");
        System.out.println("Dealing cards...");
        System.out.println("Starting Citadels with " + players.size() + " players...");
        System.out.println("You are player 1");
        

        //who is crowned
        Random ran = new Random();
        int index = ran.nextInt(numPlayers);
        crownedPlayer = players.get(index);
        crownedPlayer.setIsCrown(true);

        //golds and cards
        for (Player p : players) {
            p.setGold(2);
            for(int i = 0; i < 4; i++) {
                p.setHand(deck.remove(0));
            }
        }

        //Optional from gpt to debug
        for (Player p : players) {
            System.out.println(p);
        }

        System.out.println("Player " + crownedPlayer.getPlayerNo() + " is the crowned player and goes first.");
        System.out.println("Press t to process turns");
    }


    private void characterSelection() {
        System.out.println("==========================================================");
        System.out.println("SELECTION PHASE");
        System.out.println("==========================================================");

        //Initialise amd shuffle deck of characters
        characters.clear();
        characters.add(new Character("Assassin", "Select another character whom you wish to kill. The killed character loses their turn.\n", 1));
        characters.add(new Character("Thief", "Select another character whom you wish to rob. When a player reveals that character to take his turn, you immediately take all of his gold. You cannot rob the Assassin or the killed character.\n", 2));
        characters.add(new Character("Magician", "Can either exchange their hand with another player’s, or discard any number of district cards face down to the bottom of the deck and draw an equal number of cards from the district deck (can only do this once per turn).\n", 3));
        characters.add(new Character("King", "Gains one gold for each yellow (noble) district in their city. They receive the crown token and will be the first to choose characters on the next round.\n", 4));
        characters.add(new Character("Bishop", "Gains one gold for each blue (religious) district in their city. Their buildings cannot be destroyed by the Warlord, unless they are killed by the Assassin.\n",5));
        characters.add(new Character("Merchant", "Gains one gold for each green (trade) district in their city. Gains one extra gold.\n",6));
        characters.add(new Character("Architect", "Gains two extra district cards. Can build up to 3 districts per turn.\n",7));
        characters.add(new Character("Warlord", "Gains one gold for each red (military) district in their city. You can destroy one district of your choice by paying one fewer gold than its building cost. You cannot destroy a district in a city with 8 or more districts.\n",8));
        Collections.shuffle(characters);
        

        Random ran = new Random();
        int indexRandom = ran.nextInt(characters.size());


        //Discard cards
        //1 facedown card
        Character downDiscard = characters.remove(indexRandom);
        System.out.println("A mystery character was removed.");

        //faceup cards
        if (players.size() == 4) {
            //1st faceup card
            Character upDiscardFirst;
            do {
                indexRandom = ran.nextInt(characters.size());
                upDiscardFirst = characters.remove(indexRandom);
                if (upDiscardFirst.getName().equals("King")) {
                    //System.out.println("A mystery character was removed.");
                    System.out.println("King was removed.");
                    System.out.println("The King cannot be visibly removed, trying again...");
                    characters.add(upDiscardFirst);
                }
            } while (upDiscardFirst.getName().equals("King"));
            //System.out.println("A mystery character was removed.");
            System.out.println(upDiscardFirst.getName() + " was removed.");

            //2nd faceup card
            Character upDiscardSecond;
            do {
                indexRandom = ran.nextInt(characters.size());
                upDiscardSecond = characters.remove(indexRandom);
                if (upDiscardSecond.getName().equals("King")) {
                    System.out.println("King was removed.");
                    System.out.println("The King cannot be visibly removed, trying again...");
                    characters.add(upDiscardSecond);
                }
            } while (upDiscardSecond.getName().equals("King"));
            System.out.println(upDiscardSecond.getName() + " was removed.");

        } else if (players.size() == 5) {
            //1 faceup card only
            Character upDiscard;
            do {
                indexRandom = ran.nextInt(characters.size());
                upDiscard = characters.remove(indexRandom);
                if (upDiscard.getName().equals("King")) {
                    //System.out.println("A mystery character was removed.");
                    System.out.println("King was removed.");
                    System.out.println("The King cannot be visibly removed, trying again...");
                    characters.add(upDiscard);
                }
            } while (upDiscard.getName().equals("King"));
            System.out.println(upDiscard.getName() + " was removed.");
        }

        //Character selection
        for (int i = 0; i < players.size(); i++) {
            int playerIndex = (crownedPlayer.getPlayerNo() - 1 + i) % players.size();
            Player currentPlayer = players.get(playerIndex);

            if (currentPlayer.getPlayerNo() != 1) {
                indexRandom = ran.nextInt(characters.size());
                currentPlayer.setCharacter(characters.remove(indexRandom));
            } else {
                System.out.println("Choose your character. Available characters:");
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < characters.size(); j++) {
                    String characterAvail = characters.get(j).toString();
                    sb.append(characterAvail);
                    if (j < characters.size() - 1) {
                        sb.append(", ");
                    }
                }
                String allCharacterAvail = sb.toString();
                System.out.println(allCharacterAvail);

                //choosing available character
                Scanner scanner = new Scanner(System.in);
                int indexChosenChar = -1;

                while (indexChosenChar == -1) {
                    System.out.print("> ");
                    String chosenChar = scanner.nextLine().trim();

                    for (int j = 0; j < characters.size(); j++) {
                        if (chosenChar.equalsIgnoreCase(characters.get(j).getName())) {
                            indexChosenChar = j;
                            break;
                        }
                    }

                    if (indexChosenChar == -1) {
                         System.out.println("Invalid character. Please choose from the available characters.");
                    }
                }

                // Set and remove the chosen character
                currentPlayer.setCharacter(characters.remove(indexChosenChar));
            }
            System.out.println("Player " + currentPlayer.getPlayerNo() + " chose a character.");

            if (i < players.size() - 1) {  // Don't wait after the last player
                Scanner scanner = new Scanner(System.in);
                String input;
                do {
                    System.out.print("> ");
                    input = scanner.nextLine().trim().toLowerCase();
                    if (!input.equals("t")) {
                        System.out.println("It is not your turn. Press t to continue with other player turns.");
                    }
                } while (!input.equals("t"));
            }

        }
        System.out.println("Character choosing is over, action round will now begin.");
    }



    private void turnPhase() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("==========================================================");
        System.out.println("TURN PHASE");
        System.out.println("==========================================================");

        for (int characterNum = 1; characterNum <= 8; characterNum++) {
            String[] names = {
                "", "Assassin", "Thief", "Magician", "King", "Bishop", "Merchant", "Architect", "Warlord"
            };

            System.out.println(characterNum + ": " + names[characterNum]);

            Player currentPlayer = getPlayerByCharacterOrder(characterNum);
            if (currentPlayer == null) {
                System.out.println("No one is the " + names[characterNum]);
                continue;
            }

            System.out.println("Player " + currentPlayer.getPlayerNo() + " is the " + names[characterNum]);
            // Check if killed
            if (currentPlayer.isKilled()) {
                System.out.println("Player " + currentPlayer.getPlayerNo() + " loses their turn because they were assassinated.");
                continue;
            }

            
            //Abilities
            String role = currentPlayer.getCharacter().getName();
            if (role.equals("Assassin")) {
                Abilities.assassin(currentPlayer, players);
            } else if (role.equalsIgnoreCase("Thief")) {
                Abilities.thief(currentPlayer, players);
            } else if (role.equalsIgnoreCase("Magician")) {
                Abilities.magician(currentPlayer, players, deck);
            } else if (role.equalsIgnoreCase("King")) {
                Abilities.king(currentPlayer, players);
            } else if (role.equalsIgnoreCase("Bishop")) {
                Abilities.bishop(currentPlayer, players);
            } else if (role.equalsIgnoreCase("Merchant")) {
                Abilities.merchant(currentPlayer, players);
            } else if (role.equalsIgnoreCase("Architect")) {
                Abilities.architect(currentPlayer, players, deck);
            } else if (role.equalsIgnoreCase("Warlord")) {
                Abilities.warlord(currentPlayer, players, deck);
            }

            //Choose gold or cards
            if (!currentPlayer.getCharacter().getName().equalsIgnoreCase("architect")) {
                if (currentPlayer.getPlayerNo() == 1) {
                    System.out.println("Collect 2 gold or draw two cards and pick one [gold/cards].");
                    String choice = scanner.nextLine().trim().toLowerCase();
                    if (choice.equals("gold")) {
                        currentPlayer.setGold(currentPlayer.getGold() + 2);
                        System.out.println("Player 1 received 2 gold.");
                    } else if (choice.equals("cards")) {
                        if (deck.size() < 2) {
                            System.out.println("Not enough cards in the deck.");
                        } else {
                            DistrictCard card1 = deck.remove(0);
                            DistrictCard card2 = deck.remove(0);
                            System.out.println("Player 1 chose cards.");
                            System.out.println("Pick one of the following cards: 'collect card <option>'.");
                            System.out.println("1. " + card1);
                            System.out.println("2. " + card2);
                            System.out.print("collect card ");
                            int chosen = Integer.parseInt(scanner.nextLine().trim());
                            DistrictCard chosenCard = null;
                            do{
                                if(chosen == 1) {
                                    chosenCard = card1;
                                    if(chosenCard.getCost() > currentPlayer.getGold()) {
                                        
                                    }
                                } else if(chosen == 2) {
                                    chosenCard = card2;
                                }
                            }while(chosenCard.getCost() > currentPlayer.getGold() || chosenCard == null);

                            if (chosen == 1) {
                                System.out.println("You chose card " + card1.getName() + "[" + card1.getColor() + card1.getCost() + "]");
                                currentPlayer.setGold(currentPlayer.getGold() - card1.getCost());
                                currentPlayer.setHand(card1);
                                deck.add(card2);
                                System.out.println("Player " + currentPlayer.getPlayerNo() + " built a " + card1.getName() + "[" + card1.getColor() + card1.getCost() + "]" + "in their city.");
                            } else if (chosen == 2) {
                                System.out.println("You chose card " + card2.getName() + "[" + card2.getColor() + card2.getCost() + "]");
                                currentPlayer.setGold(currentPlayer.getGold() - card2.getCost());
                                currentPlayer.setHand(card2);
                                deck.add(card1);
                                System.out.println("Player " + currentPlayer.getPlayerNo() + " built a " + card2.getName() + "[" + card2.getColor() + card2.getCost() + "]" + "in their city.");
                            } 
                        }
                    }
                } else {
                    Random ran = new Random();
                    if (ran.nextBoolean() || deck.size() < 2) {
                        currentPlayer.setGold(currentPlayer.getGold() + 2);
                    } else {
                        DistrictCard card1 = deck.remove(0);
                        DistrictCard card2 = deck.remove(0);
                        int chosenCardNum = ran.nextInt(2) + 1;
                        if (chosenCardNum == 1 && currentPlayer.getGold() >= card1.getCost()) {
                            currentPlayer.setHand(card1);
                            currentPlayer.setGold(currentPlayer.getGold() - card1.getCost());
                            System.out.println("Player " + currentPlayer.getPlayerNo() + " built a " + card1.getName() + "[" + card1.getColor() + card1.getCost() + "]" + "in their city.");
                            deck.add(card2);
                        } else if (chosenCardNum == 2 && currentPlayer.getGold() >= card2.getCost()) {
                            currentPlayer.setHand(card2);
                            currentPlayer.setGold(currentPlayer.getGold() - card2.getCost());
                            System.out.println("Player " + currentPlayer.getPlayerNo() + " built a " + card2.getName() + "[" + card2.getColor() + card2.getCost() + "]" + "in their city.");
                            deck.add(card1);
                        } else {
                            currentPlayer.setGold(currentPlayer.getGold() + 2);
                        }
                    }
                }
            }
        }

    }


    //Helper methods
    /*private Character charInOrder(int number) {
        for (Character c : characters) {
            if (c.getOrder() == number) {
                return c;
            }
        }
        return null;
    }
    private Player playerInChar(Character character) {
        if (character == null) {
            System.out.println("null here");
            return null;
        }
        for (Player p : players) {
            Character charFinding = p.getCharacter();
            if (charFinding != null && charFinding.getName().equals(character.getName())) {
                return p;
            }
        }
        return null;
    }*/

    private Player getPlayerByCharacterOrder(int order) {
        for (Player p : players) {
            Character c = p.getCharacter();
            if (c != null && c.getOrder() == order) {
                return p;
            }
        }
        return null;
    }


    public static void main(String[] args) {
        App app = new App();
        app.initialSetup();
        System.out.print("> ");
        String command = null;
        Scanner scanner = new Scanner(System.in);
        do {
            command = scanner.nextLine().trim();
        } while(!command.equals("t"));
        app.characterSelection();
        app.turnPhase();
    }

}
