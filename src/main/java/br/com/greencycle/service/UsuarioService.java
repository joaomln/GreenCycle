package br.com.greencycle.service;

import br.com.greencycle.model.Usuario;
import br.com.greencycle.repository.UsuarioRepository;
import br.com.greencycle.util.MessagesBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private MessagesBean messages;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * @return a list of all customers
     */
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario create(Usuario usuario) {
        if (usuarioRepository.existsById(usuario.getIdUsuario())) {
            throw new EntityExistsException(messages.get("customerAlreadyExists"));
        }
        return usuarioRepository.save(usuario);
    }

    public boolean existsById(Integer id) {
        return usuarioRepository.existsById(id);
    }
}
