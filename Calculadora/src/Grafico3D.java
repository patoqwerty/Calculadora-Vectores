import java.awt.*;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;

/*
 * CLASE GRAFICO3D
 * Se encarga de la visualización en 3 dimensiones.
 * Utiliza matemáticas de proyección para simular profundidad en una pantalla 2D.
 */
public class Grafico3D extends GraficoPanel {
    
    /* Ángulos iniciales de la cámara (inclinación y rotación). */
    private double aX = Math.toRadians(20);
    private double aY = Math.toRadians(-45);
    
    /*
     * CONTROL DEL RATÓN EN 3D
     * Si se usa el clic derecho o el botón central, desplaza el plano (paneo).
     * Si se usa el clic izquierdo (el 'else'), rota la cámara modificando los ángulos.
     */
    @Override
    protected void handleDrag(int dx, int dy, MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e) || SwingUtilities.isMiddleMouseButton(e)) {
            offsetX += dx; 
            offsetY += dy;
        } else {
            aY += dx * 0.01; 
            /* Limita la inclinación vertical para que la cámara no dé la vuelta completa. */
            aX = Math.max(Math.toRadians(-89), Math.min(Math.toRadians(89), aX + dy * 0.01));
        }
    }

    /*
     * PROYECCIÓN ISOMÉTRICA
     * Convierte las coordenadas reales 3D (x, y, z) a coordenadas 2D (x, y) de la pantalla.
     * Usa trigonometría básica junto con los ángulos actuales de rotación de la cámara.
     */
    private Point proy(double x, double y, double z, int cx, int cy) {
        double x1 = x * Math.cos(aY) + z * Math.sin(aY);
        double z1 = -x * Math.sin(aY) + z * Math.cos(aY);
        double y2 = y * Math.cos(aX) - z1 * Math.sin(aX);
        return new Point(cx + (int)(x1 * getEscala()) + offsetX, cy - (int)(y2 * getEscala()) + offsetY);
    }

    /*
     * MÉTODO DE DIBUJO PRINCIPAL
     * Traza el "suelo", las "paredes", los ejes de colores y finalmente los vectores.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        /* Antialiasing para suavizar los bordes de las líneas. */
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int cx = getWidth() / 2;
        int cy = getHeight() / 2;
        
        /* Dibuja las cuadrículas de fondo (planos XY y XZ) simulando el espacio 3D. */
        g2.setColor(new Color(240, 240, 240));
        for(int i = -20; i <= 20; i += 2) {
            // Líneas del plano vertical
            Point p1 = proy(i, -20, 0, cx, cy), p2 = proy(i, 20, 0, cx, cy);
            Point p3 = proy(-20, i, 0, cx, cy), p4 = proy(20, i, 0, cx, cy);
            g2.drawLine(p1.x, p1.y, p2.x, p2.y); 
            g2.drawLine(p3.x, p3.y, p4.x, p4.y); 
            
            // Líneas del plano horizontal (el suelo)
            p1 = proy(i, 0, -20, cx, cy); p2 = proy(i, 0, 20, cx, cy); 
            p3 = proy(-20, 0, i, cx, cy); p4 = proy(20, 0, i, cx, cy);
            g2.drawLine(p1.x, p1.y, p2.x, p2.y); 
            g2.drawLine(p3.x, p3.y, p4.x, p4.y); 
        }

        /* Calcula dónde terminan los ejes X, Y, Z para poder dibujarlos. */
        Point o = proy(0, 0, 0, cx, cy);
        Point px = proy(15, 0, 0, cx, cy);
        Point py = proy(0, 15, 0, cx, cy);
        Point pz = proy(0, 0, 15, cx, cy);
        
        /* Dibuja los ejes con colores distintivos: Rojo (X), Verde (Y), Azul (Z). */
        g2.setStroke(new BasicStroke(1.8f));
        g2.setColor(new Color(220, 50, 50)); g2.drawLine(o.x, o.y, px.x, px.y); 
        g2.setColor(new Color(50, 180, 50)); g2.drawLine(o.x, o.y, py.x, py.y); 
        g2.setColor(new Color(50, 50, 220)); g2.drawLine(o.x, o.y, pz.x, pz.y); 

        /* Recorre la lista de vectores y los proyecta en el espacio 3D. */
        for (VDraw vd : vectores) {
            g2.setColor(vd.c); 
            g2.setStroke(new BasicStroke(2.5f));
            
            // Calcula en qué parte de la pantalla plana cae la punta del vector 3D
            Point p = proy(vd.v.get(0), vd.v.get(1), vd.v.get(2), cx, cy);
            
            g2.drawLine(o.x, o.y, p.x, p.y);
            g2.fillOval(p.x - 4, p.y - 4, 8, 8); // Punta del vector
            g2.drawString(vd.tag + " " + vd.v.toString(), p.x + 8, p.y - 8); // Etiqueta
        }
    }
}