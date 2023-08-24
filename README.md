# Extension_Manager_GUI
Gestión masiva de extensiones de archivos con interfaz gráfica

Características:<br>
-Modifica (o elimina) la extensión de todos los archivos que tengan una extensión concreta por otra.<br>
-Añade una extensión a todos los archivos que no dispongan de extensión.<br>
-Permite realizar una copia de los archivos con la nueva extensión, manteniendo igualmente los archivos originales.

USO:
1. Ejecuta la aplicación desde la carpeta donde se quieren realizar los cambios. La aplicación sólo modificará los archivos de dicha carpeta.
2. Seleccione la opción deseada y siga los pasos indicados por la aplicación

ADVERTENCIAS:
1. Se aconseja no modificar el nombre del ejecutable ("ExtensionManagerGUI.jar").
2. Durante la ejecución de la aplicación se ha de evitar manipular cualquier archivo que se encuentre en la misma carpeta que el ejecutable para evitar errores indeseados.

FAQs
1. ¿Qué ocurre si ya existe un archivo con la extensión especificada?
   En este caso, no se realizará ningún cambio sobre el archivo original a fin de evitar posibles errores del usuario.
   
2. La aplicación ha generado una carpeta denominada "extensionmanagerguiLog", ¿para qué sirve? ¿Es seguro eliminarlo?
   Dentro de dicha carpeta se localiza un archivo .log que contiene un histórico del funcionamiento de la aplicación, además de los errores que haya podido surgir durante su funcionamiento. Se puede eliminar sin problema si el usuario no desea su consulta.
