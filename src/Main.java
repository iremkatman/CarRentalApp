
import controller.LoginController;
import controller.UserController;
import view.LoginView;

public class Main {
    public static void main(String[] args) {
        LoginView loginView = new LoginView();
        UserController userController = new UserController();
        new LoginController(loginView, userController);

        loginView.setVisible(true);
    }
}
