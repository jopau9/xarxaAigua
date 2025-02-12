/**
 * @file controladorCabalMinim.java
 * @brief Classe ControladorCabalMinim que controla la finestra cabalMinim.fxml i gestiona el càlcul del cabal mínim per un origen.
 * @author Meritxell Masdevall i Esparch
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class controladorCabalMinim {

    private controladorPrincipal controladorPrincipal; ///< Referència al controlador principal

    @FXML
    private ChoiceBox<String> origenT; ///< Caixa de selecció per a l'origen

    @FXML
    private TextField demanda; ///< Camp de text per a la demanda

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
     * @brief Gestiona l'acció de càlcul del cabal mínim per un origen
     * @pre L'usuari ha seleccionat un origen de la caixa de selecció i ha introduït la demanda
     * @post Si s'ha seleccionat un origen i la demanda és vàlida, es tanca la finestra i s'inicia el càlcul del cabal mínim per a l'origen seleccionat
     */
    @FXML
    void cabal(ActionEvent event) {
        String nom = origenT.getValue();
        String d = demanda.getText();
        if (nom.isEmpty() || d.isEmpty()) {
            controladorPrincipal.mostrarAlerta("Falten dades: tots els camps són obligatoris.");
            return;
        }
        String numeroString = d.replaceAll("[^0-9]", "");
        float percent;
        try {
            percent = Float.parseFloat(numeroString) / 100;
            if (percent < 0) {
                controladorPrincipal.mostrarAlerta("La demanda no pot ser negativa.");
                return;
            }
        } catch (NumberFormatException e) {
            controladorPrincipal.mostrarAlerta("La demanda ha de ser un número vàlid.");
            return;
        }
        controladorPrincipal.tancarFinestra(event);
        controladorPrincipal.cabalMinim(nom, percent);
    }

    /**
     * @param event Event d'acció
     * @brief Cancel·la l'operació de càlcul del cabal mínim
     * @pre Cert
     * @post La finestra s'ha tancat sense realitzar cap acció
     */
    @FXML
    void cancelarOperacio(ActionEvent event) {
        controladorPrincipal.tancarFinestra(event);
    }
}
