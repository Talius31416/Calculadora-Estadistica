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
        if(maxRepeticiones == 1){
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
    public static float encontraCuartil(int cuartil){
        float PosicionCuartil = cuartil*(lista.length+1)/4.0f;
        float d = PosicionCuartil%1f;
        int Xi = (int) PosicionCuartil;
        return lista[Xi-1] + d*(lista[Xi]-lista[Xi-1]);
    }
    public static float encontrapercentil(int percentil){
        float Posicionpercentil = percentil*(lista.length+1)/100.0f;
        float d = Posicionpercentil%1f;
        int Xi = (int) Posicionpercentil;
        return lista[Xi-1] + d*(lista[Xi]-lista[Xi-1]);
    }
    public static float hallarICR(){
        return encontraCuartil(3)-encontraCuartil(2);
    }
    public static float[] hallarDatosAtipicos(){
        float limiteSuperior = (float) (encontraCuartil(1)-1.5*hallarICR());
        float LimiteInferior = (float) (encontraCuartil(3)+1.5*hallarICR());
        float[] datosAtipicos = new float[10];
        int contador = 0;
        for(int i : lista){
            if(i > limiteSuperior || i < LimiteInferior){
                datosAtipicos[contador] = i;
                contador++;
            }
        }
        return datosAtipicos;
    }


}
