package com.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.apache.catalina.filters.RemoteIpFilter.XForwardedRequest;

import com.example.model.Empleado;
import com.example.model.Genero;
import com.example.service.EmpleadoService;
import com.example.service.EmpleadoServiceImpl;

/**
 * Servlet implementation class AltaController
 */
@WebServlet("/AltaController")
public class AltaController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger("AltaController");
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AltaController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// La peticion (request) de mostrar el formulario de Alta,
		// se recibe en este metodo, y en respuesta se mostrara 
		// una vista que podemos llamar formulario.jsp
		request.getRequestDispatcher("formulario.jsp")
		.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/**
		* Los datos del formulario se recibiran aqui
		* y siempre como cadena de caracteres (String)
		*/ 

		String nombreRecibido = request.getParameter("nombre");
		String primerApellido = request.getParameter("primerApellido");
		String segundoApellido = request.getParameter("segundoApellido");
		LocalDate fechaAlta = LocalDate.parse(request.getParameter("fechaAlta"));
		BigDecimal salario = new BigDecimal(request.getParameter("salario"));
		Genero genero = Genero.valueOf(request.getParameter("genero"));
		int dpto = Integer.parseInt(request.getParameter("dpto"));
		String numerosTelefono = request.getParameter("telefonos");
		List<String> listadoNumerosTelefono = null;

		// Comprobar si hemos recibido numerosTelefono y crear listadoNumerosTelefono
		if (!numerosTelefono.isEmpty()) {

		String[] arrayNumerosTelefono = numerosTelefono.split(";");
		listadoNumerosTelefono = Arrays.asList(arrayNumerosTelefono);
		String direccionesCorreo = request.getParameter("emails");
		List<String> listadoDireccionesCorreo = null;

		if (!direccionesCorreo.isEmpty()) {

		String[] arrayDireccionesCorreo = direccionesCorreo.split(";");
		listadoDireccionesCorreo = Arrays.asList(arrayDireccionesCorreo);
		
		
		}
		
		// Crear el record empleado 
		Empleado empleado = new Empleado(0, nombreRecibido, 
		primerApellido, segundoApellido, 
		fechaAlta, null, salario, genero, null, dpto);	
		
		/**
		* A continuacion hay que conectarse a la capa de servicio, donde
		* habra un metodo que reciba como parametro el empleado que se va 
		* a persistir, conjuntamente con los telefonos y los correos de 
		* dicho empleado.
		* 
		* Este metodo de servicio, se conectara con la capa DAO a un metodo que 
		* sera quien realmente guarde en la tabla en el empleado con sus correos
		* y telefonos, si los tienes, y (MUY IMPORTANTE) en el marco de una 
		* Transaccion.
		*/
		EmpleadoService empleadoService = new EmpleadoServiceImpl();

		try {
		empleadoService.persistirEmpleado(empleado,
		listadoNumerosTelefono, listadoDireccionesCorreo);

		// Mostrar la vista que contiene la tabla de empleados
		// a ver si el empleado persistido aparece en ella

		List<Empleado> empleados = empleadoService.dameEmpleados();

		request.setAttribute("empleados", empleados);

		request.getRequestDispatcher("listado.jsp")
		.forward(request, response);
		} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
		
		}
	}

}
