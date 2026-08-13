import javax.swing.SwingUtilities;

/*
 * CLASE MAIN
 * Es el punto de partida del programa. 
 * Todo el código de la calculadora comienza a ejecutarse desde aquí.
 */
public class Main {
    
    public static void main(String[] args) {
        /*
         * Arranca la interfaz gráfica de manera segura.
         * Instancia la ventana principal del proyecto y la hace visible en pantalla.
         */
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }
}