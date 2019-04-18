package model.dao;

import java.util.List;

import model.entities.Department;

public interface DepartmentDao {
	void insert (Department dep);
	void deleteById (Integer id);
	void update(Department dep);
	Department findById(Integer id);
	List<Department>findAll();
}
