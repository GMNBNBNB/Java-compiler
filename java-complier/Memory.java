import java.util.*;

public class Memory {
    private static Map<String, Object> globalMemory = new HashMap<String, Object>();
    private static Stack<Map<String, Object>> localMemoryStack = new Stack<Map<String, Object>>();
    private static Map<String, Map<String, Integer>> ArrayValue = new HashMap<String, Map<String, Integer>>();
    private static Map<String, Map<String, Integer>> ReferArrayValue = new HashMap<String, Map<String, Integer>>();
    private static Map<String, Function> functionMap = new HashMap<String, Function>();
    private static Map<String, Object> functionParameter = new HashMap<String, Object>();
    private static Map<String, String> references = new HashMap<String, String>();
    private static Set<String> relatedIds = new HashSet<String>();
    private static Set<String> declareNews = new HashSet<String>();
    private static int garbageCollection = 0;
    private static int functionGarbageCollection = 0;
    private static boolean functionGc;


    public static void initialize() {
        globalMemory.clear();
        localMemoryStack.clear();
        references.clear();
        relatedIds.clear();
        declareNews.clear();
    }

    public static void FunctionGc(boolean selection) {
        functionGc = selection;
    }
    
    public static void GarbageCollection() {
        if (functionGc) {
            functionGarbageCollection++;
            System.out.println("gc:" + (functionGarbageCollection + garbageCollection));
        } else {
            garbageCollection++;
            System.out.println("gc:" + (functionGarbageCollection + garbageCollection));
        }
    }

    public static void CleargarbageCollection() {
        if (functionGc) {
            while (functionGarbageCollection > 0) {
                functionGarbageCollection--;
                System.out.println("gc:" + (functionGarbageCollection + garbageCollection));
            }
        } else {
            while (garbageCollection > 0) {
                garbageCollection--;
                System.out.println("gc:" + (functionGarbageCollection + garbageCollection));
            }
        }
    }

    public static int GetValueFromArray(String ArrayName, String id) {
        if (ReferArrayValue.containsKey(ArrayName)) {
            return ReferArrayValue.get(ArrayName).get(id);
        } else {
            return ArrayValue.get(ArrayName).get(id);
        }
    }

    public static void AssginArrayValue(String ArrayName, String id, int Value) {
        Map<String, Integer> Temp = new HashMap<String, Integer>();
        Temp.put(id, Value);
        ArrayValue.put(ArrayName, Temp);
    }

    // Set function parameter value in the map
    public static void SetFunctionParameterValue(String parammter, String id, Object value) {
        functionParameter.put(parammter, value);
        if (ArrayValue.containsKey(id)) {
            ReferArrayValue.put(parammter, ArrayValue.get(id));
        }
    }

    // Get function parameter value from the map
    public static Object GetFunctionParameterValue(String parammter) {      
        return functionParameter.get(parammter);
    }

    // Set function into memory map
    public static void SetInFunctionMap(String funName, Function fun) {
        if (functionMap.containsKey(funName)) {
            System.out.println("ERROR: " + funName + " is already declared");
        } else {
            functionMap.put(funName, fun);
        }

    }

    // return funtion from memory map
    public static Function GetFunctionByName(String funName) {
        if (!functionMap.containsKey(funName)) {
            System.out.println("ERROR: " + funName + " is not declared");
            System.exit(0);
        }
        return functionMap.get(funName);
    }

    // declares a Integer
    public static void declareInteger(String id) {
        if (localMemoryStack.isEmpty()) {
            globalMemory.put(id, 0);
        } else {
            localMemoryStack.peek().put(id, 0);
        }
    }

    public static boolean declareNew(String id, int type) {
        if (type == 1) {
            declareNews.add(id);
        }
        if (declareNews.contains(id)) {
            return true;
        } else {
            return false;
        }
    }

    // declares a Object
    public static void declareObject(String id) {
        if (localMemoryStack.isEmpty()) {
            globalMemory.put(id, "");
        } else {
            localMemoryStack.peek().put(id, "");
        }
    }

    // assign value to a integer or object
    public static void assignValue(String id, Object value0) {
        int value;
        if (value0 instanceof String) {
            value = (Integer) getValue((String) value0);
        } else {
            value = (Integer) value0;
        }
        boolean found = false;
        relatedIds.clear();
        // Check if the id is in the local memory stack
        for (int i = localMemoryStack.size() - 1; i >= 0; i--) {
            Map<String, Object> localMemory = localMemoryStack.get(i);
            if (localMemory.containsKey(id)) {
                found = true;
                break;
            }
        }

        if (functionParameter.containsKey(id)) {
            functionParameter.put(id, value);
        }

        // check if it is in local scope
        if (!found) {
            globalMemory.put(id, value);
        } else {
            localMemoryStack.peek().put(id, value);
        }

        // check reference string and set it in relatedIds array
        for (Map.Entry<String, String> entry : references.entrySet()) {
            String temp1 = entry.getValue();
            String temp2 = entry.getKey();
            if (references.containsKey(temp1)) {
                if (!relatedIds.contains(references.get(temp1))) {
                    relatedIds.add(references.get(temp1));
                }
            }
            if (temp1.equals(id) || temp2.equals(id)) {
                if (!relatedIds.contains(temp1)) {
                    relatedIds.add(temp1);
                }
                if (!relatedIds.contains(temp2)) {
                    relatedIds.add(temp2);
                }
            }
        }

        // set reference value to others
        for (String relatedId : relatedIds) {
            if (localMemoryStack.isEmpty() || !localMemoryStack.peek().containsKey(relatedId)) {
                globalMemory.put(relatedId, value);
            } else {
                localMemoryStack.peek().put(relatedId, value);
            }
        }
    }

    // return object or integer value
    public static Object getValue(String id) {
        boolean found = false;
        for (int i = localMemoryStack.size() - 1; i >= 0; i--) {
            Map<String, Object> localMemory = localMemoryStack.get(i);
            if (localMemory.containsKey(id)) {
                found = true;
                break;
            }
        }
        if (!globalMemory.containsKey(id) && !found && !functionParameter.containsKey(id)) {
            System.out.println("ERROR: " + id + " is not declared");
            System.exit(0);
        }

        if (functionParameter.containsKey(id)) {
            return functionParameter.get(id);
        }

        if (localMemoryStack.isEmpty() || !localMemoryStack.peek().containsKey(id)) {
            return globalMemory.get(id);
        } else {
            return localMemoryStack.peek().get(id);
        }
    }

    // assgin two object as reference
    public static void assignReference(String refer, String id1) {
        references.put(refer, id1);
        if (localMemoryStack.isEmpty()) {
            globalMemory.put(id1, globalMemory.get(refer));
        } else {
            localMemoryStack.peek().put(id1, localMemoryStack.peek().get(refer));
        }


        if (ArrayValue.containsKey(id1)) {
            if (functionGc && functionGarbageCollection != 0) {
                functionGarbageCollection--;
                System.out.println("gc:" + (functionGarbageCollection + garbageCollection));
            } else {
                garbageCollection--;
                System.out.println("gc:" + (functionGarbageCollection + garbageCollection));
            }

        }

        if (ArrayValue.containsKey(refer)) {
            ReferArrayValue.put(id1, ArrayValue.get(refer));
        }
    }

    // Methods to enter local memory stack
    public static void enterLocalScope() {
        localMemoryStack.push(new HashMap<String, Object>());
    }

    // Methods to exit local memory stack
    public static void exitLocalScope() {
        localMemoryStack.removeAllElements();
    }

    // use value find key in map
    public static String getKeyByValue(Map<String, String> map, String value) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
