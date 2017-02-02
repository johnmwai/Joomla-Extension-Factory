
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.StringWriter;



public class Scrap {
public static void main(String[] args) {
//    StringReader r = new StringReader("This is just a String To convert into a stream");
//    final StringWriter w = new StringWriter();
//    BufferedReader br = new BufferedReader(r);
//    final BufferedWriter bw = new BufferedWriter(w);
//    w.getBuffer().length();
//    
//    OutputStream os = new OutputStream() {
//
//        @Override
//        public void write(int b) throws IOException {
//           w.write(b);
//        }
//    };
//    w.getBuffer().length();
    new Scrap().foo();
    
}

private void  foo(){
    boolean b = "123ms wai".matches("^\\d.*$");
    System.out.println(b);
}
    
}
