import extensions.File;
import extensions.CSVFile;

class Main extends Program {
    //VARIABLES GLOBALES 
    final String[] COLORS = new String[]{"Black", "Red", "Green", "Blue", "Yellow", "Cyan", "Purple", "White"};
    final int MONSTRE_PV_RANDOM = 20; // De combien les PVs peuvent varier 
    final int MONSTRE_PV_BASE = 50; // De +0 à +MONSTRE_PV_RANDOM cette valeur
    final int MONSTRE_PV_PAR_LEVEL = 30; // De combien les PVs montent par niveau de monstre.
    final int MAX_VERBES = 100;
    final int MAX_ITEMS = 4;
    final int PV_MAX_JOUEUR = 3;
    final int CHANCE_SUR_X = 4; //chance sur x de tomber sur un coffre et non un monstre.
    final Verbe[] ALL_VERBES = allVerbes("verbes.csv");
    final Item[] ALL_ITEMS = allItems("items.csv");
    Joueur[] sauvegardes = readSauvegardes("sauvegardes.csv");
    Joueur joueurActuel;
    String positionJoueur = "Main Menu"; //(ptet un enum ?) positions incluent : "Main Menu", "Crossroad", "Academie", "Boutique Verbes", "Boutiques Items", "Donjon", "Couloir", "Combat", "Coffre"
    final String CLEAR_TERM = "\033[H\033[2J"; //Propriété de Lowan-Houte incorcporated

    void algorithm(){
        print(CLEAR_TERM);
        afficherTxt("txt.txt");
        println("VERSION BETA 0.2\n");
        String input;
        do{
            println("Quelle sauvergarde voulez vous charger ?\n0- Quitter le jeu");
            println(toStringLittle(sauvegardes));
            input = readInput();
            if(equals(input,"0")){return;}
        }while(!equals(input,"1") && !equals(input,"2") && !equals(input,"3"));
        SetUpGame(intFromString(input));
        while(!equals(input,"0")){ 
            println("Que voulez vous faire ?\n");
            println("0: Quitter le Jeu");
            println("1: consulter inventaire");
            println("2: consulter les verbes");
            println("3: parcourir un donjon de niveau 1");
            println("\n"+toStringLittle(joueurActuel));
            input = readInput();
            if(equals(input, "1")){
                println(toString(joueurActuel.inventaire));
                println("Appuyez sur entrer pour continuer");
                readString();
            }else if(equals(input,"2")){
                afficherGrimmoire();
                println("Appuyez sur entrer pour continuer");
                readString();
            }else if(equals(input,"3")){
                parcourirDonjon(1);

            }

        }
    }
    
    void SetUpGame(int numSauvegarde){
        joueurActuel = sauvegardes[numSauvegarde];
        positionJoueur = "Main Menu";

    }
    void afficherTxt(String file){
        File f = newFile(file);
        while(ready(f)){
            println(readLine(f));
        }
    }
    
    String readInput(){print(">>>");return readInput(readString());}
    String readInput(String txt){
        String res = toLowerCase(txt);
        String newRes = "";
        for(int i = 0; i < length(res); i++){
            if(charAt(res, i)!='\n' && charAt(res, i)!=' '){
                newRes += charAt(res,i);
            }
        }
        return newRes;
    }
    void test_readInput(){
        assertEquals("",readInput(""));
        assertEquals("abc",readInput("abc"));
        assertEquals("abc",readInput("a b c "));
        assertEquals("abc",readInput("ABC"));
        assertEquals("abc",readInput("a b\n C"));
        assertEquals("123",readInput("1 2 3"));
    }

