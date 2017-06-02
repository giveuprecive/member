package member.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import member.dao.EmployeeDao;
import member.entity.Employee;

/**
 * 会员管理
 * 
 * @author zhaokunyang
 *
 */
@RestController
public class EmployeeController {
	@Autowired
	private EmployeeDao employeeDao;

	// TODO 需要优化
	@RequestMapping(value = "/employee/findByName/{name}/{word}")
	public String findByName(@PathVariable(name = "name") String name, @PathVariable(name = "word") String word) {
		List<Employee> employee = employeeDao.findByNameAndWord(name, word);
		if (employee != null && employee.size() > 0) {
			return "1";
		}
		return "0";
	}

}
