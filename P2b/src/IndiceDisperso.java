import java.io.IOException;
import java.io.RandomAccessFile;

public class IndiceDisperso implements Constants {
	
    public RegIndice registro = null;
	public RandomAccessFile raf = null;
    
    public IndiceDisperso (RandomAccessFile index) {

		raf = index;
		registro = new RegIndice();
	}

	 /*-----------------------------------------------------------------
     / busqueda
     /-----------------------------------------------------------------*/

    public int buscarIndice(String clave) throws IOException {

        final int size = size();
        if( size < 1 ) return SIN_ASIGNAR;
        if( size == 1) return 0;

        final int claveBusq = Integer.parseInt( clave.replaceAll( "\\D+","" ) );;

        int i = 0;
        raf.seek( i );
        registro.read( raf );
        int claveRegistro = Integer.parseInt( registro.getClave().replaceAll( "\\D+","" ) );

        while( claveRegistro <= claveBusq && i < (size - 1 ) ) {

            raf.seek( ++i * registro.length() );
            registro.read( raf );
            claveRegistro = Integer.parseInt( registro.getClave().replaceAll( "\\D+","" ) );;

            /*
            System.out.println("i: " + i);
            System.out.println(claveRegistro + " " +
                    ( claveRegistro < claveBusq ? "less" : "equal or greater" ) +
                    " than " + claveBusq  );
             */
        }

        if ( i < (size - 1) ) {

            --i;
            raf.seek( i * registro.length() );
            registro.read( raf );
        }

        System.out.println( "\n\"" + clave + "\" se encuentra en el indice disperso "
                + i + ", empezando con el registro: \"" + registro.getClave() + "\"" );
        return i;
    }

     /*-----------------------------------------------------------------
     / Ligas getter, setter, y manipulacion
     /-----------------------------------------------------------------*/

	public void moverLigas( int inicio, int distancia ) throws IOException
	{

        final int size =  size();
		for( int i = inicio; i < size; i++ ) {

			raf.seek( i * registro.length() );
			registro.read( raf );
			registro.setLiga( registro.getLiga() + distancia );
			raf.seek( i * registro.length() );
			registro.write( raf );
		}
	}

    public int getLiga( int posicion ) throws IOException {

        raf.seek(posicion * registro.length());
        registro.read(raf);

        return registro.getLiga();
    }

    public void updateLiga( int posicion, int liga ) throws IOException {

        raf.seek( posicion * registro.length() );   // lee el registro
        registro.read( raf );

        registro.setLiga( liga );                   // actualiza la liga

        raf.seek( posicion * registro.length() );   // guarda el registro
        registro.write( raf );
    }


    /*-----------------------------------------------------------------
    / Insertar nuevo indice
    /-----------------------------------------------------------------*/

    public int insertarEn( int posicion, String clave ) throws IOException {

        final int size = size() - 1;

        for( int i = size; i >= posicion; i-- ) {

            raf.seek( i * registro.length() );
            registro.read( raf );

            raf.seek( (i + 1) * registro.length() );
            registro.write( raf );
        }

        raf.seek( posicion * registro.length() );
        registro.setClave( clave );
        registro.setLiga( SIN_ASIGNAR );
        registro.write( raf );

        return posicion;
    }

    /*-----------------------------------------------------------------
    / Mostrar los indices
    /-----------------------------------------------------------------*/

	public void mostrar() throws IOException {
        
		System.out.println( "Numero de entradas: " + size() );
		raf.seek( 0 );

        final int size = size();

		for( int i = 0; i < size; i++ ) {
            
			registro.read( raf );
            
			System.out.println( "( " + registro.getClave() + ", "
                                     + String.format( "%" + NUM_DIGITS + "d", registro.getLiga() )+ " )" );
		}
	}

	/*-----------------------------------------------------------------
    / Borrar los indices
    /-----------------------------------------------------------------*/

    public void erase() throws IOException {

        raf.setLength(0);
    }

    /*-----------------------------------------------------------------
    / Cantidad de indices
    /-----------------------------------------------------------------*/

    public int size() throws IOException {

        return (int) raf.length() / registro.length();
    }

    
    /*-----------------------------------------------------------------
    / cierra el archivo indice
    /-----------------------------------------------------------------*/
    
    public void cerrar() throws IOException { 
    	raf.close(); 
    }


    /*-----------------------------------------------------------------
     / Bordes
     /-----------------------------------------------------------------*/
    public int[] getBoundaries(String target) throws IOException {

        int[] vector = new int[2];

        int puntero = buscarIndice(target); //No hay limite derecho

        raf.seek( puntero * registro.length() );
        registro.read( raf );

        raf.seek( (size() - 1) * registro.length() );
        RegIndice last = new RegIndice();
        last.read( raf );

        if( registro.getClave().equals( target ) ) {

            vector[0] = registro.getLiga();


            if( last.getClave().equals( registro.getClave() ) ) {

                vector[1] = SIN_ASIGNAR;
            } else {

                raf.seek( ( (puntero + 1) * registro.length() ) );
                registro.read( raf );
                vector[1] = registro.getLiga() - 1;
            }
        } else if( registro.getClave().compareTo( target ) < 0 ) {

            vector[0] = registro.getLiga();
            if( last.getClave().equals( registro.getClave() ) ) {

                vector[1] = SIN_ASIGNAR;
            } else{

                raf.seek( (puntero + 1) * registro.length() );
                registro.read( raf );
                vector[1] = registro.getLiga() - 1;
            }
        } else { //target > 0

            vector[1] = registro.getLiga();

            String clave = registro.getClave();
            raf.seek( 0 );
            registro.read( raf );

            if( registro.getClave().compareTo( clave ) > 0 ) {

                vector[0] = SIN_ASIGNAR;
            } else {

                raf.seek( (puntero - 1) * registro.length() );
                registro.read( raf );
                vector[0] = registro.getLiga() + 1;
            }
        }

        return vector;
    }
}
