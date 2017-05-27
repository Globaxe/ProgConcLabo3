package airport.com;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
//represente l'avion

public class Avion implements Runnable {

	AirportFrame airportFrame;
	String codePlane;
	BlockingQueue<Avion> airArr;
	BlockingQueue<Avion> tarmacLand;
	BlockingQueue<Avion> tarmacTakeOff;
	BlockingQueue<Avion> terminal;
	BlockingQueue<Avion> airDep;
	
	List<Avion> listAirArr;
	List<Avion> listTarmacLand;
	List<Avion> listTarmacTakeOff;
	List<Avion> listTerminal;
	List<Avion> listAirDep;
	
	int nbAvion;
	int nbPisteArr;
	int nbPisteDep;
	int nbPlace;
	long time;

	int position;
	
	Semaphore semaphore = null;
	
	boolean useBlockingQueue;
	
	//constructeur avec blocking queu
	public Avion(AirportFrame _airportFrame, String _codePlane, BlockingQueue<Avion> _airArr, BlockingQueue<Avion> _tarmacLand,
			BlockingQueue<Avion> _tarmacTakeOff, BlockingQueue<Avion> _terminal, BlockingQueue<Avion> _airDep,
			int _nbAvion, int _nbPisteArr, int _nbPisteDep, int _nbPlace, Semaphore _semaphore) {
		airportFrame = _airportFrame;
		codePlane = _codePlane;

		airArr = _airArr;
		tarmacLand = _tarmacLand;
		tarmacTakeOff = _tarmacTakeOff;
		terminal = _terminal;
		airDep = _airDep;

		nbAvion = _nbAvion;
		nbPisteArr = _nbPisteArr;
		nbPisteDep = _nbPisteDep;
		nbPlace = _nbPlace;

		semaphore = _semaphore;
		
		useBlockingQueue=true;
	}
	
	//constructeur sans blocking queu
	public Avion(AirportFrame _airportFrame, String _codePlane, List<Avion> _airArr, List<Avion> _tarmacLand,
			List<Avion> _tarmacTakeOff, List<Avion> _terminal, List<Avion> _airDep,
			int _nbAvion, int _nbPisteArr, int _nbPisteDep, int _nbPlace, Semaphore _semaphore) {
		airportFrame = _airportFrame;
		codePlane = _codePlane;

		listAirArr = _airArr;
		listTarmacLand = _tarmacLand;
		listTarmacTakeOff = _tarmacTakeOff;
		listTerminal = _terminal;
		listAirDep = _airDep;

		nbAvion = _nbAvion;
		nbPisteArr = _nbPisteArr;
		nbPisteDep = _nbPisteDep;
		nbPlace = _nbPlace;
		
		semaphore = _semaphore;
		
		useBlockingQueue=false;
	}

	public void run() {
		try {
			// test pour savoir si on utilise les blocking queu ou non
			if(useBlockingQueue){
				landing();
			}
			else{
				landinguru();
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//version sans blocking queu
	public void landinguru() throws InterruptedException{

	    //les threads étant directement démarré on les met en pause instantanément
		isPaused();
		listAirArr.add(this);
		airportFrame.avionInAir(this);
		Thread.sleep(1000);
		isPaused();
		
		land();
		Thread.sleep(1000);
		isPaused();
		
		waitTarmak();
		Thread.sleep(3000);
		isPaused();
		
		takeOff();
		Thread.sleep(1000);
		isPaused();
		
		inAir();
		
		System.out.println("Process time: "+ (System.currentTimeMillis() - time));
	}
	
	//fonction pour l'atterissage sans blocking queu
	public void land(){
		
		try {
			synchronized (listTarmacLand)
            {
			    while(listTarmacLand.size()>= nbPisteArr)
			    {
			        listTarmacLand.wait();
			    }
			    
	            listTarmacLand.add(this);
            }
			airportFrame.avionLand(this);
			listAirArr.remove(this);
			System.out.println(this.getCode() + " is landing.");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//fonction d'attente au tarmac sans blocking queu
	public void waitTarmak(){
	    try {
	        synchronized (listTerminal)
            {
	            while(listTerminal.size()>= nbPlace){
	                listTerminal.wait();
	            }
	            listTerminal.add(this);
            }
	        synchronized (listTarmacLand)
            {
	            listTarmacLand.remove(this);
	            listTarmacLand.notify();  
            }
            airportFrame.avionOnTerm(this);
            System.out.println(this.getCode() + " at terminal.");
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
	
	//fonction pour le décollage sans blocking queu
	public void takeOff(){
	    try {
	        synchronized (listTarmacTakeOff)
            {
	            while(listTarmacTakeOff.size()>= nbPisteDep){
	                listTarmacTakeOff.wait();
	            }
	                listTarmacTakeOff.add(this);
            }
            synchronized (listTerminal)
            {
                listTerminal.remove(this);
                listTerminal.notify();
            }
            airportFrame.avionTakeOff(this);
            System.out.println(this.getCode() + " is taking off.");
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
	
	//fonction pour la gestion de la pause avec un sémaphore
	public void isPaused(){
		while(!semaphore.tryAcquire(1)) {
		    try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		semaphore.release();
	}
	
	//fonction de départ dans les airs sans blocking queu
	public void inAir(){
	    synchronized (listTarmacTakeOff)
        {
	        listTarmacTakeOff.remove(this);
	        listTarmacTakeOff.notify();  
        }
        listAirDep.add(this);
        airportFrame.avionInAirLeave(this);
        System.out.println(this.getCode() + " is in air.");
    }
	
	//fonction avec blocking queu
	public void landing() throws InterruptedException{
	//les threads étant directement démarré on les met en pause instantanément
	    isPaused();
	//Arrive dans l'espace a�rien de l'a�ro-porc.
		airArr.put(this);
		airportFrame.avionInAir(this);
		Thread.sleep(1000);
		isPaused();
	//(Demande) Atterissage.
		tarmacLand.put(this);
		airArr.remove(this);
		airportFrame.avionLand(this);
		System.out.println(this.getCode() + " is landing.");
		Thread.sleep(1000); //1s
		isPaused();
	//Attend au terminal.
		terminal.put(this);
		tarmacLand.remove(this);
		airportFrame.avionOnTerm(this);
		System.out.println(this.getCode() + " at terminal.");
		Thread.sleep(3000); //3s
		isPaused();
	//(Demande) D�collage.
		tarmacTakeOff.put(this);
		terminal.remove(this);
		airportFrame.avionTakeOff(this);
		System.out.println(this.getCode() + " is taking off");
		Thread.sleep(1000); //1s
		isPaused();
	//repart dans les airs
		airDep.put(this);
		tarmacTakeOff.remove(this);
		airportFrame.avionInAirLeave(this);
		System.out.println(this.getCode() + " is going away");
		
		System.out.println("Process time: "+ (System.currentTimeMillis() - time));
	}
	
	public String getCode() {
		return codePlane;
	}
	
	//code pour les test de temps
	public void setTime(long _time){
		time = _time;
	}

}
