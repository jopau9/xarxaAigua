/**
 * @file controladorAbonar.java
 * @brief Classe ControldaorAbonar que controla la finestra abonar.fxml i gestiona l'abonament dels clients.
 * @author Meritxell Masdevall i Esparch
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class controladorAbonar {
    private controladorPrincipal controladorPrincipal; ///< Referència al controlador principal

    @FXML
    private ChoiceBox<String> terminalsT; ///< Caixa de selecció per als terminals

    @FXML
    private TextField dniT; ///< Camp de text per al DNI

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
     * @post S'han afegit els terminals disponibles a la caixa de selecció
     */
    void inicialitzar() {
        ArrayList<Aixeta> aixetes = controladorPrincipal.obtenirAixetes();
        if (aixetes != null) {
            for (Aixeta aixeta : aixetes) {
                if (aixeta instanceof Terminal){
                    terminalsT.getItems().add(aixeta.nom());
                }
            }
        }
    }

    /**
     * @param event Event d'acció
     * @brief Gestiona l'acció de l'abonament d'un client
     * @pre El camp de text dniT conté un DNI vàlid
     * @post Si el DNI no és buit i s'ha seleccionat un terminal, s'ha abonat el client i la finestra s'ha tancat
     */
    @FXML
    void abonarClient(ActionEvent event) {
        String dni = dniT.getText();
        String t = terminalsT.getValue();
        if (dni.isEmpty() || t == null) {
            controladorPrincipal.mostrarAlerta("Falten dades: tots els camps són obligatoris.");
            return;
        }
        controladorPrincipal.tancarFinestra(event);
        controladorPrincipal.abonarC(dni,t);
    }

    /**
     * @param event Event d'acció
     * @brief Cancel·la l'operació d'abonament
     * @pre Cert
     * @post La finestra s'ha tancat sense realitzar cap acció
     */
    @FXML
    void cancelarOperacio(ActionEvent event) {
        controladorPrincipal.tancarFinestra(event);
    }
}
