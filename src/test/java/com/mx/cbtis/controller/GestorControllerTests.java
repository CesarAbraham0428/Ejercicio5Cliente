package com.mx.cbtis.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.mx.cbtis.modelo.Comentarios;
import com.mx.cbtis.modelo.Proyectos;
import com.mx.cbtis.modelo.Tareas;
import com.mx.cbtis.modelo.Usuario;

/**
 * El controlador crea un RestTemplate nuevo en cada metodo, por eso se
 * intercepta su construccion para no depender del backend en el puerto 8081.
 */
class GestorControllerTests {

	private final Proyectos proyecto = new Proyectos(1, "P", "D", "2025-01-01", "2025-12-31", "activo", "azul");
	private final Tareas tarea = new Tareas(2, "T", "D", "2025-06-01", "lista", "abierta", 1);
	private final Usuario usuario = new Usuario(3, "Ana", "ana@mail.com", "1234", "admin", "ana.png");
	private final Comentarios comentario = new Comentarios(4, "Ana", 2, "Hola", "2025-01-01 10:00");

	private final GestorController controller = new GestorController();
	private MockedConstruction<RestTemplate> restTemplates;

	@BeforeEach
	void interceptarRestTemplate() {
		restTemplates = Mockito.mockConstruction(RestTemplate.class, (rt, context) -> {
			when(rt.postForEntity(anyString(), any(), eq(Integer.class))).thenReturn(ResponseEntity.ok(1));
			when(rt.postForEntity(anyString(), any(), eq(Usuario.class))).thenReturn(ResponseEntity.ok(usuario));

			when(rt.postForObject(anyString(), any(), eq(Proyectos[].class))).thenReturn(new Proyectos[] { proyecto });
			when(rt.postForObject(anyString(), any(), eq(Usuario[].class))).thenReturn(new Usuario[] { usuario });
			when(rt.postForObject(anyString(), any(), eq(Tareas[].class))).thenReturn(new Tareas[] { tarea });
			when(rt.postForObject(anyString(), any(), eq(Comentarios[].class)))
					.thenReturn(new Comentarios[] { comentario });

			when(rt.getForObject(anyString(), eq(Proyectos.class), anyMap())).thenReturn(proyecto);
			when(rt.getForObject(anyString(), eq(Tareas.class), anyMap())).thenReturn(tarea);

			when(rt.exchange(anyString(), eq(HttpMethod.DELETE), any(HttpEntity.class), eq(Integer.class)))
					.thenReturn(ResponseEntity.ok(1));
		});
	}

	@AfterEach
	void liberar() {
		restTemplates.close();
	}

	@Test
	void vistasEstaticas() {
		assertEquals("home", controller.getHome());
		assertEquals("tutorial", controller.getTutorial());
		assertEquals("manual", controller.getManual());
		assertEquals("login", controller.getLogin());
	}

	@Test
	void getRegisterAgregaUsuarioAlModelo() {
		Model modelo = new ExtendedModelMap();

		assertEquals("register", controller.getRegister(modelo));
		assertNotNull(modelo.getAttribute("usuario"));
	}

	@Test
	void setSaveUsuarioRedirigeALogin() {
		ModelAndView mav = controller.setSaveUsuario(usuario);

		assertEquals("redirect:/login", mav.getViewName());
	}

	@Test
	void getInicioAgregaProyectosAlModelo() {
		Model modelo = new ExtendedModelMap();

		assertEquals("inicio", controller.getInicio(modelo));
		assertNotNull(modelo.getAttribute("proyectos"));
	}

	@Test
	void setSaveProyectoRedirigeAInicio() {
		ModelAndView mav = controller.setSaveProyecto(proyecto);

		assertEquals("redirect:/inicio", mav.getViewName());
	}

	@Test
	void consultarProyectosDevuelveLista() {
		Model modelo = new ExtendedModelMap();

		ResponseEntity<Object> respuesta = controller.consultarProyectos(modelo);

		assertEquals(HttpStatus.OK, respuesta.getStatusCode());
		assertEquals(List.of(proyecto), respuesta.getBody());
		assertEquals(List.of(proyecto), modelo.getAttribute("listaProyectos"));
	}

	@Test
	void eliminarProyectoRedirigeAInicio() {
		ModelAndView mav = controller.eliminarProyecto(proyecto);

		assertEquals("redirect:/inicio", mav.getViewName());
	}

