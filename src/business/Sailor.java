package business;

import java.util.HashMap;

public class Sailor {

    private int num_membership;
    private String name;
    private double weight;
    private HashMap<ShipType, Integer> abilities;
    private int winRate;

    public Sailor(int num_membership, String name, double weight, HashMap<ShipType, Integer> abilities, int winRate) {
        this.num_membership = num_membership;
        this.name = name;
        this.weight = weight;
        this.abilities = abilities;
        this.winRate = winRate;
    }


    public double getImpact(Ship ship) {
        return (getWeightImpact(ship) + getSkillImpact(ship)) / 2;
    }


    private double getWeightImpact(Ship ship) {
        return (100 - this.weight) / ship.getWeight();
    }

    private double getSkillImpact(Ship ship) {
        return (normalize(this.abilities.get(ship.getType())) + normalize(this.winRate)) / 2;
    }


    private double normalize(double number) {
        return number / 10;
    }


}
