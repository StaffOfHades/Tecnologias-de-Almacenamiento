import java.io.IOException;
import java.io.RandomAccessFile;

public class Disperso implements Constants {

	private RandomAccessFile raf = null;
	private IndiceDisperso indiceDisperso = null;
	RandomAccessFile tempIndex;

	//Limite de indice para reestructuracion
	int insertionLim = 10;

	//Intervalos para recreacion de Indice Disperso
	
    //Limite de inserciones
	int insertions = 0;
	
    //Restriccion para reestructuracion
	private boolean restriction = false;
    private boolean juntaIguales = true;

    public Disperso(RandomAccessFile archivo, RandomAccessFile indice) {

		tempIndex = indice;
		raf = archivo;
		indiceDisperso = new IndiceDisperso(indice, STR_SIZE);
	}

	public void insertDisperso(Registro registry) throws IOException {
		
        int regSize = (int) raf.length() / registry.length();
		int indSize = (int) indiceDisperso.raf.length() / indiceDisperso.registro.length();
		if (indSize >= 0) {
			insertarEn(regSize, registry);
			restoreIndex();
		} else {
            System.out.println("Couldnt add " + registry.toString());
		}
	}

	public void restoreIndex() throws IOException {
		String prevTemp = "@";
			
        indiceDisperso.raf.setLength( 0 );
		Registro registry =  new Registro();
		
        int fileSize = (int) raf.length() / registry.length();
		int changesControl = 0;
			
        if ( !restriction ) {
			for( int k = 0; k < fileSize; k++) {
					
                raf.seek( k * registry.length() );
			    registry.read( raf );
				
                if ( k == 0 ) {

					indiceDisperso.insertarEn( 0, registry.getSucursal() );
					indiceDisperso.updateLiga( 0, k );
					prevTemp = registry.getSucursal();
					continue;
				}
				
                if ( !juntaIguales || !registry.getSucursal().equals(prevTemp) )
					changesControl++;

                if( changesControl == X_INTERVAL ){
						
                    changesControl = 0;
					int indSize = (int) indiceDisperso.raf.length() / indiceDisperso.registro.length();
					indiceDisperso.insertarEn( indSize, registry.getSucursal() );
					indiceDisperso.updateLiga( indSize, k );
				}
			    prevTemp=registry.getSucursal();
		    }
		} else {
			for( int k = 0; k < fileSize; k++ ) {
				
                raf.seek( k * registry.length() );
				registry.read( raf );
				if( prevTemp.equals("@") ){

					indiceDisperso.insertarEn( 0, registry.getSucursal() );
					indiceDisperso.updateLiga( 0, k );
					prevTemp = registry.getSucursal();
					continue;
				}
					
                if( !registry.getSucursal().equals(prevTemp) ) {
					int indSize = (int) indiceDisperso.raf.length() / indiceDisperso.registro.length();
					indiceDisperso.insertarEn( indSize, registry.getSucursal() );
					indiceDisperso.updateLiga( indSize, k );
				}
				prevTemp=registry.getSucursal();
			}
		}
	}

	private void verifyInsertion() throws IOException {

		insertions++;
	 	if( insertions >= insertions ) {

			insertions = 0;
			restoreIndex();
		}
	}

	public void testLastEntryCheck() throws IOException {
		
        System.out.println(indiceDisperso.keyOnLast("Sucursal   3"));
	}
	
	public void checkBoundarySystem() throws IOException {

		int[] vector = indiceDisperso.getBoundaries("Sucursal   3");
		System.out.println("(" + vector[0] + ", " + vector[1] + ")");
	}

	public void insertDispersoMedio(int position, Registro registry) 
        throws IOException
	{

		int fileSize = (int) raf.length() / registry.length();
		Registro temp = new Registro();
		indiceDisperso.pushPointers( indiceDisperso.linearSearchDisperso( registry.getSucursal() ), 1 );
		
		for( int i = fileSize - 1; i >= position; i-- ) {
            
			raf.seek( i * temp.length() );
			temp.read( raf );
            
			raf.seek( (i + 1) * temp.length() );
			temp.write( raf );
		}
        
		raf.seek( position * registry.length() );
		registry.write( raf );
	}
	
