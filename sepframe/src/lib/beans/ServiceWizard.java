package lib.beans;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lib.clsDB;
import lib.entities.Aplicacion;
import lib.entities.Conducta;
import lib.entities.Encuesta;
import lib.entities.Entorno;
import lib.entities.Pagina;
import lib.entities.Servicio;

public class ServiceWizard {
	HashMap<Integer, Pagina> paginas;
	Hashtable<Integer, Hashtable<Integer, Conducta>> detConductas;
	Hashtable<Integer, Hashtable<Integer, Aplicacion>> detAplicaciones;
	Hashtable<Integer, Hashtable<Integer, Servicio>> detServicios;
	HttpServletResponse response;

	private Encuestas encuestas;
	private Entornos entornos;
	private Conductas conductas;
	private Aplicaciones aplicaciones;
	private Servicios servicios;

	private boolean processError;
	private String message;
	private boolean valida;
	private int currentPage;
	public String accion;
	private String currentEntorno;
	public Encuesta encuesta;
	public String idEncuesta;
	public boolean created = false;

	private boolean aGenteProsa;
	private boolean aAplicaciones;
	private boolean aServicios;
	private boolean bandera = false;
	private int conductasAsig;
	private Hashtable<Integer, Aplicacion> appsAsignadas;
	public String listAplicaciones;
	private String listServicios;

	public ServiceWizard() {
		initialize();
		populatePaginas();
	}

	public Encuesta getEncuesta() {
		return this.encuesta;
	}

	public void populatePaginas() {
		paginas = new HashMap<Integer, Pagina>();

		String[] pags = { "creaencuestas.jsp", "wEncuesta.jsp",
				"wAspectos.jsp?accion=Entrando",
				"catenent.jsp?accion=Entrando", "catencon.jsp",
				"catencapl.jsp?accion=Entrando",
				"catencser.jsp?accion=Entrando", "wFinal.jsp?accion=Final" };
		Pagina pagina;
		for (int p = 1; p <= pags.length; p++) {
			pagina = new Pagina();
			pagina.setNombre(pags[p - 1]);
			pagina.setActiva(true);
			paginas.put(p, pagina);
		}
	}

	public void initialize() {
		System.out.println("Inicializando... Wizard");
		entornos = new Entornos();
		conductas = new Conductas();
		conductas.buildConductas();
		System.out.println("Lista de conductas: "+ conductas.getTotalConductas());

		aplicaciones = new Aplicaciones();
		aplicaciones.buildAplicaciones();
		System.out.println("Lista de aplicaciones: "+ aplicaciones.getTotalAplicaciones());
		listAplicaciones="";
		System.out.println(listAplicaciones+"--");
		servicios = new Servicios();
		servicios.buildServicios();
		System.out.println("Lista de servicios: "
				+ servicios.getTotalServicios());
		detConductas = new Hashtable<Integer, Hashtable<Integer, Conducta>>();
		detAplicaciones = new Hashtable<Integer, Hashtable<Integer, Aplicacion>>();
		detServicios = new Hashtable<Integer, Hashtable<Integer, Servicio>>();

		processError = false;
		message = null;
		valida = false;
		currentPage = 2;
		accion = null;
		currentEntorno = null;
		encuesta = null;

		aGenteProsa = false;
		aAplicaciones = false;
		aServicios = false;
		conductasAsig=0;
		bandera=false;
	}

	public void setCurrentEntorno(String idEntorno) {
		if (idEntorno != null) {
			if (entornos != null) {
				entornos.getEntorno(Integer.valueOf(idEntorno)).setSelected(true);
			} else {
				message = "No se inicializaron los entornos";
				System.out.println(message);
				return;
			}
			if (currentEntorno != null) {
				if (!this.currentEntorno.equals(idEntorno)) {
					System.out.println("currentEntorno="+currentEntorno+"idEntorno="+idEntorno);
					System.out.println("entornos ="+entornos);
					entornos.getEntornoDisponibleById(Integer.valueOf(currentEntorno)).setSelected(false);
				}
			}
			this.currentEntorno = idEntorno;
		}
	}

	public String getCurrentEntorno() {
		return this.currentEntorno;
	}

	public Conductas getConductas() {
		return this.conductas;
	}

	public Entornos getEntornos() {
		return this.entornos;
	}

	public Aplicaciones getAplicaciones() {
		return this.aplicaciones;
	}

	public String getListAplicaciones() {
		return listAplicaciones;
	}

	public void setListAplicaciones(String listAplicaciones) {
		this.listAplicaciones = listAplicaciones;
	}

	public boolean isBandera() {
		return bandera;
	}

	public void setBandera(boolean bandera) {
		this.bandera = bandera;
		System.out.println("bandera********************************= "+bandera);
	}

	public int getConductasAsig() {
		return conductasAsig;
	}

	public void setConductasAsig(int conductasAsig) {
		this.conductasAsig = conductasAsig;
	}

	public Servicios getServicios() {
		return this.servicios;
	}

	public Hashtable<Integer, Hashtable<Integer, Conducta>> getDetConductas() {
		return this.detConductas;
	}

	public Hashtable<Integer, Entorno> getListEntornosDisponibles() {
		return this.entornos.getEntornosDisponibles();
	}

	public Hashtable<Integer, Entorno> getListEntornosAsignados() {
		return this.entornos.getEntornosAsignados();
	}

	public Hashtable<Integer, Conducta> getListConductasDisponibles() {
		return this.conductas.getConductasDisponibles();
	}

	public Hashtable<Integer, Conducta> getListConductasAsignadas() {
		if (currentEntorno != null) {
			System.out.println("currentEntorno= "+currentEntorno);
			return this.detConductas.get(Integer.valueOf(currentEntorno));
		}
		return null;
	}

