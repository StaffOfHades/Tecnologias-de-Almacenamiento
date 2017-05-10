import java.io.IOException;
import java.io.RandomAccessFile;

public class Archivo implements Constants {

	private RandomAccessFile raf = null;
	private Arbol arbol = null;

	private boolean agrupa = true;

    public Archivo(RandomAccessFile archivo, RandomAccessFile indice) {

		raf = archivo;
		arbol = new Arbol(indice);
	}

	public void insertar( Registro registro ) throws IOException {
		
        final int size = (int) raf.length() / registro.length();
		insertarEn( size, registro );

        int posicionIndice = arbol.insertar( registro.getSucursal() );

        if( posicionIndice == arbol.tamaño() - 1 ) {

            int posicionArchivo = (int) raf.length() / registro.length();
            insertarEn( posicionArchivo, registro );

            final RegIndice indice = arbol.buscar(posicionIndice);

            if( indice.getLiga() == SIN_ASIGNAR )
                arbol.modificar( posicionIndice, posicionArchivo );

        } else {

            int posicionArchivo = indiceDenso.getLiga( posicionIndice + 1 );
            insertarEn( posicionArchivo, registro );

            if( indiceDenso.getLiga( posicionIndice ) == SIN_ASIGNAR )
                indiceDenso.updateLiga( posicionIndice, posicionArchivo );

            for( posicionIndice++;
                 posicionIndice < indiceDenso.size(); posicionIndice++ )
            {
                posicionArchivo = indiceDenso.getLiga( posicionIndice ) + 1;
                indiceDenso.updateLiga( posicionIndice, posicionArchivo );
            }
        }
	}

    private void insertarEn( int posicion, Registro registro ) throws IOException {

        final int size = (int) raf.length() / registro.length() - 1;
        for( int i = size; i >= posicion; i-- ) {

            Registro temp = new Registro();
            raf.seek( i * temp.length() );
            temp.read( raf );
            raf.seek( (i + 1) * temp.length() );
            temp.write( raf );
        }
        raf.seek( posicion * registro.length() );
        registro.write( raf );
    }

	private void compactarDesde( int position ) throws IOException {

		Registro registry = new Registro();
		final int size = (int) raf.length() / registry.length() - 1;
		for( int i = position; i < size; i++ ) {

			raf.seek( (i + 1) * registry.length() );
			registry.read( raf );

			raf.seek( i * registry.length() );
			registry.write( raf );
		}
	}

    public void compactar() throws IOException {

        Registro temp = new Registro();

        final int size = (int) raf.length() / temp.length();
        boolean borrado = false;
        int i = -1;
        while( !borrado && i < size ) {

            raf.seek( ++i * temp.length() );
            temp.read( raf );

            borrado = temp.borrado();
        }

        if( borrado ) {

            compactarDesde( i );
            raf.setLength( raf.length() - temp.length() );
        }
    }

	public int busquedaLineal(String clave ) throws IOException {

		final int indice = indiceDisperso.buscarIndice( clave );

		if( indice != SIN_ASIGNAR ) {

			int posicion = indiceDisperso.getLiga( indice );

            Registro temp = new Registro();
            boolean found = false;
            int i = -1;
            final int max =  ( (agrupa ? X_INTERVAL : 1 ) * MAX_CLNT) - 1;

            while( !found && i < max) {

                raf.seek( (posicion + ++i) * temp.length() );
                temp.read( raf );
                found = temp.getSucursal().equals( clave );
            }

            if ( !found ) {
                System.out.println("Error al buscar \"" + clave + "\" en las posiciones ["
                        + posicion +  ", " + (posicion + max) + "]\n");
                return SIN_ASIGNAR;
            }

            posicion += i;

            System.out.println("\"" + clave + "\" empieza en la posicion " + posicion );
				
			return posicion;
		}

		System.out.println("Valor de busqueda \"" + clave + "\" no encontrado\n");
		
		return SIN_ASIGNAR;
	}

    public int[] busquedaLinealIntervalo( String clave ) throws IOException {

		final int indice = indiceDisperso.buscarIndice( clave );

		if( indice != SIN_ASIGNAR ) {

		    int posicion[] = new int[2]; 
            posicion[0] = indiceDisperso.getLiga( indice );

            Registro temp = new Registro();
            boolean found = false;
            int i = -1;
            int max =  ( (agrupa ? X_INTERVAL : 1 ) * MAX_CLNT) - 1;

            while( !found && i < max) {

                raf.seek( (posicion[0] + ++i) * temp.length() );
                temp.read( raf );
                found = temp.getSucursal().equals( clave );
            }

            if ( !found ) {

                System.out.println("Error al buscar \"" + clave + "\" en las posiciones ["
                        + posicion[0] +  ", " + (posicion[0] + max) + "]\n");
                int[] nulo = { SIN_ASIGNAR, SIN_ASIGNAR };
                return nulo;
            }

            posicion[0] = posicion[0] + i;

		    final int size = (int) raf.length() / temp.length() - 1;
            raf.seek( size * temp.length() );
            temp.read( raf );
            boolean same = true;
            max = temp.getSucursal().equals( clave ) ? (MAX_CLNT - 1) : max;
            i = 0;
            
            while( same && i < max ) {
                
                raf.seek( (posicion[0] + ++i) * temp.length() );
                temp.read( raf );
                same = temp.getSucursal().equals( clave );
            }
            posicion[1] = posicion[0] + i;

            System.out.println("\"" + clave + "\" se encuentra en el intervalo ["
                    + posicion[0] + ", " + posicion[1] + "]");
	
			return posicion;
		}

		System.out.println("Valor de busqueda \"" + clave + "\" no encontrado\n");
		
        int[] nulo = { SIN_ASIGNAR, SIN_ASIGNAR };
        return nulo;
	}

	public boolean eliminacionLineal( String clave ) throws IOException {

		final int posicion = busquedaLineal( clave );
		
		if( posicion == SIN_ASIGNAR ) return false;

        Registro temp = new Registro();
		raf.seek( posicion * temp.length() );
		temp.read( raf );
		temp.setBorrado( true );
			
		raf.seek( posicion * temp.length() );
		temp.write( raf );
		
		compactar();
        recalcularIndice();

		return true;
	}

	public void mostrar() throws IOException {

		Registro registro = new Registro();
		final int size = (int) raf.length() / registro.length();
		indiceDisperso.mostrar();

		System.out.println( "Numero de registros: " + size );
		raf.seek( 0 );
		for( int i = 0; i < size; i ++ ) {

			registro.read( raf );
			if ( registro.borrado() ) continue;
			System.out.println( "( " + registro.getSucursal() + ", "
                                     + String.format( "%" + NUM_DIGITS + "d", registro.getNumero() ) + ", "
                                     + registro.getNombre() + ", "
                                     + registro.getSaldo() + " )" );
		}
	}

    public void borrar() throws IOException {

        raf.setLength(0);
        indiceDisperso.borrar();
    }

    public void cerrar() throws IOException {
        
        raf.close();
        indiceDisperso.cerrar();
    }

}
