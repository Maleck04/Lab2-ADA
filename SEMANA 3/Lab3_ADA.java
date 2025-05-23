
package lab3_ada;

import java.util.Stack;

public class Lab3_ADA {

  public static int ackermann(int m, int n) {
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{m, n});
        int result = 0;

        while (!stack.isEmpty()) {
            int[] entry = stack.pop();
            int currentM = entry[0];
            int currentN = entry[1];

            if (currentN != -1) {
                if (currentM == 0) {
                    result = currentN + 1;
                } else if (currentN == 0) {
                    stack.push(new int[]{currentM - 1, 1});
                } else {
                    stack.push(new int[]{currentM - 1, -1});
                    stack.push(new int[]{currentM, currentN - 1});
                }
            } else {
                stack.push(new int[]{currentM, result});
            }
        }

        return result;
    }

    private static int leerEntero(String mensaje) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        while (true) {
            try {
                System.out.print(mensaje);
                String entrada = scanner.nextLine().trim();
                int valor = Integer.parseInt(entrada);
                
                if (valor < 0) {
                    System.out.println("Error: El valor no puede ser negativo.");
                } else {
                    return valor;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingresa un número entero válido.");
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Función de Ackermann-Péter");
        int m = leerEntero("Ingresa el valor de m (entero no negativo): ");
        int n = leerEntero("Ingresa el valor de n (entero no negativo): ");
        
        System.out.println("Resultado: A(" + m + ", " + n + ") = " + ackermann(m, n));
    }
}