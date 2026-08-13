import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/*
 * CLASE GRAFICOPANEL (CLASE BASE)
 * Es el "molde" principal para los gráficos de tu proyecto de cálculo vectorial.
 * Al ser abstracta, no se usa directamente, sino que hereda sus funciones 
 * comunes a las vistas 2D y 3D (como el zoom y el control del ratón).
 */
public abstract class GraficoPanel extends JPanel {
    
    /*
     * CLASE INTERNA VDRAW
     * Un contenedor rápido para empaquetar la información de cada vector:
     * sus cálculos (VectorMath), su color en pantalla y su nombre/etiqueta.
     */
    public static class VDraw { 
        public VectorMath v; 
        public Color c; 
        public String tag; 
        
        public VDraw(VectorMath v, Color c, String tag) {
            this.v = v; 
            this.c = c; 
            this.tag = tag;
        } 
    }
    
    // Lista para guardar todos los vectores que se van agregando al plano
    protected List<VDraw> vectores = new ArrayList<>();
    
    // Variables para controlar el zoom y el movimiento (paneo) del lienzo
    protected double escalaBase = 40;
    protected double factorZoom = 1.0;
    protected int offsetX = 0;
    protected int offsetY = 0;
    protected Point lastP;

    /*
     * CONSTRUCTOR
     * Prepara el lienzo en blanco e inicializa los "escuchadores" del ratón
     * para detectar clics, arrastres y el giro de la rueda.
     */
    public GraficoPanel() {
        setBackground(Color.WHITE); 
        setPreferredSize(new Dimension(500, 450));
        
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) { 
                lastP = e.getPoint(); // Guarda las coordenadas del clic inicial
            }
            
            @Override
            public void mouseDragged(MouseEvent e) {
                if (lastP != null) {
                    // Delega el tipo de movimiento a las clases hijas (2D o 3D)
                    handleDrag(e.getX() - lastP.x, e.getY() - lastP.y, e);
                    lastP = e.getPoint(); 
                    repaint(); // Fuerza a que la pantalla se actualice
                }
            }
            
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                // Modifica el zoom dependiendo hacia dónde gires la rueda
                factorZoom *= (e.getWheelRotation() < 0) ? 1.1 : (1/1.1);
                
                // Un candado para evitar que alejes la vista más allá del límite original
                if (factorZoom < 1.0) { 
                    factorZoom = 1.0; 
                    offsetX = 0; 
                    offsetY = 0; 
                }
                repaint();
            }
        };
        
        addMouseListener(ma); 
        addMouseMotionListener(ma); 
        addMouseWheelListener(ma);
    }
    
    // Método vacío que obliga a las clases 2D/3D a programar su propio comportamiento de arrastre
    protected abstract void handleDrag(int dx, int dy, MouseEvent e);
    
    // Borra todo del panel, reinicia el zoom y centra la vista
    public void limpiar() { 
        vectores.clear(); 
        offsetX = 0; 
        offsetY = 0; 
        factorZoom = 1.0; 
        repaint(); 
    }
    
    // Agrega un vector nuevo a la lista, recalcula la escala para que quepa y redibuja
    public void addV(VectorMath v, Color c, String t) { 
        vectores.add(new VDraw(v, c, t)); 
        calcEscala(); 
        repaint(); 
    }
    
    /*
     * AUTO-ESCALADO
     * Busca la coordenada más grande de todos los vectores actuales y 
     * genera una escala base para asegurar que absolutamente todo encaje en la ventana.
     */
    protected void calcEscala() {
        double max = 5;
        for (VDraw vd : vectores) {
            for (double val : vd.v.c) {
                max = Math.max(max, Math.abs(val));
            }
        }
        escalaBase = Math.max(160, Math.min(getWidth(), getHeight()) / 2 - 40) / (max * 1.5);
        factorZoom = 1.0; 
        offsetX = 0; 
        offsetY = 0;
    }
    
    // Combina la escala base calculada con el zoom actual del usuario
    protected double getEscala() { 
        return escalaBase * factorZoom; 
    }
}