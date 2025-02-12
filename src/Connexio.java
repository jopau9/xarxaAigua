/**
 * @file principal.Connexio.java
 * @brief Classe principal.Connexio que conté les dades i operacions de les connexions de la xarxa d'aigua i les seves connexions amb les canonades de la xarxa
 * @author Julia Baye Soler
 */

import java.util.ArrayList;

public class Connexio extends Aixeta {

    /**
     * @brief Constructor de la classe principal.Connexio amb nom i coordenades.
     * @param n El nom de la connexió.
     * @param c Les coordenades de la connexió.
     * @pre El nom no és nul.
     * @post Crea un objecte principal.Connexio amb el nom i les coordenades especificats, sense cap canonada associada.
     */
    public Connexio(String n, Coordenades c) {
        this.nom(n);
        this.coordenades = c;
    }

    /**
     * @brief Constructor de la classe principal.Connexio amb nom, coordenades i llista de canonades.
     * @param n El nom de la connexió.
     * @param _coordenades Les coordenades de la connexió.
     * @param _canonades La llista de canonades associades a la connexió.
     * @pre El nom, les coordenades i la llista de canonades no són nuls.
     * @post Crea un objecte principal.Connexio amb el nom, les coordenades i la llista de canonades especificats.
     */
    public Connexio(String n, Coordenades _coordenades, ArrayList<Canonada> _canonades) {
        this.nom(n);
        coordenades = _coordenades;
        canonades = _canonades;
    }
}