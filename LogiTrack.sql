drop database if exists LogiTrack;
create database Logitrack;
use Logitrack;

/*
 * Usuarios
 */

/* Tabla interna para normalizacion
 * End-Points Roles
 * joins de usuarios
 */
create table roles(
	id_rol int primary key auto_increment,
	nombre varchar(100) not null,
	activo bool not null default true
);

/*
 * End-Points Usuario
 * insert
 * update
 * select by id
 * select by rol
 * select by nombre
 */
create table usuarios(
	id_usuario int primary key auto_increment,
	id_rol int not null,
	nombre varchar(100) not null,
	contrasenia varchar(16) not null,
	activo bool not null default true,
	constraint fk_usuarios_roles foreign key (id_rol) references roles(id_rol)
);

/*
 * Productos
 */

/* Tabla interna para normalizacion
 * End-Points Categorias
 * joins de catalogos
 */
create table categorias(
	id_categoria int primary key auto_increment,
	nombre varchar(50) unique not null,
	activo bool not null default true
);

/*
 * End-Points catalogo
 * insert
 * update
 * select by id
 * select by categoria
 * select by nombre
 */
create table catalogos(
	id_catalogo int primary key auto_increment,
	id_categoria int not null,
	nombre varchar(150) not null,
	precio decimal(10, 2) not null,
	activo bool default true not null,
	constraint fk_producto_categorias foreign key (id_categoria) references categorias(id_categoria),
	constraint chk_numero_positivo check(precio >= 0)
);

/* Importar municipios y departamentos de la tabla de excel
 * TODO: 
 *  - Create table municipios
 *  - Create table Departamentos
 *  - fk-bodega-municipio
 */

/* 
 * End-Points Bodega
 * insert
 * update
 * select by id
 * select by nombre
 * select by ubicacion
 */
create table bodegas(
	id_bodega int primary key auto_increment,
	nombre varchar(150) not null,
	ubicacion varchar(150) not null,/*TODO: Convertir a FK de municipios*/
	capacidad int not null,
	capacidad_actual int not null,
	id_encargado int not null,
	activo bool not null default true,
	constraint fk_bodega_usuario foreign key (id_encargado) references usuarios(id_usuario),
	constraint chk_capacidad_positivo check (capacidad >= 0)
);

/*
 * End-Points producto
 * insert
 * update
 * delete
 * find by bodega id
 * find by catalogo id
 * joins de catalogos
 * joins de bodegas
 */
create table productos(
	id_catalogo int not null,
	stock int not null,
	id_bodega int not null,
	primary key(id_catalogo, id_bodega),
	constraint fk_producto_bodega foreign key (id_bodega) references bodegas(id_bodega),
	constraint fk_producto_catalogo foreign key (id_catalogo) references catalogos(id_catalogo),
	constraint chk_stock_positivo check (stock >= 0)
);

/* Auditorias 
 * Tablas internas automaticas solo para consulta 
 */


/* Cambios en usuarios */
create table audit_usuarios(
	id_aud_usuario bigint primary key auto_increment,
	id_usuario int not null,
	tipo varchar(50) not null,
	mensaje varchar(250) not null,
	fecha timestamp not null default now(),
	constraint fk_audit_usuarios_usuarios foreign key (id_usuario) references usuarios(id_usuario)
);

/* Cambios en Bodegas / Productos */
create table audit_general(
	id_audit_general bigint primary key auto_increment,
	id_catalogo int,
	id_bodega int,
	id_categoria int,
	tipo varchar(50) not null,
	mensaje varchar(250) not null,
	fecha timestamp not null default now(),
	constraint fk_audit_general_bodegas foreign key (id_bodega) references bodegas(id_bodega),	
	constraint fk_audit_general_categoria foreign key (id_categoria) references categorias(id_categoria),
	constraint fk_audit_general_catalogo foreign key (id_catalogo) references catalogos(id_catalogo)
);

/* Entrada o salida de productos */
create table audit_transacciones(
	id_audit_transaccion bigint primary key auto_increment,
	id_catalogo int not null,
	id_bodega int not null,
	tipo varchar(50) not null,
	mensaje varchar(250) not null,
	fecha timestamp not null default now(),
	constraint fk_audit_transacciones_productos foreign key (id_catalogo) references catalogos(id_catalogo),
	constraint fk_audit_transacciones_bodegas foreign key (id_bodega) references bodegas(id_bodega)
);

