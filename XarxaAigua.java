/**
 * @file principal.XarxaAigua.java
 * @brief Classe principal.XarxaAigua que representa una xarxa d'aigua amb les seves aixetes i canonades,
 * i permet realitzar operacions sobre la xarxa com ara afegir aixetes, connectar-les amb canonades,
 * tancar-les, obrir-les, determinar cabals mínims, etc. A més, també permet mostrar la representació gràfica de la xarxa d'aigua.
 * @author Julia Baye Soler
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Edge;

public class XarxaAigua {
    private Map<Aixeta, ArrayList<Aixeta>> xarxa;
    private Graph graphStream;

    // Pre: Cap
    // Post: S'ha inicialitzat una nova xarxa d'aigua sense cap aixeta
    public XarxaAigua() {
        xarxa = new HashMap();
    }


    public XarxaAigua(Map<Aixeta, ArrayList<Aixeta>> x) {
        xarxa = x;
    }

    public void mostrar() {
        for (Map.Entry<Aixeta, ArrayList<Aixeta>> entry : xarxa.entrySet()) {
            Aixeta vertex1 = entry.getKey();
            System.out.println(vertex1.nom());
            ArrayList<Aixeta> adjacents = entry.getValue();
            for(Aixeta a : adjacents){
                System.out.println(a.nom());
            }
        }
    }

    // Pre: Cap
    // Post: L'aixeta 'a' ha estat eliminada de la xarxa
    public void eliminarAixeta(Aixeta a) {
        xarxa.remove(a);
    }

    public void eliminarCanonada(Aixeta a,Aixeta b) {
        Canonada c = obtenirCanonada(a,b);
        c.origen().eliminarCanonada(c);
        c.desti().eliminarCanonada(c);
        ArrayList<Aixeta> aixetesA = xarxa.get(a);
        ArrayList<Aixeta> aixetesB = xarxa.get(b);
        aixetesA.remove(b);aixetesA.remove(a);
        aixetesB.remove(a);aixetesA.remove(b);
    }

    // Pre: Cap
    // Post: S'ha mostrat la representació gràfica de la xarxa d'aigua
    public void dibuixarXarxa(Aixeta aixeta, boolean maxFlow) {
        System.setProperty("org.graphstream.ui", "swing");
        if(maxFlow) {
            this.graphStream = this.crearGraphStream();
        }
        else{
            this.graphStream = this.crearGraphStream(aixeta);
        }
        this.graphStream.display();
    }

    // Pre: L'aixeta (aixeta) ha de ser un node vàlid de la xarxa d'aigua.
    // Post: Retorna un mapa que representa la subxarxa de la xarxa d'aigua, amb l'aixeta especificada (aixeta) com a punt
    // de referencia. La subxarxa conté totes les aixetes accessibles des de l'aixeta especificada (aixeta), incloent aquesta mateixa,
    // amb les seves corresponents connexions.
    public Map<Aixeta, ArrayList<Aixeta>> subxarxa(Aixeta aixeta) {
        Map<Aixeta, ArrayList<Aixeta>> subxarxa = new HashMap<>();
        ArrayList<Aixeta> claus = clausSubxarxa(aixeta);
        for(Aixeta element : claus){
            //System.out.println(element.nom());
            subxarxa.put(element, xarxa.get(element));
        }

        return subxarxa;
    }

    // Pre: --
    // Post: Retorna un objecte GraphStream que representa la subxarxa de la xarxa d'aigua amb l'aixeta especificada com a punt
    // central, amb nodes i arestes que mostren les connexions i les característiques visuals dels nodes i les canonades,
    // incloent-hi la direcció de flux i la capacitat de les canonades.
    public Graph crearGraphStream(Aixeta aixeta) {
        Map<Aixeta, ArrayList<Aixeta>> subxarxa = subxarxa(aixeta);

        // Activa el renderitzador J2D per a la visualització
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

        // Crea un nou graf amb el nom "XarxaGraph"
        Graph graph = new SingleGraph("XarxaGraph");

        // Itera sobre totes les entrades (nodes) a la estructura de dades xarxa
        Iterator<Map.Entry<Aixeta, ArrayList<Aixeta>>> iterator = subxarxa.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Aixeta, ArrayList<Aixeta>> entry = iterator.next();
            Aixeta aixetaSubxarxa = entry.getKey();

            // Comprova si aixetaSubxarxa és null abans de continuar
            if (aixetaSubxarxa == null) {
                continue;
            }
            Node node = graph.addNode(aixetaSubxarxa.nom()); // Crea un node en el graf amb el nom del node actual

            // Configuració visual del node
            node.setAttribute("ui.style", "size: 20px; fill-color: blue; text-size: 16px; text-alignment: under; text-padding: 5px;");
            node.setAttribute("ui.label", "Nom: " + aixetaSubxarxa.nom() + " \nprincipal.Coordenades: " + aixetaSubxarxa.coordenades().mostrar());

            // Configuració visual segons l'estat de l'aixeta
            if (aixetaSubxarxa.estat()) {
                node.setAttribute("ui.style", "size: 20px; fill-color: green; text-size: 16px; text-alignment: under; text-padding: 5px;");
            } else {
                node.setAttribute("ui.style", "size: 20px; fill-color: red; text-size: 16px; text-alignment: under; text-padding: 5px;");
            }
        }

        // Afegeix les arestes amb les seves etiquetes i propietats, incloent-hi les fletxes per indicar el sentit
        for (Map.Entry<Aixeta, ArrayList<Aixeta>> entry : subxarxa.entrySet()) {
            ArrayList<Aixeta> aixetes = entry.getValue();
            if (aixetes != null) { // Verifica si el valor no és null
                Aixeta clau = entry.getKey();
                for (Aixeta valor : aixetes) {
                    String edgeId = clau.nom() + "-" + valor.nom();
                    if (graph.getEdge(edgeId) == null) {
                        Canonada canonada = obtenirCanonada(clau, valor); // Obtenim la canonada entre els dos nodes
                        Edge edge;
                        if (canonada != null) {
                            // Si hi ha una canonada entre els dos nodes, l'aresta és dirigida amb la capacitat de la canonada
                            edge = graph.addEdge(edgeId, clau.nom(), valor.nom(), true);
                            edge.setAttribute("ui.style", "size: 3px; fill-color: #000000;"); // Ajustaments visuals bàsics
                            edge.setAttribute("ui.label", "Capacitat: " + canonada.capacitat()); // Afegim la capacitat com a etiqueta de l'aresta

                            // Afegim la direcció de l'aresta
                            if (clau == canonada.origen()) {
                                edge.setAttribute("ui.arrow1", true); // Fletxa des de l'origen cap al destí
                            } else {
                                edge.setAttribute("ui.arrow2", true); // Fletxa des del destí cap a l'origen
                            }
                        } else {
                            // Si no hi ha canonada entre els dos nodes, l'aresta és no dirigida
                            edge = graph.addEdge(edgeId, clau.nom(), valor.nom(), false);
                        }
                    }
                }
            }
        }
        return graph;
    }

    // Pre: --
    // Post: Retorna un objecte GraphStream que representa la xarxa d'aigua amb els nodes i les arestes corresponents, amb
    // nodes representats com a cercles blau verdosos i arestes amb etiquetes que mostren la capacitat de les canonades,
    // incloent-hi la direcció de flux si correspon.
    public Graph crearGraphStream() {
        // Activa el renderitzador J2D per a la visualització
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

        // Crea un nou graf amb el nom "XarxaGraph"
        Graph graph = new SingleGraph("XarxaGraph");

        // Itera sobre totes les entrades (nodes) a la estructura de dades xarxa
        for (Map.Entry<Aixeta, ArrayList<Aixeta>> entry : xarxa.entrySet()) {
            Aixeta aixetaSubxarxa = entry.getKey();

            // Comprova si aixetaSubxarxa és null abans de continuar
            if (aixetaSubxarxa == null) {
                continue;
            }
            Node node = graph.addNode(aixetaSubxarxa.nom()); // Crea un node en el graf amb el nom del node actual

            // Configuració visual del node: només mostrem el nom de l'aixeta
            node.setAttribute("ui.label", aixetaSubxarxa.nom());

            // Canvia el color del node a blau verdós, augmenta la mida del node i del text
            node.setAttribute("ui.style", "fill-color: rgb(30, 144, 255); size: 30px; text-size: 15px;"); // Ajusta 'size' i 'text-size' segons sigui necessari
        }

        // Afegeix les arestes amb les seves etiquetes i propietats, incloent-hi les fletxes per indicar el sentit
        for (Map.Entry<Aixeta, ArrayList<Aixeta>> entry : xarxa.entrySet()) {
            ArrayList<Aixeta> aixetes = entry.getValue();
            if (aixetes != null) { // Verifica si el valor no és null
                Aixeta clau = entry.getKey();
                for (Aixeta valor : aixetes) {
                    String edgeId = clau.nom() + "-" + valor.nom();
                    if (graph.getEdge(edgeId) == null) {
                        Canonada canonada = obtenirCanonada(clau, valor); // Obtenim la canonada entre els dos nodes
                        Edge edge;
                        if (canonada != null) {
                            // Si hi ha una canonada entre els dos nodes, l'aresta és dirigida amb la capacitat de la canonada
                            edge = graph.addEdge(edgeId, clau.nom(), valor.nom(), true);

                            // Afegim l'etiqueta de la capacitat a l'aresta i augmentem la mida de l'etiqueta
                            edge.setAttribute("ui.label", canonada.capacitat());
                            edge.setAttribute("ui.style", "text-size: 13;"); // Ajusta 'text-size' segons sigui necessari

                            // Afegim la direcció de l'aresta
                            if (clau == canonada.origen()) {
                                edge.setAttribute("ui.arrow1", true); // Fletxa des de l'origen cap al destí
                            } else {
                                edge.setAttribute("ui.arrow2", true); // Fletxa des del destí cap a l'origen
                            }
                        } else {
                            // Si no hi ha canonada entre els dos nodes, l'aresta és no dirigida
                            edge = graph.addEdge(edgeId, clau.nom(), valor.nom(), false);
                        }
                    }
                }
            }
        }
        return graph;
    }

    //

    // Pre: Cap
    // Post: S'ha retornat un ArrayList amb totes les claus de la xarxa
    public ArrayList<Aixeta> keys() {
        ArrayList<Aixeta> keys = new ArrayList<>();
        for (Aixeta a : xarxa.keySet()) { //s'obte clada clau del mapa
            keys.add(a);
        }
        return keys;
    }

    //Pre: aixeta no és nul·la i xarxa no és nul·la
    //Post: Retorna una llista d'Aixetes sense tenir en compte la direcció
    public ArrayList<Aixeta> veinsNoDirigit(Aixeta aixeta) {
        ArrayList<Aixeta> veins = new ArrayList<>();

        // Itera a través de totes les entrades del mapa xarxa
        for (Map.Entry<Aixeta, ArrayList<Aixeta>> entry : xarxa.entrySet()) {
            Aixeta aixetaClau = entry.getKey();
            ArrayList<Aixeta> conexions = entry.getValue();

            // Si la aixeta donada es troba a les conexions de l'aixeta de la clau afegeix l'aixeta de la clau a la llista de veïns
            if (conexions.contains(aixeta)) {
                veins.add(aixetaClau);
            }

            // També, si la aixeta donada es troba com a clau, afegeix totes les aixetes de les conexions a la llista de veïns
            if (aixetaClau.equals(aixeta)) {
                veins.addAll(conexions);
            }
        }

        return veins;
    }

    //Pre: origen i desti no són nuls
    //Post: Retorna una principal.Canonada que connecta origen amb desti, o null si no existeix
    public Canonada obtenirCanonada(Aixeta a, Aixeta b) {
        ArrayList<Canonada> canonadesOrigen = a.canonades();
        for (Canonada canonada : canonadesOrigen) {
            if (canonada.origen().nom().equals(a.nom()) && canonada.desti().nom().equals(b.nom()) || canonada.origen().nom().equals(b.nom()) && canonada.desti().nom().equals(a.nom())) {
                return canonada; // Retorna la canonada que té com a destí el node desti
            }
        }
        return null;
    }

    //Pre: No existeix cap node amb el mateix id que nodeOrigen a la xarxa
    //Post: S'ha afegit nodeOrigen a la xarxa
    public void afegir(Origen nodeOrigen) {

        ArrayList veins;
        if (!this.xarxa.containsKey(nodeOrigen.nom())) {
            veins = new ArrayList();
            this.xarxa.put(nodeOrigen, veins);
        }
    }

    //Pre:  No existeix cap node amb el mateix id que nodeTerminal a la xarxa
    //Post: S'ha afegit nodeTerminal a la xarxa
    public void afegir(Terminal nodeTerminal) {
        ArrayList veins;
        if (!this.xarxa.containsKey(nodeTerminal.nom())) {
            veins = new ArrayList();
            this.xarxa.put(nodeTerminal, veins);
        }
    }

    //Pre:  No existeix cap node amb el mateix id que nodeConnexio a la xarxa
    //Post: S'ha afegit nodeConnexio a la xarxa
    public void afegir(Connexio nodeConnexio) {
        ArrayList veins;
        if (!this.xarxa.containsKey(nodeConnexio.nom())) {
            veins = new ArrayList();
            this.xarxa.put(nodeConnexio, veins);
        }
    }

    //Pre:  No existeix cap node amb el mateix id que nodeConnexio a la xarxa
    //Post: S'ha afegit nodeConnexio a la xarxa
    public void afegir(Connexio nodeConnexio, ArrayList llista) {
        if (!this.xarxa.containsKey(nodeConnexio.nom())) {
            this.xarxa.put(nodeConnexio, llista);
        }
    }

    //Pre: a no és nul·la
    // Post: Retorna una llista que conté la subxarxa connexa on pertany a. Si no hi ha veïns, retorna una llista buida.
    public ArrayList<Aixeta> clausSubxarxa(Aixeta a){
        ArrayList<Aixeta> keys = new ArrayList<>();
        clausSubxarxaRec(a, keys);
        return keys;
    }

    //Pre: aixeta no és nul·la
    //Post: Retorna un set d'Aixetes que són les claus de les aixetes accessibles des de l'aixeta a, inclòs a.
    private void clausSubxarxaRec(Aixeta aixeta, ArrayList<Aixeta> keys) {
        boolean trobat = false;
        for (Aixeta elemento : keys) {
            if (elemento.nom().equals(aixeta.nom())) {
                trobat = true;
                break;
            }
        }
        if (!trobat) {
            keys.add(aixeta);
            //System.out.println(aixeta.nom());
            for (Aixeta element : veinsNoDirigit(aixeta)) {
                clausSubxarxaRec(element, keys);
            }
        }
    }

    //Pre: a no és nul·la
    //Post: retorna una llista d'Aixetes amb les aixetes que entren l'aixeta a
    public ArrayList<Aixeta> aixetesEntrada(Aixeta a) {
        ArrayList<Aixeta> aixetes = new ArrayList<>();
        ArrayList<Canonada> cononades = a.canonades();
        for (Canonada c : cononades) {
            if (c.desti().equals(a)) {
                aixetes.add(c.origen());
            }
        }
        return aixetes;
    }

    //Pre: a no és nul·la
    //Post: Retorna una llista d'Aixetes que són veïnes a l'aixeta a tenint en compte la direcció de les canonades
    ArrayList<Aixeta> veins(Aixeta a) {
        ArrayList<Aixeta> veins = xarxa.get(a);
        if (veins == null) {
            veins = new ArrayList<>();
        }
        return veins;
    }

    //Pre: node1 i node2 pertanyen a la xarxa, no estan connectats, i node1 no és un node terminal
    //Post: S'han connectat els nodes amb una canonada de capacitat c, amb sentit de l'aigua de node1 a node2
    public void connectarAmbCanonada(Aixeta origen, Aixeta desti, float capacitat) {
        ArrayList<Aixeta> conexionsAixeta1 = xarxa.get(origen);
        conexionsAixeta1.add(desti);
        Canonada c = new Canonada(origen, desti, capacitat);
        origen.afegirCanonada(c);
        desti.afegirCanonada(c);
    }

    //Pre: t és un node terminal de la xarxa
    //Post: S'ha afegit l'abonat amb dni a la llista d'abonats de t
    public void abonar(Terminal t, String dni) {
        t.afegirAbonat(dni);
    }

    //Pre: La xarxa no té cicles
    //Post: S'ha calculat el cabal mínim que hauria de tenir cada origen per tal que cap terminal rebi menys d'un determinat % de la seva demanda actual
    public void determinarCabal(Origen o, float cabal) {
        o.determinarCabal(cabal);
    }

    //Pre: La xarxa no té cicles
    //Post: S'ha calculat el cabal mínim que hauria de tenir cada origen per tal que cap terminal rebi menys d'un determinat % de la seva demanda actual
    public void determinarDemanda(Terminal t, float demanda) {
        t.determinarDemanda(demanda);
    }

    //Pre: a no és nul·la
    //Post: Si a existeix a la xarxa, tanca l'aixeta corresponent.
    public void tancarAixeta(Aixeta a) {
        if (xarxa.containsKey(a)) {
            a.tancarAixeta();
        }
    }

    //Pre: a no és nul·la
    //Post: Si a existeix a la xarxa, obre l'aixeta corresponent.
    public void obrirAixeta(Aixeta a) {
        if (xarxa.containsKey(a)) {
            a.obrirAixeta();
        }
    }

    public Map<Aixeta, ArrayList<Aixeta>> mapa(){
        return xarxa;
    }

    //Pre: cap
    //Post: Retorna l'principal.Aixeta amb el nom especificat; si no existeix cap aixeta amb aquest nom, retorna null.
    public Aixeta aixeta(String a) {
        Iterator var2 = this.xarxa.keySet().iterator();

        Aixeta aixeta;
        do {
            if (!var2.hasNext()) {
                return null;
            }
            aixeta = (Aixeta) var2.next();
        } while (!aixeta.nom().equals(a));

        return aixeta;
    }
}