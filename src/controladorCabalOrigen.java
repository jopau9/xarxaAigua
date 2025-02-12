/**
 * @file controladorCabalOrigen.java
 * @brief Classe ControladorCabalOrigen que controla la finestra cabalOrigen.fxml i gestiona l'establiment del cabal per un origen.
 * @author Meritxell Masdevall i Esparch
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class controladorCabalOrigen {

    private controladorPrincipal controladorPrincipal; ///< Referència al controlador principal

    @FXML
    private ChoiceBox<String> origenT; ///< Caixa de selecció per a l'origen

    @FXML
    private TextField cabalT; ///< Camp de text per al cabal

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
     * @post S'han afegit els orígens disponibles a la caixa de selecció
     */
    void inicialitzar() {
        ArrayList<Aixeta> aixetes = controladorPrincipal.obtenirAixetes();
        if (aixetes != null) {
            for (Aixeta aixeta : aixetes) {
                if(aixeta instanceof Origen) {
                    origenT.getItems().add(aixeta.nom());
                }
            }
        }
    }

    /**
     * @param event Event d'acció
     * @brief Gestiona l'acció d'establir el cabal per un origen
     * @pre L'usuari ha seleccionat un origen de la caixa de selecció i ha introduït el cabal
     * @post Si s'ha seleccionat un origen i el cabal és vàlid, es tanca la finestra i s'estableix el cabal per a l'origen seleccionat
     */
    @FXML
    void establir(ActionEvent event) {
        String nom = origenT.getValue();
        String c = cabalT.getText();
        if (nom.isEmpty() || c.isEmpty()) {
            controladorPrincipal.mostrarAlerta("Falten dades: tots els camps són obligatoris.");
            return;
        }
        float cabal;
        try {
            cabal = Float.parseFloat(c);
            if (cabal < 0) {
                controladorPrincipal.mostrarAlerta("El cabal no pot ser negatiu.");
                return;
            }
        } catch (NumberFormatException e) {
            controladorPrincipal.mostrarAlerta("El cabal ha de ser un número vàlid.");
            return;
        }
        controladorPrincipal.tancarFinestra(event);
        controladorPrincipal.establirC(nom, cabal);
    }

    /**
     * @param event Event d'acció
     * @brief Cancel·la l'operació d'establir el cabal per un origen
     * @pre Cert
     * @post La finestra s'ha tancat sense realitzar cap acció
     */
    @FXML
    void cancelarOperacio(ActionEvent event) {
        controladorPrincipal.tancarFinestra(event);
    }
}
