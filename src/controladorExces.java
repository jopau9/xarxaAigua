/**
 * @file controladorExces.java
 * @brief Classe ControladorExces que controla la finestra exces.fxml i gestiona la selecció de canonades amb excés de pressió.
 * @author Meritxell Masdevall i Esparch
 */

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.ArrayList;

public class controladorExces {

    private controladorPrincipal controladorPrincipal; ///< Referència al controlador principal

    @FXML
    private ListView<String> canonades; ///< Llista de canonades

    /**
     * @param controller El controlador principal
     * @brief Estableix el controlador principal
     * @pre El paràmetre "controller" ha de ser una instància vàlida de la classe "controladorPrincipal".
     * @post S'assigna el controlador rebut com a paràmetre com a controlador pare d'aquest objecte. A més, es crida al mètode "inicialitzar" per realitzar qualsevol configuració inicial necessària.
     */
    public void setParentController(controladorPrincipal controller) {
        this.controladorPrincipal = controller;
        inicialitzar();
    }

    /**
     * @brief Inicialitza els components de la finestra
     * @pre El controlador principal és vàlid i conté la llista d'aixetes
     * @post S'han afegit les canonades amb excés de pressió a la llista
     */
    void inicialitzar() {
        ArrayList<Aixeta> aixetes = controladorPrincipal.obtenirAixetes();
        ArrayList<String> canAux = new ArrayList<>();
        if (aixetes != null) {
            for (Aixeta aixeta : aixetes) {
                ArrayList<Canonada> can = aixeta.obtenirCanonades();
                for (Canonada c : can){
                    String nom = c.origen().nom() + "-" + c.desti().nom();
                    if (!canAux.contains(nom)){
                        canAux.add(nom);
                    }
                }
            }
            for(String nom : canAux){
                canonades.getItems().add(nom);
            }
        }
        canonades.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);
    }

    /**
     * @param event Event d'acció
     * @brief Gestiona l'acció de seleccionar les canonades amb excés de pressió
     * @pre L'usuari ha seleccionat almenys una canonada
     * @post Si s'ha seleccionat almenys una canonada, es tanca la finestra i es passa la llista de canonades seleccionades al controlador principal
     */
    @FXML
    void exces(ActionEvent event) {
        ObservableList<String> seleccions = canonades.getSelectionModel().getSelectedItems();
        if (seleccions.isEmpty()) {
            controladorPrincipal.mostrarAlerta("Cal seleccionar almenys una canonada per continuar");
            return;
        }
        controladorPrincipal.tancarFinestra(event);
        controladorPrincipal.excesC(new ArrayList<>(seleccions));
    }

    /**
     * @param event Event d'acció
     * @brief Cancel·la l'operació de selecció de canonades amb excés de pressió
     * @pre Cert
     * @post La finestra s'ha tancat sense realitzar cap acció
     */
    @FXML
    void cancelarOperacio(ActionEvent event) {
        controladorPrincipal.tancarFinestra(event);
    }
}
