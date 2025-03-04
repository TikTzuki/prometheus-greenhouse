package tik.prometheus.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tik.prometheus.rest.constants.GreenhouseType;
import tik.prometheus.rest.dtos.GreenhouseDTO;
import tik.prometheus.rest.dtos.GreenhouseLiteDTO;
import tik.prometheus.rest.models.Greenhouse;
import tik.prometheus.rest.repositories.GreenhouseRepos;
import tik.prometheus.rest.services.GreenhouseService;

@RestControllerAdvice
@RequestMapping("/greenhouses")
public class GreenhouseController {
    @Autowired
    GreenhouseRepos repos;

    @Autowired
    GreenhouseService service;

    @PostMapping()
    public ResponseEntity<Greenhouse> post(Greenhouse greenhouse) {
        repos.save(greenhouse);
        return new ResponseEntity<>(greenhouse, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<Page<GreenhouseLiteDTO>> all(
            @RequestParam(required = false) Long farmId,
            @RequestParam(required = false) String label,
            @RequestParam(required = false) GreenhouseType type,
            Pageable pageable) {
        return ResponseEntity.ok(service.getGreenhouses(farmId, label, type, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GreenhouseDTO> one(@PathVariable Long id) {
        return ResponseEntity.ok(service.getGreenhouse(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GreenhouseDTO> replace(@PathVariable Long id, @RequestBody GreenhouseDTO updateGH) {
        return ResponseEntity.ok(service.updateGreenhouse(id, updateGH));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        repos.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
