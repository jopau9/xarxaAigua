/**
 * @file controladorPrincipal.java
 * @brief Classe ControladorPrincipal que gestiona les interaccions de l'usuari amb la interfície gràfica principal.
 * Aquest controlador controla les accions i mostra la informació a la finestra principal de l'aplicació.
 * És responsable de la gestió de la xarxa d'aigua i de les diverses operacions que es poden realitzar sobre ella.
 * Aquest fitxer conté la implementació del controlador.
 * @author Meritxell Masdevall i Esparch
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.graphstream.graph.Graph;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

import java.io.*;
import java.util.*;

public class controladorPrincipal{

    private GestorXarxa gestor; ///< Gestor de la xarxa
    private XarxaAigua xarxa; ///< Xarxa d'aigua
    private ArrayList<Aixeta> aixetes; ///< Llista d'aixetes connectades a la xarxa
    private PrintStream fitxerSortida; ///< Fitxer de sortida per a les dades

    @FXML
    private Pane espaiGrafic; ///< Espai gràfic de l'aplicació

    @FXML
    private TextArea pantallaSortida; ///< Pantalla de sortida per a missatges i informació

    /**
     * @brief Inicialitza el controlador.
     * @pre Cert
     * @post S'inicialitzen les variables i es deshabilita l'edició del TextArea.
     */
    public void initialize() {
        xarxa = new XarxaAigua();
        gestor = new GestorXarxa();
        aixetes = new ArrayList<>();
        pantallaSortida.setEditable(false);
    }

    /**
     * @brief Llegeix un fitxer i executa les opcions especificades a cada línia.
     * @param fitxerIn El fitxer a llegir.
     * @param fitxerOut El nom del fitxer de sortida.
     * @throws FileNotFoundException Si el fitxer no es troba.
     * @pre fitxer != null
     * @post Les opcions del fitxer s'han executat.
     */
    public void setFitxersSeleccionats(File fitxerIn, PrintStream fitxerOut) throws FileNotFoundException {
        if (fitxerIn != null) {
            Scanner scanner = new Scanner(fitxerIn);
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
                Main.opcions(linea, gestor, xarxa, scanner, fitxerOut);
            }
            scanner.close();
            aixetes = xarxa.keys();
            dibuixar(xarxa, false, aixetes.getFirst());
        }
        fitxerSortida=fitxerOut;
    }

    /**
     * @brief Dibuixa la xarxa d'aigua en un panell gràfic.
     * @param xarxa La xarxa d'aigua a dibuixar.
     * @param maxFlow Indica si s'ha de calcular el flux màxim de la xarxa.
     * @param aixeta L'aixeta específica de la qual es vol mostrar el gràfic (només si maxFlow és fals).
     * @pre xarxa != null
     * @post La xarxa s'ha dibuixat en el panell gràfic.
     */
    public void dibuixar(XarxaAigua xarxa, boolean maxFlow, Aixeta aixeta){
        Graph grafic;
        if (maxFlow){
            grafic = xarxa.crearGraphStream();
        }
        else {
            grafic = xarxa.crearGraphStream(aixeta);
        }
        Viewer viewer = new FxViewer(grafic, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        View vista = viewer.addDefaultView(false);
        viewer.enableAutoLayout();
        espaiGrafic.getChildren().add((Node) vista);
    }

    /**
     * @brief Mostra un text de sortida en la pantalla.
     * @param sortida El text a mostrar.
     * @pre sortida != null
     * @post El text de sortida s'ha mostrat a la pantalla.
     */
    public void mostrarSortida(String sortida) {
        pantallaSortida.appendText(sortida + "\n");
        fitxerSortida.println(sortida+ "\n");
    }

    /**
     * @brief Crea una nova finestra amb el contingut especificat.
     * @param fitxer El fitxer FXML que conté el contingut de la finestra.
     * @param titol El títol de la finestra.
     * @pre fitxer != null, titol != null
     * @post S'ha creat una nova finestra amb el contingut especificat.
     */
    public void novaFinestra(String fitxer, String titol){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fitxer));
            Parent root = loader.load();
            Object controller = loader.getController();
            if (controller instanceof controladorNovaAixeta) {
                ((controladorNovaAixeta) controller).setParentController(this);
            } else if (controller instanceof controladorNovaConnex) {
                ((controladorNovaConnex) controller).setParentController(this);
            } else if (controller instanceof controladorNouTerminal) {
                ((controladorNouTerminal) controller).setParentController(this);
            } else if (controller instanceof controladorConnectar) {
                ((controladorConnectar) controller).setParentController(this);
            } else if (controller instanceof controladorObrir) {
                ((controladorObrir) controller).setParentController(this);
            } else if (controller instanceof controladorTancar) {
                ((controladorTancar) controller).setParentController(this);
            } else if (controller instanceof controladorAbonar) {
                ((controladorAbonar) controller).setParentController(this);
            } else if (controller instanceof controladorCabalOrigen) {
                ((controladorCabalOrigen) controller).setParentController(this);
            } else if (controller instanceof controladorDemandaT) {
                ((controladorDemandaT) controller).setParentController(this);
            } else if (controller instanceof controladorCicles) {
                ((controladorCicles) controller).setParentController(this);
            } else if (controller instanceof controladorArbre) {
                ((controladorArbre) controller).setParentController(this);
            } else if (controller instanceof controladorCabalMinim) {
                ((controladorCabalMinim) controller).setParentController(this);
            } else if (controller instanceof controladorExces) {
                ((controladorExces) controller).setParentController(this);
            } else if (controller instanceof controladorFluxMaxim) {
                ((controladorFluxMaxim) controller).setParentController(this);
            } else if (controller instanceof controladorProximitat) {
                ((controladorProximitat) controller).setParentController(this);
            } else if (controller instanceof controladorCabalAbonat) {
                ((controladorCabalAbonat) controller).setParentController(this);
            } else if (controller instanceof controladorDibuixar) {
                ((controladorDibuixar) controller).setParentController(this);
            }
            Stage stage = new Stage();
            stage.setTitle(titol);
            stage.setScene(new Scene(root));
            Image icona = new Image(getClass().getResourceAsStream("icona.png"));
            stage.getIcons().add(icona);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * @brief Obrir una finestra per afegir un origen a la xarxa.
     */
    @FXML
    void afegirOrigen() {
        novaFinestra("afegirOrigen.fxml","Afegir origen");
    }

    /**
     * @brief Obrir una finestra per afegir una connexió a la xarxa.
     */
    @FXML
    void afegirConnexio() {
        novaFinestra("afegirConnexio.fxml", "Afegir connexió");
    }

    /**
     * @brief Obrir una finestra per afegir un terminal a la xarxa.
     */
    @FXML
    void afegirTerminal() {
        novaFinestra("afegirTerminal.fxml", "Afegir terminal");
    }

    /**
     * @brief Obrir una finestra per connectar dues aixetes de la xarxa.
     * @pre aixetes != null && !aixetes.isEmpty()
     * @post S'obre una finestra per connectar dues aixetes.
     */
    @FXML
    void Bconnectar() {
        if(aixetes == null || aixetes.isEmpty()) {
            mostrarAlerta("La xarxa no té cap aixeta");
        }
        else {
            novaFinestra("connectar.fxml", "Connectar dues aixetes");
        }
    }

    /**
     * @brief Obrir una finestra per obrir una aixeta de la xarxa.
     * @pre aixetes != null && !aixetes.isEmpty()
     * @post S'obre una finestra per obrir una aixeta.
     */
    @FXML
    void obrirAixeta() {
        if(aixetes == null || aixetes.isEmpty()) {
            mostrarAlerta("La xarxa no té cap aixeta");
        }
        else {
            novaFinestra("obrir.fxml", "Obrir aixeta");
        }
    }

    /**
     * @brief Obrir una finestra per tancar una aixeta de la xarxa.
     * @pre aixetes != null && !aixetes.isEmpty()
     * @post S'obre una finestra per tancar una aixeta.
     */
    @FXML
    void tancarAixeta() {
        if(aixetes == null || aixetes.isEmpty()) {
            mostrarAlerta("La xarxa no té cap aixeta");
        }
        else {
            novaFinestra("tancar.fxml", "Tancar aixeta");
        }
    }

    /**
     * @brief Obrir una finestra per abonar una aixeta de la xarxa.
     * @pre aixetes != null && !aixetes.isEmpty()
     * @post S'obre una finestra per abonar una aixeta.
     */
    @FXML
    void abonar(){
        if(aixetes == null || aixetes.isEmpty()) {
            mostrarAlerta("La xarxa no té cap aixeta");
        }
        else {
            novaFinestra("abonar.fxml", "Abonar");
        }
    }

    /**
     * @brief Obrir una finestra per establir el cabal d'un origen de la xarxa.
     * @pre aixetes != null && !aixetes.isEmpty()
     * @post S'obre una finestra per establir el cabal d'un origen.
     */
    @FXML
    void establirCabal() {
        if(aixetes == null || aixetes.isEmpty()) {
            mostrarAlerta("La xarxa no té cap aixeta");
        }
        else {
            novaFinestra("cabalO.fxml", "Establir cabal en un origen");
        }
    }

    /**
     * @brief Obrir una finestra per establir la demanda d'un terminal de la xarxa.
     * @pre aixetes != null && !aixetes.isEmpty()
     * @post S'obre una finestra per establir la demanda d'un terminal.
     */
    @FXML
    void establirDemanda() {
        if(aixetes == null || aixetes.isEmpty()) {
            mostrarAlerta("La xarxa no té cap aixeta");
        }
        else {
            novaFinestra("demanda.fxml","Establir demanda en un terminal");
        }
    }

    /**
     * @brief Obrir una finestra per verificar si la xarxa té cicles.
     * @pre aixetes != null && !aixetes.isEmpty()
     * @post S'obre una finestra per verificar si la xarxa té cicles.
     */
    @FXML
    void cicles(){
        if(aixetes == null || aixetes.isEmpty()) {
            mostrarAlerta("La xarxa no té cap aixeta");
        }
        else {
            novaFinestra("cicles.fxml","La xarxa té cicles?");
        }
    }

    /**
     * @brief Obrir una finestra per verificar si la xarxa és un arbre.
     * @pre aixetes != null && !aixetes.isEmpty()
     * @post S'obre una finestra per verificar si la xarxa és un arbre.
     */
    @FXML
    void arbres(){
        if(aixetes == null || aixetes.isEmpty()) {
            mostrarAlerta("La xarxa no té cap aixeta");
        }
        else {
            novaFinestra("arbre.fxml","La xarxa és arbre?");
        }
    }

    /**
     * @brief Obrir una finestra per establir el cabal mínim d'un origen de la xarxa.
     * @pre aixetes != null && !aixetes.isEmpty()
     * @post S'obre una finestra per establir el cabal mínim d'un origen.
     */
    @FXML
    void cabalMin(){
        if(aixetes == null || aixetes.isEmpty()) {
            mostrarAlerta("La xarxa no té cap aixeta");
        }
        else {
            novaFinestra("cabalMin.fxml","Cabal mínim en un origen");
        }
    }

    /**
     * @brief Obrir una finestra per calcular l'excés de cabal en els elements de la xarxa.
     * @pre aixetes != null && !aixetes.isEmpty()
     * @post S'obre una finestra per calcular l'excés de cabal.
     */
    @FXML
    void excesCabal(){
        if(aixetes == null || aixetes.isEmpty()) {
            mostrarAlerta("La xarxa no té cap aixeta");
        }
        else {
            novaFinestra("excesC.fxml","Excés de cabal");
        }
    }

    /**
     * @brief Obrir una finestra per calcular el flux màxim d'una xarxa.
     * @pre aixetes != null && !aixetes.isEmpty()
     * @post S'obre una finestra per calcular el flux màxim d'una xarxa.
     */
    @FXML
    void fluxMaxim(){
        if(aixetes == null || aixetes.isEmpty()) {
            mostrarAlerta("La xarxa no té cap aixeta");
        }
        else {
            novaFinestra("maxFlow.fxml","Flux màxim d'una xarxa");
        }
    }

    /**
     * @brief Obrir una finestra per llistar els elements de la xarxa segons proximitat geogràfica.
     * @pre aixetes != null && !aixetes.isEmpty()
     * @post S'obre una finestra per llistar segons proximitat geogràfica.
     */
    @FXML
    void proximitat(){
        if(aixetes == null || aixetes.isEmpty()) {
            mostrarAlerta("La xarxa no té cap aixeta");
        }
        else {
            novaFinestra("proximitat.fxml","Llistar segons proximitat geogràfica");
        }
    }

    /**
     * @brief Obrir una finestra per establir el cabal en un punt terminal de la xarxa.
     * @pre aixetes != null && !aixetes.isEmpty()
     * @post S'obre una finestra per establir el cabal en un punt terminal.
     */
    @FXML
    void cabalAbonat(){
        if(aixetes == null || aixetes.isEmpty()) {
            mostrarAlerta("La xarxa no té cap aixeta");
        }
        else {
            novaFinestra("cabalAbonat.fxml","Cabal en un punt terminal");
        }
    }

    /**
     * @brief Obrir una finestra per mostrar quines aixetes cal tancar.
     * @pre aixetes != null && !aixetes.isEmpty()
     * @post S'obre una finestra per mostrar quines aixetes cal tancar.
     */
    @FXML
    void situacio(){
        if(aixetes == null || aixetes.isEmpty()) {
            mostrarAlerta("La xarxa no té cap aixeta");
        }
        else {
            novaFinestra("situacio.fxml","Aixetes a tancar");
        }
    }

    /**
     * @brief Obrir una finestra per dibuixar una subxarxa de la xarxa.
     * @pre aixetes != null && !aixetes.isEmpty()
     * @post S'obre una finestra per dibuixar una subxarxa.
     */
    @FXML
    void dibuixarAixeta(){
        if(aixetes == null || aixetes.isEmpty()) {
            mostrarAlerta("La xarxa no té cap aixeta");
        }
        else {
            novaFinestra("dibuixar.fxml","Dibuixar subxarxa");
        }
    }


    /**
     * @brief Afegeix un origen a la xarxa.
     * @param o L'origen a afegir.
     * @pre o != null
     * @post L'origen és afegit a la xarxa i es mostra un missatge de sortida.
     */
    public void afegirO(Origen o) {
        xarxa.afegir(o);
        aixetes.add(o);
        dibuixar(xarxa,false,o);
        mostrarSortida("S'ha afegit l'origen " + o.nom()+".");
    }

    /**
     * @brief Afegeix una connexió a la xarxa.
     * @param c La connexió a afegir.
     * @pre c != null
     * @post La connexió és afegida a la xarxa i es mostra un missatge de sortida.
     */
    public void afegirC(Connexio c) {
        xarxa.afegir(c);
        aixetes.add(c);
        dibuixar(xarxa,false,c);
        mostrarSortida("S'ha afegit la connexió " + c.nom()+".");
    }

    /**
     * @brief Afegeix un terminal a la xarxa.
     * @param t El terminal a afegir.
     * @pre t != null
     * @post El terminal és afegit a la xarxa i es mostra un missatge de sortida.
     */
    public void afegirT(Terminal t) {
        xarxa.afegir(t);
        aixetes=xarxa.keys();
        dibuixar(xarxa,false,t);
        mostrarSortida("S'ha afegit el terminal " + t.nom()+".");
    }

    /**
     * @brief Connecta dues aixetes de la xarxa.
     * @param origen    El nom de l'aixeta d'origen.
     * @param desti     El nom de l'aixeta de destí.
     * @param capacitat La capacitat de la connexió.
     * @pre origen != null && desti != null && capacitat > 0
     * @post Es connecten les dues aixetes especificades i es mostra un missatge de sortida.
     */
    public void connectar(String origen, String desti, Float capacitat){
        Aixeta Aorigen = xarxa.aixeta(origen);
        Aixeta Adesti = xarxa.aixeta(desti);
        if(Adesti instanceof Origen){
            xarxa.connectarOrigens(Aorigen,Adesti,capacitat);
        }
        else{
            xarxa.connectarAmbCanonada(Aorigen, Adesti, capacitat);
        }
        dibuixar(xarxa,false,Aorigen);
        mostrarSortida("S'han connectat les dues aixetes " + Aorigen.nom() + " i " + Adesti.nom()+".");
    }

    /**
     * @brief Obre una aixeta de la xarxa.
     * @param nom El nom de l'aixeta a obrir.
     * @pre nom != null
     * @post L'aixeta especificada és oberta i es mostra un missatge de sortida.
     */
    public void obrir(String nom){
        Aixeta a = xarxa.aixeta(nom);
        xarxa.obrirAixeta(a, gestor);
        dibuixar(xarxa,false,a);
        mostrarSortida("S'ha obert l'aixeta "+ a.nom()+".");
    }

    /**
     * @brief Tanca una aixeta de la xarxa.
     * @param nom El nom de l'aixeta a tancar.
     * @pre nom != null
     * @post L'aixeta especificada és tancada i es mostra un missatge de sortida.
     */
    public void tancar(String nom){
        Aixeta a = xarxa.aixeta(nom);
        xarxa.tancarAixeta(a, gestor);
        dibuixar(xarxa,false,a);
        mostrarSortida("S'ha tancat l'aixeta " + a.nom()+".");
    }

    /**
     * @brief Abona un client a un terminal de la xarxa.
     * @param dni El DNI del client a abonar.
     * @param nomT El nom del terminal al qual s'abonarà el client.
     * @pre dni != null && nomT != null
     * @post El client amb el DNI especificat queda abonat al terminal especificat i es mostra un missatge de sortida.
     */
    public void abonarC(String dni, String nomT){
        Terminal t = (Terminal) xarxa.aixeta(nomT);
        xarxa.abonar(t, dni);
        mostrarSortida("S'ha abonat el client " + dni + " al terminal " + t.nom()+".");
    }

    /**
     * @brief Estableix el cabal d'un origen de la xarxa.
     * @param nomO El nom de l'origen al qual s'establirà el cabal.
     * @param cabal El valor del cabal a establir.
     * @pre nomO != null && cabal > 0
     * @post Es determina el cabal de l'origen especificat i es mostra un missatge de sortida.
     */
    public void establirC(String nomO, Float cabal){
        Aixeta a = xarxa.aixeta(nomO);
        Origen o = (Origen) a;
        xarxa.determinarCabal(o, cabal);
        dibuixar(xarxa,false,a);
        mostrarSortida("S'ha establert el cabal de l'origen " + o.nom() + " a " + cabal + "l/s.");
    }

    /**
     * @brief Estableix la demanda d'un terminal de la xarxa.
     * @param nomT El nom del terminal al qual s'establirà la demanda.
     * @param demanda El valor de la demanda a establir.
     * @pre nomT != null && demanda > 0
     * @post Es determina la demanda del terminal especificat i es mostra un missatge de sortida.
     */
    public void establirD(String nomT, Float demanda){
        Aixeta a = xarxa.aixeta(nomT);
        Terminal t = (Terminal) a;
        xarxa.determinarDemanda(t, demanda);
        dibuixar(xarxa,false,a);
        mostrarSortida("S'ha establert la demanda del terminal " + t.nom() + " a " + demanda + "l/s.");
    }

    /**
     * @brief Comprova si una xarxa té cicles.
     * @param nomA El nom de la xarxa a comprovar.
     * @pre nomA != null
     * @post Es mostra un missatge informant si la xarxa té cicles o no.
     */
    public void teCicles(String nomA){
        if (gestor.teCicles(xarxa,nomA, true)) {
            mostrarSortida("La xarxa de " + nomA + " té cicles.");
        }
        else{
            mostrarSortida("La xarxa de " + nomA + " no té cicles.");
        }
    }

    /**
     * @brief Comprova si una xarxa és un arbre.
     * @param nomA El nom de la xarxa a comprovar.
     * @pre nomA != null
     * @post Es mostra un missatge informant si la xarxa és un arbre o no.
     */
    public void esArbre(String nomA){
        if (gestor.formaArbre(xarxa,nomA)) {
            mostrarSortida("La xarxa de " + nomA + " és un arbre.");
        }
        else{
            mostrarSortida("La xarxa de " + nomA + " no és un arbre.");
        }
    }

    /**
     * @brief Calcula el cabal mínim necessari per satisfer un percentatge de demanda en un origen.
     * @param nomA El nom de l'origen.
     * @param percent El percentatge de demanda a satisfer (en format decimal).
     * @pre nomA != null && percent > 0
     * @post Es calcula el cabal mínim per satisfer el percentatge de demanda especificat i es mostra un missatge de sortida.
     */
    public void cabalMinim(String nomA, float percent){
        Origen o = (Origen) xarxa.aixeta(nomA);
        float cabal = gestor.cabalMinim(xarxa,o,percent);
        float p = percent*100;
        float pTruncat = (float) Math.floor(p * 100) / 100;
        float cTruncat = (float) Math.floor(cabal * 100) / 100;
        mostrarSortida("El cabal mínim a " + nomA +" per satisfer el " + pTruncat + "% de la demanda \nsón " + cTruncat + "l/s.");
    }

    /**
     * @brief Comprova quines canonades tenen excés de cabal.
     * @param canonades La llista de canonades a comprovar.
     * @pre canonades != null
     * @post Es mostra un missatge informant les canonades amb excés de cabal, si n'hi ha.
     */
    public  void excesC(ArrayList<String> canonades){
        Set<Canonada> fCanonades = new HashSet<>();
        for (String can : canonades){
            String[] parts = can.split("-");
            String aOrigen = parts[0];
            String aDesti = parts[1];
            Aixeta origen = xarxa.aixeta(aOrigen);
            Aixeta desti = xarxa.aixeta(aDesti);
            Canonada c = xarxa.obtenirCanonada(origen, desti);
            if (c != null) {
                fCanonades.add(c);
            }
        }
        Set<Canonada> resultat = gestor.excesCabal(xarxa,fCanonades);
        if (resultat.size()>1){
            mostrarSortida("Les següents canonades tenen excés de cabal:");
            for (Canonada c : resultat) {
                mostrarSortida(c.origen().nom() + "-" + c.desti().nom());
            }
        }
        else if (resultat.size()==1) {
            Canonada aux = resultat.iterator().next();
            mostrarSortida("La canonada " + aux.origen().nom() + "-" +aux.desti().nom() + " té excés de cabal.");
        }

    }

    /**
     * @brief Calcula el flux màxim d'una xarxa des d'una aixeta.
     * @param nom El nom de l'aixeta des d'on es calcularà el flux màxim.
     * @pre nom != null
     * @post Es calcula el flux màxim i es mostra un missatge de sortida.
     */
    public void maxFlow(String nom){
        Aixeta a = xarxa.aixeta(nom);
        XarxaAigua xAux = new XarxaAigua(xarxa.subxarxa(a));
        float fluxMaxim = 0;
        gestor.evitarAntiparaleles(xAux);
        gestor.superAixetes(xAux);
        gestor.canonadesTornada(xAux);
        dibuixar(xAux,true,a);
        mostrarSortida("El flux màxim és " + fluxMaxim);
    }

    /**
     * @brief Calcula les aixetes més properes a unes coordenades donades.
     * @param c Les coordenades a partir de les quals es calcularà la proximitat.
     * @param aixetes La llista d'aixetes a considerar.
     * @pre c != null && aixetes != null
     * @post Es mostra un missatge amb les aixetes més properes a les coordenades especificades.
     */
    public void proximitatA(Coordenades c, ArrayList<String> aixetes){
        ArrayList<String> resultat = gestor.proximitat(xarxa, c, aixetes);
        mostrarSortida("Proximitat a " + c + ":");
        for (String nom : resultat) {
            mostrarSortida(nom);
        }
    }

    /**
     * @brief Calcula el cabal abonat a un terminal associat a un client.
     * @param dni El DNI del client.
     * @pre dni != null
     * @post Es calcula i es mostra el cabal abonat al terminal del client especificat.
     */
    public void cabalA(String dni){
        float resultat = gestor.cabalAbonat(xarxa,dni);
        mostrarSortida("El cabal abonat al terminal del client " + dni + " és de " + resultat + "l/s.");
    }

    /**
     * @brief Estableix la situació de les aixetes segons un mapa de terminals i el seu estat.
     * @param terminals El mapa que indica si cada terminal ha de tenir l'aigua oberta o tancada.
     * @pre terminals != null
     * @post Es determinen les aixetes a tancar segons l'estat especificat per a cada terminal i es mostra un missatge amb aquestes aixetes.
     */
    public void establirSituacio(Map<String,Boolean> terminals){
        Map<Terminal,Boolean> aiguaArriba = new HashMap<>();
        for (Map.Entry<String, Boolean> t : terminals.entrySet()) {
            Terminal terminal = (Terminal) xarxa.aixeta(t.getKey());
            aiguaArriba.put(terminal,t.getValue());
        }
        Set<Aixeta> result = gestor.aixetesTancar(xarxa, aiguaArriba);
        mostrarSortida("Les aixetes a tancar són:");
        System.out.println("tancar");
        for (Aixeta a : result) {
            mostrarSortida(a.nom());
        }
    }

    /**
     * @brief Dibuixa una nova aixeta en la xarxa.
     * @param a El nom de l'aixeta a dibuixar.
     * @pre a != null
     * @post Es dibuixa la nova aixeta en la xarxa.
     */
    public void dibuixarNou(String a){
        Aixeta aixeta = xarxa.aixeta(a);
        dibuixar(xarxa,false,aixeta);
    }

    /**
     * @brief Obté la llista d'aixetes de la xarxa.
     * @pre Cert
     * @return La llista d'aixetes.
     * @post Es retorna la llista d'aixetes de la xarxa.
     */
    public ArrayList<Aixeta> obtenirAixetes (){
        return aixetes;
    }

    /**
     * @brief Mostra una alerta amb un missatge d'error.
     * @param missatge El missatge d'error a mostrar.
     * @pre missatge != null
     * @post Es mostra una alerta amb el missatge d'error especificat.
     */
    public void mostrarAlerta(String missatge) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText(missatge);
        alerta.showAndWait();
    }

    /**
     * @brief Tanca la finestra actual.
     * @param event L'esdeveniment que va causar el tancament de la finestra.
     * @pre Cert
     * @post Es tanca la finestra actual.
     */
    public void tancarFinestra (ActionEvent event){
        Stage actual = (Stage) ((Node) event.getSource()).getScene().getWindow();
        actual.close();
    }

    /**
     * @brief Desfà l'última acció realitzada.
     * @pre Cert
     * @post Es desfà l'última acció realitzada i es mostra el dibuix actualitzat de la xarxa.
     */
    @FXML
    void desfer(){
        gestor.backtrack(1);
        dibuixar(xarxa,false,aixetes.getFirst());
    }

    /**
     * @brief Torna al menú principal de l'aplicació.
     * @param event L'esdeveniment que causa el retorn al menú.
     * @throws IOException Si hi ha algun error durant la càrrega del menú.
     * @pre Cert
     * @post Es tanca la finestra actual i s'obre la finestra d'inici de l'aplicació.
     */
    @FXML
    void tornarMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("iniciX.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Xarxa d'Aigua");
        stage.setScene(scene);
        Image icona = new Image(getClass().getResourceAsStream("icona.png"));
        stage.getIcons().add(icona);
        stage.show();
        Stage actual = (Stage) ((Node) event.getSource()).getScene().getWindow();
        actual.close();
    }
}
