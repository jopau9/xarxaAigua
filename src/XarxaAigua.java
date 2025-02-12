/**
 * @file principal.XarxaAigua.java
 * @brief Classe principal.XarxaAigua que representa una xarxa d'aigua amb les seves aixetes i canonades,
 * i permet realitzar operacions sobre la xarxa com ara afegir aixetes, connectar-les amb canonades,
 * tancar-les, obrir-les, determinar cabals mínims, etc. A més, també permet mostrar la representació gràfica de la xarxa d'aigua.
 * @author Julia Baye Soler
 */

import java.util.*;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Edge;

public class XarxaAigua {
    /**
     * Mapa que emmagatzema les connexions entre aixetes i les aixetes que estan connectades a elles.
     */
    private Map<Aixeta, ArrayList<Aixeta>> xarxa;

    /**
     * Grau que representa la xarxa de connexions entre aixetes.
     */
    private Graph graphStream;

    /**
     * @brief Constructor per inicialitzar una nova xarxa d'aigua sense cap aixeta.
     * @pre Cap
     * @post S'ha inicialitzat una nova xarxa d'aigua sense cap aixeta.
     */
    public XarxaAigua() {
        xarxa = new HashMap<>();
    }

    /**
     * @param x La estructura de dades Map que representa la xarxa d'aigua.
     * @brief Constructor de la classe principal.XarxaAigua amb una estructura de dades de tipus Map.
     * @pre Cap.
     * @post Crea una instància de principal.XarxaAigua amb la estructura de dades especificada.
     */
    public XarxaAigua(Map<Aixeta, ArrayList<Aixeta>> x) {
        xarxa = x;
    }
//
    /**
     * @param aixeta principal.Aixeta a partir de la qual es dibuixarà la xarxa.
     * @brief Mostra la representació gràfica de la xarxa d'aigua.
     * @pre Cap
     * @post S'ha mostrat la representació gràfica de la xarxa d'aigua.
     */
    public void dibuixarXarxa(Aixeta aixeta, boolean maxFlow) {
        System.setProperty("org.graphstream.ui", "swing");
        if (maxFlow) {
            this.graphStream = this.crearGraphStream();
        } else {
            this.graphStream = this.crearGraphStream(aixeta);
        }

        this.graphStream.display().disableAutoLayout();
    }


    /**
     * @param aixeta Aixeta de referència per obtenir la subxarxa.
     * @return Una subxarxa de la xarxa original.
     * @brief Retorna una subxarxa de la xarxa original amb les aixetes connectades a la aixeta donada.
     * @pre Cap
     * @post S'ha retornat una subxarxa de la xarxa original amb les aixetes connectades a la aixeta donada.
     */
    public Map<Aixeta, ArrayList<Aixeta>> subxarxa(Aixeta aixeta) {
        XarxaAigua subxarxa = new XarxaAigua();
        ArrayList<Aixeta> claus = clausSubxarxa(aixeta);

        for (Aixeta element : claus) {
            //System.out.println(element);
            Aixeta aixetaO = new Aixeta(element);
            ArrayList<Aixeta> llistaOriginal = veins(element);
            ArrayList<Aixeta> novaLlista = new ArrayList<>();
            for (Aixeta a : llistaOriginal) {
                Aixeta aixetaD = new Aixeta(a);
                novaLlista.add(aixetaD);
                subxarxa.connectarAmbCanonada(aixetaO,aixetaD,obtenirCanonada(element,a).capacitat());// Aquí es fa la còpia de cada aixeta
            }
            subxarxa.mapa().put(aixetaO, novaLlista); // Aquí es fa la còpia de la clau
        }
        return subxarxa.mapa();
    }