/* Triggers
 */

create trigger tgr_usuarios_after_insert
after insert on usuarios
for each row
	insert into audit_usuarios (id_usuario, tipo, mensaje) values (new.id_usuario, "AGREGADO", "Nuevo usuario creado en la BD");

delimiter //
create trigger tgr_usuarios_after_update
after update on usuarios
for each row
begin
	declare v_mensaje varchar (250) default "Cambios registrados: ";
	declare v_a_rol varchar(100);
	declare v_d_rol varchar(100);

	if new.activo != old.activo and new.activo = false then set v_mensaje = concat(v_mensaje, "El Usuario ha sido desactivado en la BD. ");
	elseif new.activo != old.activo and new.activo = true then set v_mensaje = concat(v_mensaje, "El Usuario ha sido reactivado en la BD. ");
	end if;
	if new.contrasenia != old.contrasenia then set v_mensaje = concat(v_mensaje, "El Usuario ha cambiado su contraseña. ");
	end if;
	if new.id_rol != old.id_rol then 
		select nombre into v_d_rol from roles where id_rol = new.id_rol;
		select nombre into v_a_rol from roles where id_rol = old.id_rol;
		set v_mensaje = concat(v_mensaje, "Al Usuario se le ha sido cambiado el rol a: '", v_d_rol, "', antes: '", v_a_rol,"'. ");
	end if;
	if new.nombre != old.nombre then set v_mensaje = concat(v_mensaje, "EL nombre del Usuario fue cambiado a: '", new.nombre, "', antes: '", old.nombre, "'. ");
	end if;
	if v_mensaje != "Cambios registrados: " then
		insert into audit_usuarios (id_usuario, tipo, mensaje) 
		values (new.id_usuario, "ACTUALIZACION", trim(v_mensaje));
	end if;
end//
delimiter ;

create trigger tgr_general_productos_after_insert
after insert on productos
for each row
	insert into audit_general (id_catalogo, id_bodega, tipo, mensaje) values (new.id_catalogo, new.id_bodega, "AGREGADO", "Se ha asignado un producto a la bodega");

create trigger tgr_general_bodega_after_insert
after insert on bodegas
for each row
	insert into audit_general (id_bodega, tipo, mensaje) values (new.id_bodega, "AGREGADO", "Nueva Bodega agregada a la BD");

create trigger tgr_general_catalogo_after_insert
after insert on catalogos
for each row
	insert into audit_general (id_catalogo, tipo, mensaje) values (new.id_catalogo, "AGREGADO", "Nuevo articulo agregado a el catalogo en la BD");

create trigger tgr_general_categoria_after_insert
after insert on categorias
for each row
	insert into audit_general (id_categoria, tipo, mensaje) values (new.id_categoria, "AGREGADO", "Nueva Categoria agregada a la BD");

delimiter //

create trigger tgr_catalogos_after_update
after update on catalogos
for each row
begin
	declare v_mensaje varchar(250) default "Cambios registrados: ";
	declare v_a_cat varchar(100);
	declare v_d_cat varchar(100);

	if new.activo != old.activo and new.activo = false then 
		set v_mensaje = concat(v_mensaje, "El ítem fue desactivado del catálogo. ");
	elseif new.activo != old.activo and new.activo = true then 
		set v_mensaje = concat(v_mensaje, "El ítem fue reactivado del catálogo. ");
	end if;
	if new.nombre != old.nombre then 
		set v_mensaje = concat(v_mensaje, "El nombre cambió a: '", new.nombre, "', antes: '", old.nombre, "'. ");
	end if;
	if new.precio != old.precio then 
		set v_mensaje = concat(v_mensaje, "El precio cambió a: $", new.precio, ", antes: $", old.precio, ". ");
	end if;
	if new.id_categoria != old.id_categoria then 
		select nombre into v_d_cat from categorias where id_categoria = new.id_categoria;
		select nombre into v_a_cat from categorias where id_categoria = old.id_categoria;
		set v_mensaje = concat(v_mensaje, "Se cambió de categoría a: '", v_d_cat, "', antes: '", v_a_cat, "'. ");
	end if;
	if v_mensaje != "Cambios registrados: " then
		insert into audit_general (id_catalogo, id_categoria, tipo, mensaje, fecha) 
		values (new.id_catalogo, new.id_categoria, "ACTUALIZACION", trim(v_mensaje), now());
	end if;
