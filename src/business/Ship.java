package business;

public class Ship {

    private int id;
    private String name;
    private ShipType type;
    private int weight;
    private int length;
    private int capacity;
    private int numCompetitions;
    private String state;
    private int speed;
    private String center;


    public Ship(int id, String name, ShipType type, int weight, int length, int capacity, int numCompetitions, String state, int speed, String center) {
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
