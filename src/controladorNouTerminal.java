/**
 * @file controladorNouTerminal.java
 * @brief Classe ControladorNouTerminal que controla la finestra nouTerminal.fxml i gestiona l'afegiment de nous terminals a la xarxa.
 * @author Meritxell Masdevall i Esparch
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class controladorNouTerminal {

    private controladorPrincipal controladorPrincipal; ///< Instància del controlador principal

    @FXML
    private TextField coordenadesT; ///< Camp de text per a les coordenades

    @FXML
    private TextField nomTerminal; ///< Camp de text per al nom del terminal

    @FXML
    private TextField demandaT; ///< Camp de text per a la demanda del terminal

    /**
     * @param controlador El controlador principal
     * @brief Estableix el controlador principal
     * @pre El paràmetre "controlador" ha de ser una instància vàlida de la classe "controladorPrincipal".
     * @post S'assigna el controlador rebut com a paràmetre com a controlador pare d'aquest objecte.
     */
    public void setParentController(controladorPrincipal controlador) {
        controladorPrincipal = controlador;
    }

    /**
     * @param event Event d'acció
     * @brief Gestiona l'acció d'afegir un nou terminal
     * @pre Els camps de text nomTerminal, coordenadesT i demandaT contenen les dades corresponents
     * @post Si les dades introduïdes són vàlides i no existeix cap altre terminal amb el mateix nom, es crea un nou terminal i s'afegeix a la xarxa
     */
    @FXML
    void afegirTerminal(ActionEvent event) {
        ArrayList<Aixeta> aixetes = controladorPrincipal.obtenirAixetes();
        String nom = nomTerminal.getText();
        String coordenades = coordenadesT.getText();
        String demanda = demandaT.getText();
        if (nom.isEmpty() || coordenades.isEmpty() || demanda.isEmpty()) {
            controladorPrincipal.mostrarAlerta("Falten dades: tots els camps són obligatoris.");
            return;
        }
        for(Aixeta a : aixetes){
            if (a.nom().equals(nom)) {
                controladorPrincipal.mostrarAlerta("Ja existeix una aixeta amb aquest nom.\nEscull-ne un altre");
                return;
            }
        }

        try {
            controladorPrincipal.tancarFinestra(event);
            Coordenades cord = Main.stringToCoordenades(coordenades);
            float d = Float.parseFloat(demanda);
            Terminal t = new Terminal(nom, cord, d);
            controladorPrincipal.afegirT(t);
        }catch (NumberFormatException e) {
            controladorPrincipal.mostrarAlerta("Format de coordenades incorrecte.");
        } catch (IllegalArgumentException e) {
            controladorPrincipal.mostrarAlerta("Format de coordenades incorrecte.");
        } catch (Exception e) {
            controladorPrincipal.mostrarAlerta("S'ha produït un error inesperat.");
        }
    }

    /**
     * @param event Event d'acció
     * @brief Cancel·la l'operació d'afegir un nou terminal
     * @pre Cert
     * @post La finestra s'ha tancat sense realitzar cap acció
     */
    @FXML
    void cancelarOperacio(ActionEvent event) {
        controladorPrincipal.tancarFinestra(event);
    }
}
