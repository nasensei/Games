package citadels;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import java.io.*;
import java.util.*;


public enum FullDeck {
    WATCHTOWER("Watchtower", "red", 1, 3, ""),
    PRISON("Prison", "red", 2, 3, ""),
    BATTLEFIELD("Battlefield", "red", 3, 3, ""),
    FORTRESS("Fortress", "red", 5, 2, ""),
    MANOR("Manor", "yellow", 3, 5, ""),
    CASTLE("Castle", "yellow", 4, 4, ""),
    PALACE("Palace", "yellow", 5, 3, ""),
    TAVERN("Tavern", "green", 1, 5, ""),
    MARKET("Market", "green", 2, 4, ""),
    TRADING_POST("Trading Post", "green", 2, 3, ""),
    DOCKS("Docks", "green", 3, 3, ""),
    HARBOR("Harbor", "green", 4, 3, ""),
    TOWN_HALL("Town Hall", "green", 5, 2, ""),
    TEMPLE("Temple", "blue", 1, 3, ""),
    CHURCH("Church", "blue", 2, 3, ""),
    MONASTARY("Monastary", "blue", 3, 3, ""),
    CATHEDRAL("Cathedral", "blue", 5, 2, ""),
    HAUNTED_CITY("Haunted City", "purple", 2, 1, "For the purposes of victory points, the Haunted City is considered to be of the color of your choice. You cannot use this ability if you built it during the last round of the game"),
    KEEP("Keep", "purple", 3, 2, "The Keep cannot be destroyed by the Warlord"),
    LABORATORY("Laboratory", "purple", 5, 1, "Once during your turn, you may discard a district card from your hand and receive one gold from the bank"),
    SMITHY("Smithy", "purple", 5, 1, "Once during your turn, you may pay two gold to draw 3 district cards."),
    OBSERVATORY("Observatory", "purple", 5, 1, "If you choose to draw cards when you take an action, you draw 3 cards, keep one of your choice, and put the other 2 on the bottom of the deck"),
    GRAVEYARD("Graveyard", "purple", 5, 1, "When the Warlord destroys a district, you may pay one gold to take the destroyed district into your hand. You may not do this if you are the Warlord"),
    DRAGON_GATE("Dragon Gate", "purple", 6, 1, "This district costs 6 gold to build, but is worth 8 points at the end of the game"),
    UNIVERSITY("University", "purple", 6, 1, "This district costs 6 gold to build, but is worth 8 points at the end of the game"),
    LIBRARY("Library", "purple", 6, 1, "If you choose to draw cards you take an action, you keep both of the cards you have drawn."),
    GREAT_WALL("Great Wall", "purple", 6, 1, "The cost for the Warlord to destroy any of your other districts is increased by one gold"),
    SCHOOL_OF_MAGIC("School Of Magic", "purple", 6, 1, "For the purposes of income, the School Of Magic is considered to be the color of your choice. If you are the King this round, for example, the School is considered to be a noble (yellow) district."),
    LIGHTHOUSE("Lighthouse", "purple", 3, 1, "When you place the Lighthouse in your city, you may look through the District Deck, choose one card and add it to your hand. Shuffle the deck afterwards."),
    ARMORY("Armory", "purple", 3, 1, "During your turn, you may destroy the Armory in order to destroy any other district card of your choice in another player's city"),
    MUSEUM("Museum", "purple", 4, 1, "On your turn, you may place one district card from your hand face down under the Museum. At the end of the game, you score one extra point for every card under the Museum"),
    IMPERIAL_TREASURY("Imperial Treasury", "purple", 4, 1, "At the end of the game, you score one point for each gold in your possession. Gold placed on your district cards do not count towards this total"),
    MAP_ROOM("Map Room", "purple", 5, 1, "At the end of the game, you score one point for each card in your hand."),
    WISHING_WELL("Wishing Well", "purple", 5, 1, "At the end of the game, you score one point for every OTHER purple district in your city"),
    QUARRY("Quarry", "purple", 5, 1, "When building, you may play a district already found in your city. You may only have one such duplicate district in your city at any one time"),
    POOR_HOUSE("Poor House", "purple", 5, 1, "If you have no gold at the end of your turn, receive one gold from the bank. Gold placed on your district cards does not count as your gold for this purpose"),
    BELL_TOWER("Bell Tower", "purple", 5, 1, "When you place the Bell Tower in your city, you may announce that the game will end after the round in which a player first places his 7th district. You may do this even if the Bell Tower is your 7th district. If the Bell Tower is later destroyed, the game end conditions return to normal"),
    FACTORY("Factory", "purple", 6, 1, "Your cost for building OTHER purple district cards is reduced by one. This does not affect the Warlord's cost for destroying the card"),
    PARK("Park", "purple", 6, 1, "If you have no cards in your hand at the end of your turn, you may draw 2 cards from the District Deck."),
    HOSPITAL("Hospital", "purple", 6, 1, "Even if you are assassinated, you may take an action during your turn (but you may not build a district card or use your character's power)."),
    THRONE_ROOM("Throne Room", "purple", 6, 1, "Every time the Crown switches players, you receive one gold from the bank.");


    private final String name;
    private final String color;
    private final int cost;
    private final int quantity;
    private final String ability;

    FullDeck(String name, String color, int cost, int quantity, String ability) {
        this.name = name;
        this.color = color;
        this.cost = cost;
        this.quantity = quantity;
        this.ability = ability;
    }

    public String getName() {
        return name;
    }
    public String getColor() {
        return color;
    }
    public int getCost() {
        return cost;
    }
    public int getQuantity() {
        return quantity;
    }
    public String getAbility() {
        return ability;
    }
}
