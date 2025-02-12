/**
 * @file controladorFitxer.java
 * @brief Classe ControladorFitxer que controla la finestra triaFitxer.fxml i gestiona el nom del fitxer de sortida i el pas a la següent finestra.
 * @author Meritxell Masdevall i Esparch
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.control.TextField;

import java.io.*;

public class controladorFitxer {

    File f; ///< Fitxer de dades d'entrada

    @FXML
    private TextField fitxerOut; ///< Text field per a introduir el nom del fitxer de sortida

    /**
     * @brief Estableix el fitxer d'entrada.
     * @param fitxer El fitxer s'entrada.
     * @pre Cert
     * @post es guarda fitxer a f.
     */
    public void setFitxerSeleccionat(File fitxer){
        f=fitxer;
    }

    /**
     * @brief Gestiona l'acció de passar a la següent finestra
     * @param event Event d'acció
     * @throws IOException Si hi ha algun problema en carregar la finestra
     * @pre Cert
     * @post Es carrega la finestra principal amb el fitxer seleccionat, es tanca la finestra actual i s'obre la nova finestra
     */
    @FXML
    void seguentFinestra(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("principal.fxml"));
        Parent root = loader.load();
        controladorPrincipal contPrincipal = loader.getController();
        String nomFitxer = fitxerOut.getText();
        PrintStream fitxer = new PrintStream(new FileOutputStream(nomFitxer));
        contPrincipal.setFitxersSeleccionats(f, fitxer);
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
     * @brief Torna al menú principal de l'aplicació.
     * @param event L'esdeveniment que causa el retorn al menú.
     * @throws IOException Si hi ha algun error durant la càrrega del menú.
     * @pre Cert
     * @post Es tanca la finestra actual i s'obre la finestra d'inici de l'aplicació.
     */
    @FXML
    void cancelar(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("iniciX.fxml"));
        Parent root = loader.load();
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
}
