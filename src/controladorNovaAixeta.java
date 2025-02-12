/**
 * @file controladorNovaAixeta.java
 * @brief Classe ControladorNovaAixeta que controla la finestra novaAixeta.fxml i gestiona l'afegiment de noves aixetes a la xarxa.
 * @author Meritxell Masdevall i Esparch
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class controladorNovaAixeta {

    private controladorPrincipal controladorPrincipal; ///< Referència al controlador principal

    @FXML
    private TextField coordenadesT; ///< Camp de text per a les coordenades

    @FXML
    private TextField nomAixeta; ///< Camp de text per al nom de l'aixeta

    @FXML
    private TextField cabalT; ///< Camp de text per al cabal de l'aixeta

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
     * @brief Gestiona l'acció d'afegir un nou origen
     * @pre El camp de text nomAixeta i coordenadesT contenen les dades corresponents
     * @post Si les dades introduïdes són vàlides i no existeix cap altre aixeta amb el mateix nom, es crea un nou origen i s'afegeix a la xarxa
     */
    @FXML
    void afegirOrigen(ActionEvent event) {
        ArrayList<Aixeta> aixetes = controladorPrincipal.obtenirAixetes();
        String nom = nomAixeta.getText();
        String coordenades = coordenadesT.getText();
        String c = cabalT.getText();
        if (nom.isEmpty() || coordenades.isEmpty()) {
            controladorPrincipal.mostrarAlerta("Falten dades: cal omplir els camps obligatoris.");
            return;
        }
        for(Aixeta a : aixetes){
            if (a.nom().equals(nom)) {
                controladorPrincipal.mostrarAlerta("Ja existeix una aixeta amb aquest nom.\nEscull-ne un altre.");
                return;
            }
        }
        float cabal;
        if (c.isEmpty()){
            cabal = 0.0f;
        }
        else try {
            cabal = Float.parseFloat(c);
            if (cabal < 0) {
                controladorPrincipal.mostrarAlerta("El cabal no pot ser negatiu.");
                return;
            }
        } catch (NumberFormatException e) {
            controladorPrincipal.mostrarAlerta("El cabal ha de ser un número vàlid.");
            return;
        }
        try {
            Coordenades cord = Main.stringToCoordenades(coordenades);
            Origen o = new Origen(nom, cord);
            o.determinarCabal(cabal);
            controladorPrincipal.tancarFinestra(event);
            controladorPrincipal.afegirO(o);
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
     * @brief Cancel·la l'operació d'afegir un nou origen
     * @pre Cert
     * @post La finestra s'ha tancat sense realitzar cap acció
     */
    @FXML
    void cancelarOperacio(ActionEvent event) {
        controladorPrincipal.tancarFinestra(event);
    }
}
