/******************************************************************
 /  clase: Prueba
 /
 /  autor: Dr. JosŽ Luis Zechinelli Martini
 /******************************************************************/

import java.io.*;

public class Prueba {
    
    /*-----------------------------------------------------------------
     / mŽtodos de prueba
     /-----------------------------------------------------------------*/
    
	private static void crear() throws Exception {
        
		try {
            // metadatos del archivo de datos y del archivo ’ndice
            File datos = new File( "Archivo.Datos" );
            File denso = new File( "Indice.Denso" );
            
            // handlers para manipular el contenido de los archivos
			RandomAccessFile archivoRaF = new RandomAccessFile( datos, "rw" );
			RandomAccessFile indiceRaF = new RandomAccessFile( denso, "rw" );
            
            // archivo indexado usando una clave de bœsqueda de 20 bytes
			Archivo archivo = new Archivo( archivoRaF, indiceRaF );
			Registro registro;
            
            /* 
            registro = new Registro( "Sucursal   3", 3, "Cliente 3", 300.0 );
            archivo.insertar( registro );
            registro = new Registro( "Sucursal   2", 2, "Cliente 2", 200.0 );
            archivo.insertar( registro );
            registro = new Registro( "Sucursal   0", 0, "Cliente 1", 100.0 );
            archivo.insertar( registro );
            registro = new Registro( "Sucursal   1", 1, "Cliente 0", 0.0 );
            archivo.insertar( registro );
            */
         
            for( int num = 1, i = 1; i <= 999; i++ ) {
                for( int j = 1; j <= 9; j++ ) {
                    //for( int k = 1; k <= 1; k++, num++ ) {
                        
                    String suc = "Sucursal " + String.format( "%3d", i );
                    String nom = "Cliente " + j;
                        
                    double salMin = 100.0, salMax = 30000.6;
                    double sal = Math.random() * (salMax - salMin) + salMin;
                        
                    archivo.insertar( new Registro( suc, num, nom, sal ) );
                    
                    num++;
                    
                    //}
                }
            }
            
            /*
            for( int i = 0; i < 2; i ++ )
                archivo.borrar( "Sucursal   8" );
            
            for( int i = 0; i < 2; i ++ )
                archivo.borrar( "Sucursal   6" );
           */

			archivo.cerrar();
            
		} catch( IOException e ) {
            
			System.out.println( "IOException:" );
			e.printStackTrace();
		}
	}
    
	private static void mostrar() {
        
		try {
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
            
		} catch( IOException e ) {
            
			System.out.println( "IOException:" );
			e.printStackTrace();
		}
	}
    
    /*-----------------------------------------------------------------
    / mŽtodo principal
    /-----------------------------------------------------------------*/
    
	public static void main( String[] args ) throws Exception{
        
		crear();
		mostrar();
	}
}
