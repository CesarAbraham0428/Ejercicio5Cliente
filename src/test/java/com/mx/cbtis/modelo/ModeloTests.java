package com.mx.cbtis.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class ModeloTests {

	@Test
	void usuarioConstructorVacioYSetters() {
		Usuario u = new Usuario();
		assertNull(u.getId_usuario());

		u.setId_usuario(1);
		u.setNombre("Ana");
		u.setCorreo_electronico("ana@mail.com");
		u.setContrasena("1234");
		u.setRol("admin");
		u.setImagen_usuario("ana.png");

		assertEquals(1, u.getId_usuario());
		assertEquals("Ana", u.getNombre());
		assertEquals("ana@mail.com", u.getCorreo_electronico());
		assertEquals("1234", u.getContrasena());
		assertEquals("admin", u.getRol());
		assertEquals("ana.png", u.getImagen_usuario());
	}

	@Test
	void usuarioConstructorCompleto() {
		Usuario u = new Usuario(2, "Luis", "luis@mail.com", "abcd", "user", "luis.png");

		assertEquals(2, u.getId_usuario());
		assertEquals("Luis", u.getNombre());
		assertEquals("luis@mail.com", u.getCorreo_electronico());
		assertEquals("abcd", u.getContrasena());
		assertEquals("user", u.getRol());
		assertEquals("luis.png", u.getImagen_usuario());
	}

	@Test
	void proyectosConstructorVacioYSetters() {
		Proyectos p = new Proyectos();
		assertNull(p.getId_proyecto());

		p.setId_proyecto(1);
		p.setNombre("Proyecto");
		p.setDescripcion("Descripcion");
		p.setFecha_inicio("2025-01-01");
		p.setFecha_finalizacion("2025-12-31");
		p.setEstado_proyecto("activo");
		p.setFondo("azul");

		assertEquals(1, p.getId_proyecto());
		assertEquals("Proyecto", p.getNombre());
		assertEquals("Descripcion", p.getDescripcion());
		assertEquals("2025-01-01", p.getFecha_inicio());
		assertEquals("2025-12-31", p.getFecha_finalizacion());
		assertEquals("activo", p.getEstado_proyecto());
		assertEquals("azul", p.getFondo());
	}

	@Test
	void proyectosConstructorCompleto() {
		Proyectos p = new Proyectos(3, "P", "D", "2025-02-01", "2025-03-01", "cerrado", "rojo");

		assertEquals(3, p.getId_proyecto());
		assertEquals("P", p.getNombre());
		assertEquals("D", p.getDescripcion());
		assertEquals("2025-02-01", p.getFecha_inicio());
		assertEquals("2025-03-01", p.getFecha_finalizacion());
		assertEquals("cerrado", p.getEstado_proyecto());
		assertEquals("rojo", p.getFondo());
	}

	@Test
	void tareasConstructorVacioYSetters() {
		Tareas t = new Tareas();
		assertNull(t.getId_tarea());

		t.setId_tarea(1);
		t.setNombre("Tarea");
		t.setDescripcion("Descripcion");
		t.setFecha_vencimiento("2025-06-01");
		t.setLista("pendientes");
		t.setEstado("abierta");
		t.setId_proyecto(9);

		assertEquals(1, t.getId_tarea());
		assertEquals("Tarea", t.getNombre());
		assertEquals("Descripcion", t.getDescripcion());
		assertEquals("2025-06-01", t.getFecha_vencimiento());
		assertEquals("pendientes", t.getLista());
		assertEquals("abierta", t.getEstado());
		assertEquals(9, t.getId_proyecto());
	}

	@Test
	void tareasConstructorCompleto() {
		Tareas t = new Tareas(4, "T", "D", "2025-07-01", "hechas", "cerrada", 8);

		assertEquals(4, t.getId_tarea());
		assertEquals("T", t.getNombre());
		assertEquals("D", t.getDescripcion());
		assertEquals("2025-07-01", t.getFecha_vencimiento());
		assertEquals("hechas", t.getLista());
		assertEquals("cerrada", t.getEstado());
		assertEquals(8, t.getId_proyecto());
	}

	@Test
	void comentariosConstructorVacioYSetters() {
		Comentarios c = new Comentarios();
		assertNull(c.getId_comentario());

		c.setId_comentario(1);
		c.setId_usuario("Ana");
		c.setId_tarea(2);
		c.setTexto_comentario("Hola");
		c.setFecha_hora("2025-01-01 10:00");

		assertEquals(1, c.getId_comentario());
		assertEquals("Ana", c.getId_usuario());
		assertEquals(2, c.getId_tarea());
		assertEquals("Hola", c.getTexto_comentario());
		assertEquals("2025-01-01 10:00", c.getFecha_hora());
	}

	@Test
	void comentariosConstructorCompleto() {
		Comentarios c = new Comentarios(5, "Luis", 6, "Texto", "2025-02-02 12:00");

		assertEquals(5, c.getId_comentario());
		assertEquals("Luis", c.getId_usuario());
		assertEquals(6, c.getId_tarea());
		assertEquals("Texto", c.getTexto_comentario());
		assertEquals("2025-02-02 12:00", c.getFecha_hora());
	}

	@Test
	void asignacionesConstructorVacioYSetters() {
		Asignaciones a = new Asignaciones();
		assertNull(a.getId_asignacion());

		a.setId_asignacion(1);
		a.setId_usuario(2);
		a.setId_tarea(3);
		a.setFecha_asignacion("2025-03-03");

		assertEquals(1, a.getId_asignacion());
		assertEquals(2, a.getId_usuario());
		assertEquals(3, a.getId_tarea());
		assertEquals("2025-03-03", a.getFecha_asignacion());
	}

	@Test
	void asignacionesConstructorCompleto() {
		Asignaciones a = new Asignaciones(7, 8, 9, "2025-04-04");

		assertEquals(7, a.getId_asignacion());
		assertEquals(8, a.getId_usuario());
		assertEquals(9, a.getId_tarea());
		assertEquals("2025-04-04", a.getFecha_asignacion());
	}
}
