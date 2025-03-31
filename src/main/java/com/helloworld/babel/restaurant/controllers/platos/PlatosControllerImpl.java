package com.helloworld.babel.restaurant.controllers.platos;

import com.helloworld.babel.restaurant.model.Local;
import com.helloworld.babel.restaurant.model.Plato;
import com.helloworld.babel.restaurant.servicios.platos.PlatosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Tag(name = "Platos", description = "Operaciones relacionadas con los platos")
@SecurityRequirement(name = "basicAuth")
@RestController
@RequestMapping("restaurante/platos")
public class PlatosControllerImpl implements PlatosController {

    private final PlatosService platosService;

    public PlatosControllerImpl(PlatosService platosService) {
        this.platosService = platosService;
    }


    @Override
    @GetMapping("")
    @Operation(summary = "Listado de platos",
            description = "Obtiene una lista de todos los platos registrados en el sistema",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Listado de platos obtenido exitosamente",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = Local.class)
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "204",
                            description = "No se encontraron platos",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Solicitud incorrecta",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(mediaType = "application/json")
                    )
            })
    public List<Plato> getPlatos() {
        List<Plato> platos = platosService.getPlatos();
        return platos;
    }


    @Override
    @GetMapping("/{id}")
    @Operation(summary = "Obtener el plato por su Id",
            description = "Obtenemos un plato por su ID de identificacion",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Plato encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Plato.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Plato no encontrado",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "ID inválido",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(mediaType = "application/json")
                    )
            })
    public Plato getPlatosById(@PathVariable String id) {
        Optional<Plato> plato = platosService.getPlatosById(Integer.parseInt(id));
        if (plato.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Plato no encontrado");
        } else {
            return plato.get();
        }
    }

    @Override
    @PutMapping("/{id}")
    @Operation(summary = "Crea o actualiza el plato por su id",
            description = "Si el plato existe se actualiza y si no existe se crea a partir del ID",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Plato creado exitosamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Plato.class))
                    ),
                    @ApiResponse(
                            responseCode = "204",
                            description = "Plato actualizado exitosamente (sin contenido)"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "ID inválido o datos incorrectos",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Plato no encontrado",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "405",
                            description = "Error de validación en los datos enviados",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    public ResponseEntity<Void> updatePlato(@PathVariable int id, @RequestBody Plato plato) {
        plato.setId(id);
        Optional<Plato> updatedPlato = platosService.updatePlato(plato);
        if (updatedPlato.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Plato a actualizar no encontrado");
        } else {
            return ResponseEntity.
                    noContent().
                    header("Content-Location", "/restaurante/platos/" + plato.getId()).
                    build();
        }
    }

    @Override
    @DeleteMapping("/{id}")
    @Operation(summary = "Borrar el plato a partir de su id",
            description = "Elimina un plato de la base de datos a partir de su ID",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Plato eliminado exitosamente (sin contenido)"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "ID inválido o no proporcionado",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Plato no encontrado",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(mediaType = "application/json")
                    )
            })
    public ResponseEntity<Void> deletePlato(@PathVariable int id) {
        platosService.deletePlato(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("")
    @Operation(summary = "Crear el plato")
    public ResponseEntity<Long> createPlato(@RequestBody Plato plato) {
        long platoCreadoId = platosService.createPlato(plato);
        return ResponseEntity.
                created(URI.create("/restaurante/platos/" + platoCreadoId)).
                body(platoCreadoId);
    }
}
