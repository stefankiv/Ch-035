package com.crsms.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.crsms.util.Command;

@Transactional
public interface BaseService<E> {

	void save(E entity);
	
	List<E> getAll();

	E getById(Long id);
	
	E getById(Long id, Command ... initializers);

	void update(E entity);
	
	void delete(E entity);
	
}
