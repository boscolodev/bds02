package com.devsuperior.bds02.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.bds02.dto.EventDTO;
import com.devsuperior.bds02.entities.City;
import com.devsuperior.bds02.entities.Event;
import com.devsuperior.bds02.repositories.EventRepository;
import com.devsuperior.bds02.services.exceptions.DatabaseException;
import com.devsuperior.bds02.services.exceptions.ResourceNotFoundException;

@Service
public class EventService {

	@Autowired
	private EventRepository repository;
	
	@Transactional
	public List<EventDTO> findAll(){
		List<Event> list = repository.findAll(Sort.by("name"));
		return list.stream()
				.map(x -> new EventDTO(x))
				.collect(Collectors.toList());
	}
	
	@Transactional
	public EventDTO insert(EventDTO cityDTO) {
		Event city = new Event();
		city.setName(cityDTO.getName());
		city = repository.save(city);
		return (new EventDTO(city));
	}


	public void delete(Long id) {
		try {
		repository.deleteById(id);
		}catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id Not Found: "+id);
		}catch(DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity Violation."+e);
		}
	}

	@Transactional
	public EventDTO update(Long id, EventDTO eventDTO) {
		try {
			City city = new City();
			city.setId(eventDTO.getCityId());

			Event event = repository.getOne(id);
			event.setDate(eventDTO.getDate());
			event.setName(eventDTO.getName());
			event.setUrl(eventDTO.getUrl());
			event.setCity(city);
			
			return new EventDTO(event);
			
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id Not Found" + id);
		}
	}
	
}
