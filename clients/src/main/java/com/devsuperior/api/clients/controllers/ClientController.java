package com.devsuperior.api.clients.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsuperior.api.clients.entities.Client;
import com.devsuperior.api.clients.services.ClientService;

@RestController
@RequestMapping(value = "/clients")
public class ClientController {
	
	@Autowired
	private ClientService service;
	
	@GetMapping
	public List<Client> findAll() {
		return service.findAll();
	}

	@GetMapping(value = "/{id}")
	public Client findById(@PathVariable Long id) {
		return service.findById(id);
	}
}
