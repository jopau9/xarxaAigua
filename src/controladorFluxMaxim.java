/**
 * @file controladorFluxMaxim.java
 * @brief Classe ControladorFluxMaxim que controla la finestra fluxMaxim.fxml i gestiona el càlcul del flux màxim d'una aixeta.
 * @author Meritxell Masdevall i Esparch
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

import java.util.ArrayList;

public class controladorFluxMaxim {
    private controladorPrincipal controladorPrincipal; ///< Referència al controlador principal

    @FXML
    private ChoiceBox<String> aixeta; ///< Caixa de selecció per a les aixetes

    /**
     * @param controlador El controlador principal
     * @brief Estableix el controlador principal
     * @pre El paràmetre "controlador" ha de ser una instància vàlida de la classe "controladorPrincipal".
     * @post S'assigna el controlador rebut com a paràmetre com a controlador pare d'aquest objecte. A més, es crida al mètode "inicialitzar" per realitzar qualsevol configuració inicial necessària.
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
     * @brief Gestiona l'acció de calcular el flux màxim d'una aixeta
     * @pre L'usuari ha seleccionat una aixeta de la caixa de selecció
     * @post Si s'ha seleccionat una aixeta, es tanca la finestra i es demana al controlador principal que calculi el flux màxim de l'aixeta seleccionada
     */
    @FXML
    void maxFlow(ActionEvent event) {
        String a = aixeta.getValue();
        if (a == null) {
            controladorPrincipal.mostrarAlerta( "Falten dades: és obligatori escollir una aixeta.");
            return;
        }
        controladorPrincipal.tancarFinestra(event);
        controladorPrincipal.maxFlow(a);
    }

    /**
     * @param event Event d'acció
     * @brief Cancel·la l'operació de càlcul del flux màxim
     * @pre Cert
     * @post La finestra s'ha tancat sense realitzar cap acció
     */
    @FXML
    void cancelarOperacio(ActionEvent event) {
        controladorPrincipal.tancarFinestra(event);
    }
}
