package br.com.fiap.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Connection;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import br.com.fiap.to.PokemonTO;

@Repository
public class PokemonDAO {
    public ArrayList<PokemonTO> findAll() {
        ArrayList<PokemonTO> pokemons = new ArrayList<>();
        String sql = "SELECT CODIGO, NOME, ALTURA, PESO, CATEGORIA, DATA_DA_CAPTURA "
                + "FROM POKEMON ORDER BY CODIGO";
        Connection connection = ConnectionFactory.getConnection();
        if (connection == null) {
            return null;
        }

        try (PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Date dataDaCaptura = resultSet.getDate("DATA_DA_CAPTURA");
                LocalDate data = dataDaCaptura == null ? null : dataDaCaptura.toLocalDate();
                pokemons.add(new PokemonTO(
                        resultSet.getLong("CODIGO"),
                        resultSet.getString("NOME"),
                        resultSet.getObject("ALTURA", Double.class),
                        resultSet.getObject("PESO", Double.class),
                        resultSet.getString("CATEGORIA"),
                        data));
            }
            return pokemons;
        } catch (SQLException exception) {
            System.out.println("Erro ao consultar pokémons: " + exception.getMessage());
            return null;
        } finally {
            ConnectionFactory.closeConnection();
        }
    }

    public PokemonTO findByCodigo(Long codigo) {
        String sql = "SELECT CODIGO, NOME, ALTURA, PESO, CATEGORIA, DATA_DA_CAPTURA "
                + "FROM POKEMON WHERE CODIGO = ?";
        Connection connection = ConnectionFactory.getConnection();
        if (connection == null) {
            return null;
        }

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, codigo);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Date dataDaCaptura = resultSet.getDate("DATA_DA_CAPTURA");
                    LocalDate data = dataDaCaptura == null ? null : dataDaCaptura.toLocalDate();
                    return new PokemonTO(
                            resultSet.getLong("CODIGO"),
                            resultSet.getString("NOME"),
                            resultSet.getObject("ALTURA", Double.class),
                            resultSet.getObject("PESO", Double.class),
                            resultSet.getString("CATEGORIA"),
                            data);
                }
            }
        } catch (SQLException exception) {
            System.out.println("Erro na consulta: " + exception.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
        return null;
    }

    public PokemonTO save(PokemonTO pokemon) {
        String sql = "INSERT INTO POKEMON (CODIGO, NOME, ALTURA, PESO, CATEGORIA, DATA_DA_CAPTURA) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        Connection connection = ConnectionFactory.getConnection();
        if (connection == null) {
            return null;
        }
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, pokemon.getCodigo());
            statement.setString(2, pokemon.getNome());
            statement.setObject(3, pokemon.getAltura());
            statement.setObject(4, pokemon.getPeso());
            statement.setString(5, pokemon.getCategoria());
            statement.setDate(6, Date.valueOf(pokemon.getDataDaCaptura()));
            if (statement.executeUpdate() > 0) {
                return pokemon;
            }
        } catch (SQLException exception) {
            System.out.println("Erro ao salvar: " + exception.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
        return null;
    }

    public PokemonTO update(PokemonTO pokemon) {
        String sql = "UPDATE POKEMON SET NOME = ?, ALTURA = ?, PESO = ?, "
                + "CATEGORIA = ?, DATA_DA_CAPTURA = ? WHERE CODIGO = ?";
        Connection connection = ConnectionFactory.getConnection();
        if (connection == null) {
            return null;
        }

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, pokemon.getNome());
            statement.setObject(2, pokemon.getAltura());
            statement.setObject(3, pokemon.getPeso());
            statement.setString(4, pokemon.getCategoria());
            statement.setDate(5, Date.valueOf(pokemon.getDataDaCaptura()));
            statement.setLong(6, pokemon.getCodigo());
            if (statement.executeUpdate() > 0) {
                return pokemon;
            }
        } catch (SQLException exception) {
            System.out.println("Erro ao atualizar: " + exception.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
        return null;
    }

    public boolean delete(Long codigo) {
        String sql = "DELETE FROM POKEMON WHERE CODIGO = ?";
        Connection connection = ConnectionFactory.getConnection();
        if (connection == null) {
            return false;
        }

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, codigo);
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            System.out.println("Erro ao excluir: " + exception.getMessage());
        } finally {
            ConnectionFactory.closeConnection();
        }
        return false;
    }
}
