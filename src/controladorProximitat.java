/**
 * @file controladorProximitat.java
 * @brief Classe controladorProximitat que controla la finestra proximitat.fxml i gestiona el càlcul de proximitat entre aixetes i coordenades geogràfiques.
 * @author Meritxell Masdevall i Esparch
 */

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;

public class controladorProximitat {

    private controladorPrincipal controladorPrincipal; ///< Referència al controlador principal

    @FXML
    private ListView<String> aixetesT; ///< Llista de les aixetes disponibles per seleccionar

    @FXML
    private TextField coordenadesT; ///< Caixa de text on es pot introduir les coordenades geogràfiques per calcular la proximitat.

    /**
     * @brief Estableix el controlador principal.
     * @param controller El controlador principal.
     * @pre controller != null
     * @post S'estableix el controlador principal i s'inicialitza la finestra.
     */
    public void setParentController(controladorPrincipal controller) {
        this.controladorPrincipal = controller;
        inicialitzar();
    }

    /**
     * @brief Inicialitza la finestra amb les aixetes disponibles.
     * @pre Cert
     * @post Es carreguen les aixetes disponibles i es permet seleccionar-ne més d'una.
     */
    void inicialitzar() {
        ArrayList<Aixeta> aixetes = controladorPrincipal.obtenirAixetes();
        if (aixetes != null) {
            for (Aixeta aixeta : aixetes) {
                aixetesT.getItems().add(aixeta.nom());
            }
        }
        aixetesT.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);
    }

    /**
     * @brief Gestiona l'acció de calcular la proximitat.
     * @param event L'esdeveniment que va causar l'acció.
     * @pre cap
     * @post Es comprova que s'hagin seleccionat aixetes i que s'hagi especificat una coordenada, i es calcula la proximitat entre les aixetes seleccionades i les coordenades. Si falta algun camp, es mostra una alerta.
     */
    @FXML
    void proximitat(ActionEvent event) {
        ObservableList<String> seleccions = aixetesT.getSelectionModel().getSelectedItems();
        String coordenades = coordenadesT.getText();
        if (seleccions.isEmpty() || coordenades.isEmpty()) {
            controladorPrincipal.mostrarAlerta("Falten dades: tots els camps són obligatoris.\nCal seleccionar almenys una aixeta per continuar");
            return;
        }

        try {
            Coordenades cord = Main.stringToCoordenades(coordenades);
            controladorPrincipal.tancarFinestra(event);
            controladorPrincipal.proximitatA(cord,new ArrayList<>(seleccions));
        }catch (NumberFormatException e) {
            controladorPrincipal.mostrarAlerta("Format de coordenades incorrecte.");
        } catch (IllegalArgumentException e) {
            controladorPrincipal.mostrarAlerta("Format de coordenades incorrecte.");
        } catch (Exception e) {
            controladorPrincipal.mostrarAlerta("S'ha produït un error inesperat.");
        }
    }

    /**
     * @brief Cancel·la l'operació i tanca la finestra.
     * @param event L'esdeveniment que va causar la cancel·lació.
     * @pre cap
     * @post Es tanca la finestra actual.
     */
    @FXML
    void cancelarOperacio(ActionEvent event) {
        controladorPrincipal.tancarFinestra(event);
    }
}
