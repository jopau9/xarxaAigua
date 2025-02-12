/**
 * @file principal.Main.java
 * @brief Classe principal.Main que conté el programa principal per a la gestió de la xarxa d'aigua i les seves operacions de manteniment i control de qualitat de servei
 * @author Julia Baye Soler
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.io.*;

public class Main {

    public static void main(String[] args) {
        // Comprovar si s'han proporcionat els arguments necessaris
//        if (args.length != 2) {
//            System.err.println("Usage: java -jar BeWater.jar fitxer_entrada fitxer_sortida");
//            return;
//        }
//
        // Obté els noms dels fitxers d'entrada i de sortida dels arguments
//        String nomFitxerEntrada = args[0];
//        String nomFitxerSortida = args[1];
        String nomFitxerEntrada = "test/complet1.txt";
        String nomFitxerSortida = "sortida.txt";

        // Crea les instàncies de la xarxa d'aigua i el gestor de la xarxa
        XarxaAigua xarxa = new XarxaAigua();
        GestorXarxa gestor = new GestorXarxa();
        PrintStream sortida;

//        try {
//            // Redirigeix la sortida cap al fitxer de sortida
//            sortida = new PrintStream(new FileOutputStream(nomFitxerSortida));
//            lecturaFitxer(gestor, xarxa, nomFitxerEntrada, sortida);
//        } catch (FileNotFoundException e) {
//            System.err.println("Error: No s'ha pogut obrir el fitxer de sortida: " + nomFitxerSortida);
//        }

        try {
            // Llegeix les comandes del fitxer d'entrada i executa les operacions
            sortida = new PrintStream(new FileOutputStream(nomFitxerSortida));
            lecturaFitxer (gestor, xarxa, nomFitxerEntrada, sortida);
        } catch (FileNotFoundException e) {
            System.err.println("Error: No s'ha trobat el fitxer d'entrada: " + nomFitxerEntrada);
        }
    }

    /**
     * @brief Llegeix les comandes d'un fitxer i inicialitza la xarxa i el gestor.
     *
     * @pre El fitxer existeix en el sistema.
     * @post La xarxa i el gestor han estat inicialitzats correctament i s'han llegit
     *       les comandes del fitxer.
     *
     * @param gestor El gestor de la xarxa d'aigua.
     * @param xarxa La xarxa d'aigua.
     */
    public static void lecturaFitxer(GestorXarxa gestor, XarxaAigua xarxa, String nomArxiu, PrintStream sortidaPrintStream) {
        try {
            File arxiu = new File(nomArxiu);
            Scanner scanner = new Scanner(arxiu);
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
                try {
                    opcions(linea, gestor, xarxa, scanner, sortidaPrintStream);
                } catch (IllegalArgumentException e) {
                    sortidaPrintStream.println("Error de configuració a l'opció " + e.getMessage());
                }catch (NoSuchElementException e) {
                    sortidaPrintStream.println("Error de configuració a l'opció " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    /**
     * @brief Connecta una canonada entre dos elements de la xarxa.
     *
     * @pre Els arguments "xarxa" i "scanner" no són nuls.
     * @post S'ha connectat correctament una canonada entre dos elements de la xarxa.
     *
     * @param xarxa La xarxa d'aigua.
     * @param scanner L'objecte Scanner per llegir les dades de la connexió.
     */
    public static void connectar(XarxaAigua xarxa, Scanner scanner) {
        String a1 = scanner.nextLine();
        String a2 = scanner.nextLine();
        Aixeta origen = xarxa.aixeta(a1);
        Aixeta desti = xarxa.aixeta(a2);
        float capacitat = Float.parseFloat(scanner.nextLine());
        if(desti == null){
            throw new IllegalArgumentException("Aixeta " + a2 +" no trobada");
        }
        if(origen == null){
            throw new IllegalArgumentException("Aixeta " + a1 +" no trobada");
        }
        if (desti instanceof Origen) {
            xarxa.connectarOrigens(origen, desti, capacitat);
        } else {
            xarxa.connectarAmbCanonada(origen, desti, capacitat);
        }
    }

    /**
     * @brief Tanca una aixeta de la xarxa.
     *
     * @pre Els arguments "xarxa" i "scanner" no són nuls.
     * @post S'ha tancat correctament una aixeta de la xarxa.
     *
     * @param xarxa La xarxa d'aigua.
     * @param scanner L'objecte Scanner per llegir les dades de l'aixeta a tancar.
     */
    public static void tancar(XarxaAigua xarxa, Scanner scanner, GestorXarxa gestor) {
        String a1 = scanner.nextLine();
        Aixeta a = xarxa.aixeta(a1);
        xarxa.tancarAixeta(a, gestor);
    }

    /**
     * @brief Obre una aixeta de la xarxa.
     *
     * @pre El paràmetre "xarxa" no és nul.
     * @post L'aixeta especificada ha estat oberta amb èxit.
     *
     * @param xarxa La xarxa d'aigua.
     * @param scanner L'objecte Scanner per llegir les dades de l'aixeta a obrir.
     */
    public static void obrir(XarxaAigua xarxa, Scanner scanner, GestorXarxa gestor) {
        String a1 = scanner.nextLine();
        Aixeta a = xarxa.aixeta(a1);
        xarxa.obrirAixeta(a, gestor);
    }

    /**
     * @brief Abona un usuari a la terminal especificada.
     *
     * @pre Els paràmetres "xarxa" i "scanner" no són nuls.
     * @post L'usuari ha estat abonat amb èxit a la terminal especificada.
     *
     * @param xarxa La xarxa d'aigua.
     * @param scanner L'objecte Scanner per llegir les dades de l'abonament.
     */
    public static void abonar(XarxaAigua xarxa, Scanner scanner) {
        String dni = scanner.nextLine();
        String a1 = scanner.nextLine();
        Terminal t = (Terminal) xarxa.aixeta(a1);
        xarxa.abonar(t, dni);
    }

    /**
     * @brief Realitza una operació de backtracking sobre el gestor de xarxa.
     *
     * @pre El paràmetre "gestor" no és nul.
     * @post Es realitza una operació de backtracking amb èxit sobre el gestor de xarxa.
     *
     * @param gestor El gestor de la xarxa d'aigua.
     * @param scanner L'objecte Scanner per llegir les dades de l'operació de backtracking.
     */
    public static void backTrack(GestorXarxa gestor, Scanner scanner) {
        int n = Integer.parseInt(scanner.nextLine());
        gestor.backtrack(n);
    }

    /**
     * @brief Determina el cabal per a l'origen especificat.
     *
     * @pre El paràmetre "xarxa" no és nul.
     * @post Es determina correctament el cabal per a l'origen especificat.
     *
     * @param xarxa La xarxa d'aigua.
     * @param scanner L'objecte Scanner per llegir les dades del cabal a determinar.
     */
    public static void detCabal(XarxaAigua xarxa, Scanner scanner) {
        String a1 = scanner.nextLine();
        Aixeta a = xarxa.aixeta(a1);
        if(a instanceof Origen) {
            Origen o = (Origen) a;
            float cabal = Float.parseFloat(scanner.nextLine());
            if(cabal < 0) {
                throw new IllegalArgumentException("Cabal negatiu: "+cabal);
            }
            xarxa.determinarCabal(o, cabal);
        }
    }

    /**
     * @brief Determina la demanda per a la terminal especificada.
     *
     * @pre El paràmetre "xarxa" no és nul i el següent element del scanner correspon a una aixeta de tipus terminal.
     * @post Es determina correctament la demanda per a la terminal especificada.
     *
     * @param xarxa La xarxa d'aigua.
     * @param scanner L'objecte Scanner per llegir les dades de la demanda a determinar.
     */
    public static void detDemanda(XarxaAigua xarxa, Scanner scanner) {
        String a1 = scanner.nextLine();
        Terminal t = (Terminal) xarxa.aixeta(a1);
        float demanda = Float.parseFloat(scanner.nextLine());
        if(demanda < 0) {
            throw new IllegalArgumentException("Demanda negativa: "+ demanda);
        }
        xarxa.determinarDemanda(t, demanda);
    }

    /**
     * @brief Verifica si la xarxa té cicles i mostra el resultat.
     *
     * @pre El paràmetre "xarxa" no és nul.
     * @post Es verifica si la xarxa té cicles i es mostra el resultat.
     *
     * @param xarxa La xarxa d'aigua.
     * @param gestor El gestor de la xarxa d'aigua.
     * @param scanner L'objecte Scanner per llegir les dades de la comprovació de cicles.
     */
    public static void teCicles(XarxaAigua xarxa, GestorXarxa gestor, Scanner scanner, PrintStream sortidaPrintStream) {
        String a1 = scanner.nextLine();
        if (gestor.teCicles(xarxa,a1, true)) {
            System.out.println(a1 + " te cicles");
            sortidaPrintStream.println(a1 + " te cicles");
        }
        else {
            System.out.println(a1 + " no te cicles");
            sortidaPrintStream.println(a1 + " no te cicles");
        }
    }

    /**
     * @brief Verifica si la xarxa forma un arbre i mostra el resultat.
     *
     * @pre El paràmetre "xarxa" no és nul.
     * @post Es verifica si la xarxa forma un arbre i es mostra el resultat.
     *
     * @param xarxa La xarxa d'aigua.
     * @param gestor El gestor de la xarxa d'aigua.
     * @param scanner L'objecte Scanner per llegir les dades de la comprovació.
     */
    public static void esArbre(XarxaAigua xarxa, GestorXarxa gestor, Scanner scanner, PrintStream sortidaPrintStream) {
        String a1 = scanner.nextLine();
        if (gestor.formaArbre(xarxa,a1)) {
            System.out.println(a1+" es un arbre");
            sortidaPrintStream.println(a1+" es un arbre");
        }
        else {
            System.out.println(a1 + " no es un arbre");
            sortidaPrintStream.println(a1 + " no es un arbre");
        }
    }

    /**
     * @brief Calcula el cabal mínim per a l'origen especificat.
     *
     * @pre El paràmetre "xarxa" no és nul i el següent element del scanner correspon a una aixeta de tipus origen.
     * @post Es calcula correctament el cabal mínim per a l'origen especificat.
     *
     * @param xarxa La xarxa d'aigua.
     * @param gestor El gestor de la xarxa d'aigua.
     * @param scanner L'objecte Scanner per llegir les dades del càlcul.
     */
    public static void cabalMinin(XarxaAigua xarxa, GestorXarxa gestor, Scanner scanner, PrintStream sortidaPrintStream) {
        String a1 = scanner.nextLine();
        if(xarxa.aixeta(a1) == null){
            throw new IllegalArgumentException("Aixeta " + a1 +" no trobada");
        }
        Origen o = (Origen) xarxa.aixeta(a1);
        String percentatge = scanner.nextLine();
        String numeroString = percentatge.replaceAll("[^0-9]", "");
        float percent = Float.parseFloat(numeroString) / 100;
        System.out.println("cabal minim");
        sortidaPrintStream.println("cabal minim");
        System.out.println( gestor.cabalMinim(xarxa, o, percent));
        sortidaPrintStream.println(gestor.cabalMinim(xarxa, o, percent));
    }

    /**
     * @brief Calcula l'excés de cabal per a les canonades especificades.
     *
     * @pre Els paràmetres "xarxa" i "gestor" no són nuls.
     * @post Es calcula correctament l'excés de cabal per a les canonades especificades.
     *
     * @param xarxa La xarxa d'aigua.
     * @param gestor El gestor de la xarxa d'aigua.
     * @param scanner L'objecte Scanner per llegir les dades del càlcul.
     */
    public static void excesCabal(XarxaAigua xarxa, GestorXarxa gestor, Scanner scanner, PrintStream sortidaPrintStream) {
        Set<Canonada> canonades = new HashSet<>();
        String line = scanner.nextLine();
        String[] parts = line.split("-");
        while (scanner.hasNextLine() && parts.length==2) {
            String aOrigen = parts[0];
            String aDesti = parts[1];
            Aixeta origen = xarxa.aixeta(aOrigen);
            Aixeta desti = xarxa.aixeta(aDesti);
            Canonada c = xarxa.obtenirCanonada(origen, desti);
            if (c != null) {
                canonades.add(c);
            }
            line = scanner.nextLine();
            parts = line.split("-");
        }

        Set<Canonada> result = gestor.excesCabal(xarxa,canonades);
        System.out.println("exces cabal ");
        sortidaPrintStream.println("exces cabal ");
        for (Canonada c : result) {
            System.out.println(c.origen().nom() + " - " + c.desti().nom());
            sortidaPrintStream.println(c.origen().nom() + " - " + c.desti().nom());
        }
    }

    /**
     * @brief Dibuixa la xarxa amb l'origen especificat.
     *
     * @pre Els paràmetres "xarxa" i "scanner" no són nuls.
     * @post Es dibuixa la xarxa amb l'origen especificat.
     *
     * @param xarxa La xarxa d'aigua.
     * @param scanner L'objecte Scanner per llegir les dades de l'origen.
     */
    public static void dibuixar(XarxaAigua xarxa, Scanner scanner) {
        String a1 = scanner.nextLine();
        if(xarxa.aixeta(a1) == null){
            throw new IllegalArgumentException("Aixeta " + a1 +" no trobada");
        }
        Aixeta aixeta = xarxa.aixeta(a1);
        xarxa.dibuixarXarxa(aixeta, false);
    }

    /**
     * @brief Calcula el flux màxim de la xarxa amb l'origen especificat.
     *
     * @pre Els paràmetres "xarxa" i "scanner" no són nuls.
     * @post Es calcula el flux màxim de la xarxa amb l'origen especificat.
     *
     * @param xarxa La xarxa d'aigua.
     * @param scanner L'objecte Scanner per llegir les dades de l'origen.
     * @param gestor El gestor de la xarxa d'aigua.
     */
    public static void maxFlow(XarxaAigua xarxa, Scanner scanner, GestorXarxa gestor){
        String a1 = scanner.nextLine();
        if(xarxa.aixeta(a1) == null){
            throw new IllegalArgumentException("Aixeta " + a1 +" no trobada");
        }
        Aixeta o = xarxa.aixeta(a1);
        if (o!=null) {
            gestor.fluxMaxim(xarxa, o);
        }
    }

    /**
     * @brief Converteix una cadena de coordenades en un objecte principal.Coordenades.
     *
     * @pre La cadena de coordenades no és nul·la.
     * @post Retorna un objecte principal.Coordenades creat a partir de la cadena de coordenades proporcionada.
     *
     * @param coordenadesString La cadena de coordenades a convertir.
     * @return Un objecte principal.Coordenades.
     */
    public static Coordenades stringToCoordenades(String coordenadesString) throws IllegalArgumentException {
        String[] parts = coordenadesString.split(",");

        String[] latitudParts = parts[0].split(":");
        int grausLat;
        int minutsLat;
        float segonsLat;
        char hemisferiLat;

        grausLat = Integer.parseInt(latitudParts[0]);
        minutsLat = Integer.parseInt(latitudParts[1]);
        segonsLat = Float.parseFloat(latitudParts[2].substring(0, latitudParts[2].length() - 1));
        hemisferiLat = latitudParts[2].charAt(latitudParts[2].length() - 1);
        String[] longitudParts = parts[1].split(":");

        int grausLong;
        int minutsLong;
        float segonsLong;
        char hemisferiLong;

        grausLong = Integer.parseInt(longitudParts[0]);
        minutsLong = Integer.parseInt(longitudParts[1]);
        segonsLong = Float.parseFloat(longitudParts[2].substring(0, longitudParts[2].length() - 1));
        hemisferiLong = longitudParts[2].charAt(longitudParts[2].length() - 1);
        if (grausLat < 0 || grausLat >= 90 || minutsLat < 0 || minutsLat >= 60 || segonsLat < 0 || segonsLat >= 60 ||
                grausLong < 0 || grausLong >= 180 || minutsLong < 0 || minutsLong >= 60 || segonsLong < 0 || segonsLong >= 60 ||
                (hemisferiLat != 'N' && hemisferiLat != 'S') || (hemisferiLong != 'E' && hemisferiLong != 'W')) {
            throw new IllegalArgumentException("Format de coordenades incorrecte: " + coordenadesString);
        }
        return new Coordenades(grausLat, minutsLat, segonsLat, hemisferiLat, grausLong, minutsLong, segonsLong, hemisferiLong);
    }

    /**
     * @brief Mostra la llista d'aixetes ordenades per proximitat a les coordenades especificades.
     *
     * @pre Els paràmetres "xarxa", "gestor" i "scanner" no són nuls.
     * @post Es mostra la llista d'aixetes ordenades per proximitat a les coordenades especificades.
     *
     * @param xarxa La xarxa d'aigua.
     * @param scanner L'objecte Scanner per llegir les dades de les coordenades i els noms de les aixetes.
     * @param gestor El gestor de la xarxa d'aigua.
     */
    public static void proximitat(XarxaAigua xarxa, Scanner scanner, GestorXarxa gestor, PrintStream sortidaPrintStream) {
        String coordenades = scanner.nextLine();
        ArrayList<String> nomAixetes = new ArrayList<>();
        ArrayList<String> resultat = new ArrayList<>();
        String a1 = scanner.nextLine();
        Aixeta a = xarxa.aixeta(a1);
        while (scanner.hasNextLine() && (a != null)) {
            nomAixetes.add(a1);
            a1 = scanner.nextLine();
        }
        Coordenades c = stringToCoordenades(coordenades);
        resultat = gestor.proximitat(xarxa, c, nomAixetes);
        System.out.println("proximitat");
        sortidaPrintStream.println("proximitat");
        for (String nom : resultat) {
            System.out.println(nom);
            sortidaPrintStream.println(nom);
        }
    }

    /**
     * @param xarxa La xarxa d'aigua en la qual es buscarà el cabal abonat.
     * @param scanner L'objecte Scanner utilitzat per llegir el DNI des de l'entrada estàndard.
     * @param gestor El gestor de la xarxa d'aigua que conté la lògica per calcular el cabal abonat.
     * @pre L'objecte scanner ha d'estar inicialitzat i llest per llegir.
     * @post S'imprimeix a la sortida estàndard el resultat del càlcul del cabal abonat.
     * @brief Calcula el cabal abonat per a un DNI específic a la xarxa d'aigua donada.
     */
    public static void cabalAbonat(XarxaAigua xarxa, Scanner scanner, GestorXarxa gestor, PrintStream sortidaPrintStream) {
        // Llegeix el DNI des de l'entrada estàndard
        String dni = scanner.nextLine();

        // Calcula el cabal abonat utilitzant el gestor de la xarxa d'aigua
        float resultat = gestor.cabalAbonat(xarxa, dni);

        // Imprimeix el missatge de cabal abonat i el resultat
        System.out.println("cabal abonat");
        sortidaPrintStream.println("cabal abonat");
        System.out.println(resultat);
        sortidaPrintStream.println(resultat);
    }

    /**
     * @brief Funció per gestionar la situació del flux d'aigua a la xarxa.
     *
     * Funció per gestionar la situació del flux d'aigua a la xarxa.
     *
     * @param xarxa La xarxa d'aigua.
     * @param scanner Objecte Scanner per llegir l'entrada.
     * @param gestor El gestor de la xarxa.
     * @pre xarxa i gestor no poden ser nulls.
     * @post Un conjunt de vàlvules es tancarà en funció de l'entrada.
     */
    public static void situacio(XarxaAigua xarxa, Scanner scanner, GestorXarxa gestor, PrintStream sortidaPrintStream) {
        // Mapa per emmagatzemar l'estat del flux d'aigua per a cada terminal
        Map<Terminal, Boolean> aiguaArriba = new HashMap<>();

        // Bucle per llegir les línies d'entrada
        while (scanner.hasNextLine()) {
            // Llegir cada línia de l'objecte scanner
            String line = scanner.nextLine();
            // Separar la línia per espais
            String[] parts = line.split(" ");

            // Comprovar si la línia té dues parts
            if (parts.length == 2) {
                // Obtenir l'objecte terminal corresponent a l'ID proporcionat
                Terminal a = (Terminal) xarxa.aixeta(parts[0]);
                // Determinar si l'aigua està fluïnt o no
                boolean repAigua = parts[1].equals("SI");

                // Si el terminal existeix, actualitzar el seu estat de flux d'aigua en el mapa
                if (a != null) {
                    aiguaArriba.put(a, repAigua);
                }
            } else {
                break; // Si la línia no té dues parts, sortir del bucle
            }
        }

        // Conjunt de vàlvules que es tanquaran basant-se en l'entrada
        Set<Aixeta> result = gestor.aixetesTancar(xarxa, aiguaArriba);

        // Imprimir els noms de les vàlvules que es tanquen
        System.out.println("tancar");
        sortidaPrintStream.println("tancar");
        for (Aixeta a : result) {
            System.out.println(a.nom());
            sortidaPrintStream.println(a.nom());
        }
    }

    /**
     * @brief Executa la comanda especificada correctament.
     *
     * @pre Els arguments "tipus", "gestor", "xarxa" i "scanner" no són nuls.
     * @post S'ha executat la comanda especificada correctament.
     *
     * @param tipus El tipus de la comanda a executar.
     * @param gestor El gestor de la xarxa d'aigua.
     * @param xarxa La xarxa d'aigua.
     * @param scanner L'objecte Scanner per llegir les dades de la comanda.
     * @param sortidaPrintStream El PrintStream associat al fitxer de sortida.
     */
    public static void opcions(String tipus, GestorXarxa gestor, XarxaAigua xarxa, Scanner scanner, PrintStream sortidaPrintStream) {
        if (tipus.equals("origen")) {
            String nom = scanner.nextLine();
            String coordenades = scanner.nextLine();
            Coordenades cord = stringToCoordenades(coordenades);
            Origen o = new Origen(nom, cord);
            if(xarxa.aixeta(nom) != null)
                throw new IllegalArgumentException("Aixeta "+ nom +" ja existent");
            xarxa.afegir(o);

        } else if (tipus.equals("terminal")) {
            String nom = scanner.nextLine();
            String coordenades = scanner.nextLine();
            Coordenades cord = stringToCoordenades(coordenades);
            Float demanda = Float.parseFloat(scanner.nextLine());
            Terminal t = new Terminal(nom, cord, demanda);
            if(xarxa.aixeta(nom) != null)
                throw new IllegalArgumentException("Aixeta "+ nom +" ja existent");
            xarxa.afegir(t);
        } else if (tipus.equals("connexio")) {
            String nom = scanner.nextLine();
            String coordenades = scanner.nextLine();
            Coordenades cord = stringToCoordenades(coordenades);
            Connexio c = new Connexio(nom, cord);
            if(xarxa.aixeta(nom) != null)
                throw new IllegalArgumentException("Aixeta "+ nom +" ja existent");
            xarxa.afegir(c);
        } else if (tipus.equals("connectar")) {
            connectar(xarxa, scanner);
        } else if (tipus.equals("tancar")) {
            tancar(xarxa, scanner, gestor);
        } else if (tipus.equals("obrir")) {
            obrir(xarxa, scanner, gestor);
        } else if (tipus.equals("abonar")) {
            abonar(xarxa, scanner);
        } else if (tipus.equals("backtrack")) {
            backTrack(gestor, scanner);
        } else if (tipus.equals("cabal")) {
            detCabal(xarxa, scanner);
        } else if (tipus.equals("demanda")) {
            detDemanda(xarxa, scanner);
        } else if (tipus.equals("cicles")) {
            teCicles(xarxa, gestor, scanner,sortidaPrintStream);
        } else if (tipus.equals("arbre")) {
            esArbre(xarxa, gestor, scanner,sortidaPrintStream);
        } else if (tipus.equals("cabal minim")) {
            cabalMinin(xarxa, gestor, scanner,sortidaPrintStream);
        } else if (tipus.equals("exces cabal")) {
            excesCabal(xarxa, gestor, scanner,sortidaPrintStream);
        } else if (tipus.equals("dibuix")) {
            dibuixar(xarxa, scanner);
        } else if (tipus.equals("max-flow")) {
            maxFlow(xarxa, scanner, gestor);
        } else if (tipus.equals("proximitat")) {
            proximitat(xarxa, scanner, gestor,sortidaPrintStream);
        } else if (tipus.equals("cabal abonat")) {
            cabalAbonat(xarxa, scanner, gestor,sortidaPrintStream);
        } else if(tipus.equals("situacio")){
            situacio(xarxa, scanner,gestor,sortidaPrintStream);
        }
    }
}