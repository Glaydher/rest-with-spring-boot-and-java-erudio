package br.com.erudio.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.erudio.data.vo.v1.PersonVO;
import br.com.erudio.exceptions.ResourceNotFoundException;
import br.com.erudio.mapper.DozerMapper;
import br.com.erudio.model.Person;
import br.com.erudio.repository.PersonRepository;

@Service
public class PersonServices {

	private Logger logger = Logger.getLogger(PersonServices.class.getName());
	
	@Autowired
	private PersonRepository repo;

	public PersonVO findById(Long id) {
		logger.info("Finding one person");

		var entity = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		return DozerMapper.parseObject(entity, PersonVO.class);

	}

	public List<PersonVO> findAll() {
		logger.info("Finding all people");
		
		return DozerMapper.parseListObjects(repo.findAll(), PersonVO.class);
	}

	public PersonVO create(PersonVO person) {
		logger.info("Creating a person");
		var entity = DozerMapper.parseObject(person, Person.class);
		var vo = DozerMapper.parseObject(repo.save(entity), PersonVO.class);
		return vo;
	}

	public PersonVO update(PersonVO person) {
		logger.info("Updating a person");
		var entity = repo.findById(person.getId()).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		updateData(entity, person);
		var vo = DozerMapper.parseObject(repo.save(entity), PersonVO.class);
		return vo;
	}

	public void delete(Long id) {
		logger.info("Deleting a person");
		var entity = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		repo.delete(entity);
	}
	
	private void updateData(Person entity, PersonVO person) {
		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAddress(person.getAddress());
		entity.setGender(person.getGender());
		
	}
}
