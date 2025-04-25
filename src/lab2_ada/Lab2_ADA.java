package lab2_ada;
import java.util.Scanner;


public class Lab2_ADA {
public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
       
        System.out.print("Ingresar el numero de valores: ");
        int n = scanner.nextInt();
        
        if (n <= 0) {
           System.out.println("El numero de valores debe ser mayor que 0");
           scanner.close();
           return;}
        int[] numeros = new int[n];
        System.out.println("Ingrese los " + n + " números:");
        for (int i = 0; i < n; i++) {
         numeros[i] = scanner.nextInt(); }
       
        int maxSuma = SumaMaxima(numeros);
       
        System.out.println("La suma máxima de la subsecuencia es: " + maxSuma); }
   
    public static int SumaMaxima(int[] arr) {
        int maxActual = 0;
        int maxGlobal = 0;
       
      boolean todosNegativos = true;
       for (int num : arr) {
           if (num >= 0) {
                todosNegativos = false;
                break; }
           if (num > maxGlobal) {
            maxGlobal = num;
}}
     if (todosNegativos) {
          return 0;}
      for (int i = 0; i < arr.length; i++) {
    int sumaActual = maxActual + arr[i];
    if (sumaActual > 0) {
        maxActual = sumaActual;
}else {
        maxActual = 0;}
   if (maxActual > maxGlobal) {
        maxGlobal = maxActual;
}}      
      return maxGlobal;}
}
