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


    public float getImpact(Ship ship) {
        return (getWeightImpact(ship) + getSkillImpact(ship)) / 2;
    }


    private float getWeightImpact(Ship ship) {
        return (float) ((100 - this.weight) / ship.getWeight());
    }

    private float getSkillImpact(Ship ship) {
        return (normalize10(this.abilities.get(ship.getType())) + normalize100(this.winRate)) / 2;
    }


    private float normalize10(float number) {
        return (float) (number / 10.0);
    }

    private float normalize100(float number) {
        return (float) (number / 100.0);
    }


    public int getNum_membership() {
        return num_membership;
    }

    public String getName() {
        return name;
    }
}
