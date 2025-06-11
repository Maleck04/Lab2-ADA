package lab7_ada;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

class Cliente {
    String codigo;
    String nombres;
    String apellidos;
    String telefono;
    String correo;
    String direccion;
    String codigoPostal;

    public Cliente(String codigo, String nombres, String apellidos, String telefono, 
                   String correo, String direccion, String codigoPostal) {
        this.codigo = codigo;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.codigoPostal = codigoPostal;
    }

    @Override
    public String toString() {
        return nombres + " " + apellidos + " (" + codigo + ")";
    }
}

interface HashTable {
    void put(Cliente cliente);
    Cliente get(String nombres, String apellidos);
    long getInsertionTime();
    long getSearchTime();
}

// Implementación con reasignación lineal
class LinearProbingHashTable implements HashTable {
    private Cliente[] table;
    private int size;
    private long insertionTime;
    private long searchTime;

    public LinearProbingHashTable(int size) {
        this.size = size;
        table = new Cliente[size];
        insertionTime = 0;
        searchTime = 0;
    }

    private int hash(String nombres, String apellidos) {
        String key = nombres + apellidos;
        return Math.abs(key.hashCode()) % size;
    }

    @Override
    public void put(Cliente cliente) {
        long startTime = System.nanoTime();
        int index = hash(cliente.nombres, cliente.apellidos);
        int originalIndex = index;
        
        while (table[index] != null) {
            index = (index + 1) % size;
            if (index == originalIndex) {
                JOptionPane.showMessageDialog(null, "Tabla llena!");
                return;
            }
        }
        
        table[index] = cliente;
        insertionTime = System.nanoTime() - startTime;
    }

    @Override
    public Cliente get(String nombres, String apellidos) {
        long startTime = System.nanoTime();
        int index = hash(nombres, apellidos);
        int originalIndex = index;
        
        while (table[index] != null) {
            if (table[index].nombres.equals(nombres) && 
                table[index].apellidos.equals(apellidos)) {
                searchTime = System.nanoTime() - startTime;
                return table[index];
            }
            index = (index + 1) % size;
            if (index == originalIndex) break;
        }
        
        searchTime = System.nanoTime() - startTime;
        return null;
    }

    @Override
    public long getInsertionTime() {
        return insertionTime;
    }

    @Override
    public long getSearchTime() {
        return searchTime;
    }
}

// Nodo para el árbol binario
class BSTNode {
    Cliente cliente;
    BSTNode left;
    BSTNode right;

    public BSTNode(Cliente cliente) {
        this.cliente = cliente;
    }
}

// Implementación con encadenamiento usando BST
class ChainingBSTHashTable implements HashTable {
    private BSTNode[] table;
    private int size;
    private long insertionTime;
    private long searchTime;

    public ChainingBSTHashTable(int size) {
        this.size = size;
        table = new BSTNode[size];
        insertionTime = 0;
        searchTime = 0;
    }

    private int hash(String nombres, String apellidos) {
        String key = nombres + apellidos;
        return Math.abs(key.hashCode()) % size;
    }

    @Override
    public void put(Cliente cliente) {
        long startTime = System.nanoTime();
        int index = hash(cliente.nombres, cliente.apellidos);
        table[index] = insertBST(table[index], cliente);
        insertionTime = System.nanoTime() - startTime;
    }

    private BSTNode insertBST(BSTNode root, Cliente cliente) {
        if (root == null) {
            return new BSTNode(cliente);
        }
        
        int cmp = cliente.nombres.compareTo(root.cliente.nombres);
        if (cmp == 0) cmp = cliente.apellidos.compareTo(root.cliente.apellidos);
        
        if (cmp < 0) {
            root.left = insertBST(root.left, cliente);
        } else if (cmp > 0) {
            root.right = insertBST(root.right, cliente);
        }
        return root;
    }

    @Override
    public Cliente get(String nombres, String apellidos) {
        long startTime = System.nanoTime();
        int index = hash(nombres, apellidos);
        BSTNode result = searchBST(table[index], nombres, apellidos);
        searchTime = System.nanoTime() - startTime;
        return (result != null) ? result.cliente : null;
    }

    private BSTNode searchBST(BSTNode root, String nombres, String apellidos) {
        if (root == null) return null;
        
        int cmp = nombres.compareTo(root.cliente.nombres);
        if (cmp == 0) cmp = apellidos.compareTo(root.cliente.apellidos);
        
        if (cmp < 0) return searchBST(root.left, nombres, apellidos);
        else if (cmp > 0) return searchBST(root.right, nombres, apellidos);
        else return root;
    }

