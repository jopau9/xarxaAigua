/**
 * @file controladorObrir.java
 * @brief Classe ControladorObrir que controla la finestra obrir.fxml i gestiona l'obertura d'aixetes a la xarxa.
 * @author Meritxell Masdevall i Esparch
 */

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

import javafx.event.ActionEvent;
import java.util.ArrayList;

public class controladorObrir{

    @FXML
    private ChoiceBox<String> escollirAixetes; ///< ChoiceBox per seleccionar l'aixeta a obrir

    private controladorPrincipal controladorPrincipal; ///< Referència al controlador principal

    /**
     * @param controller El controlador principal
     * @pre Cert
     * @post S'estableix el controlador principal com a controlador pare d'aquest objecte
     * @brief Estableix el controlador principal
     */
    public void setParentController(controladorPrincipal controller) {
        this.controladorPrincipal = controller;
        inicialitzar();
    }

    /**
     * @pre Cert
     * @post El ChoiceBox es carrega amb els noms de les aixetes disponibles per a obertura
     * @brief Inicialitza el ChoiceBox amb les aixetes disponibles per a obertura
     */
    void inicialitzar() {
        ArrayList<Aixeta> aixetes = controladorPrincipal.obtenirAixetes();
        if (aixetes != null) {
            for (Aixeta aixeta : aixetes) {
                if(!aixeta.estat()) {
                    escollirAixetes.getItems().add(aixeta.nom());
                }
            }
        }
    }

    /**
     * @param event Event d'acció
     * @pre Cert
     * @post Si s'ha seleccionat una aixeta, s'obre i es tanca la finestra d'obertura
     * @brief Gestiona l'acció d'obrir una aixeta
     */
    @FXML
    void obrirAixeta(ActionEvent event) {
        String nom = escollirAixetes.getValue();
        if (nom == null) {
            controladorPrincipal.mostrarAlerta("Falten dades: tots els camps són obligatoris.");
            return;
        }
        controladorPrincipal.tancarFinestra(event);
        controladorPrincipal.obrir(nom);
    }

    /**
     * @param event Event d'acció
     * @pre Cert
     * @post Es tanca la finestra sense realitzar cap acció
     * @brief Cancel·la l'operació d'obrir una aixeta
     */
    @FXML
    void cancelarOperacio(ActionEvent event) {
        controladorPrincipal.tancarFinestra(event);
    }
}
