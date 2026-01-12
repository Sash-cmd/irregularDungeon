import extensions.File;
import extensions.CSVFile;

class Main extends Program {
    //VARIABLES GLOBALES 
    final String[] COULEURS = new String[]{"Black", "Red", "Green", "Blue", "Yellow", "Cyan", "Purple", "White"};
    final int MONSTRE_PV_ALEATOIRE = 20; // De combien les PVs peuvent varier 
    final int MONSTRE_PV_BASE = 50; // De +0 à +MONSTRE_PV_ALEATOIRE cette valeur
    final int MONSTRE_PV_PAR_NIVEAU = 30; // De combien les PVs montent par niveau de monstre.
    final int MAX_VERBES = 100;
    final int MAX_OBJETS = 4;
    final int PV_MAX_JOUEUR = 3;
    final int CHANCE_SUR_X = 4; //chance sur x de tomber sur un coffre et non un monstre.

    final Verbe[] TOUS_VERBES = tousVerbes("verbes.csv");
    final Item[] TOUS_OBJETS = tousObjets("items.csv");
    final Item OBJET_VIDE = nouvObjet(0,"Vide","Vous n'avez pas d'objet a cet endroit.");

    Joueur[] sauvegardes = lireSauvegardes("sauvegardes.csv");
    final Joueur JOUEUR_VIDE = nouvJoueur("Vide",1,0,0,3);
    Joueur joueurActuel;

    final int XP_REQUIS_1 = 1000;
    final int XP_REQUIS_2 = 2000;    
    final int XP_REQUIS_3 = 3000;

    final int PRIX_VERBE = 50;

    // au final on part peut etre sur un boolean est en combat car c'est le seul utile.
    String positionJoueur = "Main Menu"; //(ptet un enum ?) positions incluent : "Main Menu", "Crossroad", "Academie", "Boutique Verbes", "Boutiques Items", "Donjon", "Couloir", "Combat", "Coffre"
    final String EFFACER_TERM = "\033[H\033[2J"; //Propriété de Lowan-Houte incorcporated
    void effacerTerm(){print(EFFACER_TERM);}

    void algorithm(){
        
        String input = "";
        do{    
        input = menuPrincipalChoix();

        if(equals(input,"1")){
            if(!equals(choisirSauvegarde(),"0")){
                jeu();
            }
        }
        }while(!equals(input,"0"));
    }

    void jeu(){
        String input = "";
        while(!equals(input,"0")){//Boucle du jeu

            afficherMessageDeplacement();

            println("\n"+affichageReduit(joueurActuel));
            input = lireEntree();

            if(equals(input, "1")){//Ouvrir le sac à dos
                ouvrirSac();

            }else if(equals(input,"2")){//Aller à l'académie
                parcourirAcademie();

            }else if(equals(input,"3")){
                parcourirDonjon(1);

            }
        }
        afficherTitre();
        println("Que voulez vous faire ?\n\n0: Retour\n1: Quitter et sauvegarder\nautre: Quitter sans sauvegarder");
        input = lireEntree();
        if(equals(input,"0")){jeu();}
        else if(equals(input,"1")){sauvegarderEtat();}

    }
    
    void initJeu(int numSauvegarde){
        joueurActuel = sauvegardes[numSauvegarde];
        positionJoueur = "Main Menu";

    }

    void afficherTxt(String file){
        File f = newFile(file);
        while(ready(f)){
            println(readLine(f));
        }
    }
    
    String lireEntree(){print(">>>");return lireEntree(readString());}
    String lireEntree(String txt){
        String res = toLowerCase(txt);
        String newRes = "";
        for(int i = 0; i < length(res); i++){
            if(charAt(res, i)!='\n' && charAt(res, i)!=' '){
                newRes += charAt(res,i);
            }
        }
        return newRes;
    }
    void test_lireEntree(){
        assertEquals("",lireEntree(""));
        assertEquals("abc",lireEntree("abc"));
        assertEquals("abc",lireEntree("a b c "));
        assertEquals("abc",lireEntree("ABC"));
        assertEquals("abc",lireEntree("a b\n C"));
        assertEquals("123",lireEntree("1 2 3"));
    }


    //--------------/Fonctions de Deplacement\-------------//

    String menuPrincipalChoix(){ //"0"=> quitter le jeu ; "2"||"3"=>action puis retour menu ; "1"=> continuer
        effacerTerm();
        afficherTitre();
        print("Sauvegardes:\n"+affichageReduit(sauvegardes)+"\n");
        afficherMessageChoixSurSauvegardes();
        String input = lireEntree();

        if      (equals(input,"0")){//Quitter le jeu
            println("Merci d'avoir Joué !");

        }else if(equals(input,"2")){//Choisir parmis les sauvegardes vides et créer une sauvegarde
            creerSauvegarde();

        }else if(equals(input,"3")){//Effacer une sauvegarde
            effacerSauvegarde();

        }
        return input;
    }

