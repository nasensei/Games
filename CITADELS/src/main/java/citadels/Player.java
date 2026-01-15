package citadels;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import java.io.*;
import java.util.*;

public class Player {
    private int playerNo;
    private int gold;
    private boolean crown;
    private List<DistrictCard> ownDistricts;
    private Character chosenChar;
    private boolean isKilled = false;
    private boolean isRobbed = false;

    

    public Player(int playerNo) {
        this.playerNo = playerNo;
        this.gold = 0;
        this.crown = false;
        this.ownDistricts = new ArrayList<>();
        this.chosenChar = null;
    }

    public int getPlayerNo() {
        return playerNo;
    }
    public int getGold() {
        return gold;
    }
    public boolean isCrown() {
        return crown;
    }
    public Character getCharacter() {
        return chosenChar;
    }
    public List<DistrictCard> getDistricts() {
        return ownDistricts;
    }
    public boolean isKilled() {
        return isKilled;
    }
    public boolean isRobbed() {
        return isRobbed;
    }


    public void setGold(int gold) {
        this.gold = gold;
    }
    public void setIsCrown(boolean crown) {
        this.crown = crown;
    }
    public String getAllDistricts() {
        if (ownDistricts.isEmpty()) {
            return " ";
        }
        StringBuilder sb = new StringBuilder();
        int numDistricts = ownDistricts.size();
        for (int i = 0; i < numDistricts; i++) {
            String eachDistrictName = ownDistricts.get(i).getName().toString();
            String eachDistrictColor = ownDistricts.get(i).getColor().toString();
            //String eachDistrictCost = ownDistricts.get(i).getCost().toString();
            String eachDistrictCost = Integer.toString(ownDistricts.get(i).getCost());
            sb.append(eachDistrictName + " [" + eachDistrictColor + eachDistrictCost + "]");
            if (i < numDistricts - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
    public void setHand(DistrictCard card) {
        ownDistricts.add(card);
    }
    public void setCharacter(Character character) {
        this.chosenChar = character;
    }
    public void setKilled(boolean killed) {
        this.isKilled = killed;
    }
    public void setRobbed(boolean robbed) {
        this.isRobbed = robbed;
    }



    @Override
    public String toString() {
        String playerInfo = null;
        if (playerNo != 1) {
            playerInfo = "Player " + playerNo + ": cards=" + ownDistricts.size() + " golds=" + gold + " city= " + getAllDistricts();
        } else{
            playerInfo = "Player 1 (you): cards=" + ownDistricts.size() + " golds=" + gold + " city= " + getAllDistricts();
        }
        return playerInfo;
    }
}
