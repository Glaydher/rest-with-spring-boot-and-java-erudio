package br.com.erudio.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import br.com.erudio.controllers.PersonController;
import br.com.erudio.data.vo.v1.PersonVO;
import br.com.erudio.data.vo.v2.PersonVOV2;
import br.com.erudio.exceptions.RequiredObjectIsNullException;
import br.com.erudio.exceptions.ResourceNotFoundException;
import br.com.erudio.mapper.DozerMapper;
import br.com.erudio.mapper.custom.PersonMapper;
import br.com.erudio.model.Person;
import br.com.erudio.repository.PersonRepository;
import jakarta.transaction.Transactional;

@Service
public class PersonServices {

	private Logger logger = Logger.getLogger(PersonServices.class.getName());
	
	@Autowired
	private PersonRepository repo;
	
	@Autowired
	PagedResourcesAssembler<PersonVO> assembler;
	
	@Autowired
	private PersonMapper mapper;

	public PagedModel<EntityModel<PersonVO>> findAll(Pageable pageable) {
		logger.info("Finding all people");
		
		var personPage = repo.findAll(pageable);
		
		var personVOsPage = personPage.map(p -> DozerMapper.parseObject(p, PersonVO.class));
		personVOsPage.map(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));
		
		Link link = linkTo(
				methodOn(PersonController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();
		return assembler.toModel(personVOsPage, link );
	}
	
	public PagedModel<EntityModel<PersonVO>> findPersonByName(String firstName, Pageable pageable) {
		logger.info("Finding all people");
		
		var personPage = repo.findPersonByName(firstName, pageable);
		
		var personVOsPage = personPage.map(p -> DozerMapper.parseObject(p, PersonVO.class));
		personVOsPage.map(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));
		
		Link link = linkTo(
				methodOn(PersonController.class)
				.findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();
		return assembler.toModel(personVOsPage, link );
	}
	
	public PersonVO findById(Long id) {
		logger.info("Finding one person");

		var entity = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		var vo = DozerMapper.parseObject(entity, PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		return vo;
	}
	
	public PersonVO create(PersonVO person) {
		
		if (person == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Creating a person");
		var entity = DozerMapper.parseObject(person, Person.class);
		var vo = DozerMapper.parseObject(repo.save(entity), PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}
	
	public PersonVOV2 createV2(PersonVOV2 person) {
		logger.info("Creating a person with V2!");
		var entity = mapper.convertVoToEntity(person);
		var vo = mapper.convertEntityToVo(repo.save(entity));
		return vo;
	}

	public PersonVO update(PersonVO person) {
		
		if (person == null) {
			throw new RequiredObjectIsNullException();
		}
		logger.info("Updating a person");
		var entity = repo.findById(person.getKey()).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		updateData(entity, person);
		var vo = DozerMapper.parseObject(repo.save(entity), PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}
	
	@Transactional
	public PersonVO disablePerson(Long id) {
		logger.info("Disabling one person");
		
		repo.disablePerson(id);

		var entity = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		var vo = DozerMapper.parseObject(entity, PersonVO.class);
		vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
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
