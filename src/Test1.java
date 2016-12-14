import java.io.FileInputStream;
import java.io.IOException;


public class Test1 {
        int f1=0;
        String f2=null;
        static int f3=0;
        
        public Test1()
        {
        	
        }
        
        public Test1(String s1)
        {
        	
        }
        
        public Test1(int f1, String f2)
        {
                this.f1 = f1;
                this.f2 = f2;
                for(int i = 0; i<10; i++)
                {
                        f3 >>= this.f1 + 1; 
                }
                
                for(int j = 0; j<5; j++)
                {
                        f3 <<= 2;
                }
        }


        public static int foo(int a) throws IllegalArgumentException
        {
                System.out.println("We are in foo()");
                return a + 2;
        }
        
        public void bar()
        {
                FileInputStream fis = null;
                 try{
                          fis = new FileInputStream("t1.txt"); 
                    int k; 
                    while(( k = fis.read() ) != -1) 
                    { 
                        System.out.print((char)k); 
                    } 
                    fis.close();
                    }catch(IOException e)
                    {
                          System.out.println("I/O error occurred:"+e);
                    }
        }
        public static void fun(int a) throws ArithmeticException
        {
                int x = 0;
                x = 3;
                Test1.foo(1);
                int y = 0;
                for(int i = 0; i<10; i++)
                {
                        for(int j = i; j<20; j++)
                        {
                                if(y-x < 0)
                                {
                                        y=y-x;
                                }
                                else
                                {
                                        x=x-y;
                                }
                        }
                }
                
                for(int i = 0; i<40; i++)
                {
                        y = y - 1;
                }                


                Test1 t = new Test1();
                t.bar();
                new Test1("Main").bar();
        }
}