    void afficherMessageChoixSurSauvegardes(){
        println("Que voulez vous faire ?\n");
        println("0: Quitter le Jeu");
        println("1: Choisir une sauvegarde");
        println("2: Créer une Sauvegarde");
        println("3: Effacer une sauvegarde\n");
    }

    void afficherMessageDeplacement(){
        effacerTerm();
        println("\nQue voulez vous faire ?\n");
        println("0: Quitter la partie");
        println("1: Ouvrir le sac à dos");
        println("2: Aller à l'académie");
        println("3: Aller dans le donjon\n");
    }

    void afficherMessageAcademie(){
        effacerTerm();
        println("\nQue voulez vous faire ?\n");
        println("0: Revenir en arrière");
        println("1: Aller en cours de sortilèges ("+PRIX_VERBE+" gold)");
        println("2: Aller à la boutique d'objets");
        println("3: Ouvrir le sac à dos\n");
    }

    //-----------------------------------------------------// 

    //----------------/Fonctions de String\----------------//    
    String affichageReduit(Joueur j){
        if(equals(j.nom,"Vide")){return "Vide";}
        if(length(j.nom) < 14){
            return j.nom+"\t\t-niveau:"+j.level+"\txp:"+j.xp+"\tgold:"+j.gold+"\tpv:"+j.pv+"/"+j.pvMax+"PV";
        }
        return j.nom+"\t-niveau:"+j.level+"\txp:"+j.xp+"\tgold:"+j.gold+"\tpv:"+j.pv+"/"+j.pvMax+"PV";
    }
    String affichageReduit(Joueur[] sauvegarde){
        String res = "";
        for(int i = 1; i<length(sauvegarde);i++){
            res += i+": "+affichageReduit(sauvegarde[i])+"\n";
        }
        return res;
    }

    String affichageCombat(Monstre m){
        return "Monstre de couleur "+COULEURS[m.color]+" "+m.pv+"/"+m.pvMax;
    }

    String affichageReduit(Verbe v){
        return "Niveau: "+v.level+" -"+v.fr+"\t\t--"+v.bv+"\t-"+v.pr+"\t-"+v.pp;
    }
    String affichageReduit(Verbe[] v){
        String res = "Voici le contenu de votre grimmoire :\n\n";
        for(int i = 0;i<length(v);i++){
            res += affichageReduit(v[i])+"\n";
        }
        return res;
    }

    String affichageReduit(Item[] inv){
        String res = "Voici le contenu de votre inventaire :\n\n";
        for(int i = 0;i<length(inv);i++){
            res += i+1 + ") " + inv[i].nom + "\n";
        }
        return res;
    }
    //-----------------------------------------------------//


    //--------------/Fonctions de sauvegardes\-------------//

    void sauvegarderEtat(){
        String[][] sauvegardesTab = sauvegardesEnTab();
        saveCSV(sauvegardesTab, "csv/sauvegardes.csv");
        sauvegardes = lireSauvegardes("sauvegardes.csv");
    }

    String[][] sauvegardesEnTab(){
        String[][] resultat = new String[4][7];
        resultat[0] = nouvStringTab("Nom","level","xp","gold","pv","listeVerbeForme\"0-1-12-\"","listeItemForme\"0000\"(0=rien,1=fiole,ect)");
        for(int i=1; i<4; i++){
            resultat[i][0] = ""+sauvegardes[i].nom;
            resultat[i][1] = ""+sauvegardes[i].level;
            resultat[i][2] = ""+sauvegardes[i].xp;
            resultat[i][3] = ""+sauvegardes[i].gold;
            resultat[i][4] = ""+sauvegardes[i].pv;
            resultat[i][5] = listeVersString(sauvegardes[i].livre);
            resultat[i][6] = listeVersString(sauvegardes[i].inventaire);
        }

        return resultat;
    }

    String listeVersString(Verbe[] tab){
        String resultat = "";
        for(int i=0;i<length(tab);i++){
            resultat += ""+tab[i].id+"-";
        }
        return resultat;
    }
    void test_listeVersString_Verbe(){
        Verbe[] v = new Verbe[]{nouvVerbe(1,"a","b","c","d",1),nouvVerbe(2,"a","b","c","d",1),nouvVerbe(38,"a","b","c","d",1)};
        assertEquals(listeVersString(v),"1-2-38-");
    }

