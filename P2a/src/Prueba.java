/******************************************************************
 /  clase: Prueba
 /
 /  autor: Dr. JosŽ Luis Zechinelli Martini
 /******************************************************************/

import java.io.*;

public class Prueba implements Constants {
    
    /*-----------------------------------------------------------------
     / mŽtodos de prueba
     /-----------------------------------------------------------------*/
    
	private static void crear() throws IOException {
        
        // metadatos del archivo de datos y del archivo ’ndice
        File datos = new File( "Archivo.Datos" );
        File denso = new File( "Indice.Denso" );
            
        // handlers para manipular el contenido de los archivos
		RandomAccessFile archivoRaF = new RandomAccessFile( datos, "rw" );
		RandomAccessFile indiceRaF = new RandomAccessFile( denso, "rw" );
            
        // archivo indexado usando una clave de bœsqueda de 20 bytes
		Archivo archivo = new Archivo( archivoRaF, indiceRaF );
		Registro registro;
                    
        for( int num = 1, i = 1; i <= MAX_SUC; i++ ) {
            for( int j = 1; j <= MAX_CLNT; j++ ) {
                        
                String suc = "Sucursal " + String.format( "%" + SUC_DIGITS +  "d", i );
                String nom = "Cliente " + String.format( "%" + CLNT_DIGITS + "d", j );
                     
                double sal = Math.random() * (SAL_MAX - SAL_MIN) + SAL_MIN;

                archivo.insertar( new Registro( suc, num++, nom, sal ) );
                     
            }
        }
            
        /*
        for( int i = 0; i < 2; i ++ )
            archivo.borrar( "Sucursal   8" );
            
        for( int i = 0; i < 2; i ++ )
            archivo.borrar( "Sucursal   6" );
        */

		archivo.cerrar();
            
	}
    
	private static void mostrar() throws IOException {
        
        // metadatos del archivo de datos y del archivo ’ndice
        File datos = new File( "Archivo.Datos" );
        File denso = new File( "Indice.Denso" );
            
        // handlers para manipular el contenido de los archivos
		RandomAccessFile archivoRaF = new RandomAccessFile( datos, "rw" );
		RandomAccessFile indiceRaF = new RandomAccessFile( denso, "rw" );
            
        // archivo indexado usando una clave de bœsqueda de 20 bytes
		Archivo archivo = new Archivo( archivoRaF, indiceRaF );
            
        // imprime los registros del indice y los del archivo
        archivo.mostrar();

        //archivo.mostrar( "Sucursal   1" );
        archivo.cerrar();        
	}
    
    /*-----------------------------------------------------------------
    / mŽtodo principal
    /-----------------------------------------------------------------*/
    
	public static void main( String[] args ) throws Exception {
        double initTime = System.nanoTime();      

		crear();

        double endTime = System.nanoTime();
        double time = (endTime - initTime) / TIME_UNIT;

		mostrar();

        System.out.println( "\nTime: " + time );
	}
}
