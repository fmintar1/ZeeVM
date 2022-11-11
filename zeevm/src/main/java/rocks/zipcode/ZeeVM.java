package rocks.zipcode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * The ZeeVM - a Zee virtual machine
 * reads in an "vm file of code" and produces an integer result.
 * this is a very simple stack machine.
 *
 */
public class ZeeVM 
{
    public static void main( String[] args ) 
    {
        ZeeEngine engine = new ZeeEngine();

        String inputSource = ZeeVM.processInput("./testinput.zvm");
        String result = engine.interpret(inputSource);
        System.out.println(result);
}

    public static String processInput(String filename) {

        Scanner scanner;
        try {
            scanner = new Scanner(new File(filename));
            StringBuilder input = new StringBuilder();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim().toLowerCase();
                if (line.isEmpty()) continue;
                // if line is comment, drop it.
                if (line.startsWith(";;")) continue; // handle two kinds of
                if (line.startsWith("//")) continue; // comments...
                input.append(line + "\n");
            }
            scanner.close();
            return input.toString();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
