package model.dao.impl;

import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{
	//static Department depart;
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
private Connection conn;

	public SellerDaoJDBC(Connection conn) {
	this.conn = conn;
}
	@Override
	public void insert(Seller obj) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"INSERT INTO seller "
					+"(Name,  Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES "		
					+ "(?,?,?,?,? )",
					Statement.RETURN_GENERATED_KEYS);
			st.setString(1,obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, (new java.sql.Date (obj.getBirthDate().getTime())));						
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			int rowsAffected  = st.executeUpdate();
			if(rowsAffected >0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			}else {
				throw new DbException("Erro inesperado Nenhuma linha inserirda");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		
		}
	
		
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		
		try {
			st =conn.prepareStatement(
					"DELETE from seller where Id = ? "
					);
			st.setInt(1, id);
			int rowsAffected = st.executeUpdate();
			if(rowsAffected == 0) {
				System.out.println("Esse ID NÃO EXISTE!");
			}
			System.out.println("rows Affected: "+rowsAffected);
		}
		 catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
	}
	//retorna as linhas do banco filtrado pelo ID
	@Override
	public Seller  findById(Integer id){
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*, department.Name as DepName "
							+ "from seller inner join department "
							+ "on seller.DepartmentId = department.Id "
							+ "where seller.Id = ?");		
				st.setInt(1, id);
				rs = st.executeQuery();
				if(rs.next()) {
					Department dep = instantiationDepartment(rs);
					Seller obj = instatiationSeller(rs, dep);
					return obj;
				}			
				return null;				
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}		
	}
	
	private Department instantiationDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));// DepartmentId = 2
		dep.setName(rs.getString("DepName"));// Nome do departamento
		return dep;
	}
	private Seller instatiationSeller(ResultSet rs, Department dep) throws SQLException {
		Seller obj = new Seller();
			obj.setId(rs.getInt("Id"));
			obj.setName(rs.getString("Name"));
			obj.setEmail(rs.getString("Email"));
			obj.setBaseSalary(rs.getDouble("BaseSalary"));
			obj.setBirthDate(rs.getDate("BirthDate"));
			obj.setDepartment(dep);
		return obj;
	}
	
	@Override
	//retorna todoas as linhas do banco de Dados
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"select seller.*,department.Name as DepName "
					+"from seller inner join department "
					+"ON seller.DepartmentId = department.Id "					
					+"order by Name");											
				rs = st.executeQuery();			
				List<Seller>list  = new ArrayList<Seller>();
				Map<Integer,Department>map = new HashMap<Integer,Department>();
				while(rs.next()) {
					Department dep = map.get(rs.getInt("DepartmentId"));
					if(dep == null) {
						 dep = instantiationDepartment(rs);
						 map.put(rs.getInt("DepartmentId"), dep);						 
					}else {			
					Seller obj = instatiationSeller(rs, dep);					
					list.add(obj);
					}
				}			
				return list;				
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}		
	
	
	}
	@Override
	public List<Seller> findByDepartment(Department department) {// 1° passo, id 2 nome null
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"select seller.*,department.Name as DepName "
					+"from seller inner join department "
					+"ON seller.DepartmentId = department.Id "
					+"where DepartmentId = ? "//3° passo, ? recebe department.getId() que vale 2
					+"order by Name");	
			
			st.setInt(1, department.getId());//2° passo, department.getId == 2							
				rs = st.executeQuery();
				//4° passo, st.executeQuery() retorna todo seller que tem DepartementId = 2
				// e coloca esse retorno dentro da variavel rs
				List<Seller>list  = new ArrayList<Seller>();
				Map<Integer,Department>map = new HashMap<Integer,Department>();
				while(rs.next()) {
					Department dep = map.get(rs.getInt("DepartmentId"));// No inicio do
					
					// da condição while, map.get retorna nulo, pois a coleção map
					//ainda não foi preenchida
					
					if(dep == null) {
						 dep = instantiationDepartment(rs);
						 map.put(rs.getInt("DepartmentId"), dep);// rs.getInt("DepartmentId") é a chave
						 //dep é o valor referenciado pela chave
						 
					}else {	//else não é necessario, colocado apenas para compreensão				
					Seller obj = instatiationSeller(rs, dep);					
					list.add(obj);
					}
				}			
				return list;				
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}		
	}
	@Override
	public Department selectDepById(Integer id) {
		
		PreparedStatement st =null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
						"SELECT * FROM Department "
						+ "where Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			Department depart = new Department();
		    depart = new Department(rs.getInt("Id"),rs.getString("Name"));
		
			return depart;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	@Override
	public void update(Seller obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE seller "
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
					+ "WHERE Id = ?");
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			st.setInt(6, obj.getId());
		
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeConnection();
			DB.closeStatement(st);
		}
	}
}
