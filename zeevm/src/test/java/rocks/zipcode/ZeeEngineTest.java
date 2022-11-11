package rocks.zipcode;

import static org.junit.jupiter.api.Assertions.*;

class ZeeEngineTest {

    @org.junit.jupiter.api.Test
    void interpret0() {
        String input = "start\npush #2\npush #4\npush #2\nsubtract\nadd\nprint\nhalt\n";
        String expected = "4";

        ZeeEngine testEngine = new ZeeEngine();
        String actual = testEngine.interpret(input);
        assertEquals(actual, expected);
    }


    @org.junit.jupiter.api.Test
    void interpret1() {
        String input = "START\nPUSH #2\nPUSH #4\nADD\nPRINT\nHALT\n";
        String expected = "6";

        ZeeEngine testEngine = new ZeeEngine();
        String actual = testEngine.interpret(input);
        assertEquals(actual, expected);
    }

    @org.junit.jupiter.api.Test
    void interpret2() {
        String input = "START\nPUSH #2\nPUSH #2\nSUBTRACT\nPRINT\nHALT\n";
        String expected = "0";

        ZeeEngine testEngine = new ZeeEngine();
        String actual = testEngine.interpret(input);
        assertEquals(actual, expected);
    }

    @org.junit.jupiter.api.Test
    void interpret3() {
        String input = "START\nPUSH #2\nPUSH #2\nADD\nPUSH #2\nADD\nPUSH #2\nADD\nPRINT\nHALT\n";
        String expected = "8";

        ZeeEngine testEngine = new ZeeEngine();
        String actual = testEngine.interpret(input);
        assertEquals(actual, expected);
    }

    @org.junit.jupiter.api.Test
    void interpret4() {
        String input = "START\nPUSH #2\nPUSH #2\nADD\nPUSH #2\nADD\nPUSH #2\nADD\nPRINT\nHALT\n";
        String expected = "8";

        ZeeEngine testEngine = new ZeeEngine();
        String actual = testEngine.interpret(input);
        assertEquals(actual, expected);
    }

    @org.junit.jupiter.api.Test
    void interpretMul1() {
        String input = "START\nPUSH #2\nPUSH #2\nMULTIPLY\nPRINT\nHALT\n";
        String expected = "4";

        ZeeEngine testEngine = new ZeeEngine();
        String actual = testEngine.interpret(input);
        assertEquals(actual, expected);
    }

    @org.junit.jupiter.api.Test
    void interpretMul2() {
        String input = "START\nPUSH #2\nPUSH #3\nMULTIPLY\nPRINT\nHALT\n";
        String expected = "6";

        ZeeEngine testEngine = new ZeeEngine();
        String actual = testEngine.interpret(input);
        assertEquals(actual, expected);
    }

    @org.junit.jupiter.api.Test
    void interpretMul3() {
        String input = "START\nPUSH #2\nPUSH #0\nMULTIPLY\nPRINT\nHALT\n";
        String expected = "0";

        ZeeEngine testEngine = new ZeeEngine();
        String actual = testEngine.interpret(input);
        assertEquals(actual, expected);
    }

    @org.junit.jupiter.api.Test
    void interpretMul4() {
        String input = "START\nPUSH #2\nPUSH #1\nMULTIPLY\nPRINT\nHALT\n";
        String expected = "2";

        ZeeEngine testEngine = new ZeeEngine();
        String actual = testEngine.interpret(input);
        assertEquals(actual, expected);
    }

    @org.junit.jupiter.api.Test
    void interpretDiv1() {
        String input = "START\nPUSH #8\nPUSH #2\nDIVIDE\nPRINT\nHALT\n";
        String expected = "4";

        ZeeEngine testEngine = new ZeeEngine();
        String actual = testEngine.interpret(input);
        assertEquals(actual, expected);
    }

    @org.junit.jupiter.api.Test
    void interpretMod1() {
        String input = "START\nPUSH #17\nPUSH #2\nMOD\nPRINT\nHALT\n";
        String expected = "1";

        ZeeEngine testEngine = new ZeeEngine();
        String actual = testEngine.interpret(input);
        assertEquals(actual, expected);
    }

    @org.junit.jupiter.api.Test
    void interpretMod2() {
        String input = "START\nPUSH #5\nPUSH #2\nMOD\nPRINT\nHALT\n";
        String expected = "1";

        ZeeEngine testEngine = new ZeeEngine();
        String actual = testEngine.interpret(input);
        assertEquals(actual, expected);
    }

    @org.junit.jupiter.api.Test
    void interpretMod3() {
        String input = "START\nPUSH #4\nPUSH #2\nMOD\nPRINT\nHALT\n";
        String expected = "0";

        ZeeEngine testEngine = new ZeeEngine();
        String actual = testEngine.interpret(input);
        assertEquals(actual, expected);
    }
    
}