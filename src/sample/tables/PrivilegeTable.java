package sample.tables;

public class PrivilegeTable {

    String id_position;
    String module;
    String access;

    public PrivilegeTable(String id_position, String module, String access) {
        this.id_position = id_position;
        this.module = module;
        this.access = access;
    }

    public String getId_position() {
        return id_position;
    }

    public void setId_position(String id_position) {
        this.id_position = id_position;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }
}
