package business;

import java.util.HashMap;

public class Sailor {

    private int num_membership;
    private String name;
    private double weight;
    private ShipType abilities;
    private int win_rate;

    public Sailor(int num_membership, String name, double weight, ShipType abilities, int win_rate) {
        this.num_membership = num_membership;
        this.name = name;
        this.weight = weight;
        this.abilities = abilities;
        this.win_rate = win_rate;
    }
}