    String listeVersString(Item[] tab){
        String resultat ="";
        for(int i=0; i<length(tab); i++){
            resultat += ""+tab[i].id;
        }
        return resultat;
    }
    void test_listeVersString_Item(){
        Item[] v = new Item[]{nouvObjet(1,"a","b"),nouvObjet(1,"a","b"),nouvObjet(2,"a","b"),nouvObjet(0,"a","b")};
        assertEquals(listeVersString(v),"1120");
    }

    String[] nouvStringTab(String elem1,String elem2,String elem3,String elem4,String elem5,String elem6,String elem7){
        String[] tab = new String[]{elem1,elem2,elem3,elem4,elem5,elem6,elem7};
        return tab;
    }


    String choisirSauvegarde(){
        //choix de la sauvegarde.
        String input;
        do{
            afficherTitre();
            println("Quelle sauvergarde voulez vous charger ?\n\n0: Retour\n");
            println(affichageReduit(sauvegardes));
            input = lireEntree();
            if(length(input)==0){}else{
            if(equals(input,"0")){return input;}
            println(sauvegardes[charAt(input,0)-'0'].nom);
            if(equals(sauvegardes[charAt(input,0)-'0'].nom,"Vide")){
                input = "0";
                println("Vous ne pouvez pas choisir une sauvegarde vide!");
                println("appuyez sur entrer pour continuer");
                readString();
            }}
        }while(!equals(input,"1") && !equals(input,"2") && !equals(input,"3"));
        initJeu(stringVersInt(input));
        return input;
    }

    void creerSauvegarde(){
        String input;
        do{
            afficherTitre();
            println("Quelle sauvergarde voulez vous créer ?\n\n0: Retour\n");
            println(affichageReduit(sauvegardes));
            input = lireEntree();
            if(length(input)==0){}else{
            if(equals(input,"0")){return;}
            println(sauvegardes[charAt(input,0)-'0'].nom);
            if(!equals(sauvegardes[charAt(input,0)-'0'].nom,"Vide")){
                input = "0";
                println("Vous ne pouvez pas choisir une sauvegarde en cours!");
                println("appuyez sur entrer pour continuer");
                readString();
            }}
        }while(!equals(input,"1") && !equals(input,"2") && !equals(input,"3"));
        sauvegardes[stringVersInt(input)] = creationJoueur();
        sauvegarderEtat();
    }

    void effacerSauvegarde(){
        String input;
        do{
            afficherTitre();
            println("Quelle sauvergarde voulez vous effacer ?\n\n0: Retour\n");
            println(affichageReduit(sauvegardes));
            input = lireEntree();
            if(length(input)==0){}else{
            if(equals(input,"0")){return;}       
            }
        }while(!equals(input,"1") && !equals(input,"2") && !equals(input,"3"));
        int effacer = charAt(input,0)-'0';
        sauvegardes[effacer] = JOUEUR_VIDE;  
        sauvegarderEtat();
    }

    Joueur creationJoueur(){
        effacerTerm();
        println("Quel est votre nom ?");
        String nom = lireEntree();
        return nouvJoueur(nom,1,0,0,3);
    }

    //-----------------/Fonctions de print\----------------//
    void afficherSauvegarde(int numSauvegarde){
        println(toString(sauvegardes[numSauvegarde]));
    }
    void afficherTitre(){
        effacerTerm();
        afficherTxt("txt/titre.txt");
        println("VERSION ALPHA\n");
    }
    void afficherMessagePasArgent(){
        println("\nMince, il vous manque du gold !");
        println("Revenez quand vous serez, mmm...... plus riche!\n");
        println("Appuyez sur entrer pour continuer");
        lireEntree();
    }

    //-----------------------------------------------------//

    //---------------/Fonctions de sac à dos\--------------//
    void afficherMessageChoixSac(){
        effacerTerm();
        println("\nVous êtes dans votre sac,\nQue voulez vous faire ?\n");
        println("0: Retour");
        println("1: Consulter l'inventaire");
        println("2: Ouvrir le grimoire\n");
    }

    void ouvrirSac(){
        String input;
        do{
            afficherMessageChoixSac();
            input = lireEntree();
            if(equals(input,"1")){
                afficherInventaire();
            }else if (equals(input,"2")){
                if(!equals(positionJoueur,"Combat")){
                    afficherGrimoire();
                }else{
                    println("Vous êtes en combat, vous ne pouvez pas consulter votre grimoire !");
                }
            }
        }while(!equals(input,"0"));
    }

    //Faire des pages et pouvoir switch entre les pages, car sinon avec trop de verbes ce sera illisible 
    void afficherGrimoire(){
        println(EFFACER_TERM+affichageReduit(joueurActuel.livre));
        println("Appuyez sur entrer pour continuer");
        readString();
    }

    void afficherInventaire(){
        println(EFFACER_TERM+affichageReduit(joueurActuel.inventaire));
        println("Appuyez sur entrer pour continuer");
        readString();
    }
    //-----------------------------------------------------//
    
