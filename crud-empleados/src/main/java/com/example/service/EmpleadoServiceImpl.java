package com.example.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.dao.DBConexion;
import com.example.model.Empleado;
import com.example.model.Genero;

public class EmpleadoServiceImpl implements EmpleadoService {

	@Override
	public List<Empleado> dameEmpleados() throws SQLException {
		
	
		/**
		* La sentencia siguiente se puede comentar porque se implementara en el bloque
		* try-with-resources
		*/	
		/* Crear una instancia, que es lo mismo que crear un objeto
		 * de la clase DBConexion */
		//DBConexion dbConexion = new DBConexion("root", "Temp2026");
	
		/* Mediante la innstancia anterior, es decir el objeto dbConexion,
		 * podemos acceder a los metodos de instancia que se encuentran en 
		 * la clase DBConexion */
		
		/* Llamada o invocacion del metodo que realiza la coneccion a la base 
		 * datos */
		
		//Connection connection = null;
		ResultSet rs = null;
		List<Empleado> empleados = new ArrayList<Empleado>();

		/**
		* El bloque try-with-rosources es la forma moderna de cerrar un recurso, una vez
		* que hemos terminado de trabajar con el, y se hace de forma automatica una vez 
		* que se llega al final del bloque try.
		* 
		* Para que se pueda cerrar el recurso de forma automatica el mismo tiene que implementar
		* la interface AutoCloseable o Closeable.
		* 
		* ¿Que es un recurso? Cualquier cosa (conexion a una base de datos, una conexion de red,
		* un archivo) que tiene que ser devuelta una vez que hemos terminado de trabajar con 
		* la misma.
		* 
		* ¿Donde de ubica el recurso que se tiene que cerrar al terminar el bloque try?
		* 
		* Se ubica entre parentesis despues de el try
		* 
		*/		
		try (DBConexion dbConexion = new DBConexion("root", "Temp2026");
				Connection connection = dbConexion.getConexion()){
			/* La sentencia siguiente crea una conexion a la base datos que un 
			 * recurso que deber ser liberado cuando termines de utilizar
			 * 
			 * Un recurso es algo que hay que liberar cuando termines de trabajar 
			 * con el, por ejemplo, un fichero, una conexion de red, etc. */
			//connection = dbConexion.getConexion();
			
			/* Invocacion o llamada del metodo de instancia getEmpleados, 
			 * que se encuetra en la capa DAO, justo en la clase DBConexion */
		    rs = dbConexion.getEmpleados(connection);
		    
		    /* Hay que recorrer el ResultSet, es decir, la variale rs y 
		     * convertirlo a una lista de Empleado (List<Empleado>) 
		     * ¿Por qué? Porque, en primer lugar, un ResultSet es bastante limitado
		     * comparado con la interfaces del framework de celecciones, como seria
		     * la interface List, en este caso. En segundo lugar, porque ResultSet es 
		     * muy particular de un Sistema de Gestion de Base de Datos MySQL, pero
		     * si cambiamos a otro Sistema de Gestion de Base de Datos, ya no 
		     * tendriamos ResultSet, como por ejemplo con Microsoft SQL Server que 
		     * lo que tendriamos seria un RecordSet. Entonces para que el controlador
		     * MainController siempre puede utilizar la List<Empleado> se hace la 
		     * conversion */
		    
		    /* La interface ResultSet implementa la interface Iterator<E> por lo 
		     * cual tiene acceso a los metodos para iterar sobre todos los registros,
		     * de Empleado, en este caso */
			 
		    while (rs.next()) {
		    	
		    	// Si estamos aqui es porque el metodo next() ha devuelto verdadero (true)
		    	// luego de comprobar que se puede avanzar al siguiente elemento del 
		    	// del conjunto de empleados, recuperado de la tabla empleados
		    	// , entonces el empleado en cuestion se agregara a la lista de empleados
		    	
		    	// Crear el objeto empleado, a partir de llamar al constructor del 
		    	// record Empleado
		    	
		    	Empleado empleado = new Empleado(rs.getInt("id"),
		    			rs.getString("nombre"),
		    			rs.getString("apellido1"),
		    			rs.getString("apellido2"),
		     rs.getDate("fecha_alta").toLocalDate(),		
		     (rs.getDate("fecha_baja") != null ) ? 
		    		 			rs.getDate("fecha_baja").toLocalDate() :
		    		 								null,
		    			rs.getBigDecimal("salario"),
		 Genero.valueOf(rs.getString("genero")),
		    			rs.getString("foto"),
		    			rs.getInt("departamentos_id"));
		    			
		    	// Con el objeto empleado creado se lo podemos pasar como parametro
		    	// al metodo add(), a continuacion, para ir agregando empleado, 
		    	// en cada iteracion del bucle while a la lista empleados
		    	
		    	empleados.add(empleado);
		    }
		    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
		
		return empleados;
		
		
	}

	@Override
	public void persistirEmpleado(Empleado empleado, List<String> numeroTelefono, List<String> direccionesCorreo) throws SQLException {
		// TODO Auto-generated method stub
		
		DBConexion dbConexion = new DBConexion("root","Temp2026");
		try {
			
			Connection connection = dbConexion .getConexion();
			dbConexion.persistirEmpleado(connection, empleado,
					numeroTelefono, direccionesCorreo);
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}
	

}
