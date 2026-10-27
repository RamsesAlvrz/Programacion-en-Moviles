package com.alvarez.lavacampus.ui.administrador

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import com.alvarez.lavacampus.data.local.entity.Servicio
import com.alvarez.lavacampus.R
import java.util.Locale

private val Morado = Color(0xFF6D28D9)
private val Fondo = Color(0xFFF8F7FC)
private val Borde = Color(0xFFE5E7EB)
private val Texto = Color(0xFF222638)

@Composable
fun FormularioServicioScreen(
    servicioExistente: Servicio?,
    cargando: Boolean,
    onGuardar: (Servicio) -> Unit,
    onVolver: () -> Unit
) {
    val editando = servicioExistente != null

    var nombre by rememberSaveable(servicioExistente?.id) {
        mutableStateOf(servicioExistente?.nombre.orEmpty())
    }

    var unidad by rememberSaveable(servicioExistente?.id) {
        mutableStateOf(servicioExistente?.unidad ?: "Kilo")
    }

    var precio by rememberSaveable(servicioExistente?.id) {
        mutableStateOf(
            servicioExistente?.precio?.let {
                String.format(Locale.US, "%.2f", it)
            }.orEmpty()
        )
    }

    var horasEntrega by rememberSaveable(servicioExistente?.id) {
        mutableIntStateOf(servicioExistente?.horasEntrega ?: 48)
    }

    var descripcion by rememberSaveable(servicioExistente?.id) {
        mutableStateOf(servicioExistente?.descripcion.orEmpty())
    }

    var icono by rememberSaveable(servicioExistente?.id) {
        mutableStateOf(servicioExistente?.icono ?: "lavadora")
    }

    var activo by rememberSaveable(servicioExistente?.id) {
        mutableStateOf(servicioExistente?.activo ?: true)
    }

    var errorNombre by remember { mutableStateOf(false) }
    var errorPrecio by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Fondo,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(Fondo),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onVolver,
                    enabled = !cargando
                ) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = Texto
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (editando) "Editar servicio"
                        else "Nuevo servicio",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Texto
                    )
                }
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            // NOMBRE: ETIQUETA DENTRO DEL CAMPO
            CampoCompacto(
                etiqueta = "Nombre del servicio",
                valor = nombre,
                onValorChange = {
                    nombre = it
                    errorNombre = false
                },
                placeholder = "Ej. Lavado y secado",
                error = errorNombre
            )

            if (errorNombre) {
                Text(
                    "El nombre es obligatorio.",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 11.sp
                )
            }

            // SE COBRA POR
            TituloSeccion("Se cobra por")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                listOf("Kilo", "Prenda", "Unidad").forEach { opcion ->
                    BotonSeleccion(
                        texto = opcion,
                        seleccionado = unidad == opcion,
                        modifier = Modifier.weight(1f),
                        onClick = { unidad = opcion }
                    )
                }
            }

            // PRECIO: ETIQUETA DENTRO DEL CAMPO
            CampoCompacto(
                etiqueta = "Precio (S/)",
                valor = precio,
                onValorChange = {
                    precio = it
                    errorPrecio = false
                },
                placeholder = "0.00",
                keyboardType = KeyboardType.Decimal,
                error = errorPrecio
            )

            if (errorPrecio) {
                Text(
                    "Ingresa un precio mayor que cero.",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 11.sp
                )
            }

            // TIEMPO DE ENTREGA
            TituloSeccion("Tiempo de entrega")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                listOf(24, 48, 72).forEach { horas ->
                    BotonSeleccion(
                        texto = "${horas}h",
                        seleccionado = horasEntrega == horas,
                        modifier = Modifier.weight(1f),
                        onClick = { horasEntrega = horas }
                    )
                }
            }

            // DESCRIPCIÓN: ETIQUETA DENTRO DEL CAMPO
            CampoCompacto(
                etiqueta = "Descripción",
                valor = descripcion,
                onValorChange = { descripcion = it },
                placeholder = "Ej. Lavado, secado y doblado",
                altura = 66.dp,
                multilinea = true
            )

            // ICONOS
            TituloSeccion("Ícono")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                val opciones = listOf(
                    "lavadora" to R.drawable.ic_servicio_lavadora,
                    "plancha" to R.drawable.ic_servicio_plancha,
                    "ropa" to R.drawable.ic_servicio_camiseta
                )

                opciones.forEach { (valor, recurso) ->

                    BotonIcono(
                        recurso = recurso,
                        seleccionado = icono == valor,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            icono = valor
                        }
                    )
                }
            }
            // SERVICIO ACTIVO
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(11.dp),
                color = Color.White
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Servicio activo",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Texto
                        )

                        Text(
                            "Los estudiantes pueden pedirlo",
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                    }

                    Switch(
                        checked = activo,
                        onCheckedChange = { activo = it },
                        modifier = Modifier.height(32.dp),
                        colors = SwitchDefaults.colors(
                            checkedTrackColor = Morado
                        )
                    )
                }
            }

            // GUARDAR
            Button(
                onClick = {
                    val precioNumerico = precio
                        .trim()
                        .replace(",", ".")
                        .toDoubleOrNull()

                    errorNombre = nombre.isBlank()
                    errorPrecio = precioNumerico == null ||
                            !precioNumerico.isFinite() ||
                            precioNumerico <= 0.0

                    if (!errorNombre && !errorPrecio) {
                        onGuardar(
                            Servicio(
                                id = servicioExistente?.id ?: 0,
                                nombre = nombre.trim(),
                                descripcion = descripcion.trim(),
                                unidad = unidad,
                                precio = precioNumerico!!,
                                horasEntrega = horasEntrega,
                                activo = activo,
                                icono = icono
                            )
                        )
                    }
                },
                enabled = !cargando,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(43.dp),
                shape = RoundedCornerShape(9.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Morado
                )
            ) {
                if (cargando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(19.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Guardar servicio",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // CANCELAR
            OutlinedButton(
                onClick = onVolver,
                enabled = !cargando,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(41.dp),
                shape = RoundedCornerShape(9.dp),
                border = BorderStroke(1.5.dp, Morado)
            ) {
                Text(
                    "Cancelar",
                    color = Morado,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(Modifier.height(4.dp))
        }
    }
}

// =============================================
// CAMPO COMPACTO CON ETIQUETA INTERIOR
// =============================================

@Composable
private fun CampoCompacto(
    etiqueta: String,
    valor: String,
    onValorChange: (String) -> Unit,
    placeholder: String,
    altura: androidx.compose.ui.unit.Dp = 53.dp,
    multilinea: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    error: Boolean = false
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(altura),
        shape = RoundedCornerShape(11.dp),
        color = Color.White,
        border = BorderStroke(
            1.dp,
            if (error) MaterialTheme.colorScheme.error else Borde
        )
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 7.dp
            ),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = etiqueta,
                fontSize = 10.sp,
                color = Color.Gray,
                lineHeight = 12.sp
            )

            Spacer(Modifier.height(2.dp))

            BasicTextField(
                value = valor,
                onValueChange = onValorChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = !multilinea,
                maxLines = if (multilinea) 2 else 1,
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType
                ),
                textStyle = TextStyle(
                    fontSize = 13.sp,
                    color = Texto
                ),
                cursorBrush = SolidColor(Morado),
                decorationBox = { innerTextField ->
                    Box {
                        if (valor.isEmpty()) {
                            Text(
                                placeholder,
                                fontSize = 12.sp,
                                color = Color.LightGray
                            )
                        }

                        innerTextField()
                    }
                }
            )
        }
    }
}

// =============================================
// TITULOS
// =============================================

@Composable
private fun TituloSeccion(texto: String) {
    Text(
        text = texto,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = Texto
    )
}

// =============================================
// BOTONES DE SELECCION
// =============================================

@Composable
private fun BotonSeleccion(
    texto: String,
    seleccionado: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(37.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        color = if (seleccionado) Morado else Color.White,
        border = if (seleccionado) null
        else BorderStroke(1.dp, Borde),
        shadowElevation = if (seleccionado) 1.dp else 0.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = texto,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = if (seleccionado) Color.White
                else Color.Gray
            )
        }
    }
}

// =============================================
// BOTONES DE ICONOS
// =============================================

@Composable
private fun BotonIcono(
    recurso: Int,
    seleccionado: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(48.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        color = if (seleccionado) Color(0xFFF1EAFE)
        else Color.White,
        border = BorderStroke(
            if (seleccionado) 1.5.dp else 1.dp,
            if (seleccionado) Morado else Borde
        )
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                painter = painterResource(id = recurso),
                contentDescription = null,
                modifier = Modifier.size(22.dp),
                tint = if (seleccionado) Morado
                else Color(0xFF9CA3AF)
            )
        }
    }
}
