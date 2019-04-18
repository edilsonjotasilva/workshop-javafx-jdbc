package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {
	private Connection conn;

	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Department obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("INSERT into department (Name) Values(?)");
			st.setString(1, obj.getName());
			st.executeUpdate();		
			////////////buscando elemento inserido/////////////
			st = conn.prepareStatement("select * from department where Name = ?");
			st.setString(1, obj.getName());
			rs = st.executeQuery();
			while(rs.next()) {
				Department dep = new Department();
				dep.setId(rs.getInt("Id"));
				dep.setName(rs.getString("Name"));
				System.out.println("departamento inserido: "+ dep.getName()+ " ID : "+ dep.getId());
			}					
		} catch (SQLException e) {
		throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE from Department " + "WHERE Id = ?");
			st.setInt(1, id);
			int rowsAffected = st.executeUpdate();
			if (rowsAffected == 0) {
				System.out.println("theres no department with this ID");
			}
			System.out.println("Rows Affected : " + rowsAffected);
		} catch (SQLException e) {
			throw new DbException(e.getMessage());

		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Department findById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("Select * from Department " + "WHERE Id = ?");
			st.setInt(1, id);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				Department dep = new Department(rs.getInt("Id"), rs.getString("Name"));
				return dep;
			} else {
				System.out.println("Department not found");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
		return null;
	}

	@Override
	public List<Department> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;

		List<Department> lista = new ArrayList<Department>();
		try {

			st = conn.prepareStatement("Select * from Department");
			rs = st.executeQuery();

			while (rs.next()) {
				Department dep = new Department();
				dep.setId(rs.getInt("Id"));
				dep.setName(rs.getString("Name"));
				lista.add(dep);

			}
			System.out.println();
			return lista;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}

	}
	@Override
	public void update(Department dep) {
	PreparedStatement st = null;
	ResultSet rs = null;
	try {
		st = conn.prepareStatement(
				"update department set Name = ? "
				+ "WHERE Id = ?");
		st.setString(1,dep.getName());
		st.setInt(2, dep.getId());
		
		int rowsAffected = st.executeUpdate();
		if(rowsAffected > 0 ) {
			System.out.println("New name of Department: "+dep.getName());
		}else {
			System.out.println("Não há Departamento com esse ID");
		}
		
		
		
	} catch (SQLException e) {
		throw new DbException(e.getMessage());
	}
	finally {
		DB.closeStatement(st);
	}
		
	}

}
