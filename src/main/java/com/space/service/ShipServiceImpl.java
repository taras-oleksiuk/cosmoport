package com.space.service;

import com.space.model.Ship;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service("shipService")
@Repository
@Transactional
public class ShipServiceImpl implements ShipService {

    private ShipRepository shipRepository;

    @Autowired
    public void setShipRepository(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    @Override
    public Page<Ship> findAll(Specification<Ship> specification, Pageable pageable) {
        return shipRepository.findAll(specification, pageable);
    }

    @Override
    public long count(Specification<Ship> specification) {
        return shipRepository.count(specification);
    }

    @Override
    public ResponseEntity<?> create(Ship ship) {
        if (!ServiceHelper.validateShip(ship)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Ship createShip = shipRepository.save(ship);
        return new ResponseEntity<>(createShip, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> findById(String idString) {
        Long id;
        if ((id = validateId(idString)) == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<Ship> ship = shipRepository.findById(id);

        if (!ship.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(ship.get(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> existsById(String idString) {
        Long id;
        if ((id = validateId(idString)) == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(shipRepository.existsById(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleteById(String idString) {
        Long id;
        if ((id = validateId(idString)) == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (!shipRepository.existsById(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else {
            shipRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<?> update(String idString, Ship ship) {
        //Если id не валидный
        Long id;
        if ((id = validateId(idString)) == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        //Если элемента с таким id нет в базе
        if (!shipRepository.existsById(id))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else {
            Ship shipFromBase = shipRepository.findById(id).get();

            // Если тело запроса пустое
            if (ship.getName() == null && ship.getPlanet() == null
                    && ship.getShipType() == null && ship.getProdDate() == null
                    && ship.getUsed() == null && ship.getSpeed() == null && ship.getRating() == null)
                //Возвращяем корабль из базы
                ship = shipFromBase;
            else {
                //Если поля не валидные
                if (ship.getSpeed() != null) ship.setSpeed(ServiceHelper.round(ship.getSpeed(), 2));

                if ((ship.getName() != null && (ship.getName().length() > 50 || ship.getName().equals("")))
                        || (ship.getPlanet() != null && (ship.getPlanet().length() > 50 || ship.getPlanet().equals("")))
                        || (ship.getSpeed() != null && (ship.getSpeed() < 0.01 || ship.getSpeed() > 0.99))
                        || (ship.getCrewSize() != null && (ship.getCrewSize() < 1 || ship.getCrewSize() > 9999))
                        || (ship.getProdDate() != null && (ship.getProdDate().getYear() + 1900 < 2800 || ship.getProdDate().getYear() + 1900 > 3019)))
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

                //Заменяем непустыми значениями
                if (ship.getName() != null) shipFromBase.setName(ship.getName());
                if (ship.getPlanet() != null) shipFromBase.setPlanet(ship.getPlanet());
                if (ship.getShipType() != null) shipFromBase.setShipType(ship.getShipType());
                if (ship.getProdDate() != null) shipFromBase.setProdDate(ship.getProdDate());
                if (ship.getUsed() != null) shipFromBase.setUsed(ship.getUsed());
                if (ship.getSpeed() != null) shipFromBase.setSpeed(ship.getSpeed());
                if (ship.getCrewSize() != null) shipFromBase.setCrewSize(ship.getCrewSize());
                shipFromBase.setRating(ServiceHelper.calculationRating(shipFromBase));

                shipRepository.save(shipFromBase);
            }
            return new ResponseEntity<>(shipFromBase, HttpStatus.OK);
        }
    }

    private Long validateId(String id) {
        try {
            Long idLong = Long.parseLong(id);
            if (idLong <= 0) return null;
            else return idLong;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}