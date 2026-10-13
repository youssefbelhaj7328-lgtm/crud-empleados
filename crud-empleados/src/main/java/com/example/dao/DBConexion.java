/* Esta clase contendrá todo lo necesario para conectar
 * la aplicacion WEB que estamos desarrollando con la base
 * de datos, utilizando el protocolo JDBC (Java Database 
 * Connectivity) 
 * 
 * ¿Donde esta la teoria que respalda esta protocolo? 
 * 
 * https://docs.oracle.com/javase/tutorial/jdbc/index.html
 * 
 * */
package com.example.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.model.Empleado;

public class DBConexion implements AutoCloseable {

	private static final Logger LOG = Logger.getLogger("DBConexion");

	private String user;
	private String password;

	/*
	 * La siguiente propiedad va a almacenar el objeto de conexion a la base de
	 * datos, para realizar consultas utilizando el lenguaje SQL, posteriormente
	 */
	private Connection conn;

	// Constructor
	public DBConexion(String user, String password) {
		super();
		this.user = user;
		this.password = password;

	}

	/*
	 * Metodo que permite conectar a la base de datos y devuelve el objeto de
	 * coneccion.
	 */
	public Connection getConexion() throws ClassNotFoundException {

		String urlConnection = "jdbc:mysql://localhost:3306/empresa_crud_empleados";

		Properties connectionInfo;

		connectionInfo = new Properties();

		connectionInfo.put("user", this.user);
		connectionInfo.put("password", this.password);

		try {
			// La instruccion siguiente es necesaria para registrar el driver
			// de coneccion a la base de datos
			Class.forName("com.mysql.cj.jdbc.Driver");

			this.conn = DriverManager.getConnection(urlConnection, connectionInfo);
			LOG.info("Conexion establecida con la base de datos");
		} catch (SQLException e) {
			e.printStackTrace();
			LOG.log(Level.SEVERE, "Error de coneccion a la base de datos");
		}

		return this.conn;
	}

	/*
	 * Metodo que recupera todos los registro de la tabla de empleados.
	 * 
	 * Tener presente que todos los metodos que lancen consultas de SQL contra la
	 * base de datos tienen que hacerlo a traves de una conexion existente, por eso
	 * es que se pasa siempre como parametro al metodo la conexion que devuelve el
	 * metodo getConexion().
	 * 
	 * Una conexion es un recurso valioso, que tiene que ser cerrada una vez que
	 * hayamos terminado de utilizarla
	 */

	public ResultSet getEmpleados(Connection connection) {

		ResultSet rs = null;

		/*
		 * La variable local siguiente, query, almacena la consulta que se va a
		 * ejecutar, que por simple que nos parezca, no debemos de escribir directamente
		 * sin antes haberla probado en un cliente de conexion a la base de datos, como
		 * pueden ser el MySQL Workbench o el DBeaver
		 */
		String query = "select * from empleados";

		/*
		 * La consulta anterior hay que adecuarla, convertirla a binario, para que el
		 * motor de base de datos la entienda, porque ahora mismo esta en lenguaje
		 * humano pero las maquinas dicho lenguaje no lo entienden.
		 */

		/*
		 * Para una consulta sin parametros variables se untiliza la interfaz siguiente.
		 */
		Statement stmt;

		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rs;
	}

