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
    
    void algorithm(){}

    //---------------/Fonctions de toString\---------------//
    String toString(String[] s){String result = "";for(int i=0;i<length(s);i++){result += s[i]+"; ";}result += "\n";return result;}
    String toString(Verbe v){return v.fr +" "+ v.bv +" "+ v.pr +" "+ v.pp +" "+ v.level;}
    String toString(Verbe[] v){String result = "";for(int i=0;i<length(v);i++){result += v[i]+"; ";}result += "\n";return result;}
    String toString(Joueur j){return "Nom: "+j.nom +" "+"niveau:"+j.level +" "+"xp: "+j.xp +" "+"gold: "+j.gold +" "+"pv: "+j.pv +" "+"id_joueur"+j.id +"\n"+toString(j.livre)+toString(j.inventaire);}
    String toString(Monstre m){return "ID: "+m.id+" PvMax: "+m.pvMax+" Pv: "+m.pv +" Couleur: "+m.color+" "+COLORS[m.color]+" xpGiven: "+m.xpGiven+" goldGiven: "+m.goldGiven+" VERBE: "+toString(m.verbe);}
    //-----------------------------------------------------//


    //-------------------/Constructeurs\-------------------//
    Joueur newJoueur(int nbJoueurs, String nom){
        /*Initialisation du joueur*/ Joueur j = new Joueur();
        j.id = nbJoueurs;
        if(length(nom) != 0){j.nom = nom;}
        j.level = 1;j.xp = 0;
        j.gold=0;j.pvMax = 3;
        j.pv = j.pvMax;
        j.livre = new Verbe[MAX_VERBES];
        j.inventaire = new String[MAX_ITEMS];
        return j;
    }
    Verbe newVerbe(String fr, String bv, String pr, String pp, int level){
        /*Initialisation du verbe*/Verbe v = new Verbe();
        v.fr= fr;
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

    //Fonction pour avoir la taille d'un csv

    //Fonction pour enregistrer les verbes d'un csv en tableau

    //Fonction pour lire le fichier de sauvegarde csv

    //Fonction pour sauvegarder dans le fichier de sauvegarde le joueur actuel (Joueur courant + autrs joueurs => save(String[][], sauvegardes.csv)
    
    //-----------------------------------------------------//

    //-----------------/Fonctions de .txt\-----------------//

    //Fonction lecture de Sprites + stockage dans un string

    //Fonction d'affichage de Sprites
    
    //-----------------------------------------------------//

    
}   