    //---------------/Fonctions de toString\---------------//
    String toString(String[] s) {String result = "";for(int i=0;i<length(s);i++){result += s[i]+"; ";}result += "\n";return result;}
    String toString(Verbe v)    {return "ID: "+v.id +" "+v.fr +" "+ v.bv +" "+ v.pr +" "+ v.pp +" lv"+ v.level;}
    String toString(Verbe[] v)  {String result = "";for(int i=0;i<length(v);i++){result += toString(v[i])+";\n";}return result;}
    String toString(Joueur j)   {return "Nom:"+j.nom +" "+"niveau:"+j.level +" "+"xp:"+j.xp +" "+"gold:"+j.gold +" "+"pv:"+j.pv +"\n\nVerbes :\n"+toString(j.livre)+"\nItems :\n"+toString(j.inventaire);}
    String toString(Joueur[] s) {String res = "";for(int i=1;i<length(s);i++){res+= toString(s[i])+"\n";}return res;}
    String toString(Monstre m)  {return "PvMax: "+m.pvMax+" Pv: "+m.pv +" Couleur: "+m.color+" "+COLORS[m.color]+" xpGiven: "+m.xpGiven+" goldGiven: "+m.goldGiven+" VERBE: "+toString(m.verbe);}
    String toString(Monstre[] f){String res="";for(int i=0;i<length(f);i++){res+=toString(f[i])+";\n";}return res;}
    String toString(Item i)     {return "ID: "+i.id+" Nom: "+i.nom+" Description: "+i.description;}
    String toString(Item[] inv) {String result="";for(int i=0;i<length(inv);i++){result+=toString(inv[i])+";\n";}return result;}
    
    String toStringLittle(Joueur j){
        if(equals(j.nom,"Vide")){return "Vide";}
        if(length(j.nom) < 14){
            return j.nom+"\t\t-niveau:"+j.level+"\txp:"+j.xp+"\tgold:"+j.gold+"\tpv:"+j.pv+"/"+j.pvMax+"PV";
        }
        return j.nom+"\t-niveau:"+j.level+"\txp:"+j.xp+"\tgold:"+j.gold+"\tpv:"+j.pv+"/"+j.pvMax+"PV";
    }
    String toStringLittle(Joueur[] sauvegarde){
        String res = "";
        for(int i = 1; i<length(sauvegarde);i++){
            res += i+"- "+toStringLittle(sauvegarde[i])+"\n";
        }
        return res;
    }
    String toStringFight(Monstre m){
        return "Monstre de couleur "+COLORS[m.color]+" "+m.pv+"/"+m.pvMax;
    }
    String toStringLittle(Verbe v){
        return "Niveau: "+v.level+" -"+v.fr+"\t\t--"+v.bv+"\t-"+v.pr+"\t-"+v.pp;
    }
    String toStringLittle(Verbe[] v){
        String res = "Voici le contenu de votre grimmoire :\n\n";
        for(int i = 0;i<length(v);i++){
            res += toStringLittle(v[i])+"\n";
        }
        return res;
    }
    //-----------------------------------------------------//

    //-----------------/Fonctions de print\----------------//
    void afficherSauvegarde(int numSauvegarde){
        println(toString(sauvegardes[numSauvegarde]));
    }

    //Faire des pages et pouvoir switch entre les pages, car sinon avec trop de verbes ce sera illisible 
    void afficherGrimmoire(){
        println(CLEAR_TERM+toStringLittle(joueurActuel.livre));
    }

    //-----------------------------------------------------//

    //-------------------/Constructeurs\-------------------//
    Joueur newJoueur(String nom, int level, int xp, int gold, int pv){
        /*Initialisation du joueur*/ Joueur j = new Joueur();
        if(length(nom) != 0){j.nom = nom;}else{j.nom = "Kévin Jourdin";}
        j.level = level;j.xp = xp;
        j.gold=gold;j.pvMax = 3;
        j.pv = pv;
        j.livre = new Verbe[MAX_VERBES];
        j.inventaire = new Item[MAX_ITEMS];
        return j;
    }
    Verbe newVerbe(int id, String fr, String bv, String pr, String pp, int level){
        /*InitialisgetRowsation du verbe*/Verbe v = new Verbe();
        v.id=id;
        v.fr=fr;
        v.bv=bv;
        v.pr=pr;
        v.pp=pp;
        v.level=level;
        return v;
    }
    Monstre newMonstre(int color, Verbe verbe){
        /*Initialisation du monstre*/
        Monstre m = new Monstre();
        m.color = color;
        m.verbe = verbe;
        m.pvMax = MONSTRE_PV_BASE + (int)(MONSTRE_PV_RANDOM*random()) + MONSTRE_PV_PAR_LEVEL*m.verbe.level; // 50-70 PVs de base, +30 à chaque niveau en plus.
        m.pv = m.pvMax;
        m.xpGiven = (90+(int)(20*random())+m.verbe.level*((m.color-1)/3))/2; //formule pour l'XP prenant en compte le niveau du verbe, et la couleur du monstre.
        m.goldGiven = m.xpGiven/3+(int)(25*random());
        return m;
    }
    Item newItem(int id, String nom, String description){
        Item i = new Item();
        i.id = id;
        i.nom = nom;
        i.description = description;
        return i;
    }   
    //-----------------------------------------------------//
    
