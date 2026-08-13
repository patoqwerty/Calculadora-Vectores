import java.awt.*;
import java.awt.event.MouseEvent;

/*
 * CLASE GRAFICO2D
 * Se encarga de dibujar el plano cartesiano tradicional en 2 dimensiones (XY).
 * Hereda las herramientas base de GraficoPanel.
 */
public class Grafico2D extends GraficoPanel {
    
    /*
     * Controla el movimiento del plano al arrastrar el ratón.
     * Suma el desplazamiento para simular que movemos la "cámara".
     */
    @Override
    protected void handleDrag(int dx, int dy, MouseEvent e) { 
        offsetX += dx; 
        offsetY += dy; 
    }
    
    /*
         MÉTODO DE DIBUJO PRINCIPAL
        Aquí se plasma visualmente todo: fondo, cuadrícula, ejes y los vectores.
     */
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        /* Suaviza los bordes de las líneas para que no se vean pixeladas. */
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        /* Calcula el centro exacto de la vista aplicando el desplazamiento del ratón. */
        int cx = getWidth() / 2 + offsetX;
        int cy = getHeight() / 2 + offsetY;
        double esc = getEscala();
        
        /* Dibuja la cuadrícula de fondo (líneas grises claras). */
        g2.setColor(new Color(235, 235, 235));
        for(int i = -50; i <= 50; i++) {
            int p = (int)(i * esc);
            g2.drawLine(cx + p, 0, cx + p, getHeight()); 
            g2.drawLine(0, cy - p, getWidth(), cy - p);
        }
        
        /* Dibuja los ejes principales X e Y centrados (líneas oscuras). */
        g2.setColor(Color.DARK_GRAY); 
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawLine(0, cy, getWidth(), cy); 
        g2.drawLine(cx, 0, cx, getHeight());
        
        /* Recorre la lista de vectores guardados y dibuja cada uno en el plano. */
        for (VDraw vd : vectores) {
            g2.setColor(vd.c); 
            g2.setStroke(new BasicStroke(2.5f));
            
            /* Convierte las coordenadas del vector al tamaño en píxeles de la pantalla. */
            int x2 = cx + (int)(vd.v.get(0) * esc);
            int y2 = cy - (int)(vd.v.get(1) * esc);
            
            g2.drawLine(cx, cy, x2, y2); // Dibuja la línea del vector
            g2.fillOval(x2 - 4, y2 - 4, 8, 8); // Dibuja la punta usando un círculo pequeño
            g2.drawString(vd.tag + " " + vd.v.toString(), x2 + 6, y2 - 6); // Imprime el texto de la etiqueta
        }
    }
}