package com.misiontic.grupo17.securityBackend.services;

import com.misiontic.grupo17.securityBackend.models.Permission;
import com.misiontic.grupo17.securityBackend.repositories.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
/**
 *
 */
public class PermissionServices {
    @Autowired
    private PermissionRepository permissionRepository;

    /**
     *
     * @return
     */
    public List<Permission> index(){
        List<Permission> resultList = (List<Permission>) this.permissionRepository.findAll();
        if(resultList.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "There is not any permission in the list.");
        return resultList;
    }

    /**
     *
     * @param id
     * @return
     */
    public Optional<Permission> show(int id){
        Optional<Permission> result = this.permissionRepository.findById(id);
        if(result.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "The requested permission.id does not exist.");
        return result;
    }

    /**
     *
     * @param newPermission
     * @return
     */
    public ResponseEntity<Permission> create(Permission newPermission){
        if(newPermission.getIdPermission() != null){
            Optional<Permission> tempPermission = this.permissionRepository.findById(newPermission.getIdPermission());
            if(tempPermission.isPresent())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Id is yet in the database.");
        }
        if((newPermission.getUrl() != null) && (newPermission.getMethod() != null)){
            return new ResponseEntity<>(this.permissionRepository.save(newPermission), HttpStatus.CREATED);
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Mandatory fields had not been provided.");
        }
    }

    /**
     *
     * @param id
     * @param permission
     * @return
     */
    public ResponseEntity<Permission> update(int id, Permission permission){
        if(id>0){
            Optional<Permission> tempPermission = this.permissionRepository.findById(id);
            if(!tempPermission.isEmpty()){
                if(tempPermission.get().getUrl() != null){
                    tempPermission.get().setUrl(permission.getMethod());
                }
                if(tempPermission.get().getMethod() != null){
                    tempPermission.get().setMethod(permission.getMethod());
                }
                return new ResponseEntity<>(this.permissionRepository.save(tempPermission.get()), HttpStatus.CREATED);
            }
            else{
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Permission.id does not exist in the database.");
            }
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Permission.id cannot be negative.");
        }
    }

    /**
     *
     * @param id
     * @return
     */
    public ResponseEntity<Boolean> delete(int id){
        Boolean success = this.show(id).map( permission -> {
                    this.permissionRepository.delete(permission);
                    return true;
        }).orElse(false);
        if(success)
            return new ResponseEntity<>(true, HttpStatus.NO_CONTENT);
        else
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Permission cannot be deleted.");
    }
}
