import javax.swing.JOptionPane;

import business.AppUserManager;
import core.Helper;
import tools.CreateAdmin;
import view.LoginView;

public class App {
    public static void main(String[] args) {
        if (args.length == 1 && "--provision-admin".equals(args[0])) {
            CreateAdmin.main(new String[0]);
            return;
        }

        AppUserManager userManager = new AppUserManager();
        if (!userManager.hasAdmin()) {
            JOptionPane.showMessageDialog(
                    null,
                    "No administrator exists. Run App --provision-admin from an interactive terminal first.",
                    "Administrator required",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        Helper.setTheme();
        new LoginView();
    }
}
