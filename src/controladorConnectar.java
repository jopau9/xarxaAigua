/**
 * @file controladorConnectar.java
 * @brief Classe ControladorConnectar que controla la finestra connectar.fxml i gestiona la connexió entre aixetes amb una capacitat determinada.
 * @author Meritxell Masdevall i Esparch
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class controladorConnectar {
    private controladorPrincipal controladorPrincipal; ///< Referència al controlador principal

    @FXML
    private ChoiceBox<String> origenT; ///< Caixa de selecció per a l'origen de la connexió

    @FXML
    private ChoiceBox<String> destiT; ///< Caixa de selecció per al destí de la connexió

    @FXML
    private TextField capacitatC; ///< Camp de text per a la capacitat de la connexió

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
     * @post S'han afegit les aixetes disponibles a les caixes de selecció d'origen i destí
     */
    void inicialitzar() {
        ArrayList<Aixeta> aixetes = controladorPrincipal.obtenirAixetes();
        if (aixetes != null) {
            for (Aixeta aixeta : aixetes) {
                destiT.getItems().add(aixeta.nom());
                origenT.getItems().add(aixeta.nom());
            }
        }
    }

    /**
     * @param event Event d'acció
     * @brief Gestiona l'acció d'afegir una connexió entre aixetes amb una capacitat determinada
     * @pre L'usuari ha seleccionat un origen, un destí i ha introduït la capacitat de la connexió
     * @post Si s'han introduït totes les dades correctament, es tanca la finestra i es crea la connexió entre les aixetes amb la capacitat especificada
     */
    @FXML
    void afegirConnexio(ActionEvent event) {
        String origen = origenT.getValue();
        String desti = destiT.getValue();
        String cap = capacitatC.getText();
        if (origen == null || desti == null || cap.isEmpty()) {
            controladorPrincipal.mostrarAlerta("Falten dades: tots els camps són obligatoris.");
            return;
        }
        float capacitat;
        try {
            capacitat = Float.parseFloat(cap);
            if (capacitat <= 0) {
                controladorPrincipal.mostrarAlerta("La capacitat no pot ser negativa o zero.");
                return;
            }
        } catch (NumberFormatException e) {
            controladorPrincipal.mostrarAlerta("La capacitat ha de ser un número vàlid.");
            return;
        }

        if (origen.equals(desti)) {
            controladorPrincipal.mostrarAlerta("L'origen i el destí han de ser diferents.");
            return;
        }
        controladorPrincipal.tancarFinestra(event);
        controladorPrincipal.connectar(origen, desti, capacitat);
    }

    /**
     * @param event Event d'acció
     * @brief Cancel·la l'operació d'afegir connexió
     * @pre Cert
     * @post La finestra s'ha tancat sense realitzar cap acció
     */
    @FXML
    void cancelarOperacio(ActionEvent event) {
        controladorPrincipal.tancarFinestra(event);
    }
}
