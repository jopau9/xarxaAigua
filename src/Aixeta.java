/**
     * @file Aixeta.java
     * @brief Classe Aixeta que conté les dades i operacions de les aixetes de la xarxa d'aigua i les seves connexions amb les canonades de la xarxa
     * @author Julia Baye Soler
     */


    import java.util.ArrayList;

    public class Aixeta {
        /**
         * Coordenades de l'aixeta.
         */
        protected Coordenades coordenades;

        /**
         * Nom de l'aixeta.
         */
        private String nom;

        /**
         * Estat actual de l'aixeta.
         */
        private boolean estat;

        /**
         * Llista de canonades connectades a l'aixeta.
         */
        protected ArrayList<Canonada> canonades;

        /**
         * Demanda d'aigua de l'aixeta.
         */
        protected float demanda;

        /**
         * Cabal ipotètic de l'aixeta.
         */
        protected float cabalIpotetic;

        /**
         * Indica si l'aixeta té un cabal definit.
         */
        private boolean teCabal;

        /**
         * @brief Constructor per defecte de la classe Aixeta.
         * @pre Cap.
         * @post Crea una instància de la classe Aixeta amb els valors per defecte.
         */
        public Aixeta() {
            canonades = new ArrayList<>();
            demanda = 0;
            teCabal = false;
            estat = true;
        }
        /**
         * @brief Constructor de copia de la classe Aixeta.
         * @param aixeta L'aixeta de la qual es copiaran les dades.
         * @pre L'aixeta no és nul.
         * @post Es crea una nova instància de Aixeta amb les mateixes propietats que l'aixeta original.
         *
         */
        public Aixeta(Aixeta aixeta) {
            this.coordenades = new Coordenades(aixeta.coordenades); // Suposant que Coordenades també té un constructor de còpia
            this.nom = aixeta.nom;
            this.estat = aixeta.estat;
            this.demanda = aixeta.demanda;
            this.cabalIpotetic = aixeta.cabalIpotetic;
            this.teCabal = aixeta.teCabal;
            canonades = new ArrayList<>();
        }

        /**
         * @param n El nom de l'aixeta.
         * @brief Estableix el nom de l'aixeta amb el valor proporcionat.
         * @pre El nom no és nul.
         * @post Estableix el nom de l'aixeta amb el valor proporcionat.
         */
        public void nom(String n) {
            this.nom = n;
        }
        /**
         * @return El nom de l'aixeta.
         * @brief Retorna el nom de l'aixeta.
         * @pre Cap.
         * @post Retorna el nom de l'aixeta.
         */
        public String nom() {
            return this.nom;
        }

        /**
         * @param c La canonada a afegir.
         * @brief Afegeix la canonada especificada a la llista de canonades de l'aixeta.
         * @pre c no és nul.
         * @post Afegeix la canonada especificada a la llista de canonades de l'aixeta.
         */
        public void afegirCanonada(Canonada c) {
            if (this.canonades.contains(c)) {
                throw new IllegalArgumentException("La canonada ja existeix.");
            } else {
                this.canonades.add(c);
            }
        }

        /**
         * @return La llista de canonades de l'aixeta.
         * @brief Retorna la llista de canonades de l'aixeta.
         * @pre Cap.
         * @post Retorna la llista de canonades de l'aixeta.
         */
        public ArrayList<Canonada> canonades() {
            return new ArrayList<>(canonades);
        }

        /**
         * @return Cert si l'aixeta està oberta, fals si està tancada.
         * @brief Retorna l'estat de l'aixeta.
         * @pre Cap.
         * @post Retorna l'estat de l'aixeta.
         */
        public boolean estat() {
            return estat;
        }

        /**
         * @return Les coordenades de l'aixeta.
         * @brief Retorna les coordenades de l'aixeta.
         * @pre Cap.
         * @post Retorna les coordenades de l'aixeta.
         */
        public Coordenades coordenades() {
            return coordenades;
        }

        /**
         * @brief Estableix l'estat de l'aixeta com a tancada.
         * @pre Cap.
         * @post Estableix l'estat de l'aixeta com a tancada.
         */
        public void tancarAixeta() {
            this.estat = false;
        }

        /**
         * @brief Estableix l'estat de l'aixeta com a oberta.
         * @pre Cap.
         * @post Estableix l'estat de l'aixeta com a oberta.
         */
        public void obrirAixeta() {
            this.estat = true;
        }

        /**
         * @param c La canonada a eliminar.
         * @brief Elimina una canonada de la llista de canonades de l'aixeta.
         * @pre Cap.
         * @post S'ha eliminat la canonada especificada de la llista de canonades de l'aixeta.
         */
        public void eliminarCanonada(Canonada c) {
            if (this.canonades.contains(c)) {
                this.canonades.remove(c);
            } else {
                throw new IllegalArgumentException("La canonada no existeix.");
            }
        }

        @Override
        public String toString() {
            String s = "";
            s += "Nom: " + nom;
            s += " Coordenades:" + coordenades;
            System.out.println("");


            s += " canonades: " + canonades;
            return s;

        }

        /**
         * @param cabal Booleà que indica si l'aixeta té cabal o no.
         * @pre Cap
         * @post El valor de 'teCabal' s'estableix al valor de 'cabal'.
         * @brief Estableix si l'aixeta té cabal o no.
         */
        public void teCabal(boolean cabal) {
            teCabal = cabal;
        }

        /**
         * @return Cert si l'aixeta té cabal, fals si no.
         * @pre Cap
         * @post Cap
         * @brief Retorna si l'aixeta té cabal o no.
         */
        public boolean teCabal() {
            return teCabal;
        }

        /**
         * @param _demanda La demanda de l'aixeta.
         * @pre Cap
         * @post La demanda de l'aixeta queda establerta amb el valor especificat.
         * @brief Estableix la demanda de l'aixeta.
         */
        public void establirDemanda(float _demanda) {
            this.demanda = _demanda;
        }

        /**
         * @param _cabal El cabal hipotètic de l'aixeta.
         * @pre Cap
         * @post El cabal hipotètic de l'aixeta queda establert amb el valor especificat.
         * @brief Estableix el cabal hipotètic de l'aixeta.
         */
        public void establirCabalHipotetic(float _cabal) {
            cabalIpotetic = _cabal;
        }

        /**
         * @return La demanda de l'aixeta.
         * @pre Cap
         * @post Cap
         * @brief Obté la demanda de l'aixeta.
         */
        public float obtenirDemanda() {
            return demanda;
        }

        /**
         * @return El cabal hipotètic de l'aixeta.
         * @pre Cap
         * @post Cap
         * @brief Obté el cabal hipotètic de l'aixeta.
         */
        public float obtenirCabalHipotetic() {
            return cabalIpotetic;
        }

        /**
         * @return La llista de canonades associades a l'aixeta.
         * @pre Cap
         * @post Cap
         * @brief Obté la llista de canonades associades a l'aixeta.
         */
        public ArrayList<Canonada> obtenirCanonades(){
            return this.canonades;
        }

        /**
         * @return El codi hash de l'aixeta.
         * @pre Cap
         * @post Cap
         * @brief Retorna el codi hash de l'aixeta.
         */
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((nom == null) ? 0 : nom.hashCode());
            return result;
        }

        /**
         * @param obj L'objecte amb el qual es compararà l'aixeta.
         * @return Cert si l'objecte és igual a l'aixeta, fals altrament.
         * @pre Cap
         * @post Cap
         * @brief Comprova si una aixeta és igual a un altre objecte.
         */
        @Override
        public boolean equals(Object obj) {
            // Comprovar si els dos objectes són el mateix objecte
            if (this == obj) {
                return true;
            }
            // Comprovar si l'objecte passat és null
            if (obj == null) {
                return false;
            }
            // Comprovar si els dos objectes són de la mateixa classe
            if (getClass() != obj.getClass()) {
                return false;
            }
            // Convertir l'objecte passat a una instància de Aixeta
            Aixeta other = (Aixeta) obj;
            // Comparar els noms de les aixetes
            if (nom == null) {
                if (other.nom != null) {
                    return false;
                }
            } else if (!nom.equals(other.nom)) {
                return false;
            }
            return true;
        }

    }