    /**
     * @param aixeta principal. Aixeta de referència per crear el GraphStream.
     * @return Un objecte GraphStream que representa la xarxa d'aigua.
     * @brief Crea un objecte GraphStream que representa la xarxa d'aigua a partir de l'aixeta donada.
     * @pre Cap
     * @post S'ha creat un objecte GraphStream que representa la xarxa d'aigua a partir de l'aixeta donada.
     */
    public Graph crearGraphStream(Aixeta aixeta) {
        Map<Aixeta, ArrayList<Aixeta>> subxarxa = subxarxa(aixeta);

        // Activa el renderitzador J2D per a la visualització
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

        // Crea un nou graf amb el nom "XarxaGraph"
        Graph graph = new SingleGraph("XarxaGraph");

        // Itera sobre totes les entrades (nodes) a la estructura de dades xarxa
        for (Map.Entry<Aixeta, ArrayList<Aixeta>> entry : subxarxa.entrySet()) {
            Aixeta aixetaSubxarxa = entry.getKey();
            if (aixetaSubxarxa != null) {
                // Obtenir les coordenades geogràfiques de l'aixeta
                Coordenades coordenades = aixetaSubxarxa.coordenades();
                double x = coordenades.X(); // Coordenada X
                double y = coordenades.Y(); // Coordenada Y

                // Crear el node amb les coordenades com a posició
                Node node = graph.addNode(aixetaSubxarxa.nom());
                node.setAttribute("xyz", x, y, 0); // Establir les coordenades com a posició (z = 0)

                // Configuració visual del node
                String nodeStyle = "size: 20px; text-size: 16px; text-alignment: under; text-padding: 5px;";
                if (aixetaSubxarxa.estat()) {
                    nodeStyle += " fill-color: green;";
                } else {
                    nodeStyle += " fill-color: red;";
                }
                node.setAttribute("ui.style", nodeStyle);

                // Etiqueta del node
                String label = aixetaSubxarxa.nom() + " " + coordenades.toString();
                if (aixetaSubxarxa instanceof Terminal) {
                    label += "\n Demanda. Punta: " + aixetaSubxarxa.obtenirDemanda();
                    label += "\n Demanda Actual: " + aixetaSubxarxa.obtenirCabalHipotetic();
                } else if (aixetaSubxarxa instanceof Origen) {
                    label += "\n Cabal: " + ((Origen) aixetaSubxarxa).cabal();
                }
                node.setAttribute("ui.label", label);
            }
        }

        // Afegeix les arestes amb les seves etiquetes i propietats, incloent-hi les fletxes per indicar el sentit
        for (Map.Entry<Aixeta, ArrayList<Aixeta>> entry : subxarxa.entrySet()) {
            ArrayList<Aixeta> aixetes = entry.getValue();
            if (aixetes != null) {
                Aixeta clau = entry.getKey();
                for (Aixeta valor : aixetes) {
                    String edgeId = clau.nom() + "-" + valor.nom();
                    if (graph.getEdge(edgeId) == null) {
                        Canonada canonada = obtenirCanonada(clau, valor); // Obtenim la canonada entre els dos nodes
                        Edge edge;
                        if (canonada != null) {
                            // Si hi ha una canonada entre els dos nodes, l'aresta és dirigida amb la capacitat de la canonada
                            edge = graph.addEdge(edgeId, clau.nom(), valor.nom(), true);
                            edge.setAttribute("ui.style", "size: 2px; text-size: 18px;"); // Ajustaments visuals bàsics
                            edge.setAttribute("ui.label", canonada.capacitat()); // Afegim la capacitat com a etiqueta de l'aresta

                            // Afegim la direcció de l'aresta
                            if (clau == canonada.origen()) {
                                edge.setAttribute("ui.arrow1", true); // Fletxa des de l'origen cap al destí
                            } else {
                                edge.setAttribute("ui.arrow2", true); // Fletxa des del destí cap a l'origen
                            }
                        }
                    }
                }
            }
        }
        return graph;
    }