    //---------------/Fonctions de l'Académie\-----------------//

    void parcourirAcademie(){
        affichageArt(lectureArt("txt/academie.txt"));
        String input = "";
        do{
        effacerTerm();
        ecrireLent("Bienvenue à l'Académie de Magie !\nQue voulez vous faire ?\n",40);
        println("0: Retour");
        println("1: Aller en cours");
        println("2: Aller au RU (magique)");
        input = lireEntree();
        if(equals(input,"1")){
            println("Bienvenue en cours ! Ici vous pouvez acheter des verbes pour votre grimoire.\n");
            println("0: Retour");
            println("1: Acheter un verbe");
            String input2 = lireEntree();
            if(equals(input2,"1")){
                achatVerbe(verbesDisponibles(joueurActuel));
            }else if(equals(input2,"0")){
                return;
            }  
            }else if(equals(input,"2")){
            println("Bienvenue au RU ! Ici vous pouvez acheter des items magiques pour votre inventaire.\n");
            achatItem();
            }else if(equals(input,"0")){
            return;
            }else{
            println("Je ne suis pas sûr de bien comprendre ce que tu veux aventurier...");
            }
        }
        while(!equals(input,"0"));
    }
    
    void achatItem(){
        for(int i=1;i<length(TOUS_OBJETS);i++){
            println(i + ": " + TOUS_OBJETS[i].nom + " : " + TOUS_OBJETS[i].description);
        }
        println("\nEntrez le numéro de l'objet que vous souhaitez acheter, ou 0 pour revenir en arrière.");
        String input = lireEntree();
        int choix = stringVersInt(input);
        if(choix == 0){
            return;
        }
        if(choix < 1 || choix >= length(TOUS_OBJETS)){
            println("Cet objet n'existe pas !");
            readString();
            return;
        }
        ajouterObjet(TOUS_OBJETS[choix]);
        
        println("Appuyez sur une touche pour continuer");
        readString();
    }

    Verbe[] verbesDisponibles(Joueur j){
        //compter les verbes non possédés
        Verbe[] temp = new Verbe[length(TOUS_VERBES)];
        int count = 0;
        for(int i=0; i<length(TOUS_VERBES); i++){
            boolean trouve = false;
            for(int k=0; k<length(j.livre); k++){
                if(TOUS_VERBES[i].id == j.livre[k].id){
                    trouve = true;
                }
            }
            if(!trouve){
                temp[count] = TOUS_VERBES[i];
                count += 1;
            }
        }
        //copier les verbes valides dans le tableau résultat de taille exacte
        Verbe[] result = new Verbe[count];
        for(int i=0; i<count; i++){
            result[i] = temp[i];
        }
        return result;
    }

    void achatVerbe(Verbe[] dispo){
        if(length(dispo) == 0){
            println("Il n'y a aucun verbe disponible à l'achat pour le moment.");
            return;
        }
        Verbe choisi = dispo[random(0, length(dispo)-1)];
        // Ajouter le verbe à la fin du livre du joueur (redimensionnement du tableau)
        int oldSize = length(joueurActuel.livre);
        Verbe[] newLivre = new Verbe[oldSize + 1];
        for(int i=0; i<oldSize; i++){
            newLivre[i] = joueurActuel.livre[i];
        }
        newLivre[oldSize] = choisi;
        joueurActuel.livre = newLivre;
        println("Vous avez appris le verbe "+affichageReduit(choisi)+" !");
        println("Appuyez sur une touche pour continuer");
        readString();
    
    }

    //-----------------------------------------------------//

    //---------------------/Tutoriel\----------------------//

    void afficherTutoriel(){
        ecrireLent("Bienvenue dans Irregular Dungeon ! \n\n"+
                "Dans ce monde, vous devez vous battre contre des monstre, mais avec des verbes irréguliers en anglais !\n"+
                "Il y a plusieurs types de monstre: les monstres rouges vous nécéssiterat de rentrer uniquement la base verbale du verbe pour le battre,\n"+
                "tandis que les verts, eux, vous demanderons le prétérit, et enfin les bleus vous demanderons de donner le participe passé du verbe.\n"+
                "Il y a à votre disposition une académie de magie dans laquelle vous pourrez acheter des verbes pour progresser,\n"+
                "mais aussi des Items pour vous aider !\n", 250);
    }

    //-----------------------------------------------------//

