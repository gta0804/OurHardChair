package fudan.se.lab2.repository;

import fudan.se.lab2.domain.Authority;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("authorityRepository")
public class AuthorityRepositoryImps implements AuthorityRepository{
    @Override
    public Authority findByAuthority(String authority) {
        return null;
    }

    @Override
    public <S extends Authority> S save(S s) {
        return null;
    }

    @Override
    public <S extends Authority> Iterable<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<Authority> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<Authority> findAll() {
        return null;
    }

    @Override
    public Iterable<Authority> findAllById(Iterable<Long> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Authority authority) {

    }

    @Override
    public void deleteAll(Iterable<? extends Authority> iterable) {

    }

    @Override
    public void deleteAll() {

    }
}
