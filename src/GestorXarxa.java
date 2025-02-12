/**
 * @file GestorXarxa.java
 * @brief Conté la implementació de la classe GestorXarxa que gestiona la xarxa d'aigua i les seves operacions.
 * @author JoanPau Rahola Ferrer
 */

import java.util.Iterator;
import java.util.ArrayList;

import javafx.util.Pair;

import java.util.*;

public class GestorXarxa {
    private Stack<Aixeta> modificacions;

    /**
     * @brief Constructor de la classe GestorXarxa.
     */
    public GestorXarxa() {
        modificacions = new Stack<>();
    }

    /**
     * Verifica si la xarxa té cicles.
     *
     * @param x        La xarxa d'aigua.
     * @param aixeta   El nom de l'aixeta des de la qual començar la comprovació.
     * @param dirigida Indica si la xarxa és dirigida (true) o no (false).
     * @return Cert si la xarxa té cicles, fals altrament.
     * @pre x és una xarxa d'aigua vàlida, aixeta és el nom d'una aixeta vàlida que pertany a x.
     * @post Retorna cert si es troba almenys un cicle a la xarxa, començant des de l'aixeta especificada, utilitzant la direcció especificada per la variable dirigida; en cas contrari, retorna fals.
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
     * @brief Comprova recursivament si la xarxa té cicles, partint d'una aixeta específica.
     * @param x La xarxa d'aigua.
     * @param aixeta L'aixeta des de la qual començar la comprovació.
     * @param pare L'aixeta pare de la aixeta actual.
     * @param visitats Conjunt d'aixetes visitades durant la comprovació.
     * @param enRecorregut Conjunt d'aixetes en el camí actual de la comprovació.
     * @param dirigida Indica si la xarxa és dirigida (true) o no (false).
     * @return Cert si es detecta un cicle, fals altrament.
     * @pre x és una xarxa d'aigua vàlida, aixeta és el nom d'una aixeta vàlida que pertany a x.
     * @post Retorna cert si es detecta un cicle partint de l'aixeta especificada, fals altrament.
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

    /**
     * @brief Comprova si la xarxa forma un arbre a partir de l'aixeta especificada.
     * @param x La xarxa d'aigua.
     * @param aixeta El nom de l'aixeta des de la qual començar la comprovació.
     * @return Cert si la xarxa forma un arbre, fals altrament.
     * @pre --
     * @post Retorna cert si la xarxa forma un arbre a partir de l'aixeta especificada, en cas contrari, retorna fals.
     */
    public boolean formaArbre(XarxaAigua x, String aixeta) {
        if (teCicles(x, aixeta, false)) {
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
        return nNodesFont == 1;
    }

    /**
     * @brief Calcula el cabal mínim necessari per garantir que cap node terminal de la xarxa rebi menys d'un determinat percentatge de la seva demanda.
     * @param x La xarxa d'aigua.
     * @param origen L'aixeta origen des de la qual començar la comprovació.
     * @param percent El percentatge mínim de demanda que s'ha de garantir.
     * @return El cabal mínim necessari.
     * @pre origen és una aixeta vàlida que pertany a la xarxa x, percent és un valor major que zero.
     * @post Retorna el cabal mínim necessari per garantir que cap node terminal de la xarxa, que sigui accessible des de l'origen especificat, rebi menys d'un determinat percentatge de la seva demanda.
     */
    public float cabalMinim(XarxaAigua x, Aixeta origen, float percent) {
        ArrayList<Aixeta> terminals = x.clausSubxarxa(origen); // Obté tots els nodes accessibles des de l'origen.
        float cabalMinim = 0;
        for (Aixeta element : terminals) {
            if (element instanceof Terminal && arribadaCabal(x, element)) {
                Terminal terminal = (Terminal) element;
                cabalMinim += terminal.obtenirDemanda() * percent;
            }
        }
        return cabalMinim;
    }

    /**
     * @brief Comprova si hi ha arribada de cabal a l'aixeta des de l'origen.
     * @param x La xarxa d'aigua.
     * @param aixeta L'aixeta que es vol comprovar.
     * @return Cert si hi ha arribada de cabal a l'aixeta, fals altrament.
     * @pre --
     * @post Diu si hi ha arribada de cabal a l'aixeta des de l'origen.
     */
    public static boolean arribadaCabal(XarxaAigua x, Aixeta aixeta) {
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

    /**
     * @brief Ordena les aixetes de la xarxa segons la seva proximitat al punt de referència.
     * @param x La xarxa d'aigua.
     * @param punt Les coordenades del punt de referència.
     * @param nomsAixetes La llista de noms d'aixetes que es volen ordenar.
     * @return Una llista de noms d'aixetes ordenades segons la seva proximitat al punt de referència.
     * @pre --
     * @post Retorna una llista de noms d'aixetes ordenades segons la seva proximitat al punt de referència.
     */
    public ArrayList<String> proximitat(XarxaAigua x, Coordenades punt, ArrayList<String> nomsAixetes) {
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
        }
        ArrayList<String> nomsAixetesOrdenades = new ArrayList<>();
        for (Pair<Aixeta, Double> par : aixetesOrdenades) {
            nomsAixetesOrdenades.add(par.getKey().nom());
        }
        return nomsAixetesOrdenades;
    }

    /**
     * @brief Realitza una cerca dicotòmica per trobar la posició adequada segons la distància.
     * @param array La llista de parelles (aixeta, distància).
     * @param pair La parella (aixeta, distància) que es vol inserir.
     * @return La posició adequada per inserir la parella en la llista ordenada.
     * @pre --
     * @post Retorna la posició adequada per inserir la parella en la llista ordenada.
     */
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

    /**
     * @brief Retorna el subconjunt de canonades que sobrepassarien la seva capacitat si es satisfés la demanda de tots els nodes terminals de la component.
     * @param x La xarxa d'aigua.
     * @param canonadesComprovar El conjunt de canonades que es volen comprovar.
     * @return El subconjunt de canonades que sobrepassarien la seva capacitat.
     * @pre nodeOrigen pertany a la xarxa x, la component connexa de la xarxa x que conté nodeOrigen no té cicles, i les canonades de canonadesComprovar pertanyen a aquesta component.
     * @post Retorna el subconjunt de canonades de canonadesComprovar tals que, si es satisfés la demanda de tots els nodes terminals de la mateixa component, es sobrepassaria la seva capacitat.
     */
    public Set<Canonada> excesCabal(XarxaAigua x, Set<Canonada> canonadesComprovar) {
        Set<Canonada> canonadesRebentades = new HashSet<>(); //set de canonades reventades

        ArrayList<Aixeta> claus = x.keys(); // Obté tots els nodes de la xarxa
        for (Aixeta aixeta : claus) {
            if (!(aixeta instanceof Terminal)) {

                aixeta.establirDemanda(0);
            }
            aixeta.establirCabalHipotetic(0);
        }
        ArrayList<Origen> origens = new ArrayList<>();
        for (Aixeta aixeta : claus) {
            if (aixeta instanceof Terminal && arribadaCabal(x, aixeta)) {
                pujarDemanda(x, aixeta, aixeta.obtenirDemanda());
            } else if (aixeta instanceof Origen) {
                origens.add((Origen) aixeta);
            }
        }
        for (Origen origen : origens) {
            repartirProporcionalment(x, origen, origen.cabal(), canonadesRebentades);
        }
        return trobarElementsIguals(canonadesRebentades, canonadesComprovar);
    }

    /**
     * @brief Retorna el subconjunt de canonades reventades que també estan en el conjunt de canonades a comprovar.
     * @param canonadesRebentades El conjunt de canonades que han reventat.
     * @param canonadesComprovar El conjunt de canonades que es volen comprovar.
     * @return El subconjunt de canonades reventades que també estan en el conjunt de canonades a comprovar.
     * @pre --
     * @post Retorna el subconjunt de canonades reventades que també estan en el conjunt de canonades a comprovar.
     */
    public Set<Canonada> trobarElementsIguals(Set<Canonada> canonadesRebentades, Set<Canonada> canonadesComprovar) {
        Set<Canonada> elementsIguals = new HashSet<>(canonadesRebentades);
        elementsIguals.retainAll(canonadesComprovar);
        return elementsIguals;
    }
    /**
     * @brief Puja la demanda d'una aixeta específica en la xarxa.
     * @param xarxa La xarxa d'aigua.
     * @param aixeta L'aixeta la demanda de la qual es vol pujar.
     * @param demandaPujar La quantitat de demanda a pujar.
     * @pre --
     * @post Puja la demanda de l'aixeta especificada en la quantitat indicada.
     */
    public void pujarDemanda(XarxaAigua xarxa, Aixeta aixeta, float demandaPujar) {
        ArrayList<Aixeta> aixetesEntrada = xarxa.aixetesEntrada(aixeta);
        float sumaTotalCapacitats = 0;
        for (Aixeta entrada : aixetesEntrada) {
            if (entrada.estat()) {
                Canonada canonada = xarxa.obtenirCanonada(entrada, aixeta);
                sumaTotalCapacitats += canonada.capacitat();
            }
        }
        float porporcio = demandaPujar / sumaTotalCapacitats;

        for (Aixeta entrada : aixetesEntrada) {
            if (entrada.estat()) {
                Canonada canonada = xarxa.obtenirCanonada(entrada, aixeta);
                demandaPujar = canonada.capacitat() * porporcio;
                float novaDemanda = entrada.obtenirDemanda() + demandaPujar;
                entrada.establirDemanda(novaDemanda);
                pujarDemanda(xarxa, entrada, demandaPujar);
            }
        }
    }

    /**
     * @brief Reparteix proporcionalment el cabal entre les aixetes sortides d'una aixeta específica.
     * @param xarxa La xarxa d'aigua.
     * @param aixeta L'aixeta des de la qual es vol repartir el cabal.
     * @param cabal La quantitat de cabal a repartir.
     * @param canonadesRebentades El conjunt de canonades que sobrepassarien la seva capacitat.
     * @pre --
     * @post Reparteix el cabal proporcionalment entre les aixetes sortides de l'aixeta especificada, i actualitza el conjunt de canonades que sobrepassarien la seva capacitat.
     */
    public void repartirProporcionalment(XarxaAigua xarxa, Aixeta aixeta, float cabal, Set<Canonada> canonadesRebentades) { //repasar
        ArrayList<Aixeta> aixetesSortida = xarxa.veins(aixeta);
        float sumaTotaDemandes = 0;
        for (Aixeta vei : aixetesSortida) {
            if (vei.estat()) {
                vei.obtenirDemanda();
                sumaTotaDemandes += vei.obtenirDemanda();
            }
        }
        float porporcio = cabal / sumaTotaDemandes;

        for (Aixeta vei : aixetesSortida) {
            if (vei.estat()) {
                Canonada canonada = xarxa.obtenirCanonada(aixeta, vei);
                cabal = vei.obtenirDemanda() * porporcio;
                vei.establirCabalHipotetic(vei.obtenirCabalHipotetic() + vei.obtenirDemanda() * porporcio);
                if (canonada.capacitat() < vei.obtenirCabalHipotetic()) {
                    canonadesRebentades.add(canonada);
                }

                repartirProporcionalment(xarxa, vei, cabal, canonadesRebentades);
            }
        }
    }

    /**
     * @brief Calcula el cabal assignat a un abonat identificat pel seu DNI.
     * @param x La xarxa d'aigua.
     * @param dni El DNI de l'abonat.
     * @return El cabal assignat a l'abonat.
     * @pre El DNI de l'abonat és vàlid i està registrat a la xarxa.
     * @post Retorna el cabal assignat a l'abonat identificat pel seu DNI.
     */
    public float cabalAbonat(XarxaAigua x, String dni) {
        Terminal t = x.terminalAbonat(dni);
        ArrayList<Aixeta> claus = x.keys(); // Obté tots els nodes de la xarxa
        for (Aixeta aixeta : claus) {
            if (!(aixeta instanceof Terminal)) {
                aixeta.establirCabalHipotetic(0);
                aixeta.establirDemanda(0);
            }
        }
        ArrayList<Origen> origens = new ArrayList<>();
        for (Aixeta aixeta : claus) {
            if (!(aixeta instanceof Terminal)) {
                aixeta.establirDemanda(0);
                aixeta.establirCabalHipotetic(0);
            }
        }
        for (Aixeta aixeta : claus) {
            if (aixeta instanceof Terminal && arribadaCabal(x, aixeta)) {
                pujarDemanda(x, aixeta, aixeta.obtenirDemanda());
            } else if (aixeta instanceof Origen) {
                origens.add((Origen) aixeta);
            }
        }
        for (Origen origen : origens) {
            repartirProporcionalmentAbonats(x, origen, origen.obtenirDemanda());
        }

        return t.obtenirCabalHipotetic();
    }

    /**
     * @brief Reparteix proporcionalment el cabal entre els abonats a partir d'una aixeta origen.
     * @param xarxa La xarxa d'aigua.
     * @param aixeta L'aixeta origen des de la qual es vol repartir el cabal.
     * @param cabal La quantitat de cabal a repartir.
     * @pre --
     * @post Reparteix el cabal proporcionalment entre els abonats a partir de l'aixeta especificada.
     */
    public void repartirProporcionalmentAbonats(XarxaAigua xarxa, Aixeta aixeta, float cabal) {
        ArrayList<Aixeta> aixetesSortida = xarxa.veins(aixeta);
        float sumaTotaDemandes = 0;
        for (Aixeta vei : aixetesSortida) {
            if (vei.estat()) {
                vei.obtenirDemanda();
                sumaTotaDemandes += vei.obtenirDemanda();
            }
        }
        float porporcio = cabal / sumaTotaDemandes;

        for (Aixeta vei : aixetesSortida) {
            if (vei.estat()) {
                Canonada canonada = xarxa.obtenirCanonada(aixeta, vei);
                cabal = vei.obtenirDemanda() * porporcio;
                if (canonada.capacitat() < cabal + vei.obtenirCabalHipotetic()) {
                    cabal = canonada.capacitat();
                    cabal = cabal - vei.obtenirCabalHipotetic();
                    vei.establirCabalHipotetic(canonada.capacitat());


                } else {
                    vei.establirCabalHipotetic(vei.obtenirCabalHipotetic() + cabal);
                }
                repartirProporcionalmentAbonats(xarxa, vei, cabal);
            }
        }
    }

    /**
     * @brief Calcula el flux màxim que pot arribar a l'únic terminal de la xarxa a partir d'una aixeta origen.
     * @param x La xarxa d'aigua.
     * @param o L'aixeta origen des de la qual es calcula el flux màxim.
     * @return El flux màxim que pot arribar a l'únic terminal de la xarxa.
     * @pre --
     * @post Retorna el flux màxim que pot arribar a l'únic terminal de la xarxa a partir de l'aixeta especificada.
     */
    public float fluxMaxim(XarxaAigua x, Aixeta o) {
        XarxaAigua xAux = new XarxaAigua(x.subxarxa(o));
        float fluxMaxim = 0;
        evitarAntiparaleles(xAux);
        superAixetes(xAux);
        //canonadesTornada(xAux);

        // Obtenir l'origen
        Aixeta origen = null;
        ArrayList<Aixeta> aixetes = xAux.keys();
        for (Aixeta aixeta : aixetes) {
            if (aixeta instanceof Origen) {
                origen = aixeta;
                break;
            }
        }

        if (origen == null) {
            return 0; // No hi ha origen, retornar 0
        }

        // Trobar i processar camins
        ArrayList<Aixeta> cami = trobarCami(origen, xAux);
        while (!cami.isEmpty()) {
            float cabalMinim = (float) Double.MAX_VALUE;

            // Calcula el cabal mínim en el camí trobat
            Aixeta previa = cami.get(0);
            for (int i = 1; i < cami.size(); i++) {
                Aixeta actual = cami.get(i);
                Canonada canonadaAnada = xAux.obtenirCanonada(previa, actual);
                float cabalPossible = canonadaAnada.capacitat() - canonadaAnada.fluxActual();
                if (cabalPossible < cabalMinim) {
                    cabalMinim = cabalPossible;
                }
                previa = actual;
            }

            // Aplica el cabal mínim a les canonades del camí
            previa = cami.get(0);
            for (int i = 1; i < cami.size(); i++) {
                Aixeta actual = cami.get(i);
                Canonada canonadaAnada = xAux.obtenirCanonada(previa, actual);
                Canonada canonadaTornada = xAux.obtenirCanonada(actual, previa);
                canonadaAnada.incrementarFlux(cabalMinim);
                canonadaTornada.incrementarFlux(-cabalMinim);
                previa = actual;
            }

            fluxMaxim += cabalMinim;
            cami = trobarCami(origen, xAux); // Busca un nou camí
        }

        Aixeta a = new Aixeta();
        xAux.dibuixarXarxa(a, true);
        return fluxMaxim;
    }

    /**
     * @brief Troba un camí en la xarxa auxiliar a partir d'una aixeta origen.
     * @param origen L'aixeta origen des de la qual es vol trobar el camí.
     * @param xarxa La xarxa auxiliar.
     * @return Una llista d'aixetes que representen el camí trobat.
     * @pre --
     * @post Retorna una llista d'aixetes que representen un camí en la xarxa auxiliar a partir de l'aixeta origen.
     */
    public ArrayList<Aixeta> trobarCami(Aixeta origen, XarxaAigua xarxa) {
        ArrayList<Aixeta> visitats = new ArrayList<>();
        ArrayList<Aixeta> cami = new ArrayList<>();
        boolean trobat = trobarCamiRec(origen, null, visitats, cami, xarxa);
        if (trobat) {
            Collections.reverse(cami);
        }
        return cami;
    }

    /**
     * @brief Cerca recursivament un camí des d'una aixeta actual fins a un terminal en la xarxa.
     * @param actual L'aixeta actual en la cerca.
     * @param anterior L'aixeta anterior en la cerca.
     * @param visitats La llista d'aixetes ja visitades.
     * @param cami La llista d'aixetes que formen el camí trobat.
     * @param xarxa La xarxa d'aigua.
     * @return Cert si es troba un camí fins a un terminal, fals altrament.
     * @pre --
     * @post Retorna cert si es troba un camí fins a un terminal, fals altrament.
     */
    public boolean trobarCamiRec(Aixeta actual, Aixeta anterior, ArrayList<Aixeta> visitats, ArrayList<Aixeta> cami, XarxaAigua xarxa) {
        visitats.add(actual);
        if (actual instanceof Terminal) {
            cami.add(actual);
            return true;
        }

        ArrayList<Aixeta> veins = xarxa.veins(actual);
        for (Aixeta vei : veins) {
            if (!visitats.contains(vei) && anterior != vei) {
                Canonada canonada = xarxa.obtenirCanonada(actual, vei);
                if (canonada != null && canonada.capacitat() > canonada.fluxActual()) {
                    boolean trobat = trobarCamiRec(vei, actual, visitats, cami, xarxa);
                    if (trobat) {
                        cami.add(actual);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * @brief Crea una canonada de tornada per cada node que indica l'aigua que envia al veí.
     * @param xAux La xarxa auxiliar.
     * @pre --
     * @post Modifica la xarxa afegint canonades de tornada per cada node, indicant l'aigua que envia al veí.
     */
    public void canonadesTornada(XarxaAigua xAux) {
        if (xAux == null || xAux.mapa() == null) {
            System.err.println("Error: XarxaAigua object or its mapa() method is null.");
            return;
        }

        Map<Aixeta, ArrayList<Aixeta>> mapa = xAux.mapa();

        for (Aixeta aixeta : mapa.keySet()) {
            ArrayList<Aixeta> veins = xAux.veins(aixeta);
            if (veins == null) {
                System.err.println("Error: veins() method returned null for aixeta: " + aixeta);
                continue;
            }
            for (Aixeta aixetaVeina : veins) {

                ArrayList<Aixeta> llista = mapa.get(aixetaVeina);
                if (llista == null) {
                    System.err.println("Error: ArrayList per aixetaVeina " + aixetaVeina + " es null. ARREGLAR DIMARTS");
                    continue;
                }
                mapa.get(aixetaVeina).add(aixeta);
                xAux.connectarAmbCanonada(aixetaVeina, aixeta, 0);
            }
        }
    }


    /**
     * @brief Processa les super aixetes en la xarxa auxiliar.
     * @param xarxa La xarxa auxiliar.
     * @pre --
     * @post Modifica la xarxa auxiliar per processar les super aixetes.
     */
    public void superAixetes(XarxaAigua xarxa) {
        Map<Aixeta, ArrayList<Aixeta>> xAux = new HashMap<>(xarxa.mapa());
        float cabalTotalEntrada = 0;
        float demandaTotal = 0;
        ArrayList<Aixeta> adjecentsOrigen = new ArrayList<>();

        Terminal superPou = new Terminal("SuperPou");
        Origen superFont = new Origen("SuperFont", cabalTotalEntrada);
        int comptOrigens = 0;
        int comptTerminals = 0;
        ArrayList<Aixeta> aixetes = xarxa.keys();
        for (Aixeta aixeta : aixetes) {
            if (aixeta instanceof Origen) {
                comptOrigens++;
            } else if (aixeta instanceof Terminal) {
                comptTerminals++;
            }
        }

        for (Map.Entry<Aixeta, ArrayList<Aixeta>> entry : xAux.entrySet()) {
            Aixeta aixeta = entry.getKey();
            if (aixeta instanceof Origen && comptOrigens > 1) {
                adjecentsOrigen.add(aixeta);
                cabalTotalEntrada += ((Origen) aixeta).cabal();
            }
            if (aixeta instanceof Terminal && comptTerminals > 1) {
                xarxa.connectarTerminals(aixeta, superPou, aixeta.obtenirDemanda());
                demandaTotal += aixeta.obtenirDemanda();
            }
        }

        if (comptOrigens > 1) {
            superFont.determinarCabal(cabalTotalEntrada);
            xarxa.afegir(superFont, adjecentsOrigen);
            ArrayList<Aixeta> adjacentsSuperfontCopiat = new ArrayList<>(adjecentsOrigen);
            for (Aixeta aixeta : adjacentsSuperfontCopiat) {
                xarxa.connectarOrigens(superFont, aixeta, ((Origen) aixeta).cabal());
            }
        }
        if (comptTerminals > 1) {
            superPou.determinarDemanda(demandaTotal);
            xarxa.afegir(superPou);
        }

    }

    /**
     * @brief Evita canonades antiparal·leles en la xarxa auxiliar.
     * @param xarxa La xarxa auxiliar.
     * @pre --
     * @post Modifica la xarxa auxiliar per evitar canonades antiparal·leles.
     */
    public void evitarAntiparaleles(XarxaAigua xarxa) {
        ArrayList<Aixeta> claus = new ArrayList<>(xarxa.keys());
        for (Aixeta vertex1 : claus) {
            System.out.println(vertex1.nom());
            ArrayList<Aixeta> cannonades = xarxa.veins(vertex1);
            System.out.println("cannonades:");
            for (Aixeta vertex2 : cannonades) {
                System.out.println(xarxa.obtenirCanonada(vertex2, vertex1).capacitat());
            }
            System.out.println("");
        }

        for (Aixeta vertex1 : claus) {
            ArrayList<Aixeta> adjacents = xarxa.veins(vertex1); // Obtenir els vèrtexs adjacents al vèrtex actual
            ArrayList<Aixeta> adjacentsAux = new ArrayList<>(adjacents);
            for (Aixeta vertex2 : adjacentsAux) {
                ArrayList<Aixeta> llistaVeins = xarxa.veins(vertex2);
                if (adjacents.contains(vertex2) && llistaVeins.contains(vertex1)) {
                    // Si s'ha trobat una aresta antiparal·lela, afegir un vèrtex intermig

                    Connexio vIntermedi1 = new Connexio(vertex1.nom() + vertex2.nom(), vertex1.coordenades()); // Cal crear un nou vèrtex intermig aquí
                    Connexio vIntermedi2 = new Connexio(vertex2.nom() + vertex1.nom(), vertex2.coordenades()); // Cal crear un nou vèrtex intermig aquí
                    ArrayList<Aixeta> adjecentsVintermidi1 = new ArrayList<>();
                    adjecentsVintermidi1.add(vertex2);
                    ArrayList<Aixeta> adjecentsVintermidi2 = new ArrayList<>();

                    adjecentsVintermidi2.add(vertex1);
                    xarxa.mapa().get(vertex1).add(vIntermedi1);
                    xarxa.mapa().get(vertex2).add(vIntermedi2);

                    xarxa.connectarAmbCanonada(vertex1, vIntermedi1, xarxa.obtenirCanonada(vertex1, vertex2).capacitat());
                    xarxa.connectarAmbCanonada(vIntermedi1, vertex2, xarxa.obtenirCanonada(vertex1, vertex2).capacitat());
                    xarxa.connectarAmbCanonada(vertex2, vIntermedi2, xarxa.obtenirCanonada(vertex2, vertex1).capacitat());
                    xarxa.connectarAmbCanonada(vIntermedi2, vertex1, xarxa.obtenirCanonada(vertex2, vertex1).capacitat());

                    xarxa.eliminarCanonada(vertex1, vertex2);
                    xarxa.eliminarCanonada(vertex2, vertex1);
                }
            }
        }
    }

    /**
     * @brief Determina les aixetes de la xarxa més properes als punts terminals per sota de les quals és segur que s'ha trencat (o embussat) alguna canonada.
     * @param xarxa La xarxa d'aigua.
     * @param aiguaArriba Un mapa que indica si l'aigua arriba a cada terminal.
     * @return Un conjunt d'aixetes que cal tancar.
     * @pre --
     * @post Retorna un conjunt d'aixetes que cal tancar per evitar fuites d'aigua a les aixetes que no funcionen correctament.
     */
    public Set<Aixeta> aixetesTancar(XarxaAigua xarxa, Map<Terminal, Boolean> aiguaArriba) {
        Set<Aixeta> aixetesTancar = new HashSet<>();
        ArrayList<Aixeta> aixetesTrencades = new ArrayList<>();
        ArrayList<Aixeta> aixetesCorrectes = new ArrayList<>();

        for (Map.Entry<Terminal, Boolean> entry : aiguaArriba.entrySet()) {
            if (entry.getKey() instanceof Terminal && !entry.getValue() && arribadaCabal(xarxa, entry.getKey())) { // es un terminal al que ens diuen que no hi arriba aigua i realment hi ahuria d'arribar aigua
                aixetesTrencades.add(entry.getKey());
            } else if (entry.getKey() instanceof Terminal && entry.getValue()) {
                aixetesCorrectes.add(entry.getKey());
            }
        }

        assignaTeCabal(xarxa, aixetesCorrectes);

        tancarAixetes(xarxa, aixetesTrencades, aixetesTancar);
        return aixetesTancar;
    }

    /**
     * @brief Modifica l'atribut teCabal a totes les aixetes que es sap segur que hi haurà cabal.
     * @param xarxa La xarxa d'aigua.
     * @param aixetesCorrectes La llista d'aixetes que es sap segur que tenen cabal.
     * @pre --
     * @post Modifica l'atribut teCabal a totes les aixetes de la llista proporcionada.
     */
    private void assignaTeCabal(XarxaAigua xarxa, ArrayList<Aixeta> aixetesCorrectes) {
        for (Aixeta aixeta : aixetesCorrectes) {
            assignaTeCabalRec(xarxa, aixeta);
        }
    }

    /**
     * @brief Modifica recursivament l'atribut teCabal a una aixeta i les seves aixetes d'entrada.
     * @param xarxa La xarxa d'aigua.
     * @param aixeta L'aixeta a la qual es vol modificar l'atribut teCabal.
     * @pre --
     * @post Modifica l'atribut teCabal a l'aixeta i les seves aixetes d'entrada.
     */
    private void assignaTeCabalRec(XarxaAigua xarxa, Aixeta aixeta) {
        ArrayList<Aixeta> aixetaEntrada = xarxa.aixetesEntrada(aixeta);
        if (!aixetaEntrada.isEmpty()) {
            assignaTeCabalRec(xarxa, aixetaEntrada.getFirst());
            aixeta.teCabal(true);
        }
    }

    /**
     * @brief Tanca les aixetes pertinents per evitar fuites d'aigua a les aixetes que no funcionen correctament.
     * @param xarxa La xarxa d'aigua.
     * @param aixetesTrencades La llista d'aixetes que es sap que estan trencades.
     * @param aixetesTancar El conjunt d'aixetes que cal tancar.
     * @pre --
     * @post Tanca les aixetes pertinents per evitar fuites d'aigua.
     */
    private void tancarAixetes(XarxaAigua xarxa, ArrayList<Aixeta> aixetesTrencades, Set<Aixeta> aixetesTancar) {
        boolean parar = false;
        for (Aixeta aixeta : aixetesTrencades) {
            tancarAixetesRec(xarxa, aixeta, aixetesTancar);
        }
    }

    /**
     * @brief Tanca recursivament les aixetes pertinents per evitar fuites d'aigua.
     * @param xarxa La xarxa d'aigua.
     * @param aixeta L'aixeta actual en la recursió.
     * @param aixetesTancar El conjunt d'aixetes que cal tancar.
     * @pre --
     * @post Tanca recursivament les aixetes pertinents per evitar fuites d'aigua.
     */
    private void tancarAixetesRec(XarxaAigua xarxa, Aixeta aixeta, Set<Aixeta> aixetesTancar) {
        ArrayList<Aixeta> aixetaEntrada = xarxa.aixetesEntrada(aixeta);
        if (aixetaEntrada.getFirst().teCabal()) {
            aixetesTancar.add(aixetaEntrada.getFirst());
        } else {
            tancarAixetesRec(xarxa, aixetaEntrada.getFirst(), aixetesTancar);
        }
    }

    /**
     * @brief Desfa una sèrie d'operacions d'obrir i tancar aixetes, tornant a una configuració anterior.
     * @param n El nombre d'operacions a desfer.
     * @pre --
     * @post Desfà les últimes n operacions d'obrir i tancar aixetes, tornant a una configuració anterior.
     */
    public void backtrack(int n) {
        //pre: --
        //post: Desfa una sèrie d’operacions d’obrir i tancar aixetes, de manera que tornem a una configuració anterior.
        for (int i = 0; i < n; i++) {
            if (!modificacions.isEmpty()) {
                if (modificacions.getLast().estat()) {
                    modificacions.getLast().tancarAixeta();
                } else {
                    modificacions.getLast().obrirAixeta();
                }
                modificacions.removeLast();
            }
        }
    }

    /**
     * @brief Afegeix una modificació a l'historial de modificacions.
     * @param aixeta L'aixeta que s'ha modificat.
     * @pre --
     * @post Afegeix l'aixeta a la llista de modificacions.
     */
    public void historialModificacions(Aixeta aixeta) {
        modificacions.add(aixeta);
    }
}