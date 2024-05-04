package com.overon.parser.classes3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class LRTable {

    private final HashSet<String> TERMINALS;
    private final HashSet<String> NON_TERMINALS;

    private final HashMap<Integer, ArrayList<LRObject>> TABLE;
    private final HashMap<String, Integer> STATES;

    private int stateCounter;

    public LRTable(String[] terminals, String[] nonTerminals) {
        // Instanciamos las estructuras de datos
        TERMINALS = new HashSet<>();
        NON_TERMINALS = new HashSet<>();
        TABLE = new HashMap<>();
        STATES = new HashMap<>();

        // Inicializamos las variables
        stateCounter = 0;

        // Añadimos los terminales y no terminales
        for (String terminal : terminals) {
            TERMINALS.add(terminal);
        }
        for (String nonTerminal : nonTerminals) {
            NON_TERMINALS.add(nonTerminal);
        }
        // Añadimos el simbolo de fin de cadena
        TERMINALS.add("$");

    }

    public void createTable(LRObject initialSymbol, ArrayList<LRObject> allObjects) {
        // Nos pasan una lista con todos los objetos/producciones del estado inicial 0
        // y vamos añadiendo a la tabla

        // Añadimos el estado inicial
        ArrayList<LRObject> stateObjects = getAllGotoStates(initialSymbol, allObjects);

        // Añadimos el estado a la tabla
        int state = stateCounter++;
        TABLE.put(state, stateObjects);
        STATES.put(initialSymbol.toString(), state);

        // Para cada uno de los objetos de la lista, miramos su siguiente
        addState(stateObjects, allObjects);

    }

    // Esta funcion, dado un LRObjet , mira su siguiente y si es un no terminal,
    // llama de nuevo a la funcion hasta obtener todoas las producciones
    private ArrayList<LRObject> getAllGotoStates(LRObject initialSymbol, ArrayList<LRObject> allObjects) {

        ArrayList<LRObject> stateObjects = new ArrayList<>();
        stateObjects.add(initialSymbol);

        // Para cada lookahead de la lista de stateObjects que sea un no terminal
        // Añadimos al stateObjects los LRObject

        boolean added = true;

        while (added) {
            added = false;
            for (int i = 0; i < stateObjects.size(); i++) {
                LRObject lrObject = stateObjects.get(i);

                if (NON_TERMINALS.contains(lrObject.getNext()) && !lrObject.isFinal()) {
                    // Si es un no terminal
                    // Añadimos los objetos de la produccion
                    for (LRObject object : allObjects) {
                        if (object.getLeft().equals(lrObject.getNext()) && !stateObjects.contains(object)) {
                            stateObjects.add(object);
                            added = true;
                        }
                    }

                }
            }
        }

        return stateObjects;

    }

    private void addState(ArrayList<LRObject> anterior, ArrayList<LRObject> allObjects) {
        // Separamos los objetos en diferentes listas dependiendo de su siguiente
        HashMap<String, ArrayList<LRObject>> stateObjects = new HashMap<>();

        // Miramos todos los objetos de la lista y todos aquellos que tengan el
        // el siguiente como no terminal, los añadimos a la lista

        for (LRObject object : anterior) {
            if (stateObjects.containsKey(object.getNext())) {
                stateObjects.get(object.getNext()).add(object.clone());
            } else {
                ArrayList<LRObject> list = new ArrayList<>();

                // Si es un terminal, añadimos el objeto
                list.add(object.clone());

                stateObjects.put(object.getNext(), list);
            }

        }

        // Avanzamos el puntero de cada lista
        ArrayList<LRObject> newObjects = new ArrayList<>();
        for (ArrayList<LRObject> objects : stateObjects.values()) {
            for (LRObject object : objects) {
                object.getNextPointer();
                if (!object.isFinal()) {

                    newObjects.add(object);

                }
            }

        }

        // Para cada lista de objetos, llamamos a la funcion para obtener todos los
        // objetos

        // Para cada lista de objetos, creamos un nuevo estado
        for (ArrayList<LRObject> objects : stateObjects.values()) {

            if (stateCounter == 6) {
                System.out.println("Debug");
            }

            ArrayList<LRObject> derivatedObjects = new ArrayList<>(objects);
            ArrayList<LRObject> tmpList = new ArrayList<>();
            for (LRObject object : derivatedObjects) {
                if (!object.isFinal()) {
                    tmpList.addAll(getAllGotoStates(object, allObjects));
                    tmpList.removeIf(tmpObject -> derivatedObjects.contains(tmpObject));
                }
            }
            derivatedObjects.addAll(tmpList);

            // Si es un no terminal, añadimos todos los objetos de la produccion

            // Creamos un nuevo estado si no esta ya en la tabla (shift) y sino, hacemos un
            // reduce hasta el estado
            if (STATES.containsKey(derivatedObjects.toString())) {
                // Hacemos un reduce
                int state = STATES.get(derivatedObjects.toString());
                TABLE.put(state, derivatedObjects);
            } else {
                // Creamos un nuevo estado
                TABLE.put(stateCounter++, derivatedObjects);
                STATES.put(derivatedObjects.toString(), stateCounter - 1);
            }

        }

        // Si sigue habiendo objetos, volvemos a llamar a la funcion
        if (!newObjects.isEmpty()) {
            addState(newObjects, allObjects);
        }
    }

    private void printTable() {
        // Imprimimos la tabla
        for (Integer state : TABLE.keySet()) {
            System.out.println("Estado " + state);
            for (LRObject object : TABLE.get(state)) {
                System.out.println(object.toString());
            }
        }

        System.out.println("=====================\nN º Estados: " + STATES.size());
        // Imprimimos los estados
        for (String state : STATES.keySet()) {
            System.out.println(state + " -> " + STATES.get(state));
        }
    }

    public static void main(String[] args) {
        // Test usando la gramatica
        // E -> T '+' E | T.
        // T -> F .
        // F -> id .

        String[] terminals = { "+", "id" };
        String[] nonTerminals = { "E", "T", "F" };

        LRTable table = new LRTable(terminals, nonTerminals);

        // Creamos los objetos
        LRObject object1 = new LRObject("E", "T+E", "$");
        LRObject object2 = new LRObject("E", "T", "$");
        LRObject object3 = new LRObject("T", "F", "$");
        LRObject object5 = new LRObject("T", "F", "+");
        LRObject object4 = new LRObject("F", "id", "$");
        LRObject object6 = new LRObject("F", "id", "+");

        ArrayList<LRObject> allObjects = new ArrayList<>();
        allObjects.add(object1);
        allObjects.add(object2);
        allObjects.add(object3);
        allObjects.add(object4);
        allObjects.add(object5);
        allObjects.add(object6);

        table.createTable(new LRObject("", "E", "$"), allObjects);
        table.printTable();

    }

}
