package sample.tables;

public class PositionTable {

    String id_position;
    String position_name;

    public PositionTable(String id_position, String position_name){
        this.id_position = id_position;
        this.position_name = position_name;
    }

    public String getId_position() {
        return id_position;
    }

    public void setId_position(String id_position) {
        this.id_position = id_position;
    }

    public String getPosition_name() {
        return position_name;
    }

    public void setPosition_name(String position_name) {
        this.position_name = position_name;
    }
}
