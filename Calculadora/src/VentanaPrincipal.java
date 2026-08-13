import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.*;

/*
 * CLASE VENTANAPRINCIPAL
 * Esta clase se encarga de construir absolutamente todo lo que ves en pantalla:
 * la ventana, las pestañas, los botones, las cajas de texto y de acomodar los gráficos.
 */
public class VentanaPrincipal extends JFrame {
    
    // Definimos los colores oficiales para cada vector (A, B y el Resultado)
    private static final Color C_A = new Color(0, 102, 204);
    private static final Color C_B = new Color(204, 51, 0);
    private static final Color C_R = new Color(0, 153, 76);
    
    /*
     * CONSTRUCTOR
     * Configura el tamaño de la ventana principal, hace que aparezca en el centro
     * de la pantalla y le agrega las dos pestañas principales (Vectores 2D y 3D).
     */
    public VentanaPrincipal() {
        super("Calculadora de Vectores");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Vectores 2D", crearPanelUI(false));
        tabs.addTab("Vectores 3D", crearPanelUI(true));
        
        add(tabs);
    }

    /*
     * CONSTRUCTOR DE INTERFAZ (EL "TODOTERRENO")
     * Este método es la joya de la corona. Construye la interfaz completa de una pestaña.
     * Recibe un 'is3D' (true o false) para saber si debe poner 2 o 3 cajas de texto (x, y, z)
     * y para decidir si carga el lienzo de dibujo 2D o el 3D.
     */
    private JPanel crearPanelUI(boolean is3D) {
        // Panel principal dividido en Izquierda (controles) y Derecha (gráfico)
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Columna de controles izquierdos
        JPanel controles = new JPanel();
        controles.setLayout(new BoxLayout(controles, BoxLayout.Y_AXIS));
        controles.setPreferredSize(new Dimension(320, 0));

        // Prepara las cajas de texto dependiendo si es 3D o 2D
        JTextField[] tfA = is3D ? new JTextField[]{new JTextField("3"), new JTextField("2"), new JTextField("4")} 
                                : new JTextField[]{new JTextField("3"), new JTextField("2")};
        JTextField[] tfB = is3D ? new JTextField[]{new JTextField("-2"), new JTextField("-1"), new JTextField("4")} 
                                : new JTextField[]{new JTextField("-2"), new JTextField("4")};

        // Agrega los paneles de texto a la columna
        controles.add(crearInputPanel("Vector A", tfA, C_A));
        controles.add(Box.createVerticalStrut(8)); // Espacio en blanco
        controles.add(crearInputPanel("Vector B", tfB, C_B));
        controles.add(Box.createVerticalStrut(8));

        // Caja de texto donde se imprimen los resultados
        JTextArea resultado = new JTextArea(6, 20);
        resultado.setEditable(false);
        
        // Decide qué lienzo cargar
        GraficoPanel grafico = is3D ? new Grafico3D() : new Grafico2D();

        // Panel de botones
        JPanel botones = new JPanel(new GridLayout(0, 1, 5, 5));
        botones.setBorder(BorderFactory.createTitledBorder("Operaciones"));
        
        // Función rápida para solo graficar sin hacer matemáticas
        Runnable graficar = () -> {
            try {
                VectorMath a = leerV(tfA);
                VectorMath b = leerV(tfB);
                grafico.limpiar(); 
                grafico.addV(a, C_A, "A"); 
                grafico.addV(b, C_B, "B");
                resultado.setText("A = " + a.toString() + "\nB = " + b.toString());
            } catch (Exception ex) { 
                error(); 
            }
        };

        // Conecta cada botón con su acción correspondiente
        agregarBtn(botones, "Graficar A y B", e -> graficar.run());
        agregarBtn(botones, "A + B (suma)", e -> procesarOp(tfA, tfB, resultado, grafico, "suma"));
        agregarBtn(botones, "A - B (resta)", e -> procesarOp(tfA, tfB, resultado, grafico, "resta"));
        agregarBtn(botones, "|A| y |B| (magnitud)", e -> procesarOp(tfA, tfB, resultado, grafico, "mag"));
        agregarBtn(botones, "Limpiar gráfico", e -> { grafico.limpiar(); resultado.setText(""); });

        controles.add(botones);

        // Arma el lado izquierdo con su barra de desplazamiento por si la pantalla es chica
        JPanel izq = new JPanel(new BorderLayout(5, 5));
        izq.add(new JScrollPane(controles), BorderLayout.CENTER);
        
        JScrollPane scrollRes = new JScrollPane(resultado);
        scrollRes.setBorder(BorderFactory.createTitledBorder("Resultado"));
        izq.add(scrollRes, BorderLayout.SOUTH);

        // Arma el lado derecho (el gráfico)
        JPanel der = new JPanel(new BorderLayout());
        String tituloDer = is3D ? "Representación gráfica (XYZ)" : "Representación gráfica (XY)";
        der.setBorder(BorderFactory.createTitledBorder(tituloDer));
        der.add(grafico, BorderLayout.CENTER);

        // Junta ambos lados con una barra divisoria ajustable
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, izq, der);
        split.setDividerLocation(340);
        panel.add(split, BorderLayout.CENTER);
        
