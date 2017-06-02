package member.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import member.dao.UserDao;
import member.entity.User;

@RestController
public class UserController {
	@Autowired
	private UserDao userDao;
	
	private final String init = "5";  //基础金额，可用作初始赠送金额

	@RequestMapping("/user/findByName/{name}")
	public List<User> findByName(@PathVariable(name = "name") String name) {
		List<User> userList = userDao.findByNameLike(name);
		return userList;
	}
	
	@RequestMapping("/user/findById/{id}")
	public List<User> findById(@PathVariable(name = "id") String id) {
		List<User> userList = userDao.findById(Integer.parseInt(id));
		return userList;
	}
	
	@RequestMapping("/user/findByMobile/{mobile}")
	public List<User> findByMobile(@PathVariable(name = "mobile") String mobile) {
		List<User> userList = userDao.findByMobile(mobile);
		return userList;
	}
	
	@RequestMapping("/user/findByPhone/{phone}")
	public List<User> findByPhone(@PathVariable(name = "phone") String phone) {
		List<User> userList = userDao.findByPhone(phone);
		return userList;
	}
	
	@RequestMapping("/user/findByWechatid/{wechatid}")
	public List<User> findByWechatid(@PathVariable(name = "wechatid") String wechatid) {
		List<User> userList = userDao.findByWechatid(wechatid);
		return userList;
	}
	
	@RequestMapping("/user/findByQq/{qq}")
	public List<User> findByQq(@PathVariable(name = "qq")  String qq) {
		List<User> userList = userDao.findByQq(qq);
		return userList;
	}

	@RequestMapping("/user/findByAddress/{address}")
	public List<User> findByAddress(@PathVariable(name = "address") String address) {
		List<User> userList = userDao.findByAddressLike(address);
		return userList;
	}
	
	@RequestMapping("/user/findByBirthday/{birthday}")
	public List<User> findByBirthday(@PathVariable(name = "birthday") String birthday) {
		List<User> userList = userDao.findByBirthday(birthday);
		return userList;
	}

	@RequestMapping("/user/findAll")
	@ResponseBody
	public List<User> findAll(Pageable pageable) {
		//這裏使用分頁
		//Pageable pageable = new PageRequest(page, size, sort); 
		//Sort sort = new Sort(Sort.Direction.DESC, "id");
		List<User> userList = userDao.findAll(pageable);
		return userList;
	}
	
	@RequestMapping("/user/save")
	@ResponseBody
	public User save(User user) {
		user.setPhone(init);
		User u  = userDao.save(user);
		return u;
	}
	
	@RequestMapping("/user/delete")
	@ResponseBody
	public String delete(@RequestBody Map<String,String> params) {
		String mobile = params.get("mobile");
		List<User> list = userDao.findByMobile(mobile);
		User u = new User();
		if(list.size() > 0){
			u = list.get(0);
			userDao.delete(u);
			return u.getMobile();
		}
		return "0";
	}

}
