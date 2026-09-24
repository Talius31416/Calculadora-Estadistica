import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CalculadoraEstadistica {

    // Devuelve una copia ordenada del arreglo para no alterar los datos originales
    public static double[] ordenar(double[] datos) {
        double[] copia = datos.clone();
        Arrays.sort(copia);
        return copia;
    }

    public static double media(double[] datos) {
        double suma = 0;
        for (double x : datos) {
            suma += x;
        }
        return suma / datos.length;
    }

    public static double desviacionEstandar(double[] datos) {
        if (datos.length <= 1) return 0.0;
        double m = media(datos);
        double sumaCuadrados = 0;
        for (double x : datos) {
            sumaCuadrados += Math.pow(x - m, 2);
        }
        return Math.sqrt(sumaCuadrados / (datos.length - 1));
    }

    // Posición con la fórmula L = r*(n+1)/4 con interpolación
    public static double cuartil(double[] datos, int r) {
        double[] ordenados = ordenar(datos);
        int n = ordenados.length;
        double pos = (r * (n + 1)) / 4.0;
        return interpolar(ordenados, pos);
    }

    // Posición del percentil k (1 <= k <= 99)
    public static double percentil(double[] datos, double k) {
        double[] ordenados = ordenar(datos);
        int n = ordenados.length;
        double pos = (k * (n + 1)) / 100.0;
        return interpolar(ordenados, pos);
    }

    private static double interpolar(double[] datos, double pos) {
        int n = datos.length;
        if (pos <= 1) return datos[0];
        if (pos >= n) return datos[n - 1];

        int i = (int) Math.floor(pos);
        double d = pos - i;
        return datos[i - 1] + d * (datos[i] - datos[i - 1]);
    }

    public static double iqr(double[] datos) {
        return cuartil(datos, 3) - cuartil(datos, 1);
    }

    public static List<Double> obtenerAtipicos(double[] datos) {
        double q1 = cuartil(datos, 1);
        double q3 = cuartil(datos, 3);
        double iqrVal = q3 - q1;
        double limiteInferior = q1 - 1.5 * iqrVal;
        double limiteSuperior = q3 + 1.5 * iqrVal;

        List<Double> atipicos = new ArrayList<>();
        for (double x : datos) {
            if (x < limiteInferior || x > limiteSuperior) {
                atipicos.add(x);
            }
        }
        return atipicos;
    }

    // Estructura de datos para la Tabla de Frecuencias
    public static class Intervalo {
        public double limiteInferior;
        public double limiteSuperior;
        public double marcaDeClase;
        public int frecuenciaAbsoluta;
        public int frecuenciaAcumulada;
        public double frecuenciaRelativa;

        public Intervalo(double li, double ls, double mc, int fa, int fac, double fr) {
            this.limiteInferior = li;
            this.limiteSuperior = ls;
            this.marcaDeClase = mc;
            this.frecuenciaAbsoluta = fa;
            this.frecuenciaAcumulada = fac;
            this.frecuenciaRelativa = fr;
        }
    }

    public static List<Intervalo> generarTablaFrecuencias(double[] datos) {
        double[] ordenados = ordenar(datos);
        int n = ordenados.length;
        double min = ordenados[0];
        double max = ordenados[n - 1];
        double rango = max - min;

        // Regla de Sturges
        int k = (int) Math.round(1 + 3.322 * Math.log10(n));
        if (k <= 0) k = 1;

        double amplitud = rango / k;
        if (amplitud == 0) amplitud = 1.0;

        List<Intervalo> tabla = new ArrayList<>();
        int fac = 0;
        double liActual = min;

        for (int i = 0; i < k; i++) {
            double lsActual = liActual + amplitud;
            double mc = (liActual + lsActual) / 2.0;

            int fa = 0;
            for (double x : ordenados) {
                if (i == k - 1) { // El último intervalo es cerrado [Li, Ls]
                    if (x >= liActual && x <= lsActual + 1e-9) fa++;
                } else { // Intervalos semiabiertos [Li, Ls)
                    if (x >= liActual && x < lsActual) fa++;
                }
            }

            fac += fa;
            double fr = (double) fa / n;
            tabla.add(new Intervalo(liActual, lsActual, mc, fa, fac, fr));
            liActual = lsActual;
        }

        return tabla;
    }
}
