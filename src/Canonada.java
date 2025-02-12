/**
 * @author Julia Baye Soler
 * @file principal.Canonada.java
 * @brief Classe principal.Canonada que conté les dades i operacions de les canonades de la xarxa d'aigua i les seves connexions amb les aixetes de la xarxa.
 */
public class Canonada {
    /**
     * Aixeta d'origen de la connexió.
     */
    private Aixeta origen;

    /**
     * Aixeta de destí de la connexió.
     */
    private Aixeta desti;

    /**
     * Demanda d'aigua de la connexió.
     */
    private float demanda;

    /**
     * Flux màxim permès per la connexió.
     */
    private float fluxMaxim;

    /**
     * Flux actual de la connexió.
     */
    private float fluxActual;

    /**
     * @param origen L'aixeta d'origen de la canonada.
     * @param desti  L'aixeta de destí de la canonada.
     * @param pes    La capacitat (pes) de la canonada.
     * @brief Constructor de la classe principal.Canonada amb origen, destí i capacitat.
     * @pre L'origen i el destí de la canonada no són nuls.
     * @post Crea una instància de principal.Canonada amb l'origen, el destí i la capacitat especificats.
     */
    public Canonada(Aixeta origen, Aixeta desti, float pes) {
        this.origen = origen;
        this.desti = desti;
        this.fluxMaxim = pes;
        this.fluxActual = 0; // Inicialització a zero del flux actual
    }

    /**
     * @param canonadaOriginal La Canonada original de la qual es crearà la còpia.
     * @pre La Canonada original no és nul·la.
     * @post Es crea una nova instància de Canonada amb les mateixes propietats que la Canonada original.
     * @brief Constructor de còpia de Canonada.
     */
    public Canonada(Canonada canonadaOriginal) {
        this.origen = new Aixeta(canonadaOriginal.origen); // Suposant que Aixeta té un constructor de còpia
        this.desti = new Aixeta(canonadaOriginal.desti); // Suposant que Aixeta té un constructor de còpia
        this.demanda = canonadaOriginal.demanda;
        this.fluxMaxim = canonadaOriginal.fluxMaxim;
        this.fluxActual = canonadaOriginal.fluxActual;
    }

    /**
     * @param increment Quantitat a incrementar en el flux actual de la canonada.
     * @brief Incrementa el flux actual de la canonada.
     * @pre increment podria ser positiu o negatiu però no ha de fer que fluxActual sigui menor que 0 o més gran que fluxMaxim.
     * @post El flux actual de la canonada s'incrementa sense excedir els límits establerts.
     */
    public void incrementarFlux(float increment) {
        float nouFlux = this.fluxActual + increment;
        if (nouFlux < 0) {
            this.fluxActual = 0; // Asegura que el flux no sigui negatiu
        } else if (nouFlux > this.fluxMaxim) {
            this.fluxActual = this.fluxMaxim; // Asegura que el flux no excedeixi la capacitat màxima
        } else {
            this.fluxActual = nouFlux; // Ajusta el flux actual al valor correcte
        }
    }

    /**
     * @return L'aixeta destí d'aquesta canonada.
     * @brief Obté l'aixeta destí d'aquesta canonada.
     */
    public Aixeta desti() {
        return this.desti;
    }

    /**
     * @return L'aixeta origen d'aquesta canonada.
     * @brief Obté l'aixeta origen d'aquesta canonada.
     */
    public Aixeta origen() {
        return this.origen;
    }

    /**
     * @return La capacitat màxima de la canonada.
     * @brief Obté la capacitat màxima de la canonada.
     */
    public float capacitat() {
        return this.fluxMaxim;
    }

    /**
     * @return El flux actual de la canonada.
     * @brief Obté el flux actual que passa per la canonada.
     */
    public float fluxActual() {
        return this.fluxActual;
    }

    /**
     * @return Una cadena de text que representa l'objecte actual.
     * @pre Cap
     * @post Cap
     * @brief Retorna una cadena que representa l'objecte actual en format llegible.
     */
    @Override
    public String toString() {
        String s = ""; // String per emmagatzemar la representació en format de cadena
        s += "Origen: " + origen.nom(); // Afegir el nom de l'origen a la cadena
        s += " Desti: " + desti.nom(); // Afegir el nom del destí a la cadena
        s += " Capacitat: " + fluxMaxim; // Afegir la capacitat a la cadena

        return s; // Retornar la cadena que representa l'objecte actual
    }
}