    //Fonction pour transformer le csv en plusieurs Tableaux de verbes selon les differents niveaux

    //Fonction d'ériture pour avoir les caractères print les uns après les autres comme dans un RPG

    //Fonction d'ajout d'item à l'inventaire
    int disponible(Joueur j){
        for(int i = 0; i<length(j.inventaire);i++){
            if(j.inventaire[i].id==0){
                return i;
            }
        }
        return -1;
    }

    String ajout_item(Item i){
        int disp = disponible(joueurActuel);
        if(disp<0){
            return "Vous n'avez pas la place dans votre inventaire !";
        }else{
            joueurActuel.inventaire[disp] = i;
            return i.nom + "à bien été ajouté à votre inventaire !";
        }
    }

    //Fonction pour Enlever des items à l'inventaire

    String retirer_item(){return "";}

    //Fonction pour avoir les infos sur les items
    String descItem(Item i){
        return i.nom + ": " +i.description;
    }

    //-----------------/Fonctions de combat\---------------//

    boolean fightMonstre(Monstre m){
        println("Un monstre se dresse devant vous !\n");
        while(m.pv > 0 && joueurActuel.pv > 0){
            println(toStringFight(m));
            if(!askQuestion(m.verbe, m.color)){
                //mauvaise réponse
                damageJoueur();
            }else{
                //bonne réponse
                damageMonstre(m, 40 + random(1,5));
                changeVerb(m,1);
            }
        }
        if(joueurActuel.pv == 0){
            return false;
        }else{
            return true;
        }
    }

    boolean askQuestion(Verbe v, int color){
        // retourne true si bonne réponse, false sinon.

        //définition de quels mots sont visibles:
        boolean[] affiche = new boolean[]{true, true, true, true};
        if(color == 0){affiche[0] = false;}
        if(color == 1 || color == 4 || color == 6 || color == 7){affiche[1] = false;}
        if(color == 2 || color == 4 || color == 5 || color == 7){affiche[2] = false;}
        if(color == 3 || color == 6 || color == 5 || color == 7){affiche[3] = false;}

        //print de la question
        if(affiche[0]){print(v.fr+" ; ");}else{print(" ______ ; ");}
        if(affiche[1]){print(v.bv+" ; ");}else{print(" ______ ; ");}
        if(affiche[2]){print(v.pr+" ; ");}else{print(" ______ ; ");}
        if(affiche[3]){println(v.pp);}else{println(" ______");}
        println("\nQuel est le verbe manquant ?");
        println("\n"+toStringLittle(joueurActuel));

        //definition 
        String reponse = readInput();
        int nbReponses = (color-1) / 3 + 1;
    
        if(color == 0){                                          if(equals(reponse,v.fr)){return true;}else{return false;}}

        if(color == 1 || color == 4 || color == 6 || color == 7){if(equals(reponse,v.bv)){return true;}else{return false;}}
        if(color == 2 || color == 4 || color == 5 || color == 7){if(equals(reponse,v.pr)){return true;}else{return false;}}
        if(color == 3 || color == 6 || color == 5 || color == 7){if(equals(reponse,v.pp)){return true;}else{return false;}}
        
        return false;
    }