    @Override
    public long getInsertionTime() {
        return insertionTime;
    }

    @Override
    public long getSearchTime() {
        return searchTime;
    }
}

public class Lab7_ADA extends JFrame {
    private HashTable linearTable = new LinearProbingHashTable(128);
    private HashTable bstTable = new ChainingBSTHashTable(128);
    private JTextArea outputArea = new JTextArea(15, 50);

    public Lab7_ADA() {
        super("Gestión de Clientes con Tabla Hash");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Panel de entrada
        JPanel inputPanel = new JPanel(new GridLayout(8, 2, 5, 5));
        JTextField codigoField = new JTextField();
        JTextField nombresField = new JTextField();
        JTextField apellidosField = new JTextField();
        JTextField telefonoField = new JTextField();
        JTextField correoField = new JTextField();
        JTextField direccionField = new JTextField();
        JTextField postalField = new JTextField();
        
        inputPanel.add(new JLabel("Código:"));
        inputPanel.add(codigoField);
        inputPanel.add(new JLabel("Nombres:"));
        inputPanel.add(nombresField);
        inputPanel.add(new JLabel("Apellidos:"));
        inputPanel.add(apellidosField);
        inputPanel.add(new JLabel("Teléfono:"));
        inputPanel.add(telefonoField);
        inputPanel.add(new JLabel("Correo:"));
        inputPanel.add(correoField);
        inputPanel.add(new JLabel("Dirección:"));
        inputPanel.add(direccionField);
        inputPanel.add(new JLabel("Código Postal:"));
        inputPanel.add(postalField);
        
        // Botones
        JButton insertarBtn = new JButton("Insertar Cliente");
        JButton buscarBtn = new JButton("Buscar por Nombre/Apellido");
        JButton compararBtn = new JButton("Comparar Métodos");
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(insertarBtn);
        buttonPanel.add(buscarBtn);
        buttonPanel.add(compararBtn);
        
        // Área de salida
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        
        // Organizar componentes
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
        
        // Manejadores de eventos
        insertarBtn.addActionListener(e -> {
            Cliente cliente = new Cliente(
                codigoField.getText(),
                nombresField.getText(),
                apellidosField.getText(),
                telefonoField.getText(),
                correoField.getText(),
                direccionField.getText(),
                postalField.getText()
            );
            
            linearTable.put(cliente);
            bstTable.put(cliente);
            
            outputArea.append("Cliente insertado:\n" + cliente + "\n");
            outputArea.append("Tiempo inserción lineal: " + linearTable.getInsertionTime() + " ns\n");
            outputArea.append("Tiempo inserción BST: " + bstTable.getInsertionTime() + " ns\n\n");
        });
        
        buscarBtn.addActionListener(e -> {
            String nombres = nombresField.getText();
            String apellidos = apellidosField.getText();
            
            Cliente clienteLineal = linearTable.get(nombres, apellidos);
            Cliente clienteBST = bstTable.get(nombres, apellidos);
            
            outputArea.append("Búsqueda para: " + nombres + " " + apellidos + "\n");
            outputArea.append("Resultado lineal: " + (clienteLineal != null ? clienteLineal : "No encontrado") + "\n");
            outputArea.append("Tiempo búsqueda lineal: " + linearTable.getSearchTime() + " ns\n");
            outputArea.append("Resultado BST: " + (clienteBST != null ? clienteBST : "No encontrado") + "\n");
            outputArea.append("Tiempo búsqueda BST: " + bstTable.getSearchTime() + " ns\n\n");
        });
        
        compararBtn.addActionListener(e -> {
            outputArea.append("\n--- COMPARACIÓN DE MÉTODOS ---\n");
            outputArea.append("Reasignación Lineal:\n");
            outputArea.append("  - Mejor caso: O(1)\n");
            outputArea.append("  - Peor caso: O(n)\n");
            outputArea.append("  - Tiempo inserción promedio: " + linearTable.getInsertionTime() + " ns\n");
            outputArea.append("  - Tiempo búsqueda promedio: " + linearTable.getSearchTime() + " ns\n\n");
            
            outputArea.append("Encadenamiento con BST:\n");
            outputArea.append("  - Mejor caso: O(1)\n");
            outputArea.append("  - Caso promedio: O(log k) (k = elementos en cubeta)\n");
            outputArea.append("  - Peor caso: O(k)\n");
            outputArea.append("  - Tiempo inserción promedio: " + bstTable.getInsertionTime() + " ns\n");
            outputArea.append("  - Tiempo búsqueda promedio: " + bstTable.getSearchTime() + " ns\n\n");
        });
        
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Lab7_ADA());
    }
}