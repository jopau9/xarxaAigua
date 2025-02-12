import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

public class XarxaAiguaGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/iniciX.fxml"));

        Scene scene = new Scene(root);

        stage.setTitle("Xarxa d'Aigua");
        stage.setScene(scene);
        Image icona = new Image(getClass().getResourceAsStream("/icona.png"));
        stage.getIcons().add(icona);
        stage.show();
    }
}