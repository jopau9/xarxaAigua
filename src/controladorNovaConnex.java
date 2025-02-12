/**
 * @file controladorNovaConnex.java
 * @brief Classe ControladorNovaConnex que controla la finestra novaConnex.fxml i gestiona l'afegiment de noves connexions a la xarxa.
 * @author Meritxell Masdevall i Esparch
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class controladorNovaConnex {

    private controladorPrincipal controladorPrincipal; ///< Referència al controlador principal

    @FXML
    private TextField coordenadesT; ///< Camp de text per a les coordenades

    @FXML
    private TextField nomConnexio; ///< Camp de text per al nom de la connexió

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
     * @brief Gestiona l'acció d'afegir una nova connexió
     * @pre El camp de text nomConnexio i coordenadesT contenen les dades corresponents
     * @post Si les dades introduïdes són vàlides i no existeix cap altra aixeta amb el mateix nom, es crea una nova connexió i s'afegeix a la xarxa
     */
    @FXML
    void afegirConnex(ActionEvent event) {
        ArrayList<Aixeta> aixetes = controladorPrincipal.obtenirAixetes();
        String nom = nomConnexio.getText();
        String coordenades = coordenadesT.getText();
        if (nom.isEmpty() || coordenades.isEmpty()) {
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
            Coordenades cord = Main.stringToCoordenades(coordenades);
            Connexio c = new Connexio(nom, cord);
            controladorPrincipal.afegirC(c);
            controladorPrincipal.tancarFinestra(event);
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
     * @brief Cancel·la l'operació d'afegir una nova connexió
     * @pre Cert
     * @post La finestra s'ha tancat sense realitzar cap acció
     */
    @FXML
    void cancelarOperacio(ActionEvent event) {
        controladorPrincipal.tancarFinestra(event);
    }
}
