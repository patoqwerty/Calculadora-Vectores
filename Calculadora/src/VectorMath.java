/*
 * CLASE VECTORMATH (NÚCLEO MATEMÁTICO)
 * Es el "cerebro" de la calculadora. Maneja la lógica matemática adaptándose 
 * automáticamente para vectores de 2D o 3D sin tener que duplicar código.
 */
public class VectorMath {
    
    // Arreglo genérico que guarda los componentes del vector (x, y) o (x, y, z).
    protected double[] c;

    /*
     * CONSTRUCTOR
     * Usa "varargs" (double... c) para aceptar 2 o 3 coordenadas de jalón 
     * al momento de crear el vector.
     */
    public VectorMath(double... c) { 
        this.c = c; 
    }

    // Devuelve la coordenada exacta en la posición solicitada (0=X, 1=Y, 2=Z).
    public double get(int i) { 
        return c[i]; 
    }
    
    /*
     * MAGNITUD
     * Aplica el teorema de Pitágoras para calcular la longitud real del vector
     * (la raíz cuadrada de la suma de sus componentes al cuadrado).
     */
    public double magnitud() {
        double sum = 0;
        for (double v : c) sum += v * v;
        return Math.sqrt(sum);
    }
    
    /*
     * OPERACIONES (SUMA Y RESTA)
     * Utiliza un multiplicador (signo) que vale 1 para sumar o -1 para restar.
     * Recorre cada componente y genera un nuevo vector con el resultado.
     */
    public VectorMath operar(VectorMath otro, int signo) {
        double[] res = new double[c.length];
        for (int i = 0; i < c.length; i++) {
            res[i] = this.c[i] + (otro.c[i] * signo);
        }
        return new VectorMath(res);
    }
    
    /*
     * VISUALIZACIÓN DE TEXTO
     * Da formato a los resultados matemáticos para que se impriman en pantalla
     * de manera limpia y siempre redondeados a 2 decimales.
     */
    @Override
    public String toString() {
        if (c.length == 2) return String.format("(%.2f, %.2f)", c[0], c[1]);
        return String.format("(%.2f, %.2f, %.2f)", c[0], c[1], c[2]);
    }
}