	public Hashtable<Integer, Aplicacion> getListAplicacionesDisponibles() {
		return this.aplicaciones.getAplicacionesDisponibles();
	}

	public Hashtable<Integer, Aplicacion> getListAplicacionesAsignadas() {
		return this.aplicaciones.getAplicacionesAsignadas();
	}

	public Hashtable<Integer, Servicio> getListServiciosDisponibles() {
		return this.servicios.getServiciosDisponibles();
	}

	public Hashtable<Integer, Servicio> getListServiciosAsignados() {
		return this.servicios.getServicios();
	}

	public void setPagina(int pagina) {
		this.currentPage = pagina;
	}

	public boolean getProcessError() {
		return this.processError;
	}

	public String getMessage() {
		return this.message;
	}

	public void processRequest(HttpServletRequest request,HttpServletResponse response) {
		// public void processRequest() {
		/*
		 * -- Tenemos 3 acciones que pueden suceder 1.Crear una encuesta
		 * 2.Modificar una encuesta 3.Eliminar una encuesta 1. Para crear una
		 * Encuesta el parametro xxx debe ser nulo 2. Para Modificar y Eliminar
		 * una encuesta el parametro idEncuesta no debe ser nulo Nota: Se debe
		 * distinguir la accion.
		 */
		this.response = response;
		System.out.println("\n\n\nURL: " + request.getRequestURL()+ " tamparametros: " + request.getParameterMap().size());

		// Al abrir la pantalla del wizard desde la pantalla de crearion de
		// encuestas
		// se manda el parametro de accion o el id de la encuesta
		// if(request.getParameterMap().size() > 0) {
		if(idEncuesta==null){
			idEncuesta = request.getParameter("idEncuesta");
		}
		System.out.println("idEncuesta=" + idEncuesta+"--");
		accion = request.getParameter("accion");
		System.out.println("La accion en Wizard es " + accion+ " currentPage - " + currentPage);
		if (accion != null) {
			System.out.println("encuestas="+encuestas);
			if (encuestas == null) {
				encuestas = new Encuestas();
			}
			System.out.println("encuestas.size="+encuestas.getEncuestas().size());
			StringTokenizer tk;
			switch (currentPage) {
			case 2:
				if (accion.equals("Modificar")) {
					System.out.println("Modificar la encuesta:" + idEncuesta+"--");
					encuesta = encuestas.getEncuesta(Integer.valueOf(idEncuesta));
					if (encuesta != null) {
						created = true;
						valida = true;
					}
				} else if (accion.equals("Eliminar")) {
					System.out.println("Eliminar la encuesta: " + idEncuesta);
					encuestas.deleteEncuesta(Integer.valueOf(idEncuesta));
					movePage(1);
				} else if (accion.equals("Crear")) {
					if (encuesta == null) {
						encuesta = encuestas.creaEncuesta(request);
					}
					System.out.println("Se creo la encuesta: "
							+ encuesta.getClave() + " nombre: "
							+ encuesta.getNombre());
					if (encuesta.getClave() != 0) {
						accion = "Siguiente";
						created = true;
						valida = true;
					} else {
						message = "La clave de la encuesta no puede ser 0";
					}
				} else if (accion.equals("Siguiente")) {
					valida = true;
				}
				break;
			case 3:
				/*
				 * Si EvaluaGenteProsa activa la pagina catenent activa la
				 * pagina catencon Si Aplicaciones activa la pagina catenapl Si
				 * Servicios activa la pagina catenser
				 */
				String aGenteProsa = null,
				aAplicaciones = null,
				aServicios = null;
				valida = false;

				if (encuesta != null) {
					System.out.println("WAS " + encuesta.getId());
					if (encuesta.getId() != 0) {
						// Por encuesta
						setAspectos(aGenteProsa, aAplicaciones, aServicios);
					} else {
						// Por parametros
						aGenteProsa = request.getParameter("GenteProsa");
						aAplicaciones = request.getParameter("Aplicaciones");
						aServicios = request.getParameter("Servicios");
						if (aGenteProsa != null) {
							if (aGenteProsa.equals("on")) {
								setEvaluaGenteProsa(true);
							} else {
								setEvaluaGenteProsa(false);
							}
						}
						if (aAplicaciones != null) {
							if (aAplicaciones.equals("on")) {
								setEvaluaAplicaciones(true);
							} else {
								setEvaluaAplicaciones(false);
							}
						}
						if (aServicios != null) {
							if (aServicios.equals("on")) {
								setEvaluaServicios(true);
							} else {
								setEvaluaServicios(false);
							}
						}
					}
				}
				System.out.println("GenteProsa: " + getEvaluaGenteProsa());
				System.out.println("Aplicaciones: " + getEvaluaAplicaciones());
				System.out.println("Servicios: " + getEvaluaServicios());
				Pagina pag = new Pagina();
				for (int p = 4; p <= paginas.size(); p++) {
					pag = paginas.get(p);
					switch (p) {
					case 4:
						pag.setActiva(getEvaluaGenteProsa());
						p++;
						pag = paginas.get(p);
						pag.setActiva(getEvaluaGenteProsa());
						break;
					case 6:
						pag.setActiva(getEvaluaAplicaciones());
						break;
					case 7:
						pag.setActiva(getEvaluaServicios());
						break;
					}
				}
				// * Quitar codigo, muestra las pantallas activas
				for (int pa = 1; pa <= paginas.size(); pa++) {
					Pagina pagina = paginas.get(pa);
					System.out.println("Pagina: " + pagina.getNombre()
							+ " activa: " + pagina.isActiva());
				}
				// */
				valida = true;
				break;
			case 4:
				//gente prosa
				System.out.println("Pagina :"+ paginas.get(currentPage).getNombre() + " Opcion 4");
				// Entornos
				if (encuesta != null) {
					// El id de la encuesta aun no se crea!
					if (accion.equals("Entrando")) {
						idEncuesta = String.valueOf(encuesta.getId());
						if (idEncuesta == null) {
							idEncuesta = "0";
						}
						if (!idEncuesta.equals(entornos.getIdEncuesta())) {
							entornos = new Entornos(idEncuesta);
							entornos.buildEntornosDisponilbes();
						}
						System.out.println("Inicializando entornos disponibles "+ entornos.getTotalEntornosDisponibles());
					}
					// else if(accion.equals("Siguiente")) {
					String listEntornos = request.getParameter("listEntornos");
					System.out.println("Se obtuvo la lista de entornos para asignar "+ listEntornos+"--");
					System.out.println("Se han asignado "+ entornos.getTotalEntornosAsignados());
					Hashtable<Integer, Entorno> asignadosTemp=null;
					Hashtable<Integer, Entorno> asignados=null;
					if (listEntornos != null && listEntornos !="") {
						if (entornos.getTotalEntornos() == 0) {
							entornos.buildEntornos();
						}
						asignados = entornos.getEntornosAsignados();
						tk = new StringTokenizer(listEntornos, ",");
						Entorno entorno;
						if (/*tk.countTokens() < asignados.size()&&*/ tk.countTokens() != 0) {
							asignadosTemp = (Hashtable<Integer, Entorno>) asignados.clone();
							asignados.clear();
						}
						while (tk.hasMoreTokens()) {
							int ent = Integer.valueOf(tk.nextToken());
							System.out.println("Asignando el entorno " + ent);
							if (!asignados.containsKey(ent)) {
								entorno = entornos.getEntornoDisponibleById(ent);
								asignados.put(entorno.getId(), entorno);
							}
						}
						if (asignados!=null && asignados.size() > 0) {
							Iterator<Entorno> iEntornos = asignados.values().iterator();
							while (iEntornos.hasNext()) {
								entorno = iEntornos.next();
								entornos.getEntornosDisponibles().remove(entorno.getId());
							}
							//valida = true;
							System.out.println("Tamaño entornos asignados: "+ asignados.size());
							System.out.println("Tamaño entornos disponibles: "+ entornos.getTotalEntornosDisponibles());
						}
					}	
						
					System.out.println("asignados=" + asignados);
					if (asignados != null && asignados.size() > 0) {
						// Las aplicaciones se guardaran por clave, ya que
						// cuando no se ha creado la encuesta, no existe id
						//Hashtable<Integer, Aplicacion> appsAsignadasTemp = (Hashtable<Integer, Aplicacion>) appsAsignadas.clone();
						System.out.println("asignadosTemp="+asignadosTemp);
						System.out.println("asignados="+asignados);
						//det = new Hashtable<Integer, Hashtable<Integer, Entorno>>();
						//detAplicaciones.put(encuesta.getClave(), appsAsignadas);
						if(asignadosTemp!=null /*&& asignados.size()<asignadosTemp.size()*/){
							for(Entorno a : asignadosTemp.values()){
								if(!asignados.containsValue(a)){
									entornos.getEntornosDisponibles().put(a.getId(), a);
								}
							}
						}
						valida = true;
						System.out.println("Tamaño entornos asignados "+ asignados.size()+ " total entornos disponibles "+ entornos.getTotalEntornosDisponibles());
					}
						
					if (accion.equals("Atras")) {
						valida = true;
					}
					
					// }
				}
				break;
			case 5:
				System.out.println("Pagina :"+ paginas.get(currentPage).getNombre()+"--");
				// Conductas
				// Las conductas cambian de acuerdo al entorno seleccionado
				/*
				 * Tenemos los entornos asignados cada entorno debe tener cierta
				 * cantidad de conductas
				 */
				if (encuesta != null && entornos.getTotalEntornosAsignados() > 0) {
					String ent = request.getParameter("selectedEntorno");
					System.out.println("selectedEntorno: " + ent+ "- total de entornos: "+ entornos.getTotalEntornos());
					Iterator<Entorno> iEnts = entornos.getEntornosAsignados().values().iterator();
					while (iEnts.hasNext()) {
						Entorno ient = iEnts.next();
						System.out.println("Entorno asignado: " + ient.getId()+ " Nombre: " + ient.getNombre());
					}
					if (ent != null && ent != "") {
						setCurrentEntorno(ent);
					}
					if (currentEntorno != null && currentEntorno != "") {
						System.out.println("Entorno seleccionado: "+ currentEntorno);
						String listConductas = request.getParameter("listConductas");
						System.out.println("listConductas:"+ listConductas+"--");
						if (listConductas != null) {
							if (conductas.getTotalConductas() == 0) {
								conductas.buildConductas();
							}
							System.out.println("El total de conductas: "+ conductas.getTotalConductas());
							System.out.println("Lista de conductas asignadas:"+ listConductas+"--");
							Hashtable<Integer, Conducta> conductasAsignadas;
							Hashtable<Integer, Conducta> conductasAsignadasTemp=null;
							Conducta con;
							String id = String.valueOf(encuesta.getId());
							System.out.println("Id encuesta: " + id);
	
							// Verifica que el entorno tenga conductas asignadas
							// en el programa
							int entorno = Integer.valueOf(currentEntorno);
							if (detConductas.containsKey(entorno)) {
								conductasAsignadas = detConductas.get(entorno);
								System.out.println("El entorno " + entorno+ " tiene " + conductasAsignadas.size()+ " conductas asignadas");
							} else {
								System.out.println("El entorno " + entorno+ " no tiene conductas.");
								conductasAsignadas = new Hashtable<Integer, Conducta>();
							}
	
							/***********if (id != null && !id.equals("0")) {
								// La encuesta existe el id es diferente de 0
								conductas.setIdEncuesta(id);
								conductas.setIdEntorno(currentEntorno);
								// Construye las conductas asignadas para esa encuesta y entorno
								conductas.buildConductasAsignadas();
								System.out.println("La encuesta: "+ id+ " existe y tiene: "+ conductas.getTotalConductasAsignadas());
								// Las conductas asignadas son las que se
								// encuentran en la base, solamente se deben de
								// tomar una vez
								conductasAsignadas = conductas.getConductasAsignadas();
							}**********/
							// Comparar las conductas asignadas contra la lista
							// de conductas asignadas
						//--------------------if (listConductas != null) {
							tk = new StringTokenizer(listConductas, ",");
							System.out.println("Contador de tokens: "+ tk.countTokens()+"\nconductasAsignadas.size()"+conductasAsignadas.size());
							if ((tk.countTokens() < conductasAsignadas.size() && tk.countTokens() != 0) ||  listConductas.equals("")) {
								conductasAsignadasTemp = (Hashtable<Integer, Conducta>) conductasAsignadas.clone();
								System.out.println("CLONE");
								conductasAsignadas.clear();
							}
							// Obtiene las conductas disponibles para ese
							// entorno
							conductas.buildConductasDisponilbes(id,currentEntorno);
							System.out.println("El total de conductas disponibles: "+ conductas.getTotalConductasDisponibles());
							while (tk.hasMoreTokens()) {
								// Id de la conducta asignada
								System.out.println("-Entra en el while");
								int num = Integer.valueOf(tk.nextToken());
								if (!conductasAsignadas.containsKey(num)) {
									con = conductas.getConductaDisponibleById(num);
									conductasAsignadas.put(num, con);
									//conductas.getConductasDisponibles().remove(con.getId());
								}
							}
							Iterator<Conducta> iAsignadas = conductasAsignadas.values().iterator();
							//System.out.println("iAsignadas="+iAsignadas);
							while (iAsignadas.hasNext()) {
								con = iAsignadas.next();
								System.out.println("con="+con);
								conductas.getConductasDisponibles().remove(con.getId());
							}
							// Quitar los entornos que no se asignaron
							System.out.println("conductasAsignadasTemp="+conductasAsignadasTemp);
							System.out.println("conductasAsignadas="+conductasAsignadas);
							if (conductasAsignadas!=null && conductasAsignadas.size() > 0) {
								//detConductas= new Hashtable<Integer, Hashtable<Integer,Conducta>>();
								detConductas.put(entorno, conductasAsignadas);
								if(conductasAsignadasTemp!=null /*&& conductasAsignadas.size()<conductasAsignadasTemp.size()*/){
									for(Conducta a : conductasAsignadasTemp.values()){
										if(!conductasAsignadas.containsValue(a)){
											conductas.getConductasDisponibles().put(a.getId(), a);
										}
									}
								}
								valida = true;
								System.out.println("Se asignaron "+ conductasAsignadas.size()+ " conductas para el entorno: "+ entorno);
							}else{
								if(listConductas.equals("")&& accion.equals("Quitar")){
									conductasAsignadas=new Hashtable<Integer, Conducta>();
									detConductas.put(entorno, conductasAsignadas);
									conductas.buildConductasDisponilbes(id, currentEntorno);
								}else{
									for(Conducta a : conductasAsignadasTemp.values()){
										if(!conductasAsignadas.containsValue(a)){
											conductasAsignadas.put(a.getId(), a);
											conductas.getConductasDisponibles().remove(a.getId());
										}
									}
								}
							}
							if (accion.equals("Atras")) {
								currentEntorno=null;
								conductas.initializeConductas();
								for(Entorno e : entornos.getEntornosAsignados().values()){
									e.setSelected(false);
								}
								valida = true;
							}else if (accion.equals("Quitar")) {
								valida = true;
							}
							else if (accion.equals("Siguiente") || accion.equals("Finalizar")) {
								valida = true;
								for(Entorno e: entornos.getEntornosAsignados().values()){
									//setCurrentEntorno(e.getId()+"");
									Hashtable<Integer, Conducta> condsPorEnt = detConductas.get(e.getId());
									if(condsPorEnt==null || condsPorEnt.isEmpty()){
										valida = false;
										break;
									}
								}
							}
							System.out.println("El total de conductas disponibles: "+ conductas.getTotalConductasDisponibles());
						}
					}
				} else {
					System.out.println("La encuesta no existe");
				}
				break;
			case 6:
				// Aplicaciones
				System.out.println("******************Aplicaciones*******************");
				System.out.println("Pagina :"+ paginas.get(currentPage).getNombre());
				valida = false;
				System.out.println("encuesta=" + encuesta);
				if (encuesta != null) {
					idEncuesta = String.valueOf(encuesta.getId());
					System.out.println("idEncuesta=" + idEncuesta);
					if (idEncuesta == null) {
						idEncuesta = "0";
					}
					if (aplicaciones.getTotalAplicacionesDisponibles() == 0) {
						System.out.println("Construyendo aplicacoines disponibles");
						aplicaciones.setEncuesta(idEncuesta);
						aplicaciones.buildAplicacionesDisponibles();
						System.out.println("Aplicaciones disponibles - "+ aplicaciones.getTotalAplicacionesDisponibles());
					}
					if (aplicaciones.getTotalAplicaciones() == 0) {
						System.out.println("Construyendo el catalogo de aplicaciones");
						aplicaciones.buildAplicaciones();
						System.out.println("Aplicaciones - "+ aplicaciones.getTotalAplicaciones());
					}
					System.out.println("Id encuesta Wizard - " + idEncuesta);
					if (detAplicaciones.containsKey(encuesta.getClave())) {
						appsAsignadas = detAplicaciones.get(encuesta.getClave());
						System.out.println("appsAsignadas = detAplicaciones.get(idEncuesta);");
					} else {
						appsAsignadas = new Hashtable<Integer, Aplicacion>();
						System.out.println("appsAsignadas = new Hashtable<Integer, Aplicacion>();");
					}
					System.out.println("appsAsignadas=" + appsAsignadas);
					if (idEncuesta != null /*&& /* ! * /idEncuesta.equals("0")*/) {
						// La encuesta existe el id es diferente de 0
						// Construye las conductas asignadas para esa encuesta y
						// entorno
						aplicaciones.setEncuesta(idEncuesta);
						aplicaciones.buildAplicacionesAsignadas();
						System.out.println("La aplicacion: " + idEncuesta
								+ " existe y tiene: "
								+ aplicaciones.getTotalAplicacionesAsignadas());
						// Las aplicaciones asignadas son las que se encuentran
						// en la base, solamente se deben de tomar una vez
						appsAsignadas = aplicaciones.getAplicacionesAsignadas();
						aplicaciones.getAplicacionesAsignadas();
					}
					listAplicaciones = request.getParameter("listAplicaciones");
					System.out.println("listAplicaciones=" + listAplicaciones+"--");
					Hashtable<Integer, Aplicacion> appsAsignadasTemp=null;
					if (listAplicaciones != null && listAplicaciones != "") {
						tk = new StringTokenizer(listAplicaciones, ",");
						if (tk.countTokens() < appsAsignadas.size()&& tk.countTokens() != 0) {
							appsAsignadasTemp = (Hashtable<Integer, Aplicacion>) appsAsignadas.clone();
							appsAsignadas.clear();
						}
						while (tk.hasMoreTokens()) {
							int app = Integer.valueOf(tk.nextToken());
							System.out.println("Asignando la aplicacion: "+ app);
							if (!appsAsignadas.containsKey(app)) {
								Aplicacion aplicacion = aplicaciones.getAplicacionDisponibleById(app);
								appsAsignadas.put(aplicacion.getId(),aplicacion);
							}
						}
						Iterator<Aplicacion> iAplicaciones = aplicaciones.getAplicacionesAsignadas().values().iterator();
						while (iAplicaciones.hasNext()) {
							int idAplicacion = iAplicaciones.next().getId();
							aplicaciones.getAplicacionesDisponibles().remove(idAplicacion);
						}
						System.out.println("Tamaño aplicaciones asignadas: "+ appsAsignadas.size());
						System.out.println("Tamaño aplicaciones disponibles: "+ aplicaciones.getTotalAplicacionesDisponibles());
					}
					System.out.println("appsAsignadas=" + appsAsignadas);
					if (appsAsignadas != null && appsAsignadas.size() > 0) {
						// Las aplicaciones se guardaran por clave, ya que
						// cuando no se ha creado la encuesta, no existe id
						//Hashtable<Integer, Aplicacion> appsAsignadasTemp = (Hashtable<Integer, Aplicacion>) appsAsignadas.clone();
						System.out.println("appsAsignadasTemp="+appsAsignadasTemp);
						System.out.println("appsAsignadas="+appsAsignadas);
						detAplicaciones = new Hashtable<Integer, Hashtable<Integer, Aplicacion>>();
						detAplicaciones.put(encuesta.getClave(), appsAsignadas);
						if(appsAsignadasTemp!=null && appsAsignadas.size()<appsAsignadasTemp.size()){
							for(Aplicacion a : appsAsignadasTemp.values()){
								if(!appsAsignadas.containsValue(a)){
									aplicaciones.getAplicacionesDisponibles().put(a.getId(), a);
								}
							}
						}
						valida = true;
					}
					if (accion.equals("Atras")) {
						valida = true;
					}
				}
				break;
			case 7:
				// Servicios
				System.out.println("******************Servicios*******************");
				valida = false;
				System.out.println("encuesta=" + encuesta);
				if (encuesta != null) {
					Hashtable<Integer, Servicio> servsAsignados;
					idEncuesta = String.valueOf(encuesta.getId());
					if (idEncuesta == null) {
						idEncuesta = "0";
					}
					if (servicios.getTotalServiciosDisponibles() == 0) {
						System.out.println("Construyendo servicios disponibles");
						servicios.setEncuesta(idEncuesta);
						servicios.buildServiciosDisponibles();
						System.out.println("Servicios disponibles - " +servicios.getTotalServiciosDisponibles());
					}
					if (servicios.getTotalServicios() == 0) {
						// #System.out.println("Construyendo el catalogo de servicios");
						servicios.buildServicios();
						// #System.out.println("Servicios - " +
						// servicios.getTotalServicios());
					}
					// #System.out.println("Id encuesta Wizard - " + id);
					if (detServicios.containsKey(encuesta.getClave())) {
						servsAsignados = detServicios.get(encuesta.getClave());
						System.out.println("servsAsignados = detServicios.get(encuesta.getClave());");
					} else {
						servsAsignados = new Hashtable<Integer, Servicio>();
						System.out.println("servsAsignados = new Hashtable<Integer, Servicio>();");
					}
					System.out.println("servsAsignados="+servsAsignados);
					if (idEncuesta != null /*&& !idEncuesta.equals("0")*/) {
						// La encuesta existe el id es diferente de 0
						servicios.setEncuesta(idEncuesta);
						servicios.buildServiciosAsignados();
						System.out.println("La encuesta: " + idEncuesta
								+ " existe y tiene: "
								+ servicios.getTotalServiciosAsignados());
						// Los servicios asignados son los que se encuentran en
						// la base, solamente se deben de tomar una vez
						servsAsignados = servicios.getServiciosAsignados();
					}
					listServicios = request.getParameter("listServicios");
					System.out.println("Se obtuvieron los servicios: "+ listServicios);
					Hashtable<Integer, Servicio> servsAsignadosTemp=null;
					if (listServicios != null && listServicios != "") {
						tk = new StringTokenizer(listServicios, ",");
						if (tk.countTokens() < servsAsignados.size()&& tk.countTokens() != 0) {
							servsAsignadosTemp = (Hashtable<Integer, Servicio>) servsAsignados.clone();
							servsAsignados.clear();
						}
						while (tk.hasMoreTokens()) {
							int app = Integer.valueOf(tk.nextToken());
							System.out.println("Asignando el servicio: " + app);
							if (!servsAsignados.containsKey(app)) {
								Servicio servicio = servicios.getServicioDisponibleById(app);
								System.out.println("servicio="+servicio);
								servsAsignados.put(servicio.getId(), servicio);
							}
						}
						if (servicios.getTotalServiciosAsignados() != 0) {
							Iterator<Servicio> iServicios = servicios.getServiciosAsignados().values().iterator();
							while (iServicios.hasNext()) {
								int idAplicacion = iServicios.next().getId();
								servicios.getServiciosDisponibles().remove(idAplicacion);
							}
						} else {
							System.out.println("No hay servicios asignados.");
						}
						System.out.println("Tamaño servicios asignados: "+ servsAsignados.size());
						System.out.println("Tamaño servicios disponibles: "+ servicios.getTotalServiciosDisponibles());
					}
					if (servsAsignados!=null && servsAsignados.size() > 0) {
						// Los servicios se guardaran por clave, ya que cuando
						// no se ha creado la encuesta, no existe id
						System.out.println("serAsignadosTemp="+servsAsignadosTemp);
						System.out.println("serAsignados="+servsAsignados);
						detServicios = new Hashtable<Integer, Hashtable<Integer, Servicio>>();
						detServicios.put(encuesta.getClave(), servsAsignados);
						if(servsAsignadosTemp!=null && servsAsignados.size()<servsAsignadosTemp.size()){
							for(Servicio a : servsAsignadosTemp.values()){
								if(!servsAsignados.containsValue(a)){
									servicios.getServiciosDisponibles().put(a.getId(), a);
								}
							}
						}
						valida = true;
					}
					if (accion.equals("Atras")) {
						valida = true;
					}
					System.out.println("Termina de asignar servicios");
				}
				break;
			}

			System.out.println("MVP Accion: " + accion +"-- y es valida="+valida);
			if (valida) {
				if (accion.equals("Siguiente")) {
					nextPage();
				} else if (accion.equals("Atras")) {
					beforePage();
				} else if (accion.equals("Finalizar")) {
					// Guardar los cambios
					// Cerrar el wizard
					if (encuesta.getId() == 0) {
						System.out.println("Finalizando la creacion de la encuesta");
						beginTransaction();
					} else {
						updateEncuesta();
					}
				}
			}else{
				if (accion.equals("Siguiente") || accion.equals("Finalizar")) {
					System.out.println("no valida");
					//movePage(paginas.get(currentPage));
					try {
						 this.response.setContentType("text/html");
						   PrintWriter out = this.response.getWriter();
						   //RequestDispatcher dispatcher = request.getRequestDispatcher("catencon.jsp");
						   //dispatcher.include(request, this.response);
						   //out.println("<html><head>");
						   //out.flush();
						   out.write("bandera=1; ");
						   //out.println("</scrip>");
					} catch (IOException e) {
						e.printStackTrace();
					}/* catch (ServletException e) {
						e.printStackTrace();
					}*/
				}
			}
		}
		// }
	}

