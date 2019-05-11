package interfaz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;

import org.math.plot.Plot2DPanel;

import base.AlgoritmoGenetico;
import interfaz.ConfigPanel.ChoiceOption;
import interfaz.ConfigPanel.ConfigListener;
import interfaz.ConfigPanel.DoubleOption;
import interfaz.ConfigPanel.IntegerOption;
import tablero.Tablero;


public class Main extends JFrame {

	private static final long serialVersionUID = 5393378737313833016L;

	public Main() {
		super("Algoritmo genetico");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		JPanel panelCentral = new JPanel(new BorderLayout());
		JPanel panelConfig = new JPanel(new BorderLayout());
		JPanel panelResultados = new JPanel(new BorderLayout());
		JPanel graficas = new JPanel(new BorderLayout());
		JPanel tablero = new JPanel();
		add(panelCentral, BorderLayout.CENTER);
		add(panelConfig, BorderLayout.WEST);

		// Añadimos las graficas al panel graficas
		Plot2DPanel plot = new Plot2DPanel();
		plot.setAxisLabels("Generacion", "Fitness");
		graficas.add(plot, BorderLayout.CENTER);

		dibujaTablero(new Tablero(), tablero);

		// Creamos el conjunto de pestañas
		JTabbedPane pestañas = new JTabbedPane();
		panelCentral.add(pestañas, BorderLayout.CENTER);
		panelCentral.add(panelResultados, BorderLayout.SOUTH);

		// Añadimos un nombre de la pestaña y el panel
		pestañas.addTab("Gráficas", graficas);
		pestañas.addTab("Tablero", tablero);


		// crea Algoritmo Genetico
		AlgoritmoGenetico AG = new AlgoritmoGenetico();
		// crea un panel central y lo asocia con el AG
		final ConfigPanel<AlgoritmoGenetico> cp = creaPanelConfiguracion();
		cp.setTarget(AG); // asocia el panel con el AG
		cp.initialize(); // carga los valores del AG en el panel
		panelConfig.add(cp, BorderLayout.NORTH);

		// crea una etiqueta que dice si todo es valido
		final String textoTodoValido = "Todos los campos OK";
		final String textoHayErrores = "Hay errores en algunos campos";
		final JLabel valido = new JLabel(textoTodoValido);
		// este evento se lanza cada vez que la validez cambia
		cp.addConfigListener(new ConfigListener() {
			@Override
			public void configChanged(boolean isConfigValid) {
				valido.setText(isConfigValid ? textoTodoValido: textoHayErrores);				
			}
		});
		add(valido, BorderLayout.SOUTH);

		final JTextArea resultado = new JTextArea(" Arbol mejor resultado(): ");
		resultado.setMargin(new Insets(10, 10, 10, 10));
		resultado.setLineWrap(true);
		
		panelResultados.add(resultado, BorderLayout.NORTH);

		// crea botones para calcular y mostrar el resultado del algoritmo
		JButton boton = new JButton("Calcula grafica");
		boton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Tablero t = new Tablero();
				plot.removeAllPlots();
				AG.AlgoritmoGeneticoFuncion();
				// define the legend position
				double[] x = new double[AG.getGeneracionActual()];
				for (int i = 0; i < x.length; i++){
					x[i] = i;
				}
				plot.addLegend("SOUTH");
				// add a line plot to the PlotPanel
				plot.addLinePlot("mejor de todas las generaciones", x, AG.getMejorAbsoluto());
				plot.addLinePlot("mejor de la generacion", x, AG.getGenMejor());
				plot.addLinePlot("media de la generacion", x, AG.getGenMedia());
				
				t.recorreTablero(AG.getElMejor().getArbol());
				dibujaTablero(t, tablero);

				resultado.setText(" Arbol mejor resultado("+ AG.getElMejor().getNumBocados() +"): " + AG.getElMejor().toString());
			}
		});

		panelConfig.add(boton, BorderLayout.PAGE_END);

		// panelCentral.add(plot);
	}

	public ConfigPanel<AlgoritmoGenetico> creaPanelConfiguracion() {
		String[] seleccion = new String[] { "Ruleta", "Torneo", "Restos", "Ranking", "Truncamiento", "Propio" };
		String[] mutacion = new String[] { "Terminal", "Funcion", "Inicializacion" };
		String[] contractividad = new String[] { "False", "True" };
		Double[] elitismo = new Double[] { 0.0, 0.01, 0.02, 0.03 };

		ConfigPanel<AlgoritmoGenetico> config = new ConfigPanel<AlgoritmoGenetico>();

		// se pueden añadir las opciones de forma independiente, o "de seguido"; el resultado es el mismo.
		config.addOption(new IntegerOption<AlgoritmoGenetico>("Poblacion", "tamaño de la poblacion", "tamPob", 1, 1000))
		.addOption(new IntegerOption<AlgoritmoGenetico>("Generaciones", "numero de generaciones", "numMaxGen", 1, 1000))
		.addOption(new IntegerOption<AlgoritmoGenetico>("Altura maxima", "Altura maxima del arbol", "hMax", 1, 10))
		.addOption(new IntegerOption<AlgoritmoGenetico>("Participantes", "numero de participantes para el metodo de seleccion", "participantes", 0, 100))
		.addOption(new DoubleOption<AlgoritmoGenetico>("% Cruce", "porcentaje de cruce", "probCruce", 0, 1))
		.addOption(new DoubleOption<AlgoritmoGenetico>("% Mutacion", "porcentaje de mutacion", "probMutacion", 0, 1))
		.addOption(new ChoiceOption<AlgoritmoGenetico>("Seleccion", "metodo de seleccion", "seleccion", seleccion))
		.addOption(new ChoiceOption<AlgoritmoGenetico>("Mutacion", "metodo de mutacion", "mutacion", mutacion))
		.addOption(new ChoiceOption<AlgoritmoGenetico>("% Elitismo", "porcentaje de la poblacion que forma la elite", "elitismo", elitismo))
		.addOption(new ChoiceOption<AlgoritmoGenetico>("Contractividad", "activa/desactiva la contractividad", "contractividad", contractividad))

		// y ahora ya cerramos el formulario
		.endOptions();

		return config;
	}
	
	private void dibujaTablero(Tablero t, JPanel tablero) {
		tablero.removeAll();
		tablero.setLayout(new GridLayout(32,32));
		
		for (int i = 0; i < 32*32; i++){
			JPanel panel=new JPanel();
			// tablero.setLayout(new FlowLayout());
			switch(t.getTablero()[(int) i/32][i%32]) {
			case CAMINADA:
				panel.setBackground(Color.orange);
				break;
			case HABIACOMIDA:
				panel.setBackground(Color.yellow);
				break;
			case HAYCOMIDA:
				panel.setBackground(Color.black);
				break;
			case NADA:
				panel.setBackground(Color.white);
				break;
			default:
				panel.setBackground(Color.red);
				break;
			}
			Border borde;
			borde = BorderFactory.createLineBorder(Color.black);  ///se le pone un borde.
			panel.setBorder(borde);
			tablero.add(panel);
		}
	}


	// construye y muestra la interfaz
	public static void main(String[] args) {
		Main p = new Main();
		p.setSize(1000, 600);
		p.setVisible(true);	
	}
}
