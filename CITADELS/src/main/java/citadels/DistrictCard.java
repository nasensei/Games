package citadels;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import java.io.*;
import java.util.*;


public class DistrictCard {
    private String name;
    private String color;
    private int cost;
    private String ability;

    public DistrictCard(String name, String color, int cost, String ability) {
        this.name = name;
        this.color = color;
        this.ability = ability;
        this.cost = cost;
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
    public String getAbility() {
        return ability;
    }

    @Override //override method from a superclass (in this case, Object.toString())
    // this defines a method that returns a String
    public String toString() {
        String card = name + " [" + color + "], " + "cost: " + cost;
        return card;
    }
}
