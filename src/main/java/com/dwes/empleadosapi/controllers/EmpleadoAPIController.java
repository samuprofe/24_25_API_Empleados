package com.dwes.empleadosapi.controllers;

import com.dwes.empleadosapi.entities.Empleado;
import com.dwes.empleadosapi.entities.Proyecto;
import com.dwes.empleadosapi.repositories.EmpleadoRepository;
import com.dwes.empleadosapi.repositories.ProyectoRepository;
import jakarta.servlet.ServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class EmpleadoAPIController {

    @Autowired
    private EmpleadoRepository empleadoRepository;
    @Autowired
    private ProyectoRepository proyectoRepository;

    /**
     * Obtener todos los empleados en un JSON
     */
    @GetMapping("/empleados")
    public ResponseEntity<List<Empleado>> getListEmpleados(){
        var empleados = empleadoRepository.findAll();
        return ResponseEntity.ok(empleados);    //Devuelve el código status 200 OK
    }

    /**
     * Obtiene un empleado
     */
    @GetMapping("/empleados/{id}")
    public ResponseEntity<Empleado> getEmpleado(@PathVariable Long id){
        /*
        Optional<Empleado> empleado = empleadoRepository.findById(id);
        if (empleado.isPresent()) {
            return ResponseEntity.ok().body(empleado.get()); // Devuelve el código status 200 OK
        } else {
            return ResponseEntity.notFound().build(); // Devuelve el código 404 Not Found
        }
         */

        return empleadoRepository.findById(id)
                .map(empleado -> ResponseEntity.ok().body(empleado))    //Devuelve el código status 200 OK
                .orElse(ResponseEntity.notFound().build());     //Devuelve el código 404 Not Found
    }

    /**
     * Insertar un empleado (recibe los datos en el cuerpo (body) en formato JSON)
     */
    @PostMapping("/empleados")
    public ResponseEntity<Empleado> insertEmpleado(@RequestBody Empleado empleado){
        var empleadoGuardado = empleadoRepository.save(empleado);
        return ResponseEntity.status(HttpStatus.CREATED).body(empleadoGuardado);    //Devuelve el código status 201 Created
    }


    /**
     * Modifica un empleado
     */
    @PutMapping("/empleados/{id}")
    public ResponseEntity<Empleado> editEmpleado(@PathVariable Long id, @RequestBody Empleado nuevoEmpleado){
        /*
        Optional<Empleado> empleado = empleadoRepository.findById(id);
        if(empleado.isPresent()){
            empleado.get().setNombre(nuevoEmpleado.getNombre());
            empleado.get().setEmail(nuevoEmpleado.getEmail());
            empleado.get().setApellidos(nuevoEmpleado.getApellidos());
            return empleadoRepository.save(empleado.get());
        }else{
            return empleadoRepository.save(nuevoEmpleado);
        }
        */

        return empleadoRepository.findById(id)
                .map(empleado -> {
                    if(nuevoEmpleado.getNombre() != null) { //Comprobamos si en el JSON de entrada el campo viene definido, sino mantenemos los datos originales
                        empleado.setNombre(nuevoEmpleado.getNombre());
                    }
                    if(nuevoEmpleado.getApellidos() != null) {
                        empleado.setApellidos(nuevoEmpleado.getApellidos());
                    }
                    if(nuevoEmpleado.getEmail() != null) {
                        empleado.setEmail(nuevoEmpleado.getEmail());
                    }
                    return ResponseEntity.ok(empleadoRepository.save(empleado));    //Devuelve el código 200 OK y en el cuerpo del mensaje el nuevo empleado en JSON
                })
                .orElseGet(() -> {
                    return ResponseEntity.notFound().build();   //Devuelve el código 404 NotFound
                });
    }

    /**
     * Borra un empleado
     */
    @DeleteMapping("/empleados/{id}")
    public ResponseEntity<?> deleteEmpleado(@PathVariable Long id){

        /*
        Optional<Empleado> empleado = empleadoRepository.findById(id);
        if(empleado.isPresent()){
            empleadoRepository.delete(empleado.get());
            return ResponseEntity.noContent().build();  //Devuelve el código status 204 Not Content
        }
        else{
            return ResponseEntity.notFound().build();   //Devuelve el código status 404 Not Found
        }
        */


        return empleadoRepository.findById(id)
                .map(empleado -> {
                    empleadoRepository.delete(empleado);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //localhost:8080/empleados/7/proyectos POST
    @PostMapping("/empleados/{id}/proyectos")
    public ResponseEntity<Empleado> insertProyecto(@PathVariable Long id, @RequestBody Proyecto proyecto){
        Optional<Empleado> empleado = empleadoRepository.findById(id);
        Optional<Proyecto> proyectoBD = proyectoRepository.findById(proyecto.getId());
        if(empleado.isPresent() && proyectoBD.isPresent()) {
            empleado.get().getProyectos().add(proyectoBD.get());
            empleadoRepository.save(empleado.get());
            return ResponseEntity.ok(empleado.get());
        }
        return ResponseEntity.notFound().build();
    }

    //localhost:8080/empleados/7/proyectos/5 DELETE
    @DeleteMapping("/empleados/{idEmpleado}/proyectos/{idProyecto}")
    public ResponseEntity<Empleado> deleteProyecto(@PathVariable Long idEmpleado, @PathVariable Long idProyecto){
        Optional<Empleado> empleado = empleadoRepository.findById(idEmpleado);
        Optional<Proyecto> proyecto = proyectoRepository.findById(idProyecto);
        if(empleado.isPresent() && proyecto.isPresent()) {
            empleado.get().getProyectos().remove(proyecto.get());
            empleadoRepository.save(empleado.get());
            return ResponseEntity.ok(empleado.get());
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    //localhost:8080/empleados/7/proyectos GET
    @GetMapping("/empleados/{id}/proyectos")
    public ResponseEntity<List<Proyecto>> getProyecto(@PathVariable Long id){
        Optional<Empleado> empleado = empleadoRepository.findById(id);
        if(empleado.isPresent()) {
            return ResponseEntity.ok(empleado.get().getProyectos());
        }
        return ResponseEntity.notFound().build();
    }
}
