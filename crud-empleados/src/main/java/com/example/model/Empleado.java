package com.example.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Empleado(
	
		int id,
		String nombre,
		String apellido1,
		String apellido2,
		LocalDate fechaAlta,
		LocalDate fechaBaja,
		BigDecimal salario,
		Genero genero,
		String foto,
		int dpto
		
) {}