	@Test
	void updateProyectoDevuelveElProyectoConsultado() {
		ResponseEntity<Object> respuesta = controller.updateProyecto(proyecto);

		assertEquals(HttpStatus.OK, respuesta.getStatusCode());
		assertSame(proyecto, respuesta.getBody());
	}

	@Test
	void verificarUsuarioDevuelveLaRespuestaDelBackend() {
		ResponseEntity<Usuario> respuesta = controller.verificarUsuario(usuario);

		assertEquals(HttpStatus.OK, respuesta.getStatusCode());
		assertSame(usuario, respuesta.getBody());
	}

	@Test
	void consultarUsuariosDevuelveLista() {
		Model modelo = new ExtendedModelMap();

		ResponseEntity<Object> respuesta = controller.consultarUsuarios(modelo);

		assertEquals(HttpStatus.OK, respuesta.getStatusCode());
		assertEquals(List.of(usuario), respuesta.getBody());
		assertEquals(List.of(usuario), modelo.getAttribute("listaUsuarios"));
	}

	@Test
	void obtenerTareasDevuelveLista() {
		Model modelo = new ExtendedModelMap();

		ResponseEntity<List<Tareas>> respuesta = controller.obtenerTareas(tarea, modelo);

		assertEquals(HttpStatus.OK, respuesta.getStatusCode());
		assertEquals(List.of(tarea), respuesta.getBody());
		assertEquals(List.of(tarea), modelo.getAttribute("tareas"));
	}

	@Test
	void obtenerComentariosDevuelveLista() {
		Model modelo = new ExtendedModelMap();

		ResponseEntity<List<Comentarios>> respuesta = controller.obtenerComentarios(comentario, modelo);

		assertEquals(HttpStatus.OK, respuesta.getStatusCode());
		assertEquals(List.of(comentario), respuesta.getBody());
		assertEquals(List.of(comentario), modelo.getAttribute("comentarios"));
	}

	@Test
	void getProyectoCargaTareaComentarioYUsuarios() {
		Model modelo = new ExtendedModelMap();

		assertEquals("proyecto", controller.getProyecto(modelo));
		assertNotNull(modelo.getAttribute("tarea"));
		assertNotNull(modelo.getAttribute("comentarios"));
		assertEquals(List.of(usuario), modelo.getAttribute("listaUsuarios"));
	}

	@Test
	void getTareaAgregaTareasAlModelo() {
		Model modelo = new ExtendedModelMap();

		assertEquals("proyecto", controller.getTarea(modelo));
		assertNotNull(modelo.getAttribute("tareas"));
	}

	@Test
	void setSaveTareasRedirigeAProyectoConSuId() {
		ModelAndView mav = controller.setSaveTareas(tarea);

		assertEquals("redirect:/proyecto", mav.getViewName());
		assertEquals(1, mav.getModel().get("id_proyecto"));
	}

	@Test
	void setSaveComentarioDevuelveVistaProyecto() {
		assertEquals("proyecto", controller.setSaveComentario(comentario));
	}

	@Test
	void updateTareaDevuelveLaTareaConsultada() {
		ResponseEntity<Object> respuesta = controller.updateTarea(tarea);

		assertEquals(HttpStatus.OK, respuesta.getStatusCode());
		assertSame(tarea, respuesta.getBody());
	}

	@Test
	void eliminarTareaRedirigeAProyecto() {
		ModelAndView mav = controller.eliminarTarea(tarea);

		assertEquals("redirect:/proyecto", mav.getViewName());
	}

	@Test
	void verificarTareaDevuelveLaRespuestaDelBackend() {
		ResponseEntity<Integer> respuesta = controller.verificarTarea(tarea);

		assertEquals(HttpStatus.OK, respuesta.getStatusCode());
		assertEquals(1, respuesta.getBody());
	}

	@Test
	void cambiosTareasDevuelveLaRespuestaDelBackend() {
		ResponseEntity<Integer> respuesta = controller.cambiosTareas(tarea);

		assertEquals(HttpStatus.OK, respuesta.getStatusCode());
		assertEquals(1, respuesta.getBody());
	}

	@Test
	void consultarTareasDevuelveLista() {
		ResponseEntity<Object> respuesta = controller.consultarTareas();

		assertEquals(HttpStatus.OK, respuesta.getStatusCode());
		assertEquals(List.of(tarea), respuesta.getBody());
	}

	@Test
	void consultarComentariosDevuelveLista() {
		ResponseEntity<Object> respuesta = controller.consultarComentarios();

		assertEquals(HttpStatus.OK, respuesta.getStatusCode());
		assertEquals(List.of(comentario), respuesta.getBody());
	}
}