	public void beginTransaction() {
		encuestas.createEncuesta(encuesta);
		encuestas.buildEncuestas();
		encuesta = encuestas.getEncuestaByClave(encuesta.getClave());
		System.out.println("El id de la encuesta creada es - "+ encuesta.getId());
		clsDB cCommit;
		cCommit = new clsDB("AgregaEntorno");
		System.out.println("Se asignaran un total de "+ entornos.getTotalEntornosAsignados() + " entornos.");
		int Encuesta = encuesta.getId();
		Iterator<Entorno> iEnts = entornos.getEntornosAsignados().values().iterator();
		while (iEnts.hasNext()) {
			Entorno ient = iEnts.next();
			cCommit.setParameter(1, Encuesta);
			cCommit.setParameter(2, ient.getId());
			cCommit.executeQuery();
			// #System.out.println("Prepare query AgregaEntorno - " +
			// cCommit.getfinalQuery());
			cCommit.setQuery(cCommit.getQuery());
			System.out.println("El entorno " + ient.getId() + " Nombre: "
					+ ient.getNombre() + " fue asignado.");
		}
		System.out.println("************A los entornos se asignan las conductas");
		cCommit.findQuery("AsignaConducta");
		// #System.out.println("Despues de encontrar el query AsignaConducta + "
		// + cCommit.getfinalQuery());
		if (entornos.getTotalEntornosAsignados() != 0) {
			iEnts = entornos.getEntornosAsignados().values().iterator();
			while (iEnts.hasNext()) {
				
				int idEntorno = iEnts.next().getId();
				Hashtable<Integer, Conducta> conductasAsignadas = detConductas.get(idEntorno);
				System.out.println("Se agregaron "+ conductasAsignadas.values().size() + " conductas");
				Iterator<Conducta> iConductas = conductasAsignadas.values().iterator();
				while (iConductas.hasNext()) {
					int idConducta = iConductas.next().getId();
					cCommit.setParameter(1, idConducta);
					cCommit.setParameter(2, Encuesta);
					cCommit.setParameter(3, idEntorno);
					// #System.out.println("Prepare query AsignaConducta + " +
					// cCommit.getfinalQuery());
					cCommit.executeQuery();
					cCommit.setQuery(cCommit.getQuery());
				}
			}
		} else {
			System.out.println("No se agregaron entornos.");
		}
		// Se asignan las aplicaciones
		cCommit.findQuery("AgregaAplicacion");
		// #System.out.println("Despues de encontrar el query AgregaAplicacion"
		// + cCommit.getfinalQuery());
		if (detAplicaciones.containsKey(encuesta.getClave())) {
			Hashtable<Integer, Aplicacion> appsAsignadas = detAplicaciones
					.get(encuesta.getClave());
			if (appsAsignadas != null) {
				System.out.println("Se asignaran "
						+ appsAsignadas.values().size() + " aplicaciones");
				Iterator<Aplicacion> iAplicaciones = appsAsignadas.values()
						.iterator();
				while (iAplicaciones.hasNext()) {
					int idAplicacion = iAplicaciones.next().getId();
					cCommit.setParameter(1, Encuesta);
					cCommit.setParameter(2, idAplicacion);
					// #System.out.println("Prepare query " +
					// cCommit.getfinalQuery());
					cCommit.executeQuery();
					cCommit.setQuery(cCommit.getQuery());
				}
			}
		} else {
			System.out.println("No se asignaron aplicaciones para la encuesta");
		}
		// Se asignan los servicios
		cCommit.findQuery("AgregaServicio");
		// #System.out.println("Despues de encontrar el query AgregaServicio" +
		// cCommit.getfinalQuery());
		if (detServicios.containsKey(encuesta.getClave())) {
			Hashtable<Integer, Servicio> servsAsignados = detServicios
					.get(encuesta.getClave());
			if (servsAsignados != null) {
				System.out.println("Se asignaran "
						+ servsAsignados.values().size() + " servicios");
				Iterator<Servicio> iServicios = servsAsignados.values()
						.iterator();
				while (iServicios.hasNext()) {

					int idServicio = iServicios.next().getId();
					cCommit.setParameter(1, Encuesta);
					cCommit.setParameter(2, idServicio);
					// #System.out.println("Prepare query " +
					// cCommit.getfinalQuery());
					cCommit.executeQuery();
					cCommit.setQuery(cCommit.getQuery());
				}
			}
		} else {
			System.out.println("No se asignaron servicios para la encuesta");
		}
		movePage(8);
	}

