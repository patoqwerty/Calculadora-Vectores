# Notas del Proyecto: Calculadora y Graficadora de Vectores (Java)

## 1. Librerías Utilizadas (Imports)
* **`javax.swing.*`**: Elementos base de la interfaz gráfica (JFrame, JPanel, JButton, JTextField, etc.).
* **`javax.swing.border.*`**: Estilos de bordes para los paneles (TitledBorder para marcos con título, EmptyBorder para márgenes invisibles).
* **`java.awt.*`**: Herramientas de dibujo y diseño geométrico (Graphics2D, Color, Font, Point, Dimension).
* **`java.awt.event.*`**: Captura de eventos del usuario (ActionListener para clics en botones, MouseAdapter para interacciones con el ratón).
* **`java.util.ArrayList` / `java.util.List`**: Estructuras de datos dinámicas para guardar la lista de los vectores que se van agregando al gráfico.

## 2. Interfaz Gráfica (UI) y Dimensiones
* **Ventana Principal (`JFrame`)**:
  * Tamaño base: Se delimitó a `1100x700` píxeles mediante el método `setSize()`.
  * Posición: Centrada automáticamente al iniciar la ejecución con `setLocationRelativeTo(null)`.
  * Comportamiento de cierre: Configurada para terminar el programa completamente al presionar la 'X' con `EXIT_ON_CLOSE`.
* **Distribución de Paneles (Layouts)**:
  * **`BorderLayout`**: Divide el área en zonas (Norte, Sur, Este, Oeste, Centro). Usado para la estructura principal de las pestañas.
  * **`BoxLayout (Y_AXIS)`**: Apila los elementos en una lista vertical pura. Usado para organizar los bloques de "Vector A", "Vector B" y "Operaciones" a la izquierda.
  * **`GridLayout`**: Crea una cuadrícula perfecta. Se usó para alinear simétricamente las etiquetas "x, y, z" junto a sus respectivas cajas de texto, y también para los botones.
* **Componentes Estructurales Clave**:
  * **`JTabbedPane`**: Contenedor principal que permite cambiar entre las interfaces "Vectores 2D" y "Vectores 3D".
  * **`JSplitPane`**: Barra divisoria ajustable ubicada entre los controles laterales y el panel de dibujo. Se fijó su posición inicial a `340px` de ancho.
  * **`JScrollPane`**: Barras de desplazamiento (scroll) agregadas tanto al cuadro de texto de resultados (`JTextArea`) como al panel izquierdo para asegurar que nada se corte en pantallas más pequeñas.

## 3. Lógica Matemática (`VectorMath`)
* **Parámetro Dinámico `double... c` (Varargs)**: Permite que el constructor reciba indistintamente 2 o 3 coordenadas sin necesidad de crear métodos por separado para 2D y 3D.
* **Almacenamiento Genérico (`double[] c`)**: Arreglo que guarda los valores del vector adaptándose a su dimensión.
* **Cálculo de Magnitud**: Se utilizó `Math.sqrt()` (raíz cuadrada) sobre la suma de los componentes al cuadrado (aplicación del teorema de Pitágoras).
* **Operador Aritmético Unificado**: Las operaciones de suma y resta se consolidaron en un solo ciclo `for`, multiplicando el segundo vector por `1` (suma) o `-1` (resta) a través de la variable `signo`.

## 4. Lógica de Gráficos y Dibujo (`Grafico2D` y `Grafico3D`)
* **Motor de Dibujo (`Graphics2D`)**: Método `paintComponent` sobrescrito para dibujar formas y líneas directamente sobre el panel.
* **Antialiasing (`RenderingHints.KEY_ANTIALIASING`)**: Filtro visual aplicado para suavizar los bordes de las líneas diagonales y texto, evitando que se vean "pixelados" o "dentados".
* **Escala Automática (`calcEscala`)**: Lógica que busca el valor máximo entre las coordenadas ingresadas y calcula un multiplicador (`escalaBase`). Garantiza que incluso un vector de magnitud 1000 se auto-ajuste para caber en el área inicial de visualización.
* **Eventos de Ratón (`MouseAdapter`)**:
  * **`mousePressed` / `mouseDragged`**: Capturan el arrastre del mouse. Actualizan los valores `offsetX` y `offsetY` para lograr el efecto de "paneo" (mover la cuadrícula). En la vista 3D, el clic izquierdo modifica las variables de los ángulos de rotación.
  * **`mouseWheelMoved`**: Detecta la rueda del ratón y modifica la variable `factorZoom` (multiplicando o dividiendo la escala por `1.1`) para acercar o alejar el plano.
* **Proyección 3D Isométrica (`proy`)**:
  * Función matemática que utiliza `Math.cos()` y `Math.sin()`.
  * Toma las coordenadas tridimensionales reales (x, y, z) junto con los ángulos de cámara virtual (`aX`, `aY`) y las aplana a coordenadas bidimensionales (`Point`), permitiendo que Swing pueda dibujarlas en una pantalla plana.

