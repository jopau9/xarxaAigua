/**
 * @file controladorInici.java
 * @brief Classe ControladorInici que controla la finestra inici.fxml i gestiona l'obertura de fitxers i el pas a la següent finestra.
 * @author Meritxell Masdevall i Esparch
 */

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class controladorInici {

    private File fitxer; ///< Fitxer seleccionat pel usuari

    /**
     * @brief Gestiona l'acció de passar a la següent finestra
     * @param event Event d'acció
     * @throws IOException Si hi ha algun problema en carregar la finestra
     * @pre Cert
     * @post Es carrega la finestra del fitxer de sortida amb el fitxer seleccionat, es tanca la finestra actual i s'obre la nova finestra
     */
    @FXML
    void seguentFinestra(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("triaFitxer.fxml"));
        Parent root = loader.load();
        controladorFitxer contFitxer = loader.getController();
        contFitxer.setFitxerSeleccionat(fitxer);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Xarxa d'Aigua");
        stage.setScene(scene);
        Image icona = new Image(getClass().getResourceAsStream("icona.png"));
        stage.getIcons().add(icona);
        stage.show();
        Stage actual = (Stage) ((Node) event.getSource()).getScene().getWindow();
        actual.close();
    }

    /**
     * @brief Gestiona l'acció de triar un fitxer
     * @param event Event d'acció
     * @throws IOException Si hi ha algun problema en carregar la finestra
     * @pre Cert
     * @post Es mostra una finestra de diàleg per seleccionar un fitxer de text. Si es selecciona un fitxer, es passa a la següent finestra.
     */
    @FXML
    void triaFitxer(ActionEvent event) throws IOException {
        FileChooser triaFitxer = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fitxers de text (*.txt)", "*.txt");
        triaFitxer.getExtensionFilters().add(extFilter);
        fitxer = triaFitxer.showOpenDialog(null);
        if (fitxer != null) {
            seguentFinestra(event);
        }
    }
}
