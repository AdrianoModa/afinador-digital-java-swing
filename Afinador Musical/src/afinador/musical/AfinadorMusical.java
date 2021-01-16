package afinador.musical;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import static afinador.musical.constantes.NotasMusicaisConstantes.*;

public class AfinadorMusical extends JFrame {

	/**
	 * @author Adriano Moda Feitosa
	 * @since 07/01/2016
	 */

	private static final long serialVersionUID = 1L;
	private JButton btnE6;
	private JButton btnA5;
	private JButton btnD4;
	private JButton btnG3;
	private JButton btnB2;
	private JButton btnE1;
	private JButton btnPararSomE6;
	private JButton btnPararSomA5;
	private JButton btnPararSomD4;
	private JButton btnPararSomG3;
	private JButton btnPararSomB2;
	private JButton btnPararSomE1;
	private JPanel contentPane;
	private JTextField campoNotaMusical;
	private Player afinar;
	private int seletor = 0;
	private String notaMusical[] = {"e4", "b3", "g3", "d3", "a2", "e2"};
	private JMenuItem abrirVolume;
	private JMenuItem menuItemSair;
	private static final String IMAGE_ICON_NAME = "/imagens/play_black.png";
	private static final String IMAGE_STOP_NAME = "/imagens/stop.png";

	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable(){
			public void run(){
				AfinadorMusical frame = new AfinadorMusical();
				frame.setVisible(true);
			}
		});
	}

	public void botaoParar(){
		btnPararSomE6.setVisible(false);
		btnE6.setVisible(true);
		btnPararSomA5.setVisible(false);
		btnA5.setVisible(true);
		btnPararSomD4.setVisible(false);
		btnD4.setVisible(true);
		btnPararSomG3.setVisible(false);
		btnG3.setVisible(true);
		btnPararSomB2.setVisible(false);
		btnB2.setVisible(true);
		btnPararSomE1.setVisible(false);
		btnE1.setVisible(true);
	}

	public AfinadorMusical(){
		setResizable(false);
		setTitle("Afinador Musical");
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}			

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(150, 150, 228, 449);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu menuArquivo = new JMenu("Arquivo");
		menuBar.add(menuArquivo);

		abrirVolume = new JMenuItem("Abrir Volume");
		abrirVolume.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Runtime.getRuntime().exec("sndvol");
				} catch (RuntimeException erroRuntime) {
					erroRuntime.printStackTrace();
				} catch (IOException erroDeArquivo) {					
					erroDeArquivo.printStackTrace();
					JOptionPane.showMessageDialog(null,"Arquivo inexistente ou corrompido!", "Erro de Arquivo",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		menuArquivo.add(abrirVolume);

		menuItemSair = new JMenuItem("Sair");
		menuItemSair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int confirm = JOptionPane.showConfirmDialog(null, "Deseja sair do programa", "Afinador Musical - Sair", 
						JOptionPane.INFORMATION_MESSAGE);
				if (confirm == 0) {
					System.exit(0);
				}
			}
		});
		menuArquivo.add(menuItemSair);

		JMenu menuAjuda = new JMenu("Informações");
		menuBar.add(menuAjuda);

		JMenuItem mntmLeiame = new JMenuItem("Leiame");
		mntmLeiame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Path caminho = Paths.get("C://Program Files/Afinador Digital/Leiame.txt");
				try {
					java.awt.Desktop.getDesktop().open(new File(caminho.toString()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		menuAjuda.add(mntmLeiame);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		campoNotaMusical = new JTextField("Afinador Musical Violão");
		campoNotaMusical.setHorizontalAlignment(SwingConstants.CENTER);
		campoNotaMusical.setBackground(Color.WHITE);
		campoNotaMusical.setBounds(10, 43, 196, 58);
		campoNotaMusical.setFont(new Font("Arial", Font.BOLD, 12));
		campoNotaMusical.setForeground(Color.BLUE);
		campoNotaMusical.setEditable(false);
		contentPane.add(campoNotaMusical);
		campoNotaMusical.setColumns(30);

		btnE6 = new JButton("Mi - 6ª Corda");
		btnE6.setIcon(new ImageIcon(AfinadorMusical.class.getResource(IMAGE_ICON_NAME)));
		btnE6.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				seletor = 5;
				Musica mi_6 = new Musica();	
				if(afinar == null){
					botaoParar();					
					campoNotaMusical.setText(MI_6);
					mi_6.start();
					btnE6.setVisible(false);
					btnPararSomE6.setVisible(true);					
				}else{
					botaoParar();
					afinar.close();
					campoNotaMusical.setText(MI_6);
					mi_6.start();
					btnE6.setVisible(false);
					btnPararSomE6.setVisible(true);					
				}
			}	
		});
		btnE6.setBounds(10,112,196,33);
		contentPane.add(btnE6);

		btnA5 = new JButton("La - 5ª Corda");
		btnA5.setIcon(new ImageIcon(AfinadorMusical.class.getResource(IMAGE_ICON_NAME)));
		btnA5.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				seletor = 4;
				if(afinar == null){
					botaoParar();
					campoNotaMusical.setText(LA_5);
					Musica la_5 = new Musica();
					la_5.start();
					btnA5.setVisible(false);
					btnPararSomA5.setVisible(true);					
				}else{
					botaoParar();
					afinar.close();
					campoNotaMusical.setText(LA_5);
					Musica la_5 = new Musica();
					la_5.start();
					btnA5.setVisible(false);
					btnPararSomA5.setVisible(true);
				}
			}
		});
		btnA5.setBounds(10,152,196,33);
		contentPane.add(btnA5);

		btnD4 = new JButton("Re - 4ª Corda");
		btnD4.setIcon(new ImageIcon(AfinadorMusical.class.getResource(IMAGE_ICON_NAME)));
		btnD4.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				seletor = 3;
				if(afinar == null){
					botaoParar();
					campoNotaMusical.setText(RE_4);
					Musica re_4 = new Musica();
					re_4.start();
					btnD4.setVisible(false);
					btnPararSomD4.setVisible(true);
				}else{
					botaoParar();
					afinar.close();
					campoNotaMusical.setText(RE_4);
					Musica re_4 = new Musica();
					re_4.start();
					btnD4.setVisible(false);
					btnPararSomD4.setVisible(true);					
				}
			}
		});
		btnD4.setBounds(10,192,196,33);
		contentPane.add(btnD4);

		btnG3 = new JButton("Sol - 3ª Corda");
		btnG3.setIcon(new ImageIcon(AfinadorMusical.class.getResource(IMAGE_ICON_NAME)));
		btnG3.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				seletor = 2;
				if(afinar == null){
					botaoParar();
					campoNotaMusical.setText(SOL_3);
					Musica sol_3 = new Musica();
					sol_3.start();
					btnG3.setVisible(false);
					btnPararSomG3.setVisible(true);
				}else{
					botaoParar();
					afinar.close();
					campoNotaMusical.setText(SOL_3);					
					Musica sol_3 = new Musica();
					sol_3.start();
					btnG3.setVisible(false);
					btnPararSomG3.setVisible(true);
				}
			}
		});
		btnG3.setBounds(10,232,196,33);
		contentPane.add(btnG3);

		btnB2 = new JButton("Si - 2ª Corda");
		btnB2.setIcon(new ImageIcon(AfinadorMusical.class.getResource(IMAGE_ICON_NAME)));
		btnB2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				seletor = 1;
				if(afinar == null){
					botaoParar();
					campoNotaMusical.setText(SI_2);
					Musica si_2 = new Musica();
					si_2.start();
					btnB2.setVisible(false);
					btnPararSomB2.setVisible(true);
				}else{
					botaoParar();
					afinar.close();
					campoNotaMusical.setText(SI_2);					
					Musica si_2 = new Musica();
					si_2.start();
					btnB2.setVisible(false);
					btnPararSomB2.setVisible(true);					
				}
			}
		});
		btnB2.setBounds(10,272,196,33);
		contentPane.add(btnB2);

		btnE1 = new JButton("mi - 1ª Corda");
		btnE1.setIcon(new ImageIcon(AfinadorMusical.class.getResource(IMAGE_ICON_NAME)));
		btnE1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				seletor = 0;
				botaoParar();
				if(afinar == null){
					campoNotaMusical.setText(MI_1);
					Musica mi_1 = new Musica();
					mi_1.start();
					btnE1.setVisible(false);
					btnPararSomE1.setVisible(true);
				}else{
					afinar.close();
					campoNotaMusical.setText(MI_1);
					Musica mi_1 = new Musica();
					mi_1.start();
					btnE1.setVisible(false);
					btnPararSomE1.setVisible(true);					
				}
			}
		});
		btnE1.setBounds(10,312,196,33);
		contentPane.add(btnE1);		

		JLabel lblNotaMusical = new JLabel("Nota Musical");
		lblNotaMusical.setBounds(10, 18, 196, 14);
		contentPane.add(lblNotaMusical);
		// corda 6
		btnPararSomE6 = new JButton("Parar");
		btnPararSomE6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				afinar.close();
				campoNotaMusical.setText("");
				btnE6.setVisible(true);
				btnPararSomE6.setVisible(false);
			}
		});
		btnPararSomE6.setIcon(new ImageIcon(AfinadorMusical.class.getResource(IMAGE_STOP_NAME)));
		btnPararSomE6.setBounds(10,112,196,33);
		contentPane.add(btnPararSomE6);
		// corda 5
		btnPararSomA5 = new JButton("Parar");
		btnPararSomA5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				afinar.close();
				campoNotaMusical.setText("");
				btnA5.setVisible(true);
				btnPararSomA5.setVisible(false);
			}
		});
		btnPararSomA5.setIcon(new ImageIcon(AfinadorMusical.class.getResource(IMAGE_STOP_NAME)));
		btnPararSomA5.setBounds(10,152,196,33);
		contentPane.add(btnPararSomA5);
		// corda 4
		btnPararSomD4 = new JButton("Parar");
		btnPararSomD4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				afinar.close();
				campoNotaMusical.setText("");
				btnD4.setVisible(true);
				btnPararSomD4.setVisible(false);
			}
		});
		btnPararSomD4.setIcon(new ImageIcon(AfinadorMusical.class.getResource(IMAGE_STOP_NAME)));
		btnPararSomD4.setBounds(10,192,196,33);
		contentPane.add(btnPararSomD4);
		// corda 3
		btnPararSomG3 = new JButton("Parar");
		btnPararSomG3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				afinar.close();
				campoNotaMusical.setText("");
				btnG3.setVisible(true);
				btnPararSomG3.setVisible(false);
			}
		});
		btnPararSomG3.setIcon(new ImageIcon(AfinadorMusical.class.getResource(IMAGE_STOP_NAME)));
		btnPararSomG3.setBounds(10,232,196,33);
		contentPane.add(btnPararSomG3);
		// corda 2
		btnPararSomB2 = new JButton("Parar");
		btnPararSomB2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				afinar.close();
				campoNotaMusical.setText("");
				btnB2.setVisible(true);
				btnPararSomB2.setVisible(false);
			}
		});
		btnPararSomB2.setIcon(new ImageIcon(AfinadorMusical.class.getResource(IMAGE_STOP_NAME)));
		btnPararSomB2.setBounds(10,272,196,33);
		contentPane.add(btnPararSomB2);
		// corda 1
		btnPararSomE1 = new JButton("Parar");
		btnPararSomE1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				afinar.close();
				campoNotaMusical.setText("");
				btnE1.setVisible(true);
				btnPararSomE1.setVisible(false);
			}
		});
		btnPararSomE1.setIcon(new ImageIcon(AfinadorMusical.class.getResource(IMAGE_STOP_NAME)));
		btnPararSomE1.setBounds(10,312,196,33);
		contentPane.add(btnPararSomE1);

		JButton btnSair = new JButton("Sair");
		btnSair.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent arg0) {
				int confirm = JOptionPane.showConfirmDialog(null, "Deseja sair do programa", "Afinador Musical - Sair", 
						JOptionPane.INFORMATION_MESSAGE);
				if (confirm == 0) {
					System.exit(0);
				}
			}
		});
		btnSair.setIcon(new ImageIcon(AfinadorMusical.class.getResource("/imagens/exit-to-app.png")));
		btnSair.setBounds(10, 356, 196, 33);
		contentPane.add(btnSair);

	}

	// classe interna para Thread

	class Musica extends Thread {
		public void run() {
			try {
				InputStream input = this.getClass().getResourceAsStream("/notas/musicais/" + notaMusical[seletor] + ".mp3");				
				afinar = new Player(input);
				afinar.play();
			} catch (JavaLayerException e) {
				e.printStackTrace();
			}
		}
	}
}
