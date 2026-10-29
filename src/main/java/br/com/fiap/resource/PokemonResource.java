package br.com.fiap.resource;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.bo.PokemonBO;
import br.com.fiap.to.PokemonTO;

@RestController
@RequestMapping("/pokemons")
public class PokemonResource {
    private final PokemonBO pokemonBO;

    public PokemonResource(PokemonBO pokemonBO) {
        this.pokemonBO = pokemonBO;
    }

    @GetMapping
    public ResponseEntity<List<PokemonTO>> findAll() {
        List<PokemonTO> pokemons = pokemonBO.findAll();
        if (pokemons != null && !pokemons.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(pokemons);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping
    public ResponseEntity<PokemonTO> save(@Valid @RequestBody PokemonTO pokemon) {
        PokemonTO savedPokemon = pokemonBO.save(pokemon);
        if (savedPokemon == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPokemon);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<?> findByCodigo(@PathVariable Long codigo) {
        PokemonTO pokemon = pokemonBO.findByCodigo(codigo);
        if (pokemon != null) {
            return ResponseEntity.ok(pokemon);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pokémon não encontrado!");
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<String> delete(@PathVariable Long codigo) {
        if (pokemonBO.delete(codigo)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pokémon não encontrado!");
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<PokemonTO> update(
            @PathVariable Long codigo,
            @Valid @RequestBody PokemonTO pokemon) {
        pokemon.setCodigo(codigo);
        PokemonTO updatedPokemon = pokemonBO.update(pokemon);
        if (updatedPokemon == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedPokemon);
    }
}
