package com.UTP.Avance3.Controller;

import com.UTP.Avance3.DTO.EmpresaResponse;
import com.UTP.Avance3.DTO.ModificarEmpresaRequest;
import com.UTP.Avance3.DTO.PerfilEmpresaResponse;
import com.UTP.Avance3.DTO.RegistroEmpresaRequest;
import com.UTP.Avance3.Servicios.EmpresaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoints de la empresa: registro, perfil y configuración.
 *
 * Requerimientos funcionales:
 *  - RF-01 (Registro de usuarios): POST /empresas/registro registra la empresa, su contacto y su usuario
 *    administrador en un solo paso.
 *  - RF-06 (Personalizar perfiles): GET /empresas/{id} entrega el perfil de la empresa: datos,
 *    contacto y los usuarios que pertenecen a ella con su rol. PUT /usuarios/{idUsuario}/empresa
 *    deja que el administrador modifique los datos y el contacto de su empresa.
 *  - RF-03 (Control de acceso basado en roles): modificar la empresa es solo para el rol Admin
 *    (se comprueba en EmpresaService).
 */
@RestController
@RequestMapping("/api")
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    // RF-01 (Registro de usuarios): Registro de la empresa. El usuario que se crea recibe el rol
    //    Admin (ver EmpresaService).
    // @Valid hace que Spring valide el DTO antes de ejecutar el método
    @PostMapping("/empresas/registro")
    public ResponseEntity<EmpresaResponse> registrar(@Valid @RequestBody RegistroEmpresaRequest request) {
        EmpresaResponse respuesta = empresaService.registrarEmpresa(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    // RF-06 (Personalizar perfiles): Datos que muestra perfil.html.
    @GetMapping("/empresas/{id}")
    public PerfilEmpresaResponse obtener(@PathVariable Long id) {
        return empresaService.obtenerPerfil(id);
    }

    // RF-06 (Personalizar perfiles): El administrador modifica los datos y el contacto de su empresa
    //    (configuracion.html).
    // RF-03 (Control de acceso basado en roles): Solo el rol Admin y solo su propia empresa.
    // @Valid hace que Spring valide el DTO antes de ejecutar el método
    @PutMapping("/usuarios/{idUsuario}/empresa")
    public PerfilEmpresaResponse modificar(@PathVariable Long idUsuario,
                                           @Valid @RequestBody ModificarEmpresaRequest request) {
        return empresaService.modificarEmpresa(idUsuario, request);
    }
}
