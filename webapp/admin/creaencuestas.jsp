<%--
/*
################################################################################
# Nombre del Programa : creaencuestas.jsp                                      #
# Autor               : Alfredo Campos Hernandez                               #
# Compania            : GOHANDSOFT ideas                                       #
# Proyecto/Procliente : C-08-2069-11                         Fecha: 24/02/2011 #
# Descripcion General : Sistema Encuestas PROSA                                #
# Programa Dependiente:                                                        #
# Programa Subsecuente:                                                        #
# Cond. de ejecucion  :                                                        #
# Dias de ejecucion   :                                      Horario: hh:mm    #
#                              MODIFICACIONES                                  #
#------------------------------------------------------------------------------#
# Autor               : Rafael Gaucin Alvarez                                  #
# Compania            : GOHANDSOFT ideas                                       #
# Proyecto/Procliente : C-08-2075-11                         Fecha: 04/09/2011 #
# Modificacion        : Modificacion tabla                                     #
#------------------------------------------------------------------------------#
# Numero de Parametros:                                                        #
# Parametros Entrada  :                                      Formato:          #
# Parametros Salida   :                                      Formato:          #
################################################################################
*/
--%>
<%@include file="regacc.jsp" %>
<%@page import="java.util.*" %>
<%@page import="lib.clsDB" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
        String tblEncuestas="";
        String Qry="",Msg="<tr><td colspan='2'>&nbsp;</td></tr>";
        //ArrayList<String[]> arrAO;
	
        String Accion=request.getParameter("hidAccion");
	
        String taeID=request.getParameter("E_TAE_ID");
        String taeCVE_ENCUESTA=request.getParameter("E_TAE_CVE_ENCUESTA");
        String taeNOMBRE_ENCUESTA=request.getParameter("E_TAE_NOMBRE_ENCUESTA");
        String taeTIPO_ENCUESTA=request.getParameter("E_TAE_TIPO_ENCUESTA");
        String taeESTATUS=request.getParameter("E_TAE_ESTATUS_ENCUESTA");
        String taeFechaIniAsig=request.getParameter("E_TAE_FECHA_INI_ASIGNAR");
        String taeFechaFinAsig=request.getParameter("E_TAE_FECHA_FIN_ASIGNAR");
        String taeFechaIniEva=request.getParameter("E_TAE_FECHA_INI_EVALUAR");
        String taeFechaFinEva=request.getParameter("E_TAE_FECHA_FIN_EVALUAR");
        if(Accion==null)Accion="";
        clsDB objDB = new clsDB();
	
        if(objDB.Conectar())
        {
                /*-----------------------------------------------------------------------------#
                #-Marca del Cambio : C-08-2075-11           Inicia     Modificacion 04/09/2011 #
                #-----------------------------------------------------------------------------*/
                /*
                if(Accion.compareTo("AGREGAR")==0)
                {
                        Qry="SELECT NVL(MAX(TAE_ID),0)+1 FROM TBL_ALTA_ENCUESTAS";
                        taeID=objDB.ConsultaRes(Qry, 1, 1);
                        Qry="INSERT INTO TBL_ALTA_ENCUESTAS "
                        + "(TAE_ID,TAE_CVE_ENCUESTA,TAE_TIPO_ENCUESTA,TAE_NOMBRE_ENCUESTA,TAE_FECHA_INI_ASIGNAR,"
                        + "TAE_FECHA_FIN_ASIGNAR,TAE_FECHA_INI_EVALUAR,TAE_FECHA_FIN_EVALUAR,TAE_ESTATUS_ENCUESTA)"
                        + "VALUES ("+taeID+",'"+taeCVE_ENCUESTA+"','"+taeTIPO_ENCUESTA+"','"+taeNOMBRE_ENCUESTA+"',"
                        + "TO_DATE('"+taeFechaIniAsig+"','YYYY-MM-DD'),TO_DATE('"+taeFechaFinAsig+"','YYYY-MM-DD'),"
                        + "TO_DATE('"+taeFechaIniEva+"','YYYY-MM-DD'),TO_DATE('"+taeFechaFinEva+"','YYYY-MM-DD'),"
                        + "'"+taeESTATUS+"')";
                        if(objDB.Ejecuta(Qry))Msg="La encuesta se agreg&oacute; con &eacute;xito!";
                        else Msg="No se pudo agregar la encuesta!";
                        Msg="<tr><td width='15px'>&nbsp;</td><td align='center' class='Mensaje'>"+Msg+"</td></tr>";
                }
                if(Accion.compareTo("ELIMINAR")==0)
                {
                        Qry="DELETE FROM TBL_ALTA_ENCUESTAS WHERE TAE_ID="+taeID;
                        if(objDB.Ejecuta(Qry))Msg="La encuesta se elimin&oacute; con &eacute;xito!";
                        else Msg="No se pudo eliminar la encuesta, verifique que no tenga detalle!";
                        Msg="<tr><td width='15px'>&nbsp;</td><td align='center' class='Mensaje'>"+Msg+"</td></tr>";
                }
                if(Accion.compareTo("CAMBIAR")==0)
                {
                        Qry="UPDATE TBL_ALTA_ENCUESTAS SET "
                                        + "TAE_CVE_ENCUESTA='"+taeCVE_ENCUESTA+"',"
                                        + "TAE_NOMBRE_ENCUESTA='"+taeNOMBRE_ENCUESTA+"',"
                                        + "TAE_TIPO_ENCUESTA='"+taeTIPO_ENCUESTA+"',"
                                        + "TAE_ESTATUS_ENCUESTA='"+taeESTATUS+"',"
                                        + "TAE_FECHA_INI_ASIGNAR=TO_DATE('"+taeFechaIniAsig+"','YYYY-MM-DD'),"
                                        + "TAE_FECHA_FIN_ASIGNAR=TO_DATE('"+taeFechaFinAsig+"','YYYY-MM-DD'),"
                                        + "TAE_FECHA_INI_EVALUAR=TO_DATE('"+taeFechaIniEva+"','YYYY-MM-DD'),"
                                        + "TAE_FECHA_FIN_EVALUAR=TO_DATE('"+taeFechaFinEva+"','YYYY-MM-DD')"
                                        + "WHERE TAE_ID="+taeID;
                        if(objDB.Ejecuta(Qry))Msg="La encuesta se actualiz&oacute; con &eacute;xito!";
                        else Msg="No se pudo actualizar la encuesta!";
                        Msg="<tr><td width='15px'>&nbsp;</td><td align='center' class='Mensaje'>"+Msg+"</td></tr>";
                }
                //Obtiene la tabla de encuestas
                Qry="SELECT TAE_ID,TAE_CVE_ENCUESTA,TAE_TIPO_ENCUESTA,TAE_NOMBRE_ENCUESTA,"
                + "TO_CHAR(TAE_FECHA_INI_ASIGNAR, 'YYYY-MM-DD') as TAE_FECHA_INI_ASIGNAR,TO_CHAR(TAE_FECHA_FIN_ASIGNAR, 'YYYY-MM-DD') as TAE_FECHA_FIN_ASIGNAR,"
                + "TO_CHAR(TAE_FECHA_INI_EVALUAR, 'YYYY-MM-DD') as TAE_FECHA_INI_EVALUAR,TO_CHAR(TAE_FECHA_FIN_EVALUAR, 'YYYY-MM-DD') as TAE_FECHA_FIN_EVALUAR,"
                + "TAE_ESTATUS_ENCUESTA FROM TBL_ALTA_ENCUESTAS";
                ArrayList<String[]> Data = objDB.Consulta(Qry);
                tblEncuestas=objDB.MostrarTabla(Data);
                */
                /*-----------------------------------------------------------------------------#
                #-Marca del Cambio : C-08-2075-11           Termina    Modificacion 04/09/2011 #
                #-----------------------------------------------------------------------------*/
        }
        //dbug=objDB.getDebug();
        objDB = null;
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <title>Creaci&oacute;n - Sistema de Encuestas Prosa</title>
        <meta name="author" content="Alfredo Campos" >
        <meta name="generator" content="SIC" >
        <meta name="description" content="Sistema de Encuestas Prosa" >
        <meta name="keywords" content="Sistema,Encuestas" >
        <meta http-equiv="content-type" content="text/html; charset=ISO-8859-1" >
        <meta http-equiv="Content-Script-Type" content="text/javascript" >
        <meta http-equiv="Content-Style-Type" content="text/css" >
        <link rel="stylesheet" type="text/css" href="../css/sep.css">        
        <link rel="stylesheet" href="../css/themes/ui-lightness/jquery.ui.all.css">
        <link rel="stylesheet" href="../css/shadowbox.css" >
        <script src="../js/funciones.js"></script>		
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
        <script src="../js/shadowbox.js"></script>

        <script language="JavaScript">
            // increase the default animation speed to exaggerate the effect
            $.fx.speeds._default = 1000;
			
            $(function() {
                $( "#dialog" ).dialog({
                    autoOpen: false,
                    show: "blind",
                    hide: "explode"
                });

                $( "#opener" ).click(function() {
                    $( "#dialog" ).dialog( "open" );
                    return false;
                });
            });
            var originalref;
            $(function() {
                $('#E_TAE_FECHA_INI_ASIGNAR,#E_TAE_FECHA_FIN_ASIGNAR').datepicker({
                    dateFormat: 'yy-mm-dd',
                    beforeShow: rangoFechaAsignar
                }) ;
                $('#E_TAE_FECHA_INI_EVALUAR,#E_TAE_FECHA_FIN_EVALUAR').datepicker({
                    dateFormat: 'yy-mm-dd',
                    beforeShow: rangoFechaEvaluar
                }) ;
            });			
            function rangoFechaAsignar(input) {
                var hoy = new Date();
                var dateMin = null;
                var dateMax = new Date(hoy.getFullYear()+2, 0, 1);

                if (input.id === "E_TAE_FECHA_INI_ASIGNAR") {
                    if ($("#E_TAE_FECHA_FIN_ASIGNAR").datepicker("getDate") != null) {
                        dateMax = $("#E_TAE_FECHA_FIN_ASIGNAR").datepicker("getDate");
                    }
                    else {
                        //dateMax = new Date; //Set this to your absolute maximum date
                    }
                }
                else if (input.id === "E_TAE_FECHA_FIN_ASIGNAR") {
                    //dateMax = new Date; //Set this to your absolute maximum date
                    if ($("#E_TAE_FECHA_INI_ASIGNAR").datepicker("getDate") != null) {
                        dateMin = $("#E_TAE_FECHA_INI_ASIGNAR").datepicker("getDate");
                    }
                }
                return {
                    minDate: dateMin,
                    maxDate: dateMax
                };
            }
            function rangoFechaEvaluar(input) {
                var hoy = new Date();
                var dateMin = null;
                var dateMax = new Date(hoy.getFullYear()+2, 0, 1);

                if (input.id === "E_TAE_FECHA_INI_EVALUAR") {
                    if ($("#E_TAE_FECHA_FIN_EVALUAR").datepicker("getDate") != null) {
                        dateMax = $("#E_TAE_FECHA_FIN_EVALUAR").datepicker("getDate");
                    }
                    else {
                        //dateMax = new Date; //Set this to your absolute maximum date
                    }
                }
                else if (input.id === "E_TAE_FECHA_FIN_EVALUAR") {
                    //dateMax = new Date; //Set this to your absolute maximum date
                    if ($("#E_TAE_FECHA_INI_EVALUAR").datepicker("getDate") != null) {
                        dateMin = $("#E_TAE_FECHA_INI_EVALUAR").datepicker("getDate");
                    }
                }
                return {
                    minDate: dateMin,
                    maxDate: dateMax
                };
            }
            function setSelectedEncuesta(fila, Valor) {
                var filas = document.getElementById('tblEncuestas').getElementsByTagName('tbody')[0].getElementsByTagName('tr');
                resetStyle(filas);
                for(f = 1; f < filas.length; f++) {
                    if(f == fila) {
                        var estilo = filas[f].getAttribute('class');
                        if(estilo != 'styleSelected') {
                            filas[f].setAttribute('class', 'styleSelected');
                            document.frmCat.selectedEncuesta.value = Valor;
                            document.getElementById("modEncuesta").href="wEncuesta.jsp?idEncuesta="+document.getElementById("selectedEncuesta").value+"&accion=Modificar";
                            //alert("url="+document.getElementById("modEncuesta").href+"--");
                            /* Modifica el valor del link Cambiar*/
                            /*
                                                var lnk = document.getElementById('modEncuesta');
                                                lnk.href = lnk.href+'?Encuesta='+Valor+'&accion=Modificar';
                                                if(originalref == "" || originalref == null) {
                                                        originalref = lnk.getAttribute('href');
                                                }
                                                if(lnk.getAttribute('href').search("%P%") == -1) {
                                                        lnk.setAttribute('href', originalref);
                                                }
                                                lnk.setAttribute('href', lnk.getAttribute('href').replace("%P%", Valor));	*/					
                            return;
                        }
                    }
                }
            }	
            function resetStyle(filas) {
                for(f = 1; f < filas.length; f++) {
                    if(f % 2 == 0) {
                        filas[f].setAttribute('class', 'styleRenglonClaro');
                    }
                    else {
                        filas[f].setAttribute('class', 'styleRenglonObscuro');
                    }
                }
            }
            function Modificar(lnk) {
                if(document.frmCat.selectedEncuesta != null && document.frmCat.selectedEncuesta != "") {
                    //alert("Modificara la encuesta: " + document.frmCat.selectedEncuesta.value);
                    var valor = document.frmCat.selectedEncuesta.value;
					
                    //var ref = "wEncuesta.jsp?Encuesta="+valor+"&accion=Modificar";
                    var ref = lnk.getAttribute('href')+"="+valor;
                    lnk.setAttribute('href', ref);
                    var atributo = lnk.getAttribute('href');
                    return true;
                    /*
                                Shadowbox.init({
                                        skipSetup: true
                                });
                                Shadowbox.open({
                                        content: url=ref,
                                        player: "html",
                                        width: 850,
                                        height: 550,
                                        animateFade: false
                                });
                     */
                }
                else {
                    alert("Debe seleccionar una encuesta.");
                    return false;
                }
            }
            function Eliminar()
            {
                if(document.frmCat.selectedEncuesta != null && document.frmCat.selectedEncuesta != "") {
                    alert("Se eliminara toda informacion de la encuesta " + document.frmCat.selectedEncuesta.value + " Desea continuar?" );
                    document.frmCat.hidAccion.value = "Eliminar";
                    document.frmCat.submit();
                }
                else {
                    alert("Debe seleccionar una encuesta.");
                }
            }
            function Cambiar()
            {
                document.frmCat.hidAccion.value="CAMBIAR";
                document.frmCat.submit();
            }
            $(function () {
                var filas = document.getElementById('tblEncuestas').getElementsByTagName('tbody')[0].getElementsByTagName('tr');
                resetStyle(filas);
            });
            function VerPestana(sel)
            {
                var taeID = document.getElementById("E_TAE_ID");
                var BTN_ENC = document.getElementById("BTN_ENC");
                var BTN_APL = document.getElementById("BTN_APL");
                var BTN_SER = document.getElementById("BTN_SER");
                var BTN_PRE = document.getElementById("BTN_PRE");
                var DIV_ENC = document.getElementById("DIV_ENC");
                var DIV_APL = document.getElementById("DIV_APL");
                var DIV_SER = document.getElementById("DIV_SER");
                var DIV_PRE = document.getElementById("DIV_PRE");
                var IFR_APL = document.getElementById("IFR_APL");
                var IFR_SER = document.getElementById("IFR_SER");
                var IFR_PRE = document.getElementById("IFR_PRE");
                BTN_ENC.style.backgroundImage="url(../img/pestanaApagada.jpg)";
                BTN_APL.style.backgroundImage="url(../img/pestanaApagada.jpg)";
                //BTN_SER.style.backgroundImage="url(../img/pestanaApagada.jpg)";
                //BTN_PRE.style.backgroundImage="url(../img/pestanaApagada.jpg)";
                DIV_ENC.style.display="none";
                DIV_APL.style.display="none";
                DIV_SER.style.display="none";
                DIV_PRE.style.display="none";
                IFR_APL.src="";
                IFR_SER.src="";
                IFR_PRE.src="";
                IFR_FOR.src="";
                IFR_AOP.src="";
                IFR_FYAOP.src="";
                switch(sel)
                {
                    case 0:
                        BTN_ENC.style.backgroundImage="url(../img/pestanaResaltada.jpg)";
                        DIV_ENC.style.display="";
                        break;
                    case 1:
                        BTN_APL.style.backgroundImage="url(../img/pestanaResaltada.jpg)";
                        IFR_APL.src="catencapl.jsp?id="+taeID.value;
                        DIV_APL.style.display="";
                        break;
                    case 2:
                        BTN_SER.style.backgroundImage="url(../img/pestanaResaltada.jpg)";
                        IFR_SER.src="catencser.jsp?id="+taeID.value;
                        DIV_SER.style.display="";
                        break;
                    case 3:
                        BTN_PRE.style.backgroundImage="url(../img/pestanaResaltada.jpg)";
                        IFR_PRE.src="catencpre.jsp?id="+taeID.value;
                        DIV_PRE.style.display="";
                        break;
                }
            }
            
            function Calendario()
            {
                var strUrl="../calendario.jsp";
                var car="width=250,height=250,Scrollbars=NO,Resizable=NO,Menubar=NO,Toolbar=NO";
                window.open(strUrl, "calendario", car);
            }
            Shadowbox.init({
                handleOversize: "drag",
                modal: true				
            });/**/
        </script>
        <style type="text/css">
            #apDiv1 {
                position:absolute;
                width:400px;
                height:100px;
                z-index:1;
                left: 550px;
                top: 16px;
            }
        </style>		

    </head>
    <body>
        <jsp:useBean id="srvEncuestas" class="lib.beans.Encuestas" />
        <%
            HttpSession sesion=request.getSession();
            System.out.println("sesion="+sesion);
        %>
        <table cellpadding="0" cellspacing="0" align="center" style="width:100%;height:600px;">
            <tr>
                <td style="width:310px;height:100px;"><img src="../img/evaLogoProsa.jpg"/></td>
                <td class="TituloSistema"><img src="../img/evaTitulo.jpg"/>

                    <div id="apDiv1">
                        <table width="378" height="50" border="0" top: 16px >
                               <tr>
                                <td ><button id="opener">Instrucciones: Crea Encuestas </button></td>
                            </tr>
                            <tr>
                                <td id="dialog" title="Crea Encuestas">Dar de alta una ecuesta: <br>
                                    Asigne una clave personal a la encuesta
                                    De nombre a la encuesta
                                </td>
                            </tr>
                        </table>
                    </div>

                </td>
                <td class="infoUser"><%@include file="cabecero.jsp" %></td>
            </tr>
            <tr>
                <td style="width:310px;height:500px;">
                    <table cellpadding="0" cellspacing="0" style="width:310px;height:500px;">
                        <tr>
                            <td style="width:150px;height:155px;" rowspan="3"><img src="../img/evaMenu1.jpg"/></td>
                            <td style="width:160px;height:55px;"><A href="creaencuestas.jsp" title="Creaci&oacute;n de encuestas"><img border="0" src="../img/evaBtnEncuestas.jpg"/></A></td>
                        </tr>
                        <tr>
                            <td style="width:160px;height:50px;"><A href="catalogos.jsp" title="Administraci&oacute;n de Cat&aacute;logos"><img border="0" src="../img/evaBtnCatalogos.jpg"/></A></td>
                        </tr>
                        <tr>
                            <td style="width:160px;height:50px;"><A href="consencuestas.jsp" title="Ver Encuestas"><img border="0" src="../img/evaBtnCreacion.jpg"/></A></td>
                        </tr>
                        <tr>
                            <td style="width:310px;height:345px;" colspan="2"><img src="../img/evaMenu2.jpg"/></td>
                        </tr>
                    </table>
                </td>
                <td style="width:100%;height:500px;" colspan="2">
                    <div id="backgroundLeft">
                        <div id="backgroundRight">
                            <table cellpadding="0" cellspacing="0" style="width:90%;">
                                <tr><td style="width:100%;height:35px">
                                        <table cellpadding="0" cellspacing="0">
                                            <tr style="height:10px;"><td colspan="7" style="font-size:5px;">&nbsp;</td></tr>
                                            <tr style="height:25px;">
                                                <td style="width:10px;">&nbsp;</td>
                                                <th align="center" name="BTN_ENC" id="BTN_ENC" style="width:90px;background-image:url(../img/pestanaResaltada.jpg)"><A class="menuPest" HREF="JavaScript:VerPestana(0);">Encuestas</A></th>
                                                <%--<th align="center" name="BTN_APL" id="BTN_APL" style="width:100px;background-image:url(../img/pestanaApagada.jpg)"><A class="menuPest" HREF="JavaScript:VerPestana(1);">Entornos</A></th>
                                                <th align="center" name="BTN_SER" id="BTN_SER" style="width:80px;background-image:url(../img/pestanaApagada.jpg)"><A class="menuPest" HREF="JavaScript:VerPestana(2);">Servicios</A></th>
                                                <th align="center" name="BTN_PRE" id="BTN_PRE" style="width:80px;background-image:url(../img/pestanaApagada.jpg)"><A class="menuPest" HREF="JavaScript:VerPestana(3);">Preguntas</A></th>--%>

                                            </tr>
                                        </table>
                                    </td></tr>
                                <tr >
                                    <td style="width:100%;height:465px;" valign="top">
                                        <form name="frmCat" id="frmCat" method="POST" action="creaencuestas.jsp">
                                            <input type="hidden" name="hidAccion" id="hidAccion"/>
                                            <input type="hidden" name="E_TAE_ID" id="E_TAE_ID"/>
                                            <input type="hidden" name="selectedEncuesta" id="selectedEncuesta"/>
                                            <%
                                                String acc = request.getParameter("hidAccion");
                                                if(acc != null) {
                                                        if(acc.equals("Eliminar")) {
                                                                String id = request.getParameter("selectedEncuesta");
                                                                if(id != null && id != "") {
                                                                        srvEncuestas.deleteEncuesta(Integer.valueOf(id));
                                                                }
                                                        }
                                                }
                                            %>
                                            <DIV name="DIV_ENC" id="DIV_ENC" style="display:block;">
                                                <%-----------------------------------------------------------------------------#
                                                #-Marca del Cambio : C-08-2075-11           Inicia     Modificacion 04/09/2011 #
                                                #-----------------------------------------------------------------------------%>
                                                <%--
                                                        <table><tr>
                                                                <td>
                                                                <table class="styleTabla" style="width:240px">
                                                                        <tr class="styleRenglonClaro"><th colspan="2" align="center">Vigencia de Asignaci&oacute;n</th></tr>
                                                                        <tr class="styleRenglonClaro">
                                                                                <th  align="center">Inicio</th>
                                                                                <th  align="center">Fin</th></tr>
                                                                        <tr class="styleRenglonClaro">
                                                                                <td><input type="text" name="E_TAE_FECHA_INI_ASIGNAR" id="E_TAE_FECHA_INI_ASIGNAR" style="width:120px;"/></td>
                                                                                <td><input type="text" name="E_TAE_FECHA_FIN_ASIGNAR" id="E_TAE_FECHA_FIN_ASIGNAR" style="width:120px;"/></td>
                                                                        </tr>
                                                                </table>
                                                                </td>
                                                                <td>
                                                                <table class="styleTabla" style="width:240px">
                                                                        <tr class="styleRenglonClaro"><th colspan="2" align="center">Vigencia de Evaluaci&oacute;n</th></tr>
                                                                        <tr class="styleRenglonClaro">
                                                                                <th align="center">Inicio</th>
                                                                                <th align="center">Fin</th></tr>
                                                                        <tr class="styleRenglonClaro">
                                                                                <td><input type="text" name="E_TAE_FECHA_INI_EVALUAR" id="E_TAE_FECHA_INI_EVALUAR" style="width:120px;"/></td>
                                                                                <td><input type="text" name="E_TAE_FECHA_FIN_EVALUAR" id="E_TAE_FECHA_FIN_EVALUAR" style="width:120px;"/></td>
                                                                        </tr>
                                                                </table>
                                                                </td>
                                                        </tr></table>
                                                --%>
                                                <%-----------------------------------------------------------------------------#
                                                #-Marca del Cambio : C-08-2075-11           Termina    Modificacion 04/09/2011 #
                                                #-----------------------------------------------------------------------------%>
                                                <table width="100%" cellpadding="5" cellspacing="5">
                                                    <tr>
                                                        <td width="15">&nbsp;
                                                        </td>
                                                        <td width="188" align="left">                                                
                                                        </td>
                                                    </tr>                                       
                                                    <tr><td width="15">&nbsp;</td>
                                                        <td align="center">
                                                            <DIV style="width:90%;height:230px;overflow:auto;">
                                                                <table class="styleTabla" id="tblEncuestas" onClick="">
                                                                    <tr class="styleCabeceroTabla" name="TR_ROW_1" id="TR_ROW_1">
                                                                        <th name="TD_ROW_1_COL_0_FNAME_TAE_ID" id="TD_ROW_1_COL_0_FNAME_TAE_ID">Encuesta ID</th>
                                                                        <th name="TD_ROW_1_COL_1_FNAME_TAE_CVE_ENCUESTA" id="TD_ROW_1_COL_1_FNAME_TAE_CVE_ENCUESTA">Clave</th>
                                                                        <th name="TD_ROW_1_COL_2_FNAME_TAE_NOMBRE_ENCUESTA" id="TD_ROW_1_COL_2_FNAME_TAE_NOMBRE_ENCUESTA">Nombre</th>
                                                                        <th name="TD_ROW_1_COL_3_FNAME_TAE_TIPO_ENCUESTA" id="TD_ROW_1_COL_3_FNAME_TAE_TIPO_ENCUESTA">Tipo</th>
                                                                        <th name="TD_ROW_1_COL_4_FNAME_TAE_FECHA_INI_ASIGNAR" id="TD_ROW_1_COL_4_FNAME_TAE_FECHA_INI_ASIGNAR">VigIniAsig</th>
                                                                        <th name="TD_ROW_1_COL_5_FNAME_TAE_FECHA_FIN_ASIGNAR" id="TD_ROW_1_COL_5_FNAME_TAE_FECHA_FIN_ASIGNAR">VigFinAsig</th>
                                                                    </tr>
                                                                    <c:forEach var="encuesta" items="${srvEncuestas.encuestas}">                                                            
                                                                        <tr class="" onClick="setSelectedEncuesta(this.rowIndex, ${encuesta.value.id});">
                                                                            <td>${encuesta.value.id}</td>
                                                                            <td>${encuesta.value.clave}</td>
                                                                            <td>${encuesta.value.nombre}</td>
                                                                            <td>${encuesta.value.tipo}</td>
                                                                            <td>${encuesta.value.inicio}</td>
                                                                            <td>${encuesta.value.fin}</td>
                                                                        </tr>
                                                                    </c:forEach>
                                                                </table>
                                                            </DIV>
                                                        </td>
                                                    </tr>
                                                    <tr><td width="15">&nbsp;</td>
                                                        <td align="center">
                                                            <div id="Botones">
                                                                <ul>
                                                                    <li>
                                                                        <a rel="shadowbox;width=850;height=550" href="importenc.jsp">Importar Excel</a>
                                                                    </li>
                                                                    <li>
                                                                        <a rel="shadowbox;width=850;height=550" href="wEncuesta.jsp">Agregar</a>
                                                                    </li>
                                                                    <li>
                                                                        <a id="modEncuesta" rel="shadowbox;width=850;height=550" href="wEncuesta.jsp?accion=Modificar&idEncuesta=" >Cambiar</a>
                                                                        <!--<a id="modEncuesta" rel="shadowbox;width=850;height=550" href="Modificar();">Cambiar</a>-->
                                                                        <!--<a id="modEncuesta" rel="shadowbox;width=850;height=550" href="wEncuesta.jsp?Encuesta=${param.selectedEncuesta}&accion=Modificar"-->
                                                                        <!-- <a id="modEncuesta" rel="shadowbox;width=850;height=550" href="wEncuesta.jsp?Encuesta=%P%&accion=Modificar" >Cambiar</a>onClick="location.href=this.href+'?Encuesta='+document.frmCat.selectedEncuesta.value;" -->
                                                                    </li>
                                                                    <!--                                                    
                                                                    <li onClick="Modificar();">
                                                                        Cambiar
                                                                    </li>
                                                                    -->
                                                                    <li onClick="Eliminar();">
                                                                        Eliminar
                                                                    </li>
                                                                </ul>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                    <%=Msg%>                                        
                                                </table>
                                            </DIV>
                                        </form>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </td>
            </tr>
        </table>
    </body>
</html>