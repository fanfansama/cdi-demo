package fr.stepinfo.tomee.jpa.service;


import fr.stepinfo.tomee.jpa.model.ContentEntity;
import fr.stepinfo.tomee.jpa.model.UserEntity;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.security.Principal;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static java.util.stream.Collectors.toMap;

@ApplicationScoped
public class ContentService {

    @Inject
    private EntityManager em;

    @Inject
    @RequestScoped
    private Principal principal;

    @Inject
    private Logger LOG;

    @Transactional
    public ContentEntity save(final ContentEntity slot) {
        em.persist(slot);
        return slot;
    }

    @Transactional
    public ContentEntity likeContent(final String contentId) {
        ContentEntity content = findById(contentId);
        if (content == null) {
            LOG.info("create " + contentId);
            content = save(new ContentEntity(contentId));
        }

        UserEntity user = findUser(principal.getName());
        if (user == null) {
            LOG.info("create user with " + principal.getName());
            user = createUser();
        }
        if (isUserLikingToContent(content, user)) {
            LOG.info(principal.getName() + " already like " + content.getId());
            return content;
        }

        user.getContents().add(content);
        return content;
    }

    @Transactional
    public ContentEntity unlikeContent(final String contentId) {
        final ContentEntity entity = findById(contentId);
        if (entity == null) {
            return null;
        }

        UserEntity attendee = findUser(principal.getName());
        if (attendee == null || !isUserLikingToContent(entity, attendee)) {
            return entity;
        }

        attendee.getContents().remove(entity);
        return entity;
    }

    @Transactional
    public ContentEntity findById(final String id) {
        return em.find(ContentEntity.class, id);
    }

    public int count(final ContentEntity content) {
        return em.createNamedQuery("User.countByContent", Number.class)
                .setParameter("content", content)
                .getSingleResult()
                .intValue();
    }

    @Transactional
    public Collection<String> getLikedContentIdsForUser(String name) {
        Collection<String> contentIds = new LinkedList<>();
        UserEntity attendee = findUser(name);
        if (attendee != null) {
            attendee.getContents().forEach(contentEntity -> contentIds.add(contentEntity.getId()));
        }
        return contentIds;
    }

    public Map<String, Integer> getUsersCountGroupedByContentId() {
        return em.createNamedQuery("User.countGroupByAllContent", Object[].class)
                .getResultList().stream()
                .collect(toMap(tuple -> (String) tuple[0], tuple -> Number.class.cast(tuple[1]).intValue()));
    }

    private boolean isUserLikingToContent(final ContentEntity content, final UserEntity user) {
        return em.createNamedQuery("User.findByNameAndContent", Number.class)
                .setParameter("name", user.getName())
                .setParameter("content", content)
                .getSingleResult()
                .intValue() > 0;
    }

    private UserEntity findUser(String name) {
        try {
            return em.createNamedQuery("User.findByName", UserEntity.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (final NoResultException nre) {
            return null;
        }
    }

    public List<ContentEntity> findAll() {
        try {
            return em.createNamedQuery("Content.findAll", ContentEntity.class)
                    .getResultList();
        } catch (final NoResultException nre) {
            return null;
        }
    }

    private UserEntity createUser() {
        final UserEntity entity = new UserEntity();
        entity.setName(principal.getName());
        em.persist(entity);
        return entity;
    }

}
