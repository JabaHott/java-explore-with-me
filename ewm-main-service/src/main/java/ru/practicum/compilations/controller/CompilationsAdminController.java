package ru.practicum.compilations.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilations.dto.CompilationsDtoRequest;
import ru.practicum.compilations.dto.CompilationsDtoResponse;
import ru.practicum.compilations.dto.CompilationsDtoUpdate;
import ru.practicum.compilations.mapper.CompilationsMapper;
import ru.practicum.compilations.service.CompilationsService;

@RestController
@RequestMapping("/admin/compilations")
@Validated
@Slf4j
@RequiredArgsConstructor
public class CompilationsAdminController {
    private final CompilationsService compilationsService;
    private final CompilationsMapper compilationsMapper;


    @PostMapping
    public ResponseEntity<CompilationsDtoResponse> addCompilation(
            @Valid @RequestBody CompilationsDtoRequest compilationsDtoRequest
    ) {
        log.info("Compilations. Admin Controller: 'addCompilation' method called");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        compilationsMapper.fromCompilationsEntityToCompilationsDtoResponse(compilationsService.addCompilation(compilationsDtoRequest))
                );
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<?> deleteCompilation(
            @Positive @PathVariable Long compId
    ) {
        log.info("Compilations. Admin Controller: 'deleteCompilation' method called");
        compilationsService.deleteCompilation(compId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationsDtoResponse> updateCompilation(
            @Positive @PathVariable Long compId,
            @Valid @RequestBody CompilationsDtoUpdate compilationsDtoUpdate
    ) {
        log.info("Compilations. Admin Controller: 'updateCompilation' method called");
        return ResponseEntity.ok(
                compilationsMapper.fromCompilationsEntityToCompilationsDtoResponse(compilationsService.updateCompilation(compId, compilationsDtoUpdate))
        );
    }
}