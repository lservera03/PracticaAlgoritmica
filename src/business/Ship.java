package business;

public class Ship {

    private int id;
    private String name;
    private ShipType type;
    private double weight;
    private double length;
    private int capacity;
    private int numCompetitions;
    private String state;
    private int speed;
    private String center;


    public Ship(int id, String name, ShipType type, double weight, double length, int capacity, int numCompetitions, String state, int speed, String center) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.weight = weight;
        this.length = length;
        this.capacity = capacity;
        this.numCompetitions = numCompetitions;
        this.state = state;
        this.speed = speed;
        this.center = center;
    }
}
