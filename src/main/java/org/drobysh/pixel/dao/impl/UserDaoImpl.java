package org.drobysh.pixel.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.drobysh.pixel.dao.UserDao;
import org.drobysh.pixel.dto.UserSearchCriteria;
import org.drobysh.pixel.model.EmailData;
import org.drobysh.pixel.model.PhoneData;
import org.drobysh.pixel.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void removeEmail(Long id, String email) {
        entityManager.createQuery("DELETE FROM EmailData e WHERE e.email = :email AND e.user.id = :id")
                .setParameter("email", email)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public List<User> findUsersByFilters(UserSearchCriteria criteria, int offset, int size) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> user = query.from(User.class);

        buildCriteria(criteria,cb,user,query);

        return entityManager.createQuery(query)
                .setFirstResult(offset)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public Long countUsersByFilters(UserSearchCriteria criteria) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<User> user = query.from(User.class);

        query.select(cb.count(user));

        buildCriteria(criteria,cb,user,query);

        return entityManager.createQuery(query).getSingleResult();
    }

    @Override
    @Transactional
    public void removePhone(Long id, String phone) {
        entityManager.createQuery("DELETE FROM PhoneData p WHERE p.phone = :phone AND p.user.id = :id")
                .setParameter("phone", phone)
                .setParameter("id", id)
                .executeUpdate();
    }

    public Optional<User> findByEmail(String email) {
            return Optional.ofNullable(
                    entityManager.createQuery(
                                    "SELECT u FROM User u JOIN u.emails e WHERE e.email = :email",
                                    User.class)
                        .setParameter("email", email)
                        .getSingleResult()
            );
    }

    @Override
    public Optional<User> findByPhone(String phone) {
        return Optional.ofNullable(
                entityManager.createQuery(
                        "SELECT u FROM User u JOIN u.phones e WHERE e.phone = :phone", User.class)
                    .setParameter("phone", phone)
                    .getSingleResult()
        );
    }

    @Override
    public Optional<List<String>> findEmailsByUserId(Long id) {
        List<String> emails = entityManager.createQuery(
                        "SELECT e.email FROM EmailData e WHERE e.user.id = :userId", String.class)
                .setParameter("userId", id)
                .getResultList();

        return emails.isEmpty() ? Optional.empty() : Optional.of(emails);
    }

    @Override
    public Optional<List<String>> findPhonesByUserId(Long id) {
        List<String> phones = entityManager.createQuery(
                        "SELECT p.phone FROM PhoneData p WHERE p.user.id = :userId", String.class)
                .setParameter("userId", id)
                .getResultList();

        return phones.isEmpty() ? Optional.empty() : Optional.of(phones);
    }

    @Override
    @Transactional
    public Optional<EmailData> findEmail(Long userId, String email) {
        return Optional.ofNullable(entityManager.createQuery(
                        "SELECT e FROM EmailData e " +
                                "WHERE e.user.id = :userId AND e.email = :email",
                        EmailData.class)
                .setParameter("userId", userId)
                .setParameter("email", email)
                .getSingleResult());
    }

    @Override
    public Optional<PhoneData> findPhone(Long userId, String phone) {
        return Optional.ofNullable(entityManager.createQuery(
                        "SELECT p FROM PhoneData p " +
                                "WHERE p.user.id = :userId AND p.phone = :phone",
                        PhoneData.class)
                .setParameter("userId", userId)
                .setParameter("phone", phone)
                .getSingleResult());
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return Optional.ofNullable(entityManager.createQuery(
                "SELECT u FROM User u WHERE u.id =:id  ", User.class)
                .setParameter("id", id)
                .getSingleResult());
    }

    @Override
    public Boolean existsByEmail(String email) {
        return entityManager.createQuery(
                        "SELECT COUNT(e) > 0 FROM EmailData e WHERE e.email = :email",
                        Boolean.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    @Override
    public Boolean existsByPhone(String phone) {
        return entityManager.createQuery(
                        "SELECT COUNT(p) > 0 FROM PhoneData p WHERE p.phone = :phone",
                        Boolean.class)
                .setParameter("phone", phone)
                .getSingleResult();
    }

    @Override
    @Transactional
    public void save(EmailData emailData) {
        entityManager.merge(emailData);
        entityManager.flush();
    }

    @Override
    @Transactional
    public void save(PhoneData phoneData) {
        entityManager.merge(phoneData);
        entityManager.flush();
    }

    private void buildCriteria(UserSearchCriteria criteria, CriteriaBuilder cb, Root<User> user, CriteriaQuery<?> query ) {
        List<Predicate> predicates = new ArrayList<>();

        if (criteria.getDateOfBirth() != null) {
            predicates.add(cb.greaterThan(user.get("dateOfBirth"), criteria.getDateOfBirth()));
        }

        if (criteria.getPhone() != null) {
            predicates.add(cb.equal(user.get("phone"), criteria.getPhone()));
        }

        if (criteria.getName() != null) {
            predicates.add(cb.like(user.get("name"), criteria.getName() + "%"));
        }

        if (criteria.getEmail() != null) {
            predicates.add(cb.equal(user.get("email"), criteria.getEmail()));
        }

        query.where(predicates.toArray(new Predicate[0]));
    }
}
