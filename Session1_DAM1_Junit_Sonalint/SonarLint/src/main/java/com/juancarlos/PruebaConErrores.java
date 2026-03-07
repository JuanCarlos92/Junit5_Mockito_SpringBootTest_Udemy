package com.juancarlos;

import java.io.*;

/**
 *
 * @author Juan Carlos
 */
public class PruebaConErrores {

    private static File fichero = new File("D:\\config.ini");

    //Constructores públicos
    public PruebaConErrores() {
    }

    public PruebaConErrores(String path) {
        fichero = new File(path);
    }

    //Metodo que lee un fichero
    public static void leerfichero() throws FileNotFoundException, IOException {

        //Creamos un flujo de lectura
        FileReader fr = new FileReader(fichero);
        BufferedReader bf = new BufferedReader(fr);

        //Leemos una linea
        String linea = bf.readLine();

        //Mientras qeu haya lineas (que no sea nula) seguimos leyendo
        while (linea != null) {
            System.out.println(linea);
            linea = bf.readLine();
        }
        String a = new String("Hola");
        if (linea == null) {
            if (a == null) {
                fr.close();
            }
        }
    }
    //Metodo para escribir en un fichero

    public static void escribirfichero(String cadena) throws IOException {
        FileWriter fw = new FileWriter(fichero);
        fw.write(cadena);
        fw.close();

    }
}
