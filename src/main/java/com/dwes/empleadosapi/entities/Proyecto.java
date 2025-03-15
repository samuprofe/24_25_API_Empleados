package com.dwes.empleadosapi.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.jmx.export.annotation.ManagedAttribute;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "proyectos")
public class Proyecto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "El nombre no puede estar en blanco")
    private String nombre;
    @NotBlank(message = "La descripción no puede estar en blanco")
    private String descripcion;
    @Column(precision = 10, scale = 2)  //Para números de hasta 999.999.999,99 con 2 decimales
    @Min(value = 0, message = "El valor del presupuesto tiene que ser mayor que cero")
    private BigDecimal presupuesto;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaInicio;

    @ManyToMany(mappedBy = "proyectos", cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<Empleado> empleados = new ArrayList<>();

}