	/*
	 * Metodo que recupera todos los registros de la tabla de departamentos
	 */
	public ResultSet getDepartamentos(Connection connection) {
		ResultSet rs = null;
		String query = "select * from departamentos";
		try {
			Statement stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;

	}

	/*
	 * Metodo que persiste (guardar) en la base de datos un empleado con sus correos
	 * y telefonos correspondientes, en el marco de una transacion . que es
	 * transaccion¿ es un conjunto de operaciones ,sentencias de SQL en este
	 * caso,que el motor/** Metodo que persiste (guarda) en la base de datos un
	 * empleado con sus correos y telefonos correspondientes, en el marco de una
	 * transaccion.
	 * 
	 * ¿Que es una transaccion?
	 * 
	 * Es un conjunto de operaciones, sentencias de SQL en este caso, que el motor
	 * (engine) de base de datos ve como si fueran una sola, es una apuesta de todo
	 * o nada, es decir, si falla una operacion hay que deshacer todas las demas
	 * (rollback), y solamente se confirma la transaccion si todas las operaciones
	 * han tenido exito. * Ejemplos tipicos de transacciones:
	 * 
	 * 1. Sacar dinero de un cajero automatico. 2. Comprar un billete de tren o de
	 * avion, o de cualquier cosa. 3. Etc.
	 *
	 */
	public void persistirEmpleado(Connection connection, Empleado empleado, List<String> numerosTelefono,
			List<String> direccionesCorreo) throws SQLException {

		// Se van a ejecutar 3 consultas (queries)
		// una para insertar el empleado, y con el id del empleado
		// guardado o persistido, se ejecutaran las otras dos consultas
		// una para insertar los telefonos y otra para insertar los correos
		// , todo en el marco de una transaccion
		// persister empleado

		/*
		 * La consulta siguiente se ejecuta mediante un tipo de sentencia de SQL llamada
		 * Sentencia Preparada (Prepared Statement), que es la primera barrera de
		 * defensa contra los ataques de inyeccion de SQL, ya que se separa la parte
		 * fija de la consulta de los parametros variables. Por otra parte una Prepared
		 * Statement optimiza la ejecucion de las consultas y las hace tan rapidas su
		 * ejecucion como los procedimientos almacenados.
		 * 
		 * /* Utilizando el metodo println() que esta en el paquete System.out nos
		 * permite escribir en la consola, o terminal, informacion o mensajes respecto a
		 * muchas cosas, como por ejemplo si ha habido algun error en el momento de
		 * conectarnos a la base de datos, o si la conexion ha sido exitosa, pero el
		 * problema es que todos estos mensajes si el Eclipse se cierra o se bloquea ya
		 * no vamos a poder acceder a ellos, es decir, utilizando la terminologia de la
		 * ciber seguridad, no podriamos hacer un analis forence o post morten para
		 * saber porqué fallo todo.
		 */

		/*
		 * ¿Como se soluciona lo anterior, entonces? Creando un Logger para la clase, es
		 * decir un objeto, que muestre tambien los mensajes de error pero los deje
		 * registrados en un archivo .log que se puede analizar posteriormente aunque
		 * todo haya fallado * solamente se extraera el plan de ejecucion guardado y se
		 * le pasaran los parametros variables y la consulta se ejecutara muy rapido y
		 * en un entorno seguro, porque los parametros variables nunca se se llegan a
		 * encontrar directamente con la parte fija de la consulta
		 */

		String query1 = "INSERT INTO empresa_crud_empleados.empleados (nombre,apellido1,apellido2,fecha_alta,fecha_baja,salario,genero,departamentos_id)\r\n"
				+ "	VALUES (?,?,?,?,?,?,?,?)";

		// Persistir telefonos del empleado persistido
		String query2 = "INSERT INTO empresa_crud_empleados.telefonos (numero,empleados_id)\r\n" + "	VALUES (?,?)";

		// Persistir correos del empleado persistido
		String query3 = "INSERT INTO empresa_crud_empleados.correos (email,empleados_id)\r\n" + "	VALUES (?,?)";
//Declacion de las preparared Statment
		PreparedStatement pstmt1;
		PreparedStatement pstmt2;
		PreparedStatement pstmt3;

		/**
		 * ¿Como entrar en una transaction?
		 * 
		 * A traves del objeto de Connection, invocando al metodo setAutocommit = false
		 * de esta forma hasta que no se ejecute el metodo commit() no se van a guardar
		 * los cambios en las tablas de la base de datos
		 * 
		 * MUY IMPORTANTE!!! Cuando terminemos hay que poner el Autocommit a true
		 * nuevamente, porque de lo contrario todos los empleados de la empresa que
		 * esten por esa via de conexion no estaran guardando la informacion realmente
		 * en las tablas de la base de datos
		 */

		try {
			// iniciar la transaction
			// iniciar la transaction
			// a partir de que se ejecute la sentencia siguiente, cualquier cambio no se
			// guardara en las tablas de la base de dato, hasta que se haga un commit
			// explicitamente
			connection.setAutoCommit(false);
			// preparar la ejucucion de la query1
			pstmt1 = connection.prepareStatement(query1, Statement.RETURN_GENERATED_KEYS);
			// Preparar la ejecucion de la query1
			// Para obtener el id del empleado persistido hay que solicitar que
			// nos devuelvan las claves generadas, añadiendo un segundo parametro
			// al metodo preparedStatement

			// la consulta que se va a lanzar hay que completarla. porque
			// La consulta que se va a lanzar hay que completarla, porque
			// en este punto todavia los parametros variables de la Prepared Statement
			// no han sido completados
			pstmt1.setString(1, empleado.nombre());
			pstmt1.setString(2, empleado.apellido1());
			pstmt1.setString(3, empleado.apellido2());
			pstmt1.setDate(4, Date.valueOf(empleado.fechaAlta()));
			pstmt1.setDate(5, null);
			pstmt1.setBigDecimal(6, empleado.salario());
			pstmt1.setString(7, empleado.genero().name());
			pstmt1.setInt(8, empleado.dpto());

			long lasInertedId = 0L;

			// lanzar la consulta para persistir el empleado
			int totalRegitrosAfectados = pstmt1.executeUpdate();
			if (totalRegitrosAfectados > 0) {
				// Extraer las claves generadas y recorrelas para recuperar el UD DEL empleado
				// persistido
				ResultSet clavesGeneradas = pstmt1.getGeneratedKeys();
				// calaves generadas es una registro es decir una fila con varias colomnas.

				if (clavesGeneradas.next()) {

					// Recuperar el ID del empleado que esta esta en la primera posicion
					// es decir en la primerra columna

					lasInertedId = clavesGeneradas.getLong(1);

					/*
					 * Con el lastInsertedId nos podemos dirigir a las tablas de correos y telefonos
					 * e insertarlo /* Con el lastInsertedId nos podemos dirigir a las tablas de
					 * correos y telefonos e insertar los correos y los telefonos si es que los
					 * tienes, por lo cual comprobamos antes que tiene telefonos y/o correos el
					 * empleado a persistir
					 */
					// primeros con los telefonos
					if (numerosTelefono != null && !numerosTelefono.isEmpty()) {

						pstmt2 = connection.prepareStatement(query2);
						pstmt2.setInt(2, Math.toIntExact(lasInertedId));
						numerosTelefono.forEach(numeroTelefono -> {
							try {
								pstmt2.setString(1, numeroTelefono);
								pstmt2.addBatch();
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();

							}
						});
						pstmt2.executeBatch();

					}
					// Segundo con los correos

					if (direccionesCorreo != null && !direccionesCorreo.isEmpty()) {
						pstmt3 = connection.prepareStatement(query3);
						pstmt3.setInt(2, Math.toIntExact(lasInertedId));
						direccionesCorreo.forEach(email -> {
							try {

								pstmt3.setString(1, email);
								pstmt3.addBatch();
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();

							}
						});
						pstmt3.executeBatch();
					}
				}

			}

			// Si hemos llegado a este punto es que todo ha ido bien y se puede confirmar
			// (hacer commit) de todas las sentencias de SQL que no se guardaron en sus
			// respectivas tablas
			connection.commit();
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			// Rsstituimos el AutoCommet a true nuevament

			connection.setAutoCommit(true);

		}

	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub

		this.conn.close();

	}

}
