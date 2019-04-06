package interfaz;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.math.plot.Plot2DPanel;

import base.AlgoritmoGenetico;
import interfaz.ConfigPanel.ChoiceOption;
import interfaz.ConfigPanel.ConfigListener;
import interfaz.ConfigPanel.DoubleOption;
import interfaz.ConfigPanel.IntegerOption;


public class Main extends JFrame {

	private static final long serialVersionUID = 5393378737313833016L;
	
	public Main() {
		super("Algoritmo genetico");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		JPanel panelCentral = new JPanel(new BorderLayout());
		JPanel panelConfig = new JPanel(new BorderLayout());
		JPanel panelResultados = new JPanel(new BorderLayout());
		add(panelCentral, BorderLayout.CENTER);
		add(panelConfig, BorderLayout.WEST);
		add(panelResultados, BorderLayout.PAGE_END);
//		nononononono
		Plot2DPanel plot = new Plot2DPanel();
		plot.setAxisLabels("Generacion", "Fitness");
		panelCentral.add(plot, BorderLayout.CENTER);
		

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

		final JLabel resultado = new JLabel(" Mejor resultado: ");
		final JLabel ciudades1 = new JLabel(" Recorrido:");
		final JLabel ciudades2 = new JLabel("");
		panelResultados.add(resultado, BorderLayout.SOUTH);
		panelResultados.add(ciudades1, BorderLayout.NORTH);
		panelResultados.add(ciudades2, BorderLayout.CENTER);
		
		// crea botones para calcular y mostrar el resultado del algoritmo
		JButton boton = new JButton("Calcula grafica");
		boton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String listaCiudades = " ";
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
				
				resultado.setText(" Mejor resultado: " + (int) AG.getElMejor().getFitness());
				for (int i = 0; i < AG.getnGenes()/2; i++){
					listaCiudades += AG.getElMejor().genes[i].fenotipo();
					listaCiudades += ", ";
				}
				ciudades1.setText(" Recorrido: Madrid," + listaCiudades);
				
				listaCiudades = "                      ";
				for (int i = AG.getnGenes()/2; i < AG.getnGenes(); i++){
					listaCiudades += AG.getElMejor().genes[i].fenotipo();
					if (i != AG.getnGenes() - 1) listaCiudades += ", ";
				}
				ciudades2.setText(listaCiudades);
			}
		});

		panelConfig.add(boton, BorderLayout.PAGE_END);

		// panelCentral.add(plot);
	}
	
	public ConfigPanel<AlgoritmoGenetico> creaPanelConfiguracion() {
		String[] seleccion = new String[] { "Ruleta", "Torneo", "Restos", "Ranking", "Truncamiento", "Propio" };
		String[] cruce = new String[] { "PMX", "OX", "Variante de OX", "CX", "ERX", "Ordinal", "Propio" };
		String[] mutacion = new String[] { "Heuristica", "Insercion", "Intercambio", "Inversion", "Propio" };
		String[] contractividad = new String[] { "False", "True" };
		Double[] elitismo = new Double[] { 0.0, 0.01, 0.02, 0.03 };
		
		ConfigPanel<AlgoritmoGenetico> config = new ConfigPanel<AlgoritmoGenetico>();
		
		// se pueden añadir las opciones de forma independiente, o "de seguido"; el resultado es el mismo.
		config.addOption(new IntegerOption<AlgoritmoGenetico>(  // -- entero
				"Poblacion", // etiqueta del campo
				"tamaño de la poblacion",// 'tooltip' cuando pasas el puntero
				"tamPob", // campo (espera que haya un getGrosor y un setGrosor)
				1, 1000)) // min y max
			  .addOption(new IntegerOption<AlgoritmoGenetico>("Generaciones", "numero de generaciones", "numMaxGen", 1, 1000))
			  .addOption(new IntegerOption<AlgoritmoGenetico>("Participantes", "numero de participantes para el metodo de seleccion", "participantes", 0, 100))
			  .addOption(new DoubleOption<AlgoritmoGenetico>("% Cruce", "porcentaje de cruce", "probCruce", 0, 1))
			  .addOption(new DoubleOption<AlgoritmoGenetico>("% Mutacion", "porcentaje de mutacion", "probMutacion", 0, 1))
			  .addOption(new ChoiceOption<AlgoritmoGenetico>("Seleccion", "metodo de seleccion", "seleccion", seleccion))
			  .addOption(new ChoiceOption<AlgoritmoGenetico>("Cruce", "metodo de cruce para las funciones de 1 a 4", "cruce", cruce))
			  .addOption(new ChoiceOption<AlgoritmoGenetico>("Mutacion", "metodo de mutacion", "mutacion", mutacion))
			  .addOption(new ChoiceOption<AlgoritmoGenetico>("% Elitismo", "porcentaje de la poblacion que forma la elite", "elitismo", elitismo))
			  .addOption(new ChoiceOption<AlgoritmoGenetico>("Contractividad", "activa/desactiva la contractividad", "contractividad", contractividad))

			  // y ahora ya cerramos el formulario
		  	  .endOptions();
		
		return config;
	}
	
	
	// construye y muestra la interfaz
	public static void main(String[] args) {
		Main p = new Main();
		p.setSize(1000, 600);
		p.setVisible(true);	
	}
}