	public void updateEncuesta() {
		System.out.println("Actualizando la encuesta " + encuesta.getId());
		/* Obtener los entornos que se asignaron por el programa */
		Hashtable<Integer, Entorno> entornosAsignados = entornos
				.getEntornosAsignados();

		String listaEntornos = "";
		Iterator<Entorno> iAsignados = entornosAsignados.values().iterator();
		while (iAsignados.hasNext()) {
			listaEntornos += iAsignados.next().getId() + ",";
		}
		listaEntornos = listaEntornos.substring(0,
				listaEntornos.lastIndexOf(","));
		if (listaEntornos != null || listaEntornos != "") {
			// ProccesListEntornos(idEncuesta, listAsignados);
		}

		/*
		 * if(entornosAsignados.values().size() <
		 * entornosAsignadosBase.values().size()) { eUpdate = new
		 * clsDB("QuitaEntorno"); Iterator<Entorno> iBase =
		 * entornosAsignadosBase.values().iterator(); while(iBase.hasNext()) {
		 * int idEntorno = iBase.next().getId();
		 * System.out.println("Eliminando al entorno " + idEntorno +
		 * " de la encuesta " + encuesta.getId()); eUpdate.setParameter(1,
		 * encuesta.getId()); eUpdate.setParameter(2, idEntorno);
		 * System.out.println("Prepare QuitaEntorno " +
		 * eUpdate.getfinalQuery()); //eUpdate.executeQuery();
		 * eUpdate.setQuery(eUpdate.getQuery()); } } Iterator<Entorno>
		 * iAsignados = entornosAsignados.values().iterator(); eUpdate = new
		 * clsDB("AgregaEntorno"); while(iAsignados.hasNext()) { int idEntorno =
		 * iAsignados.next().getId(); System.out.println("Asignando al entorno "
		 * + idEntorno + " a la encuesta " + encuesta.getId());
		 * if(!entornosAsignadosBase.containsKey(idEntorno)) {
		 * eUpdate.setParameter(1, encuesta.getId()); eUpdate.setParameter(2,
		 * idEntorno); System.out.println("Prepare AgregaEntorno " +
		 * eUpdate.getfinalQuery()); //eUpdate.executeQuery();
		 * eUpdate.setQuery(eUpdate.getQuery()); } }
		 */
		/* Obtener las conductas por entorno del programa */
		// Verifica que el entorno tenga conductas asignadas en el programa
		clsDB eUpdate = new clsDB("AsignaConducta");
		Set<Entry<Integer, Hashtable<Integer, Conducta>>> setEntornos = detConductas
				.entrySet();
		Iterator<Entry<Integer, Hashtable<Integer, Conducta>>> idetConductas = setEntornos
				.iterator();
		/* idetConductas tiene los entornos con sus respetivas conductas */
		while (idetConductas.hasNext()) {
			Entry<Integer, Hashtable<Integer, Conducta>> eConductas = idetConductas
					.next();
			int idEntorno = eConductas.getKey();
			Iterator<Conducta> iConductas = eConductas.getValue().values()
					.iterator();
			while (iConductas.hasNext()) {

			}
		}

		/*
		 * if(detConductas.containsKey(entorno)) { conductasAsignadas =
		 * detConductas.get(entorno); System.out.println("El entorno " + entorno
		 * + " tiene " + conductasAsignadas.size() + " conductas asignadas"); }
		 * else { System.out.println("El entorno " + entorno +
		 * " no tiene conductas."); conductasAsignadas = new Hashtable<Integer,
		 * Conducta>(); }
		 * 
		 * if(id != null && !id.equals("0")) { //La encuesta existe el id es
		 * diferente de 0 conductas.setIdEncuesta(id);
		 * conductas.setIdEntorno(currentEntorno); //Construye las conductas
		 * asignadas para esa encuesta y entorno
		 * conductas.buildConductasAsignadas();
		 * System.out.println("La encuesta: " + id + " existe y tiene: " +
		 * conductas.getTotalConductasAsignadas()); //Las conductas asignadas
		 * son las que se encuentran en la base, solamente se deben de tomar una
		 * vez conductasAsignadas = conductas.getConductasAsignadas(); }
		 * //Comparar las conductas asignadas contra la lista de conductas
		 * asignadas System.out.println("Contador de tokens: " +
		 * tk.countTokens()); if(tk.countTokens() < conductasAsignadas.size() &&
		 * tk.countTokens() != 0) { conductasAsignadas.clear(); } //Obtiene las
		 * conductas disponibles para ese entorno
		 * conductas.buildConductasDisponilbes(id, currentEntorno);
		 * while(tk.hasMoreTokens()) { //Id de la conducta asignada
		 * System.out.println("-Entra en el while"); int num =
		 * Integer.valueOf(tk.nextToken());
		 * if(!conductasAsignadas.containsKey(num)) { con =
		 * conductas.getConductaDisponibleById(num); conductasAsignadas.put(num,
		 * con); } } Iterator<Conducta> iAsignadas =
		 * conductasAsignadas.values().iterator(); while(iAsignadas.hasNext()) {
		 * con = iAsignadas.next();
		 * conductas.getConductasDisponibles().remove(con.getId()); } //Quitar
		 * los entornos que no se asignaron if(conductasAsignadas.size() > 0) {
		 * detConductas.put(entorno, conductasAsignadas); valida = true;
		 * System.out.println("Se asignaron " + conductasAsignadas.size() +
		 * " conductas para el entorno: " + entorno); }
		 */

		/* Obtener las conductas asigandas por entorno desde la base */
	}

