package airport.com;
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
	int nbAvion;
	int nbPisteArr;
	int nbPisteDep;
	int nbPlace;

	int position;
	
	boolean useBlockingQueue = true;
	
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
	}

	public void run() {
		try {
			
			if(useBlockingQueue){
				landing();
			}
			else{
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
		Thread.sleep(3000); //1s
	//Attend au terminal.
		terminal.put(this);
		tarmacLand.remove(this);
		airportFrame.avionOnTerm(this);
		System.out.println(this.getCode() + " at terminal.");
		Thread.sleep(10000); //3s
	//(Demande) Décollage.
		tarmacTakeOff.put(this);
		terminal.remove(this);
		airportFrame.avionTakeOff(this);
		System.out.println(this.getCode() + " is taking off");
		Thread.sleep(3000); //1s
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
