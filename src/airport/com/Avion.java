package airport.com;
import java.util.List;
import java.util.concurrent.BlockingQueue;
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

	int position;
	
	boolean useBlockingQueue;
	
	public Avion(AirportFrame _airportFrame, String _codePlane, BlockingQueue<Avion> _airArr, BlockingQueue<Avion> _tarmacLand,
			BlockingQueue<Avion> _tarmacTakeOff, BlockingQueue<Avion> _terminal, BlockingQueue<Avion> _airDep,
			int _nbAvion, int _nbPisteArr, int _nbPisteDep, int _nbPlace) {
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
		
		useBlockingQueue=true;
	}
	public Avion(AirportFrame _airportFrame, String _codePlane, List<Avion> _airArr, List<Avion> _tarmacLand,
			List<Avion> _tarmacTakeOff, List<Avion> _terminal, List<Avion> _airDep,
			int _nbAvion, int _nbPisteArr, int _nbPisteDep, int _nbPlace) {
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
		
		useBlockingQueue=false;
	}

	public void run() {
		try {
			
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
	public void landinguru() throws InterruptedException{
		listAirArr.add(this);
		airportFrame.avionInAir(this);
		land();
	}
	public synchronized void land(){
		
		try {
			
			while(listTarmacLand.size()>= nbPisteArr){
				listTarmacLand.wait();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public synchronized void waitTarmak(){
			
		}
	public synchronized void takeOff(){
		
	}
	
	public void landing() throws InterruptedException{
		
	//Arrive dans l'espace aérien de l'aéro-porc.
		airArr.put(this);
		airportFrame.avionInAir(this);
	//(Demande) Atterissage.
		tarmacLand.put(this);
		airArr.remove(this);
		airportFrame.avionLand(this);
		System.out.println(this.getCode() + " is landing.");
		Thread.sleep(1000); //1s
	//Attend au terminal.
		terminal.put(this);
		tarmacLand.remove(this);
		airportFrame.avionOnTerm(this);
		System.out.println(this.getCode() + " at terminal.");
		Thread.sleep(3000); //3s
	//(Demande) Décollage.
		tarmacTakeOff.put(this);
		terminal.remove(this);
		airportFrame.avionTakeOff(this);
		System.out.println(this.getCode() + " is taking off");
		Thread.sleep(1000); //1s
	//Tsubasa o Kudasai
		airDep.put(this);
		tarmacTakeOff.remove(this);
		airportFrame.avionInAirLeave(this);
		System.out.println(this.getCode() + " is going away");
	}
	
	public String getCode() {
		return codePlane;
	}

}
