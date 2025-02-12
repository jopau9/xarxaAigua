/**
 * @file controladorSituacio.java
 * @brief Classe controladorSituacio que controla la finestra situacio.fxml i gestiona la configuració de la situació de les aixetes.
 *  Permet seleccionar terminals i establir si l'aigua arriba o no a aquests i proporciona una interfície per definir la situació de les aixetes.
 * @author Meritxell Masdevall i Esparch
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;

import java.util.ArrayList;
import java.util.Map;

public class controladorSituacio {

    private controladorPrincipal controladorPrincipal; ///< Referència al controlador principal

    @FXML
    private ChoiceBox<String> terminalT; ///< ChoiceBox per seleccionar els terminals

    @FXML
    private RadioButton no; ///< RadioButton per indicar que l'aigua no arriba al terminal

    @FXML
    private RadioButton si; ///< RadioButton per indicar que l'aigua arriba al terminal

    Map<String, Boolean> aiguaArriba; ///< Mapa que emmagatzema la configuració de si l'aigua arriba o no als terminals


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
                if(aixeta instanceof Terminal) {
                    terminalT.getItems().add(aixeta.nom());
                }
            }
        }
    }

    /**
     * @brief Afegeix un terminal amb la configuració de l'aigua.
     * @pre terminalT.getValue() != null
     * @pre no.isSelected() != si.isSelected()
     * @post S'afegeix un terminal amb la configuració de l'aigua.
     */
    @FXML
    void afegirTerminal(){
        String t = terminalT.getValue();
        if(no.isSelected() && si.isSelected() || !no.isSelected() && !si.isSelected()){
            controladorPrincipal.mostrarAlerta("Cal seleccionar SI o NO.");
        }
        aiguaArriba.put(t,!no.isSelected());
    }

    /**
     * @brief Estableix la situació de les aixetes i tanca la finestra.
     * @param event L'esdeveniment que ha provocat la crida.
     * @pre aiguaArriba != null
     * @post La situació de les aixetes s'estableix i la finestra es tanca.
     */
    @FXML
    void establirSituacio(ActionEvent event) {
        controladorPrincipal.tancarFinestra(event);
        controladorPrincipal.establirSituacio(aiguaArriba);
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