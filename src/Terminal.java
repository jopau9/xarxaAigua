/**
 * @file principal.Terminal.java
 * @brief Classe principal.Terminal que conté les dades i operacions de les terminals de la xarxa d'aigua i les seves connexions amb les canonades de la xarxa
 * @author Julia Baye Soler
 */

import java.util.ArrayList;

public class Terminal extends Aixeta {

    /**
     * Llista d'abonats associats a l'aixeta.
     */
    private ArrayList<String> abonats = new ArrayList<>();

    /**
     * @brief Constructor de la classe principal.Terminal amb nom, coordenades i demanda de punta.
     * @param n El nom de la terminal.
     * @param c Les coordenades de la terminal.
     * @param _demanda La demanda de punta de la terminal.
     * @pre El nom, les coordenades i la demanda de punta no són nuls.
     * @post Crea un objecte principal Terminal amb el nom, les coordenades i la demanda de punta especificats.
     */
    public Terminal(String n, Coordenades c, float _demanda) {
        this.nom(n);
        this.coordenades = c;
        this.demanda = _demanda;
    }

    /**
     * @brief Constructor per crear un objecte principal.Terminal amb el nom especificat.
     *
     * @param n El nom del terminal.
     */
    public Terminal(String n) {
        this.nom(n);
    }

    /**
     * @brief Afegeix un abonat a la llista d'abonats.
     * @param dni El DNI de l'abonat a afegir.
     * @pre El DNI no és nul.
     * @post Si l'abonat identificat pel DNI no existeix, l'afegeix a la llista d'abonats.
     *       Altrament, llança una excepció.
     */
    public void afegirAbonat(String dni) {
        if (this.abonats.contains(dni)) {
            throw new IllegalArgumentException("L'abonat ja existeix.");
        } else {
            this.abonats.add(dni);
        }
    }
    /**
     * @brief Estableix la demanda de punta de la terminal.
     * @param d La demanda de punta de la terminal.
     * @pre d és un valor vàlid.
     * @post Estableix la demanda de punta de la terminal al valor especificat.
     */
    public void determinarDemanda(float d) {
        this.demanda = d;
    }

    /**
     * @return La llista d'abonats associats a l'aixeta.
     * @pre Cap
     * @post Cap
     * @brief Obté la llista d'abonats associats a l'aixeta.
     */
    public ArrayList<String> obtenirAbonats() {
        return abonats;
    }
}
