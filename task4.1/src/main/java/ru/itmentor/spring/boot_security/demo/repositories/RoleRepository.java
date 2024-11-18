package ru.itmentor.spring.boot_security.demo.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.itmentor.spring.boot_security.demo.models.Role;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;


@Repository
@Transactional
public class RoleRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Role> findAll() {
        return entityManager.createQuery("from Role", Role.class).getResultList();
    }

    public Role findById(Long id) {
        return entityManager.find(Role.class, id);
    }

    public Role findByName(String name) {
        try {
            return entityManager.createQuery("from Role where name = :name", Role.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}