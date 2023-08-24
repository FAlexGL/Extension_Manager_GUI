
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author falexgl
 */
public class ExtensionMan {

    public final static String RUTA = System.getProperty("user.dir");
    
    private static HashMap<String, ArrayList<String>> agrupacionExtensiones = null;
    private static int archivosModificados = 0;
    private static int totalArchivos = 0;
    
    private EventosLog eventosLog = new EventosLog();
    private Logger logger = eventosLog.crearArchivoLog();

    public EventosLog getEventosLog() {
        return eventosLog;
    }

    public Logger getLogger() {
        return logger;
    }

    public static int getArchivosModificados() {
        return archivosModificados;
    }

    public static HashMap<String, ArrayList<String>> getAgrupacionExtensiones() {
        listarArchivos();
        return agrupacionExtensiones;
    }

    private static void listarArchivos() {

        File directorio = new File(RUTA);
        File[] contenidoDirectorio = directorio.listFiles();

        if (contenidoDirectorio.length <= 1) {
            System.out.println("La carpeta " + RUTA + " no contiene más archivos.");
        } else {
            agrupacionExtensiones = new HashMap<>();
            ArrayList<String> grupo;
            for (File f : contenidoDirectorio) {
                String extension = FilenameUtils.getExtension(f.getName());
                if (!agrupacionExtensiones.containsKey(extension) && f.isFile() && !f.getName().equals("ExtensionManagerGUI.jar")) {
                    grupo = new ArrayList<>();
                    grupo.add(f.getName());
                    agrupacionExtensiones.put(extension, grupo);
                    totalArchivos++;
                } else if (f.isFile() && !f.getName().equals("ExtensionManagerGUI.jar")) {
                    grupo = agrupacionExtensiones.get(extension);
                    grupo.add(f.getName());
                    agrupacionExtensiones.put(extension, grupo);
                    totalArchivos++;
                }
            }
        }
    }

    public String mostrarContenido() {
        listarArchivos();

        String resultado = "";

        resultado += "<html><H3>CONTENIDO DE LA CARPETA: " + RUTA + ": </H3>";
        resultado += "<H3>" + totalArchivos + " archivos encontrados.</H3>";

        for (Map.Entry<String, ArrayList<String>> entry : agrupacionExtensiones.entrySet()) {
            if (entry.getKey().equals("")) {
                resultado += "<p><strong>" + entry.getValue().size() + " archivos sin extensión:</strong></p>";
                resultado += mostrarArchivosMismaExtension(entry.getValue());
            } else {
                resultado += "<p><strong>" + entry.getValue().size() + " archivos con extensión \"" + entry.getKey() + "\":</strong></p>";
                resultado += mostrarArchivosMismaExtension(entry.getValue());
            }
        }

        return resultado + "</html>";
    }

    private String mostrarArchivosMismaExtension(ArrayList<String> grupoExtension) {
        String resultado = "<ul>";
        for (String archivo : grupoExtension) {
            resultado += "<li>" + archivo + "</li>";
        }
        resultado += "</ul>";
        return resultado;
    }

    public boolean modificarExtension(String extensionActual, String nuevaExtension, boolean mantenerOriginal) {
        listarArchivos();
        boolean sinError = false;
        if (!agrupacionExtensiones.containsKey(extensionActual) && !extensionActual.equals("")) {
            return false;
        } else {
            ArrayList<String> archivos = agrupacionExtensiones.get(extensionActual);
            if (mantenerOriginal) {
                mantenerArchivo(archivos, extensionActual, nuevaExtension);
            } else {
                sustituirArchivo(archivos, extensionActual, nuevaExtension);
            }
            return true;
        }
    }

    private void mantenerArchivo(ArrayList<String> archivos, String extensionActual, String nuevaExtension) {
        archivosModificados = 0;

        for (int i = 0; i < archivos.size(); i++) {

            int tamanoNombreSinExtension = archivos.get(i).length() - extensionActual.length();
            String nombreNuevo = obtenerNuevoNombre(archivos.get(i), extensionActual, tamanoNombreSinExtension, nuevaExtension);

            File archivoNuevo = new File(nombreNuevo);

            if (archivoNuevo.exists()) {
                eventosLog.registrarEvento(logger, EventosEnum.ARCHIVO_EXISTENTE, nombreNuevo);
            } else {
                try (InputStream is = new FileInputStream(archivos.get(i)); OutputStream os = new FileOutputStream(nombreNuevo)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = is.read(buffer)) > 0) {
                        os.write(buffer, 0, length);
                    }
                    archivosModificados++;
                    eventosLog.registrarEvento(logger, EventosEnum.MODIFICACION_EXTENSION, nombreNuevo);
                } catch (IOException e) {
                    eventosLog.registrarEvento(logger, EventosEnum.ERROR_IO, e.getMessage());
                }
            }
        }
    }

    private void sustituirArchivo(ArrayList<String> archivos, String extensionActual, String nuevaExtension) {
        archivosModificados = 0;

        for (int i = 0; i < archivos.size(); i++) {

            //Archivo con el antiguo nombre
            File archivoAntiguo = new File(archivos.get(i));

            //Archivo con el nombre antiguo
            int tamanoNombreSinExtension = archivos.get(i).length() - extensionActual.length();
            String nombreNuevo = obtenerNuevoNombre(archivos.get(i), extensionActual, tamanoNombreSinExtension, nuevaExtension);
            
            File archivoNuevo = new File(nombreNuevo);
            
            if (archivoNuevo.exists()) {
               eventosLog.registrarEvento(logger, EventosEnum.ARCHIVO_EXISTENTE, nombreNuevo);
            } else {
                try {
                    archivoAntiguo.renameTo(archivoNuevo);
                    archivosModificados++;
                    eventosLog.registrarEvento(logger, EventosEnum.SUSTITUCION_EXTENSION, nombreNuevo);
                } catch (Exception e) {
                    eventosLog.registrarEvento(logger, EventosEnum.ERROR_IO, e.getMessage());
                }
            }
        }
    }

    private String obtenerNuevoNombre(String nombreOriginal, String extensionActual, int inicioExtension, String nuevaExtension) {
        String nombreNuevo = "";
        if (extensionActual.equals("")) { //Si hay que añadir extensión, se adiciona un punto.
            nombreNuevo = nombreOriginal.substring(0, inicioExtension) + "." + nuevaExtension;
        } else {
            nombreNuevo = nombreOriginal.substring(0, inicioExtension) + nuevaExtension;
        }
        return nombreNuevo;
    }
}
