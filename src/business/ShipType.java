package business;

public enum ShipType {


    Windsurf, Optimist, Laser, PatiCatala, HobieDragoon, HobieCat;



    public static ShipType getEnumValue(String value){

        return switch (value) {
            case "Optimist" -> ShipType.Optimist;
            case "Laser" -> ShipType.Laser;
            case "Patí Català" -> ShipType.PatiCatala;
            case "HobieDragoon" -> ShipType.HobieDragoon;
            case "HobieCat" -> ShipType.HobieCat;
            default -> ShipType.Windsurf;
        };

    }


}
