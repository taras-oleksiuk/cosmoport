package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ServiceHelper;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ShipController {

    private ShipService shipService;

    @Autowired
    public void setShipService(ShipService shipService) {
        this.shipService = shipService;
    }

    @GetMapping(value = "/rest/ships")
    public ResponseEntity<?> getAllBy(@RequestParam(value = "name", required = false) String name,
                                      @RequestParam(value = "planet", required = false) String planet,
                                      @RequestParam(value = "shipType", required = false) ShipType shipType,
                                      @RequestParam(value = "after", required = false) Long after,
                                      @RequestParam(value = "before", required = false) Long before,
                                      @RequestParam(value = "isUsed", required = false) Boolean isUsed,
                                      @RequestParam(value = "minSpeed", required = false) Double minSpeed,
                                      @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
                                      @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
                                      @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
                                      @RequestParam(value = "minRating", required = false) Double minRating,
                                      @RequestParam(value = "maxRating", required = false) Double maxRating,
                                      @RequestParam(value = "order", required = false) ShipOrder shipOrder,
                                      @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
                                      @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize) {

        List<Ship> shipList = ServiceHelper.findByPagingCriteria(shipService,
                name, planet, shipType,
                after, before,
                isUsed,
                minSpeed, maxSpeed,
                minCrewSize, maxCrewSize,
                minRating, maxRating,
                shipOrder,
                pageNumber, pageSize);

        return new ResponseEntity<>(shipList, HttpStatus.OK);
    }

    @GetMapping(value = "/rest/ships/count")
    public ResponseEntity<?> getCount(@RequestParam(value = "name", required = false) String name,
                                      @RequestParam(value = "planet", required = false) String planet,
                                      @RequestParam(value = "shipType", required = false) ShipType shipType,
                                      @RequestParam(value = "after", required = false) Long after,
                                      @RequestParam(value = "before", required = false) Long before,
                                      @RequestParam(value = "isUsed", required = false) Boolean isUsed,
                                      @RequestParam(value = "minSpeed", required = false) Double minSpeed,
                                      @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
                                      @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
                                      @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
                                      @RequestParam(value = "minRating", required = false) Double minRating,
                                      @RequestParam(value = "maxRating", required = false) Double maxRating) {

        int count = ServiceHelper.countByCriteria(shipService,
                name, planet, shipType,
                after, before,
                isUsed,
                minSpeed, maxSpeed,
                minCrewSize, maxCrewSize,
                minRating, maxRating);

        return new ResponseEntity<>(count, HttpStatus.OK);

    }

    @PostMapping(value = "/rest/ships")
    public ResponseEntity<?> createShip(@RequestBody Ship ship) {
        return shipService.create(ship);
    }

    @GetMapping(value = "/rest/ships/{id}")
    public ResponseEntity<?> findShipById(@PathVariable String id) {
        return shipService.findById(id);
    }

    @DeleteMapping(value = "/rest/ships/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        return shipService.deleteById(id);
    }

    @PostMapping(value = "/rest/ships/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody Ship ship) {
        ResponseEntity<?> responseEntity = shipService.update(id, ship);
        Ship ship1 = (Ship) responseEntity.getBody();
        return responseEntity;
    }


}