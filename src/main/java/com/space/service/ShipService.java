package com.space.service;

import com.space.model.Ship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;

public interface ShipService {
    public ShipService shipService = new ShipService() {

        @Override
        public Page findAll(Specification<Ship> specification, Pageable pageable) {
            return null;
        }

        @Override
        public long count(Specification<Ship> specification) {
            return 0;
        }

        @Override
        public ResponseEntity<?> create(Ship ship) {
            return null;
        }

        @Override
        public ResponseEntity<?> existsById(String id) {
            return null;
        }

        @Override
        public ResponseEntity<?> findById(String id) {
            return null;
        }

        @Override
        public ResponseEntity<?> deleteById(String id) {
            return null;
        }

        @Override
        public ResponseEntity<?> update(String id, Ship ship) {
            return null;
        }
    };

    Page findAll(Specification<Ship> specification, Pageable pageable);

    long count(Specification<Ship> specification);

    ResponseEntity<?> create(Ship ship);

    ResponseEntity<?> existsById(String id);

    ResponseEntity<?> findById(String id);

    ResponseEntity<?> deleteById(String id);

    ResponseEntity<?> update(String id, Ship ship);
}