    //-------------------/Constructeurs\-------------------//
    Joueur nouvJoueur(String nom, int level, int xp, int gold, int pv){
        /*Initialisation du joueur*/ Joueur j = new Joueur();
        if(length(nom) != 0){j.nom = nom;}else{j.nom = "Kévin Jourdin";}
        j.level = level;j.xp = xp;
        j.gold=gold;j.pvMax = 3;
        j.pv = pv;
        j.livre = new Verbe[3];
        for(int i=0; i<3; i++){
            j.livre[i] = TOUS_VERBES[i];
        }
        j.inventaire = new Item[MAX_OBJETS];
        for(int i=0; i<4; i++){
            j.inventaire[i] = TOUS_OBJETS[0];
        }
        return j;
    }
    Verbe nouvVerbe(int id, String fr, String bv, String pr, String pp, int level){
        /*Initialisation du verbe*/Verbe v = new Verbe();
        v.id=id;
        v.fr=fr;
        v.bv=bv;
        v.pr=pr;
        v.pp=pp;
        v.level=level;
        return v;
    }
    Monstre nouvMonstre(int color, Verbe verbe){
        /*Initialisation du monstre*/
        Monstre m = new Monstre();
        m.color = color;
        m.verbe = verbe;
        m.pvMax = MONSTRE_PV_BASE + (int)(MONSTRE_PV_ALEATOIRE*random()) + MONSTRE_PV_PAR_NIVEAU*m.verbe.level; // 50-70 PVs de base, +30 à chaque niveau en plus.
        m.pv = m.pvMax;
        m.xpGiven = (90+(int)(20*random())+m.verbe.level*((m.color-1)/3))/2; //formule pour l'XP prenant en compte le niveau du verbe, et la couleur du monstre.
        m.goldGiven = m.xpGiven/3+(int)(25*random());
        return m;
    }
    Item nouvObjet(int id, String nom, String description){
        Item i = new Item();
        i.id = id;
        i.nom = nom;
        i.description = description;
        return i;
    }   
    //-----------------------------------------------------//
    
    //Fonction pour transformer le csv en plusieurs Tableaux de verbes selon les differents niveaux

    //Fonctions servant à l'écriture lente (délai d'apparition des lettres)
    int temps(int vitesse){
        return 1000-vitesse;
    }
    void sleep(int temps){
        long get = getTime();
        while(getTime()-get<temps){ 
        }
    }
    //Fonction d'ériture pour avoir les caractères print les uns après les autres comme dans un RPG
    void ecrireLent(String chaine, int vitesse){
        for(int i = 0; i<length(chaine); i++){
            print(charAt(chaine, i));
            sleep(vitesse);
        }

    }

    //------------------/Fonctions d'items\----------------//
    
    //Fonction de rangement de l'inventaire (les items vides à la fin)

    //Fonction de tri de l'inventaire par ID croissant



    void rangerInventaire(Joueur j){
        for(int i=0; i<length(j.inventaire);i++){
            if(j.inventaire[i].id==0){
                for(int k=i+1; k<length(j.inventaire);k++){
                    if(j.inventaire[k].id!=0){
                        Item temp = j.inventaire[i];
                        j.inventaire[i] = j.inventaire[k];
                        j.inventaire[k] = temp;
                    }
                }
            }
        }
    }

    void test_rangerInventaire(){
        Joueur j = nouvJoueur("Test",1,0,0,3);
        j.inventaire[0] = nouvObjet(1,"","");
        j.inventaire[1] = nouvObjet(0,"","");
        j.inventaire[2] = nouvObjet(2,"","");
        j.inventaire[3] = nouvObjet(0,"","");
        rangerInventaire(j);
        assertTrue(j.inventaire[0].id==1);
        assertTrue(j.inventaire[1].id==2);
        assertTrue(j.inventaire[2].id==0);
        assertTrue(j.inventaire[3].id==0);
    }




    //Fonction d'ajout d'item à l'inventaire
    int disponible(Joueur j, int id){
        for(int i = 0; i<length(j.inventaire);i++){
            if(j.inventaire[i].id==id){
                return i;
            }
        }
        return -1;
    }
    int emplacementVide(Joueur j){
        return disponible(j,0);
    }

    boolean ajouterObjet(Item i){
        int disp = emplacementVide(joueurActuel);
        if(disp<0){
            println("Vous n'avez pas la place dans votre inventaire !");
            return false;
        }else{
            joueurActuel.inventaire[disp] = i;
            println(i.nom + " à bien été ajouté à votre inventaire !");
            return true;
        }
    }

    //Fonction pour Enlever des items à l'inventaire
    String retirerObjet(Item i){
        if(disponible(joueurActuel, i.id)<0){
            return "Vous n'avez pas cet item dans votre inventaire !";
        }else{
            int disp = disponible(joueurActuel, i.id);
            joueurActuel.inventaire[disp] = OBJET_VIDE;
            return i.nom + "à bien été retiré de votre inventaire !";
        }
    }

    //Fonction pour avoir les infos sur les items
    String descObjet(Item i){
        return i.nom + ": " +i.description;
    }

