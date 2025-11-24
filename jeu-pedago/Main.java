class Main extends Program {
    //VARIABLES GLOBALES 
    final String[] COLORS = new String[]{"Black", "Red", "Green", "Blue", "Yellow", "Cyan", "Purple", "White"};
    final int MONSTRE_PV_RANDOM = 20 // De combien les PVs peuvent varier 
    final int MONSTRE_PV_BASE = 50 // De +0 à +MONSTRE_PV_RANDOM cette valeur
    final int MONSTRE_PV_PAR_LEVEL = 30 // De combien les PVs montent par niveau de monstre.
    
    void algorithm(){}

    void toString(Verbe v){println(v.fr +" "+ v.bv +" "+ v.pr +" "+ v.pp +" "+ v.level);}
    void toString(Joueur j){println("Nom: "+j.nom +" "+"niveau:"+j.level +" "+"xp: "+j.xp +" "+"gold: "+j.gold +" "+"id_joueur"+j.id);}
    void toString(Monstre m){println("ID: "m.id +" Niveau: "+m.niveau +" PvMax: "+m.pvMax+" Pv: "+m.pv +" Couleur: "+m.color+COLOR[m.color]);}

    Joueur newJoueur(int nbJoueurs, String nom){/*Initialisation du joueur*/ Joueur j = new Joueur();j.id = nbJoueurs;if(length(nom) != 0){j.nom = nom;}j.level = 1;j.xp = 0;j.gold=0;j.pvMax = 3;j.pv = j.pvMax;}
    
    //Il faut faire un truc qui change le verbe du monstre (+sa couleur si on veut) si il lui reste des PV.
    //Peut être un type de monstre qui peut en plus changer de couleur.
    Monstre newMonstre(int nbMonstres, int color, Verbe verbe){
        /*Initialisation du monstre*/
        Monstre m = new Monstre();
        m.id = nbMonstres;
        m.color = color;
        m.verbe = verbe;
        m.pvMax = MONSTRE_PV_BASE + MONSTRE_PV_RANDOM*random() + MONSTRE_PV_PAR_LEVEL*m.verbe.level; // 50-70 PVs de base, +30 à chaque niveau en plus.
        m.pv = pvMax;
        m.xpGiven = (90+20*random()+*m.verbe.level*((m.color-1)/3))/2; //formule pour l'XP prenant en compte le niveau du verbe, et la couleur du monstre.
        m.goldGiven = xpGiven/3+25*random();
        return m;
    }
}   
