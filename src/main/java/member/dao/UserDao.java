package member.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import member.entity.User;

@Transactional
public interface UserDao extends CrudRepository<User, Integer> {
	public List<User> findByNameLike(String name);
	
	public List<User> findById(int id);
	
	public List<User> findByMobile(String mobile);
	
	public List<User> findByPhone(String phone);
	
	public List<User> findByWechatid(String wechatid);
	
	public List<User> findByQq(String qq);
	
	public List<User> findByEmail(String email);
	
	public List<User> findByAddressLike(String address);

	public List<User> findByBirthday(String birthday);

	public List<User> findBySendtime(String sendtime);
	
	public List<User> findAll(Pageable pageable);
	
	@SuppressWarnings("unchecked")
	public User save(User user); 
	
	public void delete(User user); 
 
}
