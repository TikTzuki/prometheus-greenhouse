package tik.prometheus.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tik.prometheus.rest.models.Farm;
import tik.prometheus.rest.repositories.FarmRepos;

@RestControllerAdvice
@RequestMapping("/farms")
public class FarmController {
    @Autowired
    FarmRepos farmRepos;

    @PostMapping()
    public ResponseEntity<Farm> post(Farm farm) {
        farmRepos.save(farm);
        return new ResponseEntity<>(farm, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<Page<Farm>> all(Pageable pageable) {
        return ResponseEntity.ok(farmRepos.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Farm> one(@PathVariable Long id) {
        return farmRepos.findById(id).
                map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Farm> replace(@PathVariable Long id, @RequestBody Farm updateFarm) {
        return farmRepos.findById(id).map(farm -> {
            farm.setRegion(updateFarm.getRegion());
            farm.setAreOfFarm(updateFarm.getAreOfFarm());
            farm.setNumberOfGreenhouse(updateFarm.getNumberOfGreenhouse());
            return ResponseEntity.ok(farmRepos.save(farm));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        farmRepos.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