## 5. Conceptos de Programación Orientada a Objetos (POO)
* **Herencia (`extends`)**: Se usó para que la ventana principal heredara de `JFrame` y los paneles de dibujo heredaran de `JPanel`, obteniendo todas sus propiedades gráficas automáticamente.
* **Clases Abstractas (`abstract class`)**: Se creó la clase `GraficoPanel` como un "molde" base que contiene todo lo que el 2D y el 3D tienen en común (como el zoom y el control del ratón), evitando repetir código.
* **Sobrescritura de Métodos (`@Override`)**: Se utilizó para modificar el comportamiento por defecto de Java, específicamente en `paintComponent` (para decirle a Java cómo queríamos dibujar las líneas) y en los eventos del ratón.
* **Clases Internas (`static class VDraw`)**: Se usó una clase pequeña dentro de otra clase para encapsular rápidamente los datos de cada vector que se va a dibujar (guardando su vector, su color y su etiqueta en un solo "paquetito").

## 6. Herramientas Nativas de Java (Utilerías)
* **Clase `Math`**: Es el motor matemático del proyecto. Se usó intensivamente para:
  * `Math.sqrt()`: Calcular la raíz cuadrada para la magnitud.
  * `Math.max()` / `Math.min()`: Determinar los límites de la pantalla y ajustar la escala automáticamente.
  * `Math.cos()` / `Math.sin()`: Calcular las proyecciones isométricas en 3D usando trigonometría.
  * `Math.toRadians()`: Convertir los grados de rotación de la cámara 3D a radianes.
* **Expresiones Lambda (`->`)**: Se usaron en los botones (ej. `e -> graficar.run()`) para escribir funciones anónimas de manera moderna y en una sola línea, en lugar de crear clases `ActionListener` completas.
* **Manejo de Cadenas (`String.format`)**: Se utilizó para formatear la salida del texto, limitando los resultados a un máximo de dos o tres decimales (ej. `%.2f`) para que la pantalla no se llene de números infinitos.
* **Manejo de Excepciones (`try - catch`)**: Se implementó en los botones de operaciones. Sirve como un escudo: si el usuario ingresa una letra (como "a") en lugar de un número, el `catch` atrapa el error (`NumberFormatException` o `Exception`) y en lugar de que el programa se cierre de golpe, muestra un mensaje amigable con `JOptionPane.showMessageDialog`.

## 7. Características Avanzadas y Estructuras Internas de Java
* **Hilo de Despacho de Eventos (EDT)**: Se usó `SwingUtilities.invokeLater()`. Sirve para inicializar la interfaz gráfica de forma segura en un hilo de procesamiento separado (concurrencia). Esto asegura que la ventana no se congele o parpadee mientras el programa hace otras operaciones matemáticas pesadas.
* **Operador Ternario (`? :`)**: Es un `if-else` comprimido en una sola línea. Se utilizó intensivamente en la creación de la interfaz para decidir qué cargar (ej. `is3D ? new Grafico3D() : new Grafico2D()`), reduciendo a la mitad la cantidad de código necesario para diferenciar las pestañas 2D y 3D.
* **Casteo de Objetos (Type Casting)**: Se aplicó en `(Graphics2D) g`. Sirve para "forzar" o convertir un objeto básico (como la brocha de dibujo por defecto `Graphics`) en su versión más avanzada (`Graphics2D`), desbloqueando funciones como el grosor de línea (`BasicStroke`) y el antialiasing.
* **Clases Anónimas**: Implementado con `new MouseAdapter() { ... }`. Sirve para instanciar una clase abstracta al vuelo (en el momento que se necesita) y sobrescribir sus métodos ahí mismo, sin tener que crear un archivo o una clase completamente nueva solo para decirle al programa qué hacer cuando se arrastra el ratón.
* **Uso de Constantes Inmutables (`static final`)**: Se usó para definir la paleta de colores de los vectores (`C_A`, `C_B`, `C_R`). `static` hace que pertenezcan a la clase y no al objeto (ahorrando memoria RAM), y `final` asegura que nadie pueda modificar esos colores por accidente durante la ejecución del programa.
* **Conversión de Tipos (Parsing y Wrappers)**: Se usó la clase envoltorio con `Double.parseDouble()`. Sirve para tomar el texto puro (`String`) que el usuario escribe en la interfaz y transformarlo a un tipo primitivo numérico (`double`), lo cual es indispensable porque Java no puede sumar o restar letras.
* **Sanitización de Datos (`replace(",", ".")`)**: Método de seguridad encadenado al momento de leer el texto. Sirve para interceptar comas y convertirlas en puntos antes de hacer el cálculo matemático, previniendo que el sistema arroje un error fatal si un usuario ingresa "3,5" en vez del formato gringo "3.5".