	public boolean getEvaluaAplicaciones() {
		return aAplicaciones;
	}

	public void setEvaluaAplicaciones(boolean aAplicaciones) {
		this.aAplicaciones = aAplicaciones;
	}

	public boolean getEvaluaServicios() {
		return aServicios;
	}

	public void setEvaluaServicios(boolean aServicios) {
		this.aServicios = aServicios;
	}

	public boolean getEvaluaGenteProsa() {
		return aGenteProsa;
	}

	public void setEvaluaGenteProsa(boolean aGenteProsa) {
		this.aGenteProsa = aGenteProsa;
	}

	public void setAspectos(String gp, String apps, String serv) {
		if (encuesta != null) {
			System.out.println("ASE Encuesta - " + encuesta.getId());
			lib.clsDB aspectos = new lib.clsDB("EncuestaAspectos");
			aspectos.setParameter(1, encuesta.getId());

			String g = null, a = null, s = null;

			if (gp == null) {
				gp = "off";
			}
			if (apps == null) {
				apps = "off";
			}
			if (serv == null) {
				serv = "off";
			}

			Object[] aAspectos = aspectos.getData().toArray();
			if (aAspectos.length > 0) {
				System.out.println("aA Leng " + aAspectos.length);
				g = ((Object[]) aAspectos[0])[0].toString();
				a = ((Object[]) aAspectos[0])[1].toString();
				s = ((Object[]) aAspectos[0])[2].toString();
				/*
				 * if(gp.equals("off") && g.equals("true")) { gp = "on"; }
				 * 
				 * if(apps.equals("off") && a.equals("true")) { apps = "on"; }
				 * 
				 * if(serv.equals("off") && s.equals("true")) { serv = "on"; }
				 */
				if (g.equals("true")) {
					setEvaluaGenteProsa(true);
				} else {
					setEvaluaGenteProsa(false);
				}

				if (a.equals("true")) {
					setEvaluaAplicaciones(true);
				} else {
					setEvaluaAplicaciones(false);
				}

				if (s.equals("true")) {
					setEvaluaServicios(true);
				} else {
					setEvaluaServicios(false);
				}
			}
		} else {
			System.out.println("La encuesta es null setAspectos");
		}
	}

