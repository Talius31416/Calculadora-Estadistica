import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double[] datos = null;

        while (true) {
            imprimirMenu();
            System.out.print("Seleccione una opción: ");
            String opcionStr = scanner.nextLine().trim();

            if (opcionStr.equals("6")) {
                System.out.println("\n¡Saliendo de la Calculadora Estadística!");
                break;
            }

            switch (opcionStr) {
                case "1":
                    datos = ingresarDatos(scanner);
                    break;
                case "2":
                    if (validarDatos(datos)) mostrarEstadisticasBasicas(datos);
                    break;
                case "3":
                    if (validarDatos(datos)) mostrarCuartilesYAtipicos(datos);
                    break;
                case "4":
                    if (validarDatos(datos)) calcularPercentil(datos, scanner);
                    break;
                case "5":
                    if (validarDatos(datos)) mostrarTablaFrecuencias(datos);
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.\n");
            }
        }
        scanner.close();
    }

    private static void imprimirMenu() {
        System.out.println("=============================================");
        System.out.println("      CALCULADORA ESTADÍSTICA (TUI)          ");
        System.out.println("=============================================");
        System.out.println("1. Ingresar / Actualizar conjunto de datos");
        System.out.println("2. Mostrar estadísticas descriptivas generales");
        System.out.println("3. Calcular Cuartiles, IQR y Detectar Atípicos");
        System.out.println("4. Calcular Percentil personalizado");
        System.out.println("5. Generar Tabla de Frecuencias (Regla de Sturges)");
        System.out.println("6. Salir");
        System.out.println("=============================================");
    }

    private static double[] ingresarDatos(Scanner scanner) {
        System.out.print("\nIngrese los datos separados por espacios o comas: ");
        String entrada = scanner.nextLine().trim();
        if (entrada.isEmpty()) {
            System.out.println("Entrada vacía. Operación cancelada.\n");
            return null;
        }

        String[] partes = entrada.split("[,\\s]+");
        double[] temp = new double[partes.length];
        int cont = 0;

        for (String parte : partes) {
            try {
                temp[cont++] = Double.parseDouble(parte);
            } catch (NumberFormatException e) {
                System.out.println("Advertencia: Se omitió el valor no numérico '" + parte + "'");
            }
        }

        if (cont == 0) {
            System.out.println("No se ingresaron datos válidos.\n");
            return null;
        }

        double[] datos = Arrays.copyOf(temp, cont);
        System.out.println("¡Datos cargados correctamente! Total de muestra (n): " + datos.length + "\n");
        return datos;
    }

    private static boolean validarDatos(double[] datos) {
        if (datos == null || datos.length == 0) {
            System.out.println("\n[ERROR] Primero debe ingresar un conjunto de datos (Opción 1).\n");
            return false;
        }
        return true;
    }

    private static void mostrarEstadisticasBasicas(double[] datos) {
        System.out.println("\n--- ESTADÍSTICAS DESCRIPTIVAS GENERALES ---");
        double[] ordenados = CalculadoraEstadistica.ordenar(datos);
        System.out.printf("Muestra (n): %d\n", datos.length);
        System.out.printf("Valor Mínimo: %.4f\n", ordenados[0]);
        System.out.printf("Valor Máximo: %.4f\n", ordenados[ordenados.length - 1]);
        System.out.printf("Media (x̄): %.4f\n", CalculadoraEstadistica.media(datos));
        System.out.printf("Mediana (Q2): %.4f\n", CalculadoraEstadistica.cuartil(datos, 2));
        System.out.printf("Desviación Estándar (s): %.4f\n\n", CalculadoraEstadistica.desviacionEstandar(datos));
    }

    private static void mostrarCuartilesYAtipicos(double[] datos) {
        System.out.println("\n--- CUARTILES, IQR Y VALORES ATÍPICOS ---");
        double q1 = CalculadoraEstadistica.cuartil(datos, 1);
        double q2 = CalculadoraEstadistica.cuartil(datos, 2);
        double q3 = CalculadoraEstadistica.cuartil(datos, 3);
        double iqr = CalculadoraEstadistica.iqr(datos);

        System.out.printf("Q1 (25%%): %.4f\n", q1);
        System.out.printf("Q2 (50%% / Mediana): %.4f\n", q2);
        System.out.printf("Q3 (75%%): %.4f\n", q3);
        System.out.printf("Rango Intercuartílico (IQR): %.4f\n", iqr);

        List<Double> atipicos = CalculadoraEstadistica.obtenerAtipicos(datos);
        System.out.println("Valores Atípicos detectados: " + (atipicos.isEmpty() ? "Ninguno" : atipicos.toString()));
        System.out.println();
    }

    private static void calcularPercentil(double[] datos, Scanner scanner) {
        System.out.print("\nIngrese el percentil deseado (1 a 99): ");
        try {
            double k = Double.parseDouble(scanner.nextLine().trim());
            if (k < 1 || k > 99) {
                System.out.println("El percentil debe estar entre 1 y 99.\n");
                return;
            }
            double pk = CalculadoraEstadistica.percentil(datos, k);
            System.out.printf("Percentil P_%.2f = %.4f\n\n", k, pk);
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Ingrese un valor numérico.\n");
        }
    }

    private static void mostrarTablaFrecuencias(double[] datos) {
        System.out.println("\n--- TABLA DE FRECUENCIAS (AGRUPADAS) ---");
        List<CalculadoraEstadistica.Intervalo> tabla = CalculadoraEstadistica.generarTablaFrecuencias(datos);

        System.out.printf("%-20s | %-12s | %-8s | %-8s | %-8s\n", "Intervalo [Li, Ls)", "Marca Clase", "Fa", "Fac", "Fr");
        System.out.println("-------------------------------------------------------------------");
        for (CalculadoraEstadistica.Intervalo inter : tabla) {
            System.out.printf("[%7.2f, %7.2f) | %12.2f | %8d | %8d | %8.4f\n",
                    inter.limiteInferior, inter.limiteSuperior, inter.marcaDeClase,
                    inter.frecuenciaAbsoluta, inter.frecuenciaAcumulada, inter.frecuenciaRelativa);
        }
        System.out.println();
    }
}
