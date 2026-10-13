package com.example.service;

import java.sql.SQLException;
import java.util.List;

import com.example.model.Departamento;

public interface DepartamentoService {
	List<Departamento> getDepartamentos() throws SQLException;
}
