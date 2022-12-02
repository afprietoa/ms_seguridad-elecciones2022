package com.misiontic.grupo17.securityBackend.controllers;

import com.misiontic.grupo17.securityBackend.models.Permission;
import com.misiontic.grupo17.securityBackend.models.Rol;
import com.misiontic.grupo17.securityBackend.services.RolServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/rol")
public class RolController {
    @Autowired
    private RolServices rolServices;

    @GetMapping("/all")
    public List<Rol> getAllRoles(){
        return this.rolServices.index();
    }

    @GetMapping("/{id}")
    public Optional<Rol> getRolById(@PathVariable("id") int id){
        return this.rolServices.show(id);
    }

    @GetMapping("/validate/{idRol}")
    public ResponseEntity<Boolean> validate(@PathVariable("idRol") int idRol, @RequestBody Permission permission){
        return this.rolServices.validateGrant(idRol, permission);
    }

    @PostMapping("/insert")
    public ResponseEntity<Rol> insertRol(@RequestBody Rol rol){
        return this.rolServices.create(rol);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Rol> updateRol(@PathVariable("id") int id, @RequestBody Rol rol){
        return this.rolServices.update(id, rol);
    }

    @PutMapping("/update/{idRol}/add_permission/{idPermission}")
    public ResponseEntity<Rol> updateRolAddPermission(@PathVariable("idRol") int idRol, @PathVariable("idPermission") int idPermission ){
        return this.rolServices.updateAddPermission(idRol, idPermission);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteRol(@PathVariable("id") int id){
        return this.rolServices.delete(id);
    }
}