end//

delimiter ;

delimiter //

create trigger tgr_bodegas_after_update
after update on bodegas
for each row
begin
	declare v_mensaje varchar(250) default "Cambios registrados: ";

	if new.activo != old.activo and new.activo = false then 
		set v_mensaje = concat(v_mensaje, "La bodega fue desactivada en la BD. ");
	elseif new.activo != old.activo and new.activo = true then 
		set v_mensaje = concat(v_mensaje, "La bodega fue reactivada en la BD. ");
	end if;
	if new.nombre != old.nombre then 
		set v_mensaje = concat(v_mensaje, "El nombre fue cambiado a: '", new.nombre, "', antes: '", old.nombre, "'. ");
	end if;
	if new.capacidad != old.capacidad then 
		set v_mensaje = concat(v_mensaje, "La capacidad fue cambiada a: '", new.capacidad, "', antes: '", old.capacidad, "'. ");
	end if;
	if new.id_encargado != old.id_encargado then 
		set v_mensaje = concat(v_mensaje, "El encargado fue cambiado a: '", new.id_encargado, "', antes: '", old.id_encargado, "'. ");
	end if;
	if v_mensaje != "Cambios registrados: " then
		insert into audit_general (id_bodega, tipo, mensaje) 
		values (new.id_bodega, "ACTUALIZACION", trim(v_mensaje));
	end if;
end//

delimiter ;

delimiter //

create trigger tgr_categorias_after_update
after update on categorias
for each row
begin
	declare v_mensaje varchar(250) default "Cambios registrados: ";

	if new.activo != old.activo and new.activo = false then 
		set v_mensaje = concat(v_mensaje, "La categoría fue desactivada en la BD. ");
	elseif new.activo != old.activo and new.activo = true then 
		set v_mensaje = concat(v_mensaje, "La categoría fue reactivada en la BD. ");
	end if;
	if new.nombre != old.nombre then 
		set v_mensaje = concat(v_mensaje, "El nombre fue cambiado a: '", new.nombre, "', antes: '", old.nombre, "'. ");
	end if;
	if v_mensaje != "Cambios registrados: " then
		insert into audit_general (id_categoria, tipo, mensaje) 
		values (new.id_categoria, "ACTUALIZACION", trim(v_mensaje));
	end if;
end//

delimiter ;

delimiter //

create trigger tgr_productos_after_delete
after delete on productos
for each row
begin
	declare v_nombre_catalogo varchar(100);
	declare v_nombre_bodega varchar(100);
	declare v_mensaje varchar(250);

	select nombre into v_nombre_catalogo from catalogos where id_catalogo = old.id_catalogo;
	select nombre into v_nombre_bodega from bodegas where id_bodega = old.id_bodega;

	set v_mensaje = concat(
		"Se eliminó la existencia del producto '", coalesce(v_nombre_catalogo, old.id_catalogo), 
		"' de la bodega '", coalesce(v_nombre_bodega, old.id_bodega), 
		"'. Cantidad retirada: ", old.stock, " unidades."
	);

	insert into audit_general (id_catalogo, id_bodega, tipo, mensaje) 
	values (old.id_catalogo, old.id_bodega, "ELIMINACION", v_mensaje);
end//

delimiter ;

delimiter //

