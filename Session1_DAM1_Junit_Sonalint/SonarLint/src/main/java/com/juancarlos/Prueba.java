package com.juancarlos;

import java.io.*;

/**
 *
 * @author Juan Carlos
 */
public class Prueba {

    private static File fichero = new File("D:\\config.ini");
//Constructores públicos

    private Prueba() {
    }

    public static void setPath(String path) {
        fichero = new File(path);
    }
    //Método que lee un fichero

    public static void leerfichero() throws IOException {
        //Creamos un flujo de lectura

        try (BufferedReader bf = new BufferedReader(new FileReader(fichero))) {
            //Leemos una linea
            String linea = bf.readLine();
            //Mientras qeu haya lineas (que no sea nula) seguimos leyendo
            while (linea != null) {
                System.out.println(linea);
                linea = bf.readLine();
            }

        } catch (FileNotFoundException f) {
            System.out.println(fichero + "doesn't exists");
            System.out.println(f);
        } catch (IOException e) {
            System.out.println(e);
        }

    }

    //Metodo para escribir en un fichero
    public static void escribirfichero(String cadena) throws IOException {
        try (FileWriter fw = new FileWriter(fichero)) {
            fw.write(cadena);
        } catch (IOException e) {
            System.out.println(e);

        }

    }

}
