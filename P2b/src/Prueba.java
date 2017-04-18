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
        
        // metadatos del archivo de datos y del archivo ⮤ice
        File datos = new File( "ArchivoDisperso.Datos" );
        File denso = new File( "Indice.Disperso" );
            
        // handlers para manipular el contenido de los archivos
        RandomAccessFile archivoRaF = new RandomAccessFile( datos, "rw" );
        RandomAccessFile indiceRaF = new RandomAccessFile( denso, "rw" );
            
        // archivo indexado usando una clave de b쳱ueda de 20 bytes
        Archivo archivo = new Archivo( archivoRaF, indiceRaF );
        Registro registro;

        //Sparse index section---------------------------------
        for( int num = 1, i = 1; i <= MAX_SUC; i++ ) {
            for( int j = 1; j <= MAX_CLNT; j++ ) {
				
                String suc = "Sucursal " + String.format( "%" + SUC_DIGITS +  "d", i );
                String nom = "Cliente " + String.format( "%" + CLNT_DIGITS + "d", j );
                     
                double sal = Math.random() * (SAL_MAX - SAL_MIN) + SAL_MIN;
                        
                archivo.insertar( new Registro( suc, num++, nom, sal ) );
            }
        }

        archivo.cerrar();
            
    }

	private static void mostrar() throws IOException {
        // metadatos del archivo de datos y del archivo indice
        File datos = new File( "ArchivoDisperso.Datos" );
        File disperso = new File( "Indice.Disperso" );

        // handlers para manipular el contenido de los archivos
        RandomAccessFile archivoRaF = new RandomAccessFile( datos, "rw" );
        RandomAccessFile indiceRaF = new RandomAccessFile( disperso, "rw" );

        // archivo indexado usando una clave de bœsqueda de 20 bytes
        Archivo archivo = new Archivo( archivoRaF, indiceRaF );

        // imprime los registros del indice y los del archivo
        archivo.mostrar();

        archivo.cerrar();
	}

    private static void busqueda( int sucursalNum ) throws IOException {
        // metadatos del archivo de datos y del archivo indice
        File datos = new File( "ArchivoDisperso.Datos" );
        File disperso = new File( "Indice.Disperso" );

        // handlers para manipular el contenido de los archivos
        RandomAccessFile archivoRaf =  new RandomAccessFile (datos, "rw");
        RandomAccessFile indiceRaf =  new RandomAccessFile (disperso, "rw");

        // archivo indexado usando una clave de bœsqueda de 20 bytes
        Archivo archivo = new Archivo( archivoRaf, indiceRaf );

        // Busqueda de un registro por sucursal
        String suc = "Sucursal " + String.format( "%" + SUC_DIGITS + "d", sucursalNum );
        archivo.busquedaLineal( suc );

        archivo.cerrar();
    }

    private static void eliminacio( int sucursalNum ) throws IOException  {
        // metadatos del archivo de datos y del archivo indice
        File datos = new File( "ArchivoDisperso.Datos" );
        File disperso = new File( "Indice.Disperso" );

        // handlers para manipular el contenido de los archivos
        RandomAccessFile archivoRaf = new RandomAccessFile( datos, "rw" );
        RandomAccessFile indiceRaf = new RandomAccessFile( disperso, "rw" );

        // archivo indexado usando una clave de bœsqueda de 20 bytes
        Archivo archivo = new Archivo( archivoRaf, indiceRaf );

        // Eliminacion de de un registro utlizanod sucursal como referencia
        String suc = "Sucursal " + String.format( "%" + SUC_DIGITS + "d", sucursalNum );
        archivo.eliminacionLineal(suc);

        archivo.cerrar();
    }

    /*-----------------------------------------------------------------
    / metodo principal
    /-----------------------------------------------------------------*/
	public static void main( String[] args ) throws Exception {

        double initTime = System.nanoTime();

        crear();

        /*
        busqueda( 1 );
        busqueda( 10 );
        busqueda( 11 );
        busqueda( 100 );
        busqueda( 1000 );
        */

        //eliminacion();

        double endTime = System.nanoTime();
        double sparseIndexTime  = (endTime - initTime) / TIME_UNIT;

        mostrar();

        System.out.println("\nTiempo Indice Disperso: " + sparseIndexTime + " segundos");

        /*
        String a = "Sucursal11";
        String b = "Sucursal100";
        int c = Integer.parseInt( a.replaceAll( "\\D+","" ) );
        int d = Integer.parseInt( a.replaceAll( "\\D+","" ) );
        int value = c > d ? 1 : c == d ? 0 : -1;
        System.out.println("value: " + value);
        System.out.println("\"" + a + "\" "  +
                ( value > 0  ? "greater than" : "equal or less than" ) +
                " \"" + b  + "\"");

        /*
        String a = "Sucursal0";
        String b = "Sucursal1";
        String c = "Sucursal2";
        System.out.println( (a.compareTo(b) < 1 ? "Less" : "Greater") + " than"); // Less than
        System.out.println( (a.compareTo(c) < 1 ? "Less" : "Greater") + " than"); // Less than
        System.out.println( (b.compareTo(a) < 1 ? "Less" : "Greater") + " than"); // Greater than
        System.out.println( (b.compareTo(c) < 1 ? "Less" : "Greater") + " than"); // Less than
        System.out.println( (c.compareTo(a) < 1 ? "Less" : "Greater") + " than"); // Greater than
        System.out.println( (c.compareTo(b) < 1 ? "Less" : "Greater") + " than"); // Greater than
         */

	}
}