	public void sparseRemove(String target) throws IOException {
			
        Registro registry = new Registro();
		int fileSize = (int) raf.length() / registry.length();
		int position = indiceDisperso.linearSearchDisperso(target);
		for( int i = position; ( i + 1 ) < fileSize; i++ ) {
	            
			raf.seek( (i + 1) * registry.length() );
			registry.read( raf );
	            
			raf.seek( i * registry.length() );
			registry.write( raf );
		}
		
        indiceDisperso.pushPointers(position, SIN_ASIGNAR);
		raf.setLength( raf.length() - registry.length() );
		restoreIndex();
	}
	

	
	//Busqueda de clave en indice, regresa posiciuon en archivo
	public int linearSearch(String searchKey, boolean group)
        throws IOException
    {

		int searchResult = SIN_ASIGNAR;
		searchResult = indiceDisperso.linearSearch( searchKey );
		
		if(searchResult != SIN_ASIGNAR) {
			int loadInt = indiceDisperso.getLiga(searchResult);
				
			System.out.println(group ? "Valor de busqueda encontrado en la posicion " + (searchResult + 1) + 
						" en el indice" :
						"Valor de busqueda encontrado en la posicion " + (loadInt + 1) + " del archivo.");
				
			return loadInt;
		} else {
			System.out.println("Valor de busqueda no encontrado como " + (group ? "grupo" : "registro" ));
		}
		
		return searchResult;
	}

	public void linearSearchDisperso(String searchKey) throws IOException {
		
        indiceDisperso.linearSearchDisperso(searchKey);
	}
	
	public void deleteLinear(String searchKey) throws IOException {

		int selectionNumber = linearSearch(searchKey, false);
		
		Registro temp = new Registro();
			
		raf.seek( selectionNumber * temp.length() );
		temp.read( raf );
		temp.setBorrado( true );
			
		raf.seek( selectionNumber * temp.length() );
		temp.write( raf );
		
		compressFile();
	}
	
	public void compressFile() throws IOException {	
			
        Registro temp = new Registro();
		int entrySize = (int) raf.length() / temp.length();
		for(int c = 0; c < entrySize; c++) {

			raf.seek( c * temp.length() );
			temp.read( raf );
				
			if( temp.borrado() ) {
					
				Registro deletedRegistry = temp;
					
				for(int t = c; (t + 1) < entrySize; t++) {
					raf.seek( (t + 1) * temp.length() );
					temp.read( raf );
					raf.seek( t * temp.length() );
					temp.write( raf );
				}
					
				raf.setLength( raf.length() - temp.length() );
				entrySize--;
				restoreIndex();
				return;
			}
		}
	}

	public void indexUpdate() throws IOException {
		
        tempIndex.setLength( 0 );
		String prevTemp = "";
		Registro temp = new Registro();
		int entrySize = (int) raf.length()/temp.length();
		for(int k = 0; k < entrySize; k++) {

			raf.seek( k * temp.length() );
			temp.read( raf );
			if( !temp.getSucursal().equals(prevTemp) ) {

				prevTemp = temp.getSucursal();
				int indPosition = indiceDisperso.getPosicion( temp.getSucursal() );
				indiceDisperso.updateLiga( indPosition, k );
		    }
    	}
	}

	public void insertar (Registro registry) throws IOException{
		
        int indPosition = indiceDisperso.getPosicion( registry.getSucursal() );
		if (indPosition == indiceDisperso.size() - 1){

			int filePosition = (int) raf.length() / registry.length();
			insertarEn(filePosition, registry);
			if ( indiceDisperso.getLiga(indPosition) == SIN_ASIGNAR )
				indiceDisperso.updateLiga(indPosition, filePosition);
		} else {

			int filePosition = indiceDisperso.getLiga( indPosition + 1 );
			insertarEn( filePosition, registry );
			if( indiceDisperso.getLiga(indPosition) == SIN_ASIGNAR )
				indiceDisperso.updateLiga(indPosition, filePosition);

			for( indPosition++; indPosition < indiceDisperso.size(); indPosition++ ) {

				filePosition = indiceDisperso.getLiga( indPosition ) + 1;
				indiceDisperso.updateLiga( indPosition, filePosition );
			}
		}
	}

	private void insertarEn( int posicion, Registro registro ) throws IOException {
		
        int n = (int) raf.length() / registro.length();
		for( int i = n - 1; i >= posicion; i-- ) {

			Registro temp = new Registro();
			raf.seek( i * temp.length() );
			temp.read( raf );
			raf.seek( (i + 1) * temp.length() );
			temp.write( raf );
		}
		raf.seek( posicion * registro.length() );
		registro.write( raf );
	}

	public void mostrar() throws IOException {

		Registro registro = new Registro();
		int size = (int) raf.length() / registro.length();
		indiceDisperso.mostrar();
		System.out.println( "Numero de registros: " + size );
		raf.seek( 0 );
		for( int i = 0; i < size; i ++ ) {

			registro.read( raf );
			System.out.println( "( " + registro.getSucursal() + ", "
                                     + String.format( "%" + NUM_DIGITS + "d", registro.getNumero() ) + ", "
                                     + registro.getNombre() + ", "
                                     + registro.getSaldo() + " )" );
		}
	}
    
    public void cerrar() throws IOException {
        
        raf.close();
        indiceDisperso.cerrar();
    }
}
