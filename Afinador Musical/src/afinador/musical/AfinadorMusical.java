package afinador.musical;

import java.awt.*;
import java.io.*;
import java.nio.file.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import static afinador.musical.constantes.NotasMusicaisConstantes.*;

/** 
 * @author Adriano Moda Feitosa 
 * @since 07/01/2016 
 **/
public class AfinadorMusical extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTextField campoNotaMusical;
	private Player afinar;

	private static final String IMAGE_ICON_NAME = "/imagens/play_black.png";
	private static final String IMAGE_STOP_NAME = "/imagens/stop.png";
	private static final String LEIAME = "src/arquivo/Leiame.txt";

	private JButton btnSair;
	private JMenuItem abrirVolume;
	private JMenuItem menuItemSair;

	// Enum para representar as notas
	private enum Nota {
		E6("Mi - 6ª Corda", "e2", MI_6),
		A5("La - 5ª Corda", "a2", LA_5),
		D4("Re - 4ª Corda", "d3", RE_4),
		G3("Sol - 3ª Corda", "g3", SOL_3),
		B2("Si - 2ª Corda", "b3", SI_2),
		E1("Mi - 1ª Corda", "e4", MI_1);

		private final String descricao; // para o texto do botão
		private final String arquivo;   // nome do arquivo MP3
		private final String label;     // texto completo da nota (freq + nome)

		Nota(String descricao, String arquivo, String label) {
			this.descricao = descricao;
			this.arquivo = arquivo;
			this.label = label;
		}

		public String getDescricao() {
			return descricao;
		}

		public String getArquivo() {
			return arquivo;
		}

		public String getLabel() {
			return label;
		}
	}


	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			AfinadorMusical frame = new AfinadorMusical();
			frame.setVisible(true);
		});
	}

	public AfinadorMusical() {
		setResizable(false);
		setTitle("Afinador Musical");

		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(150, 150, 228, 449);

		setJMenuBar(criarMenuBar());

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

		JLabel lblNotaMusical = new JLabel("Nota Musical");
		lblNotaMusical.setBounds(10, 18, 196, 14);
		contentPane.add(lblNotaMusical);

		// Criação dos botões dinamicamente
		int y = 112;
		for (Nota nota : Nota.values()) {
			criarBotaoNota(nota, y);
			y += 40;
		}

		btnSair = new JButton("Sair", new ImageIcon(getClass().getResource("/imagens/exit-to-app.png")));
		btnSair.setBounds(10, 356, 196, 33);
		btnSair.addActionListener(e -> sairAplicacao());
		contentPane.add(btnSair);
	}

	private JMenuBar criarMenuBar() {
		JMenuBar menuBar = new JMenuBar();

		JMenu menuArquivo = new JMenu("Arquivo");
		menuBar.add(menuArquivo);

		abrirVolume = new JMenuItem("Abrir Volume");
		abrirVolume.addActionListener(e -> abrirVolumeSom());
		menuArquivo.add(abrirVolume);

		menuItemSair = new JMenuItem("Sair");
		menuItemSair.addActionListener(e -> sairAplicacao());
		menuArquivo.add(menuItemSair);

		JMenu menuAjuda = new JMenu("Informações");
		menuBar.add(menuAjuda);

		JMenuItem mntmLeiame = new JMenuItem("Leiame");
		mntmLeiame.addActionListener(e -> abrirLeiame());
		menuAjuda.add(mntmLeiame);

		return menuBar;
	}

	private void criarBotaoNota(Nota nota, int y) {
		JButton btnPlay = new JButton(nota.getDescricao(), new ImageIcon(getClass().getResource(IMAGE_ICON_NAME)));
		JButton btnStop = new JButton("Parar", new ImageIcon(getClass().getResource(IMAGE_STOP_NAME)));

		btnPlay.setBounds(10, y, 196, 33);
		btnStop.setBounds(10, y, 196, 33);
		btnStop.setVisible(false);

		btnPlay.addActionListener(e -> tocarNota(nota, btnPlay, btnStop));
		btnStop.addActionListener(e -> pararNota(btnPlay, btnStop));

		contentPane.add(btnPlay);
		contentPane.add(btnStop);
	}

	private void tocarNota(Nota nota, JButton btnPlay, JButton btnStop) {
		pararTodas();
		campoNotaMusical.setText(nota.getLabel());

		if (afinar != null) {
			afinar.close();
		}

		new Musica(nota).start();

		btnPlay.setVisible(false);
		btnStop.setVisible(true);
	}

	private void pararNota(JButton btnPlay, JButton btnStop) {
		if (afinar != null) {
			afinar.close();
		}
		campoNotaMusical.setText("");
		btnPlay.setVisible(true);
		btnStop.setVisible(false);
	}

	private void pararTodas() {
		if (afinar != null) {
			afinar.close();
		}
		campoNotaMusical.setText("");
		for (Component comp : contentPane.getComponents()) {
			if (comp instanceof JButton) {
				JButton btn = (JButton) comp;
				if ("Parar".equals(btn.getText())) {
					btn.setVisible(false);
				} else {
					btn.setVisible(true);
				}
			}
		}
	}

	private void abrirVolumeSom() {
		try {
			Runtime.getRuntime().exec("sndvol");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Arquivo inexistente ou corrompido!", "Erro de Arquivo", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void abrirLeiame() {
		try {
			Path caminho = Paths.get(LEIAME);
			Desktop.getDesktop().open(caminho.toFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sairAplicacao() {
		int confirm = JOptionPane.showConfirmDialog(null, "Deseja sair do programa?", "Afinador Musical - Sair",
				JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

	// Classe interna para Thread de áudio
	class Musica extends Thread {
		private final Nota nota;

		Musica(Nota nota) {
			this.nota = nota;
		}

		public void run() {
			try (InputStream input = getClass().getResourceAsStream("/notas/musicais/" + nota.getArquivo() + ".mp3")) {
				afinar = new Player(input);
				afinar.play();
			} catch (JavaLayerException | IOException e) {
				e.printStackTrace();
			}
		}
	}
}
