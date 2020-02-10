package View;

import Controller.Controller;
import Controller.IController;
import Model.DataTypes.*;
import Model.Expression.*;
import Model.MyException;
import Model.State.ProgramState;
import Model.Statement.*;
import Model.Type.BooleanType;
import Model.Type.IntType;
import Model.Type.ReferenceType;
import Model.Type.StringType;
import Model.Value.BooleanValue;
import Model.Value.IntValue;
import Model.Value.StringValue;
import Model.Value.Value;
import Repository.IRepository;
import Repository.Repository;
import View.Commands.Command;
import View.Commands.ExitCommand;
import View.Commands.RunExample;

import java.util.Map;

public class Interpreter {

    private TextMenu menu;

    public void interpret() throws MyException {

        IStatement example1 = new CompoundStatement( new VariableDeclarationStatement("v", new IntType()),
                new CompoundStatement(new AssignmentStatement("v", new ValueExpression(new IntValue(2))),
                        new PrintStatement(new VariableExpression("v"))));
        //int a;int b; a=2+3*5;b=a+1;Print(b)
        IStatement example2 = new CompoundStatement(new VariableDeclarationStatement("a", new IntType()),
                new CompoundStatement(new VariableDeclarationStatement("b", new IntType()), new CompoundStatement(new AssignmentStatement
                        ("a", new ArithmeticExpression(1, new ValueExpression(new IntValue(2)), new ArithmeticExpression
                                (3,new ValueExpression(new IntValue(3)),
                                        new ValueExpression(new IntValue(5))))), new CompoundStatement(new AssignmentStatement("b", new ArithmeticExpression(1,
                        new VariableExpression("a"), new ValueExpression(new IntValue(1)))),new PrintStatement(new VariableExpression("b"))))));

        //bool a; int v; a=true;(if a then v=2 else v=3);print(v)
        IStatement example3= new CompoundStatement(new VariableDeclarationStatement("a", new BooleanType()),
                new CompoundStatement(new VariableDeclarationStatement("v",
                        new IntType()), new CompoundStatement(new AssignmentStatement("a", new ValueExpression(new BooleanValue(true))),
                        new CompoundStatement(new IfStatement(new VariableExpression("a"), new AssignmentStatement("v",
                                new ValueExpression(new IntValue(2))), new AssignmentStatement("v", new ValueExpression(new IntValue(3)))),
                                new PrintStatement(new VariableExpression("v"))))));


        //String varf; varf="test.in"; openRFile(varf); int varc, readFile(varf,varc); prin(varc)
        IStatement example4 = new CompoundStatement(new VariableDeclarationStatement("varf", new StringType()), new CompoundStatement(
                new AssignmentStatement("varf", new ValueExpression(new StringValue("test.in"))), new CompoundStatement(new openRFile(
                new VariableExpression("varf")), new CompoundStatement(new VariableDeclarationStatement("varc",
                new IntType()), new CompoundStatement(new readFile(new VariableExpression("varf"), "varc"),
                new CompoundStatement(new PrintStatement(new VariableExpression("varc")),
                        new CompoundStatement(new readFile(new VariableExpression("varf"), "varc"),
                                new CompoundStatement(new PrintStatement(new VariableExpression("varc")), new
                                        closeRFile(new VariableExpression("varf"))))))))));


        //int d; d=10; int f; f=5; if d>f then print(d) else print("d smaller")
        IStatement example5=new CompoundStatement(new VariableDeclarationStatement("d", new IntType()), new CompoundStatement(
                new AssignmentStatement("d", new ValueExpression(new IntValue(10))), new CompoundStatement(new VariableDeclarationStatement(
                        "f", new IntType()), new CompoundStatement(new AssignmentStatement("f", new ValueExpression(new IntValue(5))),
                new IfStatement(new RelationalExpression(new VariableExpression("d"), new VariableExpression("f"), 6), new
                        PrintStatement(new VariableExpression("d")), new PrintStatement(new ValueExpression(new StringValue("d smaller"))))))));


        // Ref int v; new(v, 20), Ref Ref int a; new (a, v); print(v), print(a);
        Increment inc = new Increment();
        IStatement testAlloc = new CompoundStatement(new VariableDeclarationStatement("v", new ReferenceType(new IntType())),
                new CompoundStatement(new New("v", new ValueExpression(new IntValue(20)), inc),
                        new CompoundStatement(new VariableDeclarationStatement("a", new ReferenceType(
                                new ReferenceType(new IntType()))), new CompoundStatement(new New("a",
                                new VariableExpression("v"), inc), new CompoundStatement(new PrintStatement(new VariableExpression("v")),
                                new PrintStatement(new VariableExpression("a")))))));



        //Ref int v; new(v, 20); Ref Ref int a; new (a, v); print(rH(v)); print(rH(rH(a))+5)
        Increment inc7 = new Increment();
        IStatement testReadHeap = new CompoundStatement(new VariableDeclarationStatement("v", new ReferenceType(new IntType())),
                new CompoundStatement(new New("v", new ValueExpression(new IntValue(20)), inc7),
                        new CompoundStatement(new VariableDeclarationStatement("a", new ReferenceType(
                                new ReferenceType(new IntType()))), new CompoundStatement(new New("a", new VariableExpression("v"),
                                inc7), new CompoundStatement(new PrintStatement(new readHeap(new VariableExpression("v"))),
                                new PrintStatement(new ArithmeticExpression(1, new readHeap(new readHeap(new VariableExpression("a"))),
                                        new ValueExpression(new IntValue(5)))))))));

        //Ref int v; new(v, 20); print(readHeap(v)); writeHeap(v,30);print(readHeap(v)+5)
        Increment inc8  =new Increment();
        IStatement testWriteHeap = new CompoundStatement(new VariableDeclarationStatement("v", new ReferenceType(new IntType())),
                new CompoundStatement(new New("v", new ValueExpression(new IntValue(20)), inc8),
                        new CompoundStatement(new PrintStatement(new readHeap(new VariableExpression("v"))), new CompoundStatement(
                                new writeHeap("v", new ValueExpression(new IntValue(30))), new PrintStatement(
                                        new ArithmeticExpression(1, new readHeap(new VariableExpression("v")), new ValueExpression(
                                                new IntValue(5))))))));


        //Ref int v; new(v, 20), Ref ref int a; new(a, v); new(v, 30); print(readHeap(readHeap(a))
        Increment inc9=new Increment();
        IStatement testGarbageCollector = new CompoundStatement(new VariableDeclarationStatement("v", new ReferenceType(new IntType())),
                new CompoundStatement(new New("v", new ValueExpression(new IntValue(20)), inc9), new CompoundStatement(
                        new VariableDeclarationStatement("a", new ReferenceType(new ReferenceType(new IntType()))),
                        new CompoundStatement(new New("a", new VariableExpression("v"), inc9), new CompoundStatement(
                                new New("v", new ValueExpression(new IntValue(30)), inc9), new PrintStatement(new readHeap(new
                                readHeap(new VariableExpression("a")))))))));


        //Ref int v; new(v, 20), Ref ref int a; new(v, 30); print(readHeap(readHeap(a))
        Increment inc10=new Increment();
        IStatement testEraseGarbageCollector = new CompoundStatement(new VariableDeclarationStatement("v", new ReferenceType(new IntType())),
                new CompoundStatement(new New("v", new ValueExpression(new IntValue(20)), inc10), new CompoundStatement(
                        new VariableDeclarationStatement("a", new ReferenceType(new ReferenceType(new IntType()))),
                        new CompoundStatement(new New("v", new ValueExpression(new IntValue(30)), inc10), new PrintStatement(new readHeap(new
                                readHeap(new VariableExpression("a"))))))));

        //int v; v=4; (while (v>0) print(v); v=v-1);print(v)
        IStatement testWhile = new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),new CompoundStatement(
                new AssignmentStatement("v", new ValueExpression(new IntValue(4))), new CompoundStatement(new While(new RelationalExpression(
                        new VariableExpression("v"), new ValueExpression(new IntValue(0)), 5), new CompoundStatement(new PrintStatement(
                                new VariableExpression("v")), new AssignmentStatement("v", new ArithmeticExpression(2, new VariableExpression("v"),
                new ValueExpression(new IntValue(1)))))), new PrintStatement(new VariableExpression("v")))));

        IStatement testWhileWrong = new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),new CompoundStatement(
                new AssignmentStatement("v", new ValueExpression(new IntValue(4))), new CompoundStatement(new While(new VariableExpression("v"),
                new CompoundStatement(new PrintStatement(new VariableExpression("v")), new AssignmentStatement("v",
                        new ArithmeticExpression(2, new VariableExpression("v"),
                new ValueExpression(new IntValue(1)))))), new PrintStatement(new VariableExpression("v")))));

        //int v; Ref int a; v=10; new(a, 22); fork(writeHeap(a, 30); v=32; print(v); print(readHeap(a))); print(v);
        //print(readHeap(a))
        Increment inc11 = new Increment();
        IStatement testFork = new CompoundStatement(new VariableDeclarationStatement("v", new IntType()), new CompoundStatement(
                new VariableDeclarationStatement("a", new ReferenceType(new IntType())), new CompoundStatement(new AssignmentStatement("v",
                new ValueExpression(new IntValue(10))), new CompoundStatement(new New("a", new ValueExpression(new IntValue(22)),
                inc11), new CompoundStatement(new fork(new CompoundStatement(new writeHeap("a", new ValueExpression(new IntValue(30))), new
                CompoundStatement(new AssignmentStatement("v", new ValueExpression(new IntValue(32))), new CompoundStatement(
                        new PrintStatement(new VariableExpression("v")), new PrintStatement(new readHeap(new VariableExpression("a"))))))),
                new CompoundStatement(new PrintStatement(new VariableExpression("a")), new PrintStatement(new readHeap(new VariableExpression("a")))))))));


        //int v; v = 2;
        // fork(
        //   v=3; fork(v=4;print(v);)
        //   print(v))
        // fork (v=5; print(v);)
        //print(v);

        Increment inc12 = new Increment();
        IStatement testForkAgain = new CompoundStatement(new VariableDeclarationStatement("v", new IntType()), new
                CompoundStatement(new AssignmentStatement("v", new ValueExpression(new IntValue(2))),
                new CompoundStatement(new fork(new CompoundStatement(new AssignmentStatement("v", new ValueExpression(new IntValue(3))),
                        new CompoundStatement(new fork(new CompoundStatement(new AssignmentStatement("v", new ValueExpression(new IntValue(4))),
                                new PrintStatement(new VariableExpression("v")))), new PrintStatement(new VariableExpression("v"))))),
                        new CompoundStatement(new fork(new CompoundStatement(new AssignmentStatement("v", new ValueExpression(new IntValue(5))),
                                new PrintStatement(new VariableExpression("v")))), new PrintStatement(new VariableExpression("v"))))));

        IStatement testTypeCheck = new CompoundStatement(new VariableDeclarationStatement("varf",new StringType()),
                new CompoundStatement(new AssignmentStatement("varf",new ValueExpression(new IntValue(4))),
                        new CompoundStatement(new VariableDeclarationStatement("varc",new IntType()),
                                new CompoundStatement(new openRFile(new VariableExpression("varf")),new openRFile(new VariableExpression("varf"))))));


        //int v; v=20; (for (v=0;v<3;v=v+1) fork( print(v); v=v+1) ); print(v*10)
        // ^      ^      ^                            ^
        IStatement testFor = new CompoundStatement(new VariableDeclarationStatement("v", new IntType()),
                new CompoundStatement(new AssignmentStatement("v",new ValueExpression(new IntValue(20))),
                        new CompoundStatement(new forStatement(
                                "v", new ValueExpression(new IntValue(0)),
                                new ValueExpression(new IntValue(3)),
                                new ArithmeticExpression(1, new VariableExpression("v"), new ValueExpression(new IntValue(1))),
                                        new fork(new CompoundStatement(new PrintStatement(new VariableExpression("v")), new AssignmentStatement(
                                                "v", new ArithmeticExpression(1, new VariableExpression("v"), new ValueExpression(new IntValue(1)))
                                        )))),  new PrintStatement(new ArithmeticExpression(3, new VariableExpression("v"), new ValueExpression(new IntValue(10))))
                                )));


        //ref int a; new (a, 20); (for (v=0;v<3;v=v+1) fork(print(v)); v=v*rh(a))); print(rh(a));
        // ^              ^                  ^                  ^
        Increment inc13 = new Increment();
        IStatement testedFor =  new CompoundStatement(new VariableDeclarationStatement("a", new ReferenceType(new IntType())),
              new CompoundStatement( new New("a", new ValueExpression(new IntValue(20)), inc13), new CompoundStatement(
                      (new forStatement(
                              "v", new ValueExpression(new IntValue(0)),
                              new ValueExpression(new IntValue(3)),
                              new ArithmeticExpression(1, new VariableExpression("v"), new ValueExpression(new IntValue(1))),
                 new fork(new CompoundStatement(new PrintStatement(new VariableExpression("v")), new AssignmentStatement(
                "v", new ArithmeticExpression(3, new VariableExpression("v"), new readHeap(new VariableExpression("a"))))
        )))), new PrintStatement(new readHeap(new VariableExpression("a"))))));


        //Ref int v1; Ref int v2; int x; int q;
        // ^             ^          ^       ^
        //new (v1,20); new (v2,30); newLock(x);
         //  ^              ^            ^
        //fork(
        // ^
        //          fork(   lock(x); writeHeap(v1, readHeap(v1)-1); unlock(x)) );
        //            ^         ^                  ^
        //     lock(x); writeHeap(v1, readHeap(v1)*10); unlock(x) );
        //           ^         ^
        // newLock(q);
        //      ^
        //fork ( fork (lock(q); writeHeap(v2, readHeap(v2)+5); unlock(q) );
        //     lock(q); writeHeap(v2, readHeap(v2)*10); unlock(q) );
        // nop; nop; nop; nop;
        //  lock(x); print(readHeap(v1)); unlock(x);
        // lock (q); print(readHeap(v2)); unlock(q);

        Increment inc20 = new Increment();
        IStatement testLock =new CompoundStatement(new VariableDeclarationStatement("v1",new ReferenceType(new IntType())),
                new CompoundStatement(new VariableDeclarationStatement("v2",new ReferenceType(new IntType())),
                        new CompoundStatement(new VariableDeclarationStatement("x", new IntType()), new CompoundStatement(new VariableDeclarationStatement("q", new IntType()),
                                        new CompoundStatement(new New("v1",new ValueExpression(new IntValue(20)), inc20), new CompoundStatement(new New("v2",new ValueExpression(new IntValue(30)), inc20),
                                                        new CompoundStatement(new newLock("x"), new CompoundStatement(
                                                                new fork(new CompoundStatement(
                                                                        (new fork(new CompoundStatement(new lock("x"),
                                                                        new CompoundStatement(new writeHeap
                                                                                ("v1", new ArithmeticExpression(2, new readHeap(new VariableExpression("v1")), new ValueExpression(new IntValue(1)))),
                                                                                new unlock("x"))))),
                                                                        new CompoundStatement(new lock("x"), new CompoundStatement(new writeHeap
                                                                                ("v1", new ArithmeticExpression(3, new readHeap(new VariableExpression("v1")), new ValueExpression(new IntValue(10)))),
                                                                                new unlock("x"))))), new CompoundStatement(new newLock("q"),new CompoundStatement(
                                                                new fork(new CompoundStatement(
                                                                        (new fork(new CompoundStatement(new lock("q"),
                                                                                new CompoundStatement(new writeHeap
                                                                                        ("v2", new ArithmeticExpression(1, new readHeap(new VariableExpression("v2")), new ValueExpression(new IntValue(5)))),
                                                                                        new unlock("q"))))),
                                                                        new CompoundStatement(new lock("q"), new CompoundStatement(new writeHeap
                                                                                ("v2", new ArithmeticExpression(3, new readHeap(new VariableExpression("v2")), new ValueExpression(new IntValue(10)))),
                                                                                new unlock("q")))))
                                                                , new CompoundStatement(new nop(), new CompoundStatement(new nop(),
                                                                new CompoundStatement(new nop(), new CompoundStatement(new nop(),
                                                                        new CompoundStatement(new nop(), new CompoundStatement(new lock("x"), new CompoundStatement(
                                                                                new PrintStatement(new readHeap(new VariableExpression("v1"))), new
                                                                                CompoundStatement( new unlock("x"), new CompoundStatement(new lock("q"), new CompoundStatement(new PrintStatement(new
                                                                                readHeap(new VariableExpression("v2"))), new unlock("q")))))))))))))))))))));


        /*
        MyIStack stack1 = new MyExecutionStack<>();
        MyIDictionary dictionary1 = new MySymbolTable<>();
        MyIList list1 = new MyOut<>();
        MyIDictionary fileTable1 = new MyFileTable<>();
        MyIDictionary heap1 = new MyHeap<>();

        ProgramState program1 = new ProgramState(stack1, dictionary1, list1, fileTable1, heap1, example1, 1);
        IRepository repository1 = new Repository("examplee1.txt");
        repository1.addProgram(program1);
        IController controller1 = new Controller(repository1);





        MyIStack stack2 = new MyExecutionStack<>();
        MyIDictionary dictionary2 = new MySymbolTable<>();
        MyIList list2 = new MyOut<>();
        MyIDictionary fileTable2 = new MyFileTable<>();
        MyIDictionary heap2 = new MyHeap<>();

        ProgramState program2 = new ProgramState(stack2, dictionary2, list2, fileTable2, heap2, example2, 1);
        IRepository repository2 = new Repository("examplee2.txt");
        repository2.addProgram(program2);
        IController controller2 = new Controller(repository2);

        MyIStack stack3 = new MyExecutionStack<>();
        MyIDictionary dictionary3 = new MySymbolTable<>();
        MyIList list3 = new MyOut<>();
        MyIDictionary fileTable3 = new MyFileTable<>();
        MyIDictionary heap3 = new MyHeap<>();

        ProgramState program3 = new ProgramState(stack3, dictionary3, list3, fileTable3, heap3, example3, 1);
        IRepository repository3 = new Repository("examplee3.txt");
        repository3.addProgram(program3);
        IController controller3 = new Controller(repository3);

        MyIStack stack4 = new MyExecutionStack<>();
        MyIDictionary dictionary4 = new MySymbolTable<>();
        MyIList list4 = new MyOut<>();
        MyIDictionary fileTable4 = new MyFileTable<>();
        MyIDictionary heap4 = new MyHeap<>();

        ProgramState program4 = new ProgramState(stack4, dictionary4, list4, fileTable4,heap4,  example4, 1);
        IRepository repository4 = new Repository("examplee4.txt");
        repository4.addProgram(program4);
        IController controller4 = new Controller(repository4);

        MyIStack stack5 = new MyExecutionStack<>();
        MyIDictionary dictionary5 = new MySymbolTable<>();
        MyIList list5 = new MyOut<>();
        MyIDictionary fileTable5 = new MyFileTable<>();
        MyIDictionary heap5 = new MyHeap<>();

        ProgramState program5 = new ProgramState(stack5, dictionary5, list5, fileTable5,heap5, example5, 1);
        IRepository repository5 = new Repository("examplee5.txt");
        repository5.addProgram(program5);
        IController controller5 = new Controller(repository5);

        MyIStack stack6 = new MyExecutionStack<>();
        MyIDictionary dictionary6 = new MySymbolTable<>();
        MyIList list6 = new MyOut<>();
        MyIDictionary fileTable6 = new MyFileTable<>();
        MyIDictionary heap6 = new MyHeap<>();

        ProgramState program6 = new ProgramState(stack6, dictionary6, list6, fileTable6,heap6, testAlloc, 1);
        IRepository repository6 = new Repository("testAlloc.txt");
        repository6.addProgram(program6);
        IController controller6 = new Controller(repository6);


        MyIStack stack7 = new MyExecutionStack<>();
        MyIDictionary dictionary7 = new MySymbolTable<>();
        MyIList list7 = new MyOut<>();
        MyIDictionary fileTable7 = new MyFileTable<>();
        MyIDictionary heap7 = new MyHeap<>();

        ProgramState program7 = new ProgramState(stack7, dictionary7, list7, fileTable7,heap7, testReadHeap, 1);
        IRepository repository7 = new Repository("testReadHeap.txt");
        repository7.addProgram(program7);
        IController controller7 = new Controller(repository7);

        MyIStack stack8 = new MyExecutionStack<>();
        MyIDictionary dictionary8 = new MySymbolTable<>();
        MyIList list8 = new MyOut<>();
        MyIDictionary fileTable8 = new MyFileTable<>();
        MyIDictionary heap8 = new MyHeap<>();

        ProgramState program8 = new ProgramState(stack8, dictionary8, list8, fileTable8,heap8, testWriteHeap, 1);
        IRepository repository8 = new Repository("testWriteHeap.txt");
        repository8.addProgram(program8);
        IController controller8 = new Controller(repository8);


        MyIStack stack9 = new MyExecutionStack<>();
        MyIDictionary dictionary9 = new MySymbolTable<>();
        MyIList list9 = new MyOut<>();
        MyIDictionary fileTable9 = new MyFileTable<>();
        MyIDictionary heap9 = new MyHeap<>();

        ProgramState program9 = new ProgramState(stack9, dictionary9, list9, fileTable9, heap9, testGarbageCollector, 1);
        IRepository repository9 = new Repository("testGarbageCollector.txt");
        repository9.addProgram(program9);
        IController controller9 = new Controller(repository9);


        MyIStack stack10 = new MyExecutionStack<>();
        MyIDictionary dictionary10 = new MySymbolTable<>();
        MyIList list10 = new MyOut<>();
        MyIDictionary fileTable10 = new MyFileTable<>();
        MyIDictionary heap10 = new MyHeap<>();

        ProgramState program10 = new ProgramState(stack10, dictionary10, list10, fileTable10, heap10, testWhile, 1);
        IRepository repository10 = new Repository("testWhile.txt");
        repository10.addProgram(program10);
        IController controller10 = new Controller(repository10);

        MyIStack stack11 = new MyExecutionStack<>();
        MyIDictionary dictionary11 = new MySymbolTable<>();
        MyIList list11 = new MyOut<>();
        MyIDictionary fileTable11 = new MyFileTable<>();
        MyIDictionary heap11 = new MyHeap<>();

        ProgramState program11 = new ProgramState(stack11, dictionary11, list11, fileTable11, heap11, testEraseGarbageCollector, 1);
        IRepository repository11 = new Repository("testEraseGarbageCollector.txt");
        repository11.addProgram(program11);
        IController controller11 = new Controller(repository11);


         */
        MyIStack stack12 = new MyExecutionStack<>();
        MyIDictionary dictionary12 = new MySymbolTable<>();
        MyIList list12 = new MyOut<>();
        MyIDictionary fileTable12 = new MyFileTable<>();
        MyIDictionary heap12 = new MyHeap<>();
        MyIDictionary lockTable12 = new MyLockTable();



        ProgramState program12 = new ProgramState(stack12, dictionary12, list12, fileTable12, heap12, lockTable12, testFork, 1);
        IRepository repository12 = new Repository("testFork.txt");
        repository12.addProgram(program12);
        IController controller12 = new Controller(repository12);

        MyIStack stack13 = new MyExecutionStack<>();
        MyIDictionary dictionary13 = new MySymbolTable<>();
        MyIList list13 = new MyOut<>();
        MyIDictionary fileTable13 = new MyFileTable<>();
        MyIDictionary heap13 = new MyHeap<>();
        MyIDictionary lockTable13 = new MyLockTable();

        ProgramState program13 = new ProgramState(stack13, dictionary13, list13, fileTable13, heap13,lockTable13, testForkAgain, 1);
        IRepository repository13 = new Repository("testForkAgain.txt");
        repository13.addProgram(program13);
        IController controller13 = new Controller(repository13);

        MyIStack stack14 = new MyExecutionStack<>();
        MyIDictionary dictionary14 = new MySymbolTable<>();
        MyIList list14 = new MyOut<>();
        MyIDictionary fileTable14 = new MyFileTable<>();
        MyIDictionary heap14 = new MyHeap<>();
        MyIDictionary lockTable14 = new MyLockTable();

        ProgramState program14 = new ProgramState(stack14, dictionary14, list14, fileTable14, heap14,lockTable14, testTypeCheck, 14);
        IRepository repository14 = new Repository("example14.txt");
        repository14.addProgram(program14);
        IController controller14 = new Controller(repository14);

        MyIStack stack16 = new MyExecutionStack<>();
        MyIDictionary dictionary16 = new MySymbolTable<>();
        MyIList list16 = new MyOut<>();
        MyIDictionary fileTable16 = new MyFileTable<>();
        MyIDictionary heap16 = new MyHeap<>();
        MyIDictionary lockTable16 = new MyLockTable();

        ProgramState program16 = new ProgramState(stack16, dictionary16, list16, fileTable16, heap16,lockTable16, testedFor, 1);
        IRepository repository16 = new Repository("testedFor.txt");
        repository16.addProgram(program16);
        IController controller16 = new Controller(repository16);


        MyIStack stack20 = new MyExecutionStack<>();
        MyIDictionary dictionary20= new MySymbolTable<>();
        MyIList list20 = new MyOut<>();
        MyIDictionary fileTable20 = new MyFileTable<>();
        MyIDictionary heap20 = new MyHeap<>();
        MyIDictionary lockTable20 = new MyLockTable();

        ProgramState program20 = new ProgramState(stack20, dictionary20, list20, fileTable20, heap20,lockTable20, testLock, 1);
        IRepository repository20 = new Repository("testLock.txt");
        repository20.addProgram(program20);
        IController controller20 = new Controller(repository20);




        menu = new TextMenu();
        menu.addCommand(new ExitCommand("0", "exit"));
        /*
        menu.addCommand(new RunExample("1", example1.toString(), (Controller)controller1));
        menu.addCommand(new RunExample("2", example2.toString(), (Controller)controller2));
        menu.addCommand(new RunExample("3", example3.toString(), (Controller)controller3));
        menu.addCommand(new RunExample("4", example4.toString(), (Controller)controller4));
        menu.addCommand(new RunExample("5", example5.toString(), (Controller)controller5));
        menu.addCommand(new RunExample("6", testAlloc.toString(), (Controller)controller6));
        menu.addCommand(new RunExample("7", testReadHeap.toString(), (Controller)controller7));
        menu.addCommand(new RunExample("8", testWriteHeap.toString(), (Controller)controller8));
        menu.addCommand(new RunExample("9", testGarbageCollector.toString(), (Controller)controller9));
        menu.addCommand(new RunExample("10", testWhile.toString(), (Controller)controller10));
        menu.addCommand(new RunExample("11", testEraseGarbageCollector.toString(), (Controller)controller11));
        */
        menu.addCommand(new RunExample("12", testFork.toString(), (Controller)controller12));
        menu.addCommand(new RunExample("13", testForkAgain.toString(), (Controller)controller13));
        menu.addCommand(new RunExample("14", testTypeCheck.toString(), (Controller)controller14));
        menu.addCommand(new RunExample("16", testedFor.toString(), (Controller)controller16));
        menu.addCommand(new RunExample("20", testLock.toString(), (Controller)controller20));


    }

    public Map<String, Command> getCommands(){
       return menu.getCommands();
    }
}
