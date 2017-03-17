/******************************************************************
/  clase Archivo
/
/  autor: Dr. Jose Luis Zechinelli Martini
/******************************************************************/

import java.io.*;

public class Archivo {
    
	private RandomAccessFile raf = null;
    
    /*-----------------------------------------------------------------
    / constructores
    /-----------------------------------------------------------------*/
    
	public Archivo( RandomAccessFile raf ) {        
		this.raf = raf;
	}
    
    public int length() throws IOException {
        
        Registro temp = new Registro();
        
        int n = (int) (raf.length() / temp.length());

        boolean paraBorrar = true;
    
        while (paraBorrar && n > 0) {
            temp = new Registro();
            
            raf.seek( (n - 1) * temp.length() ) ;
            temp.read( raf );
            
            paraBorrar = temp.paraBorrar();
            
            if (paraBorrar)
                n--; 
        }

        return n;
    }

    /*-----------------------------------------------------------------
    / inserta un registro al inicio del archivo
    /-----------------------------------------------------------------*/
    
	public void insertarI( Registro registro ) throws IOException {
        
		insertarEn( 0, registro );
	}
   

    /*-----------------------------------------------------------------
    / inserta un registro al final del archivo
    /-----------------------------------------------------------------*/
    
	public void insertarF( Registro registro ) throws IOException {
		insertarEn( length(), registro );
	}

    /*-----------------------------------------------------------------
    / busca un registro
    /----------------------------------------------------------------*/ 
    public void imprimir(int numero) throws IOException {
        
        Registro registro = consultar(numero);
    
        if (registro != null && !registro.paraBorrar() ) { 
			System.out.println( "Se encontro:\n\t( " + registro.getSucursal() + ", "
                                     + registro.getNumero() + ", "
                                     + registro.getNombre() + ", "
                                     + registro.getSaldo() + " )" );
		}
    }  
    
    public void cambiar(int numero, double saldo) throws IOException {
        Registro registro = consultar(numero);
        if (registro != null) {
            registro.setSaldo(saldo);
            raf.seek( numero * registro.length() );   // Sobreeescribe el registro modificado
		    registro.write( raf );
            System.out.println( "El saldo actual para " + registro.getNumero() + " es de: " + registro.getSaldo());
        } 
    }
   
    public Registro consultar(int numero) throws IOException {
        Registro registro = new Registro();
		int length = length();
 
        boolean found = false;        
        int i = 0;    

        while (!found && i < length) {
            registro = new Registro();
            raf.seek( i * registro.length() );
            registro.read( raf );        
            found = registro.getNumero() == numero;
            i++;
        }
        
        if (found) { 
		    return registro;
        } else {
            System.err.println( "El numero de cuenta " + numero + " no existe");
            return null;
        }

    }

    /*-----------------------------------------------------------------
    / presenta los registros del archivo
    /-----------------------------------------------------------------*/
    
	public void imprimirRegistros() throws IOException {
        
		Registro registro = new Registro();
		int length = length();
        
		System.out.println( "Numero de registros: " + length );
           
		for( int i = 0; i < length; i++ ) {
            raf.seek( i * registro.length() );
			registro.read( raf );
            
			System.out.println( "( " + registro.getSucursal() + ", "
                                     + registro.getNumero() + ", "
                                     + registro.getNombre() + ", "
                                     + registro.getSaldo() + " )" );
		}
	}
    
    /*-----------------------------------------------------------------
    / desplaza registros para insertar un registro en la posicion p
    /-----------------------------------------------------------------*/
    
	private void insertarEn( int p, Registro registro ) throws IOException {
        
		int n = (int) (raf.length() / registro.length());
        Registro temp;

        int i = n - 1;
        boolean paraBorrar = true;
    
        while (paraBorrar && i >= p) {
            temp = new Registro();
            
            raf.seek( i * temp.length() ) ;
            temp.read( raf );
            
            paraBorrar = temp.paraBorrar();
            
            if (paraBorrar)
                i--; 
        }


        for (int j = i; j >= p; j--) {
            temp = new Registro();        
    
            raf.seek( j * temp.length() );
            temp.read( raf );

            raf.seek( (j + 1) * temp.length() );
            temp.write( raf );
        }       	
	
        raf.seek( p * registro.length() );   // inserta el nuevo registro
		registro.write( raf );
	}

    /*-----------------------------------------------------------------
    / desplaza registros para insertar un registro en la posicion p
    /-----------------------------------------------------------------*/

    public void borrarEn( int p ) throws IOException {
        
        Registro temp = new Registro();
		int n = (int) (raf.length() / temp.length());

        for (int i = p; i < (n - 1); i++) {
            temp = new Registro();        
    
            raf.seek( (i + 1) * temp.length() );
            temp.read( raf );

            raf.seek( i * temp.length() );
            temp.write( raf );
        }       	
        	
        temp = new Registro();
        temp.setParaBorrar();

        raf.seek( (n - 1) * temp.length() );
		temp.write( raf );
	}
 
 }
