/**
 * @file principal.Coordenades.java
 * @brief Classe principal.Coordenades que conté les dades i operacions de les coordenades geogràfiques i les seves connexions amb les aixetes de la xarxa d'aigua
 * @author Julia Baye Soler
 */

public class Coordenades {
    /**
     * Latitud de l'aixeta.
     */
    private float latitud;

    /**
     * Longitud de l'aixeta.
     */
    private float longitud;

    /**
     * @brief Constructor de la classe Coordenades amb graus, minuts, segons i hemisferis per a la latitud i la longitud.
     * @param grausLat Els graus de la latitud.
     * @param minutsLat Els minuts de la latitud.
     * @param segonsLat Els segons de la latitud.
     * @param hemisferiLat L'hemisferi ('N' o 'S') de la latitud.
     * @param grausLong Els graus de la longitud.
     * @param minutsLong Els minuts de la longitud.
     * @param segonsLong Els segons de la longitud.
     * @param hemisferiLong L'hemisferi ('E' o 'O') de la longitud.
     * @pre Cap.
     * @post Crea una instància de principal. Coordenades amb els valors especificats.
     */
    public Coordenades(int grausLat, int minutsLat, float segonsLat, char hemisferiLat, int grausLong, int minutsLong, float segonsLong, char hemisferiLong) {
        this.latitud = (float)grausLat + (float)minutsLat / 60.0F + segonsLat / 3600.0F;
        if (hemisferiLat == 'S' || hemisferiLat == 's') {
            this.latitud *= -1.0F;
        }
        this.longitud = (float)grausLong + (float)minutsLong / 60.0F + segonsLong / 3600.0F;
        if (hemisferiLong == 'O' || hemisferiLong == 'o') {
            this.longitud *= -1.0F;
        }
    }

    /**
     * @param altresCoordenades Les coordenades originals de les quals es crearà la còpia.
     * @pre Les coordenades originals no són nul·les.
     * @post Es crea una nova instància de Coordenades amb els mateixos valors que les coordenades originals.
     * @brief Constructor de còpia de Coordenades.
     */
    public Coordenades(Coordenades altresCoordenades) {
        this.latitud = altresCoordenades.latitud;
        this.longitud = altresCoordenades.longitud;
    }

    /**
     * @brief Constructor de la classe Coordenades amb latitud i longitud.
     * @param latitud La latitud.
     * @param longitud La longitud.
     * @pre Cap.
     * @post Crea una instància de Coordenades amb els valors especificats.
     */
    public Coordenades(float latitud, float longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    /**
     * @brief Calcula la distància entre dues coordenades utilitzant la fórmula de Haversine.
     * @param altraCoordenada Les coordenades de l'altra ubicació.
     * @return La distància entre les dues coordenades en quilòmetres.
     * @pre Cap.
     * @post Retorna la distància entre les coordenades actuals i les coordenades especificades.
     */
    public double distancia(Coordenades altraCoordenada) {
        double radiTerra = 6371.0;
        double dLat = Math.toRadians((double)(altraCoordenada.latitud - this.latitud));
        double dLon = Math.toRadians((double)(altraCoordenada.longitud - this.longitud));
        double a = Math.sin(dLat / 2.0) * Math.sin(dLat / 2.0) + Math.cos(Math.toRadians((double)this.latitud)) * Math.cos(Math.toRadians((double)altraCoordenada.latitud)) * Math.sin(dLon / 2.0) * Math.sin(dLon / 2.0);
        double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));
        double distancia = radiTerra * c;
        return distancia;
    }

    /**
     * @return Una cadena de text que representa les coordenades en format (latitud, longitud).
     * @pre Cap
     * @brief Retorna una representació en cadena de text de les coordenades.
     */
    @Override
    public String toString() {
        return "(" + latitud + ", " + longitud + ")";
    }

    /**
     * @return La coordenada X (latitud) de les coordenades.
     * @pre Cap
     * @post Cap
     * @brief Obté la coordenada X (latitud) de les coordenades.
     */
    public double X() {
        return this.latitud;
    }

    /**
     * @return La coordenada Y (longitud) de les coordenades.
     * @pre Cap
     * @brief Obté la coordenada Y (longitud) de les coordenades.
     */
    public double Y() {
        return this.longitud;
    }
}
