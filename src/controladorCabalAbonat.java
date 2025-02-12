/**
 * @file controladorCabalAbonat.java
 * @brief Classe ControladorCabalAbonat que controla la finestra cabalAbonat.fxml i gestiona el càlcul del cabal abonat per un client.
 * @author Meritxell Masdevall i Esparch
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class controladorCabalAbonat {
    private controladorPrincipal controladorPrincipal; ///< Referència al controlador principal

    @FXML
    private TextField dniT; ///< Camp de text per al DNI del client

    /**
     * @param controlador El controlador principal
     * @brief Estableix el controlador principal
     * @pre El paràmetre "controlador" ha de ser una instància vàlida de la classe "controladorPrincipal".
     * @post S'assigna el controlador rebut com a paràmetre com a controlador pare d'aquest objecte.
     */
    public void setParentController(controladorPrincipal controlador) {
        controladorPrincipal = controlador;
    }

    /**
     * @param event Event d'acció
     * @brief Gestiona l'acció de càlcul del cabal abonat per un client
     * @pre El camp de text dniT conté el DNI vàlid del client
     * @post Si el DNI no és buit, es tanca la finestra i s'inicia el càlcul del cabal abonat per al client
     */
    @FXML
    void cabalAbonat(ActionEvent event) {
        String dni = dniT.getText();
        if (dni.isEmpty()) {
            controladorPrincipal.mostrarAlerta("Cal indicar el DNI del client.");
            return;
        }
        controladorPrincipal.tancarFinestra(event);
        controladorPrincipal.cabalA(dni);
    }

    /**
     * @param event Event d'acció
     * @brief Cancel·la l'operació de càlcul del cabal abonat
     * @pre Cert
     * @post La finestra s'ha tancat sense realitzar cap acció
     */
    @FXML
    void cancelarOperacio(ActionEvent event) {
        controladorPrincipal.tancarFinestra(event);
    }
}
