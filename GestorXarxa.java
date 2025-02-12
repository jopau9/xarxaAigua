/**
 * @file principal.GestorXarxa.java
 * @brief Conté la implementació de la classe principal.GestorXarxa que gestiona la xarxa d'aigua i les seves operacions.
 * @author JoanPau Rahola Ferrer
 */

import java.util.Iterator;
import java.util.ArrayList;
import javafx.util.Pair;
import java.util.*;

public class GestorXarxa {
    public GestorXarxa() {
        //pre: --
        //post: inicialitza la xarxa d'aigua buida
        System.out.println("Branca Separada");

    }

    /**
     * Verifica si la xarxa té cicles.
     * @param x La xarxa d'aigua.
     * @param aixeta El nom de l'aixeta des de la qual començar la comprovació.
     * @param dirigida Indica si la xarxa és dirigida (true) o no (false).
     * @return Cert si la xarxa té cicles, fals altrament.
     * Pre: x és una xarxa d'aigua vàlida, aixeta és el nom d'una aixeta vàlida que pertany a x.
     * Post: Retorna cert si es troba almenys un cicle a la xarxa, començant des de l'aixeta especificada,
     *       utilitzant la direcció especificada per la variable dirigida; en cas contrari, retorna fals.
     */
    public boolean teCicles(XarxaAigua x, String aixeta, boolean dirigida) {
        Set<Aixeta> visitats = new HashSet<>();
        Set<Aixeta> enRecorregut = new HashSet<>();
        Aixeta a = x.aixeta(aixeta);

        for (Aixeta actual : x.clausSubxarxa(a)) {
            if (!visitats.contains(actual) && teCicleRecursiu(x, actual, null, visitats, enRecorregut, dirigida)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Comprova recursivament si la xarxa té cicles, partint d'una aixeta específica.
     * @param x La xarxa d'aigua.
     * @param aixeta L'aixeta des de la qual començar la comprovació.
     * @param pare L'aixeta pare de la aixeta actual.
     * @param visitats Conjunt d'aixetes visitades durant la comprovació.
     * @param enRecorregut Conjunt d'aixetes en el camí actual.
     * @param dirigida Indica si la xarxa és dirigida (true) o no (false).
     * @return Cert si la xarxa té un cicle que comença des de l'aixeta especificada, fals altrament.
     * Pre: x és una xarxa d'aigua vàlida, aixeta és una aixeta vàlida que pertany a x, dirigida és un valor booleà.
     * Post: Retorna cert si es troba almenys un cicle a la xarxa, començant des de l'aixeta especificada,
     *       utilitzant la direcció especificada per la variable dirigida; en cas contrari, retorna fals.
     */
    private boolean teCicleRecursiu(XarxaAigua x, Aixeta aixeta, Aixeta pare, Set<Aixeta> visitats, Set<Aixeta> enRecorregut, boolean dirigida) {
        visitats.add(aixeta);
        enRecorregut.add(aixeta);

        ArrayList<Aixeta> veins = dirigida ? x.veins(aixeta) : x.veinsNoDirigit(aixeta);

        for (Aixeta vei : veins) {
            if (vei.equals(pare)) {
                continue; // Evita tornar enrere pel mateix camí
            }
            if (enRecorregut.contains(vei)) {
                return true;
            }
            if (!visitats.contains(vei) && teCicleRecursiu(x, vei, aixeta, visitats, enRecorregut, dirigida)) {
                return true;
            }
        }
        enRecorregut.remove(aixeta);
        return false;
    }


    public boolean formaArbre(XarxaAigua x, String aixeta) {
        // Pre: --
        // Post: Diu si la xarxa forma un arbre a partir de l'aixeta especificada
        if (teCicles(x, aixeta, false)) {
            System.out.println("te cicles tractant-lo com no dirigit, no es arbre");
            return false;
        }

        Aixeta a = x.aixeta(aixeta);
        int nNodesFont = 0;
        boolean parar;
        for (Aixeta AixetaActual : x.clausSubxarxa(a)) {
            if (AixetaActual == null) {
                continue; // Evitar operacions quan AixetaActual és null
            }
            parar = false;
            Iterator<Canonada> iterator = AixetaActual.canonades().iterator();
            while (iterator.hasNext() && !parar) {
                Canonada canonadaActual = iterator.next();
                if (canonadaActual.desti() == AixetaActual) {
                    parar = true;
                }
            }
            if (!parar) {
                nNodesFont++;
            }
        }
        System.out.println(nNodesFont);
        return nNodesFont == 1;
    }

    public float cabalMinim(XarxaAigua x, Aixeta origen, float percent) {
        // Pre: origen és una aixeta vàlida que pertany a la xarxa x,
        //      percent és un valor major que zero.
        // Post: Retorna el cabal mínim necessari per garantir que cap node terminal de la xarxa,
        //       que sigui accessible des de l'origen especificat, rebi menys d'un determinat percentatge de la seva demanda.

        ArrayList<Aixeta> terminals = x.clausSubxarxa(origen); // Obté tots els nodes accessibles des de l'origen.
        float cabalMinim = 0;
        for (Aixeta element : terminals) {
            if (element instanceof Terminal && arribadaCabal(x, element)) {
                Terminal terminal = (Terminal) element;
                cabalMinim += terminal.getDemanda() * percent;
            }
        }
        return cabalMinim;
    }

    private boolean arribadaCabal(XarxaAigua x, Aixeta aixeta) {
        // Pre: --
        // Post: Diu si hi ha arribada de cabal a l'aixeta des de l'origen
        if (!aixeta.estat()) {
            return false; // Si l'aixeta està tancada, l'aigua no pot arribar
        }
        if (aixeta instanceof Origen) {
            return true; // Si l'aixeta és un origen i està oberta, l'aigua definitivament arriba
        }

        ArrayList<Aixeta> entrades = x.aixetesEntrada(aixeta);
        for (Aixeta entrada : entrades) {
            if (arribadaCabal(x, entrada)) {
                return true; // Si l'aigua arriba a aquesta entrada, llavors també arriba a l'aixeta actual
            }
        }
        return false; // Si cap de les entrades té arribada de cabal, llavors l'aigua no arriba a l'aixeta actual
    }


    public void proximitat(XarxaAigua x, Coordenades punt, ArrayList<String> nomsAixetes) {
        // Pre: --
        // Post: Ordena les aixetes de la xarxa segons la seva proximitat al punt de referència

        ArrayList<Pair<Aixeta, Double>> aixetesOrdenades = new ArrayList<>();
        // Calcula les distàncies i guarda les aixetes amb les seves distàncies

        if (!nomsAixetes.isEmpty()) {
            for (String nomAixeta : nomsAixetes) {

                Aixeta aixeta = x.aixeta(nomAixeta);
                double distancia = aixeta.coordenades().distancia(punt);
                Pair<Aixeta, Double> pair = new Pair<>(aixeta, distancia);

                // Utilitza la cerca dicotòmica per trobar la posició adequada
                int posicio = cercaDicotomica(aixetesOrdenades, pair);

                // Insereix l'element en la posició adequada
                aixetesOrdenades.add(posicio, pair);
            }

            System.out.println("proximitat");
            for (Pair<Aixeta, Double> par : aixetesOrdenades) {
                Aixeta aixeta = par.getKey();
                double llista = par.getValue();
                System.out.println(aixeta.nom() + " " + llista);
            }
        }
    }

    // Cerca dicotòmica per trobar la posició adequada segons la distància
    private int cercaDicotomica(ArrayList<Pair<Aixeta, Double>> array, Pair<Aixeta, Double> pair) {
        int esquerra = 0;
        int dreta = array.size() - 1;

        while (esquerra <= dreta) {
            int mig = esquerra + (dreta - esquerra) / 2;
            double distanciaMig = array.get(mig).getValue();
            String nomMig = array.get(mig).getKey().nom();

            if (distanciaMig == pair.getValue()) {
                // Si les distàncies són iguals, compara els noms
                int comparacio = nomMig.compareTo(pair.getKey().nom());
                if (comparacio == 0) return mig;
                else if (comparacio < 0) esquerra = mig + 1;
                else dreta = mig - 1;
            } else if (distanciaMig < pair.getValue()) {
                esquerra = mig + 1;
            } else {
                dreta = mig - 1;
            }
        }
        return esquerra;
    }


    public static Set<Canonada> excesCabal(XarxaAigua x, Origen nodeOrigen, Set<Canonada> _canonades){
    //Pre: nodeOrigen pertany a la xarxa x, la component connexa de la xarxa x que conté nodeOrigen no té cicles,
    // i les canonades de cjtCanonades pertanyen a aquesta component
    //Post: Retorna el subconjunt de canonades de cjtCanonades tals que, si es satisfés la demanda de tots els nodes
    // terminals de la mateixa component, es sobrepassaria la seva capacitat
        Set<Canonada> canonades = new HashSet<>();

        return canonades;
    }

    //Pre: --
    //Post: retorna el flux maxim que pot arribar a l'unic terminal de la xarxa
    public double fluxMaxim(XarxaAigua x, Aixeta o){
        XarxaAigua xAux = new XarxaAigua(x.subxarxa(o));
        double fluxMaxim=0;
        evitarAntiparaleles(xAux);
        Aixeta a = new Aixeta();
        xAux.dibuixarXarxa(a, true);
        return fluxMaxim;
    }

    //Pre: --
    //Post: mira si hi ha aluna aresta antiparal·lela, i si es el cas l'evita introduint vèrtexs intermitjos
    private void evitarAntiparaleles(XarxaAigua xAux) {
        // Recorre totes les claus (vèrtexs) del mapa original
        for (Map.Entry<Aixeta, ArrayList<Aixeta>> entry : xAux.mapa().entrySet()) {
            Aixeta vertex1 = entry.getKey();
            //System.out.println(vertex1.nom());
            ArrayList<Aixeta> adjacents = entry.getValue();
            for(Aixeta a : adjacents){
                System.out.println(a.nom());
                ArrayList<Aixeta> llistaVeins = xAux.veins(a);
                for(Aixeta b : llistaVeins){
                    System.out.println(b.nom()); //mostra els veins de l'adjecent, fixa't en la sortida, no ho fa be
                }
            }

            // Recorre tots els vèrtexs adjacents al vèrtex actual
            for (Aixeta vertex2 : adjacents) {
                ArrayList<Aixeta> llistaVeins = xAux.veins(vertex2);
                if (adjacents.contains(vertex2) && llistaVeins.contains(vertex1)) {
                    System.out.println("hola");
                    // Si s'ha trobat una aresta antiparal·lela, afegir un vèrtex intermig
                    Aixeta vIntermedi1 = new Aixeta(); // Cal crear un nou vèrtex intermig aquí
                    Aixeta vIntermedi2 = new Aixeta(); // Cal crear un nou vèrtex intermig aquí
                    //afegir els vertex a l'estructura
                    xAux.mapa().get(vertex1).add(vIntermedi1);
                    xAux.mapa().get(vertex2).add(vIntermedi2);
                    //falta obtenir la capacitat de la canonada
                    xAux.connectarAmbCanonada(vertex1, vIntermedi1, 3);
                    xAux.connectarAmbCanonada(vIntermedi1, vertex2, 3);
                    xAux.connectarAmbCanonada(vertex2, vIntermedi2, 3);
                    xAux.connectarAmbCanonada(vIntermedi2, vertex1, 3);
                    //eliminarCanonada(xAux, vIntermedi1, vIntermedi2);

                }
            }
        }
    }


    public void abonats() {
        //pre: Xarxa sense cicles
        //post: calcula el cabal minim que hauria d'haver als punts d'origen per tal que cap terminal rebi menys d'un determinat % de la seva demanda actual
    }

    public void gestioAixetes() {
        //pre: --
        //post: Obra i tenca aixetes
    }

    public void configAnterior() {
        //pre: --
        //post: Desfa una serie d’operacions d’obrir i tancar aixetes, de manera que tornem a una configuracio anterior.
    }
}
