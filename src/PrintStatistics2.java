import java.util.ArrayList;
import java.util.List;

import soot.Body;
import soot.Local;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.ValueBox;
import soot.options.Options;
import soot.toolkits.graph.LoopNestTree;
import soot.util.Chain;

//Printing statistics of a jimple file created for input file by soot

public class PrintStatistics2 {
	static SootClass sootClass;
	
	public static void main(String[] args) {
		
		if(args.length == 0) {
			System.err.println("No input java source file provided.");
			System.exit(0);
		}
		
		//Setting options before loading the input file
		//Setting option to use original variable names in soot class 
		Options.v().setPhaseOption("jb", "use-original-names:true");
		
		//Loading the input file
		sootClass = Scene.v().loadClassAndSupport(args[0]);
		sootClass.setApplicationClass();
		
		//Getting class fields
		printClassFields(sootClass);
		
		//Getting class methods
		List<SootMethod> sootMethods = sootClass.getMethods();
		
		int methodNum = 0;
		
		System.out.println("Following are the methods in the class:");
		for(SootMethod method: sootMethods) {
			
			/*if(methodNum == 0) {
				methodNum++;
				continue;
			}
			*/
			//Body body = method.retrieveActiveBody();
			//System.out.println(body);
			
			//Print the info of method
			printMethodInfo(method, methodNum);
			methodNum++;
		}
		
	}

	private static void printClassFields(SootClass sootClass) {
		
		Chain<SootField> sootField = sootClass.getFields();
		
		System.out.println("Following are the class fields:\n");
		for(SootField field : sootField) {
			System.out.println(field.getName() + " - " + field.getType());
		}
		System.out.println();
	}
	
	private static void printMethodInfo(SootMethod method, int methodNum) {
		
		System.out.println();
		System.out.println("Method " + methodNum + ":");
		System.out.println("Method Name: " + method.getName());
		System.out.println("Return Type: " + method.getReturnType());
		printParamsAndLocal(method, method.getParameterCount());
		printMethodsInvocation(method);
		printExceptions(method);
		hasLoop(method);
		hasMultiplication(method);
		System.out.println();
	}

	private static void printParamsAndLocal(SootMethod method, int paramsCount) {
		
		int i = 1, count = 0;
		Body body = method.retrieveActiveBody();
		
		ArrayList<String> params = new ArrayList<String>();
		ArrayList<Type> paramsType = new ArrayList<Type>();
		ArrayList<String> locals = new ArrayList<String>();
		ArrayList<Type> localsType = new ArrayList<Type>();
		
		//Loop through all local variables inside the soot representation of the java method
		//and find the method parameters
		for(Local local: body.getLocals()) {
			
			if(local.getName().equals("this")) {
				locals.add(local.getName());
				localsType.add(local.getType());
				++count;
			}
			else if(paramsCount == 0) {
				locals.add(local.getName());
				localsType.add(local.getType());
				++count;
			}
			else {
				params.add(local.getName());
				paramsType.add(local.getType());
				--paramsCount;
			}
		}
		
		//Loop through all local variables inside the soot representation of the java method
		//and find the method local variables
		/*for(Local local: body.getLocals()) {
			
			if(local.getName().equals("this")) {
				locals.add(local.getName());
				localsType.add(local.getType());
				++count;
			}
			else if(params.contains(local.getName())) {
				continue;
			}
			else if(local.getName().substring(0, 1).compareTo("$") != 0) {
				if(local.getType().toString().compareTo(sootClass.getName()) != 0) {
					locals.add(local.getName());
					localsType.add(local.getType());
					++count;
				}
			}
		}*/
		
		System.out.println("Number of Parameters: " + method.getParameterCount());
		//Printing method parameters
		for(int j = 0; j < params.size(); ++j) {
			System.out.println("	- Parameter " + i);
			System.out.println("		Name: " + params.get(j));
			System.out.println("		Type: " + paramsType.get(j));
			++i;
		}
		
		i = 1;
		System.out.println("Number of Local Variables: " + count);
		//Printing method local variables
		for(int j = 0; j < locals.size(); ++j) {
			System.out.println("	- Local Variable " + i);
			System.out.println("		Name: " + locals.get(j));
			System.out.println("		Type: " + localsType.get(j));
			++i;
		}
	}
	
	private static void printMethodsInvocation(SootMethod method) {
		int count = 0;
		ArrayList<String> invokeMethods = new ArrayList<String>();
		ArrayList<String> invocationType = new ArrayList<String>();
		Body body = method.retrieveActiveBody();
		
		for(ValueBox box: body.getUseBoxes()) {
			String[] ch = box.getValue().toString().split("[\\s]+");
			//System.out.println(box.getValue());
			if(ch[0].equals("virtualinvoke")) {
				invokeMethods.add(ch[3].split("[\\(]+")[0]);
				invocationType.add("Virtual Invoke");
				count++;
			}
			else if(ch[0].equals("staticinvoke")) {
				invokeMethods.add(ch[3].split("[\\(]+")[0]);
				invocationType.add("Static Invoke");
				count++;
			}
		}
		
		System.out.println("Number of invoked methods: " + count);
		
		for(int i = 0; i < count; ++i) {
			System.out.println("	- Invoked Method " + (i+1));
			System.out.println("		Name: " + invokeMethods.get(i));
			System.out.println("		Invocation Type: " + invocationType.get(i));
		}
	}
	
	private static void printExceptions(SootMethod method) {
		List<SootClass> exceptions = method.getExceptions();
		
		System.out.println("Number of exceptions thrown by the method: " + exceptions.size());
		for(SootClass exception : exceptions) {
			System.out.println("	- " + exception.getName());
		}
	}
	
	private static void hasLoop(SootMethod method) {
		Body body = method.retrieveActiveBody();
		
		LoopNestTree loopNestTree = new LoopNestTree(body);
		
		if(loopNestTree.size() > 0) {
			System.out.println("The method has a loop inside it");
		}
		else {
			System.out.println("The method does not have a loop inside it");
		}
	}
	
	private static void hasMultiplication(SootMethod method) {
		Body body = method.retrieveActiveBody();
		Boolean flag = false;
		
		for(ValueBox valueBox: body.getUseAndDefBoxes()) {
			
			if(valueBox.toString().split("[\\(]+")[0].equals("LinkedRValueBox")) {
				String[] str = valueBox.getValue().toString().split("[\\s]+");
				if(str.length > 2 && str[1].equals("*")) {
					System.out.println("Some statement(s) in the method have * (multiplication) operator");
					flag = true;
				}
			}
		}
		
		if(flag) {
			return;
		}
		else {
			System.out.println("No statement in the method has * (multiplication) operator");
		}
	}
}