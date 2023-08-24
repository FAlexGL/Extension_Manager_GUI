
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author falexgl
 */
public class EventosLog {

    public final static String RUTA_LOG_FILE = System.getProperty("user.dir") + "/extensionManagerLog/extensionmanager.log";
    public final static String RUTA_LOG_DIRECTORY = System.getProperty("user.dir") + "/extensionManagerLog";

    public static Logger crearArchivoLog() {
        //Genera el objeto Logger y lo vincula al archivo que va a ser usado
        Logger logger = Logger.getLogger("MyLog");
        FileHandler fileHandler;
        File directorio = new File(RUTA_LOG_DIRECTORY);
        if(!directorio.exists()){
            directorio.mkdir();
        }
        try {
            fileHandler = new FileHandler(RUTA_LOG_FILE, true);
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false);
            logger.setLevel(Level.ALL);
            SimpleFormatter simpleFormatter = new SimpleFormatter();
            fileHandler.setFormatter(simpleFormatter);

        } catch (SecurityException e) {
            logger.info("Exception:" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            logger.info("IO Exception:" + e.getMessage());
            e.printStackTrace();
        }
        return logger;
    }

    public static void registrarEvento(Logger logger, EventosEnum evento, String argumento) {

        //Establece el mensaje del evento dependiendo del mismo.
        switch (evento) {
            case ARCHIVO_EXISTENTE:
                logger.log(Level.INFO, "INFORMACIÓN: Ya existía el archivo: " + argumento + " por lo que no se ha modificado.\n");
                break;
            case ERROR_IO:
                logger.log(Level.SEVERE, "ERROR: Error IO: " + argumento + "\n");
                break;
            case MODIFICACION_EXTENSION:
                logger.log(Level.INFO, "INFORMACIÓN: Se ha creado el archivo con la extensión: " + argumento + " .\n");
                break;
            case SUSTITUCION_EXTENSION:
                logger.log(Level.SEVERE, "INFORMACIÓN: Se ha modificado la extensión del archivo: " + argumento + " .\n");;
                break;
            case APERTURA_APP:
                logger.log(Level.INFO, LocalDateTime.now() + "INFORMACIÓN: Aplicación iniciada.\n");
                break;
            case CIERRE_APP:
                logger.log(Level.INFO, "INFORMACIÓN: Aplicación finalizada.\n\n\n\n");
                break;
        }
    }

}
