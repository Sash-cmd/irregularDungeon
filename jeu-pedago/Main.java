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
    final Verbe[] ALL_VERBES = allVerbes("verbes.csv");
    Joueur joueurActuel;
    
    void algorithm(){println(toString(ALL_VERBES));
    }

    //---------------/Fonctions de toString\---------------//
    String toString(String[] s){String result = "";for(int i=0;i<length(s);i++){result += s[i]+"; ";}result += "\n";return result;}
    String toString(Verbe v){return "ID: "+v.id +" "+v.fr +" "+ v.bv +" "+ v.pr +" "+ v.pp +" "+ v.level;}
    String toString(Verbe[] v){
        String result = "";
        for(int i=0;i<length(v);i++){
            result += toString(v[i])+"; \n";
            }
        return result;
    }
    String toString(Joueur j){return "Nom: "+j.nom +" "+"niveau:"+j.level +" "+"xp: "+j.xp +" "+"gold: "+j.gold +" "+"pv: "+j.pv +"\n"+toString(j.livre)+"\n"+toString(j.inventaire);}
    String toString(Monstre m){return "ID: "+m.id+" PvMax: "+m.pvMax+" Pv: "+m.pv +" Couleur: "+m.color+" "+COLORS[m.color]+" xpGiven: "+m.xpGiven+" goldGiven: "+m.goldGiven+" VERBE: "+toString(m.verbe);}
    String toString(Item i){return "ID: "+i.id+" Nom: "+i.nom+" Description: "+i.description;}
    //-----------------------------------------------------//


    //-------------------/Constructeurs\-------------------//
    Joueur newJoueur(String nom, int level, int xp, int gold, int pv){
        /*Initialisation du joueur*/ Joueur j = new Joueur();
        if(length(nom) != 0){j.nom = nom;}else{j.nom = "Kévin Jourdin";}
        j.level = level;j.xp = xp;
        j.gold=gold;j.pvMax = 3;
        j.pv = pv;
        j.livre = new Verbe[MAX_VERBES];
        j.inventaire = new String[MAX_ITEMS];
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
    Monstre newMonstre(int nbMonstres, int color, Verbe verbe){
        /*Initialisation du monstre*/
        Monstre m = new Monstre();
        m.id = nbMonstres;
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
    
    //Il faut faire un truc qui change le verbe du monstre (+sa couleur si on veut) si il lui reste des PV.
    //Peut être un type de monstre qui peut en plus changer de couleur.
    void changeVerb(Monstre m){
        //m.verbe.level;
    }
    
    //Fonction pour transformer le csv en plusieurs Tableaux de verbes selon les differents niveaux

    //Fonction d'ériture pour avoir les caractères print les uns après les autres comme dans un RPG

    //Fonction d'ajout d'item à l'inventaire

    //Fonction pour Enlever des items à l'inventaire

    //Fonction pour avoir les infos sur les items

    //Fon
    
 
    //-----------------/Fonctions de CSV\------------------//

    //Fonctions pour lire le fichier de sauvegarde csv
    /*
    Joueur[] readSauvegardes(String fichier){
        CSVFile f = loadCSV("csv/"+fichier);
        Joueur[] sauvegardes = new Joueur[rowCount(f)];
        int nbJoueurs = 0;
        for(int l = 1; l<rowCount(f); l++){
            sauvegardes[l] = newJoueur(getCell(f, l, 0),getcell(f,l,1),getcell(f,l,2),getcell(f,l,3),getcell(f,l,4))
            sauvegardes[l].pvMax = PV_MAX_JOUEUR;
            sauvegardes[l].livre = newLivre(f,l);
            sauvegardes[l].inventaire = newInventaire(f,l);
            nbJoueurs += 1;
        } 
    }
    */

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

    Verbe[] newLivre(CSVFile f, int ligne){
        String livreString = getCell(f,ligne,5);
        int[] nombres = StringIntoInt(livreString);
        int nbVerbes = length(nombres);
        Verbe[] livre = new Verbe[nbVerbes];
        for(int i = 0; i<nbVerbes; i++){

        }
        return livre;
    }
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
    int[] StringIntoInt(String numbers){
        int[] n = new int[length(numbers)/2];
        for(int i = 0; i<length(n); i++){
            n[i] = 10*(charAt(numbers, i*2)-'0')+(charAt(numbers, i*2+1)-'0');
            println(n[i]);
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


    //Fonction pour sauvegarder dans le fichier de sauvegarde le joueur actuel (Joueur courant + autrs joueurs => save(String[][], sauvegardes.csv)
    
    //-----------------------------------------------------//

    //-----------------/Fonctions de .txt\-----------------//

    //Fonction lecture de Sprites + stockage dans un string

    //Fonction d'affichage de Sprites
    
    //-----------------------------------------------------//

    
}   