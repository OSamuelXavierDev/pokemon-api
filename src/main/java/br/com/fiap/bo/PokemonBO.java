package br.com.fiap.bo;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import br.com.fiap.dao.PokemonDAO;
import br.com.fiap.to.PokemonTO;

@Service
public class PokemonBO {
    private final PokemonDAO pokemonDAO;

    public PokemonBO(PokemonDAO pokemonDAO) {
        this.pokemonDAO = pokemonDAO;
    }

    public ArrayList<PokemonTO> findAll() {
        return pokemonDAO.findAll();
    }

    public PokemonTO save(PokemonTO pokemon) {
        return pokemonDAO.save(pokemon);
    }

    public PokemonTO findByCodigo(Long codigo) {
        return pokemonDAO.findByCodigo(codigo);
    }

    public boolean delete(Long codigo) {
        return pokemonDAO.delete(codigo);
    }

    public PokemonTO update(PokemonTO pokemon) {
        return pokemonDAO.update(pokemon);
    }
}