    //Il faut faire un truc qui change le verbe du monstre (+sa couleur si on veut) si il lui reste des PV.
    //Peut être un type de monstre qui peut en plus changer de couleur.
    void changeVerb(Monstre m, int level){
        m.verbe = generateVerbe(1);
    }
    

    //-----------------------------------------------------//

    //---------------/Fonctions de Donjon\-----------------//

    void parcourirDonjon(int niveau){
        Monstre[] floor = generateFloor(niveau);
        boolean alive = true;
        for(int i = 0; i<length(floor); i++){
            if(alive && fightMonstre(floor[i])){
                println("Bravo ! Le monstre à été vaincu");
            }else{
                alive = false;
            }
        }
        if(!alive){
            println("Aie ! vous avez perdu !, vous vous soignez et vous remetez en route...\n");
            joueurActuel.pv = joueurActuel.pvMax;
        }else{
            println("Bien joué, vous avez triomphé(e) du Donjon !\nMerci d'avoir joué à l'alpha\n");
        }
    }

    Monstre[] generateFloor(int level){
        int size = (level*2)+(level/2)+1;
        Monstre[] floor = new Monstre[size];
        for(int i=0; i<size; i++){
            floor[i] = generateMonstre(level);
        }
        return floor;
    }

    Monstre generateMonstre(int level){
        if(level == 1){
            int color = random(1,3);//trois couleurs primaires
            return newMonstre(color, generateVerbe(level));
        }else if(level == 2){
            int color = random(0,3);//précédents + blanc
            return newMonstre(color, generateVerbe(level));
        }else if(level == 3){
            int color = random(0,6);//précédents + couleurs secondaires
            return newMonstre(color, generateVerbe(level));
        }else{
            int color = random(0,7);//précédents + noir (toutes les couleurs)
            return newMonstre(color, generateVerbe(level));
        }
    }
    void test_generateMonstre(){
        SetUpGame(2);
        Monstre m = generateMonstre(1);
        println(toString(m));
    }

    Verbe generateVerbe(int level){
        int size = length(joueurActuel.livre);
        int[] allId = new int[size];
        int indiceTemp = 0;
        for(int i=0; i<size; i++){
            if(joueurActuel.livre[i].level == level){
                allId[indiceTemp] = joueurActuel.livre[i].id;
                indiceTemp += 1;
            }
        }
        int resultId = allId[random(0,size-1)];
        return ALL_VERBES[resultId];
    }
    void test_generateVerbe(){
        SetUpGame(2);
        Verbe verbeG = generateVerbe(1);
        println(toString(verbeG));
    }
    //-----------------------------------------------------//

    //---------------/Fonctions de Combat\-----------------//

    void damageMonstre(Monstre monstre, int damage){
        monstre.pv = monstre.pv - damage;
        if(monstre.pv < 0){
            monstre.pv = 0;
        }
        println("BAM !\n");
    }
    void damageJoueur(){
        joueurActuel.pv = joueurActuel.pv - 1;
        if(joueurActuel.pv == 0){
            //deathJoueur();
        }
        println("OUCH !\n");
    }
    void healJoueur(){
        if(joueurActuel.pv < joueurActuel.pvMax){
            joueurActuel.pv += 1;
        }
    }
    //-----------------------------------------------------//

    //-----------------/Fonctions de CSV\------------------//

    //Fonctions pour lire le fichier de sauvegarde csv
    Joueur[] readSauvegardes(String fichier){
        CSVFile f = loadCSV("csv/"+fichier);
        Joueur[] sauvegardes = new Joueur[rowCount(f)];
        int nbJoueurs = 0;
        for(int l = 1; l<rowCount(f); l++){
            sauvegardes[l] = newJoueur(getCell(f, l, 0),
            intFromString(getCell(f,l,1)),
            intFromString(getCell(f,l,2)),
            intFromString(getCell(f,l,3)),
            intFromString(getCell(f,l,4)));
            sauvegardes[l].pvMax = PV_MAX_JOUEUR;
            sauvegardes[l].livre = newLivre(f,l);
            sauvegardes[l].inventaire = newInventaire(f,l);
            nbJoueurs += 1;
        }
        return sauvegardes;
    }
    