	public boolean isCreated() {
		return this.created;
	}

	public void nextPage() {
		currentPage++;
		if (currentPage < paginas.size() + 1) {
			if (paginas.get(currentPage).isActiva()) {
				movePage(paginas.get(currentPage));
			} else {
				nextPage();
			}
		} else {
			currentPage--;
		}
	}

	public void beforePage() {
		currentPage--;
		if (currentPage >= 1) {
			if (paginas.get(currentPage).isActiva()) {
				System.out.println("bp " + paginas.get(currentPage).getNombre());
				movePage(paginas.get(currentPage));
			} else {
				beforePage();
			}
		} else {
			currentPage++;
		}
	}

	public void movePage(Pagina pag) {
		try {
			System.out.println("Redirecionando a la pagina: "
					+ paginas.get(currentPage).getNombre());
			response.sendRedirect(pag.getNombre());
			valida = false;
		} catch (IOException e) {
			System.out.println(e.getStackTrace());
		}
	}

	public void movePage(int numpage) {
		currentPage = numpage;
		movePage(paginas.get(currentPage));
	}

	public String getIdEncuesta() {
		return idEncuesta;
	}

	public void setIdEncuesta(String idEncuesta) {
		System.out.println("metiendo:"+idEncuesta+"--");
		this.idEncuesta = idEncuesta;
	}
}
