package com.helloworld.babel.restaurant.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Plato {

    public enum Categoria {
        @Schema(description = "Primer plato")
        PRIMER_PLATO("Entrante"),
        @Schema(description = "Segundo plato")
        SEGUNDO_PLATO("Plato principal"),
        @Schema(description = "Postre")
        POSTRE("Postre");

        private String descripcion;

        Categoria(String descripcion) {
            this.descripcion = descripcion;
        }

        @JsonValue
        public String getDescripcion() {
            return descripcion;
        }

        @JsonCreator
        public static Categoria fromDescripcion(String descripcion) {
            return switch (descripcion) {
                case "Entrante" -> PRIMER_PLATO;
                case "Plato principal" -> SEGUNDO_PLATO;
                case "Postre" -> POSTRE;
                default -> PRIMER_PLATO;
            };
        }

    }

    @NotNull(message = "El ID no puede ser nulo")
    @Size(min = 1, message = "El Id debe tener como mínimo 1 carácter")
    @Schema(description = "Id del plato")
    private Integer id;

    @NotNull(message = "El nombre no puede ser nulo")
    @Size(min = 3, message = "El nombre debe tener como mínimo 3 caracteres")
    @Schema(description = "Nombre del plato")
    private String nombre;

    @NotNull(message = "El precio no puede ser nulo")
    @Size(min = 1, message = "El costo tiene que tener mínimo un caracter")
    @Schema(description = "Precio del plato")
    private double precio;

    @NotNull(message = "La categoría del plato no puede ser nula")
    @Size(min = 1, max = 1, message = "Solo hay 3 categorías de 1 carácter cada una, " +
            "por lo que la categoría del plato va a tener siempre un caracter")
    @Schema(description = "Categoria del plato")
    private Categoria categoria;

    public Plato(Integer id, String nombre, double precio, Categoria categoria) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
    }

    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public static Plato fromPlatoDAO(com.helloworld.babel.restaurant.daos.model.Plato plato) {

        Categoria categoria = switch (plato.categoria()) {
            case 1 -> Categoria.PRIMER_PLATO;
            case 2 -> Categoria.SEGUNDO_PLATO;
            case 3 -> Categoria.POSTRE;
            default -> Categoria.PRIMER_PLATO;
        };

        return new Plato(
                plato.id(),
                plato.nombre(),
                plato.precio(),
                categoria
        );
    }

    public com.helloworld.babel.restaurant.daos.model.Plato toPlatoDAO() {

        int cat = switch (this.getCategoria()) {
            case PRIMER_PLATO -> 1;
            case SEGUNDO_PLATO -> 2;
            case POSTRE -> 3;
        };

        return new com.helloworld.babel.restaurant.daos.model.Plato(
                this.getId(),
                this.getNombre(),
                this.getPrecio(),
                cat
        );
    }

}
