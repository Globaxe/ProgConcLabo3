package airport.com;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class AirportFrame extends JFrame {

	private static final long serialVersionUID = 1L;
    //liste d'avion � chaque endroits
	private List<Avion> avionOnAirArray;
	private List<Avion> avionLandingArray;
	private List<Avion> avionTermArray;
	private List<Avion> avionTakeOffArray;
	private List<Avion> avionOnAirLeaveArray;
	
	//images d'avion
	
	private ArrayList<JLabel> listTerm;
	private ArrayList<JLabel> listArr;
	private ArrayList<JLabel> listDep;
	
	private JLabel nbOnAirLabel;
	private JLabel nbLandingLabel;
	private JLabel nbTermLabel;
	private JLabel nbTakeOffLabel;
	private JLabel nbOnAirLeaveLabel;
	private JButton Start;
	private JButton Stop;

	private int nbPisteArr;
	private int nbPisteDep;
	private int nbPlace;
	

	public AirportFrame(int _nbPisteArr, int _nbPisteDep, int _nbPlace, int _nbAvion) {
	    
	    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	    
		nbPisteArr = _nbPisteArr;
		nbPisteDep = _nbPisteDep;
		nbPlace = _nbPlace;
				
		avionOnAirArray = new ArrayList<Avion>();
		avionLandingArray = new ArrayList<Avion>();
		avionTermArray = new ArrayList<Avion>();
		avionTakeOffArray = new ArrayList<Avion>();
		avionOnAirLeaveArray = new ArrayList<Avion>();
		
		listArr = new ArrayList<JLabel>();
		listTerm = new ArrayList<JLabel>();
		listDep = new ArrayList<JLabel>();

		JPanel panel = new JPanel(new BorderLayout());

		JPanel airportPanel = new JPanel();
		airportPanel.setLayout(new GridLayout(1, 3));

		ImageIcon imgRoad = new ImageIcon("img/piste.png");

		JPanel landPanel = new JPanel();
		landPanel.setLayout(new GridLayout(2 + (nbPisteArr - 1), 1));
		ImageIcon imgLand = new ImageIcon("img/landing.png");
		nbLandingLabel = new JLabel("nb avion en approche :", JLabel.CENTER);

		for (int i = 1; i <= _nbPisteArr; i++) {
			JLabel imgLandingLabel = new JLabel("", Tools.scaleImage(imgLand, 50, 50), JLabel.CENTER);
			imgLandingLabel.setVisible(false);
			listArr.add(imgLandingLabel);
			landPanel.add(imgLandingLabel);
			landPanel.add(new JLabel("", Tools.scaleImage(imgRoad, 50, 50), JLabel.CENTER));
		}
		landPanel.add(new JLabel());
		landPanel.add(nbLandingLabel);
		airportPanel.add(landPanel);

		JPanel airportImgPanel = new JPanel();
		airportImgPanel.setLayout(new GridLayout(3, 1));
		ImageIcon imgAir = new ImageIcon("img/airport.png");
		airportImgPanel.add(new JLabel("", Tools.scaleImage(imgAir, 150, 150), JLabel.CENTER));
		nbTermLabel = new JLabel("nb avion au terminal :", JLabel.CENTER);
		airportImgPanel.add(nbTermLabel);
		airportPanel.add(airportImgPanel);

		JPanel takeOffPanel = new JPanel();
		takeOffPanel.setLayout(new GridLayout(2 + (nbPisteDep - 1), 1));
		ImageIcon imgTakeOff = new ImageIcon("img/takeoff.png");
		nbTakeOffLabel = new JLabel("nb avion au d�part :", JLabel.CENTER);

		for (int i = 1; i <= _nbPisteDep; i++) {
			JLabel imgTakeOffLabel = new JLabel("", Tools.scaleImage(imgTakeOff, 50, 50), JLabel.CENTER);
			imgTakeOffLabel.setVisible(false);
			listDep.add(imgTakeOffLabel);
			takeOffPanel.add(new JLabel("", Tools.scaleImage(imgRoad, 50, 50), JLabel.CENTER));
			takeOffPanel.add(imgTakeOffLabel);
		}
		takeOffPanel.add(nbTakeOffLabel);
		airportPanel.add(takeOffPanel);

		panel.add(airportPanel, BorderLayout.CENTER);

		JPanel parkPanel = new JPanel();
		
		for (int i = 1; i <= _nbPlace; i++) {
			ImageIcon imgPark = new ImageIcon("img/waiting.png");
			JLabel imgParkLabel = new JLabel("", Tools.scaleImage(imgPark, 50, 50), JLabel.CENTER);
			imgParkLabel.setVisible(false);
			listTerm.add(imgParkLabel);
			imgParkLabel.setBorder(BorderFactory.createLineBorder(Color.black));
			parkPanel.add(imgParkLabel);
			
			
		}
		panel.add(parkPanel, BorderLayout.SOUTH);

		JPanel onAirPanel = new JPanel();
		onAirPanel.setLayout(new GridLayout(2, 2));
		ImageIcon imgOnAir = new ImageIcon("img/onair.png");
		nbOnAirLabel = new JLabel("nb avion en air (arrive) :", JLabel.CENTER);
		onAirPanel.add(new JLabel("", Tools.scaleImage(imgOnAir, 50, 50), JLabel.CENTER));
		onAirPanel.add(new JLabel("", Tools.scaleImage(imgOnAir, 50, 50), JLabel.CENTER));
		onAirPanel.add(nbOnAirLabel);
		nbOnAirLeaveLabel = new JLabel("nb avion en air (depart) :", JLabel.CENTER);
		onAirPanel.add(nbOnAirLeaveLabel);
		panel.add(onAirPanel, BorderLayout.NORTH);
		
		
		JPanel bouton = new JPanel();
		bouton.setLayout(new GridLayout(1, 2)); 
		JPanel start = new JPanel();
		JPanel stop = new JPanel();
		Start = new JButton("Start");
		start.add(Start);
		Stop = new JButton("Stop");
		stop.add(Stop);
		
		bouton.add(start);bouton.add(stop);
		panel.add(bouton,BorderLayout.EAST);

		this.getContentPane().add(panel);
	}
	
	//partie raffraichissemnt affichage
	public synchronized void avionInAir(Avion av)
	{
	    this.avionOnAirArray.add(av);
	    refresh();
	}

	public synchronized void avionLand(Avion av)
	{
	    this.avionOnAirArray.remove(av);
	    this.avionLandingArray.add(av);
	    refresh();
	}

	public synchronized void avionOnTerm(Avion av)
	{
	    this.avionLandingArray.remove(av);
	    this.avionTermArray.add(av);
	    refresh();
	}

	public synchronized void avionTakeOff(Avion av)
	{
	    this.avionTermArray.remove(av);
	    this.avionTakeOffArray.add(av);
	    refresh();
	}

	public synchronized void avionInAirLeave(Avion av)
	{
	    this.avionTakeOffArray.remove(av);
	    this.avionOnAirLeaveArray.add(av);
	    refresh();
	}

	//deux fonction pour pouvoir implémenter le code daes boutons dans le main
	public JButton getButtonStart()
	{
	   return Start; 
	}
	public JButton getButtonStop()
	{
	   return Stop; 
	}
	
	//fonction pour la gestion de l'affichage
	public void refresh()
	{
	    for (int i = 0; i < this.nbPisteArr; i++)
	    {
	        JLabel label = (JLabel)this.listArr.get(i);
	        if (i < this.avionLandingArray.size())
	        {
	            Avion av = (Avion)this.avionLandingArray.get(i);
	            label.setText(av.getCode());
	            label.setVisible(true);
	        }
	        else
	        {
	            label.setVisible(false);
	        }
	    }
	    for (int i = 0; i < this.nbPlace; i++)
	    {
	        JLabel label = (JLabel)this.listTerm.get(i);
	        if (i < this.avionTermArray.size())
	        {
	            Avion av = (Avion)this.avionTermArray.get(i);
	            label.setText(av.getCode());
	            label.setVisible(true);
	        }
	        else
	        {
	            label.setVisible(false);
	        }
	    }
	    for (int i = 0; i < this.nbPisteDep; i++)
	    {
	        JLabel label = (JLabel)this.listDep.get(i);
	        if (i < this.avionTakeOffArray.size())
	        {
	            Avion av = (Avion)this.avionTakeOffArray.get(i);
	            label.setText(av.getCode());
	            label.setVisible(true);
	        }
	        else
	        {
	            label.setVisible(false);
	        }
	    }
	    this.nbOnAirLabel.setText("nb avion en air (arrive) :" + this.avionOnAirArray.size());
	    this.nbLandingLabel.setText("nb avion en approche :" + this.avionLandingArray.size());
	    this.nbTermLabel.setText("nb avion au terminal :" + this.avionTermArray.size());
	    this.nbTakeOffLabel.setText("nb avion au d�part :" + this.avionTakeOffArray.size());
	    this.nbOnAirLeaveLabel.setText("nb avion en air (depart) :" + this.avionOnAirLeaveArray.size());
	}
}
