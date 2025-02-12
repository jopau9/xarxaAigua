/**
 * @file controladorTancar.java
 * @brief Classe controladorTancar que controla la finestra tancar.fxml.
 *        Gestiona el tancament d'aixetes de la xarxa.
 *        Permet seleccionar una aixeta i tancar-la.
 *        Proporciona una interfície per a tancar aixetes.
 * @author Meritxell Masdevall i Esparch
 */

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.event.ActionEvent;
import java.util.ArrayList;

public class controladorTancar {

    @FXML
    private ChoiceBox<String> escollirAixetes; ///< Llista d'aixetes obertes

    private controladorPrincipal controladorPrincipal; ///< Referència al controlador principal

    /**
     * @brief Estableix el controlador principal per a aquesta finestra.
     * @param controller El controlador principal.
     * @pre controller != null
     * @post S'estableix el controlador principal per a aquesta finestra.
     */
    public void setParentController(controladorPrincipal controller) {
        this.controladorPrincipal = controller;
        inicialitzar();
    }

    /**
     * @brief Inicialitza la finestra amb les dades necessàries.
     * @pre controladorPrincipal != null
     * @post La finestra s'inicialitza amb les dades necessàries.
     */
    void inicialitzar() {
        ArrayList<Aixeta> aixetes = controladorPrincipal.obtenirAixetes();
        if (aixetes != null) {
            for (Aixeta aixeta : aixetes) {
                if(aixeta.estat()) {
                    escollirAixetes.getItems().add(aixeta.nom());
                }
            }
        }
    }

    /**
     * @brief Tanca l'aixeta seleccionada i tanca la finestra.
     * @param event L'esdeveniment que ha provocat la crida.
     * @pre escollirAixetes.getValue() != null
     * @post L'aixeta seleccionada es tanca i la finestra es tanca.
     */
    @FXML
    void tancarAixeta(ActionEvent event) {
        String nom = escollirAixetes.getValue();
        if (nom == null) {
            controladorPrincipal.mostrarAlerta("Falten dades: tots els camps són obligatoris.");
            return;
        }
        controladorPrincipal.tancarFinestra(event);
        controladorPrincipal.tancar(nom);
    }

    /**
     * @brief Cancel·la l'operació i tanca la finestra.
     * @param event L'esdeveniment que ha provocat la crida.
     * @post L'operació es cancel·la i la finestra es tanca.
     */
    @FXML
    void cancelarOperacio(ActionEvent event) {
        controladorPrincipal.tancarFinestra(event);
    }
}
