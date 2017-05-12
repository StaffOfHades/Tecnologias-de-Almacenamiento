import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Archivo implements Constants {

	private RandomAccessFile raf = null;
	private Arbol arbol = null;

	private boolean agrupa = true;

    public Archivo( RandomAccessFile archivo, RandomAccessFile indice ) throws IOException {

		raf = archivo;
		arbol = new Arbol(indice);
	}

	public Registro buscar(String clave) throws IOException {

		final RegIndice indice = arbol.buscar(clave);

		if( indice != null ) {

            // Se crea un registro de datos
            Registro registro = new Registro();

            // Se posiciona el puntero en la posicion del registro y se lee
            raf.seek( indice.getLiga() * registro.length() );
            registro.read(raf);

            return registro;
		}
		
		return null;
	}

    public ListIterator<Registro> buscarGrupo(String clave) throws IOException {

        final RegIndice indice = arbol.buscar(clave);

        if( indice != null ) {

            //Se crea una lista de registro de datos
            final List<Registro> list = new ArrayList<>();

            int i = indice.getLiga();
            Registro registro = new Registro();
            final int max = (int) raf.length() / registro.length();

            raf.seek( i * registro.length() );
            registro.read(raf);

            while( i++ < max && registro.getSucursal().equals(clave) ) {

                registro = new Registro();
                registro.read( raf );
                if ( registro.borrado() ) continue;
                list.add( registro );
            }

            return list.listIterator();
        }
        return null;
    }

    public void insertar(Registro registro) throws IOException {

        final int tamaño = (int) raf.length() / registro.length();
        insertarEn( tamaño, registro );

        int posicionIndice = arbol.insertar(registro.getSucursal());

        if( posicionIndice == arbol.tamaño() - 1 ) {

            int posicionArchivo = (int) raf.length() / registro.length();

            final RegIndice indice = arbol.buscar(posicionIndice);

            if( indice != null && indice.getLiga() == SIN_ASIGNAR )
                arbol.modificar( posicionIndice, posicionArchivo - 1 );

        } else {

            RegIndice indice = arbol.buscar( posicionIndice - 1 );
            int posicionArchivo = indice.getLiga();
            indice = arbol.buscar(posicionIndice);

            if( indice != null && indice.getLiga() == SIN_ASIGNAR )
                arbol.modificar( posicionIndice, posicionArchivo );

            final int tamaño_arbol = arbol.tamaño();

            for( posicionIndice++;
                 posicionIndice < tamaño_arbol; posicionIndice++ )
            {
                indice = arbol.buscar( posicionIndice );
                posicionArchivo = indice.getLiga() + 1;
                arbol.modificar( posicionIndice, posicionArchivo );
            }
        }
    }

	public boolean borrar(String clave) throws IOException {

		final RegIndice indice = arbol.buscar(clave);
		
		if( indice != null ) {

            // Se crea un registro de datos
            Registro registro = new Registro();

            // Se posiciona el puntero en la posicion del registro y se lee
            raf.seek( indice.getLiga() * registro.length() );
            registro.read(raf);

            // Borrado logica en memoria del registro
            registro.setBorrado(true);
            registro.setSucursal("@¡Eliminado!@");

            // Borrado en disco
            raf.seek( indice.getLiga() * registro.length() );
            registro.write( raf );

            if ( raf.getFilePointer() == raf.length() )
                //Con esto se compacta el archivo de Indices si este es el ultimo registro
                arbol.borrar(indice.getClave());
            else {

                //Se lee el registro justo debajo del que esta borrado
                registro.read(raf);

                if(registro.getSucursal().equals(clave))
                    //Si entra al if significa que existe otro registro con la misma sucursal
                    arbol.modificar( indice.getClave(), indice.getLiga() + 1 );
                else
                    //Se compacta el archivo Indices
                    arbol.borrar(indice.getClave());
            }

            compactar();
        }

		return false;
	}

	public void mostrar() throws IOException {

		Registro registro = new Registro();
		final int tamaño = (int) raf.length() / registro.length();
		arbol.mostrar();

		System.out.println( "Numero de registros: " + tamaño );
		raf.seek(0);
		for( int i = 0; i < tamaño; i++ ) {

			registro.read(raf);
			if(registro.borrado()) continue;
			System.out.println( "( " + registro.getSucursal() + ", "
                                     + String.format( "%" + NUM_DIGITS + "d", registro.getNumero() ) + ", "
                                     + registro.getNombre() + ", "
                                     + registro.getSaldo() + " )" );
		}
	}

    public void borrar() throws IOException {

        raf.setLength(0);
        arbol.borrar();
    }

    public void cerrar() throws IOException {
        
        raf.close();
        arbol.cerrar();
    }

    private void insertarEn( int posicion, Registro registro ) throws IOException {


        System.out.println( "\nSe creo registro para posicion: " + posicion );
        System.out.println( "( " + registro.getSucursal() + ", "
                + String.format( "%" + NUM_DIGITS + "d", registro.getNumero() ) + ", "
                + registro.getNombre() + ", "
                + registro.getSaldo() + " )\n" );
        //System.out.println( "Total: " + ( tamaño() + 1 ) + "\n" );

        final int tamaño = (int) raf.length() / registro.length() - 1;

        for( int i = tamaño; i >= posicion; i-- ) {

            Registro temp = new Registro();

            raf.seek( i * temp.length() );
            temp.read(raf);

            raf.seek( ( i + 1 ) * temp.length() );
            temp.write(raf);
        }
        raf.seek( posicion * registro.length() );
        registro.write(raf);
    }

    private int tamaño() throws IOException {
        Registro registro = new Registro();
        return (int) raf.length() / registro.length();
    }

    public void compactar() throws IOException {

        Registro temp = new Registro();

        final int tamaño = (int) raf.length() / temp.length();
        boolean borrado = false;
        int i = -1;
        while( !borrado && i < tamaño ) {

            raf.seek( ++i * temp.length() );
            temp.read(raf);

            borrado = temp.borrado();
        }

        if( borrado ) {

            compactarDesde(i);
            raf.setLength( raf.length() - temp.length() );
        }
    }

    private void compactarDesde( int position ) throws IOException {

        Registro registro = new Registro();
        final int tamaño = (int) raf.length() / registro.length() - 1;

        for( int i = position; i < tamaño; i++ ) {

            raf.seek( ( i + 1 ) * registro.length() );
            registro.read(raf);

            raf.seek( i * registro.length() );
            registro.write(raf);
        }
    }

    public void mostrarArbol() throws IOException {
        arbol.mostrarArbol();
    }

}
