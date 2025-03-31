package com.helloworld.babel.restaurant.controllers.locales;

import com.helloworld.babel.restaurant.model.Local;
import com.helloworld.babel.restaurant.model.Plato;
import com.helloworld.babel.restaurant.servicios.exceptions.NotFoundException;
import com.helloworld.babel.restaurant.servicios.locales.LocalesService;
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

@Tag(name = "Locales", description = "Operaciones relacionadas con los locales")
@SecurityRequirement(name = "basicAuth")
@RestController
@RequestMapping("restaurante/locales")
public class LocalesControllerImpl implements LocalesController {

    private final LocalesService localesService;

    public LocalesControllerImpl(LocalesService localesService) {
        this.localesService = localesService;
    }


    @Override
    @GetMapping("")
    @Operation(summary = "Listado de locales",
            description = "Obtiene una lista de todos los locales registrados en el sistema",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Listado de locales obtenido exitosamente",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = Local.class)
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "204",
                            description = "No se encontraron locales",
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
    public List<Local> getLocales() {
        return localesService.getLocales();
    }

    @Override
    @GetMapping("/{cif}")
    @Operation(summary = "Get local por su CIF",
            description = "Obtenemos un local por su CIF de identificacion",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Local encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Local.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Local no encontrado",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "CIF inválido",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(mediaType = "application/json")
                    )
            })
    public Local getLocalByCif(@PathVariable String cif) {
        Optional<Local> local = localesService.getLocalByCif(cif);
        if (local.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Local no encontrado");
        } else {
            return local.get();
        }
    }

    @Override
    @PutMapping("/{cif}")
    @Operation(summary = "Actualizar o crear un local a partir de su CIF",
            description = "Si el local existe se actualiza y si no existe se crea a partir del CIF",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Local creado exitosamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Local.class))
                    ),
                    @ApiResponse(
                            responseCode = "204",
                            description = "Local actualizado exitosamente (sin contenido)"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "CIF inválido o datos incorrectos",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Local no encontrado",
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
    public ResponseEntity<Void> createOrUpdateLocal(@PathVariable String cif, @RequestBody Local local) {
        local.setCif(cif);
        Optional<Local> updatedLocal = localesService.updateLocal(local);
        if (updatedLocal.isEmpty()) {
            localesService.createLocal(local);
            return ResponseEntity.
                    created(URI.create("/restaurante/locales/" + local.getCif())).
                    build();
        } else {
            return ResponseEntity.
                    noContent().
                    header("Content-Location", "/restaurante/locales/" + local.getCif()).
                    build();
        }
    }

    @Override
    @DeleteMapping("/{cif}")
    @Operation(summary = "Eliminando local a partir de su CIF",
            description = "Elimina un local de la base de datos a partir de su CIF",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Local eliminado exitosamente (sin contenido)"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "CIF inválido o no proporcionado",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Local no encontrado",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(mediaType = "application/json")
                    )
            })
    public ResponseEntity<Void> deleteLocal(@PathVariable String cif) {
        localesService.deleteLocal(cif);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/{cif}/platos")
    @Operation(summary = "Listado de platos",
            description = "Devuelve la lista de platos que hay en el local con este CIF",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Listado de locales obtenido exitosamente",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = Plato.class)
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "204",
                            description = "No se encontraron platos en este local",
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
            }
    )
    public List<Plato> getPlatos(@PathVariable String cif) {
        try {
            return localesService.getPlatosByLocal(cif);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Override
    @PostMapping("/{cif}/platos")
    @Operation(summary = "Añadiendo plato en el local segun el cif",
            description = "Se añade un plato en el local cuyo CIF coincide con el parámetro",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Plato agregado exitosamente",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Solicitud incorrecta, datos inválidos",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Local no encontrado",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(mediaType = "application/json")
                    )

            })
    public ResponseEntity<Void> addPlato(@PathVariable String cif, @RequestBody int plato) {
        try {
            if (localesService.addPlato(cif, plato) > 0) {
                return ResponseEntity
                        .created(URI.create("/restaurante/locales/" + cif + "/platos/" + plato))
                        .build();
            } else {
                return ResponseEntity
                        .noContent()
                        .build();
            }
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }

    }

    @Override
    @DeleteMapping("/{cif}/platos/{plato}")
    @Operation(summary = "Eliminando el plato del restaurante segun su cif",
            description = "Elimina el plato del restaurante especificado por su CIF. " +
                    "Si el plato se elimina exitosamente no se devuelve nada",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Plato eliminado exitosamente",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Plato o local no encontrado",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(mediaType = "application/json")
                    )
            })
    public ResponseEntity<Void> removePlato(@PathVariable String cif, @PathVariable int plato) {
        try {
            localesService.removePlato(cif, plato);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return ResponseEntity.noContent().build();
    }
}
