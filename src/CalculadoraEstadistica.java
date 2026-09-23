import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CalculadoraEstadistica {
    public static int[] lista;
    public CalculadoraEstadistica(int[] lista){
        this.lista = lista;
        Arrays.sort(lista);
    }
    public static float promedio(){
        float promedio = 0f;
        for(int i = 0; i<lista.length;i++){
            promedio += lista[i];
        }
        return promedio/lista.length;
    }
    public float mediana(){
        if(lista.length/2 == 0){
            return lista.length/2;
        }else{
            int primeraMitad = lista.length/2;
            int segundaMitad = primeraMitad-1;
            return (primeraMitad+segundaMitad)/2f;
        }
    }
    public static int obtenerMasRepetido(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("El arreglo no puede estar vacío");
        }

        Map<Integer, Integer> conteo = new HashMap<>();
        int elementoMasFrecuente = arr[0];
        int maxRepeticiones = 0;

        for (int num : arr) {
            // Obtiene el conteo actual o 0 si no existe, y le suma 1
            int repeticionesActuales = conteo.getOrDefault(num, 0) + 1;
            conteo.put(num, repeticionesActuales);

            // Actualiza el elemento más frecuente si se supera el máximo anterior
            if (repeticionesActuales > maxRepeticiones) {
                maxRepeticiones = repeticionesActuales;
                elementoMasFrecuente = num;
            }
        }
        if(elementoMasFrecuente == arr[1]){
            return 0;
        }

        return elementoMasFrecuente;
    }
    public static float calcularVarianza(){
        float sumatoria = 0.f;
        float promedio = promedio();
        for(int i = 0; i<lista.length; i++){
            sumatoria += Math.abs(Math.pow(lista[i]-promedio,2));
        }
        return sumatoria/ lista.length/2;
    }
    public float calcularDE(){
        return (float) Math.sqrt(calcularVarianza());
    }

}
