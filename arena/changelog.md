# Digital Wallet TP1 - Arena

## Características Anteriores (19/09):

#### Login Window:
-	Solo los usuarios registrados pueden ingresar al sistema.
-	Luego muestra una ventana de bienvenida con el nombre del usuario ingresado.
#### Administration Window:
-	Tabla con información general de los usuarios (nombre, apellido, email, estado).
-	Filtro de búsqueda del nombre y apellido de los usuarios.
-	Accesos a “Ver”, “Agregar”, “Modificar” y “Beneficios”.
#### Info User Dialog:
-	Nombre, Apellido, Email, Saldo, CVU del Usuario.
-	Acceso a “movimientos” del Usuario.
#### Add User Dialog:
-	Formulario con los campos de Nombre, Apellido, Numero de Documento, Email, Password y un checkbox indicando si es Administrador o no.
-	El campo Numero de Documento usa un transformer y un filter que limita a solo ingresar números.
-	El campo Email usa un filter, limitando a seguir un formato
(ej. arya_stark@gmail.com).
-	Una vez que el usuario es agregado, aparece en la tabla de Administration Window.
#### Edit User Dialog:
-	Se puede editar el email y el estado de cuenta del usuario.
#### Loyalty Dialog:
-	Formulario con los campos Nombre de Beneficio, Fecha Desde, Fecha Hasta, Tipo de Beneficio, Importe del Regalo/Porcentaje del Descuento, Cantidad de Operaciones e Importe de cada Operación.
-	Especifica mediante un filter si falta algún campo por completar.
-	Las fechas deben ser del tipo dd/MM/aaaa, limitadas por un filter y un transformer.
-	El porcentaje debe ser un numero entre 0 y 100.
-	Se puede cambiar entre si el beneficio es un Descuento o un Regalo, y va a cambiar el campo a llenar.
-	Los campos que indiquen montos o porcentajes, solo se podrán ingresar números.
#### Delete User Dialog:
-	Pregunta si desea borrar al usuario seleccionado. Al aceptar lo borra del sistema, y desaparece de la tabla de usuarios.

## Características Nuevas (3/10):

####Login Window:
-	Especifica mediante un filter que campo esta todavía sin llenar.
-	El mail debe seguir un determinado formato (ej. jimmy.lann@gmail.com).
-	Se agrega un acceso para registrar usuarios nuevos (se utiliza solo para demostración).
-	Verifica que solo los administradores puedan ingresar.
#### Register User Window:
-	Basado de las validaciones del anterior Add User Window, cada campo esta validado específicamente si está o no vacío.
-	El Numero de Documento debe ser de 8 cifras.
-	Una vez registrado, se agrega al sistema y a la tabla de usuarios.
#### Administration Window:
-	Se añade una tabla con la información general de los beneficios (nombre, fechas, tipo de beneficio, monto y cantidad mínima de transacciones).
-	Indica el nombre y apellido de la cuenta con la que ingreso al sistema.
-	Puede cerrarse la sesión.
-	Si se intenta ver, editar o borrar un usuario sin seleccionarlo, se abre una ventana avisando de ello.
#### Info User Dialog:
-	Desacoplamiento del dialogo de modificar usuario. Antes usaban el mismo dialogo.
#### Add User Dialog:
-	Validaciones específicas para cada campo vacío.
#### Edit User Dialog:
-	El Usuario se guarda editado recién cuando se acepta la modificación. Hay un desacoplamiento en el bindeo entre la tabla de usuarios, y el usuario a modificar.
#### Loyalty Dialog:
-	El beneficio es guardado en el sistema, y se ve en la tabla.
#### Delete User Dialog:
-	El usuario a borrar debe tener un saldo de 0.
