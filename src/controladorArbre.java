/**
 * @file controladorArbre.java
 * @brief Classe ControladorArbre que controla la finestra arbre.fxml i gestiona la selecció d'una aixeta per a l'operació d'arbre.
 * @author Meritxell Masdevall i Esparch
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

import java.util.ArrayList;

public class controladorArbre {
    private controladorPrincipal controladorPrincipal; ///< Referència al controlador principal

    @FXML
    private ChoiceBox<String> aixeta; ///< Caixa de selecció per a les aixetes

    /**
     * @param controlador El controlador principal
     * @pre El paràmetre "controlador" ha de ser una instància vàlida de la classe "controladorPrincipal".
     * @post S'assigna el controlador rebut com a paràmetre com a controlador pare d'aquest objecte. A més, es crida al mètode "inicialitzar" per realitzar qualsevol configuració inicial necessària.
     * @brief Estableix el controlador principal
     */
    public void setParentController(controladorPrincipal controlador) {
        controladorPrincipal = controlador;
        inicialitzar();
    }

    /**
     * @brief Inicialitza els components de la finestra
     * @pre El controlador principal és vàlid i conté la llista d'aixetes
     * @post S'han afegit les aixetes disponibles a la caixa de selecció
     */
    void inicialitzar() {
        ArrayList<Aixeta> aixetes = controladorPrincipal.obtenirAixetes();
        if (aixetes != null) {
            for (Aixeta a : aixetes) {
                aixeta.getItems().add(a.nom());
            }
        }
    }

    /**
     * @param event Event d'acció
     * @brief Gestiona l'acció de selecció d'una aixeta per a l'operació d'arbre
     * @pre L'usuari ha seleccionat una aixeta de la caixa de selecció
     * @post Si s'ha seleccionat una aixeta, s'ha tancat la finestra i s'ha iniciat l'operació d'arbre amb l'aixeta seleccionada
     */
    @FXML
    void esArbre(ActionEvent event) {
        String a = aixeta.getValue();
        if (a == null) {
            controladorPrincipal.mostrarAlerta("Falten dades: és obligatori escollir una aixeta.");
            return;
        }
        controladorPrincipal.tancarFinestra(event);
        controladorPrincipal.esArbre(a);
    }

    /**
     * @param event Event d'acció
     * @brief Cancel·la l'operació de selecció d'una aixeta per a l'operació d'arbre
     * @pre Cert
     * @post La finestra s'ha tancat sense realitzar cap acció
     */
    @FXML
    void cancelarOperacio(ActionEvent event) {
        controladorPrincipal.tancarFinestra(event);
    }
}