    /**
     * @return Un objecte de tipus Graph que representa la xarxa d'aigua.
     * @brief Crea un graf de GraphStream a partir de la xarxa d'aigua.
     * @pre Cap.
     * @post Retorna un objecte de tipus Graph que representa la xarxa d'aigua amb els nodes i arestes corresponents.
     */
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
                        }
                    }
                }
            }
        }
        return graph;
    }


    /**
     * @return Un ArrayList amb totes les claus de la xarxa.
     * @brief Retorna un ArrayList amb totes les claus de la xarxa.
     * @pre Cap
     * @post S'ha retornat un ArrayList amb totes les claus de la xarxa.
     */
    public ArrayList<Aixeta> keys() {
        ArrayList<Aixeta> keys = new ArrayList<>();
        for (Aixeta a : xarxa.keySet()) { // S'obte cada clau del mapa
            keys.add(a);
        }
        return keys;
    }

    /**
     * @param aixeta L'aixeta per la qual es volen obtenir els veïns.
     * @return Una llista d'Aixetes que són veïnes de l'aixeta donada.
     * @brief Retorna una llista d'Aixetes sense tenir en compte la direcció.
     * @pre L'aixeta i la xarxa no són nul·les.
     * @post S'ha retornat una llista d'Aixetes sense tenir en compte la direcció.
     */
    public ArrayList<Aixeta> veinsNoDirigit(Aixeta aixeta) throws NoSuchElementException {
        ArrayList<Aixeta> veins = new ArrayList<>();

        // Itera a través de totes les entrades del mapa xarxa
        for (Map.Entry<Aixeta, ArrayList<Aixeta>> entry : xarxa.entrySet()) {
            Aixeta aixetaClau = entry.getKey();
            ArrayList<Aixeta> conexions = entry.getValue();

            // Si l'aixeta donada es troba a les conexions de l'aixeta de la clau, afegeix l'aixeta de la clau a la llista de veïns
            if (conexions.contains(aixeta)) {
                veins.add(aixetaClau);
            }

            // També, si l'aixeta donada es troba com a clau, afegeix totes les aixetes de les conexions a la llista de veïns
            if (aixetaClau.equals(aixeta)) {
                veins.addAll(conexions);
            }
        }

        return veins;
    }

    /**
     * @param a Una de les Aixetes.
     * @param b L'altra principal.Aixeta.
     * @return Una principal.Canonada que connecta les dues Aixetes, o null si no existeix.
     * @brief Retorna una principal.Canonada que connecta dos Aixetes, o null si no existeix.
     * @pre Les Aixetes origen i destí no són nul·les.
     * @post S'ha retornat una principal.Canonada que connecta origen amb destí, o null si no existeix.
     */
    public Canonada obtenirCanonada(Aixeta a, Aixeta b) {
        ArrayList<Canonada> canonadesOrigen = a.canonades();
        for (Canonada canonada : canonadesOrigen) {
            if (canonada.origen().nom().equals(a.nom()) && canonada.desti().nom().equals(b.nom())) {
                return canonada; // Retorna la canonada que té com a destí l'aixeta destí
            }
        }
        return null;
    }

    /**
     * @param nodeOrigen El node d'origen a afegir.
     * @brief Afegeix un node d'origen a la xarxa.
     * @pre No existeix cap node amb el mateix id que nodeOrigen a la xarxa.
     * @post S'ha afegit nodeOrigen a la xarxa.
     */
    public void afegir(Origen nodeOrigen) throws IllegalArgumentException {
        ArrayList<Aixeta> veins;
        if (this.xarxa.containsKey(nodeOrigen)) {
            throw new IllegalArgumentException("El node d'origen ja existeix.");
        }
        veins = new ArrayList<>();
        this.xarxa.put(nodeOrigen, veins);
    }

    /**
     * @param nodeOrigen El node d'origen a afegir a la xarxa.
     * @param llista     La llista d'adjacències associada al node d'origen.
     * @brief Afegeix un node d'origen amb la seva llista d'adjacències a la xarxa si encara no existeix.
     * @pre El node d'origen i la llista no són nulls.
     * @post Si el node d'origen no existeix a la xarxa, s'afegeix juntament amb la seva llista d'adjacències.
     */
    public void afegir(Origen nodeOrigen, ArrayList<Aixeta> llista) throws IllegalArgumentException {
        if (this.xarxa.containsKey(nodeOrigen)) {
            throw new IllegalArgumentException("El node d'origen ja existeix.");
        }
        this.xarxa.put(nodeOrigen, llista);
    }

    /**
     * @param nodeTerminal El node terminal a afegir.
     * @brief Afegeix un node terminal a la xarxa.
     * @pre No existeix cap node amb el mateix id que nodeTerminal a la xarxa.
     * @post S'ha afegit nodeTerminal a la xarxa.
     */
    public void afegir(Terminal nodeTerminal)  throws IllegalArgumentException{
        if (this.xarxa.containsKey(nodeTerminal)) {
            throw new IllegalArgumentException("El node terminal ja existeix.");
        }
        ArrayList<Aixeta> veins = new ArrayList<>();
        this.xarxa.put(nodeTerminal, veins);
    }

    /**
     * @param nodeTerminal El node terminal a afegir a la xarxa.
     * @param llista       La llista d'adjacències associada al node terminal.
     * @brief Afegeix un node terminal amb la seva llista d'adjacències a la xarxa si encara no existeix.
     * @pre El node terminal i la llista no són nulls.
     * @post Si el node terminal no existeix a la xarxa, s'afegeix juntament amb la seva llista d'adjacències.
     */
    public void afegir(Terminal nodeTerminal, ArrayList<Aixeta> llista) throws IllegalArgumentException{
        if (this.xarxa.containsKey(nodeTerminal)) {
           throw new IllegalArgumentException("El node terminal ja existeix.");
        }
        this.xarxa.put(nodeTerminal, llista);
    }

    /**
     * @param nodeConnexio El node de connexió a afegir.
     * @brief Afegeix un node de connexió a la xarxa.
     * @pre No existeix cap node amb el mateix id que nodeConnexio a la xarxa.
     * @post S'ha afegit nodeConnexio a la xarxa.
     */
    public void afegir(Connexio nodeConnexio) throws IllegalArgumentException{
        ArrayList<Aixeta> veins;
        if (this.xarxa.containsKey(nodeConnexio)) {
           throw new IllegalArgumentException("El node de connexió ja existeix.");
        }
        veins = new ArrayList<>();
        this.xarxa.put(nodeConnexio, veins);
    }

    /**
     * @param nodeConnexio El node de connexió a afegir.
     * @param llista       La llista de veïns del node de connexió.
     * @brief Afegeix un node de connexió amb una llista de veïns a la xarxa.
     * @pre No existeix cap node amb el mateix id que nodeConnexio a la xarxa.
     * @post S'ha afegit nodeConnexio a la xarxa.
     */
    public void afegir(Connexio nodeConnexio, ArrayList<Aixeta> llista) throws IllegalArgumentException{
        if (this.xarxa.containsKey(nodeConnexio)) {
            throw new IllegalArgumentException("El node de connexió ja existeix.");
        }
        this.xarxa.put(nodeConnexio, llista);
    }

    /**
     * @param a L'aixeta per la qual es vol obtenir la subxarxa connexa.
     * @return Una llista que conté la subxarxa connexa on pertany a.
     * @brief Retorna una llista que conté la subxarxa connexa on pertany a.
     * @pre L'aixeta 'a' no és nul·la.
     * @post Retorna una llista que conté la subxarxa connexa on pertany a.
     */
    public ArrayList<Aixeta> clausSubxarxa(Aixeta a) throws NoSuchElementException {
        ArrayList<Aixeta> keys = new ArrayList<>();
        clausSubxarxaRec(a, keys);
        return new ArrayList<>(keys);
    }

    /**
     * @param aixeta L'aixeta a explorar.
     * @param keys   La llista on es guardaran les claus de la subxarxa connexa.
     * @brief Mètode recursiu auxiliar per a obtenir les claus de la subxarxa connexa.
     * @pre L'aixeta 'aixeta' no és nul·la.
     * @post S'ha actualitzat la llista 'keys' amb les claus de la subxarxa connexa.
     */
    private void clausSubxarxaRec(Aixeta aixeta, ArrayList<Aixeta> keys) throws NoSuchElementException {
        boolean trobat = false;
        for (Aixeta element : keys) {
            if (element.nom().equals(aixeta.nom())) {
                trobat = true;
                break;
            }
        }
        if (!trobat) {
            keys.add(aixeta);
            for (Aixeta element : veinsNoDirigit(aixeta)) {
                clausSubxarxaRec(element, keys);
            }
        }
    }

    /**
     * @param a L'aixeta per la qual es volen obtenir les aixetes d'entrada.
     * @return Una llista d'Aixetes que entren a l'aixeta donada.
     * @brief Retorna una llista d'Aixetes amb les aixetes que entren a l'aixeta donada.
     * @pre L'aixeta 'a' no és nul·la.
     * @post Retorna una llista d'Aixetes amb les aixetes que entren a l'aixeta donada.
     */
    public ArrayList<Aixeta> aixetesEntrada(Aixeta a) throws NoSuchElementException {
        ArrayList<Aixeta> aixetes = new ArrayList<>();
        ArrayList<Canonada> cononades = a.canonades();
        for (Canonada c : cononades) {
            if (c.desti().equals(a)) {
                aixetes.add(c.origen());
            }
        }
        return aixetes;
    }

    /**
     * @param a L'aixeta per la qual es volen obtenir els veïns.
     * @return Una llista d'Aixetes que són veïnes a l'aixeta donada.
     * @brief Retorna una llista d'Aixetes que són veïnes a l'aixeta donada, tenint en compte la direcció de les canonades.
     * @pre Cap
     * @post Retorna una llista d'Aixetes que són veïnes a l'aixeta donada.
     */
    ArrayList<Aixeta> veins(Aixeta a) throws NoSuchElementException {
        return this.xarxa.computeIfAbsent(a, k -> new ArrayList<>());
    }

    /**
     * @param a L'aixeta a eliminar.
     * @brief Elimina una aixeta de la xarxa.
     * @pre Cap
     * @post L'aixeta 'a' ha estat eliminada de la xarxa.
     */
    public void eliminarAixeta(Aixeta a) throws NoSuchElementException{
        if(!xarxa.containsKey(a)){
            throw new NoSuchElementException("L'aixeta no existeix.");
        }
        xarxa.remove(a);
    }

    /**
     * @param a La primera aixeta de la canonada.
     * @param b La segona aixeta de la canonada.
     * @brief Elimina la canonada entre dues aixetes.
     * @pre Existeix la canonada entre les aixetes 'a' i 'b'.
     * @post S'ha eliminat la canonada entre les aixetes 'a' i 'b'.
     */
    public void eliminarCanonada(Aixeta a, Aixeta b) {
        Canonada c = obtenirCanonada(a, b);
        c.origen().eliminarCanonada(c);
        c.desti().eliminarCanonada(c);
        ArrayList<Aixeta> aixetesA = xarxa.get(a);
        ArrayList<Aixeta> aixetesB = xarxa.get(b);
        aixetesA.remove(b);
        aixetesB.remove(a);
    }

    /**
     * @param a L'aixeta a convertir en connexió.
     * @return La connexió generada a partir de l'aixeta.
     * @brief Converteix una aixeta en una connexió.
     */
    public Connexio convertirConnexio(Aixeta a) {
        //ArrayList<principal.Canonada> llistaC = a.canonades();
        ArrayList<Canonada> llistaC = new ArrayList<>();
        llistaC.addAll(a.canonades());
        Connexio connexio = new Connexio(a.nom(), a.coordenades(), llistaC);
        return connexio;
    }

    /**
     * @param origen    L'aixeta d'origen de la nova canonada.
     * @param desti     L'aixeta de destí de la nova canonada.
     * @param capacitat La capacitat de la nova canonada.
     * @brief Connecta dos nodes amb una nova canonada.
     * @pre node1 i node2 pertanyen a la xarxa, no estan connectats, i node1 no és un node terminal.
     * @post S'han connectat els nodes amb una canonada de capacitat c, amb sentit de l'aigua de node1 a node2.
     */
    public void connectarAmbCanonada(Aixeta origen, Aixeta desti, float capacitat) {
        ArrayList<Aixeta> conexionsAixeta1 = xarxa.computeIfAbsent(origen, k -> new ArrayList<>());
        conexionsAixeta1.add(desti);
        Canonada c = new Canonada(origen, desti, capacitat);
        origen.afegirCanonada(c);
        desti.afegirCanonada(c);
    }


    /**
     * @param origen    L'aixeta d'origen de la connexió.
     * @param desti     L'aixeta de destinació de la connexió.
     * @param capacitat La capacitat de la connexió.
     * @brief Connecta dos nodes d'origen amb una connexió i un canal si encara no estan connectats.
     * @pre L'aixeta d'origen i l'aixeta de destinació no són nulls.
     * @post S'ha creat una connexió entre l'aixeta d'origen i l'aixeta de destinació amb la capacitat especificada.
     */
    public void connectarOrigens(Aixeta origen, Aixeta desti, float capacitat) {
        Connexio connexio = convertirConnexio(desti);
        ArrayList<Aixeta> adjecents = xarxa.get(desti);
        ArrayList<Aixeta> copiaAdjecents = new ArrayList<>(adjecents);
        eliminarAixeta(desti);
        afegir(connexio, copiaAdjecents);
        connectarAmbCanonada(origen, connexio, capacitat);

        //subxarxa.put(element, nuevaLista);
    }

    /**
     * @param origen    L'aixeta d'origen de la connexió.
     * @param desti     L'aixeta de destinació de la connexió.
     * @param capacitat La capacitat de la connexió.
     * @brief Connecta dos nodes terminals amb una connexió i un canal si encara no estan connectats.
     * @pre L'aixeta d'origen i l'aixeta de destinació no són nulls.
     * @post S'ha creat una connexió entre l'aixeta d'origen i l'aixeta de destinació amb la capacitat especificada.
     */
    public void connectarTerminals(Aixeta origen, Aixeta desti, float capacitat) {
        Connexio connexio = convertirConnexio(origen);
        ArrayList<Aixeta> adjecents = xarxa.get(origen);
        ArrayList<Aixeta> copiaAdjecents = new ArrayList<>(adjecents);
        eliminarAixeta(origen);
        afegir(connexio, copiaAdjecents);
        //System.out.println(connexio);
        connectarAmbCanonada(connexio, desti, capacitat);
    }

    /**
     * @param t   El node terminal al qual s'afegeix l'abonat.
     * @param dni El DNI del nou abonat.
     * @brief Afegeix un abonat a un node terminal.
     * @pre La xarxa no té cicles.
     * @post S'ha afegit l'abonat amb dni a la llista d'abonats de t.
     */
    public void abonar(Terminal t, String dni) throws IllegalArgumentException, NoSuchElementException{
        if(t.obtenirAbonats().contains(dni)){
            throw new IllegalArgumentException("L'abonat ja existeix.");
        }
       if(!xarxa.containsKey(t)) {
           throw new NoSuchElementException("La terminal no existeix.");
       }
        t.afegirAbonat(dni);
    }

    /**
     * @param o     L'origen a analitzar.
     * @param cabal El cabal mínim per satisfer la demanda.
     * @brief Determina la demanda mínima que ha de tenir un origen per satisfer la demanda d'una terminal.
     * @pre La xarxa no té cicles.
     * @post S'ha calculat el cabal mínim que hauria de tenir cada origen per tal que cap terminal rebi menys d'un determinat % de la seva demanda actual.
     */
    public void determinarCabal(Origen o, float cabal) throws IllegalArgumentException {
        if(cabal<0){
            throw new IllegalArgumentException("El cabal no pot ser negatiu");
        }
        o.determinarCabal(cabal);
    }

    /**
     * @param t       La terminal a analitzar.
     * @param demanda La demanda mínima que ha de tenir la terminal.
     * @brief Determina la demanda mínima que ha de tenir una terminal.
     * @pre La xarxa no té cicles.
     * @post S'ha calculat el cabal mínim que hauria de tenir cada origen per tal que cap terminal rebi menys d'un determinat % de la seva demanda actual.
     */
    public void determinarDemanda(Terminal t, float demanda) throws IllegalArgumentException{
        if(demanda<0){
            throw new IllegalArgumentException("La demanda no pot ser negativa");
        }
        t.determinarDemanda(demanda);
    }

    /**
     * @param aixeta L'aixeta a tancar.
     * @brief Tanca una aixeta de la xarxa.
     * @pre Cap
     * @post Si a existeix a la xarxa, tanca l'aixeta corresponent.
     */
    public void tancarAixeta(Aixeta aixeta, GestorXarxa gestor) {
        if (xarxa.containsKey(aixeta) && aixeta.estat()) {
            aixeta.tancarAixeta();
            gestor.historialModificacions(aixeta);
        }
    }

    /**
     * @param aixeta L'aixeta a obrir.
     * @brief Obre una aixeta de la xarxa.
     * @pre Cap
     * @post Si a existeix a la xarxa, obre l'aixeta corresponent.
     */
    public void obrirAixeta(Aixeta aixeta, GestorXarxa gestor) {
        if (xarxa.containsKey(aixeta) && !aixeta.estat()) {
            aixeta.obrirAixeta();
            gestor.historialModificacions(aixeta);
        }
    }

    /**
     * @return El mapa de la xarxa.
     * @brief Retorna el mapa de la xarxa.
     */
    public Map<Aixeta, ArrayList<Aixeta>> mapa() {
        return xarxa;
    }

    /**
     * @param a El nom de l'aixeta a buscar.
     * @return L'aixeta amb el nom especificat; si no existeix cap aixeta amb aquest nom, retorna null.
     * @brief Retorna l'aixeta amb el nom especificat.
     * @pre Cap
     * @post Retorna l'principal.Aixeta amb el nom especificat; si no existeix cap aixeta amb aquest nom, retorna null.
     */
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
    public Terminal terminalAbonat(String dni) {
        boolean trobat = false;
        Terminal abonat = null;
        ArrayList<Aixeta> clausSubxarxa = keys();
        Iterator<Aixeta> iteradorAixetes = clausSubxarxa.iterator();
        while (iteradorAixetes.hasNext() && !trobat) {
            Aixeta aixeta = iteradorAixetes.next();
            if (aixeta instanceof Terminal terminal) {
                ArrayList<String> abonats = terminal.obtenirAbonats();
                Iterator<String> iteradorAbonats = abonats.iterator();
                while (iteradorAbonats.hasNext() && !trobat) {
                    if (iteradorAbonats.next().equals(dni)) {
                        trobat = true;
                        abonat = terminal;
                    }
                }

            }
        }
        return abonat;
    }
}