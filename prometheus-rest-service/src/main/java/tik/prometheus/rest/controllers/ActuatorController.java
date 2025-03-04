package tik.prometheus.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tik.prometheus.rest.dtos.ActuatorDTO;
import tik.prometheus.rest.dtos.ActuatorLiteDTO;
import tik.prometheus.rest.dtos.ActuatorTaskDTO;
import tik.prometheus.rest.repositories.ActuatorRepos;
import tik.prometheus.rest.services.ActuatorService;

@RestControllerAdvice
@RequestMapping("/actuators")
public class ActuatorController {
    @Autowired
    ActuatorRepos actuatorRepos;

    @Autowired
    ActuatorService service;

    @GetMapping()
    public ResponseEntity<Page<ActuatorLiteDTO>> all(Pageable pageable, @RequestParam Long greenhouseId) {
        return ResponseEntity.ok(service.getActuators(greenhouseId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActuatorDTO> one(@PathVariable Long id) {
        return ResponseEntity.ok(service.getActuator(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActuatorDTO> replace(@PathVariable Long id, @RequestBody ActuatorDTO updateActuator) {
        return ResponseEntity.ok(service.updateActuator(id, updateActuator));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateState(@PathVariable Long id, @RequestBody ActuatorDTO.ActuatorState nextState) {
        service.patchActuator(id, nextState);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        actuatorRepos.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/tasks")
    public ResponseEntity<ActuatorTaskDTO> postTask(@PathVariable Long id, @RequestBody ActuatorTaskDTO body) {
        service.createTask(id, body);
        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

    @GetMapping("/{id}/tasks")
    public ResponseEntity<ActuatorTaskDTO> getTask(@PathVariable Long id) {
        return ResponseEntity.ok(service.getTask(id));
    }

    @DeleteMapping("/{id}/tasks")
    public ResponseEntity<ActuatorTaskDTO> deleteTask(@PathVariable Long id) {
        service.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
