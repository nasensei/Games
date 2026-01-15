package citadels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator; 
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class Abilities {

    //Assassin
    public static void assassin(Player player, List<Player> players) {
        Player targetPlayer = null;

        if (player.getPlayerNo() == 1) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Who do you want to kill? Choose a character from 2–8: ");
            System.out.print("> ");
            int chosenCharNo = -1;

            while (true) {
                System.out.print("> ");
                try {
                    chosenCharNo = scanner.nextInt();
                    if (chosenCharNo < 2 || chosenCharNo > 8) {
                        System.out.println("Invalid. Choose from 2–8.");
                        continue;
                    }

                    //Avoid killed character
                    boolean isKilled = false;
                    for (Player p : players) {
                        if (p.getCharacter() != null && p.getCharacter().getOrder() == chosenCharNo && p.isKilled()) {
                            isKilled = true;
                            break;
                        }
                    }
                    if (isKilled) {
                        System.out.println("You cannot steal from a character that was killed. Try another.");
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Enter a number between 2–8.");
                }
            }

            String targetCharacter = orderToChar(chosenCharNo);
            for (Player p : players) {
                if (p != null && targetCharacter.equals(p.getCharacter().getName())) {
                    targetPlayer = p;
                    targetPlayer.setKilled(true);
                    System.out.println("You chose to kill the " + targetPlayer.getCharacter().getName());
                    return;
                }
            }

            //System.out.println("No one had that character.");
        } else {
            // Bot chooses random target other than itself
            int currentPlayerNo = player.getPlayerNo();
            int targetNo = -1;
            Random ran = new Random();
            do {
                targetNo = ran.nextInt(7) + 2;
                if (targetNo != currentPlayerNo) {
                    for (Player p : players) {
                        if (p.getPlayerNo() == targetNo) {
                            targetPlayer = p;
                            targetPlayer.setKilled(true);
                            //System.out.println("Bot killed " + p.getCharacter().getName());
                            break;
                        }
                    }
                }
            } while (targetNo == currentPlayerNo || isCharacterKilled(targetNo, players));
        }
    }

    //Thief
    public static void thief(Player player, List<Player> players) {
        Player targetPlayer = null;
        if (player.getPlayerNo() == 1) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Who do you want to steal from? Choose a character from 3-8:");
            int chosenCharNo = -1;

            while (true) {
                System.out.print("> ");
                try {
                    chosenCharNo = scanner.nextInt();
                    if (chosenCharNo < 3 || chosenCharNo >8) {
                        System.out.println("Invalid. Choose from 3-8 (excluding Assassin and killed character).");
                        continue;
                    }

                    //Avoid killed character
                    boolean isKilled = false;
                    for (Player p : players) {
                        if (p.getCharacter() != null && p.getCharacter().getOrder() == chosenCharNo && p.isKilled()) {
                            isKilled = true;
                            break;
                        }
                    }

                    if (isKilled) {
                        System.out.println("You cannot steal from a character that was killed. Try another.");
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Enter a number between 3-8.");
                }
            }

            String targetCharacter = orderToChar(chosenCharNo);

            for (Player p : players) {
                if (p != null && targetCharacter.equals(p.getCharacter().getName())) {
                    player.setGold(player.getGold() + p.getGold());
                    p.setGold(0);
                    break;
                }
            }
            System.out.println("You chose to steal from the " + targetCharacter);

        } else {
            Random ran = new Random();
            int currentPlayerNo = player.getPlayerNo();
            int targetNo = -1;
            do {
                targetNo = ran.nextInt(6) + 3;
                if (targetNo != currentPlayerNo) {
                    for (Player p :players) {
                        if (p.getPlayerNo() == targetNo) {
                            targetPlayer = p;
                            player.setGold(targetPlayer.getGold() + player.getGold());
                            targetPlayer.setGold(0);
                            break;
                        }
                    }
                }
            } while (isCharacterKilled(targetNo, players) || targetNo == currentPlayerNo);
        }
        
    }

    //Magician
    public static void magician(Player player, List<Player> players, List<DistrictCard> deck) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        // human play
        if (player.getPlayerNo() == 1) {
            System.out.println("You are the Magician. Choose an action:");
            System.out.println("1. Exchange hand with another player");
            System.out.println("2. Discard any number of cards and draw the same number");

            int choice;
            while (true) {
                System.out.print("> ");
                choice = scanner.nextInt();
                scanner.nextLine(); // clear leftover newline
                if (choice == 1 || choice == 2) break;
                System.out.println("Invalid. Enter 1 or 2.");
            }

            if (choice == 1) {
                // Option 1: Exchange hand
                System.out.println("Choose player number to swap hands with:");
                for (Player p : players) {
                    if (p.getPlayerNo() != 1) {
                        System.out.println("Player " + p.getPlayerNo());
                    }
                }

                int targetNo;
                while (true) {
                    System.out.print("> ");
                    targetNo = scanner.nextInt();
                    scanner.nextLine(); // consume newline
                    if (targetNo >= 1 && targetNo <= players.size() && targetNo != 1) break;
                    System.out.println("Invalid player number.");
                }

                Player target = players.get(targetNo - 1);
                List<DistrictCard> temp = new ArrayList<>(player.getDistricts());

                for (int i = 0; i < target.getDistricts().size(); i++) {
                    player.setHand(target.getDistricts().get(i));
                }
                for (int i = 0; i < temp.size(); i++) {
                    target.setHand(temp.get(i));
                }
                
                System.out.println("Swapped hands with Player " + targetNo);
            } else {
                // Option 2: Discard and draw
                List<DistrictCard> hand = player.getDistricts();
                if (hand.isEmpty()) {
                    System.out.println("You have no cards to discard.");
                    return;
                }

                System.out.println("Your hand:");
                for (int i = 0; i < hand.size(); i++) {
                    System.out.println(i + ": " + hand.get(i));
                }

                System.out.println("Enter indices to discard (comma-separated, e.g. 0,2):");
                System.out.print("> ");
                String input = scanner.nextLine().trim();

                if (input.isEmpty()) return;

                String[] parts = input.split(",");
                List<Integer> indices = new ArrayList<>();
                for (String part : parts) {
                    try {
                        int idx = Integer.parseInt(part.trim());
                        if (idx >= 0 && idx < hand.size()) {
                            indices.add(idx);
                        }
                    } catch (NumberFormatException e) {
                    // skip
                    }
                }

                Collections.sort(indices, Collections.reverseOrder());
                int discardCount = 0;
                for (int idx : indices) {
                    hand.remove(idx);
                    discardCount++;
                }

                for (int i = 0; i < discardCount && !deck.isEmpty(); i++) {
                    hand.add(deck.remove(0));
                }

                System.out.println("You discarded " + discardCount + " and drew the same.");
            }

        // bot play
        } else {
            boolean willSwap = random.nextBoolean();

            if (willSwap) {
                List<Player> candidates = new ArrayList<>();
                for (Player p : players) {
                    if (p.getPlayerNo() != player.getPlayerNo()) {
                        candidates.add(p);
                    }
                }

                if (!candidates.isEmpty()) {
                    Player target = candidates.get(random.nextInt(candidates.size()));
                    List<DistrictCard> temp = new ArrayList<>(player.getDistricts());

                    for (int i = 0; i < target.getDistricts().size(); i++) {
                        player.setHand(target.getDistricts().get(i));
                    }
                    for (int i = 0; i < temp.size(); i++) {
                        target.setHand(temp.get(i));
                    }
                    

                    System.out.println("Player " + player.getPlayerNo() +
                        " swapped hands with Player " + target.getPlayerNo());
                }
            } else {
                // Random discard 0 to hand size
                List<DistrictCard> hand = player.getDistricts();
                int handSize = hand.size();
                int numToDiscard = handSize > 0 ? random.nextInt(handSize + 1) : 0;

                for (int i = 0; i < numToDiscard && !hand.isEmpty(); i++) {
                    int randIdx = random.nextInt(hand.size());
                    hand.remove(randIdx);
                }

                for (int i = 0; i < numToDiscard && !deck.isEmpty(); i++) {
                    hand.add(deck.remove(0));
                }

                System.out.println("Player " + player.getPlayerNo() + " discarded " + numToDiscard + " card(s) and drew the same.");
            }
        }
    }

    //King
    public static void king(Player player, List<Player> players) {
        int bonusGold = 0;

        for (DistrictCard card : player.getDistricts()) {
            if (card.getColor().equalsIgnoreCase("yellow")) {
                bonusGold++;
            }
        }

        player.setGold(player.getGold() + bonusGold);
        System.out.println("Player " + player.getPlayerNo() + " gained " + bonusGold + " gold from yellow districts.");
        
        for (Player p : players) {
            p.setIsCrown(false);
        }
        player.setIsCrown(true);
        System.out.println("Player " + player.getPlayerNo() + " is now the crowned player.");
    }

    //Bishop
    public static void bishop(Player player, List<Player> players) {
        int bonusGold = 0;

        for (DistrictCard card : player.getDistricts()) {
            if (card.getColor().equalsIgnoreCase("blue")) {
                bonusGold++;
            }
        }

        player.setGold(player.getGold() + bonusGold);
        System.out.println("Player " + player.getPlayerNo() + " gained " + bonusGold + " gold from blue (religious) districts.");
    }

    public static void merchant(Player player, List<Player> players) {
        int bonusGold = 0;

        for (DistrictCard card : player.getDistricts()) {
            if (card.getColor().equalsIgnoreCase("green")) {
                bonusGold++;
            }
        }
        bonusGold ++; // extra bonus gold
        player.setGold(player.getGold() + bonusGold);
        System.out.println("Player " + player.getPlayerNo() + " collected 1 gold from merchant action");
    }

    //Architect
    public static void architect(Player player, List<Player> players, List<DistrictCard> deck) {
        Random random = new Random();
        Scanner scanner = new Scanner(System.in);

        // human play
        if (player.getPlayerNo() == 1) {
            System.out.println("Choose an action:");
            System.out.println("1. Collect 2 gold and draw 2 district cards");
            System.out.println("2. Draw 4 district cards.");
            int choice;
            while (true) {
                System.out.println("> ");
                try {
                    choice = Integer.parseInt(scanner.nextLine().trim());
                    if (choice == 1 || choice == 2) break;
                    System.out.println("Please enter 1 or 2.");
                } catch (NumberFormatException e) {
                    System.out.println("Enter a valid number.");
                }
            }

            //Collect 2 gola and draw 2 district cards
            if (choice == 1) {
                player.setGold(player.getGold() + 2);
                System.out.println("Player 1 received 2 gold.");
                DistrictCard card1 = deck.remove(0);
                DistrictCard card2 = deck.remove(0);
                System.out.println("You drew 2 cards.");
                System.out.println("1. " + card1);
                System.out.println("2. " + card2);
                System.out.println("How many buildings you want to buid? ");
                System.out.println("> ");
                int numChoice;
                while (true) {
                    try {
                        numChoice = Integer.parseInt(scanner.nextLine().trim());
                        if (numChoice == 0 || numChoice == 1 || numChoice == 2) {break;}
                        System.out.println("Please enter 0, 1 or 2.");
                    } catch (NumberFormatException e) {
                        System.out.println("Enter a valid number.");
                    }
                }

                if (numChoice == 0) {
                    System.out.println("You chose not to build any buildings.");
                    deck.add(card1);
                    deck.add(card2);
                    return;
                } else if (numChoice == 1) {
                    System.out.println("Select which card: (1/2)");
                    System.out.print("> ");
                    int cardChoice;
                    while (true) {
                        try {
                            cardChoice = Integer.parseInt(scanner.nextLine().trim());
                            if (cardChoice == 1 || cardChoice == 2) {break;}
                            System.out.println("Please enter 1 or 2.");
                        } catch (NumberFormatException e) {
                            System.out.println("Enter a valid number.");
                        }
                    }
                    if (cardChoice == 1) {
                        if (card1.getCost() > player.getGold()) {
                            System.out.println("You don't have enough gold.");
                            return;
                        }
                        player.setHand(card1);
                        player.setGold(player.getGold() - card1.getCost());
                        deck.add(card2);
                        System.out.println("Player " + player.getPlayerNo() + " built a " + card1.getName() + "[" + card1.getColor() + card1.getCost() + "]" + "in their city.");
                        return;
                    } else if (cardChoice == 2) {
                        if (card2.getCost() > player.getGold()) {
                            System.out.println("You don't have enough gold.");
                            return;
                        }
                        player.setHand(card2);
                        player.setGold(player.getGold() - card2.getCost());
                        deck.add(card1);
                        System.out.println("Player " + player.getPlayerNo() + " built a " + card2.getName() + "[" + card2.getColor() + card2.getCost() + "]" + "in their city.");
                        return;
                    } 
                } else if (numChoice == 2) {
                    int totalCost = card1.getCost() + card2.getCost();
                    if (totalCost > player.getGold()) {
                        System.out.println("You don't have enough gold.");
                        return;
                    }
                    player.setHand(card1);
                    player.setHand(card2);
                    player.setGold(player.getGold() - totalCost);
                    System.out.println("Player " + player.getPlayerNo() + " built a " + card1.getName() + "[" + card1.getColor() + card1.getCost() + "]" + "in their city.");
                    System.out.println("Player " + player.getPlayerNo() + " built a " + card2.getName() + "[" + card2.getColor() + card2.getCost() + "]" + "in their city.");
                    return;
                }
            //Draw 4 cards and build max 3
            } else {
                List<DistrictCard> tempHand = new ArrayList<>();
                DistrictCard card1 = deck.remove(0);
                tempHand.add(card1);
                DistrictCard card2 = deck.remove(0);
                tempHand.add(card2);
                DistrictCard card3 = deck.remove(0);
                tempHand.add(card3);
                DistrictCard card4 = deck.remove(0);
                tempHand.add(card4);
                System.out.println("You drew 2 cards with another 2 extra cards from architect action.");
                System.out.println("1. " + card1);
                System.out.println("2. " + card2);
                System.out.println("3. " + card3);
                System.out.println("4. " + card4);
                System.out.println("How many buildings you want to buid? ");
                System.out.println("> ");
                int numChoice;
                while (true) {
                    try {
                        numChoice = Integer.parseInt(scanner.nextLine().trim());
                        if (numChoice == 0 || numChoice == 1 || numChoice == 2 || numChoice == 3) {break;}
                        System.out.println("Please enter 0, 1, 2 or 3");
                    } catch (NumberFormatException e) {
                        System.out.println("Enter a valid number.");
                    }
                }
                if (numChoice == 0) {
                    System.out.println("You chose not to build any buildings.");
                    deck.add(card1);
                    deck.add(card2);
                    deck.add(card3);
                    deck.add(card4);
                    return;
                } else {
                    int cardChoiceIdx;
                    for (int i = 0; i < numChoice; i++){
                        while (true) {
                            System.out.println("Select card: ");
                            System.out.print("> ");
                            try {
                                cardChoiceIdx = Integer.parseInt(scanner.nextLine().trim()) - 1;
                                if (cardChoiceIdx >= 0 && cardChoiceIdx < tempHand.size()) break;
                                System.out.println("Please enter available cards.");
                            } catch (NumberFormatException e) {
                                System.out.println("Enter a valid number.");
                            }
                        }
                        DistrictCard chosenOne = tempHand.get(cardChoiceIdx);
                        if (player.getGold() < chosenOne.getCost()) {
                            System.out.println("You don't have enough gold.");
                            continue;
                        }
                        player.setGold(player.getGold() - chosenOne.getCost());
                        player.setHand(chosenOne);
                        System.out.println("Player " + player.getPlayerNo() + " built a " + chosenOne.getName() + "[" + chosenOne.getColor() + chosenOne.getCost() + "]" + "in their city.");
                        tempHand.remove(cardChoiceIdx);
                    }
                }
            }

        //bot play
        } else {
            //bot perform architect
            boolean chooseOption1 = random.nextBoolean();  // 50/50 choice

            List<DistrictCard> drawnCards = new ArrayList<>();

            if (chooseOption1) {
                // Option 1: Collect 2 gold + draw 2 cards
                player.setGold(player.getGold() + 2);
                for (int i = 0; i < 2 && !deck.isEmpty(); i++) {
                    drawnCards.add(deck.remove(0));
                }
                System.out.println("AI Player " + player.getPlayerNo() + " took 2 gold and drew " + drawnCards.size() + " card(s).");

            } else {
                // Option 2: Draw 4 cards
                for (int i = 0; i < 4 && !deck.isEmpty(); i++) {
                    drawnCards.add(deck.remove(0));
                }
                System.out.println("AI Player " + player.getPlayerNo() + " drew " + drawnCards.size() + " card(s).");
            }

            // Try to build up to 3 affordable districts
            drawnCards.sort(Comparator.comparingInt(DistrictCard::getCost)); // Sort cheapest first

            int builds = 0;
            Iterator<DistrictCard> it = drawnCards.iterator();

            while (it.hasNext() && builds < 3) {
                DistrictCard card = it.next();
                if (player.getGold() >= card.getCost()) {
                    player.setGold(player.getGold() - card.getCost());
                    player.setHand(card);
                    builds++;
                    System.out.println("Player " + player.getPlayerNo() + " built a " + card.getName() + "[" + card.getColor() + card.getCost() + "]");
                    it.remove();
                }
            }

            // Return any unused cards to deck
            deck.addAll(drawnCards);
            return;
        }
    }

    //Warlord
    public static void warlord(Player player, List<Player> players, List<DistrictCard> deck) {
        Scanner scanner = new Scanner(System.in);

        //Gain 1 gold for red district
        int bonusGold = 0;
        for (DistrictCard card : player.getDistricts()) {
            if (card.getColor().equalsIgnoreCase("red")) {
                bonusGold++;
            }
        }
        player.setGold(player.getGold() + bonusGold);
        System.out.println("Player " + player.getPlayerNo() + " gained " + bonusGold + " gold from red (millitary) districts.");

        // Check if player wants to destroy a district
        //human play
        if (player.getPlayerNo() == 1) {
            System.out.println("Do you want to destroy a district? (y/n)");
            String response = scanner.nextLine().trim().toLowerCase();
            if (!response.equals("y")) {
                return;
            }
            List<Player> possibleTargets = new ArrayList<>();
            for (Player target : players) {
                if (target.getPlayerNo() == player.getPlayerNo() || target.getDistricts().isEmpty() || target.getDistricts().size() >= 8) {
                    continue;
                }
                // cannot touch Bishop
                if (target.getCharacter().getName().equalsIgnoreCase("Bishop") && target.isKilled()) {
                    continue;
                }

                possibleTargets.add(target);
            }

            if (possibleTargets.isEmpty()) {
                System.out.println("No valid districts to destroy.");
                return;
            }

            System.out.print("Choose a player to target:\n");
            for (Player p : possibleTargets) {
                System.out.println("Player " + p.getPlayerNo());
            }

            int targetNo;
            Player targetPlayer = null;
            while (true) {
                System.out.print("> ");
                try {
                    targetNo = Integer.parseInt(scanner.nextLine().trim());
                    for (Player p : possibleTargets) {
                        if (p.getPlayerNo() == targetNo) {
                            targetPlayer = p;
                            break;
                        }
                    }
                    if (targetPlayer != null) {
                        break;
                    }
                    System.out.println("Invalid player. Try again.");
                } catch (NumberFormatException e) {
                    System.out.println("Enter a valid number.");
                }
            }

            List<DistrictCard> targetDistricts = targetPlayer.getDistricts();
            System.out.println("Target Player " + targetPlayer.getPlayerNo() + "'s:");
            for (int i = 1; i <= targetPlayer.getDistricts().size(); i++) {
                System.out.println(i + ": " + targetDistricts.get(i));
            }

            //Choose which district to destroy
            int chosenIndex = -1;
            while (true) {
                System.out.print("Enter index of district to destroy: ");
                try {
                    chosenIndex = Integer.parseInt(scanner.nextLine().trim());
                    if (chosenIndex >= 0 && chosenIndex < targetDistricts.size()) {
                        break;
                    }
                    System.out.println("Invalid index.");
                } catch (NumberFormatException e) {
                    System.out.println("Enter a valid number.");
                }
            }

            DistrictCard toDestroy = targetDistricts.get(chosenIndex);
            int cost = toDestroy.getCost() - 1;

            if (cost > player.getGold()) {
                System.out.println("You don't have enough gold to destroy this district (cost: " + cost + ").");
            } else {
                player.setGold(player.getGold() - cost);
                targetDistricts.remove(chosenIndex);
                System.out.println("Destroy " + toDestroy.getName() + " from Player " + targetPlayer.getPlayerNo());
            }
        //bot play
        } else {
            for (Player target : players) {
                if (target.getPlayerNo() == player.getPlayerNo() || target.getDistricts().isEmpty() || target.getDistricts().size() >= 8) {
                    continue;
                }
                if (target.getCharacter().getName().equalsIgnoreCase("Bishop") && !target.isKilled()) {
                    continue;
                }
                List <DistrictCard> districts = target.getDistricts();
                for (int i = 0; i < districts.size(); i++) {
                    DistrictCard dc = districts.get(i);
                    int cost = dc.getCost() - 1;
                    if (cost <= player.getGold()) {
                        player.setGold(player.getGold() - cost);
                        districts.remove(i);
                        System.out.println("Player " + player.getPlayerNo() + " destroyed " + dc.getName() + " from Player " + target.getPlayerNo());
                        return;
                    }
                }
            }
        }
    }




    //Helper
    private static String orderToChar(int order) {
        String character = null;
        if (order == 1) {
            character = "Assassin";
        } else if (order == 2) {
            character = "Thief";
        } else if (order == 3) {
            character = "Magician";
        } else if (order == 4) {
            character = "King";
        } else if (order == 5) {
            character = "Bishop";
        } else if (order == 6) {
            character = "Merchant";
        } else if (order == 7) {
            character = "Architect";
        } else if (order == 8) {
            character = "Warlord";
        }
        return character;
    }
    private static boolean isCharacterKilled(int order, List<Player> players) {
        for (Player p : players) {
            if (p.getCharacter() != null && p.getCharacter().getOrder() == order && p.isKilled()) {
                return true;
            }
        }
        return false;
    }
}