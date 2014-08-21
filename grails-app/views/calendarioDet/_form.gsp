<%@page expressionCodec="none"%>

<g:form action="update" id="${calendarioDetInstance.id}" class="form-horizontal" >
  <div class="modal-body">

    <f:with bean="${calendarioDetInstance}">
      <f:field property='mes' input-class="form-control">
        <g:select id="mesField" class="form-control"  
                        name="mes" 
                        value="${calendarioDetInstance.mes}"
                        noSelection="['':'-Seleccione un mes-']"
                        from="${com.luxsoft.sw4.Mes.getMeses().collect{it.nombre}}"
                        autocomplete="off"
                        />
      </f:field>
      <f:field property='bimestre' input-class="form-control">
        <g:select id="mesField" class="form-control"  
                        name="bimestre" 
                        value="${calendarioDetInstance.bimestre}"
                        noSelection="['':'-Seleccione un bimestre-']"
                        from="${(1..6)}"
                        autocomplete="off"
                        />
      </f:field>
      <f:field property='folio' input-class="form-control"/>
      <f:field property='inicio' input-class="form-control"/>
      <f:field property='fin' input-class="form-control"/>
      <f:field property='fechaDePago' input-class="form-control" label="Pago"/>
      <fieldset>
          <legend><small>Asistencia:</small></legend>
          <f:field property='asistencia.fechaInicial' input-class="form-control"/>
          <f:field property='asistencia.fechaFinal' input-class="form-control"/>
      </fieldset>
    </f:with>
  

   %{-- <div class="form-group">
     <label for="inicio" class="col-sm-2 control-label">Folio</label>
     <div class="col-sm-10">
      <g:field type="text" class="form-control " id="folio" name="folio" value="${calendarioDetInstance.folio}"/>
    </div>
  </div>


 <div class="form-group">
  <label for="inicio" class="col-sm-2 control-label">Inicio</label>
  <div class="col-sm-10">
   <g:field type="text" class="form-control dateField" id="inicio" name="inicio" 
   value="${g.formatDate(date:calendarioDetInstance.inicio,format:'dd/MM/yyyy')}"
   />
 </div>
</div>

<div class="form-group">
  <label for="fin" class="col-sm-2 control-label">Fin</label>
  <div class="col-sm-10">
   <g:field type="text" class="form-control dateField" id="fin" name="fin"
   value="${g.formatDate(date:calendarioDetInstance.fin,format:'dd/MM/yyyy')}"
   />
 </div>
</div>

<div class="form-group">
  <label for="pago" class="col-sm-2 control-label">Pago</label>
  <div class="col-sm-10">
   <g:field type="text" class="form-control dateField" id="pago" name="fechaDePago"
   value="${g.formatDate(date:calendarioDetInstance.fechaDePago,format:'dd/MM/yyyy')}"
   />
 </div>
</div>

<fieldset>
  <legend><small>Corte para control de asistencia:</small></legend>
  <div class="form-group">
    <label for="asistenciaInicial" class="col-sm-2 control-label">Inicial</label>
    <div class="col-sm-10">
     <g:field type="text" class="form-control dateField" id="asistenciaInicial" name="asistencia.fechaInicial"
     value="${g.formatDate(date:calendarioDetInstance.asistencia.fechaInicial,format:'dd/MM/yyyy')}"
     />
   </div>
 </div>

 <div class="form-group">
  <label for="asistenciaFinal" class="col-sm-2 control-label">Final</label>
  <div class="col-sm-10">
   <g:field type="text" class="form-control dateField" id="asistenciaFinal" name="asistencia.fechaFinal"
   value="${g.formatDate(date:calendarioDetInstance.asistencia.fechaFinal,format:'dd/MM/yyyy')}"
   />
 </div> 
</div>

</fieldset>
--}%
 
</div>
<div class="modal-footer">
  <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
  <g:submitButton class="btn btn-primary" name="update" value="Salvar"/>
</div>
</g:form>

