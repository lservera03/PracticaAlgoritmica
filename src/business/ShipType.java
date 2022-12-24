package business;

public enum ShipType {


    Windsurf, Optimist, Laser, Pati_Catala, HobieDragoon, HobieCat;



    public static ShipType getEnumValue(String value){

        return switch (value) {
            case "Optimist" -> ShipType.Optimist;
            case "Laser" -> ShipType.Laser;
            case "Patí Català" -> ShipType.Pati_Catala;
            case "HobieDragoon" -> ShipType.HobieDragoon;
            case "HobieCat" -> ShipType.HobieCat;
            default -> ShipType.Windsurf;
        };

    }


}
