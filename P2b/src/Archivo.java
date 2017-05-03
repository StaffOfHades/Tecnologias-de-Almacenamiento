import java.io.IOException;
import java.io.RandomAccessFile;

public class Archivo implements Constants {

	private RandomAccessFile raf = null;
	private IndiceDisperso indiceDisperso = null;

	private boolean agrupa = true;

    public Archivo(RandomAccessFile archivo, RandomAccessFile indice) {

		raf = archivo;
		indiceDisperso = new IndiceDisperso(indice);
	}

	/*------------------------------------------------------------------
    /  Insertar
    /------------------------------------------------------------------*/

	public void insertar( Registro registry ) throws IOException {
		
        final int size = (int) raf.length() / registry.length();
		insertarEn( size, registry );
		recalcularIndice();
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

    /*------------------------------------------------------------------
    /  Recalcular indice
    /------------------------------------------------------------------*/

	public void recalcularIndice() throws IOException {
		String prevTemp = "@";
			
        indiceDisperso.raf.setLength( 0 );
		Registro registry = new Registro();
		
        final int size = (int) raf.length() / registry.length();
		int interval = 0;

        if (size > 0) {

            raf.seek( 0 );
            registry.read( raf );

            indiceDisperso.insertarEn( 0, registry.getSucursal() );
            indiceDisperso.updateLiga( 0, 0 );
            prevTemp = registry.getSucursal();
        }

		for( int i = 1; i < size; i++) {

			raf.seek( i * registry.length() );
			registry.read( raf );

			if ( !agrupa || !registry.getSucursal().equals( prevTemp ) )
				interval++;

			if( interval == X_INTERVAL ){

				interval = 0;
				final int indiceSize = (int) indiceDisperso.raf.length() / indiceDisperso.registro.length();
				indiceDisperso.insertarEn( indiceSize, registry.getSucursal() );
				indiceDisperso.updateLiga( indiceSize, i );
			}
			prevTemp = registry.getSucursal();
		}
	}

	 /*------------------------------------------------------------------
    /  Compactar
    /------------------------------------------------------------------*/

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
            recalcularIndice();
        }
    }

     /*------------------------------------------------------------------
    /  Busqueda
    /------------------------------------------------------------------*/

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

    /*------------------------------------------------------------------
   /  Eliminacion
   /------------------------------------------------------------------*/

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

    /*------------------------------------------------------------------
    /  Mostrar
    /------------------------------------------------------------------*/

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

	/*-----------------------------------------------------------------
    / Borrar
    /-----------------------------------------------------------------*/

    public void borrar() throws IOException {

        raf.setLength(0);
        indiceDisperso.borrar();
    }

    /*------------------------------------------------------------------
    /  Cerrar
    /------------------------------------------------------------------*/

    public void cerrar() throws IOException {
        
        raf.close();
        indiceDisperso.cerrar();
    }


       /*
	public void sparseRemove(String target) throws IOException {

        Registro registry = new Registro();

		int position = indiceDisperso.buscarIndice(target);
		System.out.println("position: " + position);

		expandirDesde( position );

        indiceDisperso.moverLigas( position, SIN_ASIGNAR );
		raf.setLength( raf.length() - registry.length() );
		recalcularIndice();
	}
	*/
}
