/**
 * @file controladorDemandaT.java
 * @brief Classe ControladorDemandaT que controla la finestra demandaT.fxml i gestiona l'establiment de la demanda per a un terminal.
 * @author Meritxell Masdevall i Esparch
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class controladorDemandaT {

    private controladorPrincipal controladorPrincipal; ///< Referència al controlador principal

    @FXML
    private ChoiceBox<String> terminalT; ///< Caixa de selecció per als terminals

    @FXML
    private TextField demandaT; ///< Camp de text per a la demanda

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
     * @post S'han afegit els terminals disponibles a la caixa de selecció
     */
    void inicialitzar() {
        ArrayList<Aixeta> aixetes = controladorPrincipal.obtenirAixetes();
        if (aixetes != null) {
            for (Aixeta aixeta : aixetes) {
                if(aixeta instanceof Terminal) {
                    terminalT.getItems().add(aixeta.nom());
                }
            }
        }
    }

    /**
     * @param event Event d'acció
     * @brief Gestiona l'acció d'establir la demanda per a un terminal
     * @pre L'usuari ha seleccionat un terminal de la caixa de selecció i ha introduït la demanda
     * @post Si s'ha introduït el terminal i la demanda és vàlida, es tanca la finestra i s'estableix la demanda per al terminal seleccionat
     */
    @FXML
    void establir(ActionEvent event) {
        String nom = terminalT.getValue();
        String d = demandaT.getText();
        if (nom.isEmpty() || d.isEmpty()) {
            controladorPrincipal.mostrarAlerta("Falten dades: tots els camps són obligatoris.");
            return;
        }
        float demanda;
        try {
            demanda = Float.parseFloat(d);
            if (demanda < 0) {
                controladorPrincipal.mostrarAlerta("El cabal no pot ser negatiu.");
                return;
            }
        } catch (NumberFormatException e) {
            controladorPrincipal.mostrarAlerta("El cabal ha de ser un número vàlid.");
            return;
        }
        controladorPrincipal.tancarFinestra(event);
        controladorPrincipal.establirD(nom, demanda);
    }

    /**
     * @param event Event d'acció
     * @brief Cancel·la l'operació d'establir la demanda per a un terminal
     * @pre Cert
     * @post La finestra s'ha tancat sense realitzar cap acció
     */
    @FXML
    void cancelarOperacio(ActionEvent event) {
        controladorPrincipal.tancarFinestra(event);
    }
}
