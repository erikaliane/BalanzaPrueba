package Operaciones;


import java.lang.*;
import java.io.*;
import java.util.*;
import javax.comm.*;	//Se agrega el API de java de comunicaciones.
 
/**
	Este programa corresponde al que configura la puerta serial. Para ello, utiliza los métodos definidos en 
	el API de java de comunicaciones. El sistema de funcionamiento es el siguiente:
		En un objeto se obtienen todos los puertos que hay en el sistema. Luego se va revisando la lista de
		puertos hasta encontrar un puerto serial. Una vez encontrado se pregunta si es el COM1, y si corresponde
		entonces se inicializa el puerto. 
		Para ello se crea un objeto de tipo SerialPort, el cual es abierto utilizando otro objeto de tipo 
		CommPortIdentifier, que posee el metodo para abrir puertos. 
		Luego se obtienen los buffer de entradas y salidas para la escritura y lectura de los datos, para ello 
		se utilizan objetos de tipo InputStream y OutputStream.
		Se asocia un listener al puerto serial, para incorporar un receptor de eventos para estar informados 
		de lo que suceda en el puerto.
		SE implementa un thread que es el que se encarga de que la aplicación se quede esperando en el puerto 
		que se haya abierto a que se reciban datos.
	Ademas, en esta clase, se obtiene el comando a enviar por la puerta serial el cual es escrito en el buffer
	de salida y luego se espera por una respuesta.
	Fianlemnte, se cuanta con un metodo para cerrar el puerto, que en este caso, solo basta con cerrar los 
	flujos de entrada y salidas y de esta manera no se puede leer ni escribir en el puerto serial.
*/
 
public class puertoSerial implements Runnable, SerialPortEventListener {
    static Enumeration portList;
    static CommPortIdentifier portId;  //este sirve para abrir el puerto.
    static SerialPort serialPort;
 
	 static ArrayList comando = new ArrayList();
 
	 static OutputStream outputStream;
	 static InputStream inputStream;
    static Thread readThread;
 
	 static puertoSerial puerto;
 
	 static final String PingMsg = "Fpoÿ";
	 static String Mensaje;
 
	/**
	* Constructor de la puerta serial. Se encarga de inicializar el puerto serial.
	*/
	 public puertoSerial() {
        try {
		  		//Si el puerto no está en uso, se intenta abrir
             serialPort = (SerialPort)portId.open("PuertoSerie",2000);
        } catch (PortInUseException e) { }
        try {
     			//Se Configuran los buffer de entrada y salida.        
				 inputStream = serialPort.getInputStream();
             outputStream = serialPort.getOutputStream();
        } catch (IOException e) { }
		  try {
    			//listener asociado a la puerta.        
				serialPort.addEventListener(this);
        } catch (TooManyListenersException e) {}
 
		  // Hacemos que se nos notifique cuando haya datos disponibles 
    	  // para lectura en el buffer de entrada.
		  serialPort.notifyOnDataAvailable(true);
 
        try {
    			//Se configuran los parametros de transmision        
				serialPort.setSerialPortParams(38400,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);
        } catch (UnsupportedCommOperationException e) {}
 
		  // Se crea y lanza el thread que se va a encargar de quedarse 
        // esperando en el puerto a que haya datos disponibles
		  readThread = new Thread(this);
        readThread.start();
    }
 
	 /**
	 * Metodo que permite a los threads no quedarse continuamente bloqueados,
	 * sirve comoun metodo de escape. En este caso, la comprobación de si hay datos
    * o no disponibles en el buffer de la puerta, se hace
    * intermitentemente
	 */
    public void run() {
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {}
    }
 
	 /**
	 * Metodo inicializacion, en este metodo se obtiene un puerto serial correspondiente al COM1 (para el caso de windows
	 * en el caso de UNIX, se debe buscar el puerto /dev/term/a).
	 */
    public static void InitPort() {
		  portList = CommPortIdentifier.getPortIdentifiers(); //lista que posee todos los puertos del sistema
		  System.out.println(portList);
        while (portList.hasMoreElements()) {		//se recorre la lista
            portId = (CommPortIdentifier) portList.nextElement();	
            //se obtiene un elemento de la lista
            System.out.println(portId);
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {	
            	//Se pregunta si corresponde a un puerto serial
                if (portId.getName().equals("COM1")) {	//Pregunta si corresponde al COM1
                   puerto = new puertoSerial();		
                   //Se inicializa el puerto
                  System.out.println(puerto.inputStream); 
		  }
            }else{
            	System.out.println("No corresponde a un puerto");
            }
        }
 }
 
	 /**
	 * Este metodo es el encargado de cerrar los flujos de entrada y salida, para cerrar el puerto serial
	 */
	 public static void ClosePort() {
	 	try {
			inputStream.close();
			outputStream.close();
		} catch (IOException e) { }
	}
 
	 /**
	 *	Metodo utilizado para inicializar la variable comando, que es la que posee la informacion para 
	 * el comando que se desee enviar.
	 */
	 public static void init() {
		int tamano = comando.size();
 
		for (int i = 0; i < tamano; i++) {
			comando.remove(0);
	}
    }

	@Override
	public void serialEvent(SerialPortEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
