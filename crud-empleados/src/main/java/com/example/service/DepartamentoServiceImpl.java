package com.example.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.dao.DBConexion;
import com.example.model.Departamento;

public class DepartamentoServiceImpl implements DepartamentoService {

	@Override
	public List<Departamento> getDepartamentos() throws SQLException {
		// TODO Auto-generated method stub
		// Conectar con la capa DAO
		
		List<Departamento> departamentos = new ArrayList<>();
		ResultSet rs = null;
		//Connection connection = null;

		try (DBConexion dbConexion = new DBConexion("root", "Temp2026");
				Connection connection = dbConexion.getConexion();)  {
			 
			 rs= dbConexion.getDepartamentos(connection);
			// Recorrer el ResultSet, que es la variable rs, y crear 
			// una lista de departamentos 
			 
			 while (rs.next())
				try {
					departamentos.add(new Departamento(rs.getInt("id"),rs.getString("nombre")));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} 
		return departamentos;

	}

}
