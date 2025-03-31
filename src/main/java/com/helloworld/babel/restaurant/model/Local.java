package com.helloworld.babel.restaurant.model;

import com.helloworld.babel.restaurant.daos.model.Restaurante;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;


public class Local {
    @Schema(description = "Codigo identificativo del local")
    private String cif;
    @Schema(description = "Nombre restaurante")
    private String nombre;
    @Schema(description = "Direccion restaurante")
    private String direccion;
    @Schema(description = "Telefono restaurante")
    private String telefono;
    @Schema(description = "Carta restaurante")
    private List<Plato> carta = new ArrayList<>();

    public Local(String cif, String nombre, String direccion, String telefono) {
        this.cif = cif;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public String getCif() {
        return cif;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public List<Plato> getCarta() {
        return carta;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void addPlato(Plato plato) {
        carta.add(plato);
    }

    public static Local fromRestaurante(Restaurante restaurante) {
        return new Local(
                restaurante.cif(),
                restaurante.nombre(),
                restaurante.direccion(),
                restaurante.telefono());
    }

    public Restaurante toRestaurante() {
        return new Restaurante(cif, nombre, direccion, telefono);
    }
}