        return panel;
    }

    /*
     * UTILIDAD: CREAR PANELES DE TEXTO
     * Arma un cuadrito bonito con título de color y alinea las etiquetas (x, y, z) 
     * con las cajas donde el usuario escribe.
     */
    private JPanel crearInputPanel(String titulo, JTextField[] tfs, Color color) {
        JPanel p = new JPanel(new GridLayout(tfs.length, 2, 5, 5));
        TitledBorder b = BorderFactory.createTitledBorder(titulo);
        b.setTitleColor(color); 
        p.setBorder(b);
        
        String[] labels = {"x:", "y:", "z:"};
        for (int i = 0; i < tfs.length; i++) {
            p.add(new JLabel(labels[i])); 
            p.add(tfs[i]);
        }
        return p;
    }

    /*
     * UTILIDAD: LEER TEXTO A NÚMEROS
     * Toma lo que el usuario escribió, cambia las comas por puntos (para evitar errores)
     * y lo convierte de texto (String) a números (double) para mandarlo a la clase matemática.
     */
    private VectorMath leerV(JTextField[] tfs) {
        double[] vals = new double[tfs.length];
        for (int i = 0; i < tfs.length; i++) {
            vals[i] = Double.parseDouble(tfs[i].getText().replace(",", "."));
        }
        return new VectorMath(vals);
    }

    /*
     * LÓGICA CENTRAL DE LOS BOTONES
     * Dependiendo del botón que se presionó (suma, resta, mag), hace el cálculo,
     * imprime el texto en la caja de resultados y manda a dibujar los 3 vectores.
     * El 'try-catch' evita que el programa se cierre si el usuario escribe letras.
     */
    private void procesarOp(JTextField[] tfA, JTextField[] tfB, JTextArea res, GraficoPanel g, String op) {
        try {
            VectorMath a = leerV(tfA);
            VectorMath b = leerV(tfB);
            VectorMath r = null;
            String txt = "";
            
            if (op.equals("suma")) { 
                r = a.operar(b, 1); 
                txt = "A + B = " + r.toString(); 
            } else if (op.equals("resta")) { 
                r = a.operar(b, -1); 
                txt = "A - B = " + r.toString(); 
            } else if (op.equals("mag")) {
                txt = String.format("|A| = %.3f\n|B| = %.3f", a.magnitud(), b.magnitud());
            }
            
            res.setText(txt); 
            g.limpiar();
            g.addV(a, C_A, "A"); 
            g.addV(b, C_B, "B");
            if (r != null) {
                g.addV(r, C_R, "R");
            }
        } catch (Exception ex) { 
            error(); 
        }
    }

    /*
     * UTILIDAD: AGREGAR BOTONES
     * Nos ahorra escribir 3 líneas de código cada vez que queremos agregar un botón al panel.
     */
    private void agregarBtn(JPanel p, String t, ActionListener a) {
        JButton b = new JButton(t); 
        b.addActionListener(a); 
        p.add(b);
    }
    
    /*
     * UTILIDAD: MENSAJE DE ERROR
     * Muestra una ventanita de alerta si hay problemas con los números ingresados.
     */
    private void error() { 
        JOptionPane.showMessageDialog(this, "Ingresa números válidos.", "Error de formato", JOptionPane.ERROR_MESSAGE); 
    }
}