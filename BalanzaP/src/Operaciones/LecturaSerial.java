package Operaciones;



import vista.FramePesoBalanza;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.Logger;
 
public class LecturaSerial  {
	  //inicializamos y declaramos variables
	static CommPortIdentifier portId;
	static Enumeration puertos;
	static SerialPort serialport;
	static InputStream entrada = null;
	public static Thread t;
	//creamos un constructor para realizar la conexion del puerto
 
        public static boolean hayBalanza=false;
 
  	public LecturaSerial() {
            super();
            lecturaAnterior();
        }
 
        public static void lecturaAnterior(){
            puertos=CommPortIdentifier.getPortIdentifiers();
            t = new Thread(new LeerSerial());
            while (puertos.hasMoreElements()) { //para recorrer el numero de los puertos, y especificar con cual  trabajaremos
        	portId = (CommPortIdentifier) puertos.nextElement(); //next elemento ,  recorre uno por uno
        	System.out.println("puerto "+portId.getName()); //puertos disponbibles
        	if (portId.getName().equals("COM3")) { // colocar el puerto correspondiente de hyperterminal
        		try {
                            serialport= (SerialPort)portId.open("LecturaSerial", 10);//tiempo ANTES ERA 9600
                            entrada = serialport.getInputStream();//esta variable del tipo InputStream obtiene el dato serial
                            t.start(); // inciamos el hilo para realizar nuestra acción de imprimir el dato serial
			} catch (Exception e) {
                        }
 
                }
            }
        }
 
        public static String salida="";
 
    //con este metodo del tipo thread realizamos
  	public static class LeerSerial implements Runnable {
	   static int aux;
           public static boolean entrar=true;
 
           public void run () {
            while(entrar){
    		  try {
                        aux = entrada.read(); // aqui estamos obteniendo nuestro dato serial
                        esperar(1);//PAUSA PARA VERIFICAR
 			if (aux>0) {
                            salida=salida+String.valueOf((char)aux);
                            verifica2();
                           }
			} catch (Exception e) {}
                }
           }
 
           public void verifica(){
               if(contarPuntos()>2){
                   char []dat=quitarDatos(salida).toCharArray();
                   String bas="";
                   for(int i=buscaUltim();i<dat.length-1;i++)
                       bas=bas+String.valueOf(dat[i]);
                   if(bas.toCharArray().length>4){
                	   FramePesoBalanza.textPeso.setText(bas);
                	   FramePesoBalanza.pesando(quitarDatos(salida));
                	   LecturaSerial.hayBalanza=true;
                   }
                   try {
                       Thread.sleep(4);
                   } catch (InterruptedException ex) {
                       Logger.getLogger(LecturaSerial.class.getName()).log(Level.SEVERE, null, ex);
                   }
               }
           }
 
           public static int control=0;
 
           public static void verifica2(){
                   char []dat=salida.toCharArray();
                   if(dat.length>8){
                        System.out.println("salida "+salida);
                        if(hola(salida)==true){
                            String dato=quitarDatos(salida);
                            FramePesoBalanza.textPeso.setText(dato);
                            System.out.println("salida sin puntos "+dato);
                            FramePesoBalanza.pesando(dato);
                            control=1;
                        }
                        LecturaSerial.hayBalanza=true;
                        //entrar=false;
                    if(dat.length>8){
                        salida="";
                    }
                   }
           }
 
           public static boolean hola(String cadena){
               char []vec=cadena.toCharArray();
               int con=0;
               for(int i=0;i<vec.length;i++){
                   if(vec[i]=='+'){
                       con=1;
                   }
                    if(vec[i]=='g')
                       con=2;
               }
               return con==2?true:false;
           }
 
           public static void esperar(int segundos) {
            try {
                //Thread.sleep (segundos*1000);
                Thread.sleep (segundos*10);
                } catch (Exception e) {}
            }
 
           public static String quitarDatos(String dato){
               if(dato==null) return "0";
               if(dato.equals("")) return "0";
               String salida="";
               char []vec=dato.toCharArray();
               for(int i=0;i<vec.length;i++){
                   if(vec[i]=='0' || vec[i]=='1' || vec[i]=='2' || vec[i]=='3' || vec[i]=='4' || vec[i]=='5' || vec[i]=='6' || vec[i]=='7' || vec[i]=='8' || vec[i]=='9' )
                       salida=salida+String.valueOf(vec[i]);
               }
               return salida;
           }
 
           public int contarPuntos(){
               char []dat=salida.toCharArray();
               int c=0;
               for(int i=0;i<dat.length;i++)
                   if(dat[i]=='.')
                       c++;
               if(c>3)
                   salida="";
               return c;
           }
 
           public int buscaUltim(){
               int c=0;
               char []dat=salida.toCharArray();
               for(int i=0;i<dat.length;i++)
                   if(dat[i]=='.')
                       c=i;
               return c-1;
           }
 
    }
 
}