package ru.ibs.crud.tests.testdata.repo;

import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import ru.ibs.crud.tests.testdata.entity.TestEntity;
import ru.ibs.crud.tests.testdata.model.TestDto;

import java.util.*;

/* *
*
* Мок репозиторий, который хранит данные в памяти.
* Реализованы не все методы, а только те которые используются в тестах.
*
* */
public class MockRepoImpl implements MockRepo {

    private final HashMap<Long, TestEntity> entityStore = new HashMap<>();
    @Getter private Long id = 1L;

    @SneakyThrows
    @Override
    public <S extends TestEntity> S save(S s) {
        if (entityStore.containsKey(s.getId())) entityStore.replace(s.getId(), s);
        if (s.getId() !=null){
        entityStore.put(s.getId(), s);
        }
        else {
            s.setId(id);
            entityStore.put(id, s);
            id++;
          }
        return s;
    }


    public void update(TestDto s, Long id) {
        if (id == null || id < 1) throw new IllegalArgumentException("Не указан Id");
        if (s == null) throw new NullPointerException();
        if (entityStore.containsKey(s.getId()))
            entityStore.remove(id);
            s.setId(id);
            entityStore.put(id, s);
    }

    @Override
    public TestEntity getById(Long aLong) {
        return entityStore.get(aLong);
    }
    public TestEntity read(Long aLong) {
        return getById(aLong);
    }

    @SneakyThrows
    @Override
    public void deleteById(Long aLong) {
        if(entityStore.containsKey(aLong)) entityStore.remove(aLong);
        else throw new Exception("Error: this id not found in the Entity Store");
        if (id > 1) id--;
        }

    @Override
    public Optional<TestEntity> findById(Long aLong) {
        return Optional.of(entityStore.get(aLong));
    }


    @Override
    public List<TestEntity> findAll() {
        return new ArrayList<>(entityStore.values());
    }

    @Override
    public <S extends TestEntity> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public void delete(TestEntity testExampleEntity) {

    }

    @Override
    public List<TestEntity> findAll(Sort sort){
        List<TestEntity> entityList = new ArrayList<>(entityStore.values());
        Comparator<TestEntity> comparator = Comparator.comparing(TestEntity::getExampleFieldOne);
        entityList.sort(comparator);
        return entityList;
    }

    @Override
    public Page<TestEntity> findAll(Pageable pageable) {
        return new PageImpl<>(new ArrayList<>(entityStore.values()));
    }

    @Override
    public List<TestEntity> findAllById(Iterable<Long> iterable) {
        return null;
    }

    @Override
    public <S extends TestEntity> List<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends TestEntity> S saveAndFlush(S s) {
        return null;
    }

    @Override
    public <S extends TestEntity> List<S> saveAllAndFlush(Iterable<S> iterable) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<TestEntity> iterable) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> iterable) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public TestEntity getOne(Long aLong) {
        return null;
    }



    @Override
    public <S extends TestEntity> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends TestEntity> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }







    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public long count() {
        return 0;
    }



    @Override
    public void deleteAllById(Iterable<? extends Long> iterable) {

    }

    @Override
    public void deleteAll(Iterable<? extends TestEntity> iterable) {

    }

    @Override
    public void deleteAll() {
        entityStore.clear();
        id = 1L;
    }



    @Override
    public <S extends TestEntity> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends TestEntity> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends TestEntity> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public Optional<TestEntity> findOne(Specification<TestEntity> spec) {
        return Optional.empty();
    }

    @Override
    public List<TestEntity> findAll(Specification<TestEntity> spec) {
        return null;
    }

    @Override
    public Page<TestEntity> findAll(Specification<TestEntity> spec, Pageable pageable) {
        return new PageImpl<>(new ArrayList<>(entityStore.values()));
    }

    @Override
    public List<TestEntity> findAll(Specification<TestEntity> spec, Sort sort) {
        return new ArrayList<>(entityStore.values());
    }

    @Override
    public long count(Specification<TestEntity> spec) {
        return 0;
    }
}
