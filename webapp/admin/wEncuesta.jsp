<%--
/*
################################################################################
# Nombre del Programa : wEncuesta.jsp                                          #
# Autor               : Rafael Gaucin Alvarez                                  #
# Compania            : GOHANDSOFT ideas                                       #
# Proyecto/Procliente : C-08-2705-11                         Fecha: 24/02/2011 #
# Descripcion General : Sistema Encuestas PROSA                                #
# Programa Dependiente:                                                        #
# Programa Subsecuente:                                                        #
# Cond. de ejecucion  :                                                        #
# Dias de ejecucion   :                                      Horario: hh:mm    #
#                              MODIFICACIONES                                  #
#------------------------------------------------------------------------------#
# Autor               :                                                        #
# Compania            :                                                        #
# Proyecto/Procliente :                                      Fecha: dd/mm/yyyy #
# Modificacion        :                                                        #
#------------------------------------------------------------------------------#
# Numero de Parametros:                                                        #
# Parametros Entrada  :                                      Formato:          #
# Parametros Salida   :                                      Formato:          #
################################################################################
*/
--%>
<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.*" %>
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <link rel="stylesheet" href="../css/sep.css"/>
        <link rel="stylesheet" href="../css/themes/ui-lightness/jquery.ui.all.css"/>
        <script src="../js/jquery-1.5.1.js"></script>
        <script src="../js/ui/jquery.ui.core.js"></script>
        <script src="../js/ui/jquery.ui.widget.js"></script>
        <script src="../js/ui/jquery.ui.datepicker.js"></script>
        <script src="../js/external/jquery.bgiframe-2.1.2.js"></script>
        <script src="../js/ui/jquery.ui.mouse.js"></script>
        <script src="../js/ui/jquery.ui.draggable.js"></script>
        <script src="../js/ui/jquery.ui.position.js"></script>
        <script src="../js/ui/jquery.ui.resizable.js"></script>
        <script src="../js/ui/jquery.ui.dialog.js"></script>	
        <script src="../js/funciones.js"></script>

        <script type="text/JavaScript" language="JavaScript">		
            $(function() {
                $('#fechaInicio,#fechaFin').datepicker({
                    dateFormat: 'yy-mm-dd',
                    beforeShow: rangoFechaAsignar
                }) ;
            });		
            function rangoFechaAsignar(input) {
                var hoy = new Date();
                var dateMin = null;
                var dateMax = new Date(hoy.getFullYear()+2, 0, 1);

                if (input.id === "fechaInicio") {
                    if ($("#fechaFin").datepicker("getDate") != null) {
                        dateMax = $("#fechaFin").datepicker("getDate");
                    }
                    else {
                        //dateMax = new Date; //Set this to your absolute maximum date
                    }
                }
                else if (input.id === "fechaFin") {
                    //dateMax = new Date; //Set this to your absolute maximum date
                    if ($("#fechaInicio").datepicker("getDate") != null) {
                        dateMin = $("#fechaInicio").datepicker("getDate");
                    }
                }
                return {
                    minDate: dateMin,
                    maxDate: dateMax
                };
            }
            function Calendario()
            {
                var strUrl="../calendario.jsp";
                var car="width=250,height=250,Scrollbars=NO,Resizable=NO,Menubar=NO,Toolbar=NO";
                window.open(strUrl, "calendario", car);
            }
            $(function() {
                $( "#datepicker" ).datepicker();
            });
            function validate(isCreated) {        
                var valuesOk=false;
                if(document.Encuesta.claveEncuesta.value.length != 0) {
                    if(!validateIsNumeric(document.Encuesta.claveEncuesta)) {
                        alert("La Clave debe ser un numero");
                        return false;
                    }
                    valuesOk = true;
                }
                else {
                    alert("Tiene que introducir la Clave de la Encuesta.");
                    valuesOk = false;
                }
                if(document.Encuesta.nombreEncuesta.value.length != 0) {
                    if(validateText(document.Encuesta.nombreEncuesta.value)) {
                        valuesOk = true;
                    }
                    else {
                        alert("No se permiten caracteres especiales: ¿ ?{ } [ ] - + ¡ ' _ .");
                        return false;
                    }
                }
                else {
                    alert("Tiene que introducir el Nombre de la Encuesta.");
                    valuesOk = false;
                }
                /*
                        if(document.Encuesta.tipoEncuesta.value != 0) {
                                valuesOk = true;
                        }
                        else {
                                alert("Tiene que introducir el Tipo de Encuesta.");
                                valuesOk = false;
                        }
                        if(document.Encuesta.estatusEncuesta.value != 0) {
                                valuesOk = true;
                        }
                        else {
                                alert("Tiene que introducir el Estatus de la Encuesta.");
                                valuesOk = false;
                        }
                 */
                if(document.Encuesta.fechaInicio.value.length != 0) {
                    valuesOk = true;
                }
                else {
                    alert("Tiene que introducir la Fecha de Inicio de la Encuesta.");
                    valuesOk = false;
                }
                if(document.Encuesta.fechaFin.value.length != 0) {
                    valuesOk = true;
                }
                else {
                    alert("Tiene que introducir la Fecha Final de la Encuesta.");
                    valuesOk = false;
                }
                if(valuesOk) {
                    if(isCreated == 0) {
                        document.Encuesta.accion.value = "Crear";
                    }
                    else {
                        document.Encuesta.accion.value = "Siguiente";
                    }
                    document.Encuesta.submit();
                }
            }
            function validateText(text) {
                var invalids = "`´+{}[].;!$%&#&/()='¿?";
                var count;
                for(t = 0; t < text.length; t++) {
                    for(i = 0; i < invalids.length; i++) {
                        if(text.charAt(t) == invalids.charAt(i)) {
                            count = 1;
                            return false;
                        }
                    }
                }
                return true;
            }
            $(document).ready(function() {
                var enc = window.parent.frmCat.selectedEncuesta.value;
                //alert("val: " + enc);
            });
            function cancelar() {
                var Shadowbox = window.parent.Shadowbox;
                window.parent.location.reload();
                Shadowbox.close();
            }
        </script>
        <style type="text/css">
            #Asignacion {			
                padding-top: 50px;
                padding-left: 100px;
                padding-right: 150px;
                vertical-align: middle;
                text-align: center;
            }
            .ExtraSize {
                text-align: center;
                font-size: 20px;
            }
            #Formulario {
                text-align: left;
            }
            #Formulario ol {
                list-style-type: none;
                padding: 0;
                margin-bottom: 0px;
                height: 300px;
            }
            li.formulario { 
                padding-top: 3px;
                padding-bottom: 3px;
            }
            label {
                float: left;
                width: 120px;
                padding-right: 10px;
                text-align: right;
                font-family: "Arial";
                font-weight: 600;
            }
            fieldset {
                padding-left: 0;
                height: 90px;
            }
            fieldset li { 
                padding-top: 3px;
                padding-bottom: 3px;
            }
            fieldset legend { 
                font-family: "Arial";
                font-weight: 700;
            }
        </style>
        <title>Wizard Encuestas :: Encuesta ::</title>

    </head>

    <body>
        <jsp:useBean id="srvWizard" class="lib.beans.ServiceWizard" scope="session" />
        <%
            HttpSession sesion=request.getSession();
            System.out.println("sesion="+sesion);
        %>
        <div id="grayBar">
            <div id="backRight">
                <div id="backLeft">
                    <div id="Asignacion">
                        <div class="Titulo ExtraSize">
                            Cree una Encuesta
                        </div>
                        <div id="Formulario">
                            <%
                                System.out.println("srvWizard= "+srvWizard);
                                /*if(!srvWizard.isCreated()) {
                                    System.out.println("inicialize!!!!!!!");
                                    srvWizard.initialize();
                                }*/
                                System.out.println("\n\n\nURL: " + request.getRequestURL()+ " tamparametros: " + request.getParameterMap().size());
                                Enumeration y=request.getParameterNames();
                                while(y.hasMoreElements()){
                                    System.out.println("el="+y.nextElement());
                                }
                                String x=request.getParameter("idEncuesta");//.value
                                System.out.println("idEncuesta="+x+"--");
                                srvWizard.setIdEncuesta(x);
                                System.out.println("idEncuesta="+srvWizard.idEncuesta+"--");
                                srvWizard.processRequest(request, response);
                            %>
                            <form name="Encuesta" action="wEncuesta.jsp" method="post">
                                <input type="hidden" name="estatus"/>
                                <input type="hidden" name="accion"/>
                                <input type="hidden" name="idEncuesta" />
                                <ol>
                                    <li class="formulario">
                                        <label>Clave: </label>
                                        <input type="text" name="claveEncuesta" id="claveEncuesta" tabindex="1"
                                               style="width:50px; text-transform: uppercase;" value="${srvWizard.encuesta.clave}"/>
                                    </li>
                                    <li class="formulario">
                                        <label>Nombre: </label>
                                        <input type="text" name="nombreEncuesta" id="nombreEncuesta" tabindex="2"
                                               style="width:170px; text-transform: uppercase;" value="${srvWizard.encuesta.nombre}"/>
                                    </li>
                                    <li class="formulario">
                                        <label>Tipo: </label>
                                        <select name="tipoEncuesta" id="tipoEncuesta" style="width:100px;" tabindex="3">
                                            <option>
                                                CLIENTE INTERNO
                                            </option>
                                            <option>
                                                CLIENTE EXTERNO
                                            </option>
                                        </select>
                                    </li>
                                    <li class="formulario">
                                        <label>Estatus: </label>
                                        <select name="estatusEncuesta" id="estatusEncuesta" tabindex="4"
                                                style="width:100px;">
                                            <option value="1">
                                                Habilitado
                                            </option>
                                            <option value="0">
                                                Deshabilitado
                                            </option>
                                        </select>
                                    </li>
                                    <fieldset>
                                        <legend>Vigencia de Asignaci&oacute;n</legend>
                                        <ol>
                                            <li>
                                                <label>Inicio:</label>
                                                <input type="text" name="fechaInicio" id="fechaInicio" tabindex="5"                                          
                                                       style="width:120px;" value="${srvWizard.encuesta.inicio}"/>
                                            </li>
                                            <li>
                                                <label>Fin:</label>
                                                <input type="text" name="fechaFin" id="fechaFin" tabindex="6"
                                                       style="width:120px;" value="${srvWizard.encuesta.fin}"/>
                                            </li>
                                        </ol>
                                    </fieldset>
                                </ol>
                                <div id="Botones">
                                    <ul>
                                        <li onclick="cancelar();">Cancelar</li>
                                        <c:choose>
                                            <c:when test="${srvWizard.encuesta.id != null}">
                                                <li onclick="validate(1);">Siguiente</li>
                                            </c:when>
                                            <c:when test="${srvWizard.encuesta.id == null}">
                                                <li onclick="validate(0);">Siguiente</li>
                                            </c:when>
                                        </c:choose>
                                    </ul>
                                </div>
                                <div>
                                    ${srvWizard.message}
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>