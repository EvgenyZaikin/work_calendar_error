import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.InputStream;

public class Main extends Application {
    public void start(Stage primaryStage) throws Exception {
        DatabaseHandler.Connect();

        Parent mainPanel = FXMLLoader.load(getClass().getResource("main_window.fxml"));
        //Parent mainPanel = FXMLLoader.load(Main.class.getResourceAsStream("/main_window.fxml"));

        primaryStage.setTitle("Производственный календарь");
        primaryStage.setScene(new Scene(mainPanel));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
