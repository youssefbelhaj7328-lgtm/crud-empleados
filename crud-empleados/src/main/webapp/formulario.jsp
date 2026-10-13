<%@page import="com.example.model.Departamento"%>
<%@page import="java.util.List"%>
<%@page import="com.example.service.DepartamentoServiceImpl"%>
<%@page import="com.example.service.DepartamentoService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Formulario</title>
</head>
<body>
<!-- Para recoger el valor de los controles del formulario, es decir,
la informacion del fomulario, en ausencia del lenguaje JavaScript, el atritubuto
principal es el atributo name de los elementos o controles de entrada, como 
pueden ser los input y el elemento select.

Cuando se pulsa el boton, que realmente no es boton, sino un elemento input con 
el atributo type igual a submit, se genera el evento submit (Un evento es culquier
cosa que ocurre en nuesto documento html y que el navegador web lo notifica para 
que hagamos algo en respuesta) que, automaticamente recogera todo lo que se este
en los controles del formulario y se lo asignara a los correspondientes atributos
name, y se enviara a la direccion (URL)
<!-- El mismo servlet AltaController que recibe la peticion de mostrar el 
formulario puede recibir los datos del formulario, porque recibe las peticiones
por metodos diferentes del protocolo HTTP (get para mostrar el formulario y post
para recibir los datos del formulario -->
<!-- El mismo servlet AltaController que recibe la peticion de mostrar el 
formulario puede recibir los datos del formulario, porque recibe las peticiones
por metodos diferentes del protocolo HTTP (get para mostrar el formulario y post
para recibir los datos del formulario -->
	<h1>Formulario de Alta de Empleado</h1>
	<fieldset>
		<legend> Formulario Alta / Modificacion</legend>
		<form action="AltaController" method="post">
			<label for="nombre">Nombre : </label> 
			<input id="nombre" name="nombre"	type="text" placeholder="Su nombre aqui ,porfa"
				title=" Se solicita nombre de empleador de alta"
				required="required"> 
				
				<label for="primerApellido">Primer Apellido : </label> 
				<input id="primerApellido" name="primerApellido" type="text"
				required="required"> 
				<label for="segundoApellido">Segundo Apellido:</label>
				 <input id="segundoApellido" type="text"
				placeholder="No es obligatorio" name="segundoApellido"> 
				<label for="fechaAlta">Fecha de	de Alta:</label> 
				<input id="fechaAlta" type="date" name="fechaAlta"
				required="required"> 
				<label for="salario">Salario: </label> 
				<input id="Salario" type="text" name="salario" >
				<fieldset>
			<legend>Genero</legend>
			<label for="hombre"> Hombre:</label> <input id="hombre" type="radio"
				name="genero"value="HOMBRE"><label for="mujer"> Mujer:</label> <input
				id="mujer" type="radio" name="genero" value="MUJER"> <label for="otro">
				Otro:</label> <input id="otro" type="radio" name="genero "value="OTRO" >
			</fieldset>
	<label for="foto">Foto del empleado:</label>
	<input id="foto" type="file">
	<label for="dpt">Departamento:</label>
	<select id="dpt"name="dpto"
	required="required">
		<option></option>
		<!-- La lista de departamentos se debe de traer de la tabla 
departamentos de la base de datos, porque de esta manera se pueden
agregar o eliminar departamentos sin tener que modificar el codigo
de nuestra aplicacion WEB -->

<% 

//  Conectarse al servicio de Departamento para recuperar 
// una lista de todos los departamentos
DepartamentoService  service = new DepartamentoServiceImpl();
List <Departamento> departamentos = service.getDepartamentos();
for (Departamento dpto : departamentos){
	%>
	<option value="<%=dpto.id() %>"><%=dpto.nombre() %></option>
	<%
}
%>







	
	</select>
	<label for="emails">Correo(s):</label>
	<input id="emails" type="text"
	name="emails"
		placeholder="uno o varios ,separados por punto y la coma">

	<label for="telefonos">Telefon(s)</label>
	<input id="telefonos"  type="text"
	name="telefonos"
		placeholder="uno o varios, separados por ;"
		title="Uno o varias direcciones de correo separadas por el punto y la coma ">



	<br />
	<br />
	<input type="submit" value="Enviar formulario"
		title=" Recoger y enviar la info de los controles del formulario al servidor">



	</form>

	</fieldset>
</body>
</html>