package member.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import member.entity.Employee;

@Transactional
public interface EmployeeDao extends CrudRepository<Employee, Integer> {
	
	public List<Employee> findByNameLike(String name);
	
	public List<Employee> findByNameAndWord(String name,String word);
	
	public List<Employee> findById(int id);
	
	public List<Employee> findByMobile(String mobile);
	
	public List<Employee> findByPhone(String phone);
	
	public List<Employee> findByWechatid(String wechatid);
	
	public List<Employee> findByQq(String qq);
	
	public List<Employee> findByEmail(String email);
	
	public List<Employee> findByAddressLike(String address);

	public List<Employee> findByBirthday(String birthday);

	public List<Employee> findBySendtime(String sendtime);
	
	public List<Employee> findAll(Pageable pageable);
	
	@SuppressWarnings("unchecked")
	public Employee save(Employee employee); 
	
	public void delete(Employee employee); 
 
}
