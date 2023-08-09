package sample.ParentScene;

import org.junit.Before;

public class ModuleParentTest {

    private ModuleParent moduleParent;

    @Before
    public void setUp() throws Exception {
        moduleParent = new ModuleParent();
    }

 /*   @Test
    public void databaseDumpValid() {
        try {

            Boolean instance = moduleParent.databaseDumpPopulated("root", "C:\\xampp\\mysql\\bin\\mysql");
            assertTrue(instance); //should be true

            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.getConnection();

            String howManyUsersQuery = "SELECT COUNT(id_user) AS usersNumber FROM user ";
            Statement statement = connectDB.createStatement();
            ResultSet resultSet = statement.executeQuery(howManyUsersQuery);

            while (resultSet.next()){
                assertEquals(4,(resultSet.getInt("usersNumber")));
                break;
            }

            String isAdminQuery = "SELECT id_user, username, password, position_name, module, access FROM user, position, privilege WHERE position.id_position = user.id_position AND privilege.id_position = position.id_position AND id_user = 1";
            statement = connectDB.createStatement();
            resultSet = statement.executeQuery(isAdminQuery);

            while (resultSet.next()){
                assertEquals("admin",(resultSet.getString("username")));
                assertEquals("admin",(resultSet.getString("password")));
                assertEquals("Administrator",(resultSet.getString("position_name")));
                assertEquals(1,(resultSet.getInt("access")));
            }

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }

    }

    @Test
    public void databaseDumpInvalid() {
        try {
            Boolean instance = moduleParent. databaseDump("rooooooooot", "C:\\xampp\\mysql\\bin\\mysql");
            assertFalse(instance); //should be false

        }catch(Exception e){
            e.printStackTrace();
            e.getCause();
        }

    }*/
}