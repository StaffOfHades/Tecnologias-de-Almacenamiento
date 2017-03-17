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
	
	//Metodo para comprobar que el archivo se encuentre ordenado
	
	public void comprobarOrden () throws IOException {
		if (raf.length()!=0) {
			Registro temp1;
			boolean ordenado = true;
			int n=0;
			int cuenta=0;	
			int tempcuenta = -1;
			
			temp1= new Registro();
			int m= (int) (raf.length() / temp1.length());
			
			while (ordenado && n<m) {
				raf.seek (n*temp1.length());
				temp1.read (raf);
				cuenta = temp1.getNumero();
				if (temp1.paraBorrar() ) {
					n = m;
				} else if (cuenta>tempcuenta) {
					tempcuenta = cuenta;
					ordenado = true;
				} else {
					ordenado= false;
				}
				n++;
			}
			
			if (!ordenado) {
				System.out.println ("El archivo necesita ordenarse");
				archivoOrdenado();
			} else {
				System.out.println ("El archivo esta ordenado");
			}
		
		} else {
			System.err.println("El archivo esta vacio");
		}
	}
    
	// Metodo para ordenar el archivo
	
	private void archivoOrdenado() throws IOException {
		
		Registro uno= new Registro();
		Registro dos= new Registro();
		Registro temp = new Registro();
		int tamano = length();
		
		for (int i=0; i<=tamano; i++) {
			
			for (int n=0; n<tamano-1; n++) {
				raf.seek (n*temp.length());
				uno.read(raf);
				int c1 = uno.getNumero();
				raf.seek((n+1)*temp.length());
				dos.read(raf);
				int c2 = dos.getNumero();
				if (c1>c2) {
					temp = uno;
					raf.seek (n*temp.length());
					dos.write (raf);
					raf.seek((n+1)*temp.length());
					temp.write(raf);
				}
			}
		}
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
	public Registro consultar (int numCuenta) throws IOException {
		
		if (raf.length()!=0) {
			
			Registro temp = new Registro ();
			int tamano = length();
			int inicio = 0;
			int fin = tamano-1;
			int medio;
			
			while(inicio<=fin) {
				medio = (int) ((inicio+fin)/2);
				System.out.println ("medio " + medio);
				raf.seek (medio*temp.length());
				temp.read (raf);
				if (temp.getNumero() == numCuenta) {
					return temp;
				}else if (temp.getNumero () < numCuenta) {
					inicio= medio+1;
				} else {
					fin = medio-1;
				}
			}
			System.out.println (" El numero de cuenta: " + numCuenta + " no existe");
			return null;
		}
		return null;
	}
	
	
	//Insercion por busqueda binaria
	public void insertarBinaria (Registro registro) throws IOException {
		Registro temp = new Registro ();
		int tamano = (int) (raf.length() / registro.length());
		//System.out.println("tamano = " + tamano);
		if (tamano == 0) {
			insertarEn(0,registro);
		} else {	
			int inicio = 0;
			int fin = tamano;
			int medio = 0;
			while (inicio<fin) {
				medio = (int) ((inicio + fin)/2);
				//System.out.println("inicio = " + inicio + "; fin = " + fin + "; medio = " + medio);
				raf.seek( medio * temp.length() );
				temp.read(raf);
				int stop = fin-inicio;
				//System.out.println("stop = " + stop);
				if (stop == 1) {
					if (temp.getNumero() <= registro.getNumero()) {
						//System.out.println(temp.getNumero() + " <= " + registro.getNumero() + "; insertarEn(" + (medio + 1) + ")");
						insertarEn(medio + 1, registro);
					} else {
						//System.out.println(temp.getNumero() + " > " + registro.getNumero() + "; insertarEn(" + medio + ")");
						insertarEn(medio, registro);
					}						
					inicio = fin + 1;
				} else {
					if (temp.getNumero() <= registro.getNumero()) {
						inicio = medio;
						//System.out.println(temp.getNumero() + " <= " + registro.getNumero());
					} else {
						fin = medio;
						//System.out.println(temp.getNumero() + " > " + registro.getNumero());
					}
				}
			}
		}
	}
	
	public void borrarBinaria (int numCuenta) throws IOException {
		Registro temp = new Registro ();
		int tamano = length();
		//System.out.println("tamano = " + tamano);
		if (tamano == 0) {
			System.err.println("Datos inexistentes para borrar");
		} else {	
			int inicio = 0;
			int fin = tamano;
			int medio = 0;
			while (inicio<fin) {
				medio = (int) ((inicio + fin)/2);
				//System.out.println("inicio = " + inicio + "; fin = " + fin + "; medio = " + medio);
				raf.seek( medio * temp.length() );
				temp.read(raf);
				int stop = fin-inicio;
				//System.out.println("stop = " + stop);
				if (stop == 1) {
					if (temp.getNumero() <= numCuenta) {
						//System.out.println(temp.getNumero() + " <= " + numCuenta + "; borrarEn(" + medio + ")");
						borrarEn(medio);
					} else {
						//System.out.println(temp.getNumero() + " > " + numCuenta + "; borrarEn(" + (medio - 1) + ")");
						borrarEn(medio-1);
					}						
					inicio = fin + 1;
				} else {
					if (temp.getNumero() <= numCuenta) {
						inicio = medio;
						//System.out.println(temp.getNumero() + " <= " + numCuenta);
					} else {
						fin = medio;
						//System.out.println(temp.getNumero() + " > " + numCuenta);
					}
				}
			}
		}
	}
	
	
	private void borrarEn( int p ) throws IOException {
        
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

	private int length() throws IOException {
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
    / presenta los registros del archivo
    /-----------------------------------------------------------------*/
    
	public void imprimirRegistros() throws IOException {
        
		Registro registro = new Registro();
        int length = length();
		
		System.out.println( "Numero de registros: " + length );
		System.out.println( "Numero total de registros: " + ((int) (raf.length() / registro.length())) );
        raf.seek( 0 );
		
           
		for( int i = 0; i < length ; i++ ) {    
			registro.read( raf );
			System.out.println( "( " + registro.getSucursal() + ", "
                                    + registro.getNumero() + ", "
                                    + registro.getNombre() + ", "
                                    + registro.getSaldo() + " )" );          
			    
		}
	}
 }
