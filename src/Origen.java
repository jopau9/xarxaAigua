/**
 * @file principal.Origen.java
 * @brief Classe principal.Origen que conté les dades i operacions dels orígens de la xarxa d'aigua i les seves connexions amb les canonades de la xarxa
 * @author Julia Baye Soler
 */
public class Origen extends Aixeta {
    /**
     * Cabal màxim de l'aixeta.
     */
    private float cabalMaxim;

    /**
     * @brief Constructor de la classe principal.Origen amb nom i coordenades.
     * @param n El nom de l'origen.
     * @param c Les coordenades de l'origen.
     * @pre El nom no és nul.
     * @post Crea un objecte principal.Origen amb el nom i les coordenades especificats, amb un cabal màxim inicial de 0.
     */
    public Origen(String n, Coordenades c) {
        this.nom(n);
        this.coordenades = c;
        this.cabalMaxim = 0;
    }

    /**
     * @brief Constructor per crear un objecte principal.Origen amb el nom i el cabal màxim especificats.
     *
     * @param n El nom de l'origen.
     * @param cabal El cabal màxim de l'origen.
     */
    public Origen(String n, float cabal) {
        this.nom(n);
        this.cabalMaxim = cabal;
    }

    /**
     * @brief Estableix el cabal màxim de l'origen amb el valor especificat.
     * @param c El cabal màxim de l'origen.
     * @pre c >= 0
     * @post Estableix el cabal màxim de l'origen amb el valor especificat.
     */
    public void determinarCabal(float c) {
        this.cabalMaxim = c;
    }

    /**
     * @brief Retorna el cabal màxim de l'origen.
     *
     * @return El cabal màxim de l'origen.
     */
    public float cabal() {
        return cabalMaxim;
    }
}