    //-----------------------------------------------------//

    //-----------------/Fonctions de combat\---------------//

    boolean combatMonstre(Monstre m){
        println("Un monstre se dresse devant vous !\n");
        while(m.pv > 0 && joueurActuel.pv > 0){
            println(affichageCombat(m));
            if(!demanderQuestion(m.verbe, m.color)){
                //mauvaise réponse
                degatsJoueur();
            }else{
                //bonne réponse
                degatsMonstre(m, 40 + random(1,5));
                changerVerbe(m,1);
            }
        }
        if(joueurActuel.pv == 0){
            return false;
        }else{
            return true;
        }
    }
    boolean[] motsVisibles(int color){
        boolean[] affiche = new boolean[]{true, true, true, true};
        if(color == 0){affiche[0] = false;}
        if(color == 1 || color == 4 || color == 6 || color == 7){affiche[1] = false;}
        if(color == 2 || color == 4 || color == 5 || color == 7){affiche[2] = false;}
        if(color == 3 || color == 6 || color == 5 || color == 7){affiche[3] = false;}
        return affiche;
    }
    void affichageQuestion(Verbe v, boolean[] affiche){
        if(affiche[0]){print(v.fr+" ; ");}else{print(" ______ ; ");}
        if(affiche[1]){print(v.bv+" ; ");}else{print(" ______ ; ");}
        if(affiche[2]){print(v.pr+" ; ");}else{print(" ______ ; ");}
        if(affiche[3]){println(v.pp);}else{println(" ______");}
        println("\nQuel est le verbe manquant ?");
        println("\n"+affichageReduit(joueurActuel));

    }

    boolean verifierReponse(Verbe v, int color){
        int nbReponses = (color-1) / 3 + 1;
        while(nbReponses > 0){
            String reponse = lireEntree();
            
            if(color == 0){                                          if(equals(reponse,v.fr)){return true;}else{return false;}}

            if(color == 1 || color == 4 || color == 6 || color == 7){if(equals(reponse,v.bv)){return true;}else{return false;}}
            if(color == 2 || color == 4 || color == 5 || color == 7){if(equals(reponse,v.pr)){return true;}else{return false;}}
            if(color == 3 || color == 6 || color == 5 || color == 7){if(equals(reponse,v.pp)){return true;}else{return false;}}
            nbReponses -= 1;       
        }
        return false;
    }

    boolean demanderQuestion(Verbe v, int color){
        // retourne true si bonne réponse, false sinon.

        //définition de quels mots sont visibles:
       boolean[] affiche = motsVisibles(color);

        //print de la question
        affichageQuestion(v, affiche);

        //definition 

        return verifierReponse(v, color);
    }

    //Il faut faire un truc qui change le verbe du monstre (+sa couleur si on veut) si il lui reste des PV.
    //Peut être un type de monstre qui peut en plus changer de couleur.
    void changerVerbe(Monstre m, int level){
        m.verbe = genererVerbe(1);
    }
    

    //-----------------------------------------------------//

    //---------------/Fonctions de Donjon\-----------------//

    void parcourirDonjon(int niveau){
        Monstre[] floor = genererEtage(niveau);
        boolean alive = true;
        for(int i = 0; i<length(floor); i++){
            if(alive && combatMonstre(floor[i])){
                println("Bravo ! Le monstre à été vaincu");
                gainXP(100+random(1,20));
                gainOr(10+random(1,30));
                
            }else{
                alive = false;
            }
        }
        if(!alive){
            println("Aie ! vous avez perdu !, vous vous soignez et vous remettez en route...\n");
            joueurActuel.pv = joueurActuel.pvMax;
        }else{
            println("Bien joué, vous avez triomphé(e) du Donjon !\nMerci d'avoir joué à l'alpha\n");
        }
    }

    Monstre[] genererEtage(int level){
        int size = (level*2)+(level/2)+1;
        Monstre[] floor = new Monstre[size];
        for(int i=0; i<size; i++){
            floor[i] = genererMonstre(level);
        }
        return floor;
    }

    Monstre genererMonstre(int level){
        if(level == 1){
            int color = random(1,3);//trois couleurs primaires
            return nouvMonstre(color, genererVerbe(level));
        }else if(level == 2){
            int color = random(0,3);//précédents + blanc
            return nouvMonstre(color, genererVerbe(level));
        }else if(level == 3){
            int color = random(0,6);//précédents + couleurs secondaires
            return nouvMonstre(color, genererVerbe(level));
        }else{
            int color = random(0,7);//précédents + noir (toutes les couleurs)
            return nouvMonstre(color, genererVerbe(level));
        }
    }
    void test_genererMonstre(){
        initJeu(2);
        Monstre m = genererMonstre(1);
        println(toString(m));
    }

