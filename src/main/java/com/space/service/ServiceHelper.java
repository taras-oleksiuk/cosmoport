package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class ServiceHelper {
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static boolean validateShip(Ship ship) {

        if (ship.getName() == null
                || ship.getPlanet() == null
                || ship.getShipType() == null
                || ship.getProdDate() == null
                || ship.getSpeed() == null
                || ship.getCrewSize() == null)
            return false;

        if (ship.getUsed() == null) ship.setUsed(false);
        if (!validatingFields(ship)) return false;

        ship.setRating(calculationRating(ship));

        return true;
    }

    public static Double calculationRating(Ship ship) {
        return round(80 * ship.getSpeed() * (ship.getUsed() ? 0.5 : 1)
                / (3019 - ship.getProdDate().getYear() - 1900 + 1), 2);
    }

    public static Boolean validatingFields(Ship ship) {

        ship.setSpeed(round(ship.getSpeed(), 2));

        if (ship.getName().length() > 50 || ship.getPlanet().length() > 50
                || ship.getName().equals("") || ship.getPlanet().equals("")
                || ship.getSpeed() < 0.01 || ship.getSpeed() > 0.99
                || ship.getCrewSize() < 1 || ship.getCrewSize() > 9999
                || ship.getProdDate().getYear() + 1900 < 2800 || ship.getProdDate().getYear() + 1900 > 3019)
            return false;
        else return true;
    }

    public static List<Ship> findByPagingCriteria(ShipService shipService,
                                                  String name, String planet, ShipType shipType,
                                                  Long after, Long before,
                                                  Boolean isUsed,
                                                  Double minSpeed, Double maxSpeed,
                                                  Integer minCrewSize, Integer maxCrewSize,
                                                  Double minRating, Double maxRating,
                                                  ShipOrder shipOrder,
                                                  Integer pageNumber, Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page page = shipService.findAll(new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (name != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("name"), "%" + name + "%")));
                }
                if (planet != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("planet"), "%" + planet + "%")));
                }
                if (shipType != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("shipType"), shipType)));
                }
                if (after != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("prodDate"), new Date(after))));
                }
                if (before != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.lessThan(root.get("prodDate"), new Date(before))));
                }
                if (isUsed != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("isUsed"), isUsed)));
                }
                if (minSpeed != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("speed"), minSpeed)));
                }
                if (maxSpeed != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("speed"), maxSpeed)));
                }
                if (minCrewSize != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("crewSize"), minCrewSize)));
                }
                if (maxCrewSize != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("crewSize"), maxCrewSize)));
                }
                if (minRating != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("rating"), minRating)));
                }
                if (maxRating != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("rating"), maxRating)));
                }
                if (shipOrder != null) {
                    query.orderBy(criteriaBuilder.asc(root.get(shipOrder.getFieldName())));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        }, pageable);

        return page.getContent();
    }

    public static Integer countByCriteria(ShipService shipService,
                                          String name, String planet, ShipType shipType,
                                          Long after, Long before,
                                          Boolean isUsed,
                                          Double minSpeed, Double maxSpeed,
                                          Integer minCrewSize, Integer maxCrewSize,
                                          Double minRating, Double maxRating) {

        return (int) shipService.count(new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (name != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("name"), "%" + name + "%")));
                }
                if (planet != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("planet"), "%" + planet + "%")));
                }
                if (shipType != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("shipType"), shipType)));
                }
                if (after != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("prodDate"), new Date(after))));
                }
                if (before != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.lessThan(root.get("prodDate"), new Date(before))));
                }
                if (isUsed != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("isUsed"), isUsed)));
                }
                if (minSpeed != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("speed"), minSpeed)));
                }
                if (maxSpeed != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("speed"), maxSpeed)));
                }
                if (minCrewSize != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("crewSize"), minCrewSize)));
                }
                if (maxCrewSize != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("crewSize"), maxCrewSize)));
                }
                if (minRating != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("rating"), minRating)));
                }
                if (maxRating != null) {
                    predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("rating"), maxRating)));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        });
    }

}