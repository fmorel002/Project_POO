package simpleUIApp;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import java.awt.geom.Point2D;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Planet extends Entity implements Serializable{

	public enum PlanetType {
		PLAYER, IA, NEUTRAL;
	}

	private static ArrayList<SpaceShip> shipsAllPlanets; // Liste des Vaisseaux
	private static ArrayList<Planet> allPlanets; // Liste des planètes
	private static ArrayList<Item> allItem; // Liste de tous les éléments du jeu
	
	// Variables gérant la production de vaisseaux des planètes joueurs ( change à chaque partie)
	private final static int speedProductionMax = 1000;
	private final static int speedProductionMin = 300;
	private static int speedProduction;
	
	// Variables gérant la production de vaisseaux des planètes neutres ( change à chaque partie)
	private final static int speedProductionNeutralMax = 2000;
	private final static int speedProductionNeutralMin = 1500;
	private static int speedProductionNeutral;
	private boolean sick; // indique si la planète est malade
	
	// Taille min/max des palnètes
	private final static int widthMax = 70;
	private final static int widthMin = 40;

	// Nombre de vaisseaux max par escadrons
	private final static int shipsByWaves = 15;
	
	// Type de planète (NEUTRAL/IA/PLAYER)
	private PlanetType type;
	
	// Nombre de vaisseaux sur la planète
	private int nbShip = 0;
	
	// Nombre de wave de vaisseaux à envoyer
	private int cptWaves = 0;

	public Planet(double x, double y, int width, PlanetType type, ArrayList<Item> it,
				  ArrayList<Planet> planets) {

		super(x, y, width, new Color(119, 136, 153));
		this.type = type;
		if (type == PlanetType.PLAYER)
			this.setColor(new Color(46, 139, 87));
		else if (type == PlanetType.IA)
			this.setColor(new Color(165, 42, 42));
		
		// Par défaut une planète n'est pas malade
		this.sick = false;
		
		// Si la planète est neutre, on lui attribue une petite chance d'être malade
		if(this.type == PlanetType.NEUTRAL)
		{
			if(Math.round(Math.random() * 11) > 8)
			{
				this.sick = true;
				System.out.println("1");
			}
			else
				System.out.println("0");
		}
		
		allItem = it;
		allPlanets = planets;

	}
	
	/**
	 *  Genere une taille de planète aléatoire compris entre widthMax et widthMin
	 * @return la taille générée
	 */
	public static int generatePlanetWidth(){
		Random r = new Random();
		return r.nextInt(widthMax - widthMin) + widthMin;
	}
	
	/**
	 *  Genere une vitesse de production de vaisseaux aléatoirement pour une planète JOUEUR compris entre speedProductionMax et speedProductionMin
	 */
	public static void setPlanetProduction(){
		Random r = new Random();
		speedProduction = r.nextInt(speedProductionMax - speedProductionMin) + speedProductionMin;
	}
	
	public static int getSpeedProduction(){
		return speedProduction;
	}
	
	/**
	 *  Genere une vitesse de production de vaisseaux aléatoirement pour une planète NEUTRE compris entre speedProductionNeutralMin et speedProductionNeutralMin
	 */
	public static void setPlanetProductionNeutral(){
		Random r = new Random();
		speedProductionNeutral  = r.nextInt(speedProductionNeutralMax - speedProductionNeutralMin) + speedProductionNeutralMin;
	}
	
	public static int getSpeedProductionNeutral(){
		return speedProductionNeutral;
	}

	public static void setShipsAllPlanets(ArrayList<SpaceShip> ships) {
		shipsAllPlanets = ships;
	}

	public PlanetType getType() {
		return this.type;
	}
	
	/**
	 *  Met à jour la planète en fonction des entrées des vaissaux (Changement de propriétaire, MAJ du nombre de vaisseaux)
	 */
	public void shipsArrived(){
		
		Iterator<SpaceShip> it = shipsAllPlanets.iterator();
		
		while(it.hasNext()){
			SpaceShip ss = it.next();
				if (ss.getBelongs() == this) { 
								
				// Si un ship arrive sur une planète on le
				if(this.contains(ss.getLocation()) && !ss.getMoving())
				{
					// Si c'est un vaisseau du joueur
					if(!ss.IsIaShip())
					{
						if (this.type == PlanetType.PLAYER)
						{
							this.nbShip ++;
						}
						else if(this.type == PlanetType.IA || this.type == PlanetType.NEUTRAL){
							this.nbShip--;
							if(this.nbShip < 0){
								this.type = PlanetType.PLAYER;
								this.setColor(new Color(46, 139, 87));
								this.nbShip = this.nbShip *(-1);
							}
						}
					}
					// Si c'est un vaisseau de l'IA
					else
					{
						if (this.type == PlanetType.IA)
						{
							this.nbShip ++;
						}
						else if(this.type == PlanetType.PLAYER || this.type == PlanetType.NEUTRAL){
							this.nbShip--;
							if(this.nbShip < 0){
								this.type = PlanetType.IA;
								this.setColor(new Color(165, 42, 42));
								this.nbShip = this.nbShip *(-1);
							}
						}
					}
					it.remove();
					allItem.remove(ss);
				}
			}
		}
	}

	/*
	 *  Appelé par un timer cette fonction incrémente de 1 le nombre de vaisseau sur la planète
	 */
	public void update() {
		if (this.getType() != PlanetType.NEUTRAL && !this.sick)
			nbShip++;
		// Si malade
		else if(this.sick && this.getType() != PlanetType.NEUTRAL )
		{
			// La planète a 1/2 de produire des vaisseaux
			if(Math.round(Math.random()) > 0.5)
			{
				// On produit un nombre random de vaisseaux
				nbShip += Math.round(Math.random() * 4);
			}
		}
	}

	/*
	 *  Envoie des vagues de vaisseaux depuis la planète d'origine d'un montant de vaissaux égale à shipsByWaves
	 * @param it : Liste d'item
	 * @param waves : nombre de wave à envoyer 
	 */
	public boolean sendWave(Item it, int waves){
		
		// On créer une liste temporaire des vaisseaux à ajouter
		ArrayList<SpaceShip> tmp = new ArrayList<SpaceShip>();

		// On génère un identifiant unique pour l'esacdron
		int waveID = (int)(Math.random() * 100001);
		
		// Pour chaque vague on attrivue une position unique par vaisseaux
		for (int i = 0; i < shipsByWaves; i++) {
			double xCircle = (double)this.center.getX() + ((double)(this.getWidth()+10)/2) * Math.cos(Math.toRadians((360/shipsByWaves) * i));
			double yCircle = (double)this.center.getY() + ((double)(this.getWidth()+10)/2) * Math.sin(Math.toRadians((360/shipsByWaves) * i));

			// On généère le vaisseau
			SpaceShip ss = new SpaceShip(xCircle, yCircle , 10, this);

			// On lui donne son objectif
			ss.setObjective(it);
			
			// On défini son escadron
			ss.setEscadronID(waveID);
			
			// On ajoute dans la liste des vaisseaux le nouveau
			shipsAllPlanets.add(ss);
			
			// idem dans la liste temporaire
			tmp.add(ss);	
		}
		
		// On ajoute une seule fois la liste temporaire dans allItem pour limiter la concurence
		allItem.addAll(tmp);
		
		this.cptWaves++;
		if(this.cptWaves == waves){
			this.cptWaves = 0;
			return true;
		}
		else{
			return false;
		}
	}

	/*
	 *  Envoie des vaisseaux en fonction des paramètre envoyer par l'utilisateur via le clavier
	 * @param it : Liste d'item
	 * @param key : touche du clavier
	 */
	public void generateShips(Item it, String key) {
		
		// On définie la proportion de vaissaux à envoyer
		int percentToSend = 0;
		if(key == "UNKNOWN")
			percentToSend = 2;
		else if(key == "CRTL")
			percentToSend = 3;
		else if(key == "SHIFT")
			percentToSend = 1;
		
		int shipsToSend = nbShip/percentToSend;
		int nbWaves = shipsToSend / shipsByWaves;
		int shipsLeft = shipsToSend % shipsByWaves;
		if(nbWaves > 0){
			Lock l = new ReentrantLock();
			l.lock();
			try {
				// Toute les 1 seconde on apelle sendwave pour envoyer la vague suivante
				final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
				executorService.scheduleAtFixedRate(new Runnable() {
					@Override
					public void run() {
						boolean allSent = false;
						allSent = sendWave(it, nbWaves);
						if(allSent == true){
							executorService.shutdown();
						}
					}
				}, 1, 1, TimeUnit.SECONDS);
			} finally {
				l.unlock();
			}
		}
		
		// On génère un ID pour l'esacdron
		int waveID = (int)(Math.random() * 100001);
		
		
		// Envoie les nombres de vaisseaux si < shipsByWaves.
		for (int j = 0; j < shipsLeft; j++) {
			double xcircle = (double)this.center.getX() + ((double)(this.getWidth()+10)/2) * Math.cos(Math.toRadians((360/shipsLeft) * j));
			double ycircle = (double)this.center.getY() + ((double)(this.getWidth()+10)/2) * Math.sin(Math.toRadians((360/shipsLeft) * j));

			SpaceShip ss = new SpaceShip(xcircle, ycircle , 10, this);

			ss.setEscadronID(waveID);
			ss.setObjective(it);
			shipsAllPlanets.add(ss);
			allItem.add(ss);
		}
		nbShip = nbShip - shipsToSend;
	}
	
	
	/**
	 * @param p : Point2D
	 * @return  retourne la planète a des coordonées spécifique sinon null
	 */
	public static Planet getPlanetFromCoord(Point2D p) {
		for (Planet plan : allPlanets) {
			if (plan.contains(p))
				return plan;
		}
		return null;
	}

	@Override
	public void draw(Graphics2D arg0) {
		Point2D pos = this.center;
		int x = (int) pos.getX(), y = (int) pos.getY(), w = this.getWidth();
		arg0.setColor(c);
		// arg0.fillRect(x - w / 2, y - w / 2, w, w);
		arg0.fillOval(x - w / 2, y - w / 2, w, w);
		
		// Si la planète est malade, on effectue un contour vert
		if(this.sick == true)
		{
			arg0.setColor(new Color(69,200,51));
			arg0.setStroke(new BasicStroke(3));
			arg0.drawOval(x - w / 2, y - w / 2, w, w);
		}
			
		arg0.setColor(Color.black);
		String text = Integer.toString(nbShip);
		arg0.drawString(text, (int) pos.getX() - arg0.getFontMetrics().stringWidth(text) / 2, (int) pos.getY() + 5);
	}

	/**
	 *  Cherche une position valide pour la planète
	 * @param width
	 *            : largeur fenetre
	 * @param height
	 *            : hauteur fenetre
	 * @param planets
	 *            : listes des planètes
	 * @param planetSize
	 *            : Taille de la planète
	 * @return valide position sinon -1
	 */
	public static Point2D findPlanetPosition(int width, int height, ArrayList<Planet> planets, int planetSize) {
		final int nbTry = 1000; // nombre d'essai max pour trouver une pos valide
		final int spacing = planetSize + 30; // on choisi l'espacement entre les planï¿½tes
		boolean isValidPos = true;
		Random random = new Random();
		Point2D pos = new Point2D.Double(0, 0);

		int nbTryCpt = 0;

		do {
			int x = random.nextInt((width - (planetSize / 2)) + 1 - (planetSize / 2)) + (planetSize / 2);
			int y = random.nextInt((height - (planetSize / 2)) + 1 - (planetSize / 2)) + (planetSize / 2);
			pos.setLocation(x, y);

			// Si c'est la premiï¿½re planete, son emplacement est forcement valide
			if (planets.size() == 0)
				isValidPos = true;

			for (Planet p : planets) {
				if (p.getLocation().distance(pos) < spacing)
					isValidPos = false;
			}

			nbTryCpt++;

		} while (!isValidPos && nbTryCpt < nbTry);

		// Au bout de "nbTry" essais, on arrï¿½te de chercher une position valide et on
		// affiche une erreur
		if (nbTryCpt == nbTry) {
			pos.setLocation(-1, -1);
		}
		return pos;

	}

	/*
	 *  Incrémente le nombre de vaisseau des planètes neutres
	 */
	public void updateNeutral() {
		if(this.type == PlanetType.NEUTRAL && !this.sick){
			nbShip++;
		}
		// Si malade
		else
		{
			// La planète a 1/2 de produire des vaisseaux
			if(Math.round(Math.random()) > 0.5)
			{
				// On produit un nombre random de vaisseaux
				nbShip += Math.round(Math.random() * 4);
			}
		}
	}
	
	/*
	 *  Intelligence Articificiel du personnage non joueur
	 */
	public static void updateIA()
	{
		// 1 : On récupère la liste des planètes posséder par l'IA
		ArrayList<Planet> listIaPlanets = new ArrayList<Planet>();
		for (Planet p : allPlanets) {
			if (p.getType() == PlanetType.IA)
				listIaPlanets.add(p);
		}
		
		// 2 : On selectionne une planète aléatoire de cette liste
		// Cette planète sera la planète attaquante
		Random rand = new Random();
		
		// SI l'IA possède des planètes
		if(listIaPlanets.size() > 0 )
		{
			int planetSelected = rand.nextInt(listIaPlanets.size());
			
			// 3 : On selectionne une planète cible
			// Cette planète sera la planète attaqué
			// L'IA peut s'envoyer des troupes vers ses propres planète
			// dans ce cas la c'est juste un ajout de troupes
			int planetObjectiveSelected = rand.nextInt(allPlanets.size());
			
			// 4 : Si la planète cible est la même que la planète de départ
			// on selectionne une autre planète
			while(listIaPlanets.get(planetSelected) == allPlanets.get(planetObjectiveSelected))
				planetObjectiveSelected = rand.nextInt(allPlanets.size());
			
			// On envoi les vaisseaux
			listIaPlanets.get(planetSelected).setObjective(allPlanets.get(planetObjectiveSelected));
			listIaPlanets.get(planetSelected).generateShips(allPlanets.get(planetObjectiveSelected), "UNKNOWN");
		}
	}

	/*
	 *  Met à jour l'objectif de tous les vaisseau posséder l'ID de l'escadron passé en paramètre
	 * @param objective : nouvelle objectif
	 * @param escadronID : ID de l'escadron à comparer
	 */
	public static void updateEscadronsObjective(Item objective, int escadronID) {
		for (SpaceShip s : shipsAllPlanets) {
			if (s.getEscadronID() == escadronID && escadronID != -1 )
				s.setObjective(objective);
		}
		
	}
		
}
