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

import member.dao.PendingDao;
import member.entity.Pending;
import member.utils.Contanst;

/**
 * 正在注册中的用户管理
 * 
 * @author zhaokunyang
 *
 */
@RestController
public class PendingController {
	@Autowired
	private PendingDao PendingDao;

	@RequestMapping("/pending/findByName/{name}")
	public List<Pending> findByName(@PathVariable(name = "name") String name) {
		List<Pending> PendingList = PendingDao.findByNameLike(name);
		return PendingList;
	}

	@RequestMapping("/pending/findById/{id}")
	public List<Pending> findById(@PathVariable(name = "id") String id) {
		List<Pending> PendingList = PendingDao.findById(Integer.parseInt(id));
		return PendingList;
	}

	@RequestMapping("/pending/findByMobile/{mobile}")
	public List<Pending> findByMobile(@PathVariable(name = "mobile") String mobile) {
		List<Pending> PendingList = PendingDao.findByMobile(mobile);
		return PendingList;
	}

	@RequestMapping("/pending/findByPhone/{phone}")
	public List<Pending> findByPhone(@PathVariable(name = "phone") String phone) {
		List<Pending> PendingList = PendingDao.findByPhone(phone);
		return PendingList;
	}

	@RequestMapping("/pending/findByWechatid/{wechatid}")
	public List<Pending> findByWechatid(@PathVariable(name = "wechatid") String wechatid) {
		List<Pending> PendingList = PendingDao.findByWechatid(wechatid);
		return PendingList;
	}

	@RequestMapping("/pending/findByQq/{qq}")
	public List<Pending> findByQq(@PathVariable(name = "qq") String qq) {
		List<Pending> PendingList = PendingDao.findByQq(qq);
		return PendingList;
	}

	@RequestMapping("/pending/findByAddress/{address}")
	public List<Pending> findByAddress(@PathVariable(name = "address") String address) {
		List<Pending> PendingList = PendingDao.findByAddressLike(address);
		return PendingList;
	}

	@RequestMapping("/pending/findByBirthday/{birthday}")
	public List<Pending> findByBirthday(@PathVariable(name = "birthday") String birthday) {
		List<Pending> PendingList = PendingDao.findByBirthday(birthday);
		return PendingList;
	}

	@RequestMapping("/pending/findAll")
	@ResponseBody
	public List<Pending> findAll(Pageable pageable) {
		// 這裏使用分頁
		// Pageable pageable = new PageRequest(page, size, sort);
		// Sort sort = new Sort(Sort.Direction.DESC, "id");
		List<Pending> PendingList = PendingDao.findAll(pageable);
		return PendingList;
	}

	@RequestMapping("/pending/save")
	@ResponseBody
	public Pending save(Pending Pending) {
		Pending u = PendingDao.save(Pending);
		return u;
	}

	@RequestMapping("/pending/delete")
	@ResponseBody
	public String delete(@RequestBody Map<String, String> params) {
		String mobile = params.get("mobile");

		List<Pending> list = PendingDao.findByMobile(mobile);
		Pending p = new Pending();
		if (list.size() > 0) {
			p = list.get(0);
			PendingDao.delete(p);
			return p.getMobile();
		}
		return Contanst.DELETEFAIL;
	}

}
