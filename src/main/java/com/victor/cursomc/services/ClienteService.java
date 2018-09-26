package com.victor.cursomc.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.victor.cursomc.domain.Cidade;
import com.victor.cursomc.domain.Cliente;
import com.victor.cursomc.domain.Endereco;
import com.victor.cursomc.domain.enums.Perfil;
import com.victor.cursomc.domain.enums.TipoCliente;
import com.victor.cursomc.dto.ClienteDTO;
import com.victor.cursomc.dto.ClienteNewDTO;
import com.victor.cursomc.repositories.ClienteRepository;
import com.victor.cursomc.repositories.EnderecoRepository;
import com.victor.cursomc.security.UserSS;
import com.victor.cursomc.services.exceptions.AuthorizationException;
import com.victor.cursomc.services.exceptions.DataIntegrityException;
import com.victor.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private EnderecoRepository enderecoRepo;
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private ImageService imageService;
	
	public Cliente find(Integer id) 
	{
		UserSS userSS = UserService.authenticated();
		if(userSS == null || !((UserSS) userSS).hasRole(Perfil.ADMIN) && !id.equals(userSS.getId())) 
		{
			throw new AuthorizationException("Acesso negado!");
		}
			
		
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() ->  new ObjectNotFoundException
				("Objeto não encontrado! Id: "+id+", Tipo: "+Cliente.class.getName()));
	}
	
	public List<Cliente> findAll() {
		return repo.findAll(); 
	}
	
	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		enderecoRepo.saveAll(obj.getEnderecos());
		return repo.save(obj);
	}

	@Transactional
	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId());
		updateData(newObj,obj);
		return repo.save(newObj);
	}

	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
		
	}

	public void delete(Integer id) {
		find(id);
		try {
			repo.deleteById(id);

		}
		catch(DataIntegrityViolationException e) 
		{
			throw new DataIntegrityException("Não é possível excluir um Cliente porque há pedidos relacionados");
		}
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction),orderBy);
		return repo.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO objDto) 
	{
		return new Cliente(objDto.getId(),objDto.getNome(),objDto.getEmail(), null,null, null);
	}
	
	public Cliente fromDTO(ClienteNewDTO objDto) 
	{
		Cliente cli = new Cliente
				(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOrCnpj(), TipoCliente.toEnum(objDto.getTipo()), pe.encode(objDto.getSenha()));
		Cidade cid = new Cidade
				(objDto.getCidadeId(),null,null);
		Endereco end = new Endereco
				(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(), objDto.getBairro(), objDto.getCep(), cli, cid);
		
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDto.getTelefone1());
		
		if(objDto.getTelefone2() != null)
			cli.getTelefones().add(objDto.getTelefone2());
		if(objDto.getTelefone3() != null)
			cli.getTelefones().add(objDto.getTelefone3());
		return cli;
	}

	public String uploadProfilePicture(MultipartFile multipartFile) 
	{
		return imageService.uploadFile(multipartFile);
	}

}
