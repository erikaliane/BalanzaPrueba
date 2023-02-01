package vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import Operaciones.puertoSerial;
public class FramePesoBalanza extends JFrame {
	private JPanel contentPane;
	public static  JTextField textPeso;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FramePesoBalanza frame = new FramePesoBalanza();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	
	
	
	
	public FramePesoBalanza() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 393, 242);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textPeso = new JTextField();
		textPeso.setHorizontalAlignment(SwingConstants.CENTER);
		textPeso.setFont(new Font("Tahoma", Font.BOLD, 30));
		textPeso.setBounds(160, 37, 163, 61);
		textPeso.setText("00.00"); //asignamos la variable del valor del peso
		contentPane.add(textPeso);
		textPeso.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Peso");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 19));
		lblNewLabel.setBounds(61, 63, 89, 16);
		contentPane.add(lblNewLabel);
		
		JButton btnConectar = new JButton("Conectar");
		btnConectar.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				puertoSerial.InitPort();;
				 System.out.println( "Se ha Conectado Correctamente" );
				
			}
			
		});
		btnConectar.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnConectar.setBounds(61, 149, 114, 23);
		
		contentPane.add(btnConectar);
		
		
		JButton btnDesconectar = new JButton("Desconectar");
		btnDesconectar.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnDesconectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				puertoSerial.ClosePort();
				 System.out.println( "Se ha Desconectado Correctamente" );
			}
		});
		btnDesconectar.setBounds(199, 149, 128, 23);
		contentPane.add(btnDesconectar);
	}

	public static void pesando(String quitarDatos) {
		
		
	}

}
