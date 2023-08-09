package sample.LoginSystem;

/**
 * Singleton class which instance is created when user has successfully logged into system. Instance of this class holds information about logged user and his privileges
 * @author Daniel Czyz
 * @version 1.0.1 29/05/2021
 */
public final class UserSession {

    private static UserSession instance;

    private int id;
    private String username;
    private String firstname;
    private String lastname;
    private String position;
    private int[] privileges;

    /**
     * Private constructor which is executed from getInstance method
     * @param id user id in database
     * @param username username assigned to user in database
     * @param firstname firstname assigned to user in database
     * @param lastname lastname assigned to user in database
     * @param position position assigned to user in database
     * @param privileges privileges assigned to user in database
     */
    private UserSession(int id, String username, String firstname, String lastname, String position , int[] privileges) {
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.position = position;
        this.privileges = privileges;
    }

    /**
     * Static method, when called and there is no instance of this class yet it creates one by executing constructor and then returns it,
     * if there already exists one it does nothing but returns previously created instance
     * @param id user id in database
     * @param username username assigned to user in database
     * @param firstname firstname assigned to user in database
     * @param lastname lastname assigned to user in database
     * @param position position assigned to user in database
     * @param privileges privileges assigned to user in database
     * @return instance of this class
     */
    public static UserSession getInstance(int id, String username, String firstname, String lastname, String position, int[] privileges) {
        if(instance == null) {
            instance = new UserSession(id, username, firstname, lastname, position, privileges);
        }
        return instance;
    }

    /**
     * Getter for user id variable
     * @return user id
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for user username variable
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter for user firstname variable
     * @return firstname
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Getter for user lastname variable
     * @return lastname
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * Getter for user position variable
     * @return position
     */
    public String getPosition() {
        return position;
    }

    /**
     * Getter for user privileges variable
     * @return privileges
     */
    public int[] getPrivileges() {
        return privileges;
    }

    /**
     * Cleans all data of existing instance of this class
     */
    public void cleanUserSession() {
        id = 0;
        username = "";
        firstname = "";
        lastname = "";
        position = "";
        privileges = new int[]{};
        instance = null;
    }

    /**
     * Converts instance variables data into strings
     * @return concatenated data from variables as one string
     */
    @Override
    public String toString() {
        return "UserSession{" +
                "id='" + id + '\'' +
                "username='" + username + '\'' +
                "firstname'" + firstname + '\'' +
                "lastname'" + lastname + '\'' +
                "position'" + position + '\'' +
                ", privileges=" + privileges +
                '}';
    }
}
