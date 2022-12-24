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

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ShipType getType() {
        return type;
    }

    public double getWeight() {
        return weight;
    }

    public double getLength() {
        return length;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getNumCompetitions() {
        return numCompetitions;
    }

    public String getState() {
        return state;
    }

    public int getSpeed() {
        return speed;
    }

    public String getCenter() {
        return center;
    }
}
