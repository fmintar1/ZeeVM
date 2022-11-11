# ZeeVM v1.2
a Very simple virtual machine. Based on a stack for expression evaluation. Uses _reverse polish_ model.

Three classes: ZeeVM, ZeeEngine, and ZeeOp.

### ZeeVM

this is where `main` is. class has two responsibilies:

- read in source code, and drop all lines which are comments
  - comment lines start with ";;" or "//"
- create the ZeeEngine and start it on the modified source
- print out the machines last print buffer

### ZeeEngine

this class simply steps thru the source, interpreting each line.
it looks for Errors and Prints and performs them.
It also looks for HALTs and stops the program when that is found.

### ZeeOp

this class, well, it's where all the magic happens. I should put together an
entire 30 min slide show to discuss the mechanics.
Notice how the class uses a static for the one and only operandStack.
And how the print buffer is done the same way.

Also, notice how the ENUM uses the `execute` method for each operation to manipulate
the internal state of the "stack virtual machine". Error handling is also easy: if an unknown word ends up in the input, the ERR op will happen and the interpreter will quit.

There is also a hashmap of strings to operations, so that the string input from the source gets mapped to the operation code without a bit hairy set of IFs or a SWITCH statement.

### What do I do?

**Add some capability to this code.**

Just like in https://github.com/xt0fer/Snowman, your job is to add `multiply`, `divide` and `mod` to the VM so it can handle code generated from your modified `snowman` compiler.

Make it so that anything you add to `snowman` gets supported here in `ZeeVM`.

*Java's VM and compiler, while a **Lot More** complicated, is pretty much like these two projects.*

## ZeeVM Internal Instructions

The VM has a single stack of Integers (32 bit signed).

The instructions of the ZeeVM v1.0.

- `START` - clears the stack of the VM
- `HALT` - stops the VM
- `NOP` - does nothing
- `PRINT` - pops the top of the stack, converts the int into a string, and prints it on std out.

- `PUSH #n` - pushes the integer after the # sign onto the stack
- `ADD` - pops the top 2 integers, adds them, and pushes the sum back onto the stack
- `SUB` - pops the top 2 integers and substracts the first one from the second
  - (as in:  x<-pop(), y<-pop(), push(y-x). this is because the source was (SUBTRACT y x) )
- `DUPE` - duplicates the top of the stack
  - (as in: x<-pop), push(x), push(x). this leaves a copy of x on the top of the stack)

### Confused about #4 ?

*why are numbers to be pushed formatted like "#3" and not just "3"??*

Well, it is common in assembly languages for **immediate** values (integer literals),
to be prefix'd with some char.
That way, in the future, you could use regular literals to refer to a number register
within the CPU. So, `PUSH 5` might mean *push the contents of register 5 to the stack*.
But `PUSH #5` means *push the literal number 5 to the stack*.
You could also then make memory address literals special by prefixing an @. (as in a memory location like "@00004F7f")
When you write the labs, you can make arbitrary design choices like this. :-)

## Adding labels

`labelmap` is a hashtable <string, integer>

`LABEL name` - declares a label, using the line position in the file. store in labelmap
`JUMP name` - sets the PC to the line postion of labelmap(name)+1, execution continues from there.
`JMPZ name` - pops TOS and jumps to name if the value is zero

YOU NEED to add these to the label support.

`JMPN name` - pops TOS and jumps to name is value is not zero

the `labelmap` has to be created before the start. 
For labels to work, you have a "resolve them at runtime", meaning in the code there will be something like "JUMP ALABEL". Well, where is "ALABEL"? What line is it on?
You need to scan the code and resolve the labels.

`registerLabels()` - the source needs to be scanned for "LABEL name" lines, and the line number of the label need to be inserted into the `labelmap`.

jumping then becomes looking up the labelname and getting the line number. then the PC needs to be set so that the next instruction read and interpreted is the one right after the label.

## Adding variables

`variablemap` is a hash table <string, intger>

`STORE name` - pops from stack and store value in variablemap as <name, value>
`LOAD name` - gets variable name from variablemap and pushes it to stack

so if you wanted to do something line `(VAR X 5)` in Snowman, you might generate code that
ZeeVm would run like this:

```
        PUSH #5
        STORE X
```

and if later you wanted to `(ADD X 7)`, you would generate code that

```
        LOAD X
        PUSH #7
        ADD
```

and of course, if you wanted to do `(VAR X (ADD X 7))`, you would gen code like

```
    LOAD X
    PUSH #7
    ADD
    STORE X
```

## Some New Functionality in v1.2

Currently the ZeeVM supports LABELs and LOAD/STORE of alpha-named variables.

```
        PUSH #9
        STORE X
        ;; and
        LOAD X
        PRINT
        ;; should display 9 in output.
```

and labels and jumps like this infinite loop.

```
        LABEL FOO
        ;; bunch of code
        JUMP FOO
```

*Gee?* so how do I avoid using JUMP (which is really a GOTO, isn't it?)?

Well, there is also conditional jump called `JMPZ FOO` which pops the stack and if the value there is equal to zero, the jump happens (jumping to where FOO is declared with a LABEL). Otherwise, the next instruction after the jump is done.


```
        LABEL FOO
        ;; bunch of code
        ;; some math leaves either a 0 or something else on the top of the stack
        JMPZ BAR ;; if the top of stack is zero, jump to BAR
        ;; some more code
        JUMP FOO ;; jump to top of loop
        ;;
        LABEL BAR
```

And there are a bunch of patterns which can be used to handle FOR loops, WHILE loops, etc.