    //Fonction pour enregistrer les verbes d'un csv en tableau
    Verbe[] allVerbes(String file){
        CSVFile f = loadCSV("csv/"+file);
        Verbe[] livre = new Verbe[rowCount(f)-1];
        for(int l = 0; l < length(livre); l++){
            livre[l] = newVerbe(intFromString(getCell(f,l+1,0)),
            getCell(f,l+1,1),
            getCell(f,l+1,2),
            getCell(f,l+1,3),
            getCell(f,l+1,4),
            intFromString(getCell(f,l+1,5)));
        }
        return livre;
    }
    //Fonctionpour avoir le tableau de verbe d'un joueur a partir de son csv
    Verbe[] newLivre(CSVFile save, int ligne){
        String livreString = getCell(save,ligne,5);
        int[] nombres = StringIntoInt(livreString);
        int nbVerbes = length(nombres);
        Verbe[] livre = new Verbe[nbVerbes];
        for(int i = 0; i<nbVerbes; i++){
            livre[i] = ALL_VERBES[nombres[i]];
        }
        return livre;
    }
    //Fonction String de chiffres en int: "123" => 123
    int intFromString(String input){
        int result = 0;
        int power = 1;
        for(int i = length(input)-1; i >= 0; i=i-1){
            result += (charAt(input,i)-'0') * power;
            power = power * 10;
        }
        return result;
    }
    void test_intFromString(){
        assertTrue(123 == intFromString("123"));
    }
    //Fonction "0102102500" => [1, 2, 10, 25, 0]
    int[] StringIntoInt(String numbers){
        int[] n = new int[length(numbers)/2];
        for(int i = 0; i<length(n); i++){
            n[i] = 10*(charAt(numbers, i*2)-'0')+(charAt(numbers, i*2+1)-'0');
        }
        return n;
    }
    void test_StringIntoInt(){
        int[] n = new int[]{1,2,10,25};
        int[] m = StringIntoInt("01021025"); 
        assertEquals(n[0], m[0]);
        assertEquals(n[1], m[1]);
        assertEquals(n[2], m[2]);
        assertEquals(n[3], m[3]);
    }

    Item[] allItems(String file){
        CSVFile f = loadCSV("csv/"+file);
        Item[] inv = new Item[rowCount(f)-1];
        for(int l = 0; l<length(inv);l++){
            inv[l] = newItem(intFromString(getCell(f,l+1,0)), getCell(f,l+1,1), getCell(f,l+1,2));
        }
        return inv;
    }
    Item[] newInventaire(CSVFile save, int ligne){
        Item[] inv = new Item[MAX_ITEMS];
        int[] nombres = numberItemIntoIntTab(getCell(save,ligne,6));
        for(int i = 0; i < length(inv); i++){
            inv[i] = ALL_ITEMS[nombres[i]];
        }
        return inv;
    }
    int[] numberItemIntoIntTab(String numbers){
        //numbers forcement de forme "1234"
        int[] n = new int[MAX_ITEMS];
        n[0] = charAt(numbers,0)-'0';
        n[1] = charAt(numbers,1)-'0';
        n[2] = charAt(numbers,2)-'0';
        n[3] = charAt(numbers,3)-'0';
        return n;
    }
    void test_numberItemIntoIntTab(){
        int[] n = new int[]{1,2,3,4};
        assertTrue(n[0]==numberItemIntoIntTab("1234")[0]);
        assertTrue(n[1]==numberItemIntoIntTab("1234")[1]);
        assertTrue(n[2]==numberItemIntoIntTab("1234")[2]);
        assertTrue(n[3]==numberItemIntoIntTab("1234")[3]);
    }
    



    //Fonction pour sauvegarder dans le fichier de sauvegarde le joueur actuel (Joueur courant + autrs joueurs => save(String[][], sauvegardes.csv)
    
    //-----------------------------------------------------//

    //-----------------/Fonctions de .txt\-----------------//

    //Fonction lecture de Sprites + stockage dans un string

    //Fonction d'affichage de Sprites
    
    //-----------------------------------------------------//

    
}   
