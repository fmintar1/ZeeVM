package rocks.zipcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ZeeVM Ops v1.2 
 * 
 * Each "instruction" (zee operation) has an execute method
 * which get called whenever op is found in code.
 * This uses static (class) variable for the Print Buffer to
 * communicate with outside world.
 * Think of this class/enum as the "CPU" of the ZeeVM
 */
public enum ZeeOp {
    START("start"){
        public void execute(String[] args) {
            operandStack.clear();
        }
    },
    HALT("halt"){
        public void execute(String[] args) {
        }
    },
    NOP(""){
        public void execute(String[] args) {
        }
    },
    ERR("err"){
        public void execute(String[] args) {
            String p = "ZeeVM ERROR: unknown instruction";
            ZeeOp.printBuffer = p;
        }
    },
    ADD("add"){
        public void execute(String[] args) {
            operandStack.push(operandStack.pop() + operandStack.pop());
        }
    },
    SUB("subtract"){
        public void execute(String[] args) {
            int second = operandStack.pop();
            operandStack.push(operandStack.pop() - second);
        }
    },
    PRINT("print"){
        public void execute(String[] args) {
                String p = operandStack.pop().toString();
                ZeeOp.printBuffer = p;
        }
    },
    DEBUG("debug"){
        public void execute(String[] args) {
            if (_DEBUG) System.err.println( String.join(" ", args));
        }
    },
    PUSH("push"){
        public void execute(String[] args) {
            // in this Op, args[1] will be like "#4",
            // need to strip "# and parse to Integer"
            // the substring does the strip
            try {
                int arg = Integer.parseInt(args[1].substring(1, args[1].length()));
                operandStack.push(arg);
            } catch (Exception e) {
                System.err.printf("ZeeVM Push: Unable to parse Integr from string [%s]", args[1]);
            }
        }
    },

    DUPE("dupe"){ // duplicate the top of the stack
        public void execute(String[] args) {
            Integer i = operandStack.pop();
            operandStack.push(i);
            operandStack.push(i);
        }
    },

    LOAD("load"){
        public void execute(String[] args) {
            String varname = args[1];
            Integer val = variablemap.get(varname);
            operandStack.push(val);
        }
    },
    STORE("store"){
        public void execute(String[] args) {
            String varname = args[1];
            variablemap.put(varname, operandStack.pop());
        }
    },
    LABEL("label"){
        public void execute(String[] args) {
            //this is a NOP in execution
        }
    },
    JUMP("jump"){
        public void execute(String[] args) {
            String target = args[1];
            ZeeOp.programCounter = labelmap.get(target);
        }
    },
    JMPZ("jmpz"){
        public void execute(String[] args) {
            String target = args[1];
            Integer top = operandStack.pop();
            if (top == 0) {
                programCounter = labelmap.get(target);
            }
        }
    },

    // etc...
    // etc...
    // etc...
    // as in, Add your code here....

    ;

    abstract void execute(String[] args);

    private String name;
    private static Boolean _DEBUG = false;

    private static final Map<String,ZeeOp> ENUM_MAP;
    private static final IntegerStack operandStack;
    private static final StringIntMap labelmap;
    private static final StringIntMap variablemap;
    public static String printBuffer;
    public static Integer programCounter;

    ZeeOp (String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    // Build an immutable map of String name to enum pairs.
    // Any Map impl can be used.

    static {
        Map<String,ZeeOp> map = new ConcurrentHashMap<String, ZeeOp>();
        for (ZeeOp instance : ZeeOp.values()) {
            map.put(instance.getName().toLowerCase(),instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
        operandStack = new IntegerStack(); //
        labelmap = new StringIntMap();     // see below
        variablemap = new StringIntMap();  //
        printBuffer = "";
        programCounter = 0;
    }

    public static ZeeOp get (String name) {
        return ENUM_MAP.getOrDefault(name.toLowerCase(), ZeeOp.ERR);
    }

    public static void registerLabels(String[] source) {
        int idx = 0;
        for (String line : source) {
            String[] tokens = line.split(" ");
            if (ZeeOp.get(tokens[0]) == ZeeOp.LABEL) {
                if (_DEBUG) System.err.printf("(L: %s %d\n", tokens[1], idx);
                labelmap.put(tokens[1], idx);
            }
            idx++;
        }
    }
    public static Integer pc() {
        return programCounter;
    }
    public static void setPC(Integer n) {
        programCounter = n;
    }
    public static void initialize() {
        programCounter = 0;
        printBuffer = "";
        operandStack.clear();
        labelmap.clear();
        variablemap.clear();
    }
}

// Used inside of ZeeOp.
class IntegerStack {
    ArrayList<Integer> stack = new ArrayList<>();
    void clear() { stack.clear(); }
    void push(Integer n) {
        //System.err.println("push "+n);
        stack.add(n);
    }
    Integer pop() {
            try {
                if (stack.isEmpty()) throw new Exception("ZeeVM IntegerStack Underflow");
            } catch (Exception e) {
                System.err.println(e);
                e.printStackTrace();
            }
        int n = stack.remove(stack.size() - 1);
        //System.err.println("pop "+n);
        return n;
    }
}

/**
 * This class can be used for both the labelmap and variablemap.
 */
class StringIntMap {
    HashMap<String,Integer> map = new HashMap<>();
    public void put(String key, Integer value) {
        map.put(key, value);
    }
    public Integer get(String key) {
        Integer i = map.getOrDefault(key, 0);
        return i;
    }
    public void clear() {
        map.clear();
    }
}