create trigger tgr_productos_after_update_stock
after update on productos
for each row
begin
	declare v_mensaje varchar(250);
	declare v_diferencia int;
	declare v_tipo varchar(50);
	declare v_nombre_catalogo varchar(100);
	declare v_nombre_bodega varchar(100);

	if new.stock != old.stock then
		
		set v_diferencia = new.stock - old.stock;

		select nombre into v_nombre_catalogo from catalogos where id_catalogo = new.id_catalogo;
		select nombre into v_nombre_bodega from bodegas where id_bodega = new.id_bodega;

		if v_diferencia > 0 then
			set v_tipo = "ENTRADA";
			set v_mensaje = concat("Ingreso de stock para '", coalesce(v_nombre_catalogo, new.id_catalogo), 
								   "' en bodega '", coalesce(v_nombre_bodega, new.id_bodega), 
								   "'. Entraron: +", v_diferencia, " unidades. Stock total: ", new.stock, ".");
		else
			set v_tipo = "SALIDA";
			set v_mensaje = concat("Salida de stock para '", coalesce(v_nombre_catalogo, new.id_catalogo), 
								   "' en bodega '", coalesce(v_nombre_bodega, new.id_bodega), 
								   "'. Salieron: ", v_diferencia, " unidades. Stock total: ", new.stock, ".");
		end if;

		insert into audit_transacciones (id_catalogo, id_bodega, tipo, mensaje) 
		values (new.id_catalogo, new.id_bodega, v_tipo, v_mensaje);

	end if;
end//

delimiter ;

delimiter //
create trigger tgr_transacciones_productos_after_insert
after insert on productos
for each row
follows tgr_general_productos_after_insert
begin
	declare v_nombre_catalogo varchar(100);
	declare v_nombre_bodega varchar(100);
	declare v_mensaje varchar(250);

	if new.stock > 0 then
		select nombre into v_nombre_catalogo from catalogos where id_catalogo = new.id_catalogo;
		select nombre into v_nombre_bodega from bodegas where id_bodega = new.id_bodega;

		set v_mensaje = concat("Ingreso inicial de stock para '", coalesce(v_nombre_catalogo, new.id_catalogo), 
							   "' en bodega '", coalesce(v_nombre_bodega, new.id_bodega), 
							   "'. Carga inicial: +", new.stock, " unidades.");

		insert into audit_transacciones (id_catalogo, id_bodega, tipo, mensaje) 
		values (new.id_catalogo, new.id_bodega, "ENTRADA", v_mensaje);
	end if;
end//

delimiter ;

/* Roles/Seguridad
 */

create role if not exists 'logi_admin';
create role if not exists 'logi_backend';
create role if not exists 'logi_auditor';
create role if not exists 'logi_reportes';


/* Privilegios */

grant all privileges on Logitrack.* to 'logi_admin';

grant select, insert, update, delete on Logitrack.usuarios to 'logi_backend';
grant select, insert, update, delete on Logitrack.roles to 'logi_backend';
grant select, insert, update, delete on Logitrack.categorias to 'logi_backend';
grant select, insert, update, delete on Logitrack.catalogos to 'logi_backend';
grant select, insert, update, delete on Logitrack.bodegas to 'logi_backend';
grant select, insert, update, delete on Logitrack.productos to 'logi_backend';

grant select on Logitrack.audit_usuarios to 'logi_backend';
grant select on Logitrack.audit_general to 'logi_backend';
grant select on Logitrack.audit_transacciones to 'logi_backend';

grant select on Logitrack.audit_usuarios to 'logi_auditor';
grant select on Logitrack.audit_general to 'logi_auditor';
grant select on Logitrack.audit_transacciones to 'logi_auditor';

grant select on Logitrack.catalogos to 'logi_reportes';
grant select on Logitrack.categorias to 'logi_reportes';
grant select on Logitrack.bodegas to 'logi_reportes';
grant select on Logitrack.productos to 'logi_reportes';

/* Creacion y activacion de usuarios*/

create user if not exists 'usr_dba'@'%' identified by 'AdminPass123!';
create user if not exists 'usr_api_service'@'%' identified by 'BackendPass123!';
create user if not exists 'usr_auditor_qa'@'%' identified by 'AuditPass123!';

grant 'logi_admin' to 'usr_dba'@'%';
grant 'logi_backend' to 'usr_api_service'@'%';
grant 'logi_auditor' to 'usr_auditor_qa'@'%';

set default role 'logi_admin' to 'usr_dba'@'%';
set default role 'logi_backend' to 'usr_api_service'@'%';
set default role 'logi_auditor' to 'usr_auditor_qa'@'%';