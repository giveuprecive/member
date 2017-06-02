package member.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import member.entity.Record;

@Transactional
public interface RecordDao extends CrudRepository<Record, Integer> {

	@SuppressWarnings("unchecked")
	public Record save(Record record);
	public List<Record> findAll();
	public List<Record> findByMobile(String mobile);
	public List<Record> findByMobile(String mobile,Pageable  pr);

}