    Verbe genererVerbe(int level){
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
        return TOUS_VERBES[resultId];
    }
    void test_genererVerbe(){
        initJeu(2);
        Verbe verbeG = genererVerbe(1);
        println(toString(verbeG));
    }
    //-----------------------------------------------------//

    //---------------/Fonctions de Combat\-----------------//

    void degatsMonstre(Monstre monstre, int damage){
        monstre.pv = monstre.pv - damage;
        if(monstre.pv < 0){
            monstre.pv = 0;
        }
        println("BAM ! le monstre à perdu"+damage+"pv!\n");
    }
    void degatsJoueur(){
        joueurActuel.pv = joueurActuel.pv - 1;
        if(joueurActuel.pv == 0){
            //deathJoueur();
        }
        println("OUCH !\n");
    }
    void soinsJoueur(){
        if(joueurActuel.pv < joueurActuel.pvMax){
            joueurActuel.pv += 1;
        }
    }
    void gainXP(int xpGagne){
        joueurActuel.xp += xpGagne;
        if(joueurActuel.level == 1 && joueurActuel.xp >= XP_REQUIS_1){joueurActuel.xp -= XP_REQUIS_1;gainNiveau();}
        if(joueurActuel.level == 2 && joueurActuel.xp >= XP_REQUIS_2){joueurActuel.xp -= XP_REQUIS_2;gainNiveau();}
        if(joueurActuel.level == 3 && joueurActuel.xp >= XP_REQUIS_3){joueurActuel.xp -= XP_REQUIS_3;gainNiveau();}

    }
    void gainNiveau(){
        println("\nVous avez gagné un niveau !!!");
        joueurActuel.level += 1;
    }
    
    void gainOr(int orGagne){
        joueurActuel.gold += orGagne;
    }

    
    void test_gainOr(){
        initJeu(2);
        int oldGold = joueurActuel.gold;
        gainOr(50);
        assertTrue(joueurActuel.gold == oldGold + 50);
    }    
    //-----------------------------------------------------//

    //-----------------/Fonctions de CSV\------------------//



    //Fonctions pour lire le fichier de sauvegarde csv
    Joueur[] lireSauvegardes(String fichier){
        CSVFile f = loadCSV("csv/"+fichier);
        Joueur[] sauvegardes = new Joueur[rowCount(f)];
        int nbJoueurs = 0;
        for(int l = 1; l<rowCount(f); l++){
            sauvegardes[l] = nouvJoueur(getCell(f, l, 0),
            stringVersInt(getCell(f,l,1)),
            stringVersInt(getCell(f,l,2)),
            stringVersInt(getCell(f,l,3)),
            stringVersInt(getCell(f,l,4)));
            sauvegardes[l].pvMax = PV_MAX_JOUEUR;
            sauvegardes[l].livre = nouvLivre(f,l);
            sauvegardes[l].inventaire = nouvInventaire(f,l);
            nbJoueurs += 1;
        }
        return sauvegardes;
    }
    

    //Fonction pour enregistrer les verbes d'un csv en tableau
    Verbe[] tousVerbes(String file){
        CSVFile f = loadCSV("csv/"+file);
        Verbe[] livre = new Verbe[rowCount(f)-1];
        for(int l = 0; l < length(livre); l++){
            livre[l] = nouvVerbe(stringVersInt(getCell(f,l+1,0)),
            getCell(f,l+1,1),
            getCell(f,l+1,2),
            getCell(f,l+1,3),
            getCell(f,l+1,4),
            stringVersInt(getCell(f,l+1,5)));
        }
        return livre;
    }
    //Fonctionpour avoir le tableau de verbe d'un joueur a partir de son csv
    Verbe[] nouvLivre(CSVFile save, int ligne){
        String livreString = getCell(save,ligne,5);
        int[] nombres = stringVersTab(livreString);
        int nbVerbes = length(nombres);
        Verbe[] livre = new Verbe[nbVerbes];
        for(int i = 0; i<nbVerbes; i++){
            livre[i] = TOUS_VERBES[nombres[i]];
        }
        return livre;
    }
    //Fonction String de chiffres en int: "123" => 123
    int stringVersInt(String input){
        int result = 0;
        int power = 1;
        for(int i = length(input)-1; i >= 0; i=i-1){
            result += (charAt(input,i)-'0') * power;
            power = power * 10;
        }
        return result;
    }
    void test_stringVersInt(){
        assertTrue(123 == stringVersInt("123"));
        assertTrue(12345 == stringVersInt("12345"));
    }

    int[] tronquer(int[] tab, int indice){
        int[] resultat = new int[indice];
        for(int i=0;i<length(resultat);i++){
            resultat[i] = tab[i];
        }
        return resultat;
    }

