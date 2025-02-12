# Projecte Be Water

## Introducció

El projecte **Be Water** es desenvolupa en el context de la sequera actual, que ha ressuscitat projectes sobre l'abastament i distribució d'aigua. L'objectiu és crear programes informàtics per simular els efectes de canvis en el subministrament d'aigua a nivell agrícola, ramader, industrial i domèstic. Això inclou estudis sobre la disminució de cabals, la interconnexió de xarxes de distribució i la detecció de fuites.

> "Be water, my friend" - Bruce Lee
>
> "Al meu país, la pluja no sap ploure
> O plou poc, o plou massa
> Si plou poc és la sequera
> Si plou massa és un desastre
> Qui portarà la pluja a escola?
> Qui li dirà com s'ha de ploure?" - Raimon

## Estructura del Projecte

El projecte està dividit en diverses classes que representen els components d'una xarxa de distribució d'aigua. A continuació es descriuen les principals classes i els seus rols:

### Classes Principals

- **Aixeta.java**: Representa una aixeta de pas en la xarxa.
- **Canonada.java**: Representa una canonada que connecta dos punts de la xarxa.
- **Connexio.java**: Representa una connexió entre diverses canonades.
- **GestorXarxa.java**: Gestiona les operacions i consultes sobre la xarxa d'aigua.
- **Origen.java**: Representa un punt d'origen d'aigua en la xarxa.
- **Terminal.java**: Representa un punt terminal on l'aigua és subministrada.
- **XarxaAigua.java**: Representa la xarxa de distribució d'aigua completa.
- **XarxaAiguaGUI.java**: Proporciona una interfície gràfica per interactuar amb la xarxa.

### Descripció de l'Entorn de l'Aplicació

#### Xarxes de Distribució d'Aigua

Una xarxa de distribució d'aigua consisteix en la unió de diverses canonades i aixetes de pas. Simplificant, les aixetes poden estar obertes o tancades, deixant passar tota l'aigua o cap. Cada xarxa pot tenir diversos punts d'entrada (orígens) i sortida (punts terminals).

#### Elements d'una Xarxa

- **Aixetes de Pas**: Poden estar obertes o tancades.
- **Canonades**: Conecten els diferents punts de la xarxa.
- **Connexions**: Elements que permeten la unió de diverses canonades.

#### Topologia de les Xarxes

Les xarxes poden tenir formes de graf amb cicles, i la direcció del cabal d'aigua és coneguda. Cada punt de la xarxa té coordenades geogràfiques per facilitar la localització.

### Repartiment del Cabal

El cabal es distribueix proporcionalment a la demanda en cada punt de la xarxa. La suma dels cabals als punts d'origen mai excedirà la demanda total.

## Funcionalitats Principals

### Operacions

1. **Unir Xarxes**: Connectar diferents xarxes d'aigua.
2. **Associar Abonats**: Assignar abonats a punts terminals.
3. **Obrir i Tancar Aixetes**: Modificar l'estat de les aixetes.
4. **Desfer Operacions**: Tornar a una configuració anterior.
5. **Establir Cabal Potencial**: Definir el cabal en un punt d'origen.
6. **Establir Demanda**: Fixar la demanda en un punt terminal.

### Consultes

1. **Trobar Cicles**: Identificar cicles en una xarxa.
2. **Forma d'Arbre**: Determinar si una xarxa té forma d'arbre.
3. **Cabal Mínim**: Calcular el cabal mínim necessari als punts d'origen.
4. **Excés de Cabal**: Detectar canonades amb cabal excessiu.
5. **Aixetes a Tancar**: Identificar aixetes properes a punts problemàtics.
6. **Cabal en Punt Terminal**: Determinar el cabal que arriba a un abonat.
7. **Llistar per Proximitat**: Ordenar aixetes segons la seva proximitat geogràfica.

## Descripció de l'Entrada i la Sortida

### Construcció de les Xarxes

L'entrada consisteix en una sèrie d'operacions per definir punts terminals, connexions i orígens, així com per connectar-los amb canonades de capacitats específiques.