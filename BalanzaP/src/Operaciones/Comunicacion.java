package Operaciones;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;
import java.io.*;
import java.util.*;
		 
		/**
		 * Este es un ejemplo de uso del API de Comunicaciones Java que permite la
		 * lectura de información a través de uno de los puertos serie de la
		 * máquina en que se ejecuta.
		 * de código que identifica el puerto a utilizar correspondiente
		 */
		 
public class Comunicacion implements Runnable,SerialPortEventListener {
		 
		  static CommPortIdentifier idPuerto;
		  static Enumeration listaPuertos;
		  InputStream entrada;
		  SerialPort puertoSerie;
		  Thread tLectura;
		 
		  public static int ddd=0;
		 
		  // En este ejemplo implementa un thread que es el que se encarga de
		  // que la aplicación se quede esperando en el puerto que se haya
		  // abierto a que se reciban datos.
		  // Primero abre el puerto y luego le fija los parámetros
		  public Comunicacion() {
		 
		    // Si el puerto no está en uso, se intenta abrir
		    try {
		      puertoSerie = (SerialPort)idPuerto.open( "AplLectura",9600 );
		    } catch( PortInUseException e ) {
		    //System.out.println(e);
		    }
		    // Se obtiene un canal de entrada
		    try {
		      entrada = puertoSerie.getInputStream();
		    } catch( IOException e ) {
		    //System.out.println(e);
		    }
		 
		    // Añadimos un receptor de eventos para estar informados de lo
		    // que suceda en el puerto
		    try {
		      puertoSerie.addEventListener( this );
			  } catch( TooManyListenersException e ) {
		          //System.out.println(e);
		          }
		 
		    // Hacemos que se nos notifique cuando haya datos disponibles
		    // para lectura en el buffer de la puerta
		    puertoSerie.notifyOnDataAvailable( true );
		 
		    // Se fijan los parámetros de comunicación del puerto
		    try {
		      puertoSerie.setSerialPortParams( 9600,
		        SerialPort.DATABITS_8,
		        SerialPort.STOPBITS_1,
		        SerialPort.PARITY_NONE );
		    } catch( UnsupportedCommOperationException e ) {
		        System.out.println(e);
		    }
		 
		    // Se crea y lanza el thread que se va a encargar de quedarse
		    // esperando en la puerta a que haya datos disponibles
		    tLectura = new Thread( this );
		    tLectura.start();
		    }
		 
		    @Override
		  public void run() {
		    try {
		      // En los threads, hay que procurar siempre que haya algún
		      // método de escape, para que no se queden continuamente
		      // bloqueados, en este caso, la comprobación de si hay datos
		      // o no disponibles en el buffer de la puerta, se hace
		      // intermitentemente
		        //System.out.println("Entra hilo");
		      Thread.sleep( 60000 );
		    } catch( InterruptedException e )
		    {
		    //System.out.println(e);
		    }
		    }
		 
		 
		    @Override
		  public void serialEvent( SerialPortEvent _ev ) {
		      //System.out.println("ENTRANDO");
		    switch( _ev.getEventType() ) {
		      // La mayoría de los eventos no se trata, éstos son los
		      // que se producen por cambios en las líneas de control del
		      // puerto que se está monitorizando
		      case SerialPortEvent.BI:
		      case SerialPortEvent.OE:
		      case SerialPortEvent.FE:
		      case SerialPortEvent.PE:
		      case SerialPortEvent.CD:
		      case SerialPortEvent.CTS:
		      case SerialPortEvent.DSR:
		      case SerialPortEvent.RI:
		      case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
		        break;
		      // Cuando haya datos disponibles se leen y luego se
		      // imprime lo recibido en la consola
		      case SerialPortEvent.DATA_AVAILABLE:
		      byte[] bufferLectura = new byte[20];
		      String salida="";
		        try {
		            int nBytes=0;
		          while( entrada.available() > 0 ) {
		            nBytes = entrada.read( bufferLectura );
		            }
		            System.out.println( new String(bufferLectura) );
		          
		        } catch( IOException e ) {
		            //System.out.println(e);
		        }
		        break;
		      }
		    }
		 
		    public String quitaEspacios(String cad){
		      char []vec=cad.toCharArray();
		      String salida="";
		      for(int i=0;i<vec.length;i++)
		          if(vec[i]=='0' || vec[i]=='1' || vec[i]=='2' || vec[i]=='3' || vec[i]=='4' || vec[i]=='5' || vec[i]=='6' || vec[i]=='7' || vec[i]=='8' || vec[i]=='9' || vec[i]=='.' ){
		                salida=salida+String.valueOf(vec[i]);
		                 }
		      int cal=Integer.valueOf(salida);
		      String salida2=String.valueOf(cal);
		      Comunicacion.ddd=1;
		      return salida2;
		    }
		 
		    public static void comun(){
		      // Lista de los puertos disponibles en la máquina. Se carga en el
		    // mimo momento en que se inicia la JVM de Java
		    listaPuertos = CommPortIdentifier.getPortIdentifiers();
		 
		    while( listaPuertos.hasMoreElements() ) {
		      idPuerto = (CommPortIdentifier)listaPuertos.nextElement();
		      if( idPuerto.getPortType() == CommPortIdentifier.PORT_SERIAL ) {
		        if( idPuerto.getName().equals("COM3") ) {  //  Lector del puerto, se quedará esperando a que llegue algo al puerto
		          Comunicacion lector = new Comunicacion();
		          }
		        }
		      }
		  }
		 
}

	


