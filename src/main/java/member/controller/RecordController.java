package member.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import member.dao.RecordDao;
import member.entity.Record;
import member.service.CommonService;
import member.utils.DateUtils;

/**
 * 修改记录
 * 
 * @author zhaokunyang
 *
 */
@RestController
public class RecordController {
	@Autowired
	private CommonService commonService;
	@Autowired
	private RecordDao recordDao;

	@RequestMapping(value = "/record/operate")
	public String findByName(@RequestBody Map<String, String> params) {
		String result = commonService.operate(params);
		return result;

	}

	// @RequestMapping(value = "/record/operateQq")
	// public String operateQq(@RequestBody Map<String,String> params) {
	// String result = commonService.operate(params);
	// return result;
	//
	// }

	@RequestMapping(value = "/record/regular")
	public String regular(@RequestBody Map<String, String> params) throws Exception {
		String result = commonService.regular(params);
		return result;

	}

	@RequestMapping("/record/findAll")
	@ResponseBody
	public List<Record> findAll() {
		// Pageable pageable = new PageRequest(page, size, sort);
		// Sort sort = new Sort(Sort.Direction.DESC, "id");
		List<Record> recordList = recordDao.findAll();
		if (recordList.size() > 0) {
			for (Record record : recordList) {
				record.setSendtime(DateUtils.timeSwitch(record.getSendtime()));
			}
		}
		return recordList;
	}

	

}