    //Fonction "1-02-10-25-0" => [1, 2, 10, 25, 0]
    int[] stringVersTab(String numbers){
        int[] n = new int[length(numbers)];
        int num = 0;
        int indice = 0;
        for(int i=0;i<length(numbers);i++){
            if(charAt(numbers,i)!='-'){
                num = num*10 + charAt(numbers,i)-'0';
            }else{
                n[indice] = num;
                indice += 1;
                num=0;
            }
        }
        int[] result = tronquer(n,indice);
        return result;
    }
    void test_stringVersTab(){
        int[] n = new int[]{1,2,10,25};
        int[] m = stringVersTab("1-02-10-25-"); 
        assertEquals(n[0], m[0]);
        assertEquals(n[1], m[1]);
        assertEquals(n[2], m[2]);
        assertEquals(n[3], m[3]);
    }

    Item[] tousObjets(String file){
        CSVFile f = loadCSV("csv/"+file);
        Item[] inv = new Item[rowCount(f)-1];
        for(int l = 0; l<length(inv);l++){
            inv[l] = nouvObjet(stringVersInt(getCell(f,l+1,0)), getCell(f,l+1,1), getCell(f,l+1,2));
        }
        return inv;
    }
    Item[] nouvInventaire(CSVFile save, int ligne){
        Item[] inv = new Item[MAX_OBJETS];
        int[] nombres = nombreObjetVersTab(getCell(save,ligne,6));
        for(int i = 0; i < length(inv); i++){
            inv[i] = TOUS_OBJETS[nombres[i]];
        }
        return inv;
    }
    int[] nombreObjetVersTab(String numbers){
        //numbers forcement de forme "1234"
        int[] n = new int[MAX_OBJETS];
        n[0] = charAt(numbers,0)-'0';
        n[1] = charAt(numbers,1)-'0';
        n[2] = charAt(numbers,2)-'0';
        n[3] = charAt(numbers,3)-'0';
        return n;
    }
    void test_nombreObjetVersTab(){
        int[] n = new int[]{1,2,3,4};
        assertTrue(n[0]==nombreObjetVersTab("1234")[0]);
        assertTrue(n[1]==nombreObjetVersTab("1234")[1]);
        assertTrue(n[2]==nombreObjetVersTab("1234")[2]);
        assertTrue(n[3]==nombreObjetVersTab("1234")[3]);
    }
    



    //Fonction pour sauvegarder dans le fichier de sauvegarde le joueur actuel (Joueur courant + autrs joueurs => save(String[][], sauvegardes.csv)
    
    //-----------------------------------------------------//

    //-----------------/Fonctions de .txt\-----------------//

    //Fonction lecture de Sprites + stockage dans un string

    String lectureArt(String chaine){
        File f=newFile(chaine);
        String retour = "";
        while(ready(f)){
            retour += readLine(f) + "\n";
        }
        return retour;

    }    
    //Fonction d'affichage de Sprites

    void affichageArt(String chaine){
        for(int i=0;i<length(chaine);i++){
            print(charAt(chaine,i));
        }
    }

    //-----------------/Fonctions de Debug\----------------//
    String toString(String[] s) {String result = "";for(int i=0;i<length(s);i++){result += s[i]+"; ";}result += "\n";return result;}
    String toString(Verbe v)    {return "ID: "+v.id +" "+v.fr +" "+ v.bv +" "+ v.pr +" "+ v.pp +" lv"+ v.level;}
    String toString(Verbe[] v)  {String result = "";for(int i=0;i<length(v);i++){result += toString(v[i])+";\n";}return result;}
    String toString(Joueur j)   {return "Nom:"+j.nom +" "+"niveau:"+j.level +" "+"xp:"+j.xp +" "+"gold:"+j.gold +" "+"pv:"+j.pv +"\n\nVerbes :\n"+toString(j.livre)+"\nItems :\n"+toString(j.inventaire);}
    String toString(Joueur[] s) {String res = "";for(int i=1;i<length(s);i++){res+= toString(s[i])+"\n";}return res;}
    String toString(Monstre m)  {return "PvMax: "+m.pvMax+" Pv: "+m.pv +" Couleur: "+m.color+" "+COULEURS[m.color]+" xpGiven: "+m.xpGiven+" goldGiven: "+m.goldGiven+" VERBE: "+toString(m.verbe);}
    String toString(Monstre[] f){String res="";for(int i=0;i<length(f);i++){res+=toString(f[i])+";\n";}return res;}
    String toString(Item i)     {return "ID: "+i.id+" Nom: "+i.nom+" Description: "+i.description;}
    String toString(Item[] inv) {String result="";for(int i=0;i<length(inv);i++){result+=toString(inv[i])+";\n";}return result;}

    
    
}   
