import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    private static final String NombreArchivo = "OK_File.txt";
    private static int numSHAs;

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Uso: java Main <numSHAs>");
            return;
        }

        try {
            numSHAs = Integer.parseInt(args[0]);
            if(numSHAs < 2){
                System.out.println("Error: El numero de SHAs debe ser mayor o igual a 2.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: El numero de SHAs debe ser un entero valido.");
            return;
        }

        MonitorOK monitor = new MonitorOK();

        Thread[] shas = new Thread[numSHAs];
        for (int i = 0; i < numSHAs; i++) {
            shas[i] = new Thread(new SHA(i, monitor));
            shas[i].start();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(NombreArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.equals("eot-ok")) {
                    monitor.setTerminado();
                    break;
                }
                monitor.asignarLinea(linea);
            }
        } catch (IOException e) {
            System.out.println("Error: No se ha encontrado el archivo OK_File.txt");
            System.exit(1);
        }


    }
}

class MonitorOK {
    private String linea;
    private boolean terminado;

    public MonitorOK() {
    }

    public synchronized void asignarLinea(String linea) {
        while (this.linea != null && !terminado) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Error encontrado");
                return;
            }
        }
        this.linea = linea;
        notifyAll();
    }

    public synchronized String procesarLinea() {
        while (this.linea == null && !terminado) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Error encontrado");
                return "";
            }
        }
        String lineaActual = linea;
        linea = null;
        notifyAll();
        return lineaActual;
    }

    public synchronized void setTerminado() {
        this.terminado = true;
        notifyAll();
    }
}

class SHA implements Runnable {
    private MonitorOK monitor;
    private int shaID;

    public SHA(int shaID, MonitorOK monitor) {
        this.shaID = shaID;
        this.monitor = monitor;
    }

    @Override
    public void run() {
        while (true) {
            String linea = monitor.procesarLinea();
            if (linea == null) {
                break;
            }
            System.out.println("[" + shaID + "] procesando linea: " + linea);
            try {
                Thread.sleep((int) (Math.floor(shaID * Math.random() + 1) * 1000)); // Multiplicamos por 1000, ya que Thread.sleep() tiene unidades de ms.
            } catch (InterruptedException e) {
                System.out.println("Error encontrado");
                return;
            }
            System.out.println("[" + shaID + "] finalizo linea: " + linea);
        }
    }
}