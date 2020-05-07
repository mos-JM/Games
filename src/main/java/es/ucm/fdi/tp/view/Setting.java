package es.ucm.fdi.tp.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import es.ucm.fdi.tp.base.Utils;

public class Setting extends JToolBar {

	private static final long serialVersionUID = 1L;
	private GameViewCtrl<?,?> gameCtrl;
	private JButton randomButton;
	private JButton smartButton;
	private JButton restartButton;
	private JButton quitButton;
	private JButton stop;
	private JSpinner threadSpinner;
	private JSpinner timeSpinner;
	private JLabel label;
	private JLabel brain;
	private JLabel clock;
	private JPanel smartPanel;
	private JComboBox<PlayerMode> combo;
	@SuppressWarnings("unused")
	private String [] valores = {"Manual", "Random", "Smart"};
	private SpinnerNumberModel model;
	private SpinnerNumberModel timerModel;
	boolean pulsado;
	private Color color;
	private Thread smartThread;
	
	public Setting(GameWindow<?,?> gameCtrl){
		this.gameCtrl = gameCtrl;
		this.pulsado = false;
		initGUI();
		
	}
	
	private void initGUI(){
		
		randomButton = new JButton();
		randomButton.setIcon(new ImageIcon(Utils.loadImage("dice.png")));
		randomButton.setToolTipText("Random Move");
		this.add(randomButton);
		randomButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("PULSADO RANDOM BUTTON");
				gameCtrl.randomActionButtonPressed();
			}
		});
		smartButton = new JButton();
		smartButton.setIcon(new ImageIcon(Utils.loadImage("nerd.png")));
		this.add(smartButton);
		smartButton.setToolTipText("Smart Move");
		smartButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("PULSADO SMART BUTTON");
				gameCtrl.smartActionButtonPressed();
			}
		});
		restartButton = new JButton();
		restartButton.setIcon(new ImageIcon(Utils.loadImage("restart.png")));
		restartButton.setToolTipText("Restart the game");
		this.add(restartButton);
		restartButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("PULSADO RESTART BUTTON");
				gameCtrl.resetActionButtonPressed();
			}
		});
		quitButton = new JButton();
		quitButton.setIcon(new ImageIcon(Utils.loadImage("exit.png")));
		quitButton.setToolTipText("Exit Game");
		this.add(quitButton);
		quitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("PULSADO QUIT BUTTON");
				gameCtrl.quitActionButtonPressed();
			}
		});
		
		label = new JLabel("  Player Mode:  ");
		this.add(label);
		combo = new JComboBox<PlayerMode>(PlayerMode.values());
		//combo.setPreferredSize(new Dimension(100, 50));
		combo.setMaximumSize(new Dimension(100, 30));
		this.add(combo);
		combo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gameCtrl.playerModeSelected((PlayerMode)combo.getSelectedItem());
			}
		});
		smartPanel = new JPanel();
		smartPanel.setLayout(new FlowLayout());
		smartPanel.setBorder(BorderFactory.createTitledBorder("Smart Moves: "));
		brain = new JLabel();
		model = new SpinnerNumberModel( 
				1, // Dato visualizado al inicio en el spinner 
				1, // Límite inferior 
				Runtime.getRuntime().availableProcessors(), // Límite superior 
				1 // incremento-decremento 
		); 
		threadSpinner = new JSpinner(model);
		threadSpinner.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e){
				gameCtrl.setMaxThreads((int)threadSpinner.getValue());
				System.out.println((int)threadSpinner.getValue() + "threads");
			}
		});
		
		JLabel threads = new JLabel("threads");
		brain.setIcon(new ImageIcon(Utils.loadImage("brain.png")));
		clock = new JLabel();
		clock.setIcon(new ImageIcon(Utils.loadImage("timer.png")));
		timerModel = new SpinnerNumberModel( 
				1000, // Dato visualizado al inicio en el spinner 
				500, // Límite inferior 
				5000, // Límite superior 
				500 // incremento-decremento 
		); 
		timeSpinner = new JSpinner(timerModel);
		timeSpinner.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e){
				gameCtrl.setTimeOut((int)timeSpinner.getValue());
				System.out.println((int)timeSpinner.getValue() + "ns, time.");
			}
		});
		
		stop = new JButton();
		stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("PULSADO QUIT STOP");
				gameCtrl.stopActionButtonPressed(smartThread);
				
			}
		});
		stop.setIcon(new ImageIcon(Utils.loadImage("stop.png")));
		stop.setPreferredSize(new Dimension(30,30));
		JLabel time = new JLabel("ms.");
		smartPanel.add(brain);
		smartPanel.add(threadSpinner);
		smartPanel.add(threads);
		smartPanel.add(clock);
		smartPanel.add(timeSpinner);
		smartPanel.add(time);
		smartPanel.add(stop);
		stop.setEnabled(false);
		this.add(smartPanel);
		this.setPreferredSize(new Dimension(680, 70));
		this.setOpaque(true);
	}
	
	public void setResetParameters(){
		combo.setSelectedItem(PlayerMode.Manual);
		threadSpinner.setValue(1);
		timeSpinner.setValue(1000);
		setEnableStop(false, null);
		setDefaultColor();

	}
	
	public void setYellow(){
		color= this.brain.getBackground();
		this.brain.setBackground(Color.YELLOW);
		this.brain.setOpaque(true);
		System.out.println("true");
	}
	
	public void setDefaultColor(){
		this.brain.setBackground(color);
		this.brain.setOpaque(true);
		System.out.println("False");
	}
	
	public void setEnableStop(boolean ok, Thread smartThread){
		if(ok){
			this.smartThread = smartThread;
			this.stop.setEnabled(true);
			System.out.println("TRUE");
		}
		else{
			this.stop.setEnabled(false);
		}
	}
}
