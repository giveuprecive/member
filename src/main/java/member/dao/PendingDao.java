package member.dao;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import member.entity.Pending;

@Transactional
public interface PendingDao extends CrudRepository<Pending, Integer> {
	public List<Pending> findByNameLike(String name);
	
	public List<Pending> findById(int id);
	
	public List<Pending> findByMobile(String mobile);
	
	public List<Pending> findByPhone(String phone);
	
	public List<Pending> findByWechatid(String wechatid);
	
	public List<Pending> findByQq(String qq);
	
	public List<Pending> findByEmail(String email);
	
	public List<Pending> findByAddressLike(String address);

	public List<Pending> findByBirthday(String birthday);

	public List<Pending> findBySendtime(String sendtime);
	
	public List<Pending> findAll(Pageable pageable);
	
	@SuppressWarnings("unchecked")
	public Pending save(Pending Pending); 
	
	public void delete(Pending Pending); 